package ver8;

import java.io.Serializable;

/**
 * Document : MinimaxMove Created on : 24/12/2021, 11:03:44 Author : beita
 */
public class MinimaxMove extends Move implements Serializable
{
    private int grade;
    private int depth;
    public static final int EMPTY = Integer.MIN_VALUE;

    /**
     * Constructor
     *
     * @param grade
     * @param depth
     * @param player
     * @param source
     * @param dest
     * @param isEat
     * @param enemyLoc
     * @param burnedLoc
     */
    public MinimaxMove(int grade, int depth, Piece player, Location source,
            Location dest, boolean isEat, Location enemyLoc, Location burnedLoc)
    {
        super(player, source, dest, isEat, enemyLoc, burnedLoc);
        this.grade = grade;
        this.depth = depth;
    }

    /**
     * Copy constructor
     *
     * @param other minimaxMove that copy his values from him
     */
    public MinimaxMove(MinimaxMove other)
    {
        super(other);
        this.grade = other.grade;
        this.depth = other.depth;
    }

    public MinimaxMove()
    {
        grade = depth = EMPTY;
    }

    public int getGrade()
    {
        return grade;
    }

    public void setGrade(int grade)
    {
        this.grade = grade;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    /**
     * Checks if depth and grade isn't empty
     *
     * @return true if grade and depth isn't empty, false else
     */
    public boolean isDepthLegit()
    {
        return grade != EMPTY && depth != EMPTY;
    }

//    private void printMinimaxMove(MinimaxMove move)
//    {
//        System.out.println("player: " + move.getPiece().getPieceColor() + ", king: "
//                + move.getPiece().isKing() + ", source: " + move.getSource()
//                + ", dest: " + move.getDestination() + ", grade: " + move.getGrade()
//                + ", depth: " + move.getDepth());
//    }
//    public void copyMinimaxMove(MinimaxMove move)
//    {
//        System.out.println("---------- in copyMinimaxMove() -----------");
//        this.copyMove(move);
//        System.out.println("after copyMove: " + this);
//        this.grade = move.grade;
//        this.depth = move.depth;
//        System.out.println("final Move: " + this);
//    }
    @Override
    public String toString()
    {
        return "MinimaxMove{" + super.toString() + ", grade=" + grade + ", depth=" + depth + '}';
    }
}
