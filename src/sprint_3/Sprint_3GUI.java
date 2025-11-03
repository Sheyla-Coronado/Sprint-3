package sprint_3;

import javax.swing.*;
import java.awt.*;


public class Sprint_3GUI extends JFrame {
    private SOSGame gameLogic;
    private JPanel setupPanel;
    private JPanel gamePanel;
    private JButton[][] boardButtons;
    private JLabel currentTurnLabel;
    private JComboBox<Integer> boardSizeCombo;
    private JComboBox<String> gameModeCombo;
    private JButton startGameButton;
    private JLabel blueScoreLabel;
    private JLabel redScoreLabel;

    private char selectedLetter = ' ';

    public Sprint_3GUI() {
        setTitle("SOS Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        showSetupScreen();
    }

    private void showSetupScreen() {
        setupPanel = new JPanel();
        setupPanel.setLayout(new GridBagLayout());
        setupPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("SOS Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        setupPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        JLabel boardSizeLabel = new JLabel("Choose Board Size:");
        boardSizeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        setupPanel.add(boardSizeLabel, gbc);

        Integer[] sizes = {3, 4, 5, 6, 7, 8};
        boardSizeCombo = new JComboBox<>(sizes);
        boardSizeCombo.setSelectedIndex(3); 
        gbc.gridx = 1;
        gbc.gridy = 1;
        setupPanel.add(boardSizeCombo, gbc);

        JLabel gameModeLabel = new JLabel("Choose Game Mode:");
        gameModeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        setupPanel.add(gameModeLabel, gbc);

        String[] modes = {"Simple", "General"};
        gameModeCombo = new JComboBox<>(modes);
        gbc.gridx = 1;
        gbc.gridy = 2;
        setupPanel.add(gameModeCombo, gbc);

        startGameButton = new JButton("Start Game");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 14));
        startGameButton.setPreferredSize(new Dimension(150, 40));
        startGameButton.addActionListener(e -> {
            int boardSize = (Integer) boardSizeCombo.getSelectedItem();
            String gameMode = ((String) gameModeCombo.getSelectedItem()).toLowerCase();
            startGame(boardSize, gameMode);
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        setupPanel.add(startGameButton, gbc);

        setContentPane(setupPanel);
        setVisible(true);
    }

    private void startGame(int boardSize, String gameMode) {
        SOSGame.Player player1 = new SOSGame.HumanPlayer("Blue");
        SOSGame.Player player2 = new SOSGame.HumanPlayer("Red");

        gameLogic = new SOSGame(boardSize, gameMode, player1, player2);
        showGameScreen();
    }

    private void showGameScreen() {
        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout(10, 10));
        gamePanel.setBackground(new Color(240, 240, 240));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 240));

        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        topLeftPanel.setBackground(new Color(240, 240, 240));
        JLabel sosLabel = new JLabel("SOS");
        sosLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topLeftPanel.add(sosLabel);

        String gameMode = gameLogic.getGameMode();
        String modeText = gameMode.equals("simple") ? "Simple game" : "General game";
        JLabel modeLabel = new JLabel("● " + modeText);
        modeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        topLeftPanel.add(modeLabel);
        topPanel.add(topLeftPanel, BorderLayout.WEST);

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topRightPanel.setBackground(new Color(240, 240, 240));
        JLabel boardSizeLabel = new JLabel("Board size");
        boardSizeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topRightPanel.add(boardSizeLabel);
        JTextField boardSizeField = new JTextField(String.valueOf(gameLogic.getBoardSize()), 3);
        boardSizeField.setEditable(false);
        boardSizeField.setFont(new Font("Arial", Font.BOLD, 16));
        topRightPanel.add(boardSizeField);
        topPanel.add(topRightPanel, BorderLayout.EAST);

        gamePanel.add(topPanel, BorderLayout.NORTH);

        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(new Color(240, 240, 240));

        // Blue player
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(240, 240, 240));
        leftPanel.setPreferredSize(new Dimension(100, 0));

        JLabel bluePlayerLabel = new JLabel("Blue player");
        bluePlayerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        leftPanel.add(bluePlayerLabel);
        leftPanel.add(Box.createVerticalStrut(10));

        JRadioButton blueSButton = new JRadioButton("S");
        blueSButton.setForeground(Color.BLUE);
        blueSButton.setFont(new Font("Arial", Font.BOLD, 12));
        JRadioButton blueOButton = new JRadioButton("O");
        blueOButton.setForeground(Color.BLUE);
        blueOButton.setFont(new Font("Arial", Font.BOLD, 12));

        ButtonGroup blueGroup = new ButtonGroup();
        blueGroup.add(blueSButton);
        blueGroup.add(blueOButton);

        leftPanel.add(blueSButton);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(blueOButton);
        leftPanel.add(Box.createVerticalStrut(20));

        blueScoreLabel = new JLabel("Score: 0");
        blueScoreLabel.setFont(new Font("Arial", Font.BOLD, 12));
        if (!gameMode.equals("simple")) {
            leftPanel.add(blueScoreLabel);
        }

        blueSButton.addActionListener(e -> {
            if (gameLogic.getCurrentPlayer().getName().equals("Blue")) {
                selectLetter('S');
            } else {
                JOptionPane.showMessageDialog(this, "It's Red player's turn!");
                blueGroup.clearSelection();
            }
        });

        blueOButton.addActionListener(e -> {
            if (gameLogic.getCurrentPlayer().getName().equals("Blue")) {
                selectLetter('O');
            } else {
                JOptionPane.showMessageDialog(this, "It's Red player's turn!");
                blueGroup.clearSelection();
            }
        });

        contentPanel.add(leftPanel, BorderLayout.WEST);

        // board
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(240, 240, 240));

        JPanel boardPanel = new JPanel();
        int boardSize = gameLogic.getBoardSize();
        boardPanel.setLayout(new GridLayout(boardSize, boardSize, 3, 3));
        boardPanel.setBackground(new Color(200, 200, 200));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        boardButtons = new JButton[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 24));
                btn.setBackground(Color.WHITE);
                btn.setFocusPainted(false);
                final int row = i;
                final int col = j;
                btn.addActionListener(e -> handleCellClick(row, col));
                boardButtons[i][j] = btn;
                boardPanel.add(btn);
            }
        }

        centerPanel.add(boardPanel);
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // Red player
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(240, 240, 240));
        rightPanel.setPreferredSize(new Dimension(100, 0));

        JLabel redPlayerLabel = new JLabel("Red player");
        redPlayerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(redPlayerLabel);
        rightPanel.add(Box.createVerticalStrut(10));

        JRadioButton redSButton = new JRadioButton("S");
        redSButton.setForeground(Color.RED);
        redSButton.setFont(new Font("Arial", Font.BOLD, 12));
        JRadioButton redOButton = new JRadioButton("O");
        redOButton.setForeground(Color.RED);
        redOButton.setFont(new Font("Arial", Font.BOLD, 12));

        ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redSButton);
        redGroup.add(redOButton);

        rightPanel.add(redSButton);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(redOButton);
        rightPanel.add(Box.createVerticalStrut(20));

        redScoreLabel = new JLabel("Score: 0");
        redScoreLabel.setFont(new Font("Arial", Font.BOLD, 12));
        if (!gameMode.equals("simple")) {
            rightPanel.add(redScoreLabel);
        }

        redSButton.addActionListener(e -> {
            if (gameLogic.getCurrentPlayer().getName().equals("Red")) {
                selectLetter('S');
            } else {
                JOptionPane.showMessageDialog(this, "It's Blue player's turn!");
                redGroup.clearSelection();
            }
        });

        redOButton.addActionListener(e -> {
            if (gameLogic.getCurrentPlayer().getName().equals("Red")) {
                selectLetter('O');
            } else {
                JOptionPane.showMessageDialog(this, "It's Blue player's turn!");
                redGroup.clearSelection();
            }
        });

        contentPanel.add(rightPanel, BorderLayout.EAST);

        gamePanel.add(contentPanel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 240));

        JPanel leftSpacer = new JPanel();
        leftSpacer.setBackground(new Color(240, 240, 240));
        leftSpacer.setPreferredSize(new Dimension(120, 0));
        bottomPanel.add(leftSpacer, BorderLayout.WEST);

        currentTurnLabel = new JLabel("Current turn: " + gameLogic.getCurrentPlayer().getName());
        currentTurnLabel.setFont(new Font("Arial", Font.BOLD, 14));
        currentTurnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(currentTurnLabel, BorderLayout.CENTER);

        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 12));
        newGameButton.addActionListener(e -> {
            setContentPane(setupPanel);
            revalidate();
            repaint();
        });
        bottomPanel.add(newGameButton, BorderLayout.EAST);

        gamePanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(gamePanel);
        revalidate();
        repaint();
    }

    private void selectLetter(char letter) {
        selectedLetter = letter;
    }

    private void handleCellClick(int row, int col) {
        if (selectedLetter == ' ') {
            JOptionPane.showMessageDialog(this, "Please select a letter first (S or O).");
            return;
        }

        SOSGame.Player current = gameLogic.getCurrentPlayer();
        if (current.makeMove(gameLogic, row, col, selectedLetter)) {
            boardButtons[row][col].setText(String.valueOf(selectedLetter));
            boardButtons[row][col].setEnabled(false);

            if (current.getName().equals("Blue")) {
                boardButtons[row][col].setForeground(Color.BLUE);
            } else {
                boardButtons[row][col].setForeground(Color.RED);
            }
            
            if (gameLogic.getWinner() != null || gameLogic.isDraw()) {
                disableBoard();

                String message;
                if (gameLogic.getWinner() != null) {
                    message = "Game Over! Winner: " + gameLogic.getWinner();
                } else {
                    message = "Game Over! It's a draw!";
                }

                Object[] options = {"New Game"};
                JOptionPane.showOptionDialog(
                    this,
                    message,
                    "Game Over",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
                );

                // Automatically go back to setup screen after clicking "New Game"
                setContentPane(setupPanel);
                revalidate();
                repaint();
            } else {
                gameLogic.switchPlayer();
                currentTurnLabel.setText("Current turn: " + gameLogic.getCurrentPlayer().getName());
            }

            selectedLetter = ' ';
        } else {
            JOptionPane.showMessageDialog(this, "Invalid move! Cell is already occupied or out of bounds.");
        }
    }
    
 // Disable all buttons after game over
    private void disableBoard() {
        for (int i = 0; i < boardButtons.length; i++) {
            for (int j = 0; j < boardButtons[i].length; j++) {
                boardButtons[i][j].setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Sprint_3GUI::new);
    }
}