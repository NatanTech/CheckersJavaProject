package ver2_Game;

import ver1.*;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

/**
 * DB - SQLמחלקת שירות לביצוע חיבור למסד נתונים מסוג אקסס וביצוע משפטי .
 * ---------------------------------------------------------------------------
 * by Ilan Peretz(ilanperets@gmail.com) 5/11/2021
 */
public class DB
{
    public static final String APP_DB_FILE_PATH = "/assets/db.accdb";

    /**
     * הפעולה מבצעת חיבור למסד הנתונים שהנתיב אליו מתקבל כפרמטר
     *
     * @param dbFileRelativePath הנתיב היחסי למסד הנתונים שאליו רוצים לקבל חיבור
     * @return מחזיר חיבור למסד הנתונים
     * @throws java.sql.SQLException נזרקת שגיאה אם החיבור למסד הנתונים לא מצליח
     */
    public static Connection getConnection(String... dbFileRelativePath) throws SQLException
    {
        String dbPath = APP_DB_FILE_PATH;
        if(dbFileRelativePath.length != 0)
        {
            dbPath = dbFileRelativePath[0];
        }

        //#####################################################################
        dbPath = "src" + dbPath;
        //dbPath = dbPath.substring(dbPath.lastIndexOf("/")+1); //TurnON for JAR
        //#####################################################################

        // dbURL: Access DB Driver Name + dbPath
        String dbURL = "jdbc:ucanaccess://" + new File(dbPath).getAbsolutePath();

        // create the connection object to db
        Connection dbCon = DriverManager.getConnection(dbURL, "", "");

        return dbCon;
    }

    /**
     * Run SQL Query Statement - SELECT הפעולה מריצה שאילתת
     *
     * @param sql שאילתה
     * @param dbFilePath פרמטר אופציונאלי לנתיב מסד הנתונים עליו רוצים להפעיל
     * השאילתה
     * @return מחזירה טבלת תוצאה
     * @throws java.sql.SQLException נזרקת שגיאה אם לא ניתן להריץ את השאילתה
     */
    public static ResultSet runQuery(String sql, String... dbFilePath) throws SQLException
    {
        Connection con = getConnection(dbFilePath);
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        // execute SQL Query statement
        ResultSet resultSetTable = st.executeQuery(sql);

        return resultSetTable;
    }

    /**
     * Run SQL Update Statement - INSERT, DELETE, UPDATE הפעולה מריצה משפט עדכון
     *
     * @param sql עדכון
     * @param dbFilePath
     * @return הפעולה מחזירה מספר השורות שעברו עדכון
     * @throws java.sql.SQLException נזרקת שגיאה אם לא ניתן להריץ משפט עדכון
     */
    public static int runUpdate(String sql, String... dbFilePath) throws SQLException
    {
        Connection con = getConnection(dbFilePath);
        Statement st = con.createStatement();

        // execute SQL Update statement
        int numRowsUpdated = st.executeUpdate(sql);

        return numRowsUpdated;
    }

    /**
     * הדפסת טבלת תוצאה של שאילתה
     *
     * @param rs
     * @param colWidth
     * @throws SQLException
     */
    public static void printResultSet(ResultSet rs, int... colWidth) throws SQLException
    {
        int colw = 7;
        if(colWidth.length != 0)
        {
            colw = colWidth[0];
        }

        System.out.println(toStringResultSet(rs, colw));
    }

    /**
     * מחזירה מחרוזת המתארת את טבלת התוצאה של השאילתה המתקבלת כפרמטר
     *
     * @param rs טבלת התוצאה של שאילתה
     * @param colWidth פרמטר אופציונאלי עבור רוחב עמודה רצוי
     * @return מחרזות לתיאור טבלת התוצאה
     * @throws SQLException נזרקת שגיאה אם לא ניתן לבצע המשימה
     */
    public static String toStringResultSet(ResultSet rs, int... colWidth) throws SQLException
    {
        int colw = 7;
        if(colWidth.length != 0)
        {
            colw = colWidth[0];
        }

        String str = " ";
        String format = "%-" + colw + "s";
        String line = new String(new char[colw - 2]).replace('\0', '-') + "  ";

        ResultSetMetaData rsmd = rs.getMetaData(); // to get columns names
        // Create Table Title (Columns Names)
        for(int i = 1; i <= rsmd.getColumnCount(); i++)
        {
            str += String.format(format, rsmd.getColumnName(i)); //center(colWidth,rsmd.getColumnName(i));
        }
        str += "\n ";
        for(int i = 1; i <= rsmd.getColumnCount(); i++)
        {
            str += line;
        }
        str += "\n ";

        // Create All Table Rows
        while(rs.next())
        {
            //str += String.format("%2d.  ",rs.getRow());
            for(int i = 1; i <= rsmd.getColumnCount(); i++)
            {
                str += String.format(format, rs.getString(i)); // center(colWidth,rs.getString(i)); //
            }
            if(!rs.isLast())
            {
                str += "\n ";
            }
        }

        return str;
    }

    // =======================================================================
    // CUSTOM METHODS HERE . . .
    // =======================================================================
    /**
     * בדיקה האם יוזר ששם המשתמש והסיסמה המתקבלים כפרמטירים קיים בטבלת היוזרים
     *
     * @param un שם המשתמש של היוזר
     * @param pw הסיסמה של היוזר
     * @return מחזיר אמת אם קיים יוזר כזה ושקר אחרת
     * @throws SQLException נזרקת שגיאה אם לא ניתן לבצע השאילה
     */
    public static boolean isUserExists(String un, String pw) throws SQLException
    {
        String sql = "SELECT * FROM users WHERE un='" + un + "' AND pw='" + pw + "'";
        //System.out.println("isUserExists Query: " + sql);
        ResultSet rs = runQuery(sql);
        boolean isExists = rs.next();

        return isExists;
    }

    /**
     * save board to unfinishedGames table (register user vs AI player)
     *
     * @param ID
     * @param board
     * @param playerTurn
     * @throws SQLException
     */
    public static void saveBoardToDB(String ID, String board, String playerTurn) throws SQLException
    {
        Date date = new Date();
        Long longDate = date.getTime();
        System.out.println("board to save: \n" + board);
        String sql = "INSERT INTO unfinishedGames(un, board, playerTurn, dateTime) "
                + "values('" + ID + "','" + board + "','" + playerTurn + "', '" + longDate + "');";
        runUpdate(sql);
        System.out.println("board saved!");
    }

    public static ArrayList<String> selectUnfinishedBoards(String un) throws SQLException
    {
        ArrayList<String> allBoards = new ArrayList<>();
        allBoards.add("New game");
        String sql = "SELECT dateTime FROM unfinishedGames WHERE un='" + un + "';";
        ResultSet rs = runQuery(sql);
        while(rs.next())
        {
            String dateBoard = rs.getNString("dateTime");
            allBoards.add(dateBoard);
        }

        return allBoards;
    }

    // save end game status- players, result and date
    public static void gameOver(String p1, String p2, String pWinner)
    {
        long dateTime = new Date().getTime();

        String sql = "INSERT INTO Games (un1, un2, winner, datetime) VALUES ('"
                + p1 + "', '" + p2 + "', '" + pWinner + "', '" + dateTime + "');";
        try
        {
            runUpdate(sql);
            System.out.println("gameOver in DB: " + sql);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static String AllGamesPlayed(String un) throws SQLException
    {
        String sql = "SELECT * FROM Games WHERE un1='" + un + "' OR "
                + "un2='" + un + "';";
        ResultSet rs = runQuery(sql);
        String allGames = toStringResultSet(rs) + "\n";
        allGames += totalGamesPlayed(un);
        allGames += totalWins(un) + "\n";
        allGames += totalTies(un) + "\n";
        allGames += totalLoses(un) + "\n";
        return allGames;
    }

    public static String totalWins(String un) throws SQLException
    {

        String sql = "SELECT COUNT(winner) AS totalWins FROM Games "
                + "WHERE winner='" + un + "';";
        //System.out.println("isUserExists Query: " + sql);
        ResultSet rs = runQuery(sql);
        rs.next();
        String totalWins = "Total wins: " + rs.getNString("totalWins");
        return totalWins;
    }

    public static String totalTies(String un) throws SQLException
    {

        String sql = "SELECT COUNT(winner) AS totalTies FROM Games "
                + "WHERE (un1='" + un + "' OR un2='" + un + "') AND winner='TIE';";
        //System.out.println("isUserExists Query: " + sql);
        ResultSet rs = runQuery(sql);
        rs.next();
        String totalTies = "Total ties: " + rs.getNString("totalTies");
        return totalTies;
    }

    public static String totalLoses(String un) throws SQLException
    {
        String sql = "SELECT COUNT(winner) AS loses FROM Games WHERE "
                + "(un1='" + un + "' OR un2='" + un + "') AND"
                + " (winner NOT LIKE '" + un + "' AND winner NOT LIKE 'TIE');";
        ResultSet rs = runQuery(sql);
        rs.next();
        String totalLoses = "Total loses: " + rs.getNString("loses");
        return totalLoses;
    }

    public static String totalGamesPlayed(String un) throws SQLException
    {
        String sql = "SELECT COUNT(*) AS totalGames FROM Games WHERE "
                + "(un1='" + un + "' OR un2='" + un + "');";
        ResultSet rs = runQuery(sql);
        rs.next();
        String totalGames = rs.getNString("totalGames");
        return totalGames;
    }

    public static String top5() throws SQLException
    {
        //String un = "";
        String sql = "";
        //System.out.println("isUserExists Query: " + sql);
        ResultSet rs = runQuery(sql);
        return sql;
    }

    public static void deleteUser(String un) throws SQLException
    {
        String sql1 = "DELETE * FROM Users Where un='" + un + "';";
        String sql2 = "DELETE * FROM unfinishedGames Where un='" + un + "';";
        String sql3 = "DELETE * FROM Games Where (un1='" + un + "' OR un2= '"
                + un + "') AND (un1='AI' OR un2='AI' OR un1='GUEST*' OR"
                + " un2='GUEST*);";
        runUpdate(sql1);
        runUpdate(sql2);
        runUpdate(sql3);
    }

    public static void deleteGamesVsAI(String un) throws SQLException
    {
        String sql = "DELETE * FROM Games Where un1='" + un + "' AND un2='AI';";
        runUpdate(sql);
    }

    public static void changePassword(String un, String pw)
    {
        String sql = "UPDATE Users SET pw='" + pw + "' WHERE un='" + un + "';";
        try
        {
            runUpdate(sql);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }

    }

    public static void deleteSavedGames(String un)
    {
        String sql = "DELETE * FROM unfinishedGames WHERE un='" + un + "';";
        try
        {
            runUpdate(sql);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static String getPlayerTurn(String dateBoard)
    {
        String sql = "SELECT playerTurn FROM unfinishedGames WHERE dateTime='" + dateBoard + "';";
        try
        {
            ResultSet rs = runQuery(sql);
            rs.next();
            String playerTurn = rs.getNString("playerTurn");
            return playerTurn;
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }

    }

    public static String getBoard(String un, String dateTime)
    {
        String sql = "SELECT board FROM unfinishedGames WHERE un='" + un + "' AND dateTime='" + dateTime + "';";
        try
        {
            ResultSet rs = runQuery(sql);
            rs.next();
            String board = rs.getNString("board");
            return board;
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public static void deleteUnfinishedGameContinued(String date)
    {
        String sql = "DELETE * FROM unfinishedGames WHERE dateTime='" + date + "';";
        try
        {
            runUpdate(sql);
            System.out.println("deleteUnfinshedBoard: " + sql);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void main(String arg[]) throws SQLException
    {
//        String board = "-----------------------b----B-w---------------------------------";
//        saveBoardToDB("ronen", board, "AI");
//        saveBoardToDB("Ilan", board, "Ilan");
    }

}
