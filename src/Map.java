import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Map extends Canvas {

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

    public Map(Game game, int mapWidth, int mapHeight, int picWidth, int picHeight, String name, File image) {
        this.setFocusable(false);
        addKeyListener(game.getKeyListener());
        this.setVisible(true);
        this.game = game;
        this.width = mapWidth;
        this.height = mapHeight;
        this.picHeight = picHeight;
        this.picWidth = picWidth;
        this.name = name;
        this.cam = game.getGameCamera();
        this.warps = new DoubleLinkedList<>();
        this.walls = new DoubleLinkedList<>();
        this.people = new DoubleLinkedList<>();
        this.wildAreas = new DoubleLinkedList<>();
        try {
            this.image = ImageIO.read(image);
            if (selected == null) {
                this.selected = ImageIO.read(new File("selected.png"));
            }
        } catch (IOException e) {
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public Game getGame() {
        return game;
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
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

            // Create the outer parts of the maps.
            g.setColor(Color.black);
            g.fillRect(-1000, -1000, 5000, 5000);

            g.drawImage(this.image, 0 - cam.getxOffset(), 0 - cam.getyOffset(), picWidth, picHeight, null);

            g.setColor(Color.red);

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

                for (int i = 0; i < warps.size(); i++) {
                    Warp warp = warps.get(i);
                    g.drawRect(warp.getBounds(0, 0).x, warp.getBounds(0, 0).y, warp.getBounds(0, 0).width, warp.getBounds(0, 0).height);
                }
            }

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

    public DoubleLinkedList<Entity> getWalls() {
        return walls;
    }

    public DoubleLinkedList<Warp> getWarps() {
        return warps;
    }

    public DoubleLinkedList<NPC> getPeople() {
        return people;
    }

    public DoubleLinkedList<WildArea> getWildAreas() {
        return wildAreas;
    }

    public void addWall(Entity wall) {
        this.walls.add(wall);
    }

    public void addWarp(Warp warp) {
        this.warps.add(warp);
    }

    public void addNPC(NPC npc) {
        this.people.add(npc);
    }

    public void addWildArea(WildArea area){
        this.wildAreas.add(area);
    }

    public static void setCurrent(int index) {
        if (index > 5) {
            index = 1;
        } else if (index < 1) {
            index = 5;
        }
        current = index;
    }

    public static int getCurrent() {
        return current;
    }

}
