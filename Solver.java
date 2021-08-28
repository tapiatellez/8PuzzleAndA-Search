/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
/* *****************************************************************************
 *  Name:   José Medardo Tapia Téllez
 *  Date:   03 / 04 /2021
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private int moves = 0;
    private SearchNode currentOriginal;

    //  find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        //  Declare the Minimal Priority Queue
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();
        //  Declare an initialize the root nodes
        SearchNode pqFirst = new SearchNode(initial, moves, null);
        SearchNode pqTwinFirst = new SearchNode(initial.twin(), moves, null);
        //  Insert the root nodes in the MPQ
        pq.insert(pqFirst);
        pqTwin.insert(pqTwinFirst);

        SearchNode currentTwin;
        while (true) {
            currentOriginal = pq.delMin();
            moves = getMoves(currentOriginal);
            if (currentOriginal.board.isGoal()) return;
            currentTwin = pqTwin.delMin();
            //  StdOut.println("Current Original: ");
            //  StdOut.println(currentOriginal.board.toString());
            if (currentTwin.board.isGoal()) return;

            Iterable<Board> neighbors = currentOriginal.board.neighbors();
            Iterable<Board> neighborsTwin = currentTwin.board.neighbors();
            //  StdOut.println("Current Original Neighbors: ");
            for (Board po : neighbors) {
                if (currentOriginal.previousNode != null && po
                        .equals(currentOriginal.previousNode.board))
                    continue;
                pq.insert(new SearchNode(po, moves, currentOriginal));
                //  StdOut.println(o);
            }
            for (Board t : neighborsTwin) {
                if (currentTwin.previousNode != null && t.equals(currentTwin.previousNode.board))
                    continue;
                pqTwin.insert(new SearchNode(t, moves, currentTwin));
            }
        }
    }

    //  is the initial board solvable? (see below)
    public boolean isSolvable() {
        return currentOriginal.board.isGoal();
    }

    //  min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return this.moves;
        else return -1;
    }

    // //  sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        else {
            //  Reconstruct solution based
            Stack<Board> solution = new Stack<>();
            SearchNode toAdd = currentOriginal;
            while (toAdd != null) {
                solution.push(toAdd.board);
                toAdd = toAdd.previousNode;
            }
            return solution;
        }
    }

    // SearchNode Class
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode previousNode;
        private int manhattan = 0;

        public SearchNode(Board board, int moves, SearchNode node) {
            this.board = board;
            this.moves = moves;
            this.previousNode = node;
            this.manhattan = board.manhattan();
        }

        public SearchNode getPrevious() {
            return previousNode;
        }

        public int compareTo(SearchNode that) {
            if ((this.moves + this.manhattan) < (that.moves + that.manhattan))
                return -1;
            if ((this.moves + this.manhattan) > (that.moves + that.manhattan))
                return +1;
            return 0;
        }
    }

    //  Private methods
    private int getMoves(SearchNode co) {
        int counter = 0;
        SearchNode toAdd = co;
        while (toAdd.previousNode != null) {
            counter++;
            toAdd = toAdd.previousNode;
        }
        return counter;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

