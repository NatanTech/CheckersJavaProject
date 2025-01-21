package ver4_statistics;

import java.util.ArrayList;
import ver4_statistics.Piece.PieceColor;

/**
 * Player- מחלקה המייצגת שחקן כלשהו במשחק
 * ---------------------------------------------------------------------------
 * by Amitay Agay (beitarsenal7@gmail.com) 10/11/2021
 */
public abstract class Player
{
    private String id; // name of player
    private PieceColor pieceColor; // color of player (black or white)
    private Player partner; // opponent of player
    private Model model; // board of the game

    /**
     * Constructor for new player
     *
     * @param id
     * @param pieceColor
     * @param partner
     */
    public Player(String id, PieceColor pieceColor, Player partner)
    {
        this.id = id;
        this.pieceColor = pieceColor;
        this.partner = partner;
        this.model = new Model();
    }

    /**
     * Constructor for new player with only ID
     *
     * @param id
     */
    public Player(String id)
    {
        this.id = id;
        model = new Model();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public PieceColor getPieceColor()
    {
        return pieceColor;
    }

    public void setPieceColor(PieceColor pieceColor)
    {
        this.pieceColor = pieceColor;
    }

    public Player getPartner()
    {
        return partner;
    }

    public void setPartner(Player partner)
    {
        this.partner = partner;
    }

    public Model getModel()
    {
        return model;
    }

    public void setModel(Model model)
    {
        this.model = new Model(model);
    }

    /**
     * Sends to player that the server closed
     */
    public abstract void serverClosed();

    /**
     * sends to player to init the board to start the game
     */
    public abstract void initGame();

    /**
     * Sends to player that he need to unlock SAVE & EXIT button
     */
    public abstract void unlockSaveGameButton();

    /**
     * Sends to player request to make move
     *
     * @return move of player selected
     */
    public abstract Move getMove();

    /**
     * Sends to player to wait turn (lock the board)
     */
    public abstract void waitTurn();

    /**
     * Sends to player that his color pieces is white (to update the window
     * title)
     */
    public abstract void youWhite();

    /**
     * Sends to player that his color pieces is white (to update the window
     * title)
     */
    public abstract void youBlack();

    /**
     * Sends to player who is his opponent (to update the window title)
     */
    public abstract void yourPartner();

    /**
     * Sends to player that this is his turn (to unlock the board)
     */
    public abstract void yourTurn();

    /**
     * Sends to player to update his view according to the move his opponent
     * made
     */
    public abstract void updateView();

    /**
     * Sends to player that game over and who is the winner (the pieceColor)
     *
     * @param player
     */
    public abstract void gameOver(PieceColor player);

    /**
     * Sends to player that game over with TIE
     */
    public abstract void gameOverTie();

    /**
     * Waits for signal from player that his count down after game session
     * finished
     */
    public abstract void countDownFinished();

    /**
     * Sends to player that his opponent exit the game and player win
     * technically
     */
    public abstract void yourOpponentExit();

    /**
     * Sends to player all his unfinished games to select one of them (or new
     * game)
     *
     * @param boards
     */
    public abstract void selectUnfinishedGames(ArrayList<String> boards);

    /**
     * Sends to player to unlock all register user options (like unique
     * statistics)
     */
    public abstract void unlockRegisterOptions();

    @Override
    public String toString()
    {
        return "Player{" + "id=" + id + ", pieceColor=" + pieceColor + ", partner=" + partner + '}';
    }

}
