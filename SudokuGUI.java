
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuGUI extends JFrame {
    private int[][] board;
    private int size;
    private JPanel sudokuPanel;
    private boolean isSolved = false;

    public SudokuGUI(int[][] board,int size) {
        this.board = board;
        this.size = size;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        sudokuPanel = new JPanel(new GridLayout(size, size));
        populateSudokuPanel();
        add(sudokuPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

        private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }
    

}