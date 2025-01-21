package ver5_final;

import java.io.Serializable;

/**
 * GameInfo- represents data from ResultSet Document : GameInfo Created on :
 * 23/03/2022, 22:58:01 Author : beita
 */
public class GameInfo implements Serializable
{
    private String un; // un of specific player
    private String pw; // password of player
    private String winner; // winner in ResultSet
    private String gameDate; // datetime in ResultSet
    private String wins; // numWins in ResultSet (top5)
    private String generalStats; // generalStats in ResultSet (allGames sumurry)

    /**
     * Constructor for new GameInfo
     *
     * @param un
     * @param winner
     * @param gameDate
     * @param wins
     * @param stats
     */
    public GameInfo(String un, String winner,
            String gameDate, String wins, String stats)
    {
        this.un = un;
        this.winner = winner;
        this.gameDate = gameDate;
        this.wins = wins;
        generalStats = stats;
    }

    /**
     * Constructor to create new gameInfo with un and pw
     *
     * @param un
     * @param pw
     */
    public GameInfo(String un, String pw)
    {
        this.un = un;
        this.pw = pw;
    }

    /**
     * Constructor to create new GameInfo with only generalStats (for
     * allGamesPlayed func)
     *
     * @param generalStats
     */
    public GameInfo(String generalStats)
    {
        this.generalStats = generalStats;
    }

    public String getUn()
    {
        return un;
    }

//    public void setOpponent(String opponent)
//    {
//        this.opponent = opponent;
//    }
    public String getWinner()
    {
        return winner;
    }

//    public void setWinner(String winner)
//    {
//        this.winner = winner;
//    }
    public String getGameDate()
    {
        return gameDate;
    }

    public String getWins()
    {
        return wins;
    }

    public String getGeneralStats()
    {
        return generalStats;
    }

    public String getPw()
    {
        return pw;
    }

    @Override
    public String toString()
    {
        return "GameInfo{"
                + ((un != null) ? (", un=" + un) : (""))
                + ((winner != null) ? (", winner=" + winner) : (""))
                + ((gameDate != null) ? (", gameDate=" + gameDate) : (""))
                + ((wins != null) ? (", wins=" + wins) : (""))
                + ((generalStats != null) ? (", generalStats=" + generalStats) : ("")) + '}';
    }

}
