/**
 * Menu.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * Menu class.
 */

//imports
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//menu class
public class Menu extends JPanel {
    
    //variables
    private JLabel begin;
    private Timer timer;
    
    //constructor
    public Menu(){
        setVisible(true);
        setLayout(null);
        
        //label for start screen
        begin = new JLabel("PRESS ANY KEY TO BEGIN");
        begin.setBounds(150, 270, 500, 200);
        begin.setFont(new Font("Monospaced", Font.BOLD, 23));
        begin.setForeground(Color.WHITE);
        
        add(begin);
        //make label flash
        timer = new Timer(800, new ActionListener() {
            @Override
            /*actionPerformed method
             * to make label flash
             * @param e, the action event
             */
                public void actionPerformed(ActionEvent e) {
                if(begin.isVisible()){
                    begin.setVisible(false);
                }else{
                    begin.setVisible(true);
                }
            }
        });
        timer.start();
    }
    
    /*paintComponent method
     * to draw stuff
     * @param g, the graphics component
     */
    @Override
    protected void paintComponent(Graphics g) {
        try {
            BufferedImage img = ImageIO.read(new File("logo.png"));//load image
            g.drawImage(img, 0, 0, 600, 600, null);//paint image
        }catch(IOException e){}
    }
    
}
