package ver8;

import java.util.ArrayList;
import java.util.Arrays;
import ver8.Piece.PieceColor;
import static ver8.Piece.PieceColor.*;

/**
 * Document : Model Created on : 11/05/2021, 13:42:23 Author : beita
 */
public class Model
{
    // Constants - קבועים
    private static final int ROWS = 8;  // מספר השורות במטריצה
    private static final int COLS = 8;  // מספר העמודות במטריצה
    //public static final int MINIMAX_TIME = 11; // זמן למינימקס

    // תכונות
    private Board logicBoard;   // לוח משחק לוגי
    private int numMoves;

    /**
     * constractor
     */
    public Model()
    {
        // יצירת לוח לוגי לצורך בדיקות וקבלת החלטות במשחק
        logicBoard = new Board();
        numMoves = 0;
    }

    //Copy constructor
    public Model(Model model)
    {
        this.logicBoard = new Board(model.logicBoard);
        //this.controller = model.controller;
    }

    /**
     * Sets this logicBoard by logicBoard other
     *
     * @param logicBoard
     */
    public void setModel(Board logicBoard)
    {
        this.logicBoard = new Board(logicBoard);
    }

    public Board getLogicBoard()
    {
        return logicBoard;
    }

    /**
     * Gets all empty locations on the board
     *
     * @return ArrayList of all empty locations
     */
    public ArrayList<Location> getEmptyLocs()
    {
        ArrayList<Location> emptyLocs = new ArrayList<>();

        for(int row = 0; row < ROWS; row++)
        {
            for(int col = 0; col < COLS; col++)
            {
                Location loc = new Location(row, col);
                Piece player = logicBoard.getPiece(loc);
                if(player == null && !isWhiteTile(loc))
                {
                    emptyLocs.add(new Location(row, col));
                }
            }
        }

        return emptyLocs;
    }

    /**
     * Checks if the tile of loc is empty tile
     *
     * @param loc
     * @return true if the tile is white, false else
     */
    private boolean isWhiteTile(Location loc)
    {
        int row = loc.getRow(), col = loc.getCol();
        return (row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0);
    }

    /**
     * makes setup to the model
     */
    public void setup()
    {
        normalSetup();
        //testingSetup();
        //smallSetup();
        //kingsMovesTest();
        //doubleEatingSetup();
        //tripleEatingSetup();
        //cantMoveTestingSetup();
    }

    // normal starting board
    private void normalSetup()
    {
        boolean isBlackTile;
        for(int row = 0; row < ROWS; row++)
        {
            isBlackTile = row % 2 != 0;
            for(int col = 0; col < COLS; col++)
            {
                Location loc = new Location(row, col);
                if(isBlackTile)
                {
                    if(row >= 0 && row <= 2)
                    {
                        Piece p = new Piece(BLACK_PLAYER, loc, false);
                        logicBoard.setPiece(loc, p);
                    }
                    if(row >= 5 && row <= 7)
                    {
                        Piece p = new Piece(WHITE_PLAYER, loc, false);
                        logicBoard.setPiece(loc, p);
                    }

                    if(row >= 3 && row <= 4)
                    {
                        logicBoard.makeEmptyTile(loc);
                    }
                    isBlackTile = false;
                }
                else
                {
                    logicBoard.makeEmptyTile(loc);
                    isBlackTile = true;
                }
            }

        }
    }

    // testing board
    private void kingsMovesTest()
    {
        logicBoard.clearBoard();
        ArrayList<Piece> list = new ArrayList(Arrays.asList(new Piece(WHITE_PLAYER, new Location(4, 3), true),
                new Piece(BLACK_PLAYER, new Location(6, 1), false),
                new Piece(WHITE_PLAYER, new Location(1, 6), false),
                new Piece(BLACK_PLAYER, new Location(1, 0), false),
                new Piece(BLACK_PLAYER, new Location(5, 4), true)));
        logicBoard.setManyPieces(list);
    }

    // testing board
    private void testingSetup()
    {
        logicBoard.clearBoard();
        ArrayList<Piece> list = new ArrayList(Arrays.asList(new Piece(WHITE_PLAYER, new Location(4, 3), false),
                new Piece(WHITE_PLAYER, new Location(4, 7), false),
                new Piece(BLACK_PLAYER, new Location(2, 3), false),
                new Piece(BLACK_PLAYER, new Location(2, 7), false)));
        logicBoard.setManyPieces(list);
    }

    // testing board
    private void smallSetup()
    {
        boolean isBlack;
        for(int row = 0; row < ROWS; row++)
        {
            isBlack = row % 2 != 0;
            for(int col = 0; col < COLS; col++)
            {
                Location loc = new Location(row, col);
                if(isBlack)
                {
                    if(row == 0)
                    {
                        Piece p = new Piece(BLACK_PLAYER, loc, false);
                        logicBoard.setPiece(loc, p);
                    }
                    if(row == 7)
                    {
                        Piece p = new Piece(WHITE_PLAYER, loc, false);
                        logicBoard.setPiece(loc, p);
                    }

                    isBlack = false;
                }
                else
                {
                    logicBoard.setPiece(loc, null);
                    isBlack = true;
                }

            }

        }
    }

    // testing board
    private void doubleEatingSetup()
    {
        logicBoard.clearBoard();
        ArrayList<Piece> list = new ArrayList(Arrays.asList(new Piece(WHITE_PLAYER, new Location(4, 3), true),
                new Piece(BLACK_PLAYER, new Location(3, 4), true),
                new Piece(BLACK_PLAYER, new Location(1, 6), false)));
        logicBoard.setManyPieces(list);
    }

    // testing board
    private void tripleEatingSetup()
    {
        logicBoard.clearBoard();
        ArrayList<Piece> list = new ArrayList(Arrays.asList(new Piece(WHITE_PLAYER, new Location(6, 3), true),
                new Piece(WHITE_PLAYER, new Location(4, 1), false),
                new Piece(BLACK_PLAYER, new Location(5, 4), false),
                new Piece(BLACK_PLAYER, new Location(3, 4), false),
                new Piece(BLACK_PLAYER, new Location(1, 4), false)));
        logicBoard.setManyPieces(list);
    }

    // testing board
    private void cantMoveTestingSetup()
    {
        logicBoard.clearBoard();
        ArrayList<Piece> list = new ArrayList(Arrays.asList(new Piece(BLACK_PLAYER, new Location(0, 7), true),
                new Piece(WHITE_PLAYER, new Location(2, 7), false),
                new Piece(WHITE_PLAYER, new Location(2, 5), false)));
        logicBoard.setManyPieces(list);
    }

    /**
     * Checks if specific player can't move
     *
     * @param board that the func checks if player can't move on it
     * @param player that the func checks if he can't move
     * @return true if player can't move, false otherwise
     */
    public boolean playerCantMove(Board board, PieceColor player)
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(!board.isEmptyTile(new Location(i, j)))
                {
                    Piece p = board.getPiece(new Location(i, j));
                    if(canDoMove(p) && p.getPieceColor() == player)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

//    /**
//     *
//     * @param move
//     * @return if the move in logic board include eat
//     */
//    public boolean isEat(Move move)
//    {
//        return isEat(logicBoard, move);
//    }
//
//    /**
//     *
//     * @param board
//     * @param move
//     * @return if the move in specific board include eat
//     */
//    public boolean isEat(Board board, Move move)
//    {
//        return getTheEatChoosed(board, move) != null;
//    }
//    /**
//     *
//     * @param move
//     * @return the enemy location that choosed to eat by the move in logic board
//     */
//    public Location getTheEatChoosed(Move move)
//    {
//        return getTheEatChoosed(logicBoard, move);
//    }
//
//    /**
//     *
//     * @param board
//     * @param move
//     * @return the enemy location that choosed to eat by the move in specific
//     * board
//     */
//    public Location getTheEatChoosed(Board board, Move move)
//    {
//        ArrayList<Location> enemyLocs = getEnemyLocations(board, move.getSource());
//        Piece piece = move.getPiece();
//        PieceColor player = piece.getPieceColor();
//        Location source = move.getSource(), dest = move.getDestination();
//        if(piece.isKing())
//        {
//            return getTheEatChoosedForKings(board, move);
//        }
//        if(player == WHITE_PLAYER)
//        {
//            Location newSource1 = new Location(source.getRow() - 2, source.getCol() + 2);
//            Location newSource2 = new Location(source.getRow() - 2, source.getCol() - 2);
//            if(newSource1.isInBounds())
//            {
//                if(newSource1.getRow() == dest.getRow() && newSource1.getCol() == dest.getCol())
//                {
//                    for(int i = 0; i < enemyLocs.size(); i++)
//                    {
//                        Location loc = enemyLocs.get(i),
//                                enemy = new Location(source.getRow() - 1, source.getCol() + 1);
//
//                        if(enemy.getRow() == loc.getRow()
//                                && enemy.getCol() == loc.getCol())
//                        {
//                            return new Location(loc.getRow(), loc.getCol());
//                        }
//                    }
//                }
//            }
//
//            if(newSource2.isInBounds())
//            {
//                if(newSource2.getRow() == dest.getRow()
//                        && newSource2.getCol() == dest.getCol())
//                {
//                    for(int i = 0; i < enemyLocs.size(); i++)
//                    {
//                        Location loc = enemyLocs.get(i),
//                                enemy = new Location(source.getRow() - 1, source.getCol() - 1);
//
//                        if(enemy.getRow() == loc.getRow()
//                                && enemy.getCol() == loc.getCol())
//                        {
//                            return new Location(loc.getRow(), loc.getCol());
//                        }
//                    }
//                }
//            }
//        }
//
//        if(player == BLACK_PLAYER)
//        {
//            Location newSource1 = new Location(source.getRow() + 2, source.getCol() + 2);
//            Location newSource2 = new Location(source.getRow() + 2, source.getCol() - 2);
//
//            if(newSource1.isInBounds())
//            {
//                if(newSource1.getRow() == dest.getRow()
//                        && newSource1.getCol() == dest.getCol())
//                {
//                    for(int i = 0; i < enemyLocs.size(); i++)
//
//                    {
//
//                    }
//                }
//            }
//
//            if(newSource2.isInBounds())
//            {
//                if(newSource2.getRow() == dest.getRow()
//                        && newSource2.getCol() == dest.getCol())
//                {
//                    for(int i = 0; i < enemyLocs.size(); i++)
//
//                    {
//
//                    }
//                }
//            }
//        }
//        return null;
//    }
//    /**
//     *
//     * @param board
//     * @param move
//     * @return location of enemy in move of king (if exists), null else
//     */
//    private Location getTheEatChoosedForKings(Board board, Move move)
//    {
//        Location enemy = getTheEatUpperRightDiagonal(board, move);
//        if(enemy != null)
//        {
//            return enemy;
//        }
//        enemy = getTheEatUpperLeftDiagonal(board, move);
//        if(enemy != null)
//        {
//            return enemy;
//        }
//        enemy = getTheEatLowerRightDiagonal(board, move);
//        if(enemy != null)
//        {
//            return enemy;
//        }
//        return getTheEatLowerLeftDiagonal(board, move);
//    }
//    /**
//     *
//     * @param board
//     * @param move
//     * @return location of enemy in move of king on the upper right diagonal (if
//     * exists), null else
//     *
//     */
//    private Location getTheEatUpperRightDiagonal(Board board, Move move)
//    {
//        Location enemyLoc = null;
//        Piece.PieceColor player = move.getPiece().getPieceColor();
//        int rowSrc = move.getSource().getRow(), colSrc = move.getSource().getCol(),
//                rowDest = move.getDestination().getRow(), colDest = move.getDestination().getCol();
//        for(int i = 1; new Location(rowSrc - i, colSrc + i).isInBounds(); i++)
//        {
//            Location tmpLoc = new Location(rowSrc - i, colSrc + i);
//            Location nextLoc = new Location(rowSrc - (i + 1), colSrc + (i + 1));
//            Piece tmpPiece = board.getPiece(tmpLoc);
//            Piece nextPiece = board.getPiece(nextLoc);
//            Piece.PieceColor opponent = tmpPiece.getPieceColor();
//
//            if(tmpPiece != null && nextLoc.isInBounds())
//            {
//                if(opponent != player && nextPiece == null)
//                {
//                    enemyLoc = new Location(tmpLoc);
//                }
//                if(tmpLoc == new Location(rowDest, colDest))
//                {
//                    return enemyLoc;
//                }
//                if(opponent == player)
//                {
//                    break;
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     *
//     * @param board
//     * @param move
//     * @return location of enemy in move of king on the upper left diagonal (if
//     * exists), null else
//     */
//    private Location getTheEatUpperLeftDiagonal(Board board, Move move)
//    {
//        Location enemyLoc = null;
//        PieceColor player = move.getPiece().getPieceColor();
//        int rowSrc = move.getSource().getRow(), colSrc = move.getSource().getCol(),
//                rowDest = move.getDestination().getRow(), colDest = move.getDestination().getCol();
//        for(int i = 1; new Location(rowSrc - i, colSrc - i).isInBounds(); i++)
//        {
//            Location tmpLoc = new Location(rowSrc - i, colSrc - i);
//            Location nextLoc = new Location(rowSrc - (i + 1), colSrc - (i + 1));
//            Piece tmpPiece = board.getPiece(tmpLoc);
//            Piece nextPiece = board.getPiece(nextLoc);
//            Piece.PieceColor opponent = tmpPiece.getPieceColor();
//
//            if(tmpPiece != null && nextLoc.isInBounds())
//            {
//                if(opponent != player && nextPiece == null)
//                {
//                    enemyLoc = new Location(tmpLoc);
//                }
//                if(tmpLoc == new Location(rowDest, colDest))
//                {
//                    return enemyLoc;
//                }
//                if(opponent == player)
//                {
//                    break;
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     *
//     * @param board
//     * @param move
//     * @return location of enemy in move of king on the lower right diagonal (if
//     * exists), null else
//     */
//    private Location getTheEatLowerRightDiagonal(Board board, Move move)
//    {
//        Location enemyLoc = null;
//        PieceColor player = move.getPiece().getPieceColor();
//        int rowSrc = move.getSource().getRow(), colSrc = move.getSource().getCol(),
//                rowDest = move.getDestination().getRow(), colDest = move.getDestination().getCol();
//        for(int i = 1; new Location(rowSrc + i, colSrc + i).isInBounds(); i++)
//        {
//            Location tmpLoc = new Location(rowSrc + i, colSrc + i);
//            Location nextLoc = new Location(rowSrc + (i + 1), colSrc + (i + 1));
//            Piece tmpPiece = board.getPiece(tmpLoc);
//            Piece nextPiece = board.getPiece(nextLoc);
//            Piece.PieceColor opponent = tmpPiece.getPieceColor();
//
//            if(tmpPiece != null && nextLoc.isInBounds())
//            {
//                if(opponent != player && nextPiece == null)
//                {
//                    enemyLoc = new Location(tmpLoc);
//                }
//                if(tmpLoc == new Location(rowDest, colDest))
//                {
//                    return enemyLoc;
//                }
//                if(opponent == player)
//                {
//                    break;
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     *
//     * @param board
//     * @param move
//     * @return location of enemy in move of king on the lower left diagonal (if
//     * exists), null else
//     */
//    private Location getTheEatLowerLeftDiagonal(Board board, Move move)
//    {
//        Location enemyLoc = null;
//        PieceColor player = move.getPiece().getPieceColor();
//        int rowSrc = move.getSource().getRow(), colSrc = move.getSource().getCol(),
//                rowDest = move.getDestination().getRow(), colDest = move.getDestination().getCol();
//        for(int i = 1; new Location(rowSrc + i, colSrc - i).isInBounds(); i++)
//        {
//            Location tmpLoc = new Location(rowSrc + i, colSrc - i);
//            Location nextLoc = new Location(rowSrc + (i + 1), colSrc - (i + 1));
//            Piece tmpPiece = board.getPiece(tmpLoc);
//            Piece nextPiece = board.getPiece(nextLoc);
//            Piece.PieceColor opponent = tmpPiece.getPieceColor();
//
//            if(tmpPiece != null && nextLoc.isInBounds())
//            {
//                if(opponent != player && nextPiece == null)
//                {
//                    enemyLoc = new Location(tmpLoc);
//                }
//                if(tmpLoc == new Location(rowDest, colDest))
//                {
//                    return enemyLoc;
//                }
//                if(opponent == player)
//                {
//                    break;
//                }
//            }
//        }
//        return null;
//    }
    /**
     *
     * @param loc
     * @return the piece that placed in loc on logic board
     */
    public Piece pieceInLoc(Location loc)
    {
        return pieceInLoc(logicBoard, loc);
    }

    /**
     *
     * @param board
     * @param loc
     * @return the piece that placed in loc on specific board
     */
    public Piece pieceInLoc(Board board, Location loc)
    {
        return board.getPiece(loc);
    }

    /**
     *
     * @param board
     * @param move make move on specific board
     * @param isCopied
     * @return updated board with the move that accepted
     */
    public Board makeMove(Board board, Move move)
    {
        //System.out.println("----------------- in makeMove() ------------------");
        //System.out.println("source: " + move.getSource() + ", dest: " + move.getDestination());
        //System.out.println("before make move: " + move.getPiece());
        Location source = move.getSource(),
                destination = move.getDestination();
        Piece p = new Piece(move.getPiece());
        PieceColor player = p.getPieceColor();
        board.setPiece(destination, p);
        board.makeEmptyTile(source);

        if(move.isEat())
        {
            Location enemy = move.getEnemyLoc();
            board.makeEmptyTile(enemy);
        }

        if(move.getBurnedLoc() != null)
        {
            board.makeEmptyTile(move.getBurnedLoc());
        }

        //System.out.println("update pieceLoc");
        //pieceInLoc(board, destination).setPieceLoc(destination);
        if((player == WHITE_PLAYER && destination.getRow() == 0)
                || (player == BLACK_PLAYER && destination.getRow() == ROWS - 1))
        {
            board.makeKing(destination);
        }

        //System.out.println("board after makeMove:");
        //board.printBoard();
        return board;
    }

    // makes move on copied board
    public Board makeMoveOnCopyBoard(Board board, Move move)
    {
        Board newBoard = new Board(board);
        //System.out.println("----------------- in makeMoveOnCopyBoard() ------------------");
        //System.out.println("source: " + move.getSource() + ", dest: " + move.getDestination());
        //System.out.println("before make move: " + move.getPiece());
        Location source = move.getSource(),
                destination = move.getDestination();
        Piece p = new Piece(move.getPiece());
        PieceColor player = p.getPieceColor();

        //updateMove(board, move);
//        if(move.isEat())
//        {
//            System.out.println("this is eat!. " + "enemyLoc: " + move.getEnemyLoc());
//        }
        newBoard.setPieceOnCopiedBoard(destination, p);
        //p.setPieceLoc(destination);
        newBoard.makeEmptyTile(source);

        if(move.isEat())
        {
            Location enemy = new Location(move.getEnemyLoc());
            newBoard.makeEmptyTile(enemy);
        }

        if(move.getBurnedLoc() != null)
        {
            newBoard.makeEmptyTile(move.getBurnedLoc());
        }

        //System.out.println("update pieceLoc");
        //pieceInLoc(newBoard, destination).setPieceLoc(destination);
        if((player == WHITE_PLAYER && destination.getRow() == 0)
                || (player == BLACK_PLAYER && destination.getRow() == ROWS - 1))
        {
            p.setKing(true);
        }

        //System.out.println("after make move: " + move.getPiece());
        //System.out.println("move in makeMove(): " + move);
        return newBoard;
    }

    /**
     * Make move on logicBoard
     *
     * @param move
     * @param isCopied
     * @return
     */
    public Board makeMove(Move move)
    {
        return makeMove(logicBoard, move);
    }

    /**
     * updateMove on logicBoard
     *
     * @param move
     */
    public Move updateMove(Move move)
    {
        return updateMove(logicBoard, move);
    }

    /**
     * Makes the move from basic move (source and dest) to extends (with
     * burnedLoc, enemyLoc, isEat...)
     *
     * @param board
     * @param move
     */
    public Move updateMove(Board board, Move move)
    {
        //System.out.println("-------------------- in updateMove ------------------------");
        ArrayList<Move> posMoves = possibleMovesForPiece(board, move.getSource(), true);
        for(Move pos : posMoves)
        {
            if(move.equals(pos))
            {
                return pos;
            }
        }
        return move;
    }

    /**
     * Makes updateMove func to list
     *
     * @param board
     * @param player
     * @param moves
     * @return updated ArrayList after updateMove for each move in list
     */
    public ArrayList<Move> updateMoveForList(Board board, PieceColor player, ArrayList<Move> moves)
    {
        ArrayList<Move> updatedMoves = new ArrayList<>();
        System.out.println("in updateMoveForList");
        for(Move move : moves)
        {
            System.out.println(move);
            Move newMove = new Move(updateMove(board, move));
            updatedMoves.add(newMove);
        }
        return updatedMoves;
    }

    // setBurnedLocForAllMoves for GAPM
    private ArrayList<Move> setBurnedLocForGAPM(ArrayList<Move> allMoves)
    {
        Move eatMove = new Move();

        for(Move posMove : allMoves)
        {
            if(posMove.isEat())
            {
                eatMove = new Move(posMove);
                //eatMove.copyMove(posMove);
                break;
            }
        }

        ArrayList<Move> movesToSet = allMoves;

        Location eatSource = eatMove.getSource(), eatDest = eatMove.getDestination();
        for(Move posMove : movesToSet)
        {

            if(eatMove.getPiece() == null) // eat isn't exists
            {
                break;
            }

            Location posSource = posMove.getSource(),
                    posDest = posMove.getDestination();
            if(!posMove.equals(eatMove) && !posMove.isEat())
            {
                //if instead of eat the piece did another move
                if(posSource.isEqual(eatSource)
                        && !posDest.isEqual(eatDest))
                {
                    posMove.setBurnedLoc(posDest); // delete piece from dest
                }

                if(!posSource.isEqual(eatSource)) //if another piece did a move
                {
                    posMove.setBurnedLoc(eatSource); // delete piece that could eat
                }
            }
        }

        return movesToSet;
    }

    /**
     * updates burnedLoc for ArrayList of moves (if exists) for logicBoard
     *
     * @param pos
     * @param player
     * @return
     */
    public ArrayList<Move> setBurnedLocForAllMoves(ArrayList<Move> pos, PieceColor player)
    {
        return setBurnedLocForAllMoves(logicBoard, player, pos);
    }

    /**
     * updates burnedLoc for ArrayList of moves (if exists) for specific board
     *
     * @param board
     * @param pos
     * @param player
     * @return updated ArrayList with burnedLoc for all moves
     */
    public ArrayList<Move> setBurnedLocForAllMoves(Board board, PieceColor player, ArrayList<Move> pos)
    {
//        for(Move move : pos)
//        {
//            System.out.println("pos: source:" + move.getSource()
//                    + ", dest: " + move.getDestination() + ", eat: " + move.isEat());
//        }

        //System.out.println("------------------ in setBurnedLocAllMoves() ------------------");
        Move eatMove = new Move();
        ArrayList<Move> pieceMoves = new ArrayList<>();
        ArrayList<Move> allMoves = new ArrayList<>();

        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Location loc = new Location(i, j);
                Piece p = pieceInLoc(board, loc);
                if(p != null && p.getPieceColor() == player)
                {
                    pieceMoves = simplePossibleMovesForPiece(board, loc);
                    allMoves.addAll(pieceMoves);
                }
            }
        }

//        System.out.println("");
//        for(Move move : pos)
//        {
//            System.out.println("pos after loop: " + move);
//        }
        for(Move posMove : allMoves)
        {
            //System.out.println("search for move with eat: " + posMove);
            if(posMove.isEat())
            {
                eatMove = new Move(posMove);
                //eatMove.copyMove(posMove);
                break;
            }
        }

//        for(Move move : allMoves)
//        {
//            System.out.println("allMoves after loop: " + move);
//        }
//
        ArrayList<Move> movesToSet = allMoves;
//
//        System.out.println("eatMove: " + eatMove);
//
////        if(eatMove.getPiece() == null)
////        {
////            return null;
////        }
//        System.out.println("eatMove: source:" + eatMove.getSource()
//                + ", dest: " + eatMove.getDestination() + ", eat: " + eatMove.isEat());
//        for(Move move : movesToSet)
//        {
//            System.out.println("\nmovesToSet before loop: source:" + move.getSource()
//                    + ", dest: " + move.getDestination() + "Eat: " + move.isEat());
//        }
        Location eatSource = eatMove.getSource(), eatDest = eatMove.getDestination();
        for(Move posMove : movesToSet)
        {

            if(eatMove.getPiece() == null) // eat isn't exists
            {
                break;
            }

            //System.out.println("movesToSet in loop: " + posMove);
            Location posSource = posMove.getSource(),
                    posDest = posMove.getDestination();
            //System.out.println("posSource: " + posSource + ", eatSource: " + eatSource);
            if(!posMove.equals(eatMove) && !posMove.isEat())
            {
                //if instead of eat the piece did another move
                if(posSource.isEqual(eatSource)
                        && !posDest.isEqual(eatDest))
                {
                    //System.out.println("pos like eatSource! delete from: " + posDest);
                    posMove.setBurnedLoc(posDest); // delete piece from dest
                    //System.out.println("final move now: " + posMove);
                }

                if(!posSource.isEqual(eatSource)) //if another piece did a move
                {
                    //System.out.println("delete from eatSource! loc: " + eatSource);
                    posMove.setBurnedLoc(eatSource); // delete piece that could eat
                    //System.out.println("final move now: " + posMove);
                }
            }
        }

//        for(Move move : movesToSet)
//        {
//            System.out.println("finalMove: " + move);
//        }
        return movesToSet;
    }

    /**
     * Checks if in the end of the move the piece will be king
     *
     * @param move
     * @return true if the piece that did move is king, false else
     */
//    public boolean isKing(Move move)
//    {
//        return move.getPiece().isKing();
//    }
    /**
     * Checks if on logicBoard the game over with tie
     *
     * @return true if the result of logic board is tie, false else
     */
    public boolean checkTie()
    {
        return checkTie(logicBoard);
    }

    /**
     * Checks if on specific board game over with tie
     *
     * @param board
     * @return true if the result of specific board is tie, false else
     */
    public boolean checkTie(Board board)
    {
        if(countKings(board, WHITE_PLAYER) == countKings(board, BLACK_PLAYER)
                && countOrdinaryPieces(board, WHITE_PLAYER) == 0
                && countOrdinaryPieces(board, BLACK_PLAYER) == 0)
        {
            if(numMoves == 5)
            {
                numMoves = 0;
                return true;
            }

            else
            {
                numMoves++;
                return false;
            }
        }

        else
        {
            return false;
        }
    }

    /**
     *
     * @param playerSign
     * @return true if the result of logic board is win to playerSign, false
     * else
     */
    public boolean checkWin(PieceColor playerSign)
    {
        return checkWin(logicBoard, playerSign);
    }

    /**
     * Checks if on specific board game over with win
     *
     * @param board
     * @param playerSign
     * @return true if the result of specific board is win to playerSign, false
     * else
     */
    public boolean checkWin(Board board, PieceColor playerSign)
    {
        int counterWhite = countAllPieces(board, WHITE_PLAYER);
        int counterBlack = countAllPieces(board, BLACK_PLAYER);

        if((playerSign == WHITE_PLAYER && counterWhite > 0 && counterBlack == 0)
                || (playerSign == BLACK_PLAYER && counterBlack > 0 && counterWhite == 0))
        {
            return true;
        }

        return playerCantMove(board, getOpponent(playerSign));
    }

    /**
     * Gets the opponent of player
     *
     * @param player that the func returns his opponent
     * @return the opponent of player
     */
    private PieceColor getOpponent(PieceColor player)
    {
        if(player == WHITE_PLAYER)
        {
            return BLACK_PLAYER;
        }
        return WHITE_PLAYER;
    }

    /**
     * Gets AI move
     *
     * @param player that func serch for him move using minimax
     * @param minimaxTImeOutSec time that the func gives to minimax to search
     * move
     * @return minimax move
     */
    public MinimaxMove getAiMove(PieceColor player, int minimaxTimeOutSec)
    {
        System.out.println("Model getAiMove() using MINIMAX");

        MinimaxMove bestMove = getBestMoveUsingMinimax(player, minimaxTimeOutSec);

        return bestMove;
    }

    // ============================ for minimax ===========================
    private MinimaxMove getBestMoveUsingMinimax(PieceColor player, int minimaxTimeOutSec)
    {
        int maxDepth = 1;
        MinimaxMove minimaxMove = new MinimaxMove();
        MinimaxMove lastMove = new MinimaxMove();
        long minimaxTime = System.currentTimeMillis() + (minimaxTimeOutSec/*11*/ * 1000);
        long iterateTime = 0;
        while(System.currentTimeMillis() < minimaxTime
                /* if  time left more than last iterate time plus sec*/
                && minimaxTime - System.currentTimeMillis() >= (iterateTime + 1000))
        {
            long startIterate = System.currentTimeMillis();
            lastMove = new MinimaxMove(minimaxMove);
            minimaxMove = minimax(player, logicBoard, true, 0, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);

            System.out.println("bestMove for depth " + maxDepth + " = " + minimaxMove);
            iterateTime = System.currentTimeMillis() - startIterate;
            System.out.println("iterate time for depth " + maxDepth + ": " + iterateTime + " milis");
            maxDepth++;
            if(maxDepth >= 8)
            {
                iterateTime *= 10;
            }
            else
            {
                iterateTime *= 2;
            }

            if(minimaxMove.getPiece() == null)
            {
                break;
            }

            if(minimaxMove.getGrade() == 100)
            {
                break;
            }
        }

        if(minimaxMove.isEat() && minimaxMove.getBurnedLoc() != null)
        {
            minimaxMove.setBurnedLoc(new Location(0, 0));
        }

        if(minimaxMove.getPiece() == null)
        {
            System.out.println("final minimax lastMove=" + minimaxMove);
            return lastMove;
        }

        else
        {
            System.out.println("final minimax move=" + minimaxMove);
            return minimaxMove;
        }
    }

    /**
     * Gets best move by minimax (for AI player)
     *
     * @param player the player the func search for him a move
     * @param board the board that func check on him a move
     * @param isMax if func search max or min grade (current player or his
     * opponent)
     * @param depth the depth that we want to stop the recursion
     * @param alpha to check if need to do alpha pruning
     * @param beta to check if need to do beta pruning
     * @return the best move to make that could be found to a specific depth
     */
    private MinimaxMove minimax(PieceColor player, Board board, boolean isMax,
            int depth, int maxDepth, int alpha, int beta)
    {
        MinimaxMove bestMove = new MinimaxMove();

        if(depth == maxDepth || isGameOver(board))
        {
            bestMove.setGrade(eval(board, player));
            bestMove.setDepth(depth);
            //System.out.println(bestMove);
            return bestMove;
        }

        if(!isMax)
        {
            player = getOpponent(player);
        }

        ArrayList<Move> possibleMoves = getAllPossibleMoves(board, player);
//        System.out.println("GAPM in minimax:");
//        for(Move move : possibleMoves)
//        {
//            System.out.println(move);
//        }

        if(isMax)
        {
            bestMove.setGrade(Integer.MIN_VALUE);

            for(int i = 0; i < possibleMoves.size(); i++)
            {
                Move move = possibleMoves.get(i);
                //System.out.println(move);
                Board newBoard = new Board(board);
                newBoard = makeMoveOnCopyBoard(newBoard, move);
                MinimaxMove minimaxMove;
                if(!move.getSecondMove().isEmpty())
                {
                    minimaxMove = minimax(player, newBoard, true, depth, maxDepth, alpha, beta);
                }

                else
                {
                    minimaxMove = minimax(player, newBoard, false, depth + 1, maxDepth, alpha, beta);
                }

                if(depth == 0)
                {
                    minimaxMove.copyMove(move);
//                    System.out.println("$$$$ " + minimaxMove);
                }

                if(minimaxMove.isDepthLegit()
                        && (minimaxMove.getGrade() > bestMove.getGrade()
                        || (minimaxMove.getGrade() == bestMove.getGrade()
                        && minimaxMove.getDepth() < bestMove.getDepth())))
                {
                    //System.out.println("minimaxMove: " + minimaxMove);
                    bestMove = new MinimaxMove(minimaxMove);
                    //bestMove.copyMinimaxMove(minimaxMove);
                }

                alpha = Math.max(bestMove.getGrade(), alpha);
                if(beta <= alpha)
                {
                    break;
                }
            }
        }

        else
        {
            bestMove.setGrade(Integer.MAX_VALUE);
            for(int i = 0; i < possibleMoves.size(); i++)
            {
                Move move = possibleMoves.get(i);
                Board newBoard = new Board(board);
                newBoard = makeMoveOnCopyBoard(newBoard, move);
                MinimaxMove minimaxMove;
                if(!move.getSecondMove().isEmpty())
                {
                    minimaxMove = minimax(player, newBoard, false, depth, maxDepth, alpha, beta);
                }

                else
                {
                    minimaxMove = minimax(player, newBoard, true, depth + 1, maxDepth, alpha, beta);
                }

                if(depth == 0)
                {
                    minimaxMove.copyMove(move);
                    //System.out.println("$$$$ " + minimaxMove);
                }

                if(minimaxMove.isDepthLegit()
                        && (minimaxMove.getGrade() < bestMove.getGrade()
                        || (minimaxMove.getGrade() == bestMove.getGrade()
                        && minimaxMove.getDepth() < bestMove.getDepth())))
                {
                    //System.out.println("minimaxMove: " + minimaxMove);
                    bestMove = new MinimaxMove(minimaxMove);
                    //bestMove.copyMinimaxMove(minimaxMove);
                }

                beta = Math.min(bestMove.getGrade(), beta);
                if(beta <= alpha)
                {
                    break;
                }
            }
        }

        return bestMove;
    }

    /**
     * Gets evaluation for player on a logic board
     *
     * @param player
     * @return evaluation grade for player on logic board
     */
    public int eval(PieceColor player)
    {
        return eval(logicBoard, player);
    }

    /**
     * Gets grade to player for specific board
     *
     * @param board the board the func evaluate
     * @param player the player for him the board evaluated
     * @return evaluation for player on a specific board
     */
    public int eval(Board board, PieceColor player)
    {
        int evalScore = 50;
        PieceColor opponent = getOpponent(player);
        if(checkWin(board, player))
        {
            return 100;
        }

        if(checkWin(board, opponent))
        {
            return -100;
        }

        else
        {
            ArrayList<Move> allMoves = getAllPossibleMoves(board, player);
            ArrayList<Move> opponentMoves = getAllPossibleMoves(board, opponent);

            int sub = subCountOrdinaryPieces(board, player);
            evalScore += (sub * 3);

            sub = subCountKings(board, player);
            evalScore += (sub * 8);

            boolean currentOneMoveKing = oneMoveKingForCurrent(player, allMoves, opponentMoves),
                    opponentOneMoveKing = oneMoveKingForOpponent(opponent, opponentMoves);
            if(currentOneMoveKing)
            {
                evalScore += 6;
            }

            if(opponentOneMoveKing)
            {
                evalScore -= 6;
            }

            sub = subPiecesOnFirstLine(board, player);
            evalScore += (sub * 2);

            sub = subOfPiecesInSides(board, player);
            evalScore += (sub * 1);

            sub = subOfMovablePieces(player, board);
            evalScore += (sub * 1);

//            boolean opponentOneMoveEaten = ordinaryCouldBeEatenNextMove(board, opponentMoves),
//                    opponentKingOneMoveEaten = kingsCouldBeEatenNextMove(board, opponentMoves);
            int /*currentMultEating = GradeForMultEatingCurrent(board, allMoves, opponentMoves),*/ opponentMultEating = GradeForMultEatingOpponent(board, opponentMoves);
//            if(opponentOneMoveEaten && !opponentKingOneMoveEaten && opponentMultEating == 0)
//            {
//                evalScore -= 2; // ordinary could eaten
//            }
//
//            if(((opponentKingOneMoveEaten && opponentOneMoveEaten)
//                    || (opponentKingOneMoveEaten && !opponentOneMoveEaten))
//                    && opponentMultEating == 0)
//            {
//                evalScore -= 7; // king could eaten
//            }

            //evalScore += currentMultEating;
            evalScore -= opponentMultEating;
        }
        return evalScore;
    }

    public int evalWithPrints(Board board, PieceColor player)
    {
        System.out.println("--------------- EVAL for " + player + "---------------");
        int evalScore = 50;
        PieceColor opponent = getOpponent(player);
        if(checkWin(board, player))
        {
            System.out.println("win for " + player);
            return 100;
        }

        if(checkWin(board, opponent))
        {
            System.out.println("lose for " + player);
            return -100;
        }

        else
        {
            System.out.println("calculate the grade:");
            ArrayList<Move> allMoves = getAllPossibleMoves(board, player);
            ArrayList<Move> opponentMoves = getAllPossibleMoves(board, opponent);

            int sub = subCountOrdinaryPieces(board, player);
            evalScore += (sub * 3);
            System.out.println("sub of ordinaries: " + sub + "\ngrade now: " + evalScore);

            sub = subCountKings(board, player);
            evalScore += (sub * 8);
            System.out.println("sub of kings: " + sub + "\ngrade now: " + evalScore);

//            boolean currentOneMoveKing = oneMoveKingForCurrent(player, allMoves, opponentMoves),
//                    opponentOneMoveKing = oneMoveKingForOpponent(opponent, opponentMoves);
//            if(currentOneMoveKing)
//            {
//                evalScore += 7;
//                System.out.println("you have one move king!\ngrade now: " + evalScore);
//            }
//
//            if(opponentOneMoveKing)
//            {
//                evalScore -= 7;
//                System.out.println("opponent one move king!\ngrade now: " + evalScore);
//            }
            sub = subPiecesOnFirstLine(board, player);
            evalScore += (sub * 2);
            System.out.println("sub of first line: " + sub + "\ngrade now: " + evalScore);

            sub = subOfPiecesInSides(board, player);
            evalScore += (sub * 1);
            System.out.println("sub of pieces in sides: " + sub + "\ngrade now: " + evalScore);

            sub = subOfMovablePieces(player, board);
            evalScore += (sub * 1);
            System.out.println("sub of movable pieces: " + sub + "\ngrade now: " + evalScore);

//            boolean opponentOneMoveEaten = ordinaryCouldBeEatenNextMove(board, opponentMoves),
//                    opponentKingOneMoveEaten = kingsCouldBeEatenNextMove(board, opponentMoves);
            int currentMultEating = GradeForMultEatingCurrent(board, allMoves, opponentMoves),
                    opponentMultEating = GradeForMultEatingOpponent(board, opponentMoves);

//            if(opponentOneMoveEaten && !opponentKingOneMoveEaten && opponentMultEating == 0)
//            {
//                evalScore -= 2;
//                System.out.println("ordinary could be eaten!\ngrade now: " + evalScore);
//            }
//
//            if(((opponentKingOneMoveEaten && opponentOneMoveEaten)
//                    || (opponentKingOneMoveEaten && !opponentOneMoveEaten))
//                    && opponentMultEating == 0)
//            {
//                evalScore -= 7;
//                System.out.println("king could be eaten!\ngrade now: " + evalScore);
//            }
            if(currentMultEating != 0)
            {
                evalScore += currentMultEating;
                System.out.println("you has mult eating!\ngrade now: " + evalScore);
            }

            if(opponentMultEating != 0)
            {
                evalScore -= opponentMultEating;
                System.out.println("opponent has mult eating!\ngrade now: " + evalScore);
            }
        }
        return evalScore;
    }

    private int subOfMovablePieces(PieceColor player, Board board)
    {
        int counterWhite = 0, counterBlack = 0;
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(!board.isEmptyTile(new Location(i, j)))
                {
                    Piece p = new Piece(board.getPiece(new Location(i, j)));
                    if(p.isWhite() && canDoMove(p))
                    {
                        counterWhite++;
                    }

                    if(p.isWhite() && canDoMove(p))
                    {
                        counterBlack++;
                    }
                }
            }
        }
        if(player == WHITE_PLAYER)
        {
            return counterWhite - counterBlack;
        }
        return counterBlack - counterWhite;
    }

    private int piecesOnFirstLine(Board board, PieceColor player)
    {
        int counter = 0;
        if(player == WHITE_PLAYER)
        {
            for(int i = 0; i < COLS; i++)
            {
                Location loc = new Location(ROWS - 1, i);
                if(!board.isEmptyTile(loc))
                {
                    Piece piece = board.getPiece(loc);
                    if(piece.getPieceColor() == player)
                    {
                        counter++;
                    }
                }
            }
        }

        if(player == BLACK_PLAYER)
        {
            for(int i = 0; i < COLS; i++)
            {
                Location loc = new Location(0, i);
                if(!board.isEmptyTile(loc))
                {
                    Piece piece = board.getPiece(loc);
                    if(piece.getPieceColor() == player)
                    {
                        counter++;
                    }
                }
            }
        }

        return counter;
    }

    //
    private int subPiecesOnFirstLine(Board board, PieceColor player)
    {
        PieceColor opponent = getOpponent(player);
        return piecesOnFirstLine(board, player) - piecesOnFirstLine(board, opponent);
    }

    // calculates the sub pieces that placed on the sides right and left
    // of board between player and his opponent
    private int subOfPiecesInSides(Board board, PieceColor player)
    {
        int counterWhite = 0, counterBlack = 0;
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(!board.isEmptyTile(new Location(i, j)) && (j == 0 || j == COLS - 1))
                {
                    if(board.getPiece(i, j).isWhite())
                    {
                        counterWhite++;
                    }
                    else
                    {
                        counterBlack++;
                    }
                }
            }
        }
        if(player == WHITE_PLAYER)
        {
            return counterWhite - counterBlack;
        }
        else
        {
            return counterBlack - counterWhite;
        }
    }

//    private boolean isMultEat(Board board, PieceColor player)
//    {
//        ArrayList<Move> posMoves = getAllPossibleMoves(board, player);
//        for(Move pos : posMoves)
//        {
//            if(pos.isEat())
//                return true;
//        }
//    }
    // Returns if piece of current player can make king next turn
    private boolean oneMoveKingForCurrent(PieceColor player, ArrayList<Move> currentMoves,
            ArrayList<Move> opponentMoves)
    {
        Move OMK = new Move();
        for(Move move : currentMoves)
        {
            if(move.getDestination().getRow() == 0 && player == WHITE_PLAYER
                    || move.getDestination().getRow() == ROWS - 1 && player == BLACK_PLAYER)
            {
                OMK = new Move(move);
                break;
            }

            if(move.hasNext())
            {
                for(Move mult : move.getSecondMove())
                {
                    if(mult.getDestination().getRow() == 0 && player == WHITE_PLAYER
                            || mult.getDestination().getRow() == ROWS - 1 && player == BLACK_PLAYER)
                    {
                        return true;
                    }
                }
            }
        }

        if(OMK.getPiece() != null)
        {
            for(Move move : opponentMoves)
            {
                if(move.isEat())
                {
                    if(move.getEnemyLoc().isEqual(OMK.getSource()))
                    {
                        return false;
                    }
                }
            }
            return true;
        }

        else
        {
            return false;
        }
    }

    // Returns if opponent can make king next turn
    private boolean oneMoveKingForOpponent(PieceColor opponent, ArrayList<Move> opponentMoves)
    {
        for(Move move : opponentMoves)
        {
            Piece piece = move.getPiece();
            int rowDest = move.getDestination().getRow();

            if(piece.getPieceColor() == WHITE_PLAYER && !piece.isKing()
                    && rowDest == 0)
            {
                return true;
            }

            if(!piece.isKing() && piece.getPieceColor() == BLACK_PLAYER
                    && rowDest == ROWS - 1)
            {
                return true;
            }

            if(move.hasNext())
            {
                for(Move mult : move.getSecondMove())
                {
                    if(mult.getDestination().getRow() == 0 && opponent == WHITE_PLAYER
                            || mult.getDestination().getRow() == ROWS - 1 && opponent == BLACK_PLAYER)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //returns the amont of ordinary pieces of player that could be eaten next
    // move of the opponent
    private boolean ordinaryCouldBeEatenNextMove(Board board, ArrayList<Move> opponentMoves)
    {
        for(Move enemyMove : opponentMoves)
        {
            if(enemyMove.isEat())
            {
                Piece enemy = pieceInLoc(board, enemyMove.getEnemyLoc());
                if(!enemy.isKing())
                {
                    return true;
                }
            }
        }

        return false;
    }

    // return the amont of king pieces of player that could be eaten next move
    // of the opponent
    private boolean kingsCouldBeEatenNextMove(Board board, ArrayList<Move> opponentMoves)
    {
        for(Move enemyMove : opponentMoves)
        {
            if(enemyMove.isEat())
            {
                Piece enemy = pieceInLoc(board, enemyMove.getEnemyLoc());
                if(enemy.isKing())
                {
                    return true;
                }
            }
        }

        return false;
    }

    // returns the amount of player pieces on specific board
    private int countOrdinaryPieces(Board board, PieceColor player)
    {
        int counter = 0;
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Location loc = new Location(i, j);
                Piece p = board.getPiece(loc);
                if(p != null && p.getPieceColor() == player
                        && !p.isKing())
                {
                    counter++;
                }
            }
        }

        return counter;
    }

    // returns the sub of countOrdinaryPieces between
    // player and his opponent
    private int subCountOrdinaryPieces(Board board, PieceColor player)
    {
        PieceColor opponent = getOpponent(player);
        return countOrdinaryPieces(board, player) - countOrdinaryPieces(board, opponent);
    }

    /**
     *
     * @param board
     * @param player
     * @return the amount of ordinary pieces (no kings) that player has on
     * specific board
     */
//    private int countOrdinaryTroops(Board board, PieceColor player)
//    {
//        int counter = 0;
//        for(int i = 0; i < ROWS; i++)
//        {
//            for(int j = 0; j < COLS; j++)
//            {
//                Location loc = new Location(i, j);
//                Piece p = board.getPiece(loc);
//                if(p != null && p.getPieceColor() == player && !p.isKing())
//                {
//                    counter++;
//                }
//            }
//        }
//
//        return counter;
//    }
    // Returns the amount of player kings on specific board
    private int countKings(Board board, PieceColor player)
    {
        int counter = 0;
        {
            for(int i = 0; i < ROWS; i++)
            {
                for(int j = 0; j < COLS; j++)
                {
                    Piece p = board.getPiece(i, j);
                    if(p != null)
                    {
                        if(p.getPieceColor() == player && p.isKing() && canDoMove(p))
                        {
                            counter++;
                        }
                    }
                }
            }
        }
        return counter;
    }

    // returns the sub of kings between player
    // and his opponent
    private int subCountKings(Board board, PieceColor player)
    {
        PieceColor opponent = getOpponent(player);
        return countKings(board, player) - countKings(board, opponent);
    }

    // count all pieces of player on board
    private int countAllPieces(Board board, PieceColor player)
    {
        return countOrdinaryPieces(board, player) + countKings(board, player);
    }

    // Returns ArrayList of locations that ordinary piece
    //on loc on specific board can move there
    private ArrayList<Move> getlegalSimpleMoves(Board board, Location loc)
    {
        ArrayList<Location> potentialLocs = new ArrayList<>();
        ArrayList<Move> legalMoves = new ArrayList<>();

        Piece p = board.getPiece(loc);
        int row = loc.getRow(), col = loc.getCol();
        if(p.isWhite())
        {
            potentialLocs.add(new Location(row - 1, col + 1));
            potentialLocs.add(new Location(row - 1, col - 1));
        }

        if(!p.isWhite())
        {
            potentialLocs.add(new Location(row + 1, col + 1));
            potentialLocs.add(new Location(row + 1, col - 1));
        }

        for(int i = 0; i < potentialLocs.size(); i++)
        {
            Location currentLoc = potentialLocs.get(i);

            if(currentLoc.isInBounds() && board.getPiece(currentLoc) == null)
            {
                Move move = new Move(p, loc, currentLoc, false);
                legalMoves.add(move);
            }
        }

        ArrayList<Move> eats = getlegalEatMoves(board, loc);
        if(!eats.isEmpty())
        {
            legalMoves.addAll(eats);
        }
        return legalMoves;
    }

    /**
     *
     * @param board
     * @param loc
     * @
     */
    // return ArrayList of locations that include eats that  ordinary
    //piece on loc on specific board can move there
    private ArrayList<Move> getlegalEatMoves(Board board, Location loc)
    {
        ArrayList<Location> potentialLocs = new ArrayList<>();
        ArrayList<Move> legalMoves = new ArrayList<>();
        Piece piece = board.getPiece(loc);
        int row = loc.getRow(), col = loc.getCol();
        if(piece.isWhite())
        {
            potentialLocs.add(new Location(row - 1, col + 1));
            potentialLocs.add(new Location(row - 2, col + 2));
            potentialLocs.add(new Location(row - 1, col - 1));
            potentialLocs.add(new Location(row - 2, col - 2));
        }

        if(!piece.isWhite())
        {
            potentialLocs.add(new Location(row + 1, col + 1));
            potentialLocs.add(new Location(row + 2, col + 2));
            potentialLocs.add(new Location(row + 1, col - 1));
            potentialLocs.add(new Location(row + 2, col - 2));
        }

        for(int i = 0; i < potentialLocs.size(); i += 2)
        {
            Location potentialEnemyLoc = potentialLocs.get(i),
                    potentialEmptyLoc = potentialLocs.get(i + 1);

            if(piece != null)
            {
                if(potentialEnemyLoc.isInBounds() && potentialEmptyLoc.isInBounds())
                {
                    Piece emptyPotential = board.getPiece(potentialEmptyLoc);
                    Piece enemyPotential = board.getPiece(potentialEnemyLoc);
                    if(piece.isWhite() && enemyPotential != null)
                    {
                        if(emptyPotential == null && !enemyPotential.isWhite())
                        {
                            Move move = new Move(piece, loc, potentialEmptyLoc, true, potentialEnemyLoc);
                            legalMoves.add(move);
                        }
                    }

                    if(!piece.isWhite() && enemyPotential != null)
                    {
                        if(enemyPotential.isWhite() && emptyPotential == null)
                        {
                            Move move = new Move(piece, loc, potentialEmptyLoc, true, potentialEnemyLoc);
                            legalMoves.add(move);
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    /**
     * Gets all possible moves that player with pieceColor playerSign can do on
     * specific board
     *
     * @param board board that search on him moves to player
     * @param playerSign player for him the func search moves
     * @return all possible moves for a player on specific board
     */
    public ArrayList<Move> getAllPossibleMoves(Board board, PieceColor playerSign)
    {
        //System.out.println("----------------- in getAllPossibleMoves -----------------------");
        //System.out.println("playerSign: " + playerSign);
        ArrayList<Move> allMoves = new ArrayList<>();

        for(int row = 0; row < ROWS; row++)
        {
            for(int col = 0; col < COLS; col++)
            {
                Location loc = new Location(row, col);
                Piece p = board.getPiece(loc);
                if(p != null)
                {
                    if(p.getPieceColor() == playerSign)
                    {
                        ArrayList<Move> posMoves = possibleMovesForPiece(board, loc, false);
                        allMoves.addAll(posMoves);
                        posMoves.clear();
                    }
                }
            }
        }
        allMoves = setBurnedLocForGAPM(allMoves);
        return allMoves;
    }

// Checks if we have move that include eating (help func)
//    private boolean searchForEat(ArrayList<Move> moves)
//    {
//        for(Move temp : moves)
//        {
//            if(temp.isEat())
//            {
//                return true;
//            }
//        }
//        return false;
//    }
//    private Move searchForBurned(ArrayList<Move> moves)
//    {
//        Move burned = new Move();
//        for(Move temp : moves)
//        {
//            if(temp.isEat())
//            {
//                burned.copyMove(temp);
//                break;
//            }
//        }
//
//        if(burned.getPiece() == null)
//        {
//            return null;
//        }
//
//        for(Move move : moves)
//        {
//            if(!move.isEat())
//            {
//                return burned;
//            }
//        }
//
//        return null;
//    }
//    private void setBurnedForList(ArrayList<Move> moves, Move burned)
//    {
//        for(Move move : moves)
//        {
//            if(!move.isEat())
//            {
//                if(!move.equals(burned))
//                {
//                    move.setBurnedLoc(burned.getSource());
//                }
//                else
//                {
//                    move.setBurnedLoc(burned.getDestination());
//                }
//            }
//        }
//    }
    /**
     *
     * @return true if the game is over (win or tie) on logic board, false else
     */
//    private boolean isGameOver()
//    {
//        return isGameOver(logicBoard);
//    }
    // return true if the game is over (win or tie)
    // on logic board, false else
    private boolean isGameOver(Board board)
    {
        boolean winForWhite = checkWin(board, WHITE_PLAYER),
                winForBlack = checkWin(board, BLACK_PLAYER),
                isTie = checkTie(board);

        if(winForWhite || winForBlack || isTie)
        {
            return true;
        }

        return false;
    }

    /**
     *
     * @param move
     * @return true if specific move is legal, false else
     */
//    public boolean isLegal(Move move)
//    {
//        Location dest = move.getDestination();
//        ArrayList<Move> posMoves = possibleMovesForPiece(move.getSource(), true);
//        for(Move pos : posMoves)
//        {
//            Location posDest = pos.getDestination();
//            if(dest.equals(posDest))
//            {
//                return true;
//            }
//        }
//
//        return false;
//    }
    /**
     *
     * @param loc
     * @return all enemy location for player on loc on logic board
     */
//    public ArrayList<Location> getEnemyLocations(Location loc)
//    {
//        return getEnemyLocations(logicBoard, loc);
//    }
    /**
     *
     * @param board
     * @param loc
     * @return all enemy location for player on loc on specific board
     */
//    private ArrayList<Location> getEnemyLocations(Board board, Location loc)
//    {
//        ArrayList<Location> potentialLocs = new ArrayList<>();
//        ArrayList<Location> finalLocs = new ArrayList<>();
//        Piece p = board.getPiece(loc);
//        int row = loc.getRow(), col = loc.getCol();
//        if(p != null)
//        {
//            if(p.isWhite() || (!p.isWhite() && p.isKing()))
//            {
//                potentialLocs.add(new Location(row - 1, col + 1));
//                potentialLocs.add(new Location(row - 1, col - 1));
//            }
//
//            if(!p.isWhite() || (p.isWhite() && p.isKing()))
//            {
//                potentialLocs.add(new Location(row + 1, col + 1));
//                potentialLocs.add(new Location(row + 1, col - 1));
//            }
//
//            for(int i = 0; i < potentialLocs.size(); i++)
//            {
//                Location enemyLoc = potentialLocs.get(i);
//
//                if(enemyLoc.isInBounds())
//                {
//                    Piece EnemyPlayer = board.getPiece(enemyLoc);
//                    if(EnemyPlayer != null)
//                    {
//                        if(p.isWhite() && !EnemyPlayer.isWhite())
//                        {
//                            finalLocs.add(enemyLoc);
//                        }
//                        if(!p.isWhite() && EnemyPlayer.isWhite())
//                        {
//                            finalLocs.add(enemyLoc);
//                        }
//                    }
//                }
//            }
//        }
//        return finalLocs;
//    }
    /**
     *
     * @param loc
     * @return all moves that piece on loc can make on logic board
     */
//    public ArrayList<Move> possibleMovesForPiece(Location loc, boolean checkBurned)
//    {
//        return possibleMovesForPiece(logicBoard, loc, checkBurned);
//    }
    /**
     * Returns all moves that piece on loc on specific board can do
     *
     * @param board
     * @param loc
     * @param checkBurned
     * @return all moves that piece on loc can make on specific board
     */
    public ArrayList<Move> possibleMovesForPiece(Board board, Location loc, boolean checkBurned)
    {
//        System.out.println("board:");
//        board.printBoard();
        //System.out.println("loc: " + loc);
        //System.out.println("-------------------- in possibleMovesForPiece -----------------------");
        ArrayList<Move> legalMoves = simplePossibleMovesForPiece(board, loc);

        Piece piece = board.getPiece(loc);
        //System.out.println("piece: " + piece);
        if(checkBurned)
        {
            //System.out.println("board: ");
            //board.printBoard();
            //System.out.println("\npieceColor: " + piece.getPieceColor());
            //System.out.println("legalMoves: " + legalMoves);
//            System.out.println("legal moves in PMFP:");
//            for(Move move : legalMoves)
//            {
//                System.out.println("legal: " + move);
//            }
            //System.out.println("in PMFP: board:");
            //board.printBoard();
            //System.out.println("player: " + piece.getPieceColor());
            //System.out.println("legalMoves: " + legalMoves);
            legalMoves = setBurnedLocForAllMoves(board, piece.getPieceColor(), legalMoves);
        }

        for(Move legal : legalMoves)
        {
            //Piece legalPiece = legal.getPiece();
            //Location legalLoc = legalPiece.getPieceLoc();
            if(legal.isEat())
            {
                //System.out.println("move before multEating: " + legal);
                ArrayList<Move> mult = getMultEating(board, legal);
                if(!mult.isEmpty())
                {
                    //System.out.println("mult isn't empty!");
                    legal.setSecondMove(mult);
                    //System.out.println("legal seconds: " + legal.getSecondMove());
                    //System.out.println("mult after update: " + legal);
                }
            }

//            if(legalPiece.getPieceLoc() != legal.getSource())
//            {
//                legal.getPiece().setPieceLoc(legal.getSource());
//            }
//            if(legalPiece.isKing() && legalPiece.isWhite() && legalLoc.getRow() != 0)
//            {
//                legal.getPiece().setKing(false);
//            }
//            if(!legalPiece.isKing() && !legalPiece.isWhite() && legalLoc.getRow() != 7)
//            {
//                legal.getPiece().setKing(false);
//            }
        }

//        System.out.println("final PMFP list: ");
//        for(Move move : legalMoves)
//        {
//            System.out.println(move);
//        }
//        System.out.println("moves after getMultEating():");
//        for(int i = 0; i < legalMoves.size(); i++)
//        {
//            Move moves = legalMoves.get(i);
//            System.out.println("move #" + i + ": " + moves);
//        }
        return legalMoves;
    }

    // get simple moves that piece on loc can do
    // (without check burnedLoc and second moves)
    public ArrayList<Move> simplePossibleMovesForPiece(Board board, Location loc)
    {
        ArrayList<Move> legalMoves = new ArrayList<>();
        Piece piece = board.getPiece(loc);

        if(piece != null)
        {
            if(piece.isKing())
            {
                legalMoves = possibleMovesForKing(board, loc);
            }
            else
            {
                legalMoves = getlegalSimpleMoves(board, loc);
            }
        }
        return legalMoves;
    }

    // Returns all the moves that specific king on loc can do
    private ArrayList<Move> possibleMovesForKing(Board board, Location loc)
    {
        ArrayList<Move> legalMoves = upperRightDiagonal(board, loc);
        ArrayList<Move> Move1 = upperLeftDiagonal(board, loc);
        ArrayList<Move> Move2 = lowerRightDiagonal(board, loc);
        ArrayList<Move> Move3 = lowerLeftDiagonal(board, loc);
        legalMoves.addAll(Move1);
        legalMoves.addAll(Move2);
        legalMoves.addAll(Move3);
        return legalMoves;
    }

    // returns Arraylist of legal moves on the upper right diagonal
    // (for kings)
    private ArrayList<Move> upperRightDiagonal(Board board, Location loc)
    {
        int row = loc.getRow(), col = loc.getCol();
        Piece piece = board.getPiece(loc);
        Location opponentLoc = null;
        ArrayList<Move> posMoves = new ArrayList<>();
        boolean isEat = false;
        PieceColor player = piece.getPieceColor();
        for(int i = row - 1, j = col + 1; new Location(i, j).isInBounds(); i--, j++) // לבדוק ולפי זה לשכפל את הלולאה
        {
            Location tempLoc = new Location(i, j);
            Location nextLoc = new Location(i - 1, j + 1);
            Move legalMove = new Move(piece, loc, tempLoc, isEat, opponentLoc);
            Piece tmpPiece = board.getPiece(tempLoc);
            if(tempLoc.isInBounds())
            {
                if(tmpPiece == null) // מיקום פנוי למוב
                {
                    posMoves.add(legalMove);
                    continue;
                }
                if(tmpPiece != null)
                {
                    if(nextLoc.isInBounds())
                    {
                        if(board.getPiece(nextLoc) == null
                                && tmpPiece.getPieceColor() != player
                                && !isEat) // סיטואציית אכילה
                        {
                            opponentLoc = new Location(tempLoc);
                            isEat = true;
                            legalMove.setDestination(nextLoc);
                            legalMove.setEnemyLoc(opponentLoc);
                            legalMove.setEat(isEat);
                            i--;
                            j++;
                            posMoves.add(legalMove);
                            continue;
                        }
                        if(board.getPiece(nextLoc) != null) //  מיקומים חסומים על ידי שני חיילים
                        {
                            break;
                        }
                        if(tmpPiece.getPieceColor() == player) // חייל שלי ממוקם באלכסון
                        {
                            break;
                        }

                        if(isEat && tmpPiece.getPieceColor() != player) // היתקלות שנייה באלכסון בחייל יריב
                        {
                            break;
                        }
                    }
                    else
                    {
                        break; // חייל ממוקם בקצה האלכסון
                    }
                }
            }
            else
            {
                break;
            }
        }

        return posMoves;
    }

    // Returns list of legal moves on the upper left diagonal (for kings)
    private ArrayList<Move> upperLeftDiagonal(Board board, Location loc)
    {
        int row = loc.getRow(), col = loc.getCol();
        Piece piece = board.getPiece(loc);
        Location opponentLoc = null;
        ArrayList<Move> posMoves = new ArrayList<>();
        boolean isEat = false;
        PieceColor player = piece.getPieceColor();
        for(int i = row - 1, j = col - 1; new Location(i, j).isInBounds(); i--, j--) // לבדוק ולפי זה לשכפל את הלולאה
        {
            Location tempLoc = new Location(i, j);
            Location nextLoc = new Location(i - 1, j - 1);
            Move legalMove = new Move(piece, loc, tempLoc, isEat, opponentLoc);
            Piece tmpPiece = board.getPiece(tempLoc);
            if(tempLoc.isInBounds())
            {
                if(tmpPiece == null) // מיקום פנוי למוב
                {
                    posMoves.add(legalMove);
                    continue;
                }
                if(tmpPiece != null)
                {
                    if(nextLoc.isInBounds())
                    {
                        if(board.getPiece(nextLoc) == null
                                && tmpPiece.getPieceColor() != player) // סיטואציית אכילה
                        {
                            opponentLoc = new Location(tempLoc);
                            isEat = true;
                            legalMove.setDestination(nextLoc);
                            legalMove.setEnemyLoc(opponentLoc);
                            legalMove.setEat(isEat);
                            i--;
                            j--;
                            posMoves.add(legalMove);
                            continue;
                        }
                        if(board.getPiece(nextLoc) != null) //  מיקומים חסומים להתקדמות על ידי שני חיילים
                        {
                            break;
                        }
                        if(tmpPiece.getPieceColor() == player) // חייל שלי ממוקם באלכסון
                        {
                            break;
                        }

                        if(isEat && tmpPiece.getPieceColor() != player) // היתקלות שנייה באלכסון בחייל יריב
                        {
                            break;
                        }
                    }
                    else
                    {
                        break; // חייל ממוקם בקצה האלכסון
                    }
                }
            }
            else
            {
                break;
            }
        }

        return posMoves;
    }

    // Returns list of legal moves on the lower right diagonal (for kings)
    private ArrayList<Move> lowerRightDiagonal(Board board, Location loc)
    {
        int row = loc.getRow(), col = loc.getCol();
        Piece piece = board.getPiece(loc);
        Location opponentLoc = null;
        ArrayList<Move> posMoves = new ArrayList<>();
        boolean isEat = false;
        PieceColor player = piece.getPieceColor();
        for(int i = row + 1, j = col + 1; new Location(i, j).isInBounds(); i++, j++) // לבדוק ולפי זה לשכפל את הלולאה
        {
            Location tempLoc = new Location(i, j);
            Location nextLoc = new Location(i + 1, j + 1);
            Move legalMove = new Move(piece, loc, tempLoc, isEat, opponentLoc);
            Piece tmpPiece = board.getPiece(tempLoc);
            if(tempLoc.isInBounds())
            {
                if(tmpPiece == null) // מיקום פנוי למוב
                {
                    posMoves.add(legalMove);
                    continue;
                }
                if(tmpPiece != null)
                {
                    if(nextLoc.isInBounds())
                    {
                        if(board.getPiece(nextLoc) == null
                                && tmpPiece.getPieceColor() != player) // סיטואציית אכילה
                        {
                            opponentLoc = new Location(tempLoc);
                            isEat = true;
                            legalMove.setDestination(nextLoc);
                            legalMove.setEnemyLoc(opponentLoc);
                            legalMove.setEat(isEat);
                            i++;
                            j++;
                            posMoves.add(legalMove);
                            continue;
                        }
                        if(board.getPiece(nextLoc) != null) //  מיקומים חסומים להתקדמות על ידי שני חיילים
                        {
                            break;
                        }
                        if(tmpPiece.getPieceColor() == player)
                        {
                            break;
                        }

                        if(isEat && tmpPiece.getPieceColor() != player) // היתקלות שנייה באלכסון בחייל יריב
                        {
                            break;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                break;
            }
        }

        return posMoves;
    }

    /**
     *
     * @param board
     * @param loc
     * @return list of legal moves on the lower left diagonal (for kings)
     */
    // Returns list of legal moves on the lower left diagonal (for kings)
    private ArrayList<Move> lowerLeftDiagonal(Board board, Location loc)
    {
        int row = loc.getRow(), col = loc.getCol();
        Piece piece = board.getPiece(loc);
        Location opponentLoc = null;
        ArrayList<Move> posMoves = new ArrayList<>();
        boolean isEat = false;
        PieceColor player = piece.getPieceColor();
        for(int i = row + 1, j = col - 1; new Location(i, j).isInBounds(); i++, j--) // לבדוק ולפי זה לשכפל את הלולאה
        {
            Location tempLoc = new Location(i, j);
            Location nextLoc = new Location(i + 1, j - 1);
            Move legalMove = new Move(piece, loc, tempLoc, isEat, opponentLoc);
            Piece tmpPiece = board.getPiece(tempLoc);
            if(tempLoc.isInBounds())
            {
                if(tmpPiece == null) // מיקום פנוי למוב
                {
                    posMoves.add(legalMove);
                    continue;
                }
                if(tmpPiece != null)
                {
                    if(nextLoc.isInBounds())
                    {
                        if(board.getPiece(nextLoc) == null
                                && tmpPiece.getPieceColor() != player) // סיטואציית אכילה
                        {
                            opponentLoc = new Location(tempLoc);
                            isEat = true;
                            legalMove.setDestination(nextLoc);
                            legalMove.setEnemyLoc(opponentLoc);
                            legalMove.setEat(isEat);
                            i++;
                            j--;
                            posMoves.add(legalMove);
                            continue;
                        }
                        if(board.getPiece(nextLoc) != null) //  מיקומים חסומים להתקדמות על ידי שני חיילים
                        {
                            break;
                        }
                        if(tmpPiece.getPieceColor() == player) // חייל שלי באלכסון
                        {
                            break;
                        }

                        if(isEat && tmpPiece.getPieceColor() != player) // היתקלות שנייה באלכסון בחייל יריב
                        {
                            break;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                break;
            }
        }

        return posMoves;
    }

    /**
     *
     * @param playerSign
     * @return all locations that opponent of player sign placed on logic board
     */
//    public ArrayList<Location> getAllEnemyLocs(PieceColor playerSign)
//    {
//        return getAllEnemyLocs(logicBoard, playerSign);
//    }
    /**
     *
     * @param board
     * @param playerSign
     * @return all the locations that opponent of player sign placed on specific
     * board
     */
//    public ArrayList<Location> getAllEnemyLocs(Board board, PieceColor playerSign)
//    {
//        ArrayList<Location> enemyLocs = new ArrayList<>();
//        for(int i = 0; i < ROWS; i++)
//        {
//            for(int j = 0; j < COLS; j++)
//            {
//                Location loc = new Location(i, j);
//                Piece p = board.getPiece(loc);
//                if(p != null)
//                {
//                    if(playerSign == WHITE_PLAYER && !p.isWhite())
//                    {
//                        enemyLocs.add(loc);
//                    }
//
//                    if(playerSign == BLACK_PLAYER && p.isWhite())
//                    {
//                        enemyLocs.add(loc);
//                    }
//                }
//            }
//        }
//
//        return enemyLocs;
//    }
    /**
     * Gets all the locations that pieces of current player placed on logicBoard
     *
     * @param currentPlayer
     * @return ArrayList of all currentPlayer pieces locations placed there
     */
    public ArrayList<Location> getAllCurrentLocs(PieceColor currentPlayer)
    {
        //System.out.println("----------------- in getAllCurrentLocs ----------------");
        ArrayList<Location> currentLoc = new ArrayList<>();

        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Location loc = new Location(i, j);
                Piece p = logicBoard.getPiece(loc);
                if(p != null)
                {
                    if(canDoMove(p))
                    {
                        if(currentPlayer == WHITE_PLAYER && p.isWhite())
                        {
                            currentLoc.add(loc);
                        }

                        if(currentPlayer == BLACK_PLAYER && !p.isWhite())
                        {
                            currentLoc.add(loc);
                        }
                    }
                }
            }
        }
        //System.out.println("currentLocs: " + currentLoc);
        return currentLoc;
    }

    //Checks if specific piece can do move
    private boolean canDoMove(Piece p)
    {
        //System.out.println("------------------ in canDoMove -------------------");
        Location loc = p.getPieceLoc();
        ArrayList<Move> allMoves = simplePossibleMovesForPiece(logicBoard, loc);
        if(allMoves.isEmpty())
        {
            return false;
        }

        return true;
    }

    /**
     * Gets all white tiles locations
     *
     * @return ArrayList of white tiles locations
     */
    public ArrayList<Location> getAllWhiteLocs()
    {
        ArrayList<Location> whiteLocs = new ArrayList<>();
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(i % 2 == 0 && j % 2 == 0)
                {
                    whiteLocs.add(new Location(i, j));
                }
                if(i % 2 != 0 && j % 2 != 0)
                {
                    whiteLocs.add(new Location(i, j));
                }
            }
        }
        return whiteLocs;
    }

//    public ArrayList<Move> getMultEating(Move move)
//    {
//        return getMultEating(logicBoard, move);
//    }
    /**
     * Checks if after the move is made the piece on specific board can do
     * double eating
     *
     * @param board
     * @param move
     * @return ArrayList of all moves that can make double eating (if not exists
     * the list is empty)
     */
    public ArrayList<Move> getMultEating(Board board, Move move)
    {
        //System.out.println("-------------- in getMultEating() ---------------");
        Board tempBoard = new Board(board);
        tempBoard = makeMove(tempBoard, move);
        ArrayList<Move> posMoves = simplePossibleMovesForPiece(tempBoard, move.getDestination());
        ArrayList<Move> multEating = new ArrayList<>();
        //System.out.println("all possible moves in multEating: ");

        for(Move pos : posMoves)
        {
            //System.out.println(pos);
            if(pos.isEat())
            {
                //System.out.println("move include eating! move: " + pos);
                //System.out.println("has next!");
                //System.out.println("move include eating!");
                multEating.add(new Move(pos));
            }
        }

//        for(Move mult : posMoves)
//        {
//            System.out.println("move in getMultEating(): " + move);
//
//        }
//        System.out.println("final mult moves:");
//        for(Move mult : multEating)
//        {
//            System.out.println(mult);
//        }
        return multEating;
    }

    // Checks if opponent can make double eating
    private boolean hasMultEatingForCurrent(ArrayList<Move> allMoves, ArrayList<Move> opponentMoves)
    {
        Move doubleEat = new Move();
        for(Move move : allMoves)
        {
            if(move.hasNext())
            {
                doubleEat = new Move(move);
                break;
            }
        }

        if(doubleEat.getPiece() != null) // if mult is exists
        {
            for(Move move : opponentMoves)
            {
                if(move.isEat())
                {
                    if(move.getEnemyLoc().isEqual(doubleEat.getSource()))
                    {
                        return false;
                    }
                }
            }
            return true;
        }

        else
        {
            return false;
        }
    }

    // evaluate the grade of specific mult eating of current  (for eval func)
    private int GradeForMultEatingCurrent(Board board, ArrayList<Move> allMoves, ArrayList<Move> opponentMoves)
    {
        Move mult = new Move();
        if(hasMultEatingForCurrent(allMoves, opponentMoves))
        {
            for(Move move : allMoves)
            {
                if(move.hasNext())
                {
                    mult = new Move(move);
                }
            }
            Location loc1 = mult.getEnemyLoc(), loc2 = mult.getSecondMove().get(0).getEnemyLoc();
            Piece enemy1 = new Piece(board.getPiece(loc1)),
                    enemy2 = new Piece(board.getPiece(loc2));

            if(enemy1.isKing() && enemy2.isKing()) // both kings
            {
                return 16;
            }
            if((!enemy1.isKing() && enemy2.isKing())
                    || (enemy1.isKing() && !enemy2.isKing())) // one of them king
            {
                return 10;
            }
            if(!enemy1.isKing() && !enemy2.isKing()) // both not king
            {
                return 5;
            }
        }
        return 0;
    }

    // Checks if the opponent can make double eating in his turn
    private boolean hasMultEatingForOpponent(ArrayList<Move> opponentMoves)
    {
        for(Move move : opponentMoves)
        {
            if(move.hasNext())
            {
                return true;
            }
        }

        return false;
    }

    // evaluate the grade of specific mult eating of opponent player (for eval func)
    private int GradeForMultEatingOpponent(Board board, ArrayList<Move> opponentMoves)
    {
        Move mult = new Move();
        if(hasMultEatingForOpponent(opponentMoves))
        {
            for(Move move : opponentMoves)
            {
                if(move.hasNext())
                {
                    mult = new Move(move);
                }
            }
            Location loc1 = mult.getEnemyLoc(), loc2 = mult.getSecondMove().get(0).getEnemyLoc();
            Piece enemy1 = new Piece(board.getPiece(loc1)),
                    enemy2 = new Piece(board.getPiece(loc2));
            if(enemy1.isKing() && enemy2.isKing())
            {
                return 16;
            }
            if((!enemy1.isKing() && enemy2.isKing())
                    || (enemy1.isKing() && !enemy2.isKing()))
            {
                return 10;
            }
            if(!enemy1.isKing() && !enemy2.isKing())
            {
                return 5;
            }
        }
        return 0;
    }

    @Override
    public String toString()
    {
        String str = "";
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Piece piece = logicBoard.getPiece(new Location(i, j));
                if(piece == null)
                {
                    str += "-";
                }
                else
                {
                    str += piece.getSignOfPiece();
                }
            }
            str += "\n";
        }
        return str;
    }
}
