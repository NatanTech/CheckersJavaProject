package ver1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import ver1.Piece.Player;

/**
 * Document : View Created on : 11/05/2021, 13:16:17 Author : beita
 */
public class View
{
    public static final String WIN_TITLE = "ver2";
    public static final int BUTTON_SIZE = 120;
    public static final Font FONT1 = new Font(null, Font.BOLD, 60);  // פונט לכפתורים
    public static final Font FONT2 = new Font(null, Font.BOLD, 16);  // פונט לכפתורים
    private static int ROWS = 8;  // מספר השורות במטריצה
    private static int COLS = 8;  // מספר העמודות במטריצה
    private static char BLACK_SIGN = 'b';
    private static char WHITE_SIGN = 'w';
    private static char EMPTY_SIGN = ' ';

    // משתנים
    private JFrame win;
    private JButton btnNewGame, btnAiMove, btnEval;
    private JLabel lblMsg;       // תוית להצגת תור מי לשחק
    private JButton[][] btnBoard;  // מטריצת הכפתורים
    private Client client;

    // פעולה בונה
    public View(Client client)
    {
        this.client = client;

        createGUI();
    }

    //פעולה בונה מעתיקה
    public View(View view)
    {
        this.win = view.win;
        this.btnNewGame = view.btnNewGame;
        this.btnAiMove = view.btnAiMove;
        this.btnEval = view.btnEval;
        this.lblMsg = view.lblMsg;
        this.btnBoard = view.btnBoard;
        this.client = client;
    }

    private void createGUI()
    {
        // יצירת החלון למחשבון
        // ============================================================
        win = new JFrame(WIN_TITLE);
        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // יצירת פאנל עליון לכפתורי ניהול המשחק
        // ============================================================
        JPanel pnlTop = new JPanel();
        pnlTop.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // כפתור להתחלת משחק חדש
        btnNewGame = new JButton("New Game");
        btnNewGame.setFocusable(false);
        btnNewGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                client.newGameButtonPressed();
            }
        });
        pnlTop.add(btnNewGame);

        // כפתור לשחקן ממוחשב
        btnAiMove = new JButton("AI Move");
        btnAiMove.setFocusable(false);
        btnAiMove.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                client.aiMoveButtonPressed();
            }
        });
        pnlTop.add(btnAiMove);

        btnEval = new JButton("Eval");
        btnEval.setFocusable(false);
        btnEval.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                client.evalButtonPressed();
            }
        });
        pnlTop.add(btnEval);
        // יצירת פאנל לכפתורי לוח המשחק
        // ============================================================
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new GridLayout(ROWS, COLS));

        // מערך הכפתורים של המחשבון
        btnBoard = new JButton[ROWS][COLS];

        // יצירת כל כפתורי המחשבון קביעת הפונט שלהם והוספתם לחלון על פי גריד שנקבע
        // לולאה שעוברת על כל השורות במטריצה
        for(int row = 0; row < ROWS; row++)
        {
            // לולאה שעוברת על כל העמודות
            for(int col = 0; col < COLS; col++)
            {
                // יצירת כפתור בלוח המשחק
                btnBoard[row][col] = new JButton();
                btnBoard[row][col].setFont(FONT1);
                btnBoard[row][col].setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE)); //קובע גודל כפתור
                btnBoard[row][col].setFocusable(false);
                btnBoard[row][col].setActionCommand(row + "," + col); // save indexs (row,col)

                // הוספת מאזין לאירוע לחיצה על הכפתור
                btnBoard[row][col].addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        // Tell to Controller that Board Button was pressed!
                        JButton btn = (JButton) e.getSource();
                        String indexs = btn.getActionCommand(); // get indexes (row,col)
                        int row = Integer.parseInt(indexs.substring(0, indexs.indexOf(',')));
                        int col = Integer.parseInt(indexs.substring(indexs.indexOf(',') + 1));
                        client.boardButtonPressed(new Location(row, col));
                    }
                });

                // הוספת הכפתור לגריד שבפנאל
                pnlButtons.add(btnBoard[row][col]);
            }
        }

        // יצירת תווית הסטטוס להצגת הודעות
        // ============================================================
        lblMsg = new JLabel("WHITE TURN");
        lblMsg.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
        lblMsg.setOpaque(true);
        lblMsg.setFont(FONT2);

        // הוספת כל הרכיבים לחלון הראשי
        // ============================================================
        win.add(pnlTop, BorderLayout.NORTH); // הוספת פאנל עליון לצפון החלון
        win.add(pnlButtons, BorderLayout.CENTER); // הוספת פאנל כפתורי הלוח למרכז החלון
        win.add(lblMsg, BorderLayout.SOUTH); // הוספת התוית לדרום החלון
        win.pack(); // התאמת גודל החלון לרכיבים שבתוכו
        win.setLocationRelativeTo(null); // מרכז החלון
        win.setVisible(true); // מציג את החלון על המסך
    }

    public void setup(Board board)
    {
        boolean isBlack;
        for(int row = 0; row < ROWS; row++)
        {
            isBlack = (row % 2 != 0);
            for(int col = 0; col < COLS; col++)
            {
                btnBoard[row][col].setBackground(isBlack ? Color.BLACK : Color.WHITE);
                Piece p = board.getPiece(row, col);
                char sign = EMPTY_SIGN;
                if(p != null)
                {
                    sign = p.getSignOfPiece();
                    btnBoard[row][col].setEnabled(p.isWhite());

                }
                btnBoard[row][col].setText("" + sign);
                btnBoard[row][col].setForeground(Color.WHITE);
                isBlack = !isBlack;
            }
        }

        btnNewGame.setEnabled(true);
        btnEval.setEnabled(true);

        lblMsg.setForeground(null);
        lblMsg.setText("White turn");
    }

    public void updateBoardButton(Move move)
    {
        Piece p = move.getPiece();
        Location source = move.getSource(), dest = move.getDestination();

        btnBoard[dest.getRow()][dest.getCol()].setText("" + p.getSignOfPiece());
        btnBoard[dest.getRow()][dest.getCol()].setForeground(Color.WHITE);
        btnBoard[source.getRow()][source.getCol()].setText(" ");

        if(move.getIsEat())
        {
            Location loc = new Location(move.getEnemyLoc());
            btnBoard[loc.getRow()][loc.getCol()].setText(" ");
        }

    }

    public void gameOver(String msg)
    {
        // lock all board buttons
        setBoardButtonsEnabled(false);
        btnNewGame.setEnabled(true);
        btnAiMove.setEnabled(false);
        btnEval.setEnabled(false);
        lblMsg.setForeground(Color.BLUE);
        lblMsg.setText(msg);

    }

    /**
     *
     * @param status lock or unlock all the buttons on board
     */
    private void setBoardButtonsEnabled(boolean status)
    {
        for(int row = 0; row < ROWS; row++)
        {
            for(int col = 0; col < COLS; col++)
            {
                btnBoard[row][col].setEnabled(status);
            }
        }
    }

    /**
     *
     * @param msg show msg on lblMsg
     */
    public void setLableMsg(String msg)
    {
        lblMsg.setText(msg);
    }

    public void close()
    {
        win.dispose();
    }

    /**
     *
     * @param posMoves draw all the locations in arrayList
     */
    public void paintAllpossibleMoves(ArrayList<Move> posMoves)//נבדק
    {
        for(int i = 0; i < posMoves.size(); i++)
        {
            Location loc = posMoves.get(i).getDestination();
            btnBoard[loc.getRow()][loc.getCol()].setBackground(Color.yellow);
        }
    }

    public void deleteAllDraws()
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(btnBoard[i][j].getBackground() == Color.YELLOW)
                {
                    btnBoard[i][j].setBackground(Color.BLACK);
                }
            }
        }
    }

    public void makeKing(Move move)
    {
        Location loc = move.getDestination();
        System.out.println("loc of king: " + loc);
        Piece p = move.getPiece();
        p.setKing(true);
        btnBoard[loc.getRow()][loc.getCol()].setText("" + p.getSignOfPiece());
    }

    public void setEnabledList(ArrayList<Location> locs, boolean status)
    {
        for(int i = 0; i < locs.size(); i++)
        {
            Location loc = locs.get(i);
            btnBoard[loc.getRow()][loc.getCol()].setEnabled(status);
        }
    }
}
