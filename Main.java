import builder.Sudoku;
import enums.Level;
import gui.SudokuGUI;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku.Builder()
                .size(9)
                .build();
        sudoku.generate();
        sudoku.printBoard();

        SwingUtilities.invokeLater(() -> new SudokuGUI(sudoku.getBoard(), sudoku.getSize()));
    }
}