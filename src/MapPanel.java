import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapPanel extends JPanel {

    private Map map;

    public MapPanel(Map map){
        this.setVisible(true);
        this.setFocusable(false);
        this.setOpaque(false);
        setLayout(new BorderLayout());
        this.map = map;
    }

    public void setMap(Map m){
        addKeyListener(m.getGame().getKeyListener());
        for(Component c : getComponents()){
            remove(c);
        }
        this.map = m;
        if(m != null) {
            this.add(m);
        }
        revalidate();
    }

    public Map getMap(){
        return this.map;
    }

}
