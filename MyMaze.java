// Names: Vincent Hoang
// x500s: hoang317

import java.util.Random;
import java.util.Scanner;

public class MyMaze{
    Cell[][] maze;
    int startRow;
    int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        this.maze = new Cell[rows][cols];

        //goes through the 2D maze array and creates a new
        //cell object for each index
        for(int i = 0; i < this.maze.length; i++) {
            for(int j = 0; j < this.maze[0].length; j++) {
                this.maze[i][j] = new Cell();
            }
        }

        this.startRow = startRow;
        this.endRow = endRow;
    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze() {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        //asks the user to enter a row and col
        System.out.println("Enter a row and col within the range of 5-20 inclusive. (format:[row] [col])");
        int userRow = scanner.nextInt();
        int userCol = scanner.nextInt();

        //handles the case when sizes are outside the bounds
        while((userRow < 5 || userRow > 20) || (userCol < 5 || userCol > 20)) {
            System.out.println("Invalid input. Enter a row and col within the range of 5-20 inclusive. (format:[row] [col])");
            userRow = scanner.nextInt();
            userCol = scanner.nextInt();
        }

        //randomly chooses a start and end row
        int startRow = random.nextInt(userRow);
        int endRow = random.nextInt(userRow);

        //create a new MyMaze object based on the user input
        MyMaze newMaze = new MyMaze(userRow, userCol, startRow, endRow);

        //removes the right boundary of the end position
        newMaze.maze[endRow][userCol - 1].setRight(false);

        //creates a stack, add the starting position to it,
        //and marks the cell at the start position as visited
        Stack1Gen<int[]> stack = new Stack1Gen();
        stack.push(new int[] {startRow, 0});
        newMaze.maze[startRow][0].setVisited(true);

        //loops through the stack to generate the maze
        //until it is empty
        while(!stack.isEmpty()) {
            //gets the top element of the stack
            int[] topElement = stack.top();

            //linked list used to store all possible
            //neighbors with a header
            NGen<int[]> neighbors = new NGen(null, null);

            //pointer to traverse through the list
            //and size to keep track of number of elements
            NGen<int[]> ptr = neighbors;
            int size = 0;

            //checks to see if neighbor above can be visited
            if(topElement[0] - 1 >= 0 && !newMaze.maze[topElement[0] - 1][topElement[1]].getVisited()) {
                //adds the neighbor to the list and move ptr forward to
                //the next node and increments size
                ptr.setNext(new NGen(new int[] {topElement[0] - 1, topElement[1]}, null));
                ptr = ptr.getNext();
                size += 1;
            }
            //checks to see if neighbor below can be visited
            if(topElement[0] + 1 < newMaze.maze.length && !newMaze.maze[topElement[0] + 1][topElement[1]].getVisited()) {
                //adds the neighbor to the list and move ptr forward to
                //the next node and increments size
                ptr.setNext(new NGen(new int[] {topElement[0] + 1, topElement[1]}, null));
                ptr = ptr.getNext();
                size += 1;
            }
            //checks to see if neighbor on the left can be visited
            if(topElement[1] - 1 >= 0 && !newMaze.maze[topElement[0]][topElement[1] - 1].getVisited()) {
                //adds the neighbor to the list and move ptr forward to
                //the next node and increments size
                ptr.setNext(new NGen(new int[] {topElement[0], topElement[1] - 1}, null));
                ptr = ptr.getNext();
                size += 1;
            }
            //checks to see if neighbor on the right can be visited
            if(topElement[1] + 1 < newMaze.maze[0].length && !newMaze.maze[topElement[0]][topElement[1] + 1].getVisited()) {
                //adds the neighbor to the list and have ptr point back to
                //the first node and increments size
                ptr.setNext(new NGen(new int[] {topElement[0], topElement[1] + 1}, null));
                size += 1;
            }

            //set ptr to point to the first element
            ptr = neighbors.getNext();

            //case when there are neighbors that can be visited
            if(size != 0) {
                //randomly choose one of the neighbors from the list
                for(int i = 0; i < random.nextInt(size); i++) {
                    ptr = ptr.getNext();
                }

                //adds the neighbor index to the stack and
                //mark the neighbor as visited
                stack.push(ptr.getData());
                newMaze.maze[ptr.getData()[0]][ptr.getData()[1]].setVisited(true);

                //case when the neighbor is above the current position
                if(topElement[0] - 1 == ptr.getData()[0]) {
                    //removes the bottom border of the neighbor's position
                    newMaze.maze[ptr.getData()[0]][ptr.getData()[1]].setBottom(false);
                }
                //case when the neighbor is below the current position
                else if(topElement[0] + 1 == ptr.getData()[0]) {
                    //removes the bottom border of the current position
                    newMaze.maze[topElement[0]][topElement[1]].setBottom(false);
                }
                //case when the neighbor is to the right of the current position
                else if(topElement[1] + 1 == ptr.getData()[1]) {
                    //removes the right border of the current position
                    newMaze.maze[topElement[0]][topElement[1]].setRight(false);
                }
                //case when the neighbor is to the left of the current position
                else {
                    //removes the right border of the neighbor's position
                    newMaze.maze[ptr.getData()[0]][ptr.getData()[1]].setRight(false);
                }
            }
            //case when there are no un-visited neighbors
            else {
                //removes the element from stack
                stack.pop();
            }
        }

        //goes through the maze and sets all cell
        //back to false
        for(int i = 0; i < newMaze.maze.length; i++) {
            for(int j = 0; j < newMaze.maze[0].length; j++) {
                newMaze.maze[i][j].setVisited(false);
            }
        }

        return newMaze;
    }

    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze() {
        System.out.print("|");;

        //creates the top of the maze
        for(int i = 0; i < this.maze[0].length; i++) {
            System.out.print("---|");
        }

        //moves down to the next row of the maze
        System.out.println();

        //goes through the maze and prints the rest of the maze
        for(int j = 0; j < this.maze.length; j++) {
            //case when it is the starting
            //position of the maze
            if(j == startRow) {
                System.out.print(" ");
            }
            //case when it is not the starting
            //position of the maze
            else {
                System.out.print("|");
            }

            //loop to create the right border of the maze
            for(int k = 0; k < this.maze[0].length; k++) {
                //checks to see if cell doesn't have a right border
                //and if it has not been visited or if we have reached
                //the end position
                if(!this.maze[j][k].getRight() && !this.maze[j][k].getVisited()) {
                    System.out.print("    ");
                }
                //checks to see if cell doesn't have a right border
                //and if it has been visited or if we have reached
                //the end position
                else if(!this.maze[j][k].getRight() && this.maze[j][k].getVisited()) {
                    System.out.print(" *  ");
                }
                //checks to see if cell has a right border
                //and if it has not been visited
                else if(this.maze[j][k].getRight() && !this.maze[j][k].getVisited()) {
                    System.out.print("   |");
                }
                //checks to see if cell has a right border
                //and if it has been visited
                else if(this.maze[j][k].getRight() && this.maze[j][k].getVisited()) {
                    System.out.print(" * |");
                }
            }

            //moves down to the next line
            System.out.print("\n" + "|");

            //loop to create the bottom border of the maze
            for(int l = 0; l < this.maze[0].length; l++) {
                //checks to see if cell doesn't have a bottom border
                if(!this.maze[j][l].getBottom()) {
                    System.out.print("   |");
                }
                //when the cell has a bottom border
                else {
                    System.out.print("---|");
                }
            }

            //moves down to the next row of the maze
            System.out.println();
        }
    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        //creates a queue and enqueue the start
        //position
        Q1Gen<int[]> queue = new Q1Gen();
        queue.add(new int[] {startRow, 0});

        //goes through the maze to solve it
        while(queue.length() != 0) {
            //gets the front index of the queue and
            //sets its cell as visited
            int[] front = queue.remove();
            this.maze[front[0]][front[1]].setVisited(true);

            //breaks from the loop and prints the maze
            //if the end position has been reached
            if(front[0] == endRow && front[1] == this.maze[0].length - 1) {
                this.printMaze();
                return;
            }

            //case when the top neighbor is reachable and are un-visited
            if(front[0] - 1 >= 0 && !this.maze[front[0] - 1][front[1]].getVisited() && !this.maze[front[0] - 1][front[1]].getBottom()) {
                //enqueue the neighbor
                queue.add(new int[] {front[0] - 1, front[1]});
            }
            //case when the bottom neighbor is reachable and are un-visited
            if(front[0] + 1 < this.maze.length && !this.maze[front[0] + 1][front[1]].getVisited() && !this.maze[front[0]][front[1]].getBottom()) {
                //enqueue the neighbor
                queue.add(new int[] {front[0] + 1, front[1]});
            }
            //case when the left neighbor is reachable and are un-visited
            if(front[1] - 1 >= 0 && !this.maze[front[0]][front[1] - 1].getVisited() && !this.maze[front[0]][front[1] - 1].getRight()) {
                //enqueue the neighbor
                queue.add(new int[] {front[0], front[1] - 1});
            }
            //case when the right neighbor is reachable and are un-visited
            if(front[1] + 1 < this.maze[0].length && !this.maze[front[0]][front[1] + 1].getVisited() && !this.maze[front[0]][front[1]].getRight()) {
                //enqueue the neighbor
                queue.add(new int[] {front[0], front[1] + 1});
            }
        }
    }

    public static void main(String[] args){
        /*Make and solve maze */
        MyMaze genMaze = makeMaze();
        System.out.println("Generated Maze:");
        genMaze.printMaze();
        System.out.println("\n" + "Solved Maze:");
        genMaze.solveMaze();
    }
}
