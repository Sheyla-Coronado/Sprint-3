package sprint_3;

public class SOSGame {
    private int boardSize;
    private String gameMode;
    private char[][] board;
    private Player player1;
    private Player player2;
    private Player currentPlayer; // blue or red
    private String winner = null;
    private boolean draw = false;
    private static final char EMPTY = ' ';

    public SOSGame(int boardSize, String gameMode, Player p1, Player p2) {
        this.boardSize = boardSize;
        this.player1= p1;
        this.player2= p2;
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
    
    public void checkGameStatus() {
    	if (gameMode.equals("simple")) {
    		if (checkSOS(currentPlayer)) {
    			winner = currentPlayer.getName(); // first SOS wins
    		}
    		else if (isBoardFull()) {
    			draw = true;
    		}
    	}
    	else {
    		int blueScore = countSOS(player1);
    		int redScore = countSOS(player2);
    		if (isBoardFull()) {
    			if (blueScore > redScore) winner = player1.getName();
    			else if (redScore > blueScore) winner = player2.getName();
    			else draw = true;
    		}
    	}
    }
    
    private boolean isBoardFull() {
    	for (int i = 0; i < boardSize; i++)
    		for (int j = 0; j < boardSize; j++)
    			if (board[i][j] == EMPTY) return false;
    	return true;
    }
    
    
    private boolean checkSOS(Player player) {
    	char letter = (player == player1) ? 'B' : 'R';
    	return countSOS(player) > 0;
    }
    
    private int countSOS(Player player) {
        int count = 0;
        char letter = (player.getName().equals("Blue")) ? 'B' : 'R';
        // Check horizontal, vertical, diagonal for SOS
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (j <= boardSize - 3 &&
                    board[i][j] == 'S' && board[i][j+1] == 'O' && board[i][j+2] == 'S') count++;
                if (i <= boardSize - 3 &&
                    board[i][j] == 'S' && board[i+1][j] == 'O' && board[i+2][j] == 'S') count++;
                if (i <= boardSize - 3 && j <= boardSize - 3 &&
                    board[i][j] == 'S' && board[i+1][j+1] == 'O' && board[i+2][j+2] == 'S') count++;
                if (i <= boardSize - 3 && j >= 2 &&
                    board[i][j] == 'S' && board[i+1][j-1] == 'O' && board[i+2][j-2] == 'S') count++;
            }
        }
        return count;
    }
    
    
    public int getBoardSize() { return boardSize; }
    public char[][] getBoard() { return board; }
    public Player getCurrentPlayer() { return currentPlayer; }
    
    public void resetGame() {
    	initializeBoard();
    	currentPlayer = player1;
    	winner = null;
    	draw = false;
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
            game.checkGameStatus();
            return true;
        }
    }
    public void initLines() {
    	
    }
    
}