package ver3_saveGames;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message.
 * ---------------------------------------------------------------------------
 * by Ilan Peretz (ilanperets@gmail.com) 10/11/2021 Message class of server
 * project
 */
public class Message implements Serializable
{
    private String subject;
    private Board board;
    private Location loc;
    private Move move;
    private ArrayList<Move> posMoves;
    private ArrayList<Location> locs;
    private LoginDetails login;
    private String stats;
    private String[] changePw;
    private ArrayList<String> boards;

    public Message(String subject)
    {
        this.subject = subject;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(Board board)
    {
        this.board = board;
    }

    public Move getMove()
    {
        return move;
    }

    public LoginDetails getLogin()
    {
        return login;
    }

    public String getStats()
    {
        return stats;
    }

    public String[] getChangePw()
    {
        return changePw;
    }

    public void setChangePw(String[] changePw)
    {
        this.changePw = changePw;
    }

    public ArrayList<String> getBoards()
    {
        return boards;
    }

    public void setBoards(ArrayList<String> boards)
    {
        this.boards = boards;
    }

    public void setStats(String statsNum)
    {
        this.stats = statsNum;
    }

    public void setLogin(LoginDetails login)
    {
        this.login = login;
    }

    public void setMove(Move move)
    {
        this.move = move;
    }

    public Location getLoc()
    {
        return loc;
    }

    public void setLoc(Location loc)
    {
        this.loc = loc;
    }

    public ArrayList<Move> getPosMoves()
    {
        return posMoves;
    }

    public void setPosMoves(ArrayList<Move> posMoves)
    {
        this.posMoves = posMoves;
    }

    public ArrayList<Location> getLocs()
    {
        return locs;
    }

    public void setLocs(ArrayList<Location> locs)
    {
        this.locs = locs;
    }

    @Override
    public String toString()
    {
        return "Message{"
                + "subject=" + subject
                + ((board != null) ? (", board=" + board) : (""))
                + ((loc != null) ? (", loc=" + loc) : (""))
                + ((move != null) ? (", move=" + move) : (""))
                + ((posMoves != null) ? (", posMoves=" + posMoves) : (""))
                + ((locs != null) ? (", locs=" + locs) : (""))
                + ((login != null) ? (", login=" + login) : (""))
                + ((stats != null) ? (", stats=" + stats) : (""))
                + ((changePw != null) ? (", changePw=" + changePw) : (""))
                + ((boards != null) ? (", boards=" + boards) : (""))
                + '}';
    }

}
