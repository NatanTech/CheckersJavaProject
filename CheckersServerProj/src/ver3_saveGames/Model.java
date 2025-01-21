package ver3_saveGames;

import java.util.ArrayList;
import java.util.Arrays;
import ver3_saveGames.Piece.PieceColor;

/**
 * Document : Model Created on : 11/05/2021, 13:42:23 Author : beita
 */
public class Model
{
    // Constants - קבועים
    private static final int ROWS = 8;  // מספר השורות במטריצה
    private static final int COLS = 8;  // מספר העמודות במטריצה

    // תכונות
    private Board logicBoard;   // לוח משחק לוגי
    private int numMoves = 0;
    //private Controller controller;

    /**
     * constractor
     */
    public Model()
    {
        // יצירת לוח לוגי לצורך בדיקות וקבלת החלטות במשחק
        logicBoard = new Board();
    }

    //פעולה בונה מעתיקה
    public Model(Model model)
    {
        this.logicBoard = new Board(model.logicBoard);
        //this.controller = model.controller;
    }

    public void setModel(Board logicBoard)
    {
        this.logicBoard = new Board(logicBoard);
    }

    public Board getLogicBoard()
    {
        return logicBoard;
    }

    // פעולה לאתחול לוגי של משחק חדש
    public void setup()
    {
        //testingSetup();
        normalSetup();
    }

//    public void setup(Model model)
//    {
//        this.logicBoard = new Board(model.logicBoard);
//    }
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
                        Piece p = new Piece(PieceColor.BLACK_PLAYER, loc, false);
                        logicBoard.setPiece(loc, p);
                    }

                    if(row >= 5 && row <= 7)
                    {
                        Piece p = new Piece(PieceColor.WHITE_PLAYER, loc, false);
                        logicBoard.setPiece(loc, p);
                    }

                    if(row == 3 || row == 4)
                    {
                        logicBoard.setPiece(loc, null);
                    }
                    isBlackTile = false;
                }
                else
                {
                    isBlackTile = true;
                }
            }

        }
    }

    private void testingSetup()
    {
        logicBoard.clearBoard();
        ArrayList<Piece> list = new ArrayList(Arrays.asList(new Piece(PieceColor.WHITE_PLAYER, new Location(4, 3), true),
                new Piece(PieceColor.WHITE_PLAYER, new Location(4, 7), false),
                new Piece(PieceColor.BLACK_PLAYER, new Location(2, 3), true),
                new Piece(PieceColor.BLACK_PLAYER, new Location(2, 7), false)));
        logicBoard.setManyPieces(list);
    }

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
                        Piece p = new Piece(PieceColor.BLACK_PLAYER, loc, false);
                        logicBoard.setPiece(loc, p);
                    }
                    if(row == 7)
                    {
                        Piece p = new Piece(PieceColor.WHITE_PLAYER, loc, false);
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

    /**
     *
     * @param move
     * @return if the move in logic board include eat
     */
    public boolean isEat(Move move)
    {
        return isEat(logicBoard, move);
    }

    /**
     *
     * @param board
     * @param move
     * @return if the move in specific board include eat
     */
    public boolean isEat(Board board, Move move)
    {
        return getTheEatChoosed(board, move) != null;
    }

    /**
     *
     * @param move
     * @return the enemy location that choosed to eat by the move in logic board
     */
    public Location getTheEatChoosed(Move move)
    {
        return getTheEatChoosed(logicBoard, move);
    }

    /**
     *
     * @param board
     * @param move
     * @return the enemy location that choosed to eat by the move in specific
     * board
     */
    public Location getTheEatChoosed(Board board, Move move)
    {
        ArrayList<Location> enemyLocs = getEnemyLocations(board, move.getSource());
        Piece piece = move.getPiece();
        PieceColor player = piece.getPieceColor();
        Location source = move.getSource(), dest = move.getDestination();
        if(player == PieceColor.WHITE_PLAYER || (player == PieceColor.BLACK_PLAYER && piece.isKing()))
        {
            Location newSource1 = new Location(source.getRow() - 2, source.getCol() + 2);
            Location newSource2 = new Location(source.getRow() - 2, source.getCol() - 2);
            if(newSource1.isInBounds())
            {
                if(newSource1.getRow() == dest.getRow() && newSource1.getCol() == dest.getCol())
                {
                    for(int i = 0; i < enemyLocs.size(); i++)
                    {
                        Location loc = enemyLocs.get(i),
                                enemy = new Location(source.getRow() - 1, source.getCol() + 1);

                        if(enemy.getRow() == loc.getRow()
                                && enemy.getCol() == loc.getCol())
                        {
                            return new Location(loc.getRow(), loc.getCol());
                        }
                    }
                }
            }

            if(newSource2.isInBounds())
            {
                if(newSource2.getRow() == dest.getRow()
                        && newSource2.getCol() == dest.getCol())
                {
                    for(int i = 0; i < enemyLocs.size(); i++)
                    {
                        Location loc = enemyLocs.get(i),
                                enemy = new Location(source.getRow() - 1, source.getCol() - 1);

                        if(enemy.getRow() == loc.getRow()
                                && enemy.getCol() == loc.getCol())
                        {
                            return new Location(loc.getRow(), loc.getCol());
                        }
                    }
                }
            }
        }

        if(player == PieceColor.BLACK_PLAYER || (player == PieceColor.WHITE_PLAYER && piece.isKing()))
        {
            Location newSource1 = new Location(source.getRow() + 2, source.getCol() + 2);
            Location newSource2 = new Location(source.getRow() + 2, source.getCol() - 2);

            if(newSource1.isInBounds())
            {
                if(newSource1.getRow() == dest.getRow()
                        && newSource1.getCol() == dest.getCol())
                {
                    for(int i = 0; i < enemyLocs.size(); i++)
                    {
                        Location loc = enemyLocs.get(i),
                                enemy = new Location(source.getRow() + 1, source.getCol() + 1);

                        if(enemy.getRow() == loc.getRow()
                                && enemy.getCol() == loc.getCol())
                        {
                            return new Location(loc.getRow(), loc.getCol());
                        }
                    }
                }
            }

            if(newSource2.isInBounds())
            {
                if(newSource2.getRow() == dest.getRow()
                        && newSource2.getCol() == dest.getCol())
                {
                    for(int i = 0; i < enemyLocs.size(); i++)
                    {
                        Location loc = enemyLocs.get(i),
                                enemy = new Location(source.getRow() + 1, source.getCol() - 1);
                        if(enemy.getRow() == loc.getRow()
                                && enemy.getCol() == loc.getCol())
                        {
                            return new Location(loc.getRow(), loc.getCol());
                        }
                    }
                }
            }
        }
        return null;
    }

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
     */
    public void makeMove(Board board, Move move)
    {
        Location source = move.getSource(),
                destination = move.getDestination();
        Piece p = move.getPiece();
        PieceColor player = p.getPieceColor();
        board.setPiece(destination, p);
        board.deletePiece(source);
        if(move.getIsEat())
        {
            Location enemy = move.getEnemyLoc();
            board.deletePiece(enemy);
        }

        if((player == PieceColor.WHITE_PLAYER && destination.getRow() == 0)
                || player == PieceColor.BLACK_PLAYER && destination.getRow() == ROWS - 1)
        {
            board.makeKing(destination);
        }
    }

    /**
     *
     * @param move make move on logic board
     */
    public void makeMove(Move move)
    {
        makeMove(logicBoard, move);
    }

    /**
     *
     * @param move
     * @return true if the piece that move is king, false else
     */
    public boolean isKing(Move move)
    {
        return move.getPiece().isKing();
    }

    /**
     *
     * @param player
     * @return true if the result of logic board is tie, false else
     */
    public boolean checkTie()
    {
        return checkTie(logicBoard);
    }

    /**
     *
     * @param board
     * @param player
     * @return true if the result of specific board is tie, false else
     */
    public boolean checkTie(Board board)
    {
        PieceColor white = PieceColor.WHITE_PLAYER;
        PieceColor black = PieceColor.BLACK_PLAYER;
        if(countKings(board, white) == countKings(board, black)
                && countOrdinaryTroops(board, white) == 0
                && countOrdinaryTroops(board, black) == 0)
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
     *
     * @param board
     * @param playerSign
     * @return true if the result of specific board is win to playerSign, false
     * else
     */
    public boolean checkWin(Board board, PieceColor playerSign)
    {
        int counterWhite = 0;
        int counterBlack = 0;
        PieceColor opponent = getOpponent(playerSign);

        // נבדוק האם יש ניצחון לשחקן הנוכחי באחת מהשורות
        for(int row = 0; row < ROWS; row++)
        {
            for(int col = 0; col < COLS; col++)
            {
                Location loc = new Location(row, col);
                Piece p = board.getPiece(loc);
                if(p != null)
                {
                    PieceColor player = p.getPieceColor();
                    if(player == PieceColor.WHITE_PLAYER)
                    {
                        counterWhite++;
                    }
                    if(player == PieceColor.BLACK_PLAYER)
                    {
                        counterBlack++;
                    }
                }
            }
        }
        if(playerSign == PieceColor.WHITE_PLAYER && counterBlack == 0)
        {
            return true;
        }

        if(playerSign == PieceColor.BLACK_PLAYER && counterWhite == 0)
        {
            return true;
        }

        if(countOrdinaryPieces(board, playerSign) - countOrdinaryPieces(board, opponent) > 0
                && countKings(board, playerSign) - countKings(board, opponent) >= 0
                && getAllPossibleMoves(board, opponent).isEmpty())
        {
            return true;
        }

        return false;
    }

    /**
     *
     * @return the empty locs on logic board
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

    private boolean isWhiteTile(Location loc)
    {
        int row = loc.getRow(), col = loc.getCol();
        return (row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0);
    }

    /**
     *
     * @param player
     * @return the opponent of player
     */
    private PieceColor getOpponent(PieceColor player)
    {
        if(player == PieceColor.WHITE_PLAYER)
        {
            return PieceColor.BLACK_PLAYER;
        }
        return PieceColor.WHITE_PLAYER;
    }

    /**
     *
     * @return minimax move
     */
    public Move getAiMove(PieceColor current)
    {
        System.out.println("Model getAiMove() using MINIMAX");

        Move bestMove = getBestMoveUsingMinimax(current);

        return bestMove;
    }

    // ============================ for minimax ===========================
    private Move getBestMoveUsingMinimax(PieceColor current)
    {
        Move minimaxMove = minimax(logicBoard, current, true, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println("move choosed:");
        printMinimaxMove(minimaxMove);
        return minimaxMove;
    }

    /**
     *
     * @param board
     * @param current
     * @param isMax
     * @param depth
     * @param alpha
     * @param beta
     * @return the best move to make that could be found to a specific depth
     */
    public Move minimax(Board board, PieceColor current, boolean isMax, int depth, int alpha, int beta)
    {
        Move bestMove = new Move();

        if(depth == 12 || isGameOver(board))
        {
            bestMove.setGrade(eval(current));
            bestMove.setDepth(depth);
            return bestMove;
        }

        if(!isMax)
        {
            current = getOpponent(current);
        }

        ArrayList<Move> possibleMoves = getAllPossibleMoves(board, current);

        if(isMax)
        {
            bestMove.setGrade(Integer.MIN_VALUE);

            for(int i = 0; i < possibleMoves.size(); i++)
            {
                Move move = possibleMoves.get(i);
                Board newBoard = new Board(board);
                makeMove(newBoard, move);
                Move minimaxMove = minimax(newBoard, current, false, depth + 1, alpha, beta);
                minimaxMove.setMove(move);
                if(depth == 0)
                {
                    printMinimaxMove(minimaxMove);
                }

                if(minimaxMove.isDepthLegit()
                        && (minimaxMove.getGrade() > bestMove.getGrade()
                        || (minimaxMove.getIsEat() && !bestMove.getIsEat())
                        || (minimaxMove.getGrade() == bestMove.getGrade()
                        && minimaxMove.getDepth() < bestMove.getDepth())))
                {
                    bestMove = minimaxMove;
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
                makeMove(newBoard, move);
                Move minimaxMove = minimax(newBoard, current, true, depth + 1, alpha, beta);
                minimaxMove.setMove(move);
                if(minimaxMove.isDepthLegit()
                        && (minimaxMove.getGrade() < bestMove.getGrade()
                        || (minimaxMove.getIsEat() && !bestMove.getIsEat())
                        || (minimaxMove.getGrade() == bestMove.getGrade()
                        && minimaxMove.getDepth() > bestMove.getDepth())))
                {
                    bestMove = minimaxMove;
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
     *
     * @param player
     * @return evaluation for player on a logic board
     */
    public int eval(PieceColor player)
    {
        return eval(logicBoard, player);
    }

    /**
     *
     * @param board
     * @param player
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

        int currentCounter = countOrdinaryPieces(board, player),
                opponentCounter = countOrdinaryPieces(board, opponent);
        int sub = currentCounter - opponentCounter;
        evalScore += (sub * 2);
        //System.out.println("current: " + currentCounter + ", opponent: " + opponentCounter);
        //System.out.println("after count ordinary: " + evalScore);

        int currentKings = countKings(board, player),
                opponentKings = countKings(board, opponent);
        sub = currentKings - opponentKings;
        evalScore += (sub * 4);
        //System.out.println("current: " + currentKings + ", opponent: " + opponentKings);
        //System.out.println("after count kings: " + evalScore);

        int currentOneMoveKing = oneMoveKing(board, player),
                opponentOneMoveKing = oneMoveKing(board, opponent);
        sub = currentOneMoveKing - opponentOneMoveKing;
        evalScore += (sub * 2);
        //System.out.println("current: " + currentOneMoveKing + ", opponent: " + opponentOneMoveKing);
        //System.out.println("after one move king: " + evalScore);

        int currentOneMoveEaten = ordinaryCouldBeEatenNextMove(board, player),
                opponentOneMoveEaten = ordinaryCouldBeEatenNextMove(board, opponent);
        sub = currentOneMoveEaten - opponentOneMoveEaten;
        evalScore -= (sub * 1);
        //System.out.println("current: " + currentOneMoveEaten + ", opponent: " + opponentOneMoveEaten);
        //System.out.println("after count ordinary could be eaten: " + evalScore);

        int currentKingOneMoveEaten = kingsCouldBeEatenNextMove(board, player),
                opponentKingOneMoveEaten = kingsCouldBeEatenNextMove(board, opponent);
        sub = currentKingOneMoveEaten - opponentKingOneMoveEaten;
        evalScore -= (sub * 3);
        //System.out.println("current: " + currentKingOneMoveEaten
        //        + ", opponent: " + opponentKingOneMoveEaten);
        //System.out.println("after count kings could be eaten: " + evalScore);

        return evalScore;
    }

    /**
     *
     * @param board
     * @param player
     * @return how many pieces of player can make king next move
     */
    private int oneMoveKing(Board board, PieceColor player)
    {
        int counter = 0;
        for(int i = 1; i < ROWS; i += 5)
        {
            for(int j = 0; j < COLS; j++)
            {
                Piece piece = board.getPiece(i, j);
                if(piece != null)
                {
                    if(j + 1 < 8)
                    {
                        if((player == PieceColor.WHITE_PLAYER && piece.getPieceColor() == PieceColor.WHITE_PLAYER
                                && i == 1 && board.getPiece(0, j + 1) == null)
                                || (player == PieceColor.BLACK_PLAYER && piece.getPieceColor() == PieceColor.BLACK_PLAYER
                                && i == 6 && board.getPiece(0, j + 1) == null))
                        {
                            counter++;
                            continue;
                        }
                    }

                    if(j - 1 >= 0)
                    {
                        if((player == PieceColor.WHITE_PLAYER && piece.getPieceColor() == PieceColor.WHITE_PLAYER
                                && i == 1 && board.getPiece(0, j - 1) == null)
                                || (player == PieceColor.BLACK_PLAYER && piece.getPieceColor() == PieceColor.BLACK_PLAYER
                                && i == 6 && board.getPiece(0, j - 1) == null))
                        {
                            counter++;
                        }
                    }
                }
            }
        }
        return counter;
    }

    /**
     *
     * @param board
     * @param player
     * @return the amont of ordinary pieces of player that could be eaten next
     * move of the opponent
     */
    private int ordinaryCouldBeEatenNextMove(Board board, PieceColor player)
    {
        int counter = 0;
        ArrayList<Location> existsEnemyLocs = new ArrayList<>();
        PieceColor opponent = getOpponent(player);
        ArrayList<Move> posEnemyMoves = getAllPossibleMoves(board, opponent);
        for(Move enemyMove : posEnemyMoves)
        {

            if(enemyMove.getIsEat())
            {
                boolean isExists = false;
                Location loc = enemyMove.getEnemyLoc();
                for(Location exists : existsEnemyLocs)
                {
                    if(exists.isEqual(loc))
                    {
                        isExists = true;
                        break;
                    }
                }
                if(!isExists)
                {
                    Piece enemy = board.getPiece(loc);
                    if(!enemy.isKing())
                    {
                        existsEnemyLocs.add(loc);
                        counter++;
                    }
                }
            }
        }

        return counter;
    }

    /**
     *
     * @param board
     * @param player
     * @return the amont of king pieces of player that could be eaten next move
     * of the opponent
     */
    private int kingsCouldBeEatenNextMove(Board board, PieceColor player)
    {
        int counter = 0;
        ArrayList<Location> existsEnemyLocs = new ArrayList<>();
        PieceColor opponent = getOpponent(player);
        ArrayList<Move> posEnemyMoves = getAllPossibleMoves(board, opponent);
        for(Move enemyMove : posEnemyMoves)
        {

            if(enemyMove.getIsEat())
            {
                boolean isExists = false;
                Location loc = enemyMove.getEnemyLoc();
                for(Location exists : existsEnemyLocs)
                {
                    if(exists.isEqual(loc))
                    {
                        isExists = true;
                        break;
                    }
                }

                if(!isExists)
                {
                    Piece enemy = board.getPiece(loc);
                    if(enemy.isKing())
                    {
                        existsEnemyLocs.add(loc);
                        counter++;
                    }
                }
            }
        }

        return counter;
    }

    /**
     *
     * @param board
     * @param player
     * @return the amount of player pieces on specific board
     */
    private int countOrdinaryPieces(Board board, PieceColor player)
    {
        int counter = 0;
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Location loc = new Location(i, j);
                Piece p = board.getPiece(loc);
                if(p != null && p.getPieceColor() == player && !p.isKing())
                {
                    counter++;
                }
            }
        }

        return counter;
    }

    /**
     *
     * @param board
     * @param player
     * @return the amount of ordinary pieces (no kings) that player has on
     * specific board
     */
    private int countOrdinaryTroops(Board board, PieceColor player)
    {
        int counter = 0;
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Location loc = new Location(i, j);
                Piece p = board.getPiece(loc);
                if(p != null && p.getPieceColor() == player && !p.isKing())
                {
                    counter++;
                }
            }
        }

        return counter;
    }

    /**
     *
     * @param board
     * @param player
     * @return the amount of player kings on specific board
     */
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
                        if(p.getPieceColor() == player && p.isKing())
                        {
                            counter++;
                        }
                    }
                }
            }
        }
        return counter;
    }

    /**
     *
     * @param board
     * @param loc
     * @return ArrayList of locations that player on loc on specific board can
     * move there
     */
    private ArrayList<Move> getlegalSimpleMoves(Board board, Location loc)
    {
        ArrayList<Location> potentialLocs = new ArrayList<>();
        ArrayList<Move> legalMoves = new ArrayList<>();

        Piece p = board.getPiece(loc);
        int row = loc.getRow(), col = loc.getCol();
        if(p.isWhite() || (!p.isWhite() && p.isKing()))
        {
            potentialLocs.add(new Location(row - 1, col + 1));
            potentialLocs.add(new Location(row - 1, col - 1));
        }

        if(!p.isWhite() || (p.isWhite() && p.isKing()))
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
        return legalMoves;
    }

    /**
     *
     * @param board
     * @param loc
     * @return ArrayList of locations that include eats that player on loc on
     * specific board can move there
     */
    private ArrayList<Move> getlegalEatMoves(Board board, Location loc)
    {
        ArrayList<Location> potentialLocs = new ArrayList<>();
        ArrayList<Move> legalMoves = new ArrayList<>();
        Piece piece = board.getPiece(loc);
        int row = loc.getRow(), col = loc.getCol();
        if(piece.isWhite() || (!piece.isWhite() && piece.isKing()))
        {
            potentialLocs.add(new Location(row - 1, col + 1));
            potentialLocs.add(new Location(row - 2, col + 2));
            potentialLocs.add(new Location(row - 1, col - 1));
            potentialLocs.add(new Location(row - 2, col - 2));
        }

        if(!piece.isWhite() || (piece.isWhite() && piece.isKing()))
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

            if(potentialEnemyLoc.isInBounds() && potentialEmptyLoc.isInBounds() && piece != null)
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
        return legalMoves;
    }

    /**
     *
     * @param board
     * @param playerSign
     * @return all possible moves for a player on specific board
     */
    public ArrayList<Move> getAllPossibleMoves(Board board, PieceColor playerSign)
    {
        ArrayList<Move> allMoves = new ArrayList<>();
        ArrayList<Move> posMoves = new ArrayList<>();

        for(int row = 0; row < ROWS; row++)
        {
            for(int col = 0; col < COLS; col++)
            {
                Location loc = new Location(row, col);
                Piece p = board.getPiece(loc);
                if(p != null)
                {
                    if(playerSign == PieceColor.WHITE_PLAYER && p.isWhite())
                    {
                        posMoves = possibleMovesForPlayer(board, loc);
                    }

                    if(playerSign == PieceColor.BLACK_PLAYER && !p.isWhite())
                    {
                        posMoves = possibleMovesForPlayer(board, loc);
                    }

                    for(Move move : posMoves)
                    {
                        allMoves.add(move);
                    }
                    posMoves.clear();
                }
            }
        }
        return allMoves;
    }

    /**
     *
     * @return true if the game is over (win or tie) on logic board, false else
     */
    public boolean isGameOver()
    {
        return isGameOver(logicBoard);
    }

    /**
     *
     * @param board
     * @return true if the game is over (win or tie) on logic board, false else
     */
    public boolean isGameOver(Board board)
    {
        return checkWin(board, PieceColor.WHITE_PLAYER) || checkWin(board, PieceColor.BLACK_PLAYER)
                || checkTie(board);
    }

    /**
     *
     * @param move
     * @return true if specific move is legal, false else
     */
    public boolean isLegal(Move move)
    {
        Piece p = move.getPiece();
        Location source = move.getSource(), dest = move.getDestination();
        if(p.isWhite() || (!p.isWhite() && p.isKing()))
        {
            if(source.getRow() - 1 == dest.getRow()
                    && source.getCol() + 1 == dest.getCol())
            {
                return true;
            }

            if(source.getRow() - 1 == dest.getRow()
                    && source.getCol() - 1 == dest.getCol())
            {
                return true;
            }

            if(source.getRow() - 2 == dest.getRow()
                    && source.getCol() + 2 == dest.getCol()
                    && source.getRow() - 1 == move.getEnemyLoc().getRow()
                    && source.getCol() + 1 == move.getEnemyLoc().getCol())
            {
                return true;
            }

            if(source.getRow() - 2 == dest.getRow()
                    && source.getCol() - 2 == dest.getCol()
                    && source.getRow() - 1 == move.getEnemyLoc().getRow()
                    && source.getCol() - 1 == move.getEnemyLoc().getCol())
            {
                return true;
            }
        }

        if(!p.isWhite() || (p.isWhite() && p.isKing()))
        {
            if(source.getRow() + 1 == dest.getRow()
                    && source.getCol() + 1 == dest.getCol())
            {
                return true;
            }

            if(source.getRow() + 1 == dest.getRow()
                    && source.getCol() - 1 == dest.getCol())
            {
                return true;
            }

            if(source.getRow() + 2 == dest.getRow()
                    && source.getCol() + 2 == dest.getCol()
                    && source.getRow() + 1 == move.getEnemyLoc().getRow()
                    && source.getCol() + 1 == move.getEnemyLoc().getCol())
            {
                return true;
            }

            if(source.getRow() + 2 == dest.getRow()
                    && source.getCol() - 2 == dest.getCol()
                    && source.getRow() + 1 == move.getEnemyLoc().getRow()
                    && source.getCol() - 1 == move.getEnemyLoc().getCol())
            {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param loc
     * @return all enemy location for player on loc on logic board
     */
    public ArrayList<Location> getEnemyLocations(Location loc)
    {
        return getEnemyLocations(logicBoard, loc);
    }

    /**
     *
     * @param board
     * @param loc
     * @return all enemy location for player on loc on specific board
     */
    private ArrayList<Location> getEnemyLocations(Board board, Location loc)
    {
        ArrayList<Location> potentialLocs = new ArrayList<>();
        ArrayList<Location> finalLocs = new ArrayList<>();
        Piece p = board.getPiece(loc);
        int row = loc.getRow(), col = loc.getCol();
        if(p != null)
        {
            if(p.isWhite() || (!p.isWhite() && p.isKing()))
            {
                potentialLocs.add(new Location(row - 1, col + 1));
                potentialLocs.add(new Location(row - 1, col - 1));
            }

            if(!p.isWhite() || (p.isWhite() && p.isKing()))
            {
                potentialLocs.add(new Location(row + 1, col + 1));
                potentialLocs.add(new Location(row + 1, col - 1));
            }

            for(int i = 0; i < potentialLocs.size(); i++)
            {
                Location enemyLoc = potentialLocs.get(i);

                if(enemyLoc.isInBounds())
                {
                    Piece EnemyPlayer = board.getPiece(enemyLoc);
                    if(EnemyPlayer != null)
                    {
                        if(p.isWhite() && !EnemyPlayer.isWhite())
                        {
                            finalLocs.add(enemyLoc);
                        }
                        if(!p.isWhite() && EnemyPlayer.isWhite())
                        {
                            finalLocs.add(enemyLoc);
                        }
                    }
                }
            }
        }
        return finalLocs;
    }

    /**
     *
     * @param loc
     * @return all moves that piece on loc can make on logic board
     */
    public ArrayList<Move> possibleMovesForPlayer(Location loc)
    {
        return possibleMovesForPlayer(logicBoard, loc);
    }

    /**
     *
     * @param board
     * @param loc
     * @return all moves that piece on loc can make on specific board
     */
    public ArrayList<Move> possibleMovesForPlayer(Board board, Location loc)
    {
        ArrayList<Move> legalMoves = getlegalSimpleMoves(board, loc);
        ArrayList<Move> posEatMoves = getlegalEatMoves(board, loc);
        legalMoves.addAll(posEatMoves);
        return legalMoves;
    }

    /**
     *
     * @param playerSign
     * @return all locations that opponent of player sign placed on logic board
     */
    public ArrayList<Location> getAllEnemyLocs(PieceColor playerSign)
    {
        return getAllEnemyLocs(logicBoard, playerSign);
    }

    /**
     *
     * @param board
     * @param playerSign
     * @return all locations that opponent of player sign placed on specific
     * board
     */
    public ArrayList<Location> getAllEnemyLocs(Board board, PieceColor playerSign)
    {
        ArrayList<Location> enemyLocs = new ArrayList<>();
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Location loc = new Location(i, j);
                Piece p = board.getPiece(loc);
                if(p != null)
                {
                    if(playerSign == PieceColor.WHITE_PLAYER && !p.isWhite())
                    {
                        enemyLocs.add(loc);
                    }

                    if(playerSign == PieceColor.BLACK_PLAYER && p.isWhite())
                    {
                        enemyLocs.add(loc);
                    }
                }
            }
        }

        return enemyLocs;
    }

    /**
     *
     * @param currentPlayer
     * @return all the locations that pieces of current player placed on logic
     * board
     */
    public ArrayList<Location> getAllCurrentLocs(PieceColor currentPlayer)
    {
        ArrayList<Location> currentLoc = new ArrayList<>();
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Location loc = new Location(i, j);
                Piece p = logicBoard.getPiece(loc);
                if(p != null)
                {
                    if(currentPlayer == PieceColor.WHITE_PLAYER && p.isWhite())
                    {
                        currentLoc.add(loc);
                    }

                    if(currentPlayer == PieceColor.BLACK_PLAYER && !p.isWhite())
                    {
                        currentLoc.add(loc);
                    }
                }
            }
        }
        return currentLoc;
    }

    /**
     *
     * @return all the locations of white squares
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

    private void printMinimaxMove(Move move)
    {
        System.out.println("player: " + move.getPiece().getPieceColor() + ", king: "
                + move.getPiece().isKing() + ", source: " + move.getSource()
                + ", dest: " + move.getDestination() + ", grade: " + move.getGrade()
                + ", depth: " + move.getDepth());
    }
}
