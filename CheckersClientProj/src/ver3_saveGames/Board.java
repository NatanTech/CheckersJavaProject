package ver3_saveGames;

import java.io.Serializable;
import java.util.ArrayList;
import static ver3_saveGames.Piece.PieceColor.*;

/**
 * Document : Board Created on : 17/08/2021, 23:46:46 Author : beita
 */
public class Board implements Serializable
{
    private Piece[][] logicBoard;
    private static int ROWS = 8;
    private static int COLS = 8;

    public Board(Piece[][] logicBoard)
    {
        this.logicBoard = logicBoard;
    }

    public Board()
    {
        logicBoard = new Piece[8][8];
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                logicBoard[i][j] = null;
            }
        }

    }

    public Board(Board board)
    {
        logicBoard = new Piece[8][8];
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Location loc = new Location(i, j);
                Piece p = board.getPiece(loc);
                if(p != null)
                {
                    p = new Piece(p);
                }
                logicBoard[i][j] = p;
            }
        }
    }

    public Piece[][] getLogicBoard()
    {
        return logicBoard;
    }

    public Piece getPiece(Location loc)
    {
        return getPiece(loc.getRow(), loc.getCol());
    }

    public Piece getPiece(int row, int col)
    {
        return logicBoard[row][col];
    }

    public void setPiece(Location loc, Piece piece)
    {
        if(piece != null)
        {
            piece.setPieceLoc(loc);
        }
        logicBoard[loc.getRow()][loc.getCol()] = piece;
    }

    public void setPiece(Piece piece)
    {
        if(piece != null)
        {
            Location loc = piece.getPieceLoc();
            logicBoard[loc.getRow()][loc.getCol()] = piece;
            //System.out.println("set piece! piece " + piece);
        }
        else
        {
            //System.out.println("piece is null!");
        }
    }

    public void setManyPieces(ArrayList<Piece> list)
    {
        for(Piece piece : list)
        {
            setPiece(piece);
        }
    }

    public void deletePiece(Location loc)
    {
        logicBoard[loc.getRow()][loc.getCol()] = null;
    }

    public void makeKing(Location loc)
    {
        logicBoard[loc.getRow()][loc.getCol()].setKing(true);
    }

    public void clearBoard()
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                deletePiece(new Location(i, j));
            }
        }
    }

    public void strToBoard(String board)
    {
        int index = 0;
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                char c = board.charAt(index);
                if(c == '-')
                {
                    this.setPiece(null);
                }

                else
                {
                    Piece piece = new Piece(WHITE_PLAYER, new Location(i, j), false);
                    piece.signToPiece(c);
                    this.setPiece(piece);
                }

                index++;
            }
        }
    }

    public String boardToStr()
    {
        String str = "";
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Piece piece = this.getPiece(new Location(i, j));
                if(piece == null)
                {
                    str += "-";
                }

                else
                {
                    str += piece.getSignOfPiece();
                }
            }
        }
        return str;
    }

    public void printBoard()
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Piece piece = this.getPiece(new Location(i, j));
                //System.out.println(piece.getSignOfPiece());
                if(piece == null)
                {
                    System.out.print("-");
                }
                else
                {
                    //System.out.println("not null");
                    char c = piece.getSignOfPiece();
                    System.out.print(c);
                }
            }
            System.out.println("");
        }
    }
}
