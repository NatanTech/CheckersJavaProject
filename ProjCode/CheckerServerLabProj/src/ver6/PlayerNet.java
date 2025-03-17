package ver6;

import java.util.ArrayList;
import ver6.Piece.PieceColor;

/**
 * PlayerNet- שחקן רשת
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public class PlayerNet extends Player
{
    private AppSocket socketToClient; 
    private boolean withAI; 

    public PlayerNet(AppSocket socketToClient, boolean withAI, int id)
    {
        super(id);
        this.socketToClient = socketToClient;
        this.withAI = withAI;
    }

    public AppSocket getSocketToClient()
    {
        return socketToClient;
    }

    public boolean isWithAI()
    {
        return withAI;
    }

    @Override
    public Move getMove(ArrayList<Move> secondMoves)
    {
        State model = this.getCurrentState();
        socketToClient.writeMessage(new Message(Message.DELETE_DRAWINGS));
        ArrayList<Move> posMoves = new ArrayList<>();
        if(secondMoves.isEmpty())
        {
            posMoves = model.getAllPossibleMoves(model.getCurrentBoard(),
                    this.getPieceColor());
        }

        else
        {
            posMoves = secondMoves;
            posMoves = model.updateMoveForList(model.getCurrentBoard(), this.getPieceColor(), posMoves);
        }

        System.out.println("all possible moves:");
        int i = 1;
        for(Move move : posMoves)
        {
            System.out.println("move #" + (i++) + ": " + move);
        }

        // מחכה ללחיצה ראשונה
        Message msg = socketToClient.readMessage();
        // בודק אם השחקן התנתק לפני שעשה לחיצה אחת
        if(msg.getSubject().equals(Const.CANCEL_EXIT))
        {
            return null;
        }

        Location loc = msg.getLocOfPress();
        System.out.println("loc: " + loc);
        // בודק האם על המקום שנבחר יש חייל
        ArrayList<Move> posForPieceSelected = new ArrayList<>();
        for(Move move : posMoves)
        {
            if(move.getSourceLoc().isEqual(loc))
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
        Move move = new Move(model.getPieceFromLoc(loc), loc, null);
        Message optionsMovesMsg = new Message(Message.OPTIONS_FOR_MOVE);
        optionsMovesMsg.setPosMoves(posForPieceSelected);
        socketToClient.writeMessage(optionsMovesMsg);

        Message msg2 = socketToClient.readMessage();
        // בודק אם השחקן התנתק לפני הלחיצה השניה
        if(msg.getSubject().equals(Message.CANCEL_EXIT))
        {
            return null;
        }

        Location loc2 = msg2.getLocOfPress();
        if(model.getPieceFromLoc(loc2) != null || move.getSourceLoc().isEqual(loc2))
        {
            getMove(posMoves);
        }

        move.setLocTarget(loc2);
        System.out.println("move: " + move);
        Move newMove = new Move();
        for(Move moveTemp : posForPieceSelected)
        {
            if(move.equals(moveTemp))
            {
                newMove = new Move(moveTemp);
                break;
            }
        }

        // בדיקת תקינות על המוב כולו
        System.out.println("final move selected: " + newMove);
        if(newMove.getPiece() != null)
        {
            System.out.println("legal move!");
            newMove = model.updateMove(newMove);
            socketToClient.writeMessage(new Message(Message.DELETE_DRAWINGS));
            return newMove;
        }
        return getMove(posMoves);
    }

    @Override
    public void waitTurn()
    {
        socketToClient.writeMessage(new Message(Message.WAIT_TURN));
    }

    @Override
    public void gameOver(PieceColor player)
    {
        Message msg = new Message(Message.GAME_OVER);
        String gameOverMsg="Game over- " + player + " won!";
        msg.setGameOverMsg(gameOverMsg);
        socketToClient.writeMessage(msg);
    }

    @Override
    public void gameOverTie()
    {
        socketToClient.writeMessage(new Message(Message.GAME_OVER_TIE));
    }

    @Override
    public void yourTurn()
    {
        State model = this.getCurrentState();
        ArrayList<Location> currentAndEmptyLocs = model.getAllCurrentLocs(this.getPieceColor());
        Message msg = new Message(Message.YOUR_TURN);
        msg.setPosLocs(currentAndEmptyLocs);
        socketToClient.writeMessage(msg);
    }

    @Override
    public void initGame()
    {
        Message msg = new Message(Message.INIT_GAME);
        msg.setBoard(this.getCurrentState().getCurrentBoard());
        socketToClient.writeMessage(msg);
    }

    @Override
    public void updateView(Move move)
    {
        Board board = new Board(this.getCurrentState().getCurrentBoard());
        Move move2 = new Move(move);
        Message msg = new Message(Message.UPDATE_VIEW);
        msg.setBoard(board);
        msg.setMove(move2);
        socketToClient.writeMessage(msg);
    }

    @Override
    public void youWhite()
    {
        socketToClient.writeMessage(new Message(Message.YOU_WHITE));
    }

    @Override
    public void youBlack()
    {
        socketToClient.writeMessage(new Message(Message.YOU_BLACK));
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
    public void yourPartner()
    {
        String color;
        if(this.getRival().getPieceColor() == PieceColor.WHITE_PLAYER)
        {
            color = "white";
        }
        else
        {
            color = "black";
        }

        Message msg = new Message(Message.YOUR_OPPONENT);
        PlayerType login = new PlayerType(this.getRival().getId(), false);
        msg.setLogin(login);
        socketToClient.writeMessage(msg);
    }

    @Override
    public void yourOpponentExit()
    {
        socketToClient.writeMessage(new Message(Message.YOUR_OPPONENT_EXIT));
        socketToClient.close();
    }
    
    @Override
    public void serverClosed()
    {
        socketToClient.writeMessage(new Message(Message.SERVER_CLOSED));
    }

}
