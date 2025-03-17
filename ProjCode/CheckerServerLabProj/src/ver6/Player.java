package ver6;

import java.util.ArrayList;
import ver6.Piece.PieceColor;

/**
 * Player- מחלקה המייצגת שחקן כלשהו במשחק
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public abstract class Player
{
    private int id;
    private PieceColor pieceColor;
    private Player rival; 
    private State currentState; 

    public Player(int id, PieceColor pieceColor, Player partner)
    {
        this.id = id;
        this.pieceColor = pieceColor;
        this.rival = partner;
        this.currentState = new State();
    }

    public Player(int id)
    {
        this.id = id;
        currentState = new State();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public Player getRival()
    {
        return rival;
    }

    public void setRival(Player rival)
    {
        this.rival = rival;
    }

    public State getCurrentState()
    {
        return currentState;
    }

    public void setCurrentState(State currentState)
    {
        this.currentState = new State(currentState);
    }
    //server lost
    public abstract void serverClosed();
    
    //starting new game
    public abstract void initGame();
    
    //get move from client
    public abstract Move getMove(ArrayList<Move> secondMoves);
    
    //wait(other player turn)
    public abstract void waitTurn();
    
    //tells client that his color his white
    public abstract void youWhite();

    //tells client that his color his white
    public abstract void youBlack();
    
    //return rival
    public abstract void yourPartner();
    
    //play (your turn to play rival waits)
    public abstract void yourTurn();

    //update Board after avery change
    public abstract void updateView(Move move);

    //game finished with a winner
    public abstract void gameOver(PieceColor player);
    
    //game finished with a tie
    public abstract void gameOverTie();
    
    //start counting down for a new game to start
    public abstract void countDownFinished();

    //the rival left the game(on porpes or not)
    public abstract void yourOpponentExit();

    @Override
    public String toString()
    {
        return "Player{" + "id=" + id + ", pieceColor=" + pieceColor + ", partner=" + rival + '}';
    }

}
