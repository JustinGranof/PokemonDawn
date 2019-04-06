/**
 * Bag.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * Bag class.
 */

//import required libraries
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.*;
import java.io.IOException;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//Bag panel displayed when bag is opened
public class Bag extends JPanel implements ActionListener {
    
    //key variables
    private Game game;
    private JList<String> list;
    private static LineBorder selected = new LineBorder(Color.red, 2, true);
    private String pBall, gBall, uBall, mBall;
    private int numP;
    public static String[] balls, desc;
    private int which;
    
    //constructor
    public Bag(final Game game, String pBall, String gBall, String uBall,
               String mBall) {
        // Set a null layout
        setLayout(null);
        // Set the panel to be visible.
        this.setVisible(true);
        // Create game
        this.game = game;
        
        //giving numbers for each ball if no number is given
        if (pBall == null) {
            pBall = "0";
        }
        if (gBall == null) {
            gBall = "0";
        }
        if (uBall == null) {
            uBall = "0";
        }
        if (mBall == null) {
            mBall = "0";
        }
        
        // create pokeballs
        this.pBall = pBall;
        this.gBall = gBall;
        this.uBall = uBall;
        this.mBall = mBall;
        
        //save numbers to variables
        this.numP = Integer.parseInt(this.pBall);
        int numG = Integer.parseInt(this.gBall);
        int numU = Integer.parseInt(this.uBall);
        int numM = Integer.parseInt(this.mBall);
        
        //create arrays to output
        balls = new String[4];
        balls[0] = "Pokeball x" + numP;
        balls[1] = "Great Ball x" + numG;
        balls[2] = "Ultra Ball x" + numU;
        balls[3] = "Masterball x" + numM;
        
        //description of each ball
        desc = new String[4];
        desc[0] = "Poor catch rate";
        desc[1] = "Decent catch rate";
        desc[2] = "Good catch rate";
        desc[3] = "100% catch rate";
        
        // Create the font for options.
        Font font = new Font("Monospaced", Font.BOLD, 15);
        
        // Description Field for the ball description
        final JTextField text = new JTextField();
        text.setBounds(40, 450, 220, 80);
        text.setEditable(false);
        text.setBackground(Color.WHITE);
        text.setFont(font);
        add(text);
        
        // create Jlist with the array of balls created earlier
        list = new JList<String>(balls);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setBounds(330, 80, 250, 200);
        list.setFont(new Font("Monospaced", Font.BOLD, 24));
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {//add actionlistener to actively paint pokeball and change descriptions
                text.setText(desc[list.getSelectedIndex()]);//change description
                which = list.getSelectedIndex();
                game.repaint();//update screen
            }
        });
        JScrollPane listScroller = new JScrollPane(list);
        add(list);//add list to panel
        
        // Toss Button to get rid of balls
        JButton toss = new JButton("Toss");
        toss.setContentAreaFilled(false);
        toss.setFocusPainted(false);
        toss.setFont(font);
        toss.addActionListener(this);
        toss.setBounds(100, 380, 100, 50);
        add(toss);
        
        // Close Bag Button
        JButton exit = new JButton("Close Bag");
        exit.setContentAreaFilled(false);
        exit.setBorder(new LineBorder(Color.black, 3, true));
        exit.setFocusPainted(false);
        exit.setFont(font);
        exit.addActionListener(this);
        exit.setBounds(396, 400, 100, 50);
        add(exit);
        
    }
    /*Action performed method
     * @param the action the user inputted
     */
    @Override//action performed method to respond to events
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button.getText().equalsIgnoreCase("Close Bag")) {//if close bag is pressed
                // Exit options.
                game.setContent(game.getMapPanel());//return to map player was on
                game.requestFocus();
                return;
            } else if (button.getText().equalsIgnoreCase("Toss")) {//get rid of one ball each time
                if (list.getSelectedIndex() == 0) {
                    toss();
                }
            }
            
        }
    }

    /*method to return pokeball number
     * @return numP
     */
    public int getNumP() {
        return this.numP;
    }

    /**method to set pokeball number
     * @param num the number to set it to
     **/
    public void setNumP(int num){
        this.numP = num;
    }

    /**method to get rid of one pokeball
     * called by actionperformed method to get rid of a ball
     **/
    public void toss() {
        int num = Integer.parseInt(balls[0].substring(balls[0].indexOf("x") + 1)) - 1;//convert to a number and subtract one
        if (num >= 0) {
            String reduced = balls[0].substring(0, balls[0].indexOf("x") + 1) + Integer.toString(num);//convert to string
            balls[0] = reduced;//update array
            setNumP(num);
            game.repaint();//update screen
        }
    }
    /**paintComponent method
     * to draw pictures to screen
     * @param g graphics component used to draw stuff
     **/
    @Override
    protected void paintComponent(Graphics g) {
        // Paint the picture
        try {
            BufferedImage border = ImageIO.read(new File("border.png"));//load in from the picture files
            BufferedImage bag = ImageIO.read(new File("bag.png"));
            BufferedImage one = ImageIO.read(new File("pokeball.png"));
            BufferedImage two = ImageIO.read(new File("greatball.png"));
            BufferedImage three = ImageIO.read(new File("ultraball.png"));
            BufferedImage four = ImageIO.read(new File("masterball.png"));
            g.drawImage(border, 295, 0, 300, 570, null);//draw at specified locations
            g.drawImage(border, 0, 330, 295, 240, null);
            g.drawImage(bag, -10, 0, 305, 330, null);
            
            switch (which) {//decide which ball to draw and draw it
                case 0:
                    g.drawImage(one, 13, 240, 65, 70, null);
                    break;
                case 1:
                    g.drawImage(two, 13, 240, 65, 70, null);
                    break;
                case 2:
                    g.drawImage(three, 13, 240, 65, 70, null);
                    break;
                case 3:
                    g.drawImage(four, 13, 240, 65, 70, null);
                    break;
            }
            
        } catch (IOException e) {
        }
        ;
    }
}