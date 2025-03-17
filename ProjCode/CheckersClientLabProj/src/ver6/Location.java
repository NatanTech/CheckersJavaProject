package ver6;

import java.io.Serializable;

/**
 * Location- represents location on the board Document : Location Created on :
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public class Location implements Serializable
{
    private int row; 
    private int col; 

    public Location(int r, int c)
    {
        row = r;
        col = c;
    }

    public Location()
    {
        row = 0;
        col = 0;
    }

    public Location(Location loc)
    {
        this.row = loc.row;
        this.col = loc.col;
    }

    // return the row of location
    public int getRow()
    {
        return row;
    }

    // return col of locationם
    public int getCol()
    {
        return col;
    }
    //checks if location is in the bounds of the mat
    public boolean isInBounds()
    {
        return row >= 0 && row <= 7
                && col >= 0 && col <= 7;
    }
    //compare tow locations
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
