package ver3_saveGames;

import java.io.Serializable;

/**
 * Document : Location Created on : 11/05/2021, 13:09:16 Author : beita
 */
public class Location implements Serializable
{
    // תכונות הם משתנים שמגדירים איפה ישמרו הנתונים של הטיפוס
    private int row;    // נשמור את מספר השורה
    private int col;    // נשמור את מספר העמודה

    // פעולות
    // constructor פעולה בונה
    public Location(int r, int c)
    {
        row = r;
        col = c;
    }

    // default constractor פעולה בונה ריקה
    public Location()
    {
        row = 0;
        col = 0;
    }

    //פעולה בונה מעתיקה
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

    public boolean isInBounds()
    {
        return row >= 0 && row <= 7
                && col >= 0 && col <= 7;
    }

    public boolean isEqual(Location loc)
    {
        return this.row == loc.row && this.col == loc.col;
    }

    // הפעולה מחזירה מחרוזת המתארת את מצב העצם
    @Override
    public String toString()
    {
        //return "row="+row+",col="+col;
        return "(" + row + "," + col + ")";
    }
}
