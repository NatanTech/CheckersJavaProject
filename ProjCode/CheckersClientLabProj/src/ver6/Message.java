package ver6;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message- message that sends between client and server
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public class Message extends Const implements Serializable 
{
    private String subject; 
    private Board board; 
    private Location locOfPress; 
    private Move move; 
    private ArrayList<Move> posMoves; 
    private ArrayList<Location> posLocs; 
    private PlayerType login; 
    private String gameOverMsg;
    

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

    public PlayerType getLogin()
    {
        return login;
    }

    public void setLogin(PlayerType login)
    {
        this.login = new PlayerType(login);
    }

    public void setMove(Move move)
    {
        this.move = new Move(move);
    }

    public Location getLocOfPress()
    {
        return locOfPress;
    }

    public void setLocOfPress(Location locOfPress)
    {
        this.locOfPress = new Location(locOfPress);
    }

    public ArrayList<Move> getPosMoves()
    {
        return posMoves;
    }

    public void setPosMoves(ArrayList<Move> posMoves)
    {
        this.posMoves = new ArrayList<>(posMoves);
    }

    public ArrayList<Location> getPosLocs()
    {
        return posLocs;
    }

    public void setPosLocs(ArrayList<Location> posLocs)
    {
        this.posLocs = new ArrayList<>(posLocs);
    }

        public String getGameOverMsg() {
        return gameOverMsg;
    }

    public void setGameOverMsg(String gameOverMsg) {
        this.gameOverMsg = gameOverMsg;
    }

    @Override
    public String toString()
    {
        return "Message{"
                + "subject=" + subject
                + ((board != null) ? (", board=" + board) : (""))
                + ((locOfPress != null) ? (", loc=" + locOfPress) : (""))
                + ((move != null) ? (", move=" + move) : (""))
                + ((posMoves != null) ? (", posMoves=" + posMoves) : (""))
                + ((posLocs != null) ? (", locs=" + posLocs) : (""))
                + ((login != null) ? (", login=" + login) : (""));
    }

}
