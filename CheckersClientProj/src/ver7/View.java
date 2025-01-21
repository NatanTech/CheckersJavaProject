package ver7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * View of checkers game Document : View Created on : 11/05/2021, 13:16:17
 * Author : beita
 */
public class View
{
    //constants
    public static final String WIN_TITLE = "Checkers"; // win title
    public static final int BUTTON_SIZE = 80; //גודל הכפתור
    public static final Font FONT1 = new Font(null, Font.BOLD, 40);  // font to buttons
    public static final Font FONT2 = new Font(null, Font.BOLD, 16);  // font to buttons
    public static final Font BUTTON_FONT = new Font(null, Font.BOLD, 20);  // font to icons
    public static final int ICON_SIZE = (int) (BUTTON_SIZE * 0.70);

    private static int ROWS = 8;  // num of rows in matrix
    private static int COLS = 8;  // num of cols in matrix
    private static char EMPTY_SIGN = ' '; // represents empty tile
    private ImageIcon WHITE_ICON; // icon for white ordinary piece
    private ImageIcon WHITE_KING_ICON; // icon for white king piece
    private ImageIcon BLACK_ICON; // icon for black ordinary piece
    private ImageIcon BLACK_KING_ICON; // icon for black king piece

    // משתנים
    private JFrame win; // window of game
    private JButton btnSaveGame; // button to save game
    private JLabel lblMsg; // label that shows play/wait turn
    private JMenu StatsMenu; // submenu for statistics
    private JMenu optionsMenu; // main menu for statistics or user options
    private JMenu userOptionsMenu; // submenu for user optins
    private JMenu aboutMenu; // main menu for about game info
    private JMenuItem gamesPlayedMenuItem; // submenu in statistics for games played
    private JMenuItem gamesLastWeekMenuItem; // submenu in statistics for games played last week
    private JMenuItem gamesVsAiMenuItem; // submenu in statistics for games played VS AI
    private JMenuItem top5MenuItem; // submenu in statistics for top 5
    //private JMenuItem deleteUserMenuItem;
    private JMenuItem deleteSavedGamedMenuItem; // submenu in user options for delete saved games
    private JMenuItem ChangePwMenuItem; // submenu in user options for change password
    private JMenuItem forfeitMenuItem; // // submenu in user options to forfeit the game
    private JButton[][] btnBoard;  // matrix of game board
    private Client client; // client to receive and send info and commands

    // Constructor
    public View(Client client)
    {
        this.client = client;
        WHITE_ICON = loadImage("/assets/WHITE.png", ICON_SIZE, ICON_SIZE);
        BLACK_ICON = loadImage("/assets/BLACK.png", ICON_SIZE, ICON_SIZE);
        WHITE_KING_ICON = loadImage("/assets/WHITE-KING.png", ICON_SIZE, ICON_SIZE);
        BLACK_KING_ICON = loadImage("/assets/BLACK-KING.png", ICON_SIZE, ICON_SIZE);
        createGUI();
    }

    // copy constructor
    public View(View view)
    {
        this.win = view.win;
        this.lblMsg = view.lblMsg;
        this.btnBoard = view.btnBoard;
        this.btnSaveGame = view.btnSaveGame;
        this.client = view.client;
    }

    public JFrame getWin()
    {
        return win;
    }

    // create the GUI of the game
    private void createGUI()
    {
        // create the win of the game
        // ============================================================
        win = new JFrame(WIN_TITLE);
        //win.setSize(700, 700);
        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        win.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                client.exitClient();
            }
        });

        // creates the menues
        // ============================================================
        JMenuBar menuBar = new JMenuBar();
        //menuBar.setSize(100, 100);

        // main menues
        // ---------------------------------------
        StatsMenu = new JMenu("Statistics"); // תפריט ראשי אודות
        StatsMenu.setFont(FONT2);
        optionsMenu = new JMenu("Options"); // תפריט ראשי אודות
        optionsMenu.setFont(FONT2);
        userOptionsMenu = new JMenu("User options");
        userOptionsMenu.setFont(FONT2);
        aboutMenu = new JMenu("About");
        aboutMenu.setFont(FONT2);
        JMenu settingsMenu = new JMenu(); // תפריט ראשי הגדרות ללא טקסט
        settingsMenu.setIcon(loadImage("/assets/settings.png", 15, 15)); // עם אייקון גלגל שיניים

        optionsMenu.add(StatsMenu);
        menuBar.add(optionsMenu);
        optionsMenu.add(userOptionsMenu);
        //menuBar.add(Box.createHorizontalGlue());
        menuBar.add(aboutMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(settingsMenu);

        // submenues for statistics menues
        // ----------------------------------------
        // submenu for all games played statistic
        gamesPlayedMenuItem = new JMenuItem("All games played");
        gamesPlayedMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.AllGamesPlayed();
            }
        });

        // submenu for top 5 statistic
        top5MenuItem = new JMenuItem("Top5");
        top5MenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.Top5();
            }
        });

        // submenu for all games played last week statistic
        gamesLastWeekMenuItem = new JMenuItem("Games played last week");
        gamesLastWeekMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.gamesPlayedLastWeek();
            }
        });

        // submenu for all games played VS AI statistic
        gamesVsAiMenuItem = new JMenuItem("Games played VS AI");
        gamesVsAiMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.gamesPlayedVsAI();
            }
        });

        // adds all submenues to statsMenu
        StatsMenu.add(gamesPlayedMenuItem);
        StatsMenu.add(gamesLastWeekMenuItem);
        StatsMenu.add(gamesVsAiMenuItem);
        StatsMenu.add(top5MenuItem);

//        deleteUserMenuItem = new JMenuItem("Delete user");
//        deleteUserMenuItem.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(ActionEvent e)
//            {
//                String message = "Do you really want to delete your account? "
//                        + "all unfinished games and your data will removed";
//                int res = .showConfirmDialog(win, message, "Delete user", JOptionPane.YES_NO_OPTION);
//                if(res == JOptionPane.YES_OPTION)
//                {
//                    client.deleteUser();
//                }
//            }
//        });
        // submenues for userOptions menu
        // =======================================
        // submenu for delete saved games option
        deleteSavedGamedMenuItem = new JMenuItem("Delete saved games");
        deleteSavedGamedMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.deleteSavedGames();
            }
        });

        // submenu for change password option
        ChangePwMenuItem = new JMenuItem("Change password");
        ChangePwMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changePassword("");
            }

        });

        // submenu for forfeit option
        forfeitMenuItem = new JMenuItem("Forfeit");
        forfeitMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JLabel label = new JLabel("You really want to forfeit? you will loose technically!");
                label.setFont(new Font("Arial", Font.BOLD, 18));
                int res = JOptionPane.showConfirmDialog(win, label, "Forfeit", JOptionPane.YES_NO_OPTION);
                if(res == JOptionPane.YES_OPTION)
                {
                    client.Forfeit();
                    win.dispose();
                }
            }
        });

        // // submenu for change password option
        //userOptionsMenu.add(deleteUserMenuItem);
        userOptionsMenu.add(deleteSavedGamedMenuItem);
        userOptionsMenu.add(ChangePwMenuItem);
        userOptionsMenu.add(forfeitMenuItem);

        // submenues for about menu
        // ===========================
        // submenu for game & rules
        JMenuItem rulesMenuItem = new JMenuItem("Game & Rules");
        rulesMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String title = "About Game & Rules";

                String msg = "Checkers is a game for two players,\n";
                msg += "white and black. All players have 12 pieces. \n";
                msg += "All piece can move to next tile in diagonal.\n";
                msg += "When in next tile on diagonal placed enemy piece \n";
                msg += "and next tile after the enemy piece is Empty, your \n";
                msg += "piece can eat the enemy piece- can skip to the empty \n";
                msg += "tile and the enemy piece out.\n";
                msg += "When piece of specific player comes to the end \n";
                msg += "of board, the piece becomes king that can moves \n";
                msg += "any tile on all diagonals.\n";
                msg += "The player who succeeds to eat all pieces of\n";
                msg += "the opponent win the game. If both players have\n";
                msg += "same number of kings without ordinary pieces- if after\n";
                msg += "5 turns the game isn't advanced (game over or at least \n";
                msg += "piece eaten) the game over with tie. enjoy and good luck! \n\n";

                msg = strToHTML(msg);
                JLabel label = new JLabel(msg);
                label.setFont(new Font("Arial", Font.BOLD, 18));

                JOptionPane.showMessageDialog(win, label, title, -1);
                //JOptionPane.showMessageDialog(win, msg, title, JOptionPane.PLAIN_MESSAGE);
            }
        });

        // submenu for credits
        JMenuItem creditsMenuItem = new JMenuItem("Credits");
        creditsMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String title = "Credits";

                String msg = "Programming:\n";
                msg += "1. Amitay Agai (beitarsenal7@gmail.com)\n";
                msg += "2. Kiryat Noar.\n";
                msg += "3. My teacher: Ilan Perets.\n";
                msg += "3. My pieces icons: Noam Cohen (behind Bennet).\n\n";
                //msg += "Sound & Music & Graphics:\n";
                //msg += "1. freesound.org\n";
                //msg += "2. UI Designer - Amitay Agay.\n";
                //msg += "3. Icons & Images - Noam Cohen Ra'anana\n\n";

                msg += "All rights reserved (c) 2021\n";

                msg = strToHTML(msg);
                JLabel label = new JLabel(msg);
                label.setFont(new Font("Arial", Font.BOLD, 18));

                JOptionPane.showMessageDialog(win, label, title, -1);
                //JOptionPane.showMessageDialog(win, msg, title, JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Adds all submenues to About menu
        aboutMenu.add(rulesMenuItem);
        aboutMenu.add(creditsMenuItem);

        // submenues for settings menu
        // ---------------------------------------------------------------
        // submenues for set window size option
        JMenuItem setSizeMenuItem = new JMenuItem("Set win size");
        setSizeMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setWinSize();
            }
        });

        settingsMenu.add(setSizeMenuItem);

        // תת תפריט צק בוקס להפעלה או הפסקה של צלילי מוסיקת הרקע
//        JCheckBoxMenuItem soundMusicMenuItem = new JCheckBoxMenuItem("Sound Music");
//        soundMusicMenuItem.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(ActionEvent e)
//            {
//                boolean isMusicOn = soundMusicMenuItem.isSelected();
//                //controller.setSoundMusic(isMusicOn);
//            }
//        });
//
//        // תת תפריט צק בוקס להפעלה או הפסקת צלילי האפקטים
//        JCheckBoxMenuItem soundEffectsMenuItem = new JCheckBoxMenuItem("Sound Effects");
//
//        // הוספת קבוצת כפתורי רדיו
//        ButtonGroup radioGroup = new ButtonGroup();
//
//        JRadioButtonMenuItem radio1 = new JRadioButtonMenuItem("radio1");
//        radio1.setSelected(true);
//
//        JRadioButtonMenuItem radio2 = new JRadioButtonMenuItem("radio2");
//
//        radioGroup.add(radio1);
//        radioGroup.add(radio2);
//
//        settingsMenu.add(soundMusicMenuItem);
//        settingsMenu.add(soundEffectsMenuItem);
//        settingsMenu.addSeparator(); // קוו הפרדה
//        settingsMenu.add(radio1);
//        settingsMenu.add(radio2);
        // creates upper panel to manage the game
        // ============================================================
        JPanel pnlTop = new JPanel();
        pnlTop.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // SAVE & EXIT button
        btnSaveGame = new JButton("Save & Exit");
        btnSaveGame.setFocusable(false);
        btnSaveGame.setFont(BUTTON_FONT);
        btnSaveGame.setEnabled(false);
        btnSaveGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                JLabel label = new JLabel("Do you really want to save the game?");
                label.setFont(new Font("Arial", Font.BOLD, 18));

                //JOptionPane.showMessageDialog(win, label, title, -1);
                int res = JOptionPane.showConfirmDialog(win, label, "Save & Exit", JOptionPane.YES_NO_OPTION);
                if(res == JOptionPane.YES_OPTION)
                {
                    client.saveAndExitPressed();
                    win.dispose();
                }
            }
        });

        pnlTop.add(btnSaveGame);
        setEnebledRegisterOptions(false);

        // creates panel for matrix of game board
        // ============================================================
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new GridLayout(ROWS, COLS));

        // מערך הכפתורים של המחשבון
        btnBoard = new JButton[ROWS][COLS];

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

        // creates label to shows play/ wait turn
        // ============================================================
        lblMsg = new JLabel("Welcome to checkers game. please login to server");
        lblMsg.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
        lblMsg.setOpaque(true);
        lblMsg.setFont(FONT2);

        // הוספת כל הרכיבים לחלון הראשי
        // ============================================================
        win.setJMenuBar(menuBar); // הצמדת בר התפריטים לחלון
        win.add(pnlTop, BorderLayout.NORTH); // הוספת פאנל עליון לצפון החלון
        win.add(pnlButtons, BorderLayout.CENTER); // הוספת פאנל כפתורי הלוח למרכז החלון
        win.add(lblMsg, BorderLayout.SOUTH); // הוספת התוית לדרום החלון
        win.pack(); // התאמת גודל החלון לרכיבים שבתוכו
        win.setLocationRelativeTo(null); // מרכז החלון
        win.setVisible(true); // מציג את החלון על המסך
    }

    /**
     * Updates the btnBoard like board
     *
     * @param board the func updates the btnBoard like him
     */
    public void updateBoard(Board board)
    {
        for(int row = 0; row < ROWS; row++)
        {
            for(int col = 0; col < COLS; col++)
            {
                Piece p = board.getPiece(row, col);
                char sign = EMPTY_SIGN;

                if(p != null)
                {
                    sign = p.getSignOfPiece();
                    ImageIcon icon = charToIcon(sign);
                    btnBoard[row][col].setIcon(icon);
                }
                else
                {
                    btnBoard[row][col].setIcon(null);
                }
            }
        }
    }

    /**
     * Shows window for change password and send the details to client (that
     * send to server and wait for comment)
     *
     * @param errMsg error message to show (if un or pw isn't correct)
     */
    public void changePassword(String errMsg)
    {
        String[] pws = changePasswordDialog(errMsg);
        if(pws != null)
        {
            String current = pws[0], newPw = pws[1];
            client.changePassword(current, newPw);
        }
    }

    /**
     * Shows window for change password (need to enter userName, current pw and
     * new pw)
     *
     * @param errMsg
     * @return
     */
    public String[] changePasswordDialog(String errMsg)
    {
        JLabel errLabel = new JLabel(errMsg);          // להצגת הודעת שגיאה
        errLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errLabel.setForeground(Color.RED);
        errLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD | Font.ITALIC, 12));

        JPanel pnl1 = new JPanel(new GridLayout(1, 2));
        JTextField current = new JTextField("");
        pnl1.add(new JLabel("current password:"));
        pnl1.add(current);
        current.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                //errLabel.setText(" ");
            }
        });

        JPanel pnl2 = new JPanel(new GridLayout(1, 2));
        JTextField newPw = new JTextField("");
        pnl2.add(new JLabel("new password:"));
        pnl2.add(newPw);
        newPw.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                //errLabel.setText(" ");
            }
        });

        Object[] changePw =
        {
            errLabel, pnl1, pnl2
        };
        int res = JOptionPane.showConfirmDialog(win, changePw, "Change password", JOptionPane.OK_CANCEL_OPTION);

        if(res == JOptionPane.OK_OPTION)
        {
            String currentPassword = current.getText(), newPassword = newPw.getText();
            String[] pws =
            {
                currentPassword, newPassword
            };
            return pws;
        }

        else
        {
            return null;
        }
    }

    /**
     * Makes setup for btnBoard
     *
     * @param board Makes setup for btnBoard like him
     */
    public void setup(Board board)
    {
        boolean isBlackTile;
        for(int row = 0; row < ROWS; row++)
        {
            isBlackTile = (row % 2 != 0);
            for(int col = 0; col < COLS; col++)
            {
                btnBoard[row][col].setBackground(isBlackTile ? Color.BLACK : Color.WHITE);
                Piece p = board.getPiece(row, col);
                char sign = EMPTY_SIGN;

                if(p != null)
                {
                    sign = p.getSignOfPiece();
                    ImageIcon icon = charToIcon(sign);
                    btnBoard[row][col].setIcon(icon);
                    if(icon == WHITE_ICON)
                    {
                        btnBoard[row][col].setDisabledIcon(WHITE_ICON);
                    }
                    else
                    {
                        btnBoard[row][col].setDisabledIcon(BLACK_ICON);
                    }
                }
                else
                {
                    btnBoard[row][col].setIcon(null);
                }
                btnBoard[row][col].setForeground(Color.WHITE);
                isBlackTile = !isBlackTile;
            }
        }

        btnSaveGame.setEnabled(true);
        lblMsg.setForeground(null);
        lblMsg.setText("White turn");
    }

//    /**
//     * Receive board and update the btnBoard like this board
//     *
//     * @param board the func update btnBoard like him
//     */
//    public void updateBoardButton(Board board)
//    {
//        System.out.println("board in update view: ");
//        board.printBoard();
//        for(int row = 0; row < ROWS; row++)
//        {
//            for(int col = 0; col < COLS; col++)
//            {
//                Piece p = board.getPiece(row, col);
//                char sign = EMPTY_SIGN;
//
//                if(p != null)
//                {
//                    sign = p.getSignOfPiece();
//                    ImageIcon icon = charToIcon(sign);
//                    btnBoard[row][col].setIcon(icon);
//                }
//                else
//                {
//                    btnBoard[row][col].setIcon(null);
//                }
//            }
//        }
//    }
    /**
     *
     * @param msg
     */
    public void gameOver(String msg)
    {
        // lock all board buttons
        setBoardButtonsEnabled(false);
        //btnNewGame.setEnabled(true);
        //btnAiMove.setEnabled(false);
        //btnEval.setEnabled(false);
        lblMsg.setForeground(Color.BLUE);
        lblMsg.setText(msg);

    }

    /**
     *
     * @param status lock or unlock all the buttons on board
     */
    public void setBoardButtonsEnabled(boolean status)
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
     * Change the text on labelMsg to another text
     *
     * @param msg show msg on lblMsg
     */
    public void setLabelMsg(String msg)
    {
        lblMsg.setText(msg);
    }

    /**
     * Closes the window
     */
    public void close()
    {
        win.dispose();
    }

    /**
     * Paints all possible moves that specific piece can make
     *
     * @param posMoves draw all the locations in this arrayList
     */
    public void paintAllpossibleMoves(ArrayList<Move> posMoves)
    {
        for(int i = 0; i < posMoves.size(); i++)
        {
            Location loc = posMoves.get(i).getDestination();
            btnBoard[loc.getRow()][loc.getCol()].setBackground(Color.yellow);
        }
        ArrayList<Location> locs = new ArrayList<>();
        for(Move move : posMoves)
        {
            locs.add(new Location(move.getDestination()));
        }
        setEnabledList(locs, true);
    }

    /**
     * Removes all paints from btnBoard (change the yellow tiles to black)
     */
    public void deleteAllPaints()
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(btnBoard[i][j].getBackground() == Color.YELLOW)
                {
                    btnBoard[i][j].setBackground(Color.BLACK);
                    btnBoard[i][j].setEnabled(false);
                }
            }
        }
    }

    /**
     * Makes the piece that make move to king
     *
     * @param move piece that make the move and makes him king
     */
    public void makeKing(Move move)
    {
        Location loc = move.getDestination();
        System.out.println("loc of king: " + loc);
        Piece p = move.getPiece();
        p.setKing(true);
        btnBoard[loc.getRow()][loc.getCol()].setText("" + p.getSignOfPiece());
    }

    /**
     * Locks or unlock arraylist of locations
     *
     * @param locs arraylist of locations that lock or unlock
     * @param status true to unlock, false to lock
     */
    public void setEnabledList(ArrayList<Location> locs, boolean status)
    {
        for(int i = 0; i < locs.size(); i++)
        {
            Location loc = locs.get(i);
            btnBoard[loc.getRow()][loc.getCol()].setEnabled(status);
        }
    }

    /**
     * Loads ImageIcon from assets
     *
     * @param fileName the path of the file
     * @param width of ImageIcon
     * @param height of ImageIcon
     * @return ImageIcon that has fileName path with width X height size
     */
    private ImageIcon loadImage(String fileName, int width, int height)
    {
        // טעינת התמונה מתוך הקובץ שנימצא בתקיית הנכסים
        ImageIcon imgIcon = new ImageIcon(getClass().getResource(fileName));

        if(width != -1 || height != -1)
        {
            imgIcon = new ImageIcon(imgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        }

        return imgIcon;
    }

    /**
     * Shows login window to connect to server and start play
     *
     * @param errMsg shows error message on errLabel
     * @return message with the choose of client and login details (login as
     * user, as guest or exit from login process)
     * @throws IOException if can't read or send message
     */
    public Message loginDialog(String errMsg) throws IOException
    {

        // הצגת תמונת כניסה
        ImageIcon loginIcon = new ImageIcon(Client.class.getResource("/assets/login.png"));
        loginIcon = new ImageIcon(loginIcon.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH));
        JLabel lblSplash = new JLabel(loginIcon);
        lblSplash.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));

        // שדות קלט עבור קליטת נתונים
        JLabel errLabel = new JLabel(errMsg);          // להצגת הודעת שגיאה
        errLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errLabel.setForeground(Color.RED);
        errLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD | Font.ITALIC, 12));

        JTextField unField = new JTextField("");   // לקליטת שם המשתמש
        unField.setForeground(Color.BLUE);

        unField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                //errLabel.setText(" ");
            }
        });

        JTextField pwField = new JTextField("");   // לקליטת הסיסמה
        pwField.setForeground(Color.BLUE);
        pwField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                //errLabel.setText(" ");
            }
        });

        JCheckBox aiCheckBox = new JCheckBox("Play with AI", false);
        Object[] inputFields =
        {
            lblSplash,
            errLabel,
            "Enter User Name",
            unField,
            "Enter Password",
            pwField,
            aiCheckBox,
        };

        JOptionPane optionPane = new JOptionPane(inputFields, -1, 1, null, new Object[]
        {
            "Login", "Guest", "Cancel & Exit"
        });

        JDialog dialog = optionPane.createDialog("Login");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);  // WILL BLOCK the program
        dialog.dispose();   // close dialog

        String userName = unField.getText(), password = pwField.getText();
        boolean withAI = aiCheckBox.isSelected();
        String status = (String) optionPane.getValue();

        LoginDetails login = new LoginDetails(userName, password, withAI);
        Message msg = new Message(status);

        System.out.println("subject: " + status + ", loginDetails: " + login);
        switch(status)
        {

            case "Login":
                System.out.println("send login details to server!");
                msg.setSubject(Constants.LOGIN_AS_USER);
                msg.setLogin(login);
                //clientSocket.writeMessage(msg);
                break;

            case "Guest":
                System.out.println("send login guest to server!");
                msg.setSubject(Constants.LOGIN_AS_GUEST);
                msg.setLogin(login);
                //clientSocket.writeMessage(msg);
                break;

            case "Cancel & Exit":
                System.out.println("send cancel to server!");
                msg.setSubject(Constants.CANCEL_EXIT);
                optionPane.setVisible(false);
                break;
        }
        // שליפת שם המשתמש והסיסמה והשדות הקלט
        System.out.println("un: " + login.getUserName());
        System.out.println("pw: " + login.getPassword());
        System.out.println("Play with AI: " + login.isWithAI());
        return msg;

    }

    /**
     * Shows all game played statistic of specific player
     *
     * @param msg message with details of this statistic
     */
    public void printAllGamesPlayed(Message msg)
    {
        String title = "All games played";
        String games = "";
        if(msg.getGameInfo().isEmpty())
        {
            games = "No game played yet! please start play";
        }
        else
        {
            games = toStringGames(msg.getGameInfo());
        }

        games = strToHTML(games);
        JLabel label = new JLabel(games);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JOptionPane.showMessageDialog(win, label, title, -1);
    }

    /**
     * Shows all game played last week statistic of specific player
     *
     * @param msg message with details of this statistic
     */
    public void printGamesLastWeek(Message msg)
    {
        String title = "Games played last week";
        String games = toStringGames(msg.getGameInfo());
        games = strToHTML(games);
        JLabel label = new JLabel(games);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        JOptionPane.showMessageDialog(win, label, title, -1);
    }

    /**
     * Shows all game played VS AI statistic of specific player
     *
     * @param msg message with details of this statistic
     */
    public void printGamesVsAI(Message msg)
    {
        String title = "Games played VS AI";
        String games = toStringGamesVsAI(msg.getGameInfo());

        games = strToHTML(games);
        JLabel label = new JLabel(games);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JOptionPane.showMessageDialog(win, label, title, -1);
        //JOptionPane.showMessageDialog(win, games, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows top 5 of the game statistic
     *
     * @param msg message with details of this statistic
     */
    public void printTop5(Message msg)
    {
        String title = "Top 5";
        String top5 = toStringTop5(msg.getGameInfo());

        top5 = strToHTML(top5);
        JLabel label = new JLabel(top5);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JOptionPane.showMessageDialog(win, label, title, -1);
        //JOptionPane.showMessageDialog(win, top5, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows window with all unfinished board to select one of them
     *
     * @param dateBoards arraylist of all dates that boards saved
     * @return
     */
    public String selectUnfinishedBoard(ArrayList<String> dateBoards)
    {
        JPanel pnl = new JPanel(new GridLayout(1, 2));
        String[] formatDates = new String[dateBoards.size()];
        for(int i = 0; i < dateBoards.size(); i++)
        {

            //System.out.println("board before casting: \n" + arrBoards[i] + "\n");
            if(!dateBoards.get(i).equals("New game"))
            {
                System.out.println("date: " + dateBoards.get(i));
                String formatDate = makeFormatDate(dateBoards.get(i));
                formatDates[i] = formatDate;
            }
            else
            {
                formatDates[i] = dateBoards.get(i);
            }

            System.out.println("board date: \n" + formatDates[i]);
        }

        JComboBox cb = new JComboBox(formatDates);
        pnl.add(new JLabel("choose board:"));
        pnl.add(cb);

        JOptionPane jop = new JOptionPane(pnl, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog dialog = jop.createDialog(win, "Select unfinished board");
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);

        int res = cb.getSelectedIndex();
        String boardSelected = dateBoards.get(res);
        System.out.println("board selected: \n" + boardSelected);
        return boardSelected;
    }

    // Converts date as long to date in format
    // dd/M/yyyy    hh:mm
    private String makeFormatDate(String date)
    {
        long newDate = Long.parseLong(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy    hh:mm");
        String finalDate = formatter.format(newDate);
        return finalDate;
    }

    /**
     * Shows JOptionPane that declared that the password changed successfully
     */
    public void passwordChanged()
    {
        String text = strToHTML("Your password has changed!");
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JOptionPane.showConfirmDialog(win, label, "Password changed", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        //JOptionPane.showMessageDialog(win, "Your password changed!", "Password changed", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows JOptionPane that declared that your opponent exit from the game
     */
    public void yourOpponentExit()
    {
        String text = strToHTML("Your opponent exit! you won technically");
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        //JOptionPane.showConfirmDialog(win, label, "Opponent exit", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        int res = JOptionPane.showConfirmDialog(win, label, "Opponent exit", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if(res == JOptionPane.OK_OPTION || res == JOptionPane.CLOSED_OPTION)
        {
            win.dispose();
        }
        client.stopClient();
    }

    /**
     * Shows JOptionPane that declared that server closed
     */
    public void serverClosed()
    {
        String text = strToHTML("Server stopped! please try connect again");
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        int res = JOptionPane.showConfirmDialog(win, label, "Server stopped", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        if(res == JOptionPane.OK_OPTION || res == JOptionPane.CLOSED_OPTION)
        {
            win.dispose();
        }
    }

    /**
     * Locks or unlocks register user options (like all games played statistic)
     *
     * @param status
     */
    public void setEnebledRegisterOptions(boolean status)
    {
        gamesPlayedMenuItem.setEnabled(status);
        gamesLastWeekMenuItem.setEnabled(status);
        gamesVsAiMenuItem.setEnabled(status);
        //deleteUserMenuItem.setEnabled(status);
        deleteSavedGamedMenuItem.setEnabled(status);
        ChangePwMenuItem.setEnabled(status);
        forfeitMenuItem.setEnabled(status);
    }

    // Convert char to ImageIcon:
    // w = ordinary white
    // W = white king
    // b - ordinary black
    // B - black king
    private ImageIcon charToIcon(char c)
    {
        if(c == 'w')
        {
            return WHITE_ICON;
        }
        if(c == 'W')
        {
            return WHITE_KING_ICON;
        }
        if(c == 'b')
        {
            return BLACK_ICON;
        }
        if(c == 'B')
        {
            return BLACK_KING_ICON;
        }
        return null;
    }

    /**
     * Show JOptionpane that declared that all saved games of user delete
     * succeessfully
     */
    public void deleteSavedGamesSuccess()
    {
        String text = strToHTML("Delete saved games succeed!");
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JOptionPane.showMessageDialog(win, label, "Delete succeed", JOptionPane.DEFAULT_OPTION);
    }

    /**
     * Shows JOptionpane that declared that delete all saved games of user
     * failed
     */
    public void deleteSavedGamesFailed()
    {
        String text = strToHTML("Delete saved games failed! please try again later...");
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JOptionPane.showMessageDialog(win, label, "Delete failed", JOptionPane.DEFAULT_OPTION);
    }

//    /**
//     * shows JOptionpane that declared that delete user succeeded
//     */
//    public void deleteUserSuccess()
//    {
//
//        JOptionPane.showMessageDialog(win, "Delete user succeed! the game will close...", "Delete user", JOptionPane.DEFAULT_OPTION);
//        win.dispose();
//    }
//
//    /**
//     * shows JOptionpane that declared that delete user failed
//     */
//    public void deleteUserFailed()
//    {
//        JOptionPane.showMessageDialog(win, "Delete user failed! please try again later...", "Delete failed", JOptionPane.DEFAULT_OPTION);
//    }
    /**
     * unlocks the SAVE & EXIT button
     */
    public void unlockSaveGameButton()
    {
        btnSaveGame.setEnabled(true);
    }

    /**
     * Locks SAVE & EXIT button and unlock forfeit option
     */
    public void initGame()
    {
        cleanAllBorders();
        btnSaveGame.setEnabled(false);
        forfeitMenuItem.setEnabled(true);
    }

    // toString for allGames and gamesPlayedLastWeek
    private static String toStringGames(ArrayList<GameInfo> games)
    {
        String allGames = "opponent            winner               dateTime\n";
        allGames += "-------------            ---------                  -------------\n";
        for(int i = 0; i < games.size(); i++)
        {
            GameInfo game = games.get(i);
            if(game.getGeneralStats() == null)
            {
                String opponent = game.getUn();
                String winner = game.getWinner();
                String date = game.getGameDate();

                String spaces = "              ";
                if(opponent.length() != 1)
                {
                    int subSpaces = spaces.length() - opponent.length();
                    spaces = "";
                    for(int j = 0; j < subSpaces; j++)
                    {
                        spaces += "  ";
                        if(opponent.startsWith("GUEST") && j == subSpaces - 2)
                        {
                            break;
                        }
                    }
                }
                allGames += opponent + spaces;

                spaces = "                        ";
                if(winner.length() != 1)
                {
                    int subSpaces = spaces.length() - winner.length() - 10;
                    spaces = "";
                    for(int j = 0; j < subSpaces; j++)
                    {
                        spaces += "  ";
                    }
                }

                if(winner.startsWith("GUEST"))
                {
                    spaces = "          ";
                }

                allGames += winner + spaces + date + "\n";
            }

            else
            {
                String info = game.getGeneralStats();
                allGames += "\n" + info;
            }
        }

        return allGames;
    }

    // toString for gamesVsAI func
    private String toStringGamesVsAI(ArrayList<GameInfo> games)
    {
        String gamesVsAI = "winner            dateTime\n";
        gamesVsAI += "----------            ---------------\n";

        for(int i = 0; i < games.size(); i++)
        {
            String winner = games.get(i).getWinner();
            String date = games.get(i).getGameDate();
            String spaces = "                      ";
            if(winner.length() != 1)
            {
                String newSpaces = spaces.substring(0, (spaces.length() - winner.length()) - 3);
                spaces = newSpaces;
            }

            if(winner.startsWith("AI"))
            {
                spaces = "                    ";
            }
            gamesVsAI += winner + spaces + date + "\n";

        }
        return gamesVsAI;
    }

    //toString for top5 (help func)
    private String toStringTop5(ArrayList<GameInfo> games)
    {
//        System.out.println("in toStringTop5:");
//        for(GameInfo game : games)
//        {
//            System.out.println(game);
//        }

        String top5 = "userName    wins\n";
        top5 += "---------------    ---------\n";

        for(int i = 0; i < games.size(); i++)
        {
            String un = games.get(i).getUn();
            String wins = games.get(i).getWins();
            //System.out.println("winner #" + i + ": " + winner);
            //System.out.println("date #" + i + ": " + date);
            String spaces = "                  ";
            if(un.length() != 1)
            {
                String newSpaces = spaces.substring(0, (spaces.length() - un.length()) - 2);
                spaces = newSpaces;
            }

            top5 += (i + 1) + ". " + un + spaces + wins + "\n";

        }
        return top5;
    }

    // unlock or lock single lock (for double eating)
    public void setEnabledSingleLoc(Location loc, boolean status)
    {
        System.out.println("set enabled " + status + ". loc: " + loc);
        btnBoard[loc.getRow()][loc.getCol()].setEnabled(status);
    }

    // set size of window
    private void setWinSize()
    {
//        for(int i = 0; i < ROWS; i++)
//        {
//            for(int j = 0; j < COLS; j++)
//            {
//                btnBoard[i][j].setSize(new Dimension(BUTTON_SIZE - 10, BUTTON_SIZE - 10));
//            }
//        }

        win.setSize(new Dimension(662, 810));
        //win.setSize(new Dimension(750, 900));
        win.setLocationRelativeTo(null);
    }

    /**
     * Makes the pieces visible (not in gray)
     */
    public synchronized void setDisabledIcons()
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(btnBoard[i][j].getIcon() != null)
                {
                    System.out.print("loc: (" + i + ", " + j + ") ");
                    if(btnBoard[i][j].getIcon() == WHITE_ICON)
                    {
                        System.out.println("Icon: WHITE_ICON");
                        btnBoard[i][j].setDisabledIcon(WHITE_ICON);
                    }

                    if(btnBoard[i][j].getIcon() == WHITE_KING_ICON)
                    {
                        System.out.println("Icon: WHITE_KING_ICON");
                        btnBoard[i][j].setDisabledIcon(WHITE_KING_ICON);
                    }

                    if(btnBoard[i][j].getIcon() == BLACK_ICON)
                    {
                        System.out.println("Icon: BLACK_ICON");
                        btnBoard[i][j].setDisabledIcon(BLACK_ICON);
                    }

                    if(btnBoard[i][j].getIcon() == BLACK_KING_ICON)
                    {
                        System.out.println("Icon: BLACK_KING_ICON");
                        btnBoard[i][j].setDisabledIcon(BLACK_KING_ICON);
                    }
                }
            }
        }
    }

    /**
     * Receive move and make yellow border on destination loc and red border on
     * burned loc (if exists)
     *
     * @param move
     */
    public void makeBorders(Move move)
    {
        System.out.println("in makeBorder. move: " + move);
        Location source = move.getSource(), dest = move.getDestination(),
                burned = move.getBurnedLoc();
        btnBoard[source.getRow()][source.getCol()].setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
        if(move.isEat())
        {
            Location enemy = move.getEnemyLoc();
            btnBoard[enemy.getRow()][enemy.getCol()].setBorder(BorderFactory.createLineBorder(Color.orange, 4));
        }

        if(move.getBurnedLoc() != null)
        {
            if(dest.isEqual(move.getBurnedLoc()))
            {
                btnBoard[dest.getRow()][dest.getCol()].setBorder(BorderFactory.createLineBorder(Color.RED, 4));
            }

            else
            {
                btnBoard[dest.getRow()][dest.getCol()].setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
                btnBoard[burned.getRow()][burned.getCol()].setBorder(BorderFactory.createLineBorder(Color.RED, 4));
            }
        }

        else
        {
            btnBoard[dest.getRow()][dest.getCol()].setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
        }
    }

    /**
     * Cleans all borders on the board
     */
    public void cleanAllBorders()
    {
        System.out.println("in cleanAllBorders()...");
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(btnBoard[i][j].getBorder() != null)
                {
                    btnBoard[i][j].setBorder(null);
                }

            }
        }
    }

    // receives String and return him
    //in HTML model
    private String strToHTML(String str)
    {
        String html = "<html>";
        for(int i = 0; i < str.length(); i++)
        {
            if(str.charAt(i) == '\n')
            {
                html += "<br>";
            }

            if(str.charAt(i) == ' ')
            {
                html += "&nbsp;";
            }

            else
            {
                html += str.charAt(i);
            }
        }

        html += "<html/>";
        return html;
    }
}
