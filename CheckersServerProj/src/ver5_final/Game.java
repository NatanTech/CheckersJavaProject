package ver5_final;

import java.util.ArrayList;
import ver5_final.Piece.PieceColor;
import static ver5_final.Piece.PieceColor.*;

/**
 * Game- represents game between 2 players Document : Game Created on:
 * 14/11/2021, 19:48:51 Author : beita
 */
public class Game
{
    private Player p1; // player 1 in game
    private Player p2; // player 2 in game
    private Model model; // model of the game board
    private Player currentPlayerTurn; // the player that his turn todo move
    private boolean gameStopped; // if game stopped by player

    /**
     * Constructor for new game between 2 players
     *
     * @param player1 player1 in game (will play first)
     * @param player2 player2 in game
     */
    public Game(Player player1, Player player2)
    {

        //this.ID = ID;
        this.p1 = player1;
        this.p2 = player2;
        p1.setPieceColor(WHITE_PLAYER);
        p1.setPartner(player2);
        p1.youWhite();

        p2.setPieceColor(BLACK_PLAYER);
        p2.setPartner(player1);
        p2.youBlack();

        p1.yourPartner();
        p2.yourPartner();
        this.model = new Model();
        gameStopped = false;
    }

    /**
     * Constructor for continued game between player 1 and AI
     *
     * @param player1 player1 in game
     * @param player2 player2 in game
     * @param model the model of game
     * @param currentPlayerTurn player that his turn to play when game will
     * start
     */
    public Game(Player player1, Player player2, Model model,
            Player currentPlayerTurn)
    {
        this.p1 = player1;
        this.p2 = player2;
        this.model = new Model(model);
        this.currentPlayerTurn = currentPlayerTurn;

        p1.setPieceColor(WHITE_PLAYER);
        p1.setPartner(player2);
        p1.youWhite();

        p2.setPieceColor(BLACK_PLAYER);
        p2.setPartner(player1);
        p2.youBlack();

        p1.yourPartner();
        p2.yourPartner();

        gameStopped = false;
    }

    public Player getP1()
    {
        return p1;
    }

    public Player getP2()
    {
        return p2;
    }

    public Player getCurrentPlayerTurn()
    {
        return currentPlayerTurn;
    }

    public void setGameStopped(boolean gameStopped)
    {
        this.gameStopped = gameStopped;
    }

    /**
     * Makes the game to start running
     *
     * @param continueGame if game is saved game
     */
    public void runGame(boolean continueGame)
    {

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("$$$ Start game thread: " + p1.getId()
                        + " vs " + p2.getId());
                boolean isContinue = false;
                if(continueGame)
                {
                    isContinue = true;
                }

                System.out.println("is continue game: " + isContinue);
                while(true)
                {
                    ArrayList<Move> doubleEating = new ArrayList<>();
                    if(!isContinue)
                    {
                        defineStartGame();
                    }

                    else
                    {
                        defineContinueGame();
                        isContinue = false;
                    }

                    boolean isGameOver = false;
                    while(!isGameOver)
                    {
                        // פותח לשחקן שעכשיו תורו את המיקומים שרלוונטיים אליו בלוח
                        System.out.println("currentPlayerTurn: " + currentPlayerTurn.getPieceColor());
                        currentPlayerTurn.yourTurn(); // O(N^2)
                        Move move = currentPlayerTurn.getMove(doubleEating);
                        doubleEating.clear();
                        //System.out.println("move in game: " + move);
                        if(move == null) // אם השחקן שעכשיו תורו התנתק יוצא מלולאת המשחק
                        {
                            gameStopped = true;
                            break;
                        }

                        model.makeMove(move);

                        //System.out.println("print logic board in game:");
                        //model.getLogicBoard().printBoard();
                        p1.setModel(model);
                        p2.setModel(model);
                        p1.updateView(move);
                        p2.updateView(move);
                        System.out.println("board after updateView: ");
                        model.getLogicBoard().printBoard();
                        boolean switchTurn = true;

                        // mult eating or opponent can't move
                        if(move.hasNext())
                        {
                            switchTurn = false;
                            doubleEating = move.getSecondMove();
                        }

                        System.out.println("eval for " + currentPlayerTurn.getPieceColor() + ": "
                                + model.evalWithPrints(model.getLogicBoard(), currentPlayerTurn.getPieceColor()));
                        System.out.println("swichTurn: " + switchTurn);
                        isGameOver = checkGameStatus(switchTurn); // O(N^2)
                    }

                    // אם שחקן התנתק מסיים את הריצה של המשחק
                    // ושומר את הסטטוס אם אחד מהם היה שחקן רשום
                    if(gameStopped)
                    {
                        System.out.println("Game stopped!");
                        if(registerPlayerPlayed())
                        {
                            saveGamePlayerDisconnected();
                        }

                        System.out.println("$$$ Finish game thread: " + p1.getId()
                                + " vs " + p2.getId());
                        break;
                    }

                    if(registerPlayerPlayed())
                    {
                        System.out.println("save game status!");
                        //לשמור במסד הנתונים את תוצאת המשחק
                        if(model.checkWin(currentPlayerTurn.getPieceColor()))
                        {
                            DB.gameOver(p1.getId(), p2.getId(), currentPlayerTurn.getId());
                        }
                        else
                        {
                            DB.gameOver(p1.getId(), p2.getId(), "TIE");
                        }
                    }
                    p1.countDownFinished();
                    p2.countDownFinished();
                }

            }

        }).start();

    }

    // define start new game mode for game
    private void defineStartGame()
    {
        System.out.println("#DEFINE NEW GAME");
        currentPlayerTurn = p1;
        model.setup();
        p1.setModel(model);
        p2.setModel(model);
        p1.initGame();
        p2.initGame();
        if(p2 instanceof PlayerAI && !(p1.getId().startsWith("GUEST#")))
        {
            p1.unlockSaveGameButton();
        }

        // במידה וטענו לוחות שלא נגמרו עבור שחקן מסויים והוא בחר
        // להתחיל משחק חדש
        if(!p1.getId().startsWith("GUEST#"))
        {
            p1.unlockRegisterOptions();
        }

        p2.waitTurn();
        System.out.println("finish startNewGame");
    }

    // define start continue game mode for game
    private void defineContinueGame()
    {
        System.out.println("#DEFINE CONTINUE GAME");
        p1.setModel(model);
        p2.setModel(model);
        p1.initGame();
        p2.initGame();
        if(p2 instanceof PlayerAI && !(p1.getId().startsWith("GUEST#")))
        {
            p1.unlockSaveGameButton();
        }

        if(!p1.getId().startsWith("GUEST#"))
        {
            p1.unlockRegisterOptions();
        }

        p1.waitTurn();
        p2.waitTurn();

    }

    // checks if the game over. if not, switch turns
    // (if switchTurn is true)
    private boolean checkGameStatus(boolean swithTurn)
    {
        System.out.print(">> checkGameStatus: ");

        //1. נבדוק האם יש ניצחון לשחקן הנוכחי זה ששיחק כעת
        // =============================================
        boolean isGameOverCurrent = model.checkWin(currentPlayerTurn.getPieceColor());
        boolean isGameOverOpponent = model.checkWin(getOpponent().getPieceColor());
        boolean isTie = model.checkTie();
        if(isGameOverCurrent)
        {
            PieceColor playerWin;
            if(currentPlayerTurn == p1)
            {
                playerWin = p1.getPieceColor();
            }
            else
            {
                playerWin = p2.getPieceColor();
            }

            p1.gameOver(playerWin);
            p2.gameOver(playerWin);
            //view.gameOver(msg);
            // read countdown finished
            return true;
        }

        if(isGameOverOpponent)
        {
            PieceColor playerWin;
            if(getOpponent() == p1)
            {
                playerWin = p1.getPieceColor();
            }
            else
            {
                playerWin = p2.getPieceColor();
            }

            p1.gameOver(playerWin);
            p2.gameOver(playerWin);
            //view.gameOver(msg);
            // read countdown finished
            return true;
        }
        //2. נבדוק האם יש תיקו במשחק
        //תיקו אומר שלשני השחקנים אותה כמות מלכים ולאף אחד אין חייל רגיל
        if(isTie)
        {
            p1.gameOverTie();
            p2.gameOverTie();
            //view.gameOver(msg);
            return true;
        }
        //3. אין ניצחון ואין תיקו מחליפים תור וממשיכים לשחק
        // ====================================================
        if(swithTurn)
        {
            switchTurn();
        }

        return false;
    }

    // return the opponent of currentPlayerTurn
    private Player getOpponent()
    {
        if(currentPlayerTurn == p1)
        {
            return p2;
        }
        else
        {
            return p1;
        }
    }

    // switch turn in game (currentPlayerTurn changed)
    private void switchTurn()
    {
        System.out.println("switch players & continue to play...");
        currentPlayerTurn = getOpponent();
        Player player = getOpponent();
        player.waitTurn();
    }

    /**
     * Checks if at least of 2 players is register
     *
     * @return true if at least 1 player is register, false else
     */
    public boolean registerPlayerPlayed()
    {
        return (!p1.getId().startsWith("GUEST"))
                || (!p2.getId().equals("AI") && !p2.getId().startsWith("GUEST"));
    }

    /**
     * change the gameStopped flag to true (game thread will finish)
     */
    public void stopRunGame()
    {
        System.out.println("stopRunGame()... game " + p1.getId() + " VS " + p2.getId());
        this.setGameStopped(true);
    }

    /**
     * receive un of player disconnected and update his name that he
     * disconnected
     *
     * @param un
     */
    public void playerDisconnected(String un)
    {
        System.out.println("playerDisconnected in Game class. player: " + un);
        if(p1.getId().equals(un))
        {
            p1.setId(p1.getId() + " disconnected");
        }
        if(p2.getId().equals(un))
        {
            p2.setId(p2.getId() + " disconnected");
        }
    }

    //save to DB game status when player disconnected
    private void saveGamePlayerDisconnected()
    {
        System.out.println("saveGamePlayerDisconnected()...");

        if(p1.getId().endsWith("disconnected"))
        {
            String un = p1.getId();
            String unP1 = un.substring(0, un.indexOf(' '));
            DB.gameOver(p1.getId(), unP1, p2.getId());
        }

        if(p2.getId().endsWith("disconnected"))
        {
            String un = p2.getId();
            String unP2 = un.substring(0, un.indexOf(' '));
            DB.gameOver(p1.getId(), unP2, p1.getId());
        }
    }

    @Override
    public String toString()
    {
        return "Game{" + "p1=" + p1 + ", p2=" + p2 + ", model=" + model + ", currentPlayerTurn=" + currentPlayerTurn + ", gameStopped=" + gameStopped + '}';
    }

}
