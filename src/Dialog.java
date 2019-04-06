import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.sound.sampled.*;


public class Dialog extends JPanel {
    
    private String file, all = "";
    private Game game;
    private boolean pokemon = false;
    private Dialog instance;
    public static String name;
    private Clip introMusic = null, gameMusic = null;
    
    public Dialog(String file, final Game game){
        this.file = file;
        this.game = game;
        instance = this;

        setLayout(new BorderLayout());
        setVisible(true);
        
        Sounds.INTRO.play(true, false);
        
        try{
            Scanner scanner = new Scanner(new File(file));
            
            while(scanner.hasNextLine()){
                all += scanner.nextLine() + "#";
            }
            
        }catch(Exception e){
        }
        
        DialogBox intro = new DialogBox(game, all, 100){

            @Override
            public void onLine(String line){
                if(line.equalsIgnoreCase("But everyone calls me the Professor.")){
                    // Spawn the pokemon.
                    pokemon = true;
                    instance.repaint();
                }
                if(line.equalsIgnoreCase("What's your name by the way?")){
                    // get their name.
                    name = JOptionPane.showInputDialog("Enter your name");
                }
            }

            @Override
            public void onComplete(){
                Sounds.GAME_MUSIC.play(true, false);
                // Bring player to the first map
                game.setContent(game.getMapPanel());
                // TODO Change the name here for the starting town.
                game.getMapPanel().setMap(game.getMap("oakville"));
                
            }
        };
        intro.show(this);
    }
     public static String getPersonName(){
      return name;
    }
     
    @Override
    protected void paintComponent(Graphics g) {
        try {
            BufferedImage img = ImageIO.read(new File("Intro.png"));
            g.drawImage(img, 0, 0, 800, 600, null);

            if(pokemon){
                BufferedImage pke = ImageIO.read(new File("mew.png"));
                g.drawImage(pke, 150, 100, 160, 160, null);
            }
        }catch(IOException e){}
    }
}