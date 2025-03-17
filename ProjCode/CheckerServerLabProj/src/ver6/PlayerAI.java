package ver6;

import java.util.ArrayList;
import ver6.Piece.PieceColor;

/**
 * PlayerAI.
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public class PlayerAI extends Player
{
    private int minimaxTimeoutInSec;

    public PlayerAI(int minimaxTimeout, int id)
    {
        super(-1000);
        this.minimaxTimeoutInSec = minimaxTimeout;
    }

    public PlayerAI()
    {
        super(-1000);
        minimaxTimeoutInSec = 15;
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
        State model = this.getCurrentState();
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
                    Board newBoard = new Board(model.makeMoveOnCopyBoard(model.getCurrentBoard(), move));
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

        ArrayList<Move> posMoves = model.getAllPossibleMoves(model.getCurrentBoard(), current);
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
    public void yourPartner()
    {
    }

    @Override
    public void yourOpponentExit()
    {
    }


    @Override
    public void serverClosed()
    {
    }

}
