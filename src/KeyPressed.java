/**
 * KeyPressed.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * KeyPressed class.
 */

//import libraries
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.sampled.*;
import java.io.File;

//key pressed class to respond to actions
public class KeyPressed implements KeyListener {

    // key variables
    private Game game;
    private Player player;
    private DoubleLinkedList<Integer> pressed;
    public static int pressedAmount = 0;
    private Clip healSound = null;

    // constructor
    public KeyPressed(Game game) {
        // create game
        this.game = game;
        // create player
        this.player = game.getPlayer();
        // create list
        pressed = new DoubleLinkedList<>();
    }

    /*
     * getGame method to get game
     * 
     * @return game the frame
     */
    public Game getGame() {
        return game;
    }

    // to satisfy the interface
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /*
     * keyPressed method to respond to keys pressed
     * 
     * @param e the key event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // start game
        if (!game.isRunning()) {
            game.start();
        } else {

            if (game.getPlayer().isInDialog()) {// increase pressed amount
                pressedAmount++;
                return;
            }

            if (game.isDialogRunning()) {// check if they are in conversation to see if they can open options
                return;
            }

            if (game.getMapPanel().getMap() == null) {// check if map is null to see if they can open options
                return;
            }

            // open the options panel
            if (e.getKeyCode() == KeyEvent.VK_O && game.getContentPane().equals(game.getMapPanel())) {
                game.setPaused(!game.isPaused());// pause game
                return;
            }

            if (!game.getContentPane().equals(game.getMapPanel())) {// check to make sure the user sees the panel they
                                                                    // are on
                return;
            }
            // set the panel
            if (game.isPaused()) {
                // Check if they press enter...
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Determine which option they are on.
                    switch (Map.getCurrent()) {
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
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    Map.setCurrent(Map.getCurrent() - 1);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    Map.setCurrent(Map.getCurrent() + 1);
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
                    if (talk) {// if they are looking at each other create dialogbox
                        DialogBox box = new DialogBox(game, npc.getName() + ": " + npc.getDialog(),
                                game.getGlobalTextSpeed()) {
                            /*
                             * onComplete method to do stuff after completion
                             */
                            @Override
                            public void onComplete() {
                                if (npc.getName().equalsIgnoreCase("Owner")) {// if talking to owner set panel to
                                                                              // pokemart
                                    game.setContent(game.getPokemart());
                                }
                                if (npc.getName().equalsIgnoreCase("Nurse")) {// if talking to nurse call heal method
                                    player.healParty();
                                }
                            }

                            /*
                             * onLine method to do stuff in between
                             * 
                             * @param line the line to do stuff on
                             */
                            @Override
                            public void onLine(String line) {
                                if (npc.getName().equalsIgnoreCase("Nurse")) {// if talking to nurse
                                    if (line.equalsIgnoreCase("Please wait while I restore your Pokemon.")) {
                                        Sounds.HEAL.play(false, true);// play music
                                    }
                                }
                            }
                        };
                        box.show(game.getMapPanel());// show dialog
                    }
                }
            }

            // Handle moving the character.
            if (!player.canMove()) {
                return;
            }

            if (!player.isMoving()) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {// move charcter up
                    player.setyVelocity(player.getMoveSpeed() * -1);// move map down
                    if (!pressed.contains(e.getKeyCode())) {
                        player.setDirection("back");// set picture
                        pressed.add(e.getKeyCode());
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {// move map
                    player.setyVelocity(game.getPlayer().getMoveSpeed());
                    if (!pressed.contains(e.getKeyCode())) {
                        player.setDirection("forward");
                        pressed.add(e.getKeyCode());
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {// move map
                    player.setxVelocity(game.getPlayer().getMoveSpeed() * -1);
                    if (!pressed.contains(e.getKeyCode())) {
                        player.setDirection("left");
                        pressed.add(e.getKeyCode());
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {// move map
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

    /*
     * keyReleased method to set speeds
     * 
     * @param e the key event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (game.isRunning()) {
            if (pressed.contains(e.getKeyCode())) {
                player.setxVelocity(0);// set speed to 0
                player.setyVelocity(0);
                pressed.remove(pressed.indexOf(e.getKeyCode()));
            }

            if (pressed.isEmpty()) {
                player.setMoving(false);
            }
        }
    }

}
