public class GameCamera {

    private int xOffset;
    private int yOffset;

    public GameCamera(int xOffset, int yOffset){
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void move(int xAmt, int yAmt){
        this.xOffset += xAmt;
        this.yOffset += yAmt;
    }
    
    public void centerOnEntity(Entity p){
    	this.xOffset = (p.getX() - 600/2) + p.getWidth()/2;
    	this.yOffset = (p.getY() - 600/2) + p.getHeight()/2 + 20;
    }

    public int getxOffset() {
        return this.xOffset;
    }

    public int getyOffset(){
        return this.yOffset;
    }
}
