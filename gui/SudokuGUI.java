package gui;

import builder.Sudoku;
import solver.CSPSolver;
import solver.GeneticSolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuGUI extends JFrame {
    private int[][] board;
    private int size;
    private JPanel sudokuPanel;
    private boolean isSolved = false;

    public SudokuGUI(int[][] board, int size) {
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

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton cspSolverButton = createButton("CSP Solver", e -> solveWithCSP());
        JButton geneticSolverButton = createButton("Genetic Solver", e -> solveWithGenetic());
        JButton restartButton = createButton("Restart", e -> restart());

        buttonPanel.add(cspSolverButton);
        buttonPanel.add(geneticSolverButton);
        buttonPanel.add(restartButton);

        return buttonPanel;
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    private void populateSudokuPanel() {
        sudokuPanel.removeAll();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                JTextField textField = new JTextField(1);
                textField.setHorizontalAlignment(JTextField.CENTER);
                if (board[i][j] != 0) {
                    textField.setText(String.valueOf(board[i][j]));
                    textField.setEditable(false);
                }
                sudokuPanel.add(textField);
            }
        }
        sudokuPanel.revalidate();
        sudokuPanel.repaint();
    }



    private void handleSolution(int[][] solvedBoard) {
        if (solvedBoard != null) {
            isSolved = true;
            board = solvedBoard;
            populateSudokuPanel();
        } else {
            JOptionPane.showMessageDialog(this, "No solution found.");
        }
    }

    private void restart() {
        isSolved = false;
        Sudoku sudoku = new Sudoku.Builder()
                .size(size).build();
        sudoku.generate();
        sudoku.printBoard();
        board = sudoku.getBoard();
        populateSudokuPanel();
    }
}