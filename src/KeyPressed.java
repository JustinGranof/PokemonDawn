import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.sampled.*;
import java.io.File;
    
public class KeyPressed implements KeyListener {

    private Game game;
    private Player player;
    private DoubleLinkedList<Integer> pressed;
    public static int pressedAmount = 0;
    private Clip healSound = null;

    public KeyPressed(Game game) {
        this.game = game;
        this.player = game.getPlayer();
        pressed = new DoubleLinkedList<>();
    }

    public Game getGame() {
        return game;
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e){
        if (!game.isRunning()) {
            game.start();
        } else {

            if (game.getPlayer().isInDialog()) {
                pressedAmount++;
                return;
            }

            if(game.isDialogRunning()){
                return;
            }
            
            if(game.getMapPanel().getMap() == null){
             return;
            }

            // TODO check if they are allowed to open options.
            if(e.getKeyCode() == KeyEvent.VK_O && game.getContentPane().equals(game.getMapPanel())){
                game.setPaused(!game.isPaused());
                return;
            }

            if(!game.getContentPane().equals(game.getMapPanel())){
                return;
            }

            if(game.isPaused()){
                // Check if they press enter...
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    // Determine which option they are on.
                    switch(Map.getCurrent()){
                        case 1:
                            // Pokemon
                            game.setContent(game.getParty());
                            break;
                        case 2:
                            // Bag
                            game.setContent(game.getBag());
                            break;
                        case 3:
                            // Options
                            game.setContent(game.getOptions());
                            break;
                        case 4:
                            // Save
                            game.saveGame(true);
                            break;
                        case 5:
                            // Exit
                            game.setPaused(false);
                            break;
                    }
                }
                // Check if they navigate the keys.
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    Map.setCurrent(Map.getCurrent()-1);
                }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    Map.setCurrent(Map.getCurrent()+1);
                }
                return;
            }

            // Handle selecting or talking to NPCs.
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                for (int i = 0; i < game.getMapPanel().getMap().getPeople().size(); i++) {
                    final NPC npc = game.getMapPanel().getMap().getPeople().get(i);
                    // Check if player is looking at this npc.
                    boolean talk = false;
                    if (player.getBounds(5, 0).intersects(npc.getBounds(0, 0))
                            && player.getDirection().equalsIgnoreCase("right")) {
                        talk = true;
                    } else if (player.getBounds(-5, 0).intersects(npc.getBounds(0, 0))
                            && player.getDirection().equalsIgnoreCase("left")) {
                        talk = true;
                    } else if (player.getBounds(0, 5).intersects(npc.getBounds(0, 0))
                            && player.getDirection().equalsIgnoreCase("forward")) {
                        talk = true;
                    } else if (player.getBounds(0, -5).intersects(npc.getBounds(0, 0))
                            && player.getDirection().equalsIgnoreCase("back")) {
                        talk = true;
                    }
                    if(talk){
                        DialogBox box = new DialogBox(game, npc.getName() + ": " + npc.getDialog(), game.getGlobalTextSpeed()){
                            @Override
                            public void onComplete(){
                                if(npc.getName().equalsIgnoreCase("Owner")){
                                    // OPEN SHOP HERE.
                                 game.setContent(game.getPokemart());
                                }
                                if(npc.getName().equalsIgnoreCase("Nurse")){
                                    player.healParty();
                                }
                            }
                            @Override
                            public void onLine(String line){
                                if(npc.getName().equalsIgnoreCase("Nurse")){
                                  if(line.equalsIgnoreCase("Please wait while I restore you Pokemon.")){
                                     Sounds.HEAL.play(false, true);
                                  }
                                }
                            }
                        };
                        box.show(game.getMapPanel());
                    }
                }
            }

            // Handle moving the character.
            if (!player.canMove()) {
                return;
            }

            if (!player.isMoving()) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    player.setyVelocity(player.getMoveSpeed() * -1);
                    if (!pressed.contains(e.getKeyCode())) {
                        player.setDirection("back");
                        pressed.add(e.getKeyCode());
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    player.setyVelocity(game.getPlayer().getMoveSpeed());
                    if (!pressed.contains(e.getKeyCode())) {
                        player.setDirection("forward");
                        pressed.add(e.getKeyCode());
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player.setxVelocity(game.getPlayer().getMoveSpeed() * -1);
                    if (!pressed.contains(e.getKeyCode())) {
                        player.setDirection("left");
                        pressed.add(e.getKeyCode());
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player.setxVelocity(game.getPlayer().getMoveSpeed());
                    if (!pressed.contains(e.getKeyCode())) {
                        player.setDirection("right");
                        pressed.add(e.getKeyCode());
                    }
                }

                if (!pressed.isEmpty()) {
                    player.setMoving(true);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (game.isRunning()) {
            if (pressed.contains(e.getKeyCode())) {
                player.setxVelocity(0);
                player.setyVelocity(0);
                pressed.remove(pressed.indexOf(e.getKeyCode()));
            }

            if (pressed.isEmpty()) {
                player.setMoving(false);
            }
        }
    }

}
