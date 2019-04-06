/**
 * MapPanel.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * MapPanel class.
 */

//imports
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//MapPanel class
public class MapPanel extends JPanel {
    
    //variables
    private Map map;
    
    //constructor
    public MapPanel(Map map){
        this.setVisible(true);
        this.setFocusable(false);
        this.setOpaque(false);
        setLayout(new BorderLayout());
        this.map = map;
    }
    
    /* setMap method
     * to set map
     * @param m the map to set to
     */
    public void setMap(Map m){
        addKeyListener(m.getGame().getKeyListener());
        for(Component c : getComponents()){
            remove(c);
        }
        this.map = m;//set map
        if(m != null) {
            this.add(m);
        }
        revalidate();
    }
    
    /* getMap method
     * to get map
     * @return map, the map wanted
     */
    public Map getMap(){
        return this.map;
    }
    
}
