package ver6;

import java.io.Serializable;

/**
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public class MinimaxMove extends Move implements Serializable
{
    private int grade;
    private int depth;
    public static final int EMPTY = Integer.MIN_VALUE;

    public MinimaxMove(int grade, int depth, Piece player, Location source,
            Location dest, boolean isEat, Location enemyLoc, Location burnedLoc)
    {
        super(player, source, dest, isEat, enemyLoc, burnedLoc);
        this.grade = grade;
        this.depth = depth;
    }

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
    //checking if the depth and the grade didnt get down to MIN_Value
    public boolean chaeckDepth()
    {
        return grade != EMPTY && depth != EMPTY;
    }

    @Override
    public String toString()
    {
        return "MinimaxMove{" + super.toString() + ", grade=" + grade + ", depth=" + depth + '}';
    }
}
