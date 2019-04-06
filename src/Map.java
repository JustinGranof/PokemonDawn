/**
 * Map.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * Map class.
 */

//import
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

//map class
public class Map extends Canvas {
    
    //variables
    private int width;
    private int height;
    private int picWidth;
    private int picHeight;
    private String name;
    private Game game;
    private GameCamera cam;
    private BufferedImage image;
    private static BufferedImage selected;
    private static int current = 1;
    
    // List of map items
    private DoubleLinkedList<Warp> warps;
    private DoubleLinkedList<Entity> walls;
    private DoubleLinkedList<NPC> people;
    private DoubleLinkedList<WildArea> wildAreas;
    
    //constructor
    public Map(Game game, int mapWidth, int mapHeight, int picWidth, int picHeight, String name, URL image) {
        //initialize items
        this.setFocusable(false);
        addKeyListener(game.getKeyListener());
        this.setVisible(true);
        this.game = game;
        // Set to a black background
        this.setBackground(Color.black);
        this.width = mapWidth;
        this.height = mapHeight;
        this.picHeight = picHeight;
        this.picWidth = picWidth;
        this.name = name;
        this.cam = game.getGameCamera();
        //create lists
        this.warps = new DoubleLinkedList<>();
        this.walls = new DoubleLinkedList<>();
        this.people = new DoubleLinkedList<>();
        this.wildAreas = new DoubleLinkedList<>();
        try {
            this.image = ImageIO.read(image);
            if (selected == null) {
                this.selected = ImageIO.read(new File("selected.png"));//load picture
            }
        } catch (IOException e) {
        }
    }
  
    
    /*getWidth method
     * @return width, the width of map
     */
    public int getWidth() {
        return width;
    }
    
    /*getHeight method
     * @return height, the height of map
     */
    public int getHeight() {
        return height;
    }
    
    /*getName method
     * @return name, the name of map
     */
    public String getName() {
        return name;
    }
    
    /*getGame method
     * @return game, the frame
     */
    public Game getGame() {
        return game;
    }
    
    /*renderh method
     * to constantly update screen
     */
    public void render() {
        BufferStrategy bs = this.getBufferStrategy();//create buffer strategy
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        
        if (game.isPaused()) {
            // Paint the pause menu.
            g.setColor(Color.black);
            g.fillRect(430, 0, 170, 180);
            g.setColor(Color.white);
            g.fillRect(435, 5, 155, 170);
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g.setColor(Color.black);
            // Draw the options
            g.drawString("POKEMON", 478, 40);
            g.drawString("BAG", 478, 70);
            g.drawString("OPTIONS", 478, 100);
            g.drawString("SAVE", 478, 130);
            g.drawString("EXIT", 478, 160);
            // Draw the arrow.
            g.drawImage(selected, 452, getCurrent() * 30 - 3, 16, 16, null);
        } else {
            
            // Clear screen
            g.clearRect(0, 0, 600, 600);
            
            //draw image
            g.drawImage(this.image, 0 - cam.getxOffset(), 0 - cam.getyOffset(), picWidth, picHeight, null);
            
            //set color to red
            g.setColor(Color.red);
            
            //paint hitboxes
            if (game.getOptions().getHitboxes().equalsIgnoreCase("On")) {
                for (int i = 0; i < getWalls().size(); i++) {
                    Entity wall = getWalls().get(i);
                    g.drawRect(wall.getBounds(0, 0).x, wall.getBounds(0, 0).y, wall.getBounds(0, 0).width, wall.getBounds(0, 0).height);
                }
                
                // Print all the wild areas
                for(int i = 0; i < wildAreas.size(); i++){
                    WildArea wa = wildAreas.get(i);
                    g.drawRect(wa.getBounds(0, 0).x, wa.getBounds(0, 0).y, wa.getBounds(0, 0).width, wa.getBounds(0, 0).height);
                }
                
                //draw warps
                for (int i = 0; i < warps.size(); i++) {
                    Warp warp = warps.get(i);
                    g.drawRect(warp.getBounds(0, 0).x, warp.getBounds(0, 0).y, warp.getBounds(0, 0).width, warp.getBounds(0, 0).height);
                }
            }
            
            //draw NPCs
            for (int i = 0; i < people.size(); i++) {
                NPC person = people.get(i);
                g.drawImage(person.getImg(), person.getX() - cam.getxOffset(), person.getY() - cam.getyOffset(), person.getWidth(), person.getHeight(), null);
            }
            
            // Draw Player
            Player p = getGame().getPlayer();
            if (p.isVisible()) {
                g.drawImage(p.getSprite(), p.getX() - cam.getxOffset(), p.getY() - cam.getyOffset(), p.getWidth(), p.getHeight(), null);
            }
        }
        
        // ------------------
        bs.show();
        g.dispose();
    }
    
    /*getWalls method
     * @return walls, the wall to block access
     */
    public DoubleLinkedList<Entity> getWalls() {
        return walls;
    }
    
    /*getWarps method
     * @return warps, the warp to teleport player
     */
    public DoubleLinkedList<Warp> getWarps() {
        return warps;
    }
    
    /*getPeople method
     * @return people, the npcs on map
     */
    public DoubleLinkedList<NPC> getPeople() {
        return people;
    }
    
    /*getWildAreas method
     * @return wildAreas, the areas where pokemon appear
     */
    public DoubleLinkedList<WildArea> getWildAreas() {
        return wildAreas;
    }
    
    /*addWall method
     * @param wall, the wall to add
     */
    public void addWall(Entity wall) {
        this.walls.add(wall);
    }
    
    /*addWarp method
     * @param warp, the warp to add
     */
    public void addWarp(Warp warp) {
        this.warps.add(warp);
    }
    
    /*addNPC method
     * @param npc, the npc to add
     */
    public void addNPC(NPC npc) {
        this.people.add(npc);
    }
    
    /*addWildArea method
     * @param wildArea, the area to add
     */
    public void addWildArea(WildArea area){
        this.wildAreas.add(area);
    }
    
    /*setCurrent method
     * @param index, the index to move based of
     */
    public static void setCurrent(int index) {
        if (index > 5) {
            index = 1;
        } else if (index < 1) {
            index = 5;
        }
        current = index;
    }
    
    /*getCurrent method
     * @return current, the current placement of the arrow
     */
    public static int getCurrent() {
        return current;
    }
    
}
