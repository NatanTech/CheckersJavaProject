package ver3_saveGames;

import java.io.Serializable;
import static ver3_saveGames.Piece.PieceColor.*;

;

/**
 * Document : Piece Created on : 17/08/2021, 23:40:44 Author : beita
 */
public class Piece implements Serializable
{
    private PieceColor pieceColor;
    private Location pieceLoc;
    private boolean king;

    public Piece(PieceColor pieceColor, Location pieceLoc, boolean king)
    {
        this.pieceColor = pieceColor;
        this.pieceLoc = pieceLoc;
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
        this.pieceLoc = pieceLoc;
    }

    public boolean isWhite()
    {
        return pieceColor == PieceColor.WHITE_PLAYER;
    }

    public char getSignOfPiece()
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

    public void signToPiece(char c)
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

    @Override
    public String toString()
    {
        return "Piece{" + "pieceColor = " + pieceColor + ", pieceLoc = " + pieceLoc
                + ", king = " + king + '}';
    }

    public enum PieceColor
    {
        WHITE_PLAYER, BLACK_PLAYER
    };
}
