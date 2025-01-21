package ver1;

import java.io.Serializable;

/**
 * Message.
 * ---------------------------------------------------------------------------
 * by Ilan Peretz (ilanperets@gmail.com) 10/11/2021
 */
public class Message implements Serializable
{
    private String subject;
    private Board board;
    private Move move;
    private LoginDetails login;

    public Message(String subject)
    {
        this.subject = subject;
    }
    
    public Message(String subject, Board board)
    {
        this.subject = subject;
        this.board = board;
    }

    public Message(String subject, Board board, Move move)
    {
        this.subject = subject;
        this.board = board;
        this.move = move;
    }

    public Message(String subject, Board board, Move move, LoginDetails login)
    {
        this.subject = subject;
        this.board = board;
        this.move = move;
        this.login = login;
    }

    public Message(String subject, LoginDetails login)
    {
        this.subject = subject;
        this.login = login;
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

    public void setLogin(LoginDetails login)
    {
        this.login = login;
    }
    
    public void setMove(Move move)
    {
        this.move = move;
    }

    @Override
    public String toString()
    {
        return "Message{" + "subject=" + subject + ", board=" + board + ", move=" + move + ", login=" + login + '}';
    }

    
    
}