package ver6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalButtonUI;

/**
 * by Natan tzuberi (tzuberinat@gmail.com) 10/10/2022
 */
public class View
{
    //constants
    public static final String WIN_TITLE = "Welcom to Checkers!"; // win title
    public static final int WIN_DEFAULT_SIZE = 600;
    public static final int BUTTON_SIZE = 65; //גודל הכפתור
    public static final Font FONT1 = new Font(null, Font.BOLD, 40);  // font to buttons
    public static final Font FONT2 = new Font(null, Font.BOLD, 16);  
    public static final Font BUTTON_FONT = new Font(null, Font.BOLD, 20);  // font to icons
    public static final int ICON_SIZE = (int) (BUTTON_SIZE * 0.9);

    private static int ROWS = 8; 
    private static int COLS = 8;  
    private static char EMPTY_SIGN = ' '; 
    private char w_ICON; // icon for white ordinary piece
    private char W_ICON; // icon for white king piece
    private char b_ICON; // icon for black ordinary piece
    private char B_ICON; // icon for black king piece

    // משתנים
    private JFrame win; 
    private JLabel lblMsg; 
    private JLabel lblMsg2;
    private JMenu aboutMenu; 
    private JMenuItem quitMenuItem; 
    private JButton[][] btnBoard; 
    private Client client; 

    // Constructor
    public View(Client client)
    {
        this.client = client;
        
        w_ICON='\u25CB'; 
        W_ICON='\u26C1'; 
        b_ICON='\u25CF'; 
        B_ICON='\u26C3'; 
        createGUI();
    }

    public JFrame getWin()
    {
        return win;
    }

    // create the GUI of the game
    private void createGUI()
    {
        // create the win of the game
        win = new JFrame(WIN_TITLE);

        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        win.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
//                if(client.exitClient())
//                    win.dispose();
                client.exitClient();
                
            }
        });

        // creates the menues
        JMenuBar menuBar = new JMenuBar();

        // main menues
        aboutMenu = new JMenu("About");
        aboutMenu.setFont(FONT2);
        JMenu settingsMenu = new JMenu(); // תפריט ראשי הגדרות ללא טקסט
        settingsMenu.setIcon(loadImage("/assets/settings.png", 15, 15)); // עם אייקון גלגל שיניים

        menuBar.add(aboutMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(settingsMenu);

        // submenu for forfeit option
        quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JLabel label = new JLabel("you will loose technically!");
                label.setFont(new Font("Arial", Font.BOLD, 18));
                int res = JOptionPane.showConfirmDialog(win, label, "Forfeit", JOptionPane.YES_NO_OPTION);
                if(res == JOptionPane.YES_OPTION)
                {
                    client.Quit();
                    win.dispose();
                }
            }
        });

        // submenues for about menu
        // submenu for game & rules
        JMenuItem rulesMenuItem = new JMenuItem("Game & Rules");
        rulesMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String title = "About Game & Rules";

                String msg = "Checkers is played by two opponents.\n" +
                "If the adjacent square contains an opponent's piece, and the square immediately beyond it is vacant, the piece may be captured (and removed from the game) by jumping over it.\n" +
                "Only the dark squares of the checkerboard are used. \n" +
                "If the player does not capture, the other player can remove the opponent's piece as penalty.\n" +
                "the player without pieces remaining, or who cannot move due to being blocked, loses the game.\n" +
                "Pieces:\n" +
                "Man:\n" +
                "An uncrowned piece (man) moves one step diagonally forwards and captures an adjacent opponent's piece by jumping over it and landing on the next square. \n" +
                "King:\n" +
                "When a man reaches the farthest row forward , it becomes a king. It is marked by placing an additional piece on top of. \n" +
                "The king has additional powers, including the ability to move backwards and, in variants where men cannot already do so, capture backwards.,\n";

                msg = strToHTML(msg);
                JLabel label = new JLabel(msg);
                label.setFont(new Font("Arial", Font.BOLD, 12));
                JOptionPane.showMessageDialog(win, label, title, -1);
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
                msg += "1. Natan Tzuberi (tzuberinat@gmail.com)\n";
                msg += "2. Kiryat Noaar Student.\n\n";
                msg += "Sound & Music & Graphics:\n";
                msg += "1. freesound.org\n";
                msg += "2. UI Designer - Natan Tzuberi\n";
                msg += "3. Icons & Images - www.flaticon.com\n\n";
                msg += "All rights reserved (c) 2022\n";
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
        aboutMenu.add(quitMenuItem);
//         תת תפריט צק בוקס להפעלה או הפסקה של צלילי מוסיקת הרקע
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

//        JRadioButtonMenuItem radio1 = new JRadioButtonMenuItem("radio1");
//        radio1.setSelected(true);

//        JRadioButtonMenuItem radio2 = new JRadioButtonMenuItem("radio2");

//        radioGroup.add(radio1);
//        radioGroup.add(radio2);

        SpinnerModel setSizeMenuItem = new SpinnerNumberModel(WIN_DEFAULT_SIZE,500,1000,10);
                // הוספת כפתור לקביעת עומק המינימקס
        JMenuItem SetRowSize = new JMenuItem("Set win size");
        JSpinner winSizeSpiner = new JSpinner(setSizeMenuItem);   
            winSizeSpiner.setBounds(100,100,50,30);  
        winSizeSpiner.addChangeListener(new ChangeListener() {  
        public void stateChanged(ChangeEvent e) {  
         setWinSize((int)(((JSpinner)e.getSource()).getValue()));
        }  
     }); 
        settingsMenu.add(soundMusicMenuItem);
        settingsMenu.add(soundEffectsMenuItem);
        settingsMenu.addSeparator(); // קוו הפרדה
//        settingsMenu.add(radio1);
//        settingsMenu.add(radio2);
        settingsMenu.addSeparator(); // קוו הפרדה
        settingsMenu.add(SetRowSize);    
        settingsMenu.add(winSizeSpiner);    
        settingsMenu.setSize(300,300);    
        settingsMenu.setLayout(null);    
        settingsMenu.setVisible(true);  
        // creates upper panel to manage the game
        JPanel pnlTop = new JPanel();
        pnlTop.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // creates panel for matrix of game board
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
        lblMsg = new JLabel("Welcome to checkers game. please login to server");
        lblMsg.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 2));
        lblMsg.setOpaque(true);
        lblMsg.setFont(FONT2);
        //create label for message
        lblMsg2 = new JLabel(" ");
        lblMsg2.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
        lblMsg2.setOpaque(true);
        lblMsg2.setFont(FONT2);
        // הוספת כל הרכיבים לחלון הראשי
        win.setJMenuBar(menuBar); // הצמדת בר התפריטים לחלון
        win.add(pnlTop, BorderLayout.NORTH); // הוספת פאנל עליון לצפון החלון
        win.add(pnlButtons, BorderLayout.CENTER); // הוספת פאנל כפתורי הלוח למרכז החלון
        win.add(lblMsg, BorderLayout.NORTH); // הוספת התוית לדרום החלון
        win.add(lblMsg2, BorderLayout.SOUTH); // הוספת התוית לדרום החלון
        win.pack(); // התאמת גודל החלון לרכיבים שבתוכו
        win.setLocationRelativeTo(null); // מרכז החלון
        win.setVisible(true); // מציג את החלון על המסך
        win.setSize(WIN_DEFAULT_SIZE,WIN_DEFAULT_SIZE);

    }

    //Updates the btnBoard
    public void updateBoard(Board board)
    {
        for(int row = 0; row < ROWS; row++)
        {
            for(int col = 0; col < COLS; col++)
            {
                Piece p = board.getPiece(row, col);

                if(p != null)
                {
                    char sign = p.getCharOfPiece();
                    char icon = charToIcon(sign);
                    btnBoard[row][col].setText(icon+"");
                    if(icon == w_ICON)
                    {
                        btnBoard[row][col].setText(w_ICON+"");
                    }
                    if(icon == b_ICON)
                    {
                        btnBoard[row][col].setText(b_ICON+"");
                    }
                    if(icon == W_ICON)
                    {
                        btnBoard[row][col].setText(W_ICON+"");
                    }
                    if(icon == B_ICON)
                    {
                        btnBoard[row][col].setText(B_ICON+"");
                    }
                }
                else
                {
                    btnBoard[row][col].setText(EMPTY_SIGN+"");
                }
            }
        }
//        setLabel2Msg(" ");
    }
    //setup the board
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

                if(p != null)
                {
                    char sign = p.getCharOfPiece();
                    char icon = charToIcon(sign);
                    btnBoard[row][col].setText(icon+"");
                    if(icon == w_ICON)
                    {
                        btnBoard[row][col].setText(w_ICON+"");
                    }
                    if(icon == b_ICON)
                    {
                        btnBoard[row][col].setText(b_ICON+"");
                    }
                    if(icon == W_ICON)
                    {
                        btnBoard[row][col].setText(W_ICON+"");
                    }
                    if(icon == B_ICON)
                    {
                        btnBoard[row][col].setText(B_ICON+"");
                    }
                }
                else
                {
                    btnBoard[row][col].setIcon(null);
                }
                btnBoard[row][col].setForeground(Color.red);

                isBlackTile = !isBlackTile;
            }
        }

        lblMsg.setForeground(null);
        lblMsg.setText("White turn");
    }

    //game over
    public void gameOver(String msg)
    {
        setBoardButtonsEnabled(false);
        lblMsg.setForeground(Color.BLUE);
        lblMsg.setText(msg);

    }

    //status lock or unlock all the buttons on board
    public void setBoardButtonsEnabled(boolean status)
    {
        for(int row = 0; row < ROWS; row++)
        {
            for(int col = 0; col < COLS; col++)
            {
//                btnBoard[row][col].setText("<html><font color = red>3</font></html>");
                btnBoard[row][col].setUI(new MetalButtonUI() {
                    protected Color getDisabledTextColor() {
                        return Color.red;
                    }
                });
                btnBoard[row][col].setEnabled(status);

            }
        }
    }

    //show msg on lblMsg
    public void setLabelMsg(String msg)
    {
        lblMsg.setText(msg);
    }
    public void setLabel2Msg(String msg)
    {
        lblMsg2.setText(msg);
        lblMsg2.setBackground(Color.red);
    }
       public void clearLabel2Msg()
    {
        lblMsg2.setText(" ");
        lblMsg2.setBackground(null);
    }
    //Close the window
    public void close()
    {
        win.dispose();
        System.exit(0);
    }

    //draw all the locations in this arrayList
    public void paintAllpossibleMoves(ArrayList<Move> posMoves)
    {
        for(int i = 0; i < posMoves.size(); i++)
        {
            Location loc = posMoves.get(i).getLocTarget();
            btnBoard[loc.getRow()][loc.getCol()].setBackground(Color.GRAY);
        }
        ArrayList<Location> locs = new ArrayList<>();
        for(Move move : posMoves)
        {
            locs.add(new Location(move.getLocTarget()));
        }
        setEnabledList(locs, true);
    }

    //Removes all paints from btnBoard (change the grey tiles to black)
    public void deleteAllPaints()
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                if(btnBoard[i][j].getBackground() == Color.GRAY)
                {
                    btnBoard[i][j].setBackground(Color.BLACK);
                    btnBoard[i][j].setEnabled(false);
                }
            }
        }
    }

    //make the piece king
    public void makeKing(Move move)
    {
        Location loc = move.getLocTarget();
        System.out.println("loc of king: " + loc);
        Piece p = move.getPiece();
        p.setKing(true);
        btnBoard[loc.getRow()][loc.getCol()].setText("" + p.getCharOfPiece());
    }

    //Locks or unlock arraylist of locations
    public void setEnabledList(ArrayList<Location> locs, boolean status)
    {
        for(int i = 0; i < locs.size(); i++)
        {
            Location loc = locs.get(i);
            btnBoard[loc.getRow()][loc.getCol()].setEnabled(status);
        }
    }

    //Loads Image from assets
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
    //show a login dialog for the player
    public Message loginDialog(String welcomMsg) throws IOException
    {

        // הצגת תמונת כניסה
        ImageIcon loginIcon = new ImageIcon(Client.class.getResource("/assets/login.jpg"));
        loginIcon = new ImageIcon(loginIcon.getImage().getScaledInstance(370, 150, Image.SCALE_SMOOTH));
        JLabel lblSplash = new JLabel(loginIcon);
        lblSplash.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JLabel welcomeLabel = new JLabel(welcomMsg);          
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD , 22));
    
        JCheckBox aiCheckBox = new JCheckBox("play against AI?", false);
        Object[] inputFields =
        {
            lblSplash,
            welcomeLabel,
            aiCheckBox,
        };

        JOptionPane optionPane = new JOptionPane(inputFields, -1, 1, null, new Object[]
        {
             "play"
        });
        
        
        JDialog dialog = optionPane.createDialog("Checkers Game");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e)
         {
           System.out.println("send cancel to server!");
           optionPane.setVisible(false);
           if(client.exitClient())
           {
               dialog.dispose();
           }

         }
        });
        dialog.setVisible(true);   
        dialog.setAlwaysOnTop(true);
        
        boolean withAI = aiCheckBox.isSelected();
        String status = (String) optionPane.getValue();
        PlayerType login = new PlayerType(withAI);
        Message msg = new Message(status);
        System.out.println("subject: " + status + ", loginDetails: " + login);
        switch(status)
        {
            case "play":
                System.out.println("send login guest to server!");
                msg.setSubject(Message.ENTER_PLAY);
                msg.setLogin(login);
                break;
        }
        System.out.println("Play with AI: " + login.getWithAI());
        return msg;

    }
    //Shows JOptionPane that declared that your opponent exit from the game
    public void yourOpponentExit()
    {
        String text = strToHTML("Your opponent Quit! you won technically");
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        int res = JOptionPane.showConfirmDialog(win, label, "Opponent exit", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if(res == JOptionPane.OK_OPTION || res == JOptionPane.CLOSED_OPTION)
        {
            win.dispose();
        }
        client.stopClient();
    }

    //Shows JOptionPane that declared that server closed
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


    // Convert char to ImageIcon:
    // w = ordinary white
    // W = white king
    // b - ordinary black
    // B - black king
    private char charToIcon(char c)
    {
        if(c == 'w')
        {
            return w_ICON;
        }
        if(c == 'W')
        {
            return W_ICON;
        }
        if(c == 'b')
        {
            return b_ICON;
        }
        if(c == 'B')
        {
            return B_ICON;
        }
        return ' ';
    }
    //start game
    public void initGame()
    {
        cleanAllBorders();
        quitMenuItem.setEnabled(true);
    }

    // unlock or lock single lock (for double eating)
    public void setEnabledSingleLoc(Location loc, boolean status)
    {
        System.out.println("set enabled " + status + ". loc: " + loc);
        btnBoard[loc.getRow()][loc.getCol()].setEnabled(status);
    }

    // set size of window
    private void setWinSize(int x)
    {
        win.setSize(new Dimension(x,x));
        win.setLocationRelativeTo(null);
    }

    public void makeBorders(Move move)
    {
        clearLabel2Msg();
        System.out.println("in makeBorder. move: " + move);
        Location source = move.getSourceLoc(), dest = move.getLocTarget(),
                burned = move.getBurnedLoc();
        btnBoard[source.getRow()][source.getCol()].setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        if(move.isIsEat())
        {
            Location enemy = move.getEatenLoc();
            btnBoard[enemy.getRow()][enemy.getCol()].setBorder(BorderFactory.createLineBorder(Color.red, 4));
            String msg= move.getPiece().getPieceColor()+" piece in location-->("+move.getLocTarget().getRow()+" , "+move.getLocTarget().getCol()+") made eat on("+enemy.getRow()+" , "+enemy.getCol()+") !";
            setLabel2Msg(msg);
            System.out.println("label2 --> "+msg);

        }

        if(move.getBurnedLoc() != null)
        {
            if(dest.isEqual(move.getBurnedLoc()))
            {
                btnBoard[dest.getRow()][dest.getCol()].setBorder(BorderFactory.createLineBorder(Color.RED, 4));
            }

            else
            {
                btnBoard[dest.getRow()][dest.getCol()].setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
                btnBoard[burned.getRow()][burned.getCol()].setBorder(BorderFactory.createLineBorder(Color.RED, 4));
                
            }
            String msg="piece in location ("+burned.getRow()+" , "+burned.getCol()+") is burned!";
            setLabel2Msg(msg);
            System.out.println("label2 --> "+msg);
        }

        else
        {
            btnBoard[dest.getRow()][dest.getCol()].setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        }
    }

    //Clean all borders on the board
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

    // receives String and return him in html
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
