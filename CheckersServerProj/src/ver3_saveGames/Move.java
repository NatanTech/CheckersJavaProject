package ver3_saveGames;

import java.io.Serializable;

/**
 * Document : Move Created on : 11/05/2021, 13:11:48 Author : beita
 */
public class Move implements Serializable
{
    private Piece piece;
    private Location source, destination;
    private boolean isEat;
    private Location enemyLoc;
    private int grade;
    private int depth;
    public static final int EMPTY = Integer.MIN_VALUE;

    public Move(Piece player, Location source, Location destination,
            boolean isEat, Location enemyLoc, int grade, int depth)
    {
        this.piece = player;
        this.source = source;
        this.destination = destination;
        this.isEat = isEat;
        this.enemyLoc = enemyLoc;
        this.grade = grade;
        this.depth = depth;
    }

    public Move(Piece player, Location source, Location destination, boolean isEat, Location enemyLoc)
    {
        this(player, source, destination, isEat, enemyLoc, EMPTY, EMPTY);
    }

    public Move(Piece player, Location source, Location destination)
    {
        this(player, source, destination, false, null, EMPTY, EMPTY);
    }

    public Move()
    {
        this(null, null, null, false, null, EMPTY, EMPTY);
    }

    //פעולה בונה מעתיקה
    public Move(Move move)
    {
        this.piece = move.piece;
        this.source = move.source;
        this.destination = move.destination;
        this.isEat = move.isEat;
        this.enemyLoc = move.enemyLoc;
        this.grade = move.grade;
        this.depth = move.depth;
    }

    Move(Piece piece, Location source, Location destination, boolean isEat)
    {
        this(piece, source, destination, false, null, EMPTY, EMPTY);
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

    public boolean getIsEat()
    {
        return isEat;
    }

    public Location getEnemyLoc()
    {
        return enemyLoc;
    }

    public int getGrade()
    {
        return grade;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    public void setPiece(Piece piece)
    {
        this.piece = piece;
    }

    public void setSource(Location source)
    {
        this.source = source;
    }

    public void setDestination(Location destination)
    {
        this.destination = destination;
    }

    public void setIsEat(boolean isEat)
    {
        this.isEat = isEat;
    }

    public void setEnemyLoc(Location enemyLoc)
    {
        this.enemyLoc = enemyLoc;
    }

    public void setGrade(int grade)
    {
        this.grade = grade;
    }

    public void initMove()
    {
        piece = null;
        source = destination = enemyLoc = null;
        isEat = false;
        grade = 0;
    }

    public boolean isInBounds()
    {
        return this.source.getRow() >= 0 && this.source.getRow() <= 7
                && this.source.getCol() >= 0 && this.source.getCol() <= 7;
    }

    public void setMove(Move move)
    {
        this.piece = move.piece;
        this.source = move.source;
        this.destination = move.destination;
        this.isEat = move.isEat;
        this.enemyLoc = move.enemyLoc;
    }

    public boolean isDepthLegit()
    {
        return depth != EMPTY && grade != EMPTY;
    }

    public boolean isEquals(Move move)
    {
        return piece.equals(move.piece) && source.isEqual(move.source)
                && destination.isEqual(move.getDestination());
    }

    @Override
    public String toString()
    {
        return "Move{" + "piece=" + piece + ", source=" + source + ", destination=" + destination
                + ", isEat=" + isEat + ", enemyLoc=" + enemyLoc + ", grade=" + grade + ", depth=" + depth + '}';
    }
}
