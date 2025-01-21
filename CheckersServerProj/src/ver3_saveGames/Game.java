package ver3_saveGames;

import ver3_saveGames.Piece.PieceColor;
import static ver3_saveGames.Piece.PieceColor.*;

/**
 * Document : Game Created on : 14/11/2021, 19:48:51 Author : beita
 */
public class Game
{
    //private int ID;
    private Player p1;
    private Player p2;
    private Model model;
    private Player currentPlayerTurn;
    private boolean gameStopped;

    /**
     *
     * @param player1
     * @param player2
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

    public Game(Player player1, Player player2, Model model, Player currentPlayerTurn)
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
                    if(!isContinue)
                    {
                        defineStartGame();
                    }

                    else
                    {
                        defineContinueGame();
                        isContinue = false;
                    }

                    System.out.println("print logic board in new game:");
                    model.getLogicBoard().printBoard();
                    System.out.println("print logic board of p1:");
                    p1.getModel().getLogicBoard().printBoard();
                    System.out.println("print logic board of p2:");
                    p2.getModel().getLogicBoard().printBoard();
                    boolean isGameOver = false;
                    while(!isGameOver)
                    {

                        // פותח לשחקן שעכשיו תורו את המיקומים שרלוונטיים אליו בלוח
                        currentPlayerTurn.yourTurn();
                        Move move = currentPlayerTurn.getMove();
                        System.out.println("move in game: " + move);
                        if(move == null) // אם השחקן שעכשיו תורו התנתק יוצא מלולאת המשחק
                        {
                            gameStopped = true;
                            break;
                        }

                        model.makeMove(move);

                        System.out.println("print logic board in game:");
                        model.getLogicBoard().printBoard();
                        p1.setModel(model);
                        p2.setModel(model);
                        p1.updateView();
                        p2.updateView();
                        isGameOver = checkGameStatus();
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

        if(!p1.getId().startsWith("GUEST#"))
        {
            p1.unlockRegisterOptions();
        }
        if(!p2.getId().startsWith("GUEST#") && !p2.getId().equals("AI"))
        {
            p2.unlockRegisterOptions();
        }
        //p1.waitTurn();
        p2.waitTurn();

    }

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
        if(!p2.getId().startsWith("GUEST#") && !p2.getId().equals("AI"))
        {
            p2.unlockRegisterOptions();
        }
        p1.waitTurn();
        p2.waitTurn();

    }

    // הפעולה בודקת את מצב המשחק
    // האם המשחק ניגמר בניצחון או תיקו
    // אם המשחק לא ניגמר אז מבצעים החלפת תור שחקן
    private boolean checkGameStatus()
    {
        System.out.print(">> checkGameStatus: ");

        //1. נבדוק האם יש ניצחון לשחקן הנוכחי זה ששיחק כעת
        // =============================================
        boolean isGameOver = model.checkWin(currentPlayerTurn.getPieceColor());
        boolean isTie = model.checkTie();
        if(isGameOver)
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
        switchTurn();
        return false;
    }

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

    private void switchTurn()
    {
        System.out.println("switch players & continue to play...");
        currentPlayerTurn = getOpponent();
        Player player = getOpponent();
        player.waitTurn();
    }

    public boolean registerPlayerPlayed()
    {
        return (!p1.getId().startsWith("GUEST"))
                || (!p2.getId().equals("AI") && !p2.getId().startsWith("GUEST"));
    }

    //change the gameStopped flag to true (game thread will finish)
    public void stopRunGame()
    {
        System.out.println("stopRunGame()... game " + p1.getId() + " VS " + p2.getId());
        this.setGameStopped(true);
    }

    //receive un of player disconnected and update
    //his name that he disconnected
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

//    public void playerSaveGame()
//    {
//        p2.setId("AI save game");
//    }
    @Override
    public String toString()
    {
        return "Game{" + "p1=" + p1 + ", p2=" + p2 + ", model=" + model + ", currentPlayerTurn=" + currentPlayerTurn + ", gameStopped=" + gameStopped + '}';
    }

}
