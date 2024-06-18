package builder;

import enums.Level;


import java.util.Random;

public class Sudoku {
    private final int[][] board;
    private final int size;
    private final int sqrtSize;
    private final Random random = new Random();


    private Sudoku(Builder builder) {
        this.size = builder.size;
        this.sqrtSize = (int) Math.sqrt(size);
        this.board = new int[size][size];

    }

    public void generate() {
        boolean generated = false;
        while (!generated) {
            clearBoard();
            fillDiagonal();
            if (fillRemaining(0, sqrtSize)) {
                removeDigits();

                    generated = true;
                
            }
        }
    }

    private void clearBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
            }
        }
    }

    private void fillDiagonal() {
        for (int i = 0; i < size; i += sqrtSize) {
            fillBox(i, i);
            printBoard();
            System.out.print("\n\n");
        }
    }

    private void fillBox(int row, int col) {
        boolean[] used = new boolean[size + 1];
        for (int i = 0; i < sqrtSize; i++) {
            for (int j = 0; j < sqrtSize; j++) {
                int num;
                do {
                    num = random.nextInt(size) + 1;
                } while (used[num]);
                used[num] = true;
                board[row + i][col + j] = num;
            }
        }
    }

    private boolean fillRemaining(int i, int j) {
        if (j >= size && i < size - 1) {
            i++;
            j = 0;
        }
        if (i >= size && j >= size) {
            return true;
        }
        if (i < sqrtSize) {
            if (j < sqrtSize) {
                j = sqrtSize;
            }
        } else if (i < size - sqrtSize) {
            if (j == (i / sqrtSize) * sqrtSize) {
                j += sqrtSize;
            }
        } else {
            if (j == size - sqrtSize) {
                i++;
                j = 0;
                if (i >= size) {
                    return true;
                }
            }
        }
        for (int num = 1; num <= size; num++) { 
            if (isSafe(i, j, num)) {
                board[i][j] = num;
                if (fillRemaining(i, j + 1)) {
                    return true;
                }
                board[i][j] = 0;
            }
        }
        return false;
    }

    private boolean isSafe(int i, int j, int num) {
        return !usedInRow(i, num) && !usedInCol(j, num) && !usedInBox(i - i % sqrtSize, j - j % sqrtSize, num);
    }

    private boolean usedInRow(int i, int num) {
        for (int j = 0; j < size; j++) {
            if (board[i][j] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean usedInCol(int j, int num) {
        for (int i = 0; i < size; i++) {
            if (board[i][j] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean usedInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < sqrtSize; i++) {
            for (int j = 0; j < sqrtSize; j++) {
                if (board[rowStart + i][colStart + j] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    private void removeDigits() {
        int totalCells = size * size;
        int cellsToRemove = totalCells / 4;

        while (cellsToRemove != 0) {
            int i = random.nextInt(size);
            int j = random.nextInt(size);
            if (board[i][j] != 0) {
                board[i][j] = 0;
                cellsToRemove--;
            }
        }
    }


    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }


    public int[][] getBoard() {
        return board;
    }

    public int getSize() {
        return size;
    }

    public static class Builder {
        private int size = 9;


        public Builder size(int size) {
            if (Math.sqrt(size) % 1 != 0) {
                throw new IllegalArgumentException("Size should be a perfect square.");
            }
            this.size = size;
            return this;
        }

        public Sudoku build() {
            return new Sudoku(this);
        }
    }


}
