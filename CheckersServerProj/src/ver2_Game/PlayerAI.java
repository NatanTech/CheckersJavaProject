package ver2_Game;

import ver1.*;
import java.io.Serializable;
import java.util.ArrayList;
import ver2_Game.Piece.PieceColor;

/**
 * PlayerAI.
 * ---------------------------------------------------------------------------
 * by Ilan Peretz (ilanperets@gmail.com) 10/11/2021
 */
public class PlayerAI extends Player
{
    private int minimaxTimeoutInSec;

    public PlayerAI(int minimaxTimeout, String id)
    {
        super("AI");
        this.minimaxTimeoutInSec = minimaxTimeout;
    }

    public PlayerAI()
    {
        super("AI");
    }

    public int getMinimaxTimeout()
    {
        return minimaxTimeoutInSec;
    }

    public void setMinimaxTimeout(int minimaxTimeout)
    {
        this.minimaxTimeoutInSec = minimaxTimeout;
    }

    @Override
    public void gameOverTie()
    {
    }

    @Override
    public String toString()
    {
        return "PlayerAI{" + super.toString() + ", minimaxTimeout=" + minimaxTimeoutInSec + '}';
    }

    @Override
    public Move getMove(Model model)
    {
        PieceColor current = this.getPieceColor();
        Move move = model.getAiMove(current);
        return move;
    }

    @Override
    public void waitTurn()
    {
    }

    @Override
    public void gameOver(PieceColor player)
    {
    }

    @Override
    public void yourTurn(Model model)
    {
    }

    @Override
    public void initGame(Model model)
    {
    }

    @Override
    public void updateView()
    {
    }

    @Override
    public void youWhite()
    {
    }

    @Override
    public void youBlack()
    {
    }

    @Override
    public void countDownFinished()
    {
    }

    @Override
    public void unlockSaveGameButton()
    {
    }

    @Override
    public void yourPartner()
    {
    }

    @Override
    public void yourOpponentExit()
    {
    }

    @Override
    public void selectUnfinishedGames(ArrayList<String> boards)
    {
    }

    @Override
    public void unlockRegisterOptions()
    {
    }

    @Override
    public void serverClosed()
    {
    }

}
