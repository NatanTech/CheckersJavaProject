package ver9;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message- represents message that sends between client and server
 * ---------------------------------------------------------------------------
 * by Amitay Agay (beitarsenal7@gmail.com) 10/11/2021 Message class of client
 * project
 */
public class Message implements Serializable
{
    private String subject; // subject of message (what to do)
    private Board board; // board of game
    private Location loc; // location that player pressed
    private Move move; // move that player selected
    private ArrayList<Move> posMoves; // all moves player can do
    private ArrayList<Location> locs; // all locations player can pressed
    private LoginDetails login; // todo login to server
    private String dateBoard; // date of saves board selected
    private String[] changePw; // details needs to change password
    private ArrayList<String> boards; // array of dateBoards to select one
    private ArrayList<GameInfo> gameInfo; // data from resultset

    /**
     * Constructor to create new message with only subject
     *
     * @param subject
     */
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
        this.board = new Board(board);
    }

    public Move getMove()
    {
        return move;
    }

    public LoginDetails getLogin()
    {
        return login;
    }

    public String getDateBoard()
    {
        return dateBoard;
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
        this.boards = new ArrayList<>(boards);
    }

    public void setDateBoard(String statsNum)
    {
        this.dateBoard = statsNum;
    }

    public void setLogin(LoginDetails login)
    {
        this.login = new LoginDetails(login);
    }

    public void setMove(Move move)
    {
        this.move = new Move(move);
    }

    public Location getLoc()
    {
        return loc;
    }

    public void setLoc(Location loc)
    {
        this.loc = new Location(loc);
    }

    public ArrayList<Move> getPosMoves()
    {
        return posMoves;
    }

    public void setPosMoves(ArrayList<Move> posMoves)
    {
        this.posMoves = new ArrayList<>(posMoves);
    }

    public ArrayList<Location> getLocs()
    {
        return locs;
    }

    public void setLocs(ArrayList<Location> locs)
    {
        this.locs = new ArrayList<>(locs);
    }

    public ArrayList<GameInfo> getGameInfo()
    {
        return gameInfo;
    }

    public void setGameInfo(ArrayList<GameInfo> gameInfo)
    {
        this.gameInfo = new ArrayList<>(gameInfo);
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
                + ((dateBoard != null) ? (", stats=" + dateBoard) : (""))
                + ((changePw != null) ? (", changePw=" + changePw) : (""))
                + ((boards != null) ? (", boards=" + boards) : (""))
                + ((gameInfo != null) ? (", gameInfo=" + gameInfo) : ("")) + '}';
    }

}
