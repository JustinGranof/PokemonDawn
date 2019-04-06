/**
 * Dialog.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * Dialog class.
 */

//import required libraries
import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.sound.sampled.*;

//dialog panel used when the game is run for the first time
public class Dialog extends JPanel {
    
    //key variables
    private String file, all = "";
    private Game game;
    private boolean pokemon = false;
    private Dialog instance;
    public static String name;
    private Clip introMusic = null, gameMusic = null;
    
    //constructor
    public Dialog(String file, final Game game){
        //initialize
        this.file = file;
        //create game
        this.game = game;
        instance = this;
        //set layout
        setLayout(new BorderLayout());
        //set visibility
        setVisible(true);
        
        //play music
        Sounds.INTRO.play(true, false);
        
        //read in text from file and store in a variable
        try{
            Scanner scanner = new Scanner(new File(file));
            
            while(scanner.hasNextLine()){
                all += scanner.nextLine() + "#";
            }
            
        }catch(Exception e){
        }
        
        //create a dialog box to display the information needed
        DialogBox intro = new DialogBox(game, all, 6){
            
            /*OnLine method to perform actions in between
             * @param line the line to execute the action
             */
            @Override
            public void onLine(String line){
                if(line.equalsIgnoreCase("But everyone calls me the Professor.")){
                    // Spawn the pokemon.
                    pokemon = true;//draw a pokemon to the screen
                    instance.repaint();
                }
                if(line.equalsIgnoreCase("What's your name by the way?")){
                    // get their name.
                    name = JOptionPane.showInputDialog("Enter your name");//get the users name
                }
            }
            /*onComplete method to execute actions after the information is displayed
             */
            @Override
            public void onComplete(){
                //play  music
                Sounds.GAME_MUSIC.play(true, false);
                // Bring player to the first map
                game.setContent(game.getMapPanel());
                // TODO Change the name here for the starting town.
                game.getMapPanel().setMap(game.getMap("oakville"));
                
            }
        };
        //show the information by calling the show method
        intro.show(this);
    }
    /*getPersonName method
     * gets the name of the person
     */
    public static String getPersonName(){
        return name;
    }
    /*paintComponent method to draw stuff
     * @param g the graphics component
     */
    @Override
    protected void paintComponent(Graphics g) {
        try {
            BufferedImage img = ImageIO.read(new File("Intro.png"));//to access the picture to draw
            g.drawImage(img, 0, 0, 800, 600, null);//draw at location
            
            if(pokemon){
                BufferedImage pke = ImageIO.read(new File("mew.png"));//draw pokemon when allowed to
                g.drawImage(pke, 150, 100, 160, 160, null);
            }
        }catch(IOException e){}
    }
}