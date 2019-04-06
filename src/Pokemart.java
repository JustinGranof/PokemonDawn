/**
 * Pokemart.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * Pokemart object class.
 */

//imports
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.*;
import java.io.IOException;
import javax.swing.border.LineBorder;

//pokemart class used when player enters pokemart
public class Pokemart extends JPanel implements ActionListener, KeyListener{
    
    //variables
    private Game game;
    private Player player;
    private int money, number;
    private static LineBorder selected = new LineBorder(Color.black, 3, true);
    private boolean allow = false, allow2 = false, allow3 = false, allow4 = false;
    private JButton exit, buy, yes, no;
    private JTextField num;
    private JLabel text, times, inBag, amount;
    private  Font font = new Font("Monospaced", Font.BOLD, 20);
    
    //constructor
    public Pokemart(final Game game, int money, Player player){
        // Set a null layout
        setLayout(null);
        // Set the panel to be visible.
        this.setVisible(true);
        // Create game
        this.game = game;
        this.money = money;      
        this.player = player;
        
        //button to buy balls
        buy = new JButton("Buy");
        buy.setContentAreaFilled(false);
        buy.setFocusPainted(false);
        //buy.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        buy.setFont(font);
        buy.addActionListener(this);
        buy.setBounds(30, 40, 180, 55);
        add(buy);
        
        //Close Bag Button
        exit = new JButton("See ya!");
        exit.setContentAreaFilled(false);
        exit.setFocusPainted(false);
        //exit.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        exit.setFont(font);
        exit.addActionListener(this);
        exit.setBounds(30, 95, 180, 55);
        add(exit);
    }
    /*actionPerformed to respond to events
     * @param e, the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() instanceof JButton){
            JButton button = (JButton) e.getSource();
            
            if(button.getText().equalsIgnoreCase("See ya!")){//if button see ya
                // Exit options.
                game.setContent(game.getMapPanel());//return to map
                game.requestFocus();
                return;
            }
            else if(button.getText().equalsIgnoreCase("Buy")){//if buy
                allow = true;//draw stuff
                this.remove(exit);
                this.remove(buy);
                game.repaint();
            }
            else if(button.getText().equalsIgnoreCase("")){//if pokeball selected
                allow2 = true;
                
                //JLabel
                text = new JLabel("How many would you like?");//buy information
                text.setBounds(40, 385, 500, 100);
                text.setFont(new Font("Monospaced", Font.BOLD, 20));
                add(text);
                
                //JLabel
                inBag = new JLabel("IN BAG: " + Bag.balls[0].substring(Bag.balls[0].indexOf("x") + 1));
                inBag.setBounds(40, 302, 180, 60);
                inBag.setFont(new Font("Monospaced", Font.BOLD, 20));
                add(inBag);
                
                //JLabel
                times = new JLabel("X");
                times.setBounds(350, 300, 20, 20);
                times.setFont(new Font("Monospaced", Font.BOLD, 20));
                add(times);
                
                //JTextField
                num = new JTextField("0");
                num.setBounds(370, 295, 50, 30);
                num.setFont(new Font("Monospaced", Font.BOLD, 20));
                num.addKeyListener(this);
                add(num);
                
                game.repaint();//update
            }
            else if(button.getText().equalsIgnoreCase("YES")){//yes button
                money = money - (number*500);//reduce money, add balls, set screen
                player.setMoney(money);
                int old = Integer.parseInt(Bag.balls[0].substring(Bag.balls[0].indexOf("x") + 1)) + number;
                String updated =Bag.balls[0].substring(0,Bag.balls[0].indexOf("x") + 1)  + Integer.toString(old);
                Bag.balls[0] = updated;
                game.getBag().setNumP(old);
                text.setText("Here you are! Thank you, please come again!");
                game.repaint();
                
                allow4 = true;//repaint stuff
                yes.setVisible(false);
                no.setVisible(false);
                
                game.setContent(game.getMapPanel());//return to map
                game.requestFocus();
            }
            else if(button.getText().equalsIgnoreCase("NO")){//if no
                allow4 = true;//repaint screen
                yes.setVisible(false);
                no.setVisible(false);
                
                game.setContent(game.getMapPanel());//return to map
                game.requestFocus();
            }
        }
    }
    
    @Override
    /** Handle the key typed event from the text field. */
        public void keyTyped(KeyEvent e) {
        
    }
    /*keyPressed method, respond to key input
     * @param e, key event
     */
    @Override
    /** Handle the key-pressed event from the text field. */
        public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){//if enter key
            number = Integer.parseInt(num.getText());//calculate money
            if(number * 500 <= money){
                
                //JLabel
                amount = new JLabel("$" + number * 500);
                amount.setBounds(480, 295, 100, 30);
                amount.setFont(new Font("Monospaced", Font.BOLD, 20));
                add(amount);
                
                game.repaint();//update
                
                allow3 = true;
                text.setText("This will cost you $" + number*500 + ". Okay?");//text to show
                amount.setText("");
                times.setText("");
                inBag.setText("");
                num.setVisible(false);
                game.repaint();
                
                //JButton
                yes = new JButton("YES");
                yes.setContentAreaFilled(false);
                yes.setFocusPainted(false);
                yes.setBorder(javax.swing.BorderFactory.createEmptyBorder());
                yes.setFont(font);
                yes.addActionListener(this);
                yes.setBounds(330, 275, 180, 30);
                add(yes);
                
                //JButton
                no = new JButton("NO");
                no.setContentAreaFilled(false);
                no.setFocusPainted(false);
                no.setBorder(javax.swing.BorderFactory.createEmptyBorder());
                no.setFont(font);
                no.addActionListener(this);
                no.setBounds(330, 310, 180, 30);
                add(no);
            }
        }
    }
    
    @Override
    /** Handle the key-released event from the text field. */
        public void keyReleased(KeyEvent e) {
        
    }
    /*paintComponent method
     * to paint stuff in different layers
     * @param g, the paint component
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Paint the picture
        try{
            //load images
            BufferedImage border = ImageIO.read(new File("border.png"));
            BufferedImage pic = ImageIO.read(new File("maps" + File.separator + "pokemart.png"));
            BufferedImage guy = ImageIO.read(new File("NPCs" + File.separator + "owner.png"));
            BufferedImage player = ImageIO.read(new File("player" + File.separator + "left.png"));
            BufferedImage mart = ImageIO.read(new File("mart.png"));
            BufferedImage ball = ImageIO.read(new File("pokeball.png"));
            //draw rectangles
            g.fillRect(0, 0, 800, 800);
            g.setColor(Color.BLACK);
            //draw images
            g.drawImage(pic, 0, 65, 683, 450, null);
            g.drawImage(guy, 140, 228, 30, 40, null);
            g.drawImage(player, 205, 232, 30, 40, null);
            g.drawImage(border, 20, 20, 200, 150, null);
            
            //set font
            g.setFont(new Font("Monospaced", Font.BOLD, 25));
            g.setColor(Color.black);
            
            //first layer
            if(allow){
                g.fillRect(0, 0, 600, 600);
                g.setColor(Color.black);
                g.drawImage(mart, 0, 55, 600, 450, null);
                g.drawString("Money", 30, 95);
                g.drawString("$" + money, 95, 135);
                g.drawString("Pokeball", 240, 105);
                g.drawString("$500", 480, 105);
                g.drawImage(ball, 20, 410, 60, 60, null); 
                
                //buttons
                JButton pb = new JButton("");
                pb.setContentAreaFilled(false);
                pb.setBorder(javax.swing.BorderFactory.createEmptyBorder());
                pb.setFocusPainted(false);
                pb.addActionListener(this);
                pb.setBounds(230, 80, 340, 35);
                add(pb);
            }
            //2nd layer
            if(allow2){
                g.drawImage(border, 0, 370, 600, 135, null);
                g.drawImage(border, 0, 295, 200, 75, null);
                g.drawImage(border, 320, 250, 275, 120, null);
            }
            //3rd layer
            if(allow3){
                g.drawImage(mart, 0, 55, 600, 450, null);
                g.drawString("Money", 30, 95);
                g.drawString("$" + money, 95, 135);
                g.drawString("Pokeball", 240, 105);
                g.drawString("$500", 480, 105);
                g.drawImage(border, 0, 370, 600, 135, null);
                g.drawImage(border, 320, 250, 200, 120, null);  
            }
            //4th layer
            if(allow4){
                g.drawImage(mart, 0, 55, 600, 450, null);
                g.drawString("Money", 30, 95);
                g.drawString("$" + money, 95, 135);
                g.drawString("Pokeball", 240, 105);
                g.drawString("$500", 480, 105);
                g.drawImage(border, 0, 370, 600, 135, null);
            }
        }catch(IOException e){
        };     
    }   
    
    
}