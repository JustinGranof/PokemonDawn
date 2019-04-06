import java.awt.*;

public class Entity {

    private int x;
    private int y;
	private int width;
	private int height;
	private Game game;

    public Entity(Game game, int x, int y, int width, int height){
        this.x = x;
        this.width = width;
        this.game = game;
        this.height = height;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void setX(int x){
    	this.x = x;
    }
    
    public void setY(int y){
    	this.y = y;
    }
    
    public int getWidth(){
    	return this.width;
    }
    
    public int getHeight(){
    	return this.height;
    }

    public Rectangle getBounds(int xVel, int yVel) {
        Rectangle bounds = new Rectangle();
        bounds.setBounds((x + xVel) - game.getGameCamera().getxOffset(), (y + yVel) - game.getGameCamera().getyOffset(), width, height);
        return bounds;
    }


}
