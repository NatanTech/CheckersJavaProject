package ver1;

import java.io.Serializable;

/**
 * Document : Game Created on : 14/11/2021, 19:48:51 Author : beita
 */
public class Game implements Serializable
{
    private Player p1;
    private Player p2;
    private Model model;
    private Player currentPlayerTurn;

    public Game(Player p1, Player p2)
    {
        this.p1 = p1;
        this.p2 = p2;

        //model = new Model();
    }

    public void runGame()
    {
        currentPlayerTurn = p1;
        while(!model.isGameOver(model.getLogicBoard()))
        {
            Move move = currentPlayerTurn.getMove();
            model.makeMove(move);

            if(model.isGameOver(model.getLogicBoard()))
            {
                p1.gameOver();
                p2.gameOver();
            }
            else
            {
                if(currentPlayerTurn == p1)
                {
                    currentPlayerTurn = p2;
                }
                else
                {
                    currentPlayerTurn = p1;
                }
            }
        }

        // save to db game status
    }
}
