package ver6;

import java.util.ArrayList;
import java.util.Arrays;
import ver6.Piece.PieceColor;
import static ver6.Piece.PieceColor.*;

/**
 * Document : State Created on : 11/05/2021, 13:42:23 Author : beita
 */
public class State
{
    // Constants - קבועים
    private static final int ROWS = 8;  // מספר השורות במטריצה
    private static final int COLS = 8;  // מספר העמודות במטריצה
    //public static final int MINIMAX_TIME = 11; // זמן למינימקס

    // תכונות
    private Board currentBoard;   // לוח משחק לוגי
    private Player currentPlayer; // the current player turn
    private int numMoves;

    public State(Board logicBoard, Player currentPlayer) {
        this.currentBoard = logicBoard;
        this.currentPlayer = currentPlayer;
    }


    /**
     * constractor
     */
    public State()
    {
        // יצירת לוח לוגי לצורך בדיקות וקבלת החלטות במשחק
        currentBoard = new Board();
        numMoves = 0;
    }

    //Copy constructor
    public State(State state)
    {
        this.currentBoard = new Board(state.currentBoard);
        this.currentPlayer=state.getCurrentPlayer();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Sets this currentBoard by currentBoard other
     *
     * @param logicBoard
     */
    public void setModel(Board logicBoard)
    {
        this.currentBoard = new Board(logicBoard);
    }

    public Board getCurrentBoard()
    {
        return currentBoard;
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
                Piece player = currentBoard.getPiece(loc);
                if(player == null && !isEmptySquar(loc))
                {
                    emptyLocs.add(new Location(row, col));
                }
            }
        }

        return emptyLocs;
    }

    //Checks if the squar of loc is empty tile
    private boolean isEmptySquar(Location loc)
    {
        int row = loc.getRow(), col = loc.getCol();
        return (row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0);
    }

    //makes setup to current Board
    public void setup()
    {
        regullarSetup();
//      testingSetup();
        //smallSetup();
        //kingsMovesTest();
        //doubleEatingSetup();
        //tripleEatingSetup();
        //cantMoveTestingSetup();
    }

    // normal starting board
    private void regullarSetup()
    {
        boolean isBlackPiece;
        for(int row = 0; row < ROWS; row++)
        {
            isBlackPiece = row % 2 != 0;
            for(int col = 0; col < COLS; col++)
            {
                Location loc = new Location(row, col);
                if(isBlackPiece)
                {
                    if(row >= 0 && row <= 2)
                    {
                        Piece p = new Piece(BLACK_PLAYER, loc, false);
                        currentBoard.setPiece(loc, p);
                    }
                    if(row >= 5 && row <= 7)
                    {
                        Piece p = new Piece(WHITE_PLAYER, loc, false);
                        currentBoard.setPiece(loc, p);
                    }

                    if(row >= 3 && row <= 4)
                    {
                        currentBoard.clearSquar(loc);
                    }
                    isBlackPiece = false;
                }
                else
                {
                    currentBoard.clearSquar(loc);
                    isBlackPiece = true;
                }
            }

        }
    }

    // testing board
    private void kingsTestSetup()
    {
        currentBoard.clearBoard();
        ArrayList<Piece> list = new ArrayList(Arrays.asList(new Piece(WHITE_PLAYER, new Location(4, 3), true),
                new Piece(BLACK_PLAYER, new Location(6, 1), false),
                new Piece(WHITE_PLAYER, new Location(1, 6), false),
                new Piece(BLACK_PLAYER, new Location(1, 0), false),
                new Piece(BLACK_PLAYER, new Location(5, 4), true)));
        currentBoard.setListPieces(list);
    }

    // testing board
    private void testSetup()
    {
        currentBoard.clearBoard();
        ArrayList<Piece> list = new ArrayList(Arrays.asList(new Piece(WHITE_PLAYER, new Location(4, 3), false),
                new Piece(WHITE_PLAYER, new Location(4, 7), false),
                new Piece(BLACK_PLAYER, new Location(2, 3), false),
                new Piece(BLACK_PLAYER, new Location(2, 7), false)));
        currentBoard.setListPieces(list);
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
                        currentBoard.setPiece(loc, p);
                    }
                    if(row == 7)
                    {
                        Piece p = new Piece(WHITE_PLAYER, loc, false);
                        currentBoard.setPiece(loc, p);
                    }

                    isBlack = false;
                }
                else
                {
                    currentBoard.setPiece(loc, null);
                    isBlack = true;
                }

            }

        }
    }

    // testing board
    private void doubleEatingTestSetup()
    {
        currentBoard.clearBoard();
        ArrayList<Piece> list = new ArrayList(Arrays.asList(new Piece(WHITE_PLAYER, new Location(4, 3), true),
                new Piece(BLACK_PLAYER, new Location(3, 4), true),
                new Piece(BLACK_PLAYER, new Location(1, 6), false)));
        currentBoard.setListPieces(list);
    }

    // testing board
    private void tripleEatingTestSetup()
    {
        currentBoard.clearBoard();
        ArrayList<Piece> list = new ArrayList(Arrays.asList(new Piece(WHITE_PLAYER, new Location(6, 3), true),
                new Piece(WHITE_PLAYER, new Location(4, 1), false),
                new Piece(BLACK_PLAYER, new Location(5, 4), false),
                new Piece(BLACK_PLAYER, new Location(3, 4), false),
                new Piece(BLACK_PLAYER, new Location(1, 4), false)));
        currentBoard.setListPieces(list);
    }

    // testing board
    private void cantMoveTestingSetup()
    {
        currentBoard.clearBoard();
        ArrayList<Piece> list = new ArrayList(Arrays.asList(new Piece(BLACK_PLAYER, new Location(0, 7), true),
                new Piece(WHITE_PLAYER, new Location(2, 7), false),
                new Piece(WHITE_PLAYER, new Location(2, 5), false)));
        currentBoard.setListPieces(list);
    }

    //Checks if specific player can't move
    public boolean playerCantMove(Board board, PieceColor player)
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(!board.isEmptySquar(new Location(i, j)))
                {
                    Piece p = board.getPiece(new Location(i, j));
                    if(isCanDoMove(p) && p.getPieceColor() == player)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    //returns the piese from loc
    public Piece getPieceFromLoc(Location loc)
    {
        return State.this.getPieceFromLoc(currentBoard, loc);
    }


    //return the piece that placed from loc on specific board
    public Piece getPieceFromLoc(Board board, Location loc)
    {
        return board.getPiece(loc);
    }

    //make move on Board
    public Board makeMove(Board board, Move move)
    {
        Location source = move.getSourceLoc(),
        destination = move.getLocTarget();
        Piece p = new Piece(move.getPiece());
        PieceColor player = p.getPieceColor();
        board.setPiece(destination, p);
        board.clearSquar(source);

        if(move.isIsEat())
        {
            Location enemy = move.getEatenLoc();
            board.clearSquar(enemy);
        }

        if(move.getBurnedLoc() != null)
        {
            board.clearSquar(move.getBurnedLoc());
        }

        if((player == WHITE_PLAYER && destination.getRow() == 0)
                || (player == BLACK_PLAYER && destination.getRow() == ROWS - 1))
        {
            board.makeKing(destination);
        }

        return board;
    }

    // makes move on copy board
    public Board makeMoveOnCopyBoard(Board board, Move move)
    {
        Board newBoard = new Board(board);
        Location source = move.getSourceLoc(),
                destination = move.getLocTarget();
        Piece p = new Piece(move.getPiece());
        PieceColor player = p.getPieceColor();

        newBoard.setPieceOnCopiedBoard(destination, p);
        //p.setPieceLoc(destination);
        newBoard.clearSquar(source);

        if(move.isIsEat())
        {
            Location enemy = new Location(move.getEatenLoc());
            newBoard.clearSquar(enemy);
        }

        if(move.getBurnedLoc() != null)
        {
            newBoard.clearSquar(move.getBurnedLoc());
        }

        if((player == WHITE_PLAYER && destination.getRow() == 0)
                || (player == BLACK_PLAYER && destination.getRow() == ROWS - 1))
        {
            p.setKing(true);
        }

        return newBoard;
    }

    //Making move on current Board
    public Board makeMove(Move move)
    {
        return makeMove(currentBoard, move);
    }

    //updateMove on currentBoard
    public Move updateMove(Move move)
    {
        return updateMove(currentBoard, move);
    }
    /*
      adding move details like(with burnedLoc, enemyLoc, isEat...)
    */
    public Move updateMove(Board board, Move move)
    {
        ArrayList<Move> posMoves = possibleMovesForPiece(board, move.getSourceLoc(), true);
        for(Move pos : posMoves)
        {
            if(move.equals(pos))
            {
                return pos;
            }
        }
        return move;
    }

    //Makes updateMove to list
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

    // setBurnedLocForAllMoves 
    private ArrayList<Move> setBurnedLocForAllMoves(ArrayList<Move> allMoves)
    {
        Move eatMove = new Move();

        for(Move posMove : allMoves)
        {
            if(posMove.isIsEat())
            {
                eatMove = new Move(posMove);
                //eatMove.copyMove(posMove);
                break;
            }
        }

        ArrayList<Move> movesToSet = allMoves;

        Location eatSource = eatMove.getSourceLoc(), eatDest = eatMove.getLocTarget();
        for(Move posMove : movesToSet)
        {

            if(eatMove.getPiece() == null) // eat isn't exists
            {
                break;
            }

            Location posSource = posMove.getSourceLoc(),
                    posDest = posMove.getLocTarget();
            if(!posMove.equals(eatMove) && !posMove.isIsEat())
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

    public ArrayList<Move> setBurnedLocForAllMoves(ArrayList<Move> pos, PieceColor player)
    {
        return setBurnedLocForAllMoves(currentBoard, player, pos);
    }

    //updates burnedLoc for ArrayList of moves (if exists) for specific board
    public ArrayList<Move> setBurnedLocForAllMoves(Board board, PieceColor player, ArrayList<Move> pos)
    {
        Move eatMove = new Move();
        ArrayList<Move> pieceMoves = new ArrayList<>();
        ArrayList<Move> allMoves = new ArrayList<>();

        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Location loc = new Location(i, j);
                Piece p = State.this.getPieceFromLoc(board, loc);
                if(p != null && p.getPieceColor() == player)
                {
                    pieceMoves = simplePossibleMovesForPiece(board, loc);
                    allMoves.addAll(pieceMoves);
                }
            }
        }

        for(Move posMove : allMoves)
        {
            if(posMove.isIsEat())
            {
                eatMove = new Move(posMove);
                break;
            }
        }

        ArrayList<Move> movesToSet = allMoves;

        Location eatSource = eatMove.getSourceLoc(), eatDest = eatMove.getLocTarget();
        for(Move posMove : movesToSet)
        {

            if(eatMove.getPiece() == null) // eat isn't exists
            {
                break;
            }

            Location posSource = posMove.getSourceLoc(),
                    posDest = posMove.getLocTarget();
            
            if(!posMove.equals(eatMove) && !posMove.isIsEat())
            {
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
    
    // Checks if on currentBoard the game over with tie
    public boolean checkTie()
    {
        return checkTie(currentBoard);
    }

    //Checks if on specific board game over with tie
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

    // checks if the result of logic board is win to playerSign (return true), false else
    public boolean checkWin(PieceColor playerSign)
    {
        return checkWin(currentBoard, playerSign);
    }

    //Checks if on specific board game over with win for playerSign
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

    //Gets the opponent of player
    private PieceColor getOpponent(PieceColor player)
    {
        if(player == WHITE_PLAYER)
        {
            return BLACK_PLAYER;
        }
        return WHITE_PLAYER;
    }
    
    //Gets AI move
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
            minimaxMove = minimax(player, currentBoard, true, 0, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);

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

        if(minimaxMove.isIsEat() && minimaxMove.getBurnedLoc() != null)
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

    //Gets best move by minimax (for AI player)
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
//                    System.out.println("***** " + minimaxMove);
                }

                if(minimaxMove.chaeckDepth()
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
                    //System.out.println("**** " + minimaxMove);
                }

                if(minimaxMove.chaeckDepth()
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

    //give evaluation for player on a logic board
    public int eval(PieceColor player)
    {
        return eval(currentBoard, player);
    }
    //give grade to player on specific board
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

            boolean opponentOneMoveEaten = ordinaryCouldBeEatenNextMove(board, opponentMoves),
                    opponentKingOneMoveEaten = kingsCouldBeEatenNextMove(board, opponentMoves);
            int currentMultEating = GradeForMultEatingCurrent(board, allMoves, opponentMoves), opponentMultEating = GradeForMultEatingOpponent(board, opponentMoves);
            if(opponentOneMoveEaten && !opponentKingOneMoveEaten && opponentMultEating == 0)
            {
                evalScore -= 2; // ordinary could eaten
            }

            if(((opponentKingOneMoveEaten && opponentOneMoveEaten)
                    || (opponentKingOneMoveEaten && !opponentOneMoveEaten))
                    && opponentMultEating == 0)
            {
                evalScore -= 7; // king could eaten
            }

            evalScore += currentMultEating;
            evalScore -= opponentMultEating;
        }
        return evalScore;
    }
    //eval for game and not for ai
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

            boolean currentOneMoveKing = oneMoveKingForCurrent(player, allMoves, opponentMoves),
                    opponentOneMoveKing = oneMoveKingForOpponent(opponent, opponentMoves);
            if(currentOneMoveKing)
            {
                evalScore += 6;
                System.out.println("you have one move king!\ngrade now: " + evalScore);
            }

            if(opponentOneMoveKing)
            {
                evalScore -= 6;
                System.out.println("opponent one move king!\ngrade now: " + evalScore);
            }
            sub = subPiecesOnFirstLine(board, player);
            evalScore += (sub * 2);
            System.out.println("sub of first line: " + sub + "\ngrade now: " + evalScore);

            sub = subOfPiecesInSides(board, player);
            evalScore += (sub * 1);
            System.out.println("sub of pieces in sides: " + sub + "\ngrade now: " + evalScore);

            sub = subOfMovablePieces(player, board);
            evalScore += (sub * 1);
            System.out.println("sub of movable pieces: " + sub + "\ngrade now: " + evalScore);

            boolean opponentOneMoveEaten = ordinaryCouldBeEatenNextMove(board, opponentMoves),
                    opponentKingOneMoveEaten = kingsCouldBeEatenNextMove(board, opponentMoves);
            int currentMultEating = GradeForMultEatingCurrent(board, allMoves, opponentMoves),
                    opponentMultEating = GradeForMultEatingOpponent(board, opponentMoves);

            if(opponentOneMoveEaten && !opponentKingOneMoveEaten && opponentMultEating == 0)
            {
                evalScore -= 2;
                System.out.println("ordinary could be eaten!\ngrade now: " + evalScore);
            }

            if(((opponentKingOneMoveEaten && opponentOneMoveEaten)
                    || (opponentKingOneMoveEaten && !opponentOneMoveEaten))
                    && opponentMultEating == 0)
            {
                evalScore -= 7;
                System.out.println("king could be eaten!\ngrade now: " + evalScore);
            }
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
    
    //eval calculate func
    //return sub of all pieces 
    private int subOfMovablePieces(PieceColor player, Board board)
    {
        int counterWhite = 0, counterBlack = 0;
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(!board.isEmptySquar(new Location(i, j)))
                {
                    Piece p = new Piece(board.getPiece(new Location(i, j)));
                    if(p.isWhite() && isCanDoMove(p))
                    {
                        counterWhite++;
                    }

                    if(p.isWhite() && isCanDoMove(p))
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
    
    //eval calculate func
    private int piecesOnFirstLine(Board board, PieceColor player)
    {
        int counter = 0;
        if(player == WHITE_PLAYER)
        {
            for(int i = 0; i < COLS; i++)
            {
                Location loc = new Location(ROWS - 1, i);
                if(!board.isEmptySquar(loc))
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
                if(!board.isEmptySquar(loc))
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

    //eval calculate func
    private int subPiecesOnFirstLine(Board board, PieceColor player)
    {
        PieceColor opponent = getOpponent(player);
        return piecesOnFirstLine(board, player) - piecesOnFirstLine(board, opponent);
    }

    // calculates the sub pieces that placed on the sides right and left of board between player and his opponent
    private int subOfPiecesInSides(Board board, PieceColor player)
    {
        int counterWhite = 0, counterBlack = 0;
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(!board.isEmptySquar(new Location(i, j)) && (j == 0 || j == COLS - 1))
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

    // Returns if piece of current player can make king next turn
    private boolean oneMoveKingForCurrent(PieceColor player, ArrayList<Move> currentMoves,
            ArrayList<Move> opponentMoves)
    {
        Move OMK = new Move();
        for(Move move : currentMoves)
        {
            if(move.getLocTarget().getRow() == 0 && player == WHITE_PLAYER
                    || move.getLocTarget().getRow() == ROWS - 1 && player == BLACK_PLAYER)
            {
                OMK = new Move(move);
                break;
            }

            if(move.hasAnotherMove())
            {
                for(Move mult : move.getSecondMove())
                {
                    if(mult.getLocTarget().getRow() == 0 && player == WHITE_PLAYER
                            || mult.getLocTarget().getRow() == ROWS - 1 && player == BLACK_PLAYER)
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
                if(move.isIsEat())
                {
                    if(move.getEatenLoc().isEqual(OMK.getSourceLoc()))
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
            int rowDest = move.getLocTarget().getRow();

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

            if(move.hasAnotherMove())
            {
                for(Move mult : move.getSecondMove())
                {
                    if(mult.getLocTarget().getRow() == 0 && opponent == WHITE_PLAYER
                            || mult.getLocTarget().getRow() == ROWS - 1 && opponent == BLACK_PLAYER)
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
            if(enemyMove.isIsEat())
            {
                Piece enemy = State.this.getPieceFromLoc(board, enemyMove.getEatenLoc());
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
            if(enemyMove.isIsEat())
            {
                Piece enemy = State.this.getPieceFromLoc(board, enemyMove.getEatenLoc());
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
                        if(p.getPieceColor() == player && p.isKing() && isCanDoMove(p))
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

    //Gets all possible moves that player with pieceColor playerSign can do on specific board
    public ArrayList<Move> getAllPossibleMoves(Board board, PieceColor playerSign)
    {

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
        allMoves = setBurnedLocForAllMoves(allMoves);
        return allMoves;
    }

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

    //Returns all moves that piece on loc on specific board can do
    public ArrayList<Move> possibleMovesForPiece(Board board, Location loc, boolean checkBurned)
    {
        ArrayList<Move> legalMoves = simplePossibleMovesForPiece(board, loc);

        Piece piece = board.getPiece(loc);
        //System.out.println("piece: " + piece);
        if(checkBurned)
        {
            legalMoves = setBurnedLocForAllMoves(board, piece.getPieceColor(), legalMoves);
        }

        for(Move legal : legalMoves)
        {
            if(legal.isIsEat())
            {
                ArrayList<Move> mult = getMultEating(board, legal);
                if(!mult.isEmpty())
                {
                    legal.setSecondMove(mult);
                }
            }
        }

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
            Piece tempPiece = board.getPiece(tempLoc);
            if(tempLoc.isInBounds())
            {
                if(tempPiece == null)
                {
                    posMoves.add(legalMove);
                    continue;
                }
                if(tempPiece != null)
                {
                    if(nextLoc.isInBounds())
                    {
                        if(board.getPiece(nextLoc) == null
                                && tempPiece.getPieceColor() != player
                                && !isEat) // סיטואציית אכילה
                        {
                            opponentLoc = new Location(tempLoc);
                            isEat = true;
                            legalMove.setLocTarget(nextLoc);
                            legalMove.setEatenLoc(opponentLoc);
                            legalMove.setIsEat(isEat);
                            i--;
                            j++;
                            posMoves.add(legalMove);
                            continue;
                        }
                        if(board.getPiece(nextLoc) != null) //  loc blocked with tow pieces
                        {
                            break;
                        }
                        if(tempPiece.getPieceColor() == player)
                        {
                            break;
                        }

                        if(isEat && tempPiece.getPieceColor() != player) 
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
                            legalMove.setLocTarget(nextLoc);
                            legalMove.setEatenLoc(opponentLoc);
                            legalMove.setIsEat(isEat);
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
                            legalMove.setLocTarget(nextLoc);
                            legalMove.setEatenLoc(opponentLoc);
                            legalMove.setIsEat(isEat);
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
                            legalMove.setLocTarget(nextLoc);
                            legalMove.setEatenLoc(opponentLoc);
                            legalMove.setIsEat(isEat);
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

    //Get all the locations that pieces of current player placed on currentBoard
    public ArrayList<Location> getAllCurrentLocs(PieceColor currentPlayer)
    {
        ArrayList<Location> currentLoc = new ArrayList<>();

        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Location loc = new Location(i, j);
                Piece p = currentBoard.getPiece(loc);
                if(p != null)
                {
                    if(isCanDoMove(p))
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
        return currentLoc;
    }

    //Checks if specific piece can do move
    private boolean isCanDoMove(Piece p)
    {
        Location loc = p.getPieceLoc();
        ArrayList<Move> allMoves = simplePossibleMovesForPiece(currentBoard, loc);
        if(allMoves.isEmpty())
        {
            return false;
        }

        return true;
    }

    //Get all white tiles locations
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

    //Checks if after the move is made the piece on specific board can do double eating
    public ArrayList<Move> getMultEating(Board board, Move move)
    {
        Board tempBoard = new Board(board);
        tempBoard = makeMove(tempBoard, move);
        ArrayList<Move> posMoves = simplePossibleMovesForPiece(tempBoard, move.getLocTarget());
        ArrayList<Move> multEating = new ArrayList<>();
        //System.out.println("all possible moves in multEating: ");

        for(Move pos : posMoves)
        {
            //System.out.println(pos);
            if(pos.isIsEat())
            {
                multEating.add(new Move(pos));
            }
        }
        return multEating;
    }

    // Checks if opponent can make double eating
    private boolean hasMultEatingForCurrent(ArrayList<Move> allMoves, ArrayList<Move> opponentMoves)
    {
        Move doubleEat = new Move();
        for(Move move : allMoves)
        {
            if(move.hasAnotherMove())
            {
                doubleEat = new Move(move);
                break;
            }
        }

        if(doubleEat.getPiece() != null) // if mult is exists
        {
            for(Move move : opponentMoves)
            {
                if(move.isIsEat())
                {
                    if(move.getEatenLoc().isEqual(doubleEat.getSourceLoc()))
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
                if(move.hasAnotherMove())
                {
                    mult = new Move(move);
                }
            }
            Location loc1 = mult.getEatenLoc(), loc2 = mult.getSecondMove().get(0).getEatenLoc();
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
            if(move.hasAnotherMove())
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
                if(move.hasAnotherMove())
                {
                    mult = new Move(move);
                }
            }
            Location loc1 = mult.getEatenLoc(), loc2 = mult.getSecondMove().get(0).getEatenLoc();
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
                Piece piece = currentBoard.getPiece(new Location(i, j));
                if(piece == null)
                {
                    str += "-";
                }
                else
                {
                    str += piece.getCharOfPiece();
                }
            }
            str += "\n";
        }
        return str;
    }
}
