package ver6;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public class Move implements Serializable
{
    private Piece piece;
    private Location sourceLoc; 
    private Location locTarget; 
    private boolean isEat; 
    private Location eatenLoc;
    private Location burnedLoc; 
    private ArrayList<Move> secondMove;

    public Move(Piece piece, Location source, Location dest, boolean isEat,
            Location enemyLoc, Location burnedLoc)
    {
        this.piece = piece;
        this.sourceLoc = source;
        this.locTarget = dest;
        this.isEat = isEat;
        this.eatenLoc = enemyLoc;
        this.burnedLoc = burnedLoc;
        this.secondMove = new ArrayList<>();
    }

    public Move(Piece player, Location source, Location destination)
    {
        this(player, source, destination, false, null, null);
        this.secondMove = new ArrayList<>();
    }

    public Move(Piece piece, Location source, Location dest, boolean Eat)
    {
        this.piece = piece;
        this.sourceLoc = source;
        this.locTarget = dest;
        this.isEat = Eat;
        this.secondMove = new ArrayList<>();
    }

    public Move(Piece player, Location source, Location destination, Location burnedLoc)
    {
        this(player, source, destination, false, null, burnedLoc);
        this.secondMove = new ArrayList<>();
    }

    public Move(Piece player, Location source, Location destination, boolean isEat,
            Location enemyLoc)
    {
        this(player, source, destination, isEat, enemyLoc, null);
        this.secondMove = new ArrayList<>();
    }

    //Default constructor
    public Move()
    {
        this(null, null, null, false, null, null);
        this.secondMove = new ArrayList<>();
    }

    // Copy constructor
    public Move(Move move)
    {
        if(move != null)
        {
            if(move.getPiece() != null)
            {
                this.piece = new Piece(move.getPiece());
            }

            if(move.getSourceLoc() != null)
            {
                this.sourceLoc = new Location(move.getSourceLoc());
            }

            if(move.getLocTarget() != null)
            {
                this.locTarget = new Location(move.getLocTarget());
            }

            this.isEat = move.isIsEat();

            if(move.getEatenLoc() != null)
            {
                this.eatenLoc = new Location(move.getEatenLoc());
            }

            if(move.getBurnedLoc() != null)
            {
                this.burnedLoc = new Location(move.getBurnedLoc());
            }

            this.secondMove = new ArrayList<>();
            if(!move.getSecondMove().isEmpty())
            {
                for(int i = 0; i < move.getSecondMove().size(); i++)
                {
                    Move second = move.getSecondMove().get(i);
                    this.secondMove.add(i, second);
                }
            }
        }
        else
        {
            System.out.println("move is null");
        }
    }

    //checking if the player have another eat to make
    public boolean hasAnotherMove()
    {
        return !secondMove.isEmpty();
    }
    //return list of second move
    public ArrayList<Move> getSecondMove()
    {
        return secondMove;
    }

    public void setSecondMove(ArrayList<Move> secondMove)
    {
        for(Move move : secondMove)
        {
            addToSecondMove(move);
        }
    }

    public void addToSecondMove(Move secondMove)
    {
        this.secondMove.add(new Move(secondMove));
    }

    public Piece getPiece()
    {
        return piece;
    }

    public Location getSourceLoc()
    {
        return sourceLoc;
    }

    public Location getLocTarget()
    {
        return locTarget;
    }

    public boolean isIsEat()
    {
        return isEat;
    }

    public Location getEatenLoc()
    {
        return eatenLoc;
    }

    public Location getBurnedLoc()
    {
        return burnedLoc;
    }

    public void setBurnedLoc(Location burnedLoc)
    {
        this.burnedLoc = new Location(burnedLoc);
    }

    public void setPiece(Piece piece)
    {
        this.piece = new Piece(piece);
    }

    public void setSourceLoc(Location sourceLoc)
    {
        this.sourceLoc = new Location(sourceLoc);
    }

    public void setLocTarget(Location locTarget)
    {
        this.locTarget = new Location(locTarget);
    }

    public void setIsEat(boolean isEat)
    {
        this.isEat = isEat;
    }

    public void setEatenLoc(Location eatenLoc)
    {
        this.eatenLoc = new Location(eatenLoc);
    }
    //doing a restart to move
    public void initMove()
    {
        piece = null;
        sourceLoc = locTarget = eatenLoc = burnedLoc = null;
        isEat = false;
        secondMove.clear();
    }
    //checking that the location did not out from bounds of mat
    public boolean chaeckBounds()
    {
        return this.sourceLoc.getRow() >= 0 && this.sourceLoc.getRow() <= 7
                && this.sourceLoc.getCol() >= 0 && this.sourceLoc.getCol() <= 7;
    }

    public void copyMove(Move move)
    {
        this.piece = new Piece(move.getPiece());
        this.sourceLoc = new Location(move.getSourceLoc());
        this.locTarget = new Location(move.getLocTarget());
        this.isEat = move.isIsEat();
        if(move.getEatenLoc() != null)
        {
            this.eatenLoc = new Location(move.getEatenLoc());
        }

        if(move.getBurnedLoc() != null)
        {
            this.burnedLoc = new Location(move.getBurnedLoc());
        }

        if(!move.getSecondMove().isEmpty())
        {
            this.setSecondMove(move.secondMove);
        }
    }
    //compering tow moves
    public boolean equals(Move move)
    {
        return this.sourceLoc.isEqual(move.getSourceLoc())
                && this.locTarget.isEqual(move.getLocTarget());
    }

    @Override
    public String toString()
    {
        return "Move{piece=" + piece + ", source=" + sourceLoc + ", destination=" + locTarget
                + ", Eat=" + isEat
                + ((eatenLoc != null) ? (", enemyLoc=" + eatenLoc) : (""))
                + ((burnedLoc != null) ? (", burnedLoc=" + burnedLoc) : (""))
                + ((!secondMove.isEmpty()) ? (", secondMove=" + secondMove) : (""));
    }
}
