package builder;

import enums.Level;
import solver.SudokuSolverBase;

import java.util.Random;

public class Sudoku {
    private final int[][] board;
    private final int size;
    private final int sqrtSize;
    private final Random random = new Random();
    private final Level level;

    private Sudoku(Builder builder) {
        this.size = builder.size;
        this.sqrtSize = (int) Math.sqrt(size);
        this.board = new int[size][size];
        this.level = builder.level;
    }

    public void generate() {
        boolean generated = false;
        while (!generated) {
            clearBoard();
            fillDiagonal();
            if (fillRemaining(0, sqrtSize)) {
                removeDigits();
                if (isUniqueSolution()) {
                    generated = true;
                }
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
        int cellsToRemove = switch (level) {
            case EASY -> totalCells / 4;
            case MEDIUM -> totalCells / 2;
            case HARD -> (int) (totalCells * 0.75);
        };

        while (cellsToRemove != 0) {
            int i = random.nextInt(size);
            int j = random.nextInt(size);
            if (board[i][j] != 0) {
                board[i][j] = 0;
                cellsToRemove--;
            }
        }
    }

    private boolean isUniqueSolution() {
        int[][] copy = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, size);
        }
        return solve(copy, 0, 0) == 1;
    }

    private int solve(int[][] grid, int row, int col) {
        if (row == size) {
            row = 0;
            if (++col == size) {
                return 1;
            }
        }
        if (grid[row][col] != 0) {
            return solve(grid, row + 1, col);
        }

        int solutions = 0;
        for (int num = 1; num <= size; num++) {
            if (isSafe(grid, row, col, num)) {
                grid[row][col] = num;
                solutions += solve(grid, row + 1, col);
                grid[row][col] = 0;
                if (solutions > 1) {
                    return solutions;
                }
            }
        }
        return solutions;
    }

    private boolean isSafe(int[][] grid, int row, int col, int num) {
        for (int x = 0; x < size; x++) {
            if (grid[row][x] == num || grid[x][col] == num) {
                return false;
            }
        }
        int startRow = row - row % sqrtSize, startCol = col - col % sqrtSize;
        for (int r = 0; r < sqrtSize; r++) {
            for (int c = 0; c < sqrtSize; c++) {
                if (grid[startRow + r][startCol + c] == num) {
                    return false;
                }
            }
        }
        return true;
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
        private Level level = Level.EASY;

        public Builder size(int size) {
            if (Math.sqrt(size) % 1 != 0) {
                throw new IllegalArgumentException("Size must be a perfect square.");
            }
            this.size = size;
            return this;
        }

        public Builder level(Level level) {
            this.level = level;
            return this;
        }

        public Sudoku build() {
            return new Sudoku(this);
        }
    }


}
