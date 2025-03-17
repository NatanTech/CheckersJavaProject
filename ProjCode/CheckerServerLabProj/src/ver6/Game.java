package ver6;

import java.util.ArrayList;
import ver6.Piece.PieceColor;
import static ver6.Piece.PieceColor.*;

/**
 * Game- represents game between 2 players 
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public class Game
{
    private Player player1; // player 1 
    private Player player2; // player 2 in game
    private State currentState ;//current player and current board
    private boolean gameStopped; 

    public Game(Player player1, Player player2)
    {
        this.player1 = player1;
        this.player2 = player2;
        this.player1.setPieceColor(WHITE_PLAYER);
        this.player1.setRival(player2);
        this.player1.youWhite();

        this.player2.setPieceColor(BLACK_PLAYER);
        this.player2.setRival(player1);
        this.player2.youBlack();

        this.player1.yourPartner();
        this.player2.yourPartner();
        this.currentState = new State();
        gameStopped = false;
    }

    public Game(Player player1, Player player2, State model,
            Player currentPlayerTurn)
    {
        this.player1 = player1;
        this.player2 = player2;
        this.currentState = new State(model);
        this.currentState.setCurrentPlayer(currentPlayerTurn);

        this.player1.setPieceColor(WHITE_PLAYER);
        this.player1.setRival(player2);
        this.player1.youWhite();

        this.player2.setPieceColor(BLACK_PLAYER);
        this.player2.setRival(player1);
        this.player2.youBlack();

        this.player1.yourPartner();
        this.player2.yourPartner();

        gameStopped = false;
    }
    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
    public Player getPlayer1()
    {
        return player1;
    }

    public Player getPlayer2()
    {
        return player2;
    }

    public Player getCurrentPlayer()
    {
        return currentState.getCurrentPlayer();
    }

    public void setGameStopped(boolean gameStopped)
    {
        this.gameStopped = gameStopped;
    }

    //start running game
    public void runGame()
    {

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("$$$ Start game thread: " + player1.getId()
                        + " vs " + player2.getId());
                while(true)
                {
                    ArrayList<Move> doubleEating = new ArrayList<>();
                    defineStartGame();
                    boolean isGameOver = false;
                    while(!isGameOver)
                    {
                        System.out.println("currentPlayerTurn: " + currentState.getCurrentPlayer().getPieceColor());
                        currentState.getCurrentPlayer().yourTurn(); 
                        Move move = currentState.getCurrentPlayer().getMove(doubleEating);
                        doubleEating.clear();
                        if(move == null) 
                        {
                            gameStopped = true;
                            break;
                        }
                        currentState.makeMove(move);
                        player1.setCurrentState(currentState);
                        player2.setCurrentState(currentState);
                        player1.updateView(move);
                        player2.updateView(move);
                        System.out.println("board after updateView: ");
                        currentState.getCurrentBoard().printBoard();
                        boolean switchTurn = true;

                        if(move.hasAnotherMove())
                        {
                            switchTurn = false;
                            doubleEating = move.getSecondMove();
                        }
                        System.out.println("eval for " + currentState.getCurrentPlayer().getPieceColor() + ": "
                                + currentState.evalWithPrints(currentState.getCurrentBoard(), currentState.getCurrentPlayer().getPieceColor()));
                        System.out.println("swichTurn: " + switchTurn);
                        isGameOver = checkGameStatus(switchTurn);
                    }

                    // אם שחקן התנתק מסיים את הריצה של המשחק
                    if(gameStopped)
                    {
                        System.out.println("Game stopped!"); 
                        System.out.println(" Finish game thread: " + player1.getId()
                                + " vs " + player2.getId());
                        break;
                    }
                    player1.countDownFinished();
                    player2.countDownFinished();
                }
            }
        }).start();
    }

    // define start new game mode for game
    private void defineStartGame()
    {
        System.out.println("#DEFINE NEW GAME");
        currentState.setCurrentPlayer(player1);
        currentState.setup();
        player1.setCurrentState(currentState);
        player2.setCurrentState(currentState);
        player1.initGame();
        player2.initGame();
        player2.waitTurn();
        System.out.println("finish startNewGame");
    }
    
    // check game over. if not, switch turns
    private boolean checkGameStatus(boolean switchTurn)
    {
        System.out.print(">> checkGameStatus: ");

        boolean isGameOverCurrent = currentState.checkWin(currentState.getCurrentPlayer().getPieceColor());
        boolean isGameOverOpponent = currentState.checkWin(getOpponent().getPieceColor());
        boolean isTie = currentState.checkTie();
        if(isGameOverCurrent)
        {
            PieceColor playerWin;
            if(currentState.getCurrentPlayer() == player1)
            {
                playerWin = player1.getPieceColor();
            }
            else
            {
                playerWin = player2.getPieceColor();
            }
            player1.gameOver(playerWin);
            player2.gameOver(playerWin);
            return true;
        }

        if(isGameOverOpponent)
        {
            PieceColor playerWin;
            if(getOpponent() == player1)
            {
                playerWin = player1.getPieceColor();
            }
            else
            {
                playerWin = player2.getPieceColor();
            }

            player1.gameOver(playerWin);
            player2.gameOver(playerWin);
            return true;
        }
        //2. נבדוק האם יש תיקו במשחק
        if(isTie)
        {
            player1.gameOverTie();
            player2.gameOverTie();
            return true;
        }
        //3. אין ניצחון ואין תיקו מחליפים תור וממשיכים לשחק
        if(switchTurn)
        {
            switchTurn();
        }
        return false;
    }

    // return the opponent of currentPlayer
    private Player getOpponent()
    {
        if(currentState.getCurrentPlayer() == player1)
        {
            return player2;
        }
        else
        {
            return player1;
        }
    }

    // switch turn in game
    private void switchTurn()
    {
        System.out.println("switch players & continue to play...");
        currentState.setCurrentPlayer(getOpponent());
        Player player = getOpponent();
        player.waitTurn();
    }
    //change the gameStopped flag to true 
    public void stopRunGame()
    {
        System.out.println("stopRunGame()... game " + player1.getId() + " VS " + player2.getId());
        gameStopped = true;
    }

    //changing the id of player that dissconnected to -1
    public void playerDisconnected(int ID)
    {
        System.out.println("playerDisconnected in Game class. player: " + ID);
        if(player1.getId()==(ID))
        {
            player1.setId(-1);
        }
        if(player2.getId()==(ID))
        {
            player2.setId(-1);
        }
    }

    @Override
    public String toString()
    {
        return "Game{" + "p1=" + player1 + ", p2=" + player2 + ", state=" + currentState + ", currentPlayerTurn=" + currentState.getCurrentPlayer() + ", gameStopped=" + gameStopped + '}';
    }

}
