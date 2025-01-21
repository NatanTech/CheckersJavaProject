package ver1;

import java.io.Serializable;

/**
 *  Document   : Piece
 *  Created on : 17/08/2021, 23:40:44
 *  Author     : beita
 */
public class Piece implements Serializable
{
    private Player pieceColor;
    private Location pieceLoc;
    private boolean king;
     

    public Piece(Player pieceColor, Location pieceLoc, boolean king)
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

    public Player getPieceColor()
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

    public void setPieceColor(Player pieceColor)
    {
        this.pieceColor = pieceColor;
    }

    public void setPieceLoc(Location pieceLoc)
    {
        this.pieceLoc = pieceLoc;
    }
    
    public boolean isWhite()
    {
        return pieceColor == Player.white;
    }
    
    public char getSignOfPiece()
    {
        if(this.isWhite() && !this.isKing())
            return 'w';
        
        if(this.isWhite() && this.isKing())
            return 'W';
        
        if(!this.isWhite() && !this.isKing())
            return 'b';
        
        if(!this.isWhite() && this.isKing())
            return 'B';
        return ' ';
    }

    @Override
    public String toString()
    {
        return "Piece{" + "pieceColor = " + pieceColor + ", pieceLoc = " + pieceLoc + 
        ", king = " + king + '}';
    }
    
    enum Player{white, black};
}
