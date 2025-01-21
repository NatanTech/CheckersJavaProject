package ver4_statistics;

import java.util.ArrayList;
import ver4_statistics.Piece.PieceColor;

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
     * @param socketToClient
     * @param withAI
     * @param id
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
    public Move getMove()
    {
        Model model = this.getModel();
        System.out.println("#getMove in playerNet");
        socketToClient.writeMessage(new Message(Constants.DELETE_DRAWS));
        ArrayList<Move> posMoves = model.getAllPossibleMoves(model.getLogicBoard(),
                this.getPieceColor());

        // מחכה ללחיצה ראשונה
        Message msg = socketToClient.readMessage();
        // בודק אם השחקן התנתק לפני שעשה לחיצה אחת
        if(msg.getSubject().equals(Constants.CANCEL_EXIT))
        {
            return null;
        }

        Location loc = msg.getLoc();
        System.out.println("loc: " + loc);

        // בודק האם על המקום שנבחר יש חייל
        ArrayList<Move> posForPieceSelected = new ArrayList<>();
        for(Move move : posMoves)
        {
            if(move.getSource().isEqual(loc))
            {
                posForPieceSelected.add(move);
            }
        }

        System.out.println("posForPieceSelected: ");
        System.out.println(posForPieceSelected);
        if(posForPieceSelected.isEmpty())
        {
            getMove();
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
        move.setDestination(loc2);
        Move newMove = new Move(null, null, null);
        for(Move moveTemp : posForPieceSelected)
        {
            if(moveTemp.isEquals(move))
            {
                newMove = new Move(moveTemp);
                break;
            }
        }

        // בדיקת תקינות על המוב כולו
        System.out.println("move: " + move);
        if(newMove.getPiece() != null)
        {
            socketToClient.writeMessage(new Message(Constants.DELETE_DRAWS));
            return newMove;
        }
        return getMove();
    }

    @Override
    public void waitTurn()
    {
        socketToClient.writeMessage(new Message(Constants.WAIT_TURN));
    }

    @Override
    public void gameOver(PieceColor player)
    {
        String msg = "Game over- " + player + " won!";
        socketToClient.writeMessage(new Message(msg));
    }

    @Override
    public void gameOverTie()
    {
        socketToClient.writeMessage(new Message(Constants.GAME_OVER_TIE));
    }

    @Override
    public void yourTurn()
    {
        Model model = this.getModel();
        ArrayList<Location> currentAndEmptyLocs = model.getAllCurrentLocs(this.getPieceColor());
        ArrayList<Location> emptyLocs = model.getEmptyLocs();
        currentAndEmptyLocs.addAll(emptyLocs);

        Message msg = new Message(Constants.YOUR_TURN);
        msg.setLocs(currentAndEmptyLocs);
        socketToClient.writeMessage(msg);
    }

    @Override
    public void initGame()
    {
        Message msg = new Message(Constants.INIT_GAME);
        msg.setBoard(this.getModel().getLogicBoard());
        System.out.println("model of player " + this.getId());
        msg.getBoard().printBoard();
        socketToClient.writeMessage(msg);
    }

    @Override
    public void updateView()
    {
        Board board = new Board(this.getModel().getLogicBoard());
        System.out.println("board in  update view in playerNet ");
        board.printBoard();

        Message msg = new Message(Constants.UPDATE_VIEW);
        msg.setBoard(board);
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
        String msg = " VS " + this.getPartner().getId() + " (" + color + ")";
        socketToClient.writeMessage(new Message(msg));
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
