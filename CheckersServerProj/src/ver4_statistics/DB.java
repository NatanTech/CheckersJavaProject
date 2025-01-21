package ver4_statistics;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang.time.DateUtils;

/**
 * DB - SQLמחלקת שירות לביצוע חיבור למסד נתונים מסוג אקסס וביצוע משפטי .
 * ---------------------------------------------------------------------------
 * by Amitay Agay (beitarsenal7@gmail.com) 5/11/2021
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
            str += String.format(format, rsmd.getColumnName(i)) + "\t"; //center(colWidth,rsmd.getColumnName(i));
        }
        str += "\n ";
        for(int i = 1; i <= rsmd.getColumnCount(); i++)
        {
            str += line + "\t";
        }
        str += "\n ";

        // Create All Table Rows
        while(rs.next())
        {
            //str += String.format("%2d.  ",rs.getRow());
            for(int i = 1; i <= rsmd.getColumnCount(); i++)
            {
                str += String.format(format, rs.getString(i)) + "\t"; // center(colWidth,rs.getString(i)); //
            }
            if(!rs.isLast())
            {
                str += "\n ";
            }
        }

        return str;
    }

    // receive date as long and returns him as
    // format date of: dd/M/yyyy  hh:mm
    private static String makeFormatDate(String date)
    {
        long newDate = Long.parseLong(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy  hh:mm");
        String finalDate = formatter.format(newDate);
        return finalDate;
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
     */
    public static void saveBoardToDB(String ID, String board, String playerTurn)
    {
        Date date = new Date();
        Long longDate = date.getTime();
        //System.out.println("board to save: \n" + board);
        String sql = "INSERT INTO unfinishedGames(un, board, playerTurn, dateTime) "
                + "values('" + ID + "','" + board + "','" + playerTurn + "', '" + longDate + "');";
        try
        {
            System.out.println("board saved!");
            runUpdate(sql);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * make arrayList of dates of all saved boards for player un
     *
     * @param un
     * @return
     * @throws SQLException
     */
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

    /**
     * save game status when game over or user disconnected (save game status
     * only if at least one register)
     *
     * @param p1
     * @param p2
     * @param pWinner
     */
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

    /**
     * return all games player un played and summury of them (total games
     * played, wins, looses, ties)
     *
     * @param un
     * @return
     * @throws SQLException
     */
    public static ArrayList<GameInfo> allGamesPlayed(String un) throws SQLException
    {
        ArrayList<GameInfo> allGames = new ArrayList<>();
        String sql = "SELECT un1 AS opponent, winner, datetime FROM Games WHERE un2='" + un + "';";
        String sql2 = "SELECT un2 AS opponent, winner, datetime FROM Games WHERE un1='" + un + "';";

        ResultSet rs1 = runQuery(sql);
        while(rs1.next())
        {
            String opponent = rs1.getNString(1);
            String winner = rs1.getNString(2);
            String dateTime = makeFormatDate(rs1.getNString(3));
            allGames.add(new GameInfo(opponent, winner, dateTime, null, null));
        }

        ResultSet rs2 = runQuery(sql2);
        while(rs2.next())
        {
            String opponent = rs2.getNString(1);
            String winner = rs2.getNString(2);
            String dateTime = makeFormatDate(rs2.getNString(3));
            allGames.add(new GameInfo(opponent, winner, dateTime, null, null));
        }

        allGames.add(new GameInfo(totalGamesPlayed(un)));
        allGames.add(new GameInfo(totalWins(un)));
        allGames.add(new GameInfo(totalTies(un)));
        allGames.add(new GameInfo(totalLoses(un)));

        return allGames;
    }

    // returns total wins for player un (for allGamesPlayed summury)
    private static String totalWins(String un) throws SQLException
    {

        String sql = "SELECT COUNT(winner) AS totalWins FROM Games "
                + "WHERE winner='" + un + "';";
        //System.out.println("isUserExists Query: " + sql);
        ResultSet rs = runQuery(sql);
        rs.next();
        String totalWins = "Total wins: " + rs.getNString("totalWins");
        return totalWins;
    }

    // returns total ties for player un (for allGamesPlayed summury)
    private static String totalTies(String un) throws SQLException
    {

        String sql = "SELECT COUNT(winner) AS totalTies FROM Games "
                + "WHERE (un1='" + un + "' OR un2='" + un + "') AND winner='TIE';";
        //System.out.println("isUserExists Query: " + sql);
        ResultSet rs = runQuery(sql);
        rs.next();
        String totalTies = "Total ties: " + rs.getNString("totalTies");
        return totalTies;
    }

    // returns total looses for player un (for allGamesPlayed summury)
    private static String totalLoses(String un) throws SQLException
    {
        String sql = "SELECT COUNT(winner) AS loses FROM Games WHERE "
                + "(un1='" + un + "' OR un2='" + un + "') AND"
                + " (winner NOT LIKE '" + un + "' AND winner NOT LIKE 'TIE');";
        ResultSet rs = runQuery(sql);
        rs.next();
        String totalLoses = "Total loses: " + rs.getNString("loses");
        return totalLoses;
    }

    // returns total games played for player un (for allGamesPlayed summury)
    private static String totalGamesPlayed(String un) throws SQLException
    {
        String sql = "SELECT COUNT(*) AS totalGames FROM Games WHERE "
                + "(un1='" + un + "' OR un2='" + un + "');";
        ResultSet rs = runQuery(sql);
        rs.next();
        return "Total games played: " + rs.getNString("totalGames");
    }

    public static ArrayList<GameInfo> top5() throws SQLException
    {
        ArrayList<GameInfo> top5 = new ArrayList<>();
        String sql = "SELECT un, (SELECT COUNT(*) FROM Games WHERE winner=un) FROM Users ORDER BY 2 DESC;";
        ResultSet rs = runQuery(sql);
        for(int i = 0; i < 5; i++)
        {
            rs.next();
            String un = rs.getString(1);
            String wins = rs.getString(2);
            top5.add(new GameInfo(un, null, null, wins, null));
        }

        return top5;
    }

    /**
     * delete user un from DB (and all his unfinished board and finished games
     * if played vs AI or guest)
     *
     * @param un
     * @throws SQLException
     */
    public static String deleteUser(String un) throws SQLException
    {
        String sql1 = "DELETE * FROM unfinishedGames Where un='" + un + "';";
        String sql2 = "DELETE *\n"
                + "FROM Games\n"
                + "WHERE (un1='" + un + "' AND (un2 LIKE 'GUEST*' OR un2 LIKE 'AI')) \n"
                + "OR (un2='" + un + "' AND un1 LIKE 'GUEST*');";
        String sql3 = "DELETE * FROM Users Where un='" + un + "';";

        runUpdate(sql1);
        System.out.println("delete UG succeeded!");
        runUpdate(sql2);
        System.out.println("delete games succeeded!");
        runUpdate(sql3);
        System.out.println("delete user " + un + " succeeded!");
        return "delete complete!";
    }

    /**
     * delete finished games of player un VS AI
     *
     * @param un
     * @throws SQLException
     */
    public static void deleteGamesVsAI(String un) throws SQLException
    {
        String sql = "DELETE * FROM Games Where un1='" + un + "' AND un2='AI';";
        runUpdate(sql);
    }

    /**
     * change password of player un to pw
     *
     * @param un
     * @param pw
     */
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

    /**
     * delete unfinished games of player un VS AI
     *
     * @param un
     */
    public static String deleteSavedGames(String un)
    {
        String sql = "DELETE * FROM unfinishedGames WHERE un='" + un + "';";
        try
        {
            runUpdate(sql);
            return "delete complete!";
        }
        catch(SQLException ex)
        {
            //ex.printStackTrace();
            return "delete cancel";
        }
    }

    /**
     * return playerTurn of specific dateBoard
     *
     * @param dateBoard
     * @return playerTurn by date
     */
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

    /**
     * returns board by userName un and dateTime that game saves
     *
     * @param un
     * @param dateTime
     * @return
     */
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

    /**
     * delete unfinished board that player continued by date of board continued
     *
     * @param date
     */
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

    /**
     * returns all games played last week
     *
     * @param un
     * @return ArrayList of all games played last week
     */
    public static ArrayList<GameInfo> gamesPlayedLastWeek(String un) throws SQLException
    {
        ArrayList<GameInfo> gamesLastWeek = new ArrayList<>();
        long date = new Date().getTime() - (DateUtils.MILLIS_PER_DAY * 7);
        String sql1 = "SELECT un1 AS opponent, winner, datetime FROM Games WHERE "
                + "un2='" + un + "' AND datetime >= '" + date + "';";

        String sql2 = "SELECT un2 AS opponent, winner, datetime FROM Games WHERE "
                + "un1='" + un + "' AND datetime >= '" + date + "';";

        ResultSet rs1 = runQuery(sql1);
        while(rs1.next())
        {
            String opponent = rs1.getNString(1);
            String winner = rs1.getNString(2);
            String dateTime = makeFormatDate(rs1.getNString(3));
            gamesLastWeek.add(new GameInfo(opponent, winner, dateTime, null, null));
        }

        ResultSet rs2 = runQuery(sql2);
        while(rs2.next())
        {
            String opponent = rs2.getNString(1);
            String winner = rs2.getNString(2);
            String dateTime = makeFormatDate(rs2.getNString(3));
            gamesLastWeek.add(new GameInfo(opponent, winner, dateTime, null, null));
        }

        return gamesLastWeek;

    }

    /**
     * returns all finished games of player un played vs AI
     *
     * @param un
     * @return ArrayList of all games of player un VS AI
     */
    public static ArrayList<GameInfo> gamesVsAI(String un) throws SQLException
    {
        ArrayList<GameInfo> games = new ArrayList<>();
        String sql = "SELECT winner, datetime FROM Games WHERE un1='" + un + "' AND un2='AI';";
        ResultSet rs = runQuery(sql);
        while(rs.next())
        {
            String winner = rs.getNString(1);
            String date = makeFormatDate(rs.getNString(2));
            games.add(new GameInfo(null, winner, date, null, null));
        }
        return games;
    }

    public static void main(String arg[]) throws SQLException
    {
        //System.out.println(top5());

        String board = "-------------------W-w-b----B-w---------------------------------";
        //saveBoardToDB("ronen", board, "AI");
        //saveBoardToDB("Ilan", board, "Ilan");
//        saveBoardToDB("nofar", board, "nofar");
        //saveBoardToDB("gilad", board, "AI");
//        saveBoardToDB("ariel", board, "AI");
        //saveBoardToDB("gilad", board, "gilad");
//        saveBoardToDB("nofar", board, "AI");
        //saveBoardToDB("gilad", board, "gilad");
        System.out.println(deleteUser("gilad"));
    }

}
