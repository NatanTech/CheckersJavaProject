package ver4_statistics;

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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
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
    public static final String WIN_TITLE = "Checkers";
    public static final int BUTTON_SIZE = 90; //גודל הכפתור
    public static final Font FONT1 = new Font(null, Font.BOLD, 40);  // פונט לכפתורים
    public static final Font FONT2 = new Font(null, Font.BOLD, 16);  // פונט לכפתורים
    public static final Font BUTTON_FONT = new Font(null, Font.BOLD, 20);  // פונט לכפתורים
    public static final int ICON_SIZE = (int) (BUTTON_SIZE * 0.70);

    private static int ROWS = 8;  // מספר השורות במטריצה
    private static int COLS = 8;  // מספר העמודות במטריצה
    private static char EMPTY_SIGN = ' '; // ייצוג למשבצת ריקה
    private ImageIcon WHITE_ICON; // אייקון לחייל לבן פשוט
    private ImageIcon WHITE_KING_ICON; // אייקון לחייל לבן מלך
    private ImageIcon BLACK_ICON; //  אייקון לחייל שחור פשוט
    private ImageIcon BLACK_KING_ICON; //אייקון לחייל שחור מלך

    // משתנים
    private JFrame win; // חלון המשחק
    private JButton btnSaveGame; // כפתור לשמירת המשחק
    private JLabel lblMsg; // תוית להצגת תור מי לשחק
    private JMenu StatsMenu; // תת תפריט לסטטיסטיקות
    private JMenu optionsMenu; //
    private JMenu userOptionsMenu;
    private JMenu aboutMenu;
    private JMenuItem gamesPlayedMenuItem;
    private JMenuItem gamesLastWeekMenuItem;
    private JMenuItem gamesVsAiMenuItem;
    private JMenuItem top5MenuItem;
    private JMenuItem deleteUserMenuItem;
    private JMenuItem deleteSavedGamedMenuItem;
    private JMenuItem ChangePwMenuItem;
    private JMenuItem forfeitMenuItem;
    private JButton[][] btnBoard;  // מטריצת הכפתורים
    private Client client;

    // פעולה בונה
    public View(Client client)
    {
        this.client = client;
        createGUI();

        WHITE_ICON = loadImage("/assets/WHITE.png", ICON_SIZE, ICON_SIZE);
        BLACK_ICON = loadImage("/assets/BLACK.png", ICON_SIZE, ICON_SIZE);
        WHITE_KING_ICON = loadImage("/assets/WHITE-KING.png", ICON_SIZE, ICON_SIZE);
        BLACK_KING_ICON = loadImage("/assets/BLACK-KING.png", ICON_SIZE, ICON_SIZE);
    }

    //פעולה בונה מעתיקה
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

//    public JButton getBtnSaveGame()
//    {
//        return btnSaveGame;
//    }
//
//    public JMenuItem getGamesPlayedMenuItem()
//    {
//        return gamesPlayedMenuItem;
//    }
//
//    public JMenuItem getDeleteUserMenuItem()
//    {
//        return deleteUserMenuItem;
//    }
//
//    public JMenuItem getDeleteSavedGamedMenuItem()
//    {
//        return deleteSavedGamedMenuItem;
//    }
//
//    public JMenuItem getChangePwMenuItem()
//    {
//        return ChangePwMenuItem;
//    }
//
//    public JMenuItem getForfeitMenuItem()
//    {
//        return forfeitMenuItem;
//    }
//
//    public JMenuItem getGamesLastWeekMenuItem()
//    {
//        return gamesLastWeekMenuItem;
//    }
    private void createGUI()
    {
        // יצירת החלון למשחק
        // ============================================================
        win = new JFrame(WIN_TITLE);
        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        win.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                client.exitClient();
            }
        });

        // יצירת התפריט
        // ============================================================
        JMenuBar menuBar = new JMenuBar();
        //menuBar.setSize(100, 100);

        // תפריטים ראשיים
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

        // תת תפריטים עבור התפריט הראשי אודות
        // ----------------------------------------
        // תת תפריט לקרדיטים
        gamesPlayedMenuItem = new JMenuItem("All games played");
        gamesPlayedMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.AllGamesPlayed();
            }
        });

        // תת תפריט ליציאה מהמשחק
        top5MenuItem = new JMenuItem("Top5");
        top5MenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.Top5();
            }
        });

        gamesLastWeekMenuItem = new JMenuItem("Games played last week");
        gamesLastWeekMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.gamesPlayedLastWeek();
            }
        });

        gamesVsAiMenuItem = new JMenuItem("Games played VS AI");
        gamesVsAiMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.gamesPlayedVsAI();
            }
        });

        StatsMenu.add(gamesPlayedMenuItem);
        StatsMenu.add(gamesLastWeekMenuItem);
        StatsMenu.add(gamesVsAiMenuItem);
        StatsMenu.add(top5MenuItem);

        deleteUserMenuItem = new JMenuItem("Delete user");
        deleteUserMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int res = JOptionPane.showConfirmDialog(win, "Do you really want to delete your account?", "Delete user", JOptionPane.YES_NO_OPTION);
                if(res == JOptionPane.YES_OPTION)
                {
                    client.deleteUser();
                }
            }
        });

        deleteSavedGamedMenuItem = new JMenuItem("Delete saved games");
        deleteSavedGamedMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                client.deleteSavedGames();
            }
        });

        ChangePwMenuItem = new JMenuItem("Change password");
        ChangePwMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changePassword("");
            }

        });

        forfeitMenuItem = new JMenuItem("Forfeit");
        forfeitMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int res = JOptionPane.showConfirmDialog(win, "You really want to forfeit? you will loose technically!", "Forfeit", JOptionPane.YES_NO_OPTION);
                if(res == JOptionPane.YES_OPTION)
                {
                    client.Forfeit();
                    win.dispose();
                }
            }
        });

        userOptionsMenu.add(deleteUserMenuItem);
        userOptionsMenu.add(deleteSavedGamedMenuItem);
        userOptionsMenu.add(ChangePwMenuItem);
        userOptionsMenu.add(forfeitMenuItem);

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

                JOptionPane.showMessageDialog(win, msg, title, JOptionPane.PLAIN_MESSAGE);
            }
        });

        // תת תפריט לקרדיטים
        JMenuItem creditsMenuItem = new JMenuItem("Credits");
        creditsMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String title = "Credits";

                String msg = "Programming:\n";
                msg += "1. Amitay Agay (beitarsenal7@gmail.com)\n";
                msg += "2. Kiryat Noar.\n";
                msg += "3. My teacher: Ilan Perets.\n\n";

                msg += "Sound & Music & Graphics:\n";
                msg += "1. freesound.org\n";
                msg += "2. UI Designer - Amitay Agay.\n";
                msg += "3. Icons & Images - Noam Cohen Ra'anana\n\n";

                msg += "All rights reserved (c) 2021\n";

                JOptionPane.showMessageDialog(win, msg, title, JOptionPane.PLAIN_MESSAGE);
            }
        });

        aboutMenu.add(rulesMenuItem);
        aboutMenu.add(creditsMenuItem);

        // תת תפריטים עבור התפריט הראשי הגדרות
        // ---------------------------------------------------------------
        // שינוי גודל לחלון
        JMenuItem setSizeMenuItem = new JMenuItem("Set win size");
        setSizeMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setWinSize();
            }
        });

        // תת תפריט צק בוקס להפעלה או הפסקה של צלילי מוסיקת הרקע
        JCheckBoxMenuItem soundMusicMenuItem = new JCheckBoxMenuItem("Sound Music");
        soundMusicMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean isMusicOn = soundMusicMenuItem.isSelected();
                //controller.setSoundMusic(isMusicOn);
            }
        });

        // תת תפריט צק בוקס להפעלה או הפסקת צלילי האפקטים
        JCheckBoxMenuItem soundEffectsMenuItem = new JCheckBoxMenuItem("Sound Effects");

        // הוספת קבוצת כפתורי רדיו
        ButtonGroup radioGroup = new ButtonGroup();

        JRadioButtonMenuItem radio1 = new JRadioButtonMenuItem("radio1");
        radio1.setSelected(true);

        JRadioButtonMenuItem radio2 = new JRadioButtonMenuItem("radio2");

        radioGroup.add(radio1);
        radioGroup.add(radio2);

        settingsMenu.add(setSizeMenuItem);
        settingsMenu.add(soundMusicMenuItem);
        settingsMenu.add(soundEffectsMenuItem);
        settingsMenu.addSeparator(); // קוו הפרדה
        settingsMenu.add(radio1);
        settingsMenu.add(radio2);

        // יצירת פאנל עליון לכפתורי ניהול המשחק
        // ============================================================
        JPanel pnlTop = new JPanel();
        pnlTop.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // כפתור להתחלת משחק חדש
        btnSaveGame = new JButton("Save & Exit");
        btnSaveGame.setFocusable(false);
        btnSaveGame.setFont(BUTTON_FONT);
        btnSaveGame.setEnabled(false);
        btnSaveGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                int res = JOptionPane.showConfirmDialog(win, "Do you really want to save the game?", "Save & Exit", JOptionPane.YES_NO_OPTION);
                if(res == JOptionPane.YES_OPTION)
                {
                    client.saveAndExitPressed();
                    win.dispose();
                }
            }
        });
        pnlTop.add(btnSaveGame);
        setEnebledRegisterOptions(false);

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
        win.setJMenuBar(menuBar); // הצמדת בר התפריטים לחלון
        win.add(pnlTop, BorderLayout.NORTH); // הוספת פאנל עליון לצפון החלון
        win.add(pnlButtons, BorderLayout.CENTER); // הוספת פאנל כפתורי הלוח למרכז החלון
        win.add(lblMsg, BorderLayout.SOUTH); // הוספת התוית לדרום החלון
        win.pack(); // התאמת גודל החלון לרכיבים שבתוכו
        win.setLocationRelativeTo(null); // מרכז החלון
        win.setVisible(true); // מציג את החלון על המסך
    }

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

    public void changePassword(String errMsg)
    {
        String[] pws = changePasswordDialog(errMsg);
        if(pws != null)
        {
            String current = pws[0], newPw = pws[1];
            client.changePassword(current, newPw);
        }
    }

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
     *
     * @param msg show msg on lblMsg
     */
    public void setLabelMsg(String msg)
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

        JOptionPane.showMessageDialog(win, games, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void printGamesLastWeek(Message msg)
    {
        String title = "Games played last week";
        String games = toStringGames(msg.getGameInfo());

        JOptionPane.showMessageDialog(win, games, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void printGamesVsAI(Message msg)
    {
        String title = "Games played VS AI";
        String games = toStringGamesVsAI(msg.getGameInfo());

        JOptionPane.showMessageDialog(win, games, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void printTop5(Message msg)
    {
        String title = "Top 5";
        String top5 = "Top 5: \n " + toStringTop5(msg.getGameInfo());

        JOptionPane.showMessageDialog(win, top5, title, JOptionPane.INFORMATION_MESSAGE);
    }

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

    private String makeFormatDate(String date)
    {
        long newDate = Long.parseLong(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy    hh:mm");
        String finalDate = formatter.format(newDate);
        return finalDate;
    }

    public void passwordChanged()
    {
        JOptionPane.showMessageDialog(win, "Your password changed!", "Password changed", JOptionPane.INFORMATION_MESSAGE);
    }

    public void yourOpponentExit()
    {
        int res = JOptionPane.showConfirmDialog(win, "Your opponent exit! you won technically", "Opponent exit", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if(res == JOptionPane.OK_OPTION || res == JOptionPane.CLOSED_OPTION)
        {
            win.dispose();
        }
        client.stopClient();
    }

    public void serverClosed()
    {
        int res = JOptionPane.showConfirmDialog(win, "Server stopped! please try connect again", "Server stopped", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        if(res == JOptionPane.OK_OPTION || res == JOptionPane.CLOSED_OPTION)
        {
            win.dispose();
        }
    }

    public void setEnebledRegisterOptions(boolean status)
    {
        gamesPlayedMenuItem.setEnabled(status);
        gamesLastWeekMenuItem.setEnabled(status);
        deleteUserMenuItem.setEnabled(status);
        deleteSavedGamedMenuItem.setEnabled(status);
        ChangePwMenuItem.setEnabled(status);
        forfeitMenuItem.setEnabled(status);
    }

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
        JOptionPane.showMessageDialog(win, "Delete saved games succeed!", "Delete succeed", JOptionPane.DEFAULT_OPTION);
    }

    /**
     * Shows JOptionpane that declared that delete all saved games of user
     * failed
     */
    public void deleteSavedGamesFailed()
    {
        JOptionPane.showMessageDialog(win, "Delete saved games failed! please try again later...", "Delete failed", JOptionPane.DEFAULT_OPTION);
    }

    /**
     * shows JOptionpane that declared that delete user succeeded
     */
    public void deleteUserSuccess()
    {
        JOptionPane.showMessageDialog(win, "Delete user succeed! the game will close...", "Delete user", JOptionPane.DEFAULT_OPTION);
        win.dispose();
    }

    /**
     * shows JOptionpane that declared that delete user failed
     */
    public void deleteUserFailed()
    {
        JOptionPane.showMessageDialog(win, "Delete user failed! please try again later...", "Delete failed", JOptionPane.DEFAULT_OPTION);
    }

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
        btnSaveGame.setEnabled(false);
        forfeitMenuItem.setEnabled(true);
    }

    // toString for allGames and gamesPlayedLastWeek
    private static String toStringGames(ArrayList<GameInfo> games)
    {
        String allGames = "opponent    winner    dateTime\n";
        allGames += "--------    ------    ---------\n";
        for(int i = 0; i < games.size(); i++)
        {
            GameInfo game = games.get(i);
            if(game.getGeneralStats() == null)
            {
                String opponent = game.getUn();
                String winner = game.getWinner();
                String date = game.getGameDate();

                String spaces = "           ";
                if(opponent.length() != 1)
                {
                    int subSpaces = spaces.length() - opponent.length() + 1;
                    spaces = "";
                    for(int j = 0; j < subSpaces; j++)
                    {
                        spaces += " ";
                    }
                }
                allGames += opponent + spaces;

                spaces = "         ";
                if(winner.length() != 1)
                {
                    int subSpaces = spaces.length() - winner.length() + 1;
                    spaces = "";
                    for(int j = 0; j < subSpaces; j++)
                    {
                        spaces += " ";
                    }
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
        String gamesVsAI = "winner    dateTime\n";
        gamesVsAI += "------    ---------\n";
        for(int i = 0; i < games.size(); i++)
        {
            String winner = games.get(i).getWinner();
            String date = games.get(i).getGameDate();
            String spaces = "         ";
            if(winner.length() != 1)
            {
                int subSpaces = spaces.length() - winner.length() + 1;
                spaces = "";
                for(int j = 0; j < subSpaces; j++)
                {
                    spaces += " ";
                }
            }
            gamesVsAI += winner + spaces + date + "\n";

        }
        return gamesVsAI;
    }

    //toString for top5 (help func)
    private String toStringTop5(ArrayList<GameInfo> games)
    {
        String top5 = "userName    wins\n";
        top5 += "--------    ------\n";
        for(int i = 0; i < 5; i++)
        {
            String un = games.get(i).getUn();
            String wins = games.get(i).getWins();
            String spaces = "         ";
            if(un.length() != 1)
            {
                spaces = "";
                for(int j = 0; j < un.length(); j++)
                {
                    spaces += " ";
                }

            }
            top5 += (i + 1) + ". " + un + spaces + wins + "\n";
        }

        return top5;
    }

    // set size of window
    private void setWinSize()
    {
        win.setSize(new Dimension(600, 800));
        win.setLocationRelativeTo(null);
    }

}
