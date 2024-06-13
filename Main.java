import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Sudoku sudoku = new Sudoku.Builder()
                .size(9)
                .build();
        sudoku.generate();
        sudoku.printBoard();
    }
}