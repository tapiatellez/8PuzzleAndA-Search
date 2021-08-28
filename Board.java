/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private int[][] gameBoard;

    //  create a board from n-by-b array of tiles,
    //  where tiles[row][col] = tile at row (row, col)
    public Board(int[][] tiles) {
        int boardSize = tiles.length;
        gameBoard = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                gameBoard[i][j] = tiles[i][j];
    }

    // string representation of this board
    public String toString() {
        int sizeOfBoard = gameBoard.length;
        StringBuilder s = new StringBuilder();
        s.append(sizeOfBoard + "\n");
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {
                s.append(String.format("%2d ", gameBoard[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    //  board dimension n
    public int dimension() {
        return gameBoard.length;
    }

    //  Number of tiles out of place
    public int hamming() {
        int hamming = 0;
        int sizeOfBoard = dimension();
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {
                if ((sizeOfBoard * i + j + 1) != gameBoard[i][j] && gameBoard[i][j] != 0) hamming++;
            }
        }
        return hamming;
    }

    //  sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        int boardValue = 0;
        int goalR;
        int goalC;
        int sizeOfBoard = dimension();
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {
                boardValue = gameBoard[i][j];
                if (boardValue != (sizeOfBoard * i + j + 1) && boardValue != 0) {
                    boardValue = boardValue - 1;
                    goalR = boardValue / sizeOfBoard;
                    goalC = boardValue % sizeOfBoard;
                    distance = distance + Math.abs(i - goalR) + Math.abs(j - goalC);
                }
            }
        }
        return distance;
    }

    //  is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        return Arrays.deepEquals(this.gameBoard, that.gameBoard);
    }

    //  all neighboring boards
    public Iterable<Board> neighbors() {
        int sizeOfBoard = this.dimension();
        ArrayList<Board> neighbors = new ArrayList<>();
        int row = 0;
        int col = 0;
        outerloop:
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {
                if (this.gameBoard[i][j] == 0) {
                    row = i;
                    col = j;
                    break outerloop;
                }
            }
        }
        // check up
        if (row != 0) {
            Board upBoard = new Board(this.gameBoard);
            upBoard.gameBoard[row - 1][col] = 0;
            upBoard.gameBoard[row][col] = this.gameBoard[row - 1][col];
            neighbors.add(upBoard);
        }
        // check down
        if (row != sizeOfBoard - 1) {
            Board downBoard = new Board(this.gameBoard);
            downBoard.gameBoard[row + 1][col] = 0;
            downBoard.gameBoard[row][col] = this.gameBoard[row + 1][col];
            neighbors.add(downBoard);
        }
        if (col != sizeOfBoard - 1) {
            Board rightBoard = new Board(this.gameBoard);
            rightBoard.gameBoard[row][col + 1] = 0;
            rightBoard.gameBoard[row][col] = this.gameBoard[row][col + 1];
            neighbors.add(rightBoard);
        }
        if (col != 0) {
            Board leftBoard = new Board(this.gameBoard);
            leftBoard.gameBoard[row][col - 1] = 0;
            leftBoard.gameBoard[row][col] = this.gameBoard[row][col - 1];
            neighbors.add(leftBoard);
        }
        return neighbors;
    }

    //  a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        //  Choose two tiles different to zero
        int firstR = 0, firstC = 0, secondR = 0, secondC = 0;
        outerloop:
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.gameBoard[i][j] != 0) {
                    firstR = i;
                    firstC = j;
                    break outerloop;
                }
            }
        }
        outerloop:
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if ((this.gameBoard[i][j] != 0) &&
                        (this.gameBoard[firstR][firstC] != this.gameBoard[i][j])) {
                    secondR = i;
                    secondC = j;
                    break outerloop;
                }
            }
        }
        // Create a new Board object
        Board twin = new Board(this.gameBoard);
        //  Swap the tiles
        twin.gameBoard[firstR][firstC] = this.gameBoard[secondR][secondC];
        twin.gameBoard[secondR][secondC] = this.gameBoard[firstR][firstC];
        return twin;

    }
/*    public Board twin() {
        // Choose two tiles different to zero
        int firstRow = StdRandom.uniform(this.dimension());
        int firstCol = StdRandom.uniform(this.dimension());
        int secondRow = StdRandom.uniform(this.dimension());
        int secondCol = StdRandom.uniform(this.dimension());
        while ((this.gameBoard[firstRow][firstCol] == 0) || (this.gameBoard[secondRow][secondCol]
                == 0) || ((firstRow == secondRow) && (firstCol == secondCol))) {
            firstRow = StdRandom.uniform(this.dimension());
            firstCol = StdRandom.uniform(this.dimension());
            secondRow = StdRandom.uniform(this.dimension());
            secondCol = StdRandom.uniform(this.dimension());
        }
        // Create a new Board object
        Board twin = new Board(this.gameBoard);
        //  Swap the tiles
        twin.gameBoard[firstRow][firstCol] = this.gameBoard[secondRow][secondCol];
        twin.gameBoard[secondRow][secondCol] = this.gameBoard[firstRow][firstCol];
        return twin;
    }*/

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        int[][] tilesNew = new int[n][n];
        int counter = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
                tilesNew[i][j] = counter;
                counter++;
            }
        }
        //  Checking the constructor
        Board initial = new Board(tiles);
        Board continued = new Board(tilesNew);
        //  Checking the toString function
        String strInitial = initial.toString();
        StdOut.print(strInitial);
        //  Checking the hamming function
        StdOut.println("The hamming value: " + initial.hamming());
        //  Checking the manhattan function
        StdOut.println("The manhattan distance is: " + initial.manhattan());
        //  Checking equals function
        StdOut.println("The board is the same as itself? " + initial.equals(initial));
        StdOut.println("The board is the same as the ordered one? " + initial.equals(continued));
        // Checking the neighbors function
        Iterable<Board> neighbors = initial.neighbors();
        StdOut.println("Printing the neighbors: ");
        for (Board b : neighbors) {
            StdOut.println(b);
        }
        //  Checking the twin function
        StdOut.println("Printing the twin: ");
        StdOut.println(initial.twin().toString());
        StdOut.println("Printing and calling the twin again: ");
        StdOut.println(initial.twin().toString());
    }
}
