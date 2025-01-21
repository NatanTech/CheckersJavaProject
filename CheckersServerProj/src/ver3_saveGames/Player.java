package ver3_saveGames;

import java.util.ArrayList;
import ver3_saveGames.Piece.PieceColor;

/**
 * Player.
 * ---------------------------------------------------------------------------
 * by Ilan Peretz (ilanperets@gmail.com) 10/11/2021
 */
public abstract class Player
{
    private String id;
    private PieceColor pieceColor;
    private Player partner;
    private Model model;

    public Player(String id, PieceColor pieceColor, Player partner)
    {
        this.id = id;
        this.pieceColor = pieceColor;
        this.partner = partner;
        this.model = new Model();
    }

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

    public abstract void serverClosed();

    public abstract void initGame();

    public abstract void unlockSaveGameButton();

    public abstract Move getMove();

    public abstract void waitTurn();

    public abstract void youWhite();

    public abstract void youBlack();

    public abstract void yourPartner();

    public abstract void yourTurn();

    public abstract void updateView();

    public abstract void gameOver(PieceColor player);

    public abstract void gameOverTie();

    public abstract void countDownFinished();

    public abstract void yourOpponentExit();

    public abstract void selectUnfinishedGames(ArrayList<String> boards);

    public abstract void unlockRegisterOptions();

    @Override
    public String toString()
    {
        return "Player{" + "id=" + id + ", pieceColor=" + pieceColor + ", partner=" + partner + '}';
    }

}
