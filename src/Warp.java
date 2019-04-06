import java.awt.*;

public class Warp extends Entity {

    private Map map;
    private int newX;
    private int newY;
    private String direction;

    public Warp(Game game, int x, int y, int width, int height, Map map, int newX, int newY, String direction){
        super(game, x, y, width, height);
        this.map = map;
        this.direction = direction;
        this.newX = newX;
        this.newY = newY;
    }

    public void warp(Player p){
        p.setX(newX);
        p.setY(newY);
        p.getGame().getMapPanel().setMap(map);
    }

    public String getDirection() {
        return direction;
    }

}
