package ver5_final;

import java.io.Serializable;
import static ver5_final.Piece.PieceColor.*;

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
     * @param pieceColor white or black piece
     * @param pieceLoc location on board
     * @param king if piece is king
     */
    public Piece(PieceColor pieceColor, Location pieceLoc, boolean king)
    {
        this.pieceColor = pieceColor;
        this.pieceLoc = new Location(pieceLoc);
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
        this.pieceLoc = new Location(pieceLoc);
    }

    /**
     * Checks if this piece is white player
     *
     * @return true if piece is white piece, false otherwise
     */
    public boolean isWhite()
    {
        return pieceColor == WHITE_PLAYER;
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

    /**
     * Checks if piece is equals to this
     *
     * @param piece
     * @return
     */
    public boolean equals(Piece piece)
    {
        return pieceColor == piece.pieceColor
                && pieceLoc.isEqual(piece.pieceLoc)
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
