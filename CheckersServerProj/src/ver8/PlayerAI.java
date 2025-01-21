package ver8;

import java.util.ArrayList;
import ver8.Piece.PieceColor;

/**
 * PlayerAI.
 * ---------------------------------------------------------------------------
 * by Ilan Peretz (ilanperets@gmail.com) 10/11/2021
 */
public class PlayerAI extends Player
{
    private int minimaxTimeoutInSec;

    /**
     * Constructor
     *
     * @param minimaxTimeout how many time minimax run for search move
     * @param id of playerAI
     */
    public PlayerAI(int minimaxTimeout, String id)
    {
        super("AI");
        this.minimaxTimeoutInSec = minimaxTimeout;
    }

    /**
     * Default constructor (without minimaxTimeoutInSec)
     */
    public PlayerAI()
    {
        super("AI");
        minimaxTimeoutInSec = 11;
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
    public Move getMove(ArrayList<Move> secondMoves)
    {
        Model model = this.getModel();
        PieceColor current = this.getPieceColor();

        if(!secondMoves.isEmpty())
        {
            System.out.println("has secondMove! moves:");
            System.out.println(secondMoves);
            try
            {
                Thread.sleep(2000);
            }
            catch(InterruptedException ex)
            {
                System.out.println("can't do sleeping!");
            }

            /*ArrayList<Move> */
            //posMoves = model.updateMoveForList(model.getLogicBoard(), current, secondMoves);
            if(secondMoves.size() == 1)
            {
                return secondMoves.get(0);
            }
            else
            {
                ArrayList<Integer> grades = new ArrayList<>();
                int maxGrade = -101;
                int index = -1;
                for(int i = 0; i < secondMoves.size(); i++)
                {
                    Move move = secondMoves.get(i);
                    Board newBoard = new Board(model.makeMoveOnCopyBoard(model.getLogicBoard(), move));
                    int grade = model.eval(newBoard, current);
                    grades.add(grade);
                    if(maxGrade < grade)
                    {
                        maxGrade = grade;
                        index = i;
                    }
                }
                return secondMoves.get(index);
            }
        }

        ArrayList<Move> posMoves = model.getAllPossibleMoves(model.getLogicBoard(), current);
        System.out.println("getMove in PlayerAI:");
        for(Move move : posMoves)
        {
            System.out.println(move);
        }
        Move move = model.getAiMove(current, minimaxTimeoutInSec);
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
    public void yourTurn()
    {
    }

    @Override
    public void initGame()
    {
    }

    @Override
    public void updateView(Move move)
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
