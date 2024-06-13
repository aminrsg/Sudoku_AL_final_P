import javax.swing.*;
import gui.SudokuGUI;
import builder.Sudoku;
import enums.level;

public class Main {
    public static void main(String[] args) {
        
        Sudoku sudoku = new Sudoku.Builder()
                .size(4)
                .build();
        sudoku.generate();
        sudoku.printBoard();

        SwingUtilities.invokeLater(() -> new SudokuGUI(sudoku.getBoard(), sudoku.getSize()));
    }
}