/**
 * NPC.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * NPC object class.
 */

//imports
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

//NPC class
public class NPC extends Entity {
    
    //variables
    private String dialog;
    private String name;
    private BufferedImage img;
    
    //constructor
    public NPC(Game game, int x, int y, int width, int height, String dialog, String name, URL image) {
        super(game, x, y, width, height);
        this.dialog = dialog;
        this.name = name;
        try{
            this.img = ImageIO.read(image);//load image
        }catch(IOException e){}
    }
    
    /*getImg method
     * @return img, the picture
     */
    public BufferedImage getImg() {
        return img;
    }
    
    /*getDialog method
     * @return dialog, the messgae
     */
    public String getDialog() {
        return dialog;
    }
    
    /*getName method
     * @return name, the npc name
     */
    public String getName(){
        return this.name;
    }
    
    //does nothing for now
    public void tick(){
        // Update path
    }
    
    /*getBounds method
     * @return original, the size of rectsngle
     */
    @Override
    public Rectangle getBounds(int xVel, int yVel){
        Rectangle original = super.getBounds(xVel, yVel);
        original.setBounds(original.x, original.y + 5, original.width, original.height - 15);
        return original;
    }
    
}
