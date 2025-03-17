package ver6;

import java.io.Serializable;
import static ver6.Piece.PieceColor.*;

/**
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public class Piece implements Serializable
{
    private PieceColor pieceColor; 
    private Location pieceLoc; 
    private boolean king; 

    public Piece(PieceColor pieceColor, Location pieceLoc, boolean king)
    {
        this.pieceColor = pieceColor;
        this.pieceLoc = new Location(pieceLoc);
        this.king = king;
    }

    public Piece(Piece piece)
    {
        this.pieceColor = piece.pieceColor;
        this.pieceLoc = new Location(piece.pieceLoc);
        this.king = piece.king;
    }

    public PieceColor getPieceColor()
    {
        return pieceColor;
    }

    public Location getPieceLoc()
    {
        return pieceLoc;
    }

    public boolean isKing()
    {
        return king;
    }

    public void setKing(boolean king)
    {
        this.king = king;
    }

    public void setPieceColor(PieceColor pieceColor)
    {
        this.pieceColor = pieceColor;
    }

    public void setPieceLoc(Location pieceLoc)
    {
        this.pieceLoc = new Location(pieceLoc);
    }
    
    public boolean isWhite()
    {
        return pieceColor == WHITE_PLAYER;
    }
    //return the char of the piece color
    /*
    w - regullar white piece
    W - king white piece
    b - regullar black piece
    B - king black piece
    - empty squar
    */
    public char getCharOfPiece()
    {
        if(this.isWhite() && !this.isKing())
        {
            return 'w';
        }

        if(this.isWhite() && this.isKing())
        {
            return 'W';
        }

        if(!this.isWhite() && !this.isKing())
        {
            return 'b';
        }

        if(!this.isWhite() && this.isKing())
        {
            return 'B';
        }
        return '-';
    }

    public void charToPiece(char c)
    {
        if(c == 'w')
        {
            this.setPieceColor(WHITE_PLAYER);
            this.setKing(false);
        }

        if(c == 'W')
        {
            this.setPieceColor(WHITE_PLAYER);
            this.setKing(true);
        }

        if(c == 'b')
        {
            this.setPieceColor(BLACK_PLAYER);
            this.setKing(false);
        }

        if(c == 'B')
        {
            this.setPieceColor(BLACK_PLAYER);
            this.setKing(true);
        }

    }
    //compering tow pieces
    public boolean equals(Piece piece)
    {
        return pieceColor == piece.pieceColor
                && pieceLoc.isEqual(piece.pieceLoc)
                && king == piece.king;
    }
    
    public enum PieceColor
    {
        WHITE_PLAYER, BLACK_PLAYER
    };
    
    @Override
    public String toString()
    {
        return "Piece{" + "pieceColor = " + pieceColor + ", pieceLoc = " + pieceLoc
                + ", king = " + king + '}';
    }
    
  
}
