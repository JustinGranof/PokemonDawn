/**
 * GameCamera.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * GameCamera class.
 */

//to create the game camera
public class GameCamera {
    
    //key variables
    private int xOffset;
    private int yOffset;
    
    //constructor to set where to look at
    public GameCamera(int xOffset, int yOffset){
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    
    /*move method to display a part of the map
     * @param xAmt the x amount to add
     * @param yAmt the y amount to add 
     */
    public void move(int xAmt, int yAmt){
        this.xOffset += xAmt;
        this.yOffset += yAmt;
    }
    
    /*centerOnEntity method to focus on player
     * @param p the entity to focus on
     */
    public void centerOnEntity(Entity p){
        this.xOffset = (p.getX() - 600/2) + p.getWidth()/2;
        this.yOffset = (p.getY() - 600/2) + p.getHeight()/2 + 20;
    }
    
    /*getxOffset method to access variable
     * @return xOffset the x amount skipped
     */
    public int getxOffset() {
        return this.xOffset;
    }
    
    /*getyOffset method to access variable
     * @return yOffset the y amount skipped
     */
    public int getyOffset(){
        return this.yOffset;
    }
}
