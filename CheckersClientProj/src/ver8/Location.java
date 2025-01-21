package ver8;

import java.io.Serializable;

/**
 * Location- represents location on the board Document : Location Created on :
 * 11/05/2021, 13:09:16 Author : beita
 */
public class Location implements Serializable
{
    private int row; // row index of loc
    private int col; // col index of loc

    /**
     * Constructor to create new location
     *
     * @param r
     * @param c
     */
    public Location(int r, int c)
    {
        row = r;
        col = c;
    }

    /**
     * Constructor with default values (0, 0)
     */
    public Location()
    {
        row = 0;
        col = 0;
    }

    /**
     * Copy constructor
     *
     * @param loc
     */
    public Location(Location loc)
    {
        this.row = loc.row;
        this.col = loc.col;
    }

    // פעולה מאחזרת את ערך השורה
    public int getRow()
    {
        return row;
    }

    // פעולה מאחזרת את ערך העמודה של המיקום
    public int getCol()
    {
        return col;
    }

    /**
     * Check if the loc is in bounds of the board
     *
     * @return true if in bounds, false else
     */
    public boolean isInBounds()
    {
        return row >= 0 && row <= 7
                && col >= 0 && col <= 7;
    }

    /**
     * if the this loc is equals to loc
     *
     * @param loc
     * @return true if equals, false else
     */
    public boolean isEqual(Location loc)
    {
        return this.row == loc.row && this.col == loc.col;
    }

    @Override
    public String toString()
    {
        return "(" + row + "," + col + ")";
    }
}
