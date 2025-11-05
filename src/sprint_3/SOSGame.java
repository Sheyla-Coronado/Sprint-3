package sprint_3;

import java.util.ArrayList;
import java.util.List;

public class SOSGame {
    private int boardSize;
    private String gameMode;
    private char[][] board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private String winner = null;
    private boolean draw = false;
    private static final char EMPTY = ' ';
    
    private int blueScore = 0;
    private int redScore = 0;
    
    public static class SOSLine {
        public int startRow, startCol, endRow, endCol;
        public String player; // "Blue" or "Red"
        
        public SOSLine(int startRow, int startCol, int endRow, int endCol, String player) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
            this.player = player;
        }
    }
    
    private List<SOSLine> sosLines = new ArrayList<>();

    public SOSGame(int boardSize, String gameMode, Player p1, Player p2) {
        this.boardSize = boardSize;
        this.player1 = p1;
        this.player2 = p2;
        this.board = new char[boardSize][boardSize];
        this.currentPlayer = player1;
        this.gameMode = gameMode;
        initializeBoard();
    }
    
    public String getWinner() {
        return winner;
    }
    
    public boolean isDraw() {
        return draw;
    }
    
    public String getGameMode() {
        return gameMode;
    }
    
    public int getBlueScore() {
        return blueScore;
    }
    
    public int getRedScore() {
        return redScore;
    }
    
    public List<SOSLine> getSOSLines() {
        return sosLines;
    }

    private void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = EMPTY;
            }
        }
    }
    
    public void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }
    
    public int checkForNewSOS() {
        return countNewSOS(currentPlayer);
    }
    
    public void checkGameStatus() {
        if (gameMode.equals("simple")) {
            if (winner != null) {
                return;
            }
        } else { // General mode
            if (isBoardFull()) {
                if (blueScore > redScore) {
                    winner = "Blue";
                } else if (redScore > blueScore) {
                    winner = "Red";
                } else {
                    draw = true;
                }
            }
        }
    }
    
    private boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                if (board[i][j] == EMPTY) return false;
        return true;
    }
    

    private int countNewSOS(Player player) {
        int count = 0;
        
        // Check horizontal
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j <= boardSize - 3; j++) {
                if (board[i][j] == 'S' && board[i][j+1] == 'O' && board[i][j+2] == 'S') {
                    if (!lineExists(i, j, i, j+2)) {
                        sosLines.add(new SOSLine(i, j, i, j+2, player.getName()));
                        count++;
                    }
                }
            }
        }
        
        // Check vertical
        for (int i = 0; i <= boardSize - 3; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 'S' && board[i+1][j] == 'O' && board[i+2][j] == 'S') {
                    if (!lineExists(i, j, i+2, j)) {
                        sosLines.add(new SOSLine(i, j, i+2, j, player.getName()));
                        count++;
                    }
                }
            }
        }
        
        // Check diagonal (top-left to bottom-right)
        for (int i = 0; i <= boardSize - 3; i++) {
            for (int j = 0; j <= boardSize - 3; j++) {
                if (board[i][j] == 'S' && board[i+1][j+1] == 'O' && board[i+2][j+2] == 'S') {
                    if (!lineExists(i, j, i+2, j+2)) {
                        sosLines.add(new SOSLine(i, j, i+2, j+2, player.getName()));
                        count++;
                    }
                }
            }
        }
        
        // Check diagonal (top-right to bottom-left)
        for (int i = 0; i <= boardSize - 3; i++) {
            for (int j = 2; j < boardSize; j++) {
                if (board[i][j] == 'S' && board[i+1][j-1] == 'O' && board[i+2][j-2] == 'S') {
                    if (!lineExists(i, j, i+2, j-2)) {
                        sosLines.add(new SOSLine(i, j, i+2, j-2, player.getName()));
                        count++;
                    }
                }
            }
        }
        
        // Update scores
        if (count > 0) {
            if (player.getName().equals("Blue")) {
                blueScore += count;
            } else {
                redScore += count;
            }
            
            if (gameMode.equals("simple")) {
                winner = player.getName();
            }
        }
        
        return count;
    }
    
    private boolean lineExists(int startRow, int startCol, int endRow, int endCol) {
        for (SOSLine line : sosLines) {
            if ((line.startRow == startRow && line.startCol == startCol && 
                 line.endRow == endRow && line.endCol == endCol) ||
                (line.startRow == endRow && line.startCol == endCol && 
                 line.endRow == startRow && line.endCol == startCol)) {
                return true;
            }
        }
        return false;
    }
    
    public int getBoardSize() { return boardSize; }
    public char[][] getBoard() { return board; }
    public Player getCurrentPlayer() { return currentPlayer; }
    
    public void resetGame() {
        initializeBoard();
        currentPlayer = player1;
        winner = null;
        draw = false;
        blueScore = 0;
        redScore = 0;
        sosLines.clear();
    }
    
    public static abstract class Player {
        protected String name;
        public Player(String name) { this.name = name; }
        public String getName() { return name; }
        
        public abstract boolean makeMove(SOSGame game, int row, int col, char letter);
    }
    
    public static class HumanPlayer extends Player {
        public HumanPlayer(String name) { super(name); }

        @Override
        public boolean makeMove(SOSGame game, int row, int col, char letter) {
            if (row < 0 || row >= game.boardSize || col < 0 || col >= game.boardSize)
                return false;
            if (game.board[row][col] != EMPTY)
                return false;
            if (letter != 'S' && letter != 'O')
                return false;

            game.board[row][col] = letter;
            return true;
        }
    }
    
    public void initLines() {
        
    }
}
