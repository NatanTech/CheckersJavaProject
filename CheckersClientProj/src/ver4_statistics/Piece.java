package ver4_statistics;

import java.io.Serializable;
import static ver4_statistics.Piece.PieceColor.*;

/**
 * Piece- represent piece on the board Document : Piece Created on : 17/08/2021,
 * 23:40:44 Author : beita
 */
public class Piece implements Serializable
{
    private PieceColor pieceColor; // color of piece (white or black)
    private Location pieceLoc; // location on the board
    private boolean king; // if piece is king

    /**
     * Constructor to create new piece
     *
     * @param pieceColor
     * @param pieceLoc
     * @param king
     */
    public Piece(PieceColor pieceColor, Location pieceLoc, boolean king)
    {
        this.pieceColor = pieceColor;
        this.pieceLoc = pieceLoc;
        this.king = king;
    }

    /**
     * Copy constructor
     *
     * @param piece
     */
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

    /**
     * return the char the represents the piece w- ordinary white W- white king
     * b- ordinary black B- black king
     *
     * @return
     */
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

    /**
     * Gives char and make piece by him: w- ordinary white W- white king b-
     * ordinary black B- black king
     *
     * @param c
     */
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

    public boolean equals(Piece piece)
    {
        return pieceColor.equals(piece.pieceColor)
                && pieceLoc.equals(piece.pieceLoc)
                && king == piece.king;
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
