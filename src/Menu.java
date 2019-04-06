import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Menu extends JPanel {

    private JLabel begin;
    private Timer timer;

    public Menu(){
        setVisible(true);
        setLayout(null);
        
        begin = new JLabel("PRESS ANY KEY TO BEGIN");
        begin.setBounds(150, 270, 500, 200);
        begin.setFont(new Font("Monospaced", Font.BOLD, 23));
        begin.setForeground(Color.WHITE);

        add(begin);
        timer = new Timer(800, new ActionListener() {
            @Override
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

    @Override
    protected void paintComponent(Graphics g) {
        try {
            BufferedImage img = ImageIO.read(new File("logo.png"));
            g.drawImage(img, 0, 0, 600, 600, null);
        }catch(IOException e){}
    }

}
