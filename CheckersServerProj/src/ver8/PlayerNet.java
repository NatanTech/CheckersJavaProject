package ver8;

import java.util.ArrayList;
import ver8.Piece.PieceColor;

/**
 * PlayerNet- שחקן רשת
 * ---------------------------------------------------------------------------
 * by Amitay Agay (beitarsenal7@gmail.com) 10/11/2021
 */
public class PlayerNet extends Player
{
    private AppSocket socketToClient; // player socket
    private boolean withAI; // if played VS AI

    /**
     * Constructor to create new playerNet
     *
     * @param socketToClient socket to the player (for read and write messages)
     * @param withAI if the player pis playing against AI
     * @param id of player
     */
    public PlayerNet(AppSocket socketToClient, boolean withAI, String id)
    {
        super(id);
        this.socketToClient = socketToClient;
        this.withAI = withAI;
    }

    public AppSocket getSocketToClient()
    {
        return socketToClient;
    }

//    public void setSocketToClient(AppSocket socketToClient)
//    {
//        this.socketToClient = socketToClient;
//    }
    public boolean isWithAI()
    {
        return withAI;
    }

    @Override
    public Move getMove(ArrayList<Move> secondMoves)
    {
        Model model = this.getModel();
        //System.out.println("");
        //System.out.println("----------- #getMove in playerNet -------------");
        socketToClient.writeMessage(new Message(Constants.DELETE_DRAWINGS));
        ArrayList<Move> posMoves = new ArrayList<>();
        if(secondMoves.isEmpty())
        {
            //System.out.println("secondMove empty");
            posMoves = model.getAllPossibleMoves(model.getLogicBoard(),
                    this.getPieceColor());
        }

        else
        {
            //System.out.println("secondMove isn't empty");
            posMoves = secondMoves;
            posMoves = model.updateMoveForList(model.getLogicBoard(), this.getPieceColor(), posMoves);
        }

        System.out.println("all possible moves:");
        int i = 1;
        for(Move move : posMoves)
        {
            System.out.println("move #" + (i++) + ": " + move);
            //move.printSimpleMove();
        }

        // מחכה ללחיצה ראשונה
        Message msg = socketToClient.readMessage();
        // בודק אם השחקן התנתק לפני שעשה לחיצה אחת
        if(msg.getSubject().equals(Constants.CANCEL_EXIT))
        {
            return null;
        }

        Location loc = msg.getLoc();
        System.out.println("loc: " + loc);
        //System.out.println("board in getMove:");
        //this.getModel().getLogicBoard().printBoard();

        // בודק האם על המקום שנבחר יש חייל
        ArrayList<Move> posForPieceSelected = new ArrayList<>();
        /*= model.(this.getModel().getLogicBoard(), loc, true);*/
        for(Move move : posMoves)
        {
            if(move.getSource().isEqual(loc))
            {
                posForPieceSelected.add(move);
            }
        }

        System.out.println("posForPieceSelected: ");
        i = 1;
        for(Move move : posForPieceSelected)
        {
            System.out.println("move #" + (i++) + ": " + move);
            //move.printSimpleMove();
        }

        if(posForPieceSelected.isEmpty())
        {
            getMove(posMoves);
        }

        // יצירת מוב ומחכה ללחיצה שנייה
        Move move = new Move(model.pieceInLoc(loc), loc, null);
        Message optionsMovesMsg = new Message(Constants.OPTIONS_FOR_MOVE);
        optionsMovesMsg.setPosMoves(posForPieceSelected);
        socketToClient.writeMessage(optionsMovesMsg);

        Message msg2 = socketToClient.readMessage();
        // בודק אם השחקן התנתק לפני הלחיצה השניה
        if(msg.getSubject().equals(Constants.CANCEL_EXIT))
        {
            return null;
        }

        Location loc2 = msg2.getLoc();
        if(model.pieceInLoc(loc2) != null || move.getSource().isEqual(loc2))
        {
            getMove(posMoves);
        }

        move.setDestination(loc2);
        System.out.println("move: " + move);
        Move newMove = new Move();
        for(Move moveTemp : posForPieceSelected)
        {
            //System.out.println("moveTemp: " + moveTemp);
            if(move.equals(moveTemp))
            {
                //System.out.println("newMove updated");
                //System.out.println("moveTemp: " + moveTemp);
                newMove = new Move(moveTemp);
                //newMove.copyMove(moveTemp);

                //System.out.println("newMove after update: " + newMove);
                break;
            }
        }

        // בדיקת תקינות על המוב כולו
        System.out.println("final move selected: " + newMove);
        if(newMove.getPiece() != null)
        {
            System.out.println("legal move!");
            //System.out.println("move before update: " + newMove);
            newMove = model.updateMove(newMove);
            //System.out.println("move after update: " + newMove);
            socketToClient.writeMessage(new Message(Constants.DELETE_DRAWINGS));
            return newMove;
        }
        return getMove(posMoves);
    }

    @Override
    public void waitTurn()
    {
        socketToClient.writeMessage(new Message(Constants.WAIT_TURN));
    }

    @Override
    public void gameOver(PieceColor player)
    {
        Message msg = new Message(Constants.GAME_OVER);
        ArrayList<GameInfo> info = new ArrayList<>();
        info.add(new GameInfo("Game over- " + player + " won!"));
        msg.setGameInfo(info);
        socketToClient.writeMessage(msg);
    }

    @Override
    public void gameOverTie()
    {
        socketToClient.writeMessage(new Message(Constants.GAME_OVER_TIE));
    }

    @Override
    public void yourTurn()
    {
        //System.out.println("------------------ in yourTurn ------------------");
        Model model = this.getModel();
        ArrayList<Location> currentAndEmptyLocs = model.getAllCurrentLocs(this.getPieceColor());
        //ArrayList<Location> emptyLocs = model.getEmptyLocs();
        //currentAndEmptyLocs.addAll(emptyLocs);

        Message msg = new Message(Constants.YOUR_TURN);
        msg.setLocs(currentAndEmptyLocs);
        socketToClient.writeMessage(msg);
    }

    @Override
    public void initGame()
    {
        Message msg = new Message(Constants.INIT_GAME);
        msg.setBoard(this.getModel().getLogicBoard());
        //System.out.println("model of player " + this.getId());
        //msg.getBoard().printBoard();
        socketToClient.writeMessage(msg);
    }

    @Override
    public void updateView(Move move)
    {
        Board board = new Board(this.getModel().getLogicBoard());
        Move move2 = new Move(move);
        //System.out.println("board in  update view in playerNet " + this.getId() + ":");
        //board.printBoard();

        Message msg = new Message(Constants.UPDATE_VIEW);
        msg.setBoard(board);
        msg.setMove(move2);
        //System.out.println("\nboard in  update view in playerNet " + this.getId() + " before sending:");
        //msg.getBoard().printBoard();
        socketToClient.writeMessage(msg);
    }

    @Override
    public void youWhite()
    {
        socketToClient.writeMessage(new Message(Constants.YOU_WHITE));
    }

    @Override
    public void youBlack()
    {
        socketToClient.writeMessage(new Message(Constants.YOU_BLACK));
    }

    @Override
    public void countDownFinished()
    {
        socketToClient.readMessage();
    }

    @Override
    public String toString()
    {
        return "PlayerNet{" + "socketToClient=" + socketToClient + ", withAI=" + withAI + '}';
    }

    @Override
    public void unlockSaveGameButton()
    {
        socketToClient.writeMessage(new Message(Constants.UNLOCK_SAVE_GAME_BUTTON));
    }

    @Override
    public void yourPartner()
    {
        String color;
        if(this.getPartner().getPieceColor() == PieceColor.WHITE_PLAYER)
        {
            color = "white";
        }
        else
        {
            color = "black";
        }

        Message msg = new Message(Constants.YOUR_OPPONENT);
        String opponent = " VS " + this.getPartner().getId() + " (" + color + ")";
        LoginDetails login = new LoginDetails(opponent, null, false);
        msg.setLogin(login);
        socketToClient.writeMessage(msg);
    }

    @Override
    public void yourOpponentExit()
    {
        socketToClient.writeMessage(new Message(Constants.YOUR_OPPONENT_EXIT));
    }

    @Override
    public void selectUnfinishedGames(ArrayList<String> boards)
    {
        Message msg = new Message(Constants.SELECT_UNFINISHED_BOARD);
        msg.setBoards(boards);
        socketToClient.writeMessage(msg);
    }

    @Override
    public void unlockRegisterOptions()
    {
        socketToClient.writeMessage(new Message(Constants.UNLOCK_REGISTER_OPTIONS));
    }

    @Override
    public void serverClosed()
    {
        socketToClient.writeMessage(new Message(Constants.SERVER_CLOSED));
    }

}
