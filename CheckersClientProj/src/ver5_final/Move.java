package ver5_final;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Move- represents move (location source and destination) that some player do
 * Document : Move Created on : 11/05/2021, 13:11:48 Author : beita
 */
public class Move implements Serializable
{
    private Piece piece; //piece will do the move
    private Location source; // location source of piece
    private Location destination; // dest loc piece will move there
    private boolean Eat; //if move include eating
    private Location enemyLoc; // loc of enemy piece we will eat (if exists)
    private Location burnedLoc; // if any piece will be burned
    private ArrayList<Move> secondMove; // all options for double eating

    /**
     * Constructor
     *
     * @param player
     * @param source
     * @param dest
     * @param isEat
     * @param enemyLoc
     * @param burnedLoc
     */
    public Move(Piece player, Location source, Location dest, boolean isEat,
            Location enemyLoc, Location burnedLoc)
    {
        this.piece = player;
        this.source = source;
        this.destination = dest;
        this.Eat = isEat;
        this.enemyLoc = enemyLoc;
        this.burnedLoc = burnedLoc;
        this.secondMove = new ArrayList<>();
    }

    /**
     * Constructor to create simple move with only piece, source and dest loc
     *
     * @param player
     * @param source
     * @param destination
     */
    public Move(Piece player, Location source, Location destination)
    {
        this(player, source, destination, false, null, null);
        this.secondMove = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param piece
     * @param source
     * @param dest
     * @param Eat
     */
    public Move(Piece piece, Location source, Location dest, boolean Eat)
    {
        this.piece = piece;
        this.source = source;
        this.destination = dest;
        this.Eat = Eat;
        this.secondMove = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param player
     * @param source
     * @param destination
     * @param burnedLoc
     */
    public Move(Piece player, Location source, Location destination, Location burnedLoc)
    {
        this(player, source, destination, false, null, burnedLoc);
        this.secondMove = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param player
     * @param source
     * @param destination
     * @param isEat
     * @param enemyLoc
     */
    public Move(Piece player, Location source, Location destination, boolean isEat,
            Location enemyLoc)
    {
        this(player, source, destination, isEat, enemyLoc, null);
        this.secondMove = new ArrayList<>();
    }

    /**
     * Default constructor
     */
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

            if(move.getSource() != null)
            {
                this.source = new Location(move.getSource());
            }

            if(move.getDestination() != null)
            {
                this.destination = new Location(move.getDestination());
            }

            this.Eat = move.isEat();

            if(move.getEnemyLoc() != null)
            {
                this.enemyLoc = new Location(move.getEnemyLoc());
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

    /**
     * Checks if any move include second move (exists if move has double eating)
     *
     * @return
     */
    public boolean hasNext()
    {
        return (!secondMove.isEmpty());
    }

    public ArrayList<Move> getSecondMove()
    {
        return secondMove;
    }

    public void setSecondMove(ArrayList<Move> secondMove)
    {
        //this.secondMove = new ArrayList<>(secondMove);
        for(Move move : secondMove)
        {
            addToSecondMove(move);
        }
    }

    /**
     * Adds a move to secondMove Array
     *
     * @param secondMove
     */
    public void addToSecondMove(Move secondMove)
    {
        this.secondMove.add(new Move(secondMove));
    }

    public Piece getPiece()
    {
        return piece;
    }

    public Location getSource()
    {
        return source;
    }

    public Location getDestination()
    {
        return destination;
    }

    public boolean isEat()
    {
        return Eat;
    }

    public Location getEnemyLoc()
    {
        return enemyLoc;
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

    public void setSource(Location source)
    {
        this.source = new Location(source);
    }

    public void setDestination(Location destination)
    {
        this.destination = new Location(destination);
    }

    public void setEat(boolean isEat)
    {
        this.Eat = isEat;
    }

    public void setEnemyLoc(Location enemyLoc)
    {
        this.enemyLoc = new Location(enemyLoc);
    }

    /**
     * Initializes exists move to default values
     */
    public void initMove()
    {
        piece = null;
        source = destination = enemyLoc = burnedLoc = null;
        Eat = false;
        secondMove.clear();
    }

    /**
     * Checks if the move is in bounds of the board (legal move)
     *
     * @return
     */
    public boolean isInBounds()
    {
        return this.source.getRow() >= 0 && this.source.getRow() <= 7
                && this.source.getCol() >= 0 && this.source.getCol() <= 7;
    }

    /**
     * Copies the values of move to this
     *
     * @param move
     */
    public void copyMove(Move move)
    {
        this.piece = new Piece(move.getPiece());
        this.source = new Location(move.getSource());
        this.destination = new Location(move.getDestination());
        this.Eat = move.isEat();
        if(move.getEnemyLoc() != null)
        {
            this.enemyLoc = new Location(move.getEnemyLoc());
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

    /**
     * Checks if move is equals to this
     *
     * @param move
     * @return
     */
    public boolean equals(Move move)
    {
        return this.source.isEqual(move.getSource())
                && this.destination.isEqual(move.getDestination());
    }

//    public void printSimpleMove()
//    {
//        String sout = "piece: " + piece + " ,king: " + piece.isKing() + " , source: "
//                + source + " ,dest: " + destination + " ,eat: " + Eat
//                + ((enemyLoc != null) ? (", enemyLoc: " + enemyLoc) : (""))
//                + ((burnedLoc != null) ? (", burnedLoc: " + burnedLoc) : (""))
//                + ((!secondMove.isEmpty()) ? (", secondMove: " + secondMove) : (""));
//        System.out.println(sout);
//    }
    @Override
    public String toString()
    {
//        System.out.println("piece: " + piece + ", source: " + source + ", dest: " + destination +
//                "eat: " + Eat);
//        System.out.println("enemyLoc: " + enemyLoc);
//        System.out.println("burnedLoc: " + burnedLoc);
//        System.out.println("secondMove: " + );

        return "Move{piece=" + piece + ", source=" + source + ", destination=" + destination
                + ", Eat=" + Eat
                + ((enemyLoc != null) ? (", enemyLoc=" + enemyLoc) : (""))
                + ((burnedLoc != null) ? (", burnedLoc=" + burnedLoc) : (""))
                + ((!secondMove.isEmpty()) ? (", secondMove=" + secondMove) : (""));
    }
}
