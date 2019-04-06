import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class NPC extends Entity {

    private String dialog;
    private String name;
    private BufferedImage img;

    public NPC(Game game, int x, int y, int width, int height, String dialog, String name, File image) {
        super(game, x, y, width, height);
        this.dialog = dialog;
        this.name = name;
        try{
            this.img = ImageIO.read(image);
        }catch(IOException e){}
    }

    public BufferedImage getImg() {
        return img;
    }

    public String getDialog() {
        return dialog;
    }

    public String getName(){
        return this.name;
    }

    public void tick(){
        // Update path
    }

    @Override
    public Rectangle getBounds(int xVel, int yVel){
        Rectangle original = super.getBounds(xVel, yVel);
        original.setBounds(original.x, original.y + 5, original.width, original.height - 15);
        return original;
    }

}
