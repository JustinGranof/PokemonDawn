import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.sound.sampled.*;


public class Game extends JFrame {

    // Variable to tell if the game is currently running or not.
    private boolean running;
    // Variable to store all the maps in the game.
    private DoubleLinkedList<Map> maps;
    // The panel that will be used to display the current map.
    private MapPanel mp;
    // The game camera that will follow the player.
    private GameCamera gc;
    // The variable that will store the player and all it's info.
    private Player player;
    // A variable to store if the game is paused or not.
    private boolean paused;
    // A variable to store the options panel.
    private Options options;
    // A variable to store the party panel
    private Party party;
    // A variable to store the bag panel
    private Bag bag;
    // A variable to store the pokemart panel
    private Pokemart pokemart;
    // A variable to hold the key listener for adding it to other components &
    // panels.
    private KeyPressed kl;
    // A variable to store whether or not dialog is currently running.
    private boolean dialogRunning;
    // A variable to store clip
    public static Clip gameMusic = null;

    /**
     * Method to set the content that the player is viewing on the JFrame.
     * @param panel the panel that will be set for users to see.
     */
    public void setContent(JPanel panel) {
        // Set the content pane of the JFrame.
        this.setContentPane(panel);
        // Refresh the JFrame
        this.revalidate();
    }

    /**
     * Constructor for the game. Initializes variables & more.
     */
    public Game() {
        // Set the current panel to the main menu.
        setContent(new Menu());
        // Set the game to have not started, player is at main menu.
        this.running = false;
        // Create a panel to store the map.
        this.mp = new MapPanel(null);
        // Set dialog to not be running yet
        this.dialogRunning = false;
        // Create player
        this.player = new Player(this, 400, 400, "forward", 28, 40);
        // Load the players party
        this.player.loadParty();
        // Load all the players money if there is.
        String money = PokemonDawn.getValue("Money");
        // If there is money saved in the file...
        if (money != null) {
            // Get that amount of money
            int amt = Integer.parseInt(money);
            // Set the players balance to be that saved amount from the file.
            player.setMoney(amt);
        }
        // Create a new game camera.
        this.gc = new GameCamera(0, 0);
        // Create the linked list
        this.maps = new DoubleLinkedList<>();
        // Center the camera onto the player.
        gc.centerOnEntity(this.player);
        // Create an options JPanel
        String textSpeed = PokemonDawn.getValue("TextSpeed");
        if (textSpeed == null) {
            // There are no options, so use the default ones.
            this.options = new Options(this, "Medium", "No", "Off");
        } else {
            // Get the options from the data file.
            String autoSave = PokemonDawn.getValue("AutoSave");
            String hitboxes = PokemonDawn.getValue("Hitboxes");
            // Load the options with all the saved options.
            this.options = new Options(this, textSpeed, autoSave, hitboxes);
        }
        // Load all the pokeball numbers from the data file.
        String pBall = PokemonDawn.getValue("Pokeball");
        String gBall = PokemonDawn.getValue("Great Ball");
        String uBall = PokemonDawn.getValue("Ultra Ball");
        String mBall = PokemonDawn.getValue("Masterball");
        // Create the bag panel
        this.bag = new Bag(this, pBall, gBall, uBall, mBall);
        // Create a party jpanel instance.
        this.party = new Party(this, false);
        // load the maps
        loadMaps();
        // Set the game to not be paused.
        this.paused = false;
        // Set the title of the game.
        setTitle("Pokémon Dawn");
        // Set the panel to be visible
        setVisible(true);
        // Set the size of the frame.
        setSize(600, 600);
        // Disable resizing of the frame
        setResizable(false);
        // Make the program shutdown on closing.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // Add a listener to handle auto saving the game.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Check if user has auto saving enabled.
                if (getOptions().getAutoSave().equalsIgnoreCase("Yes")) {
                    // Auto save the game.
                    saveGame(false);
                }
                System.exit(0);
            }
        });
        // Make the frame open in the center of the screen.
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
                / 2 - this.getSize().height / 2);
        // Save the instance of the key listener that will be used in the game.
        this.kl = new KeyPressed(this);
        // Add the key listener for movement and dialog to the frame.
        addKeyListener(kl);
        Sounds.START.play(true, false);
    }

    /**
     * Method to be run from the game loop. (Rendering, player movement, etc.)
     */
    public void tick() {
        // If the map is not null, and has been set..
        if (mp.getMap() != null) {
            // Check to see if the player is in dialog.
            if (!player.isInDialog()) {
                // Render the maps picture
                mp.getMap().render();
                // if the game is not paused...
                if (!isPaused()) {
                    // Loop through all the npcs on the map
                    for (int i = 0; i < mp.getMap().getPeople().size(); i++) {
                        // Get the npc at the certain index.
                        NPC npc = mp.getMap().getPeople().get(i);
                        // Tick for the NPC.
                        npc.tick();
                    }
                }
            }
        }
        // If the game is not paused
        if (!isPaused()) {
            // Center the game camera on the player.
            getGameCamera().centerOnEntity(player);
            // Tick for the player (collision, NPC interaction)
            player.tick();
        }
    }

    /**
     * Method to determine if the game has started yet.
     * @return true if the game is started, false if not.
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Method to load the players last location.
     */
    public void loadPlayer() {
        // Get the location from the data file for the player.
        String playerLocation = PokemonDawn.getValue("Location");
        // If the player has no saved location, start the intro.
        if (playerLocation == null) {
            // Start a new game
            setContent(new Dialog("IntroText.txt", this));
        } else {
            // Get the map from the data file that the player was last on.
            String map = playerLocation.substring(0,
                    playerLocation.indexOf("~"));
            // Separate the string to just have the x and y coordinates of the player.
            playerLocation = playerLocation.substring(playerLocation
                    .indexOf("~") + 1);
            // Get the x coordinate that the player had saved.
            int x = Integer.parseInt(playerLocation.substring(0,
                    playerLocation.indexOf("~")));
            // Separate to be only the y coordinate.
            playerLocation = playerLocation.substring(playerLocation
                    .indexOf("~") + 1);
            // Get the y coordinate that the player had saved.
            int y = Integer.parseInt(playerLocation.substring(0));
            // Set the x and y of the player to be the saved coordinates.
            player.setX(x);
            player.setY(y);
            // Get the map using the map name from the data file.
            Map m = getMap(map);
            // If the map is found...
            if (m != null) {
                // Set the content pane to be the map panel, displaying the saved map.
                setContent(getMapPanel());
                // Set the map of the map panel to be the one found from the data file.
                getMapPanel().setMap(getMap(map));
            }
        }
    }

    /**
     * Method to start the game.
     */
    public void start() {
        // Play the game music
        Sounds.GAME_MUSIC.play(true, false);
        // Set hthe game to be running.
        this.running = true;
        // Load the players location.
        loadPlayer();
        // Create the game loop.
        Thread loop = new Thread() {
            @Override
            public void run() {
                // Create a variable to hold preferred/max fps.
                int fps = 120;
                // Calculate the amount of time it takes per tick.
                double timePerTick = 1000000000 / fps;
                // Have a variable to store delta
                double delta = 0;
                // Variable to store the time later.
                long now;
                // Save the current time
                long lastTime = System.nanoTime();
                // Variable to count ticks.
                long timer = 0;
                // Variable to hold number of ticks.
                int ticks = 0;
                // Loop while the game is running.
                while (isRunning()) {
                    // Set now to be the current time.
                    now = System.nanoTime();
                    // Calculate delta
                    delta += (now - lastTime) / timePerTick;
                    // Calculate the timer
                    timer += now - lastTime;
                    // The last time is set to the current time
                    lastTime = now;
                    // Check if delta is greater than or equal to one.
                    if (delta >= 1) {
                        // Call the tick method
                        tick();
                        // Increase number of ticks
                        ticks++;
                        // Decrease delta
                        delta--;
                    }
                    // Check if the timer needs to be reset.
                    if (timer >= 1000000000) {
                        // OPTIONAL: Print number of frames that the game is running on.
                        // System.out.println("Frames: " + ticks);
                        // Reset the variables timer & ticks.
                        ticks = 0;
                        timer = 0;
                    }
                }
                // The game is no longer running, stop the thread.
                this.stop();
            }
        };
        // Start the thread/game loop.
        loop.start();
    }


    /**
     * Method to create all the maps in the entire game.
     */
    public void loadMaps() {
        // Create the spawn map.
        Map spawn = new Map(this, 1200, 1200, 1200, 1200, "Oakville", new File(
                "maps" + File.separator + "oakville.png"));

        // Create the pokecenter map.
        Map pokecenter = new Map(this, 1067, 600, 513, 280, "Pokecenter",
                new File("maps" + File.separator + "pokecenter.png"));
        // Create the pokemart map.
        Map pokemart = new Map(this, 933, 600, 683, 450, "Pokemart", new File(
                "maps" + File.separator + "pokemart.png"));
        // Create the first level cave map
        Map cave1 = new Map(this, 640, 640, 640, 640, "Cave 1", new File("maps"
                + File.separator + "cave1.png"));
        // Create the second level cave map.
        Map cave2 = new Map(this, 819, 640, 819, 640, "Cave 2", new File("maps"
                + File.separator + "cave2.png"));
        // Create map of the player's home
        Map home = new Map(this, 700, 600, 450, 400, "Home", new File("maps"
                + File.separator + "house1.png"));
        // Create the map for the hallway inside the player's home
        Map hallway = new Map(this, 700, 600, 510, 240, "Hallway", new File(
                "maps" + File.separator + "hallway.png"));
        // Create the map for the room inside the player's home
        Map room = new Map(this, 700, 600, 300, 225, "Room", new File("maps"
                + File.separator + "room.png"));
        // Create the lab map
        Map lab = new Map(this, 800, 800, 490, 566, "Lab", new File("maps"
                + File.separator + "lab.png"));
        // Create the map for the house in the bottom left of the map.
        Map house = new Map(this, 600, 600, 430, 350, "House", new File("maps"
                + File.separator + "house2.png"));
        // Create the map of the secret room.
        Map end = new Map(this, 600, 600, 550, 450, "End", new File("maps"
                + File.separator + "end.png"));
        // Create the dialog for the nurse that heals the player's pokemon.
        String dialog = "Welcome to our Pokemon Center!#Please wait while I restore you Pokemon.#Thank you for waiting.#We've restored your Pokemon to full health.#We hope to see you soon!";
        // Add the nurse npc to the counter so the player can talk to her over the counter.
        pokecenter.addNPC(new NPC(this, 245, 45, 50, 80, dialog, "Nurse",
                new File("NPCs" + File.separator + "nothing.png")));
        // Add the nurse with the proper sprite so she is displayed behind the counter.
        pokecenter.addNPC(new NPC(this, 242, 45, 30, 40, "", "", new File(
                "NPCs" + File.separator + "healer.png")));
        // Create the dialog for the pokemart seller.
        String talk = "Hi there!#How may I help you?#";
        // Add the counter as an npc so player can interact "over the counter".
        pokemart.addNPC(new NPC(this, 170, 183, 30, 20, talk, "Owner",
                new File("NPCs" + File.separator + "nothing.png")));
        // Add the actual NPC sprite.
        pokemart.addNPC(new NPC(this, 140, 163, 30, 40, "", "", new File("NPCs"
                + File.separator + "owner.png")));

        /* DRAW ALL THE WALLS FOR THE SPAWN/FIRST MAP */
        spawn.addWall(new Entity(this, 20, 0, 40, spawn.getHeight()));
        spawn.addWall(new Entity(this, 0, 20, spawn.getWidth(), 40));
        spawn.addWall(new Entity(this, 0, spawn.getHeight() - 95, spawn
                .getWidth(), 40));
        spawn.addWall(new Entity(this, spawn.getWidth() - 45, 0, 40, spawn
                .getHeight()));
        // Lab house
        spawn.addWall(new Entity(this, 90, 80, 250, 157));
        // Outside lab house
        spawn.addWall(new Entity(this, 340, 120, 200, 113));
        // Bird house
        spawn.addWall(new Entity(this, 580, 110, 40, 50));
        // Player house
        spawn.addWall(new Entity(this, 885, 80, 185, 110));
        // Player house tree
        spawn.addWall(new Entity(this, 765, 85, 90, 100));
        // Bench
        spawn.addWall(new Entity(this, 635, 145, 60, 20));
        // Poke center
        spawn.addWall(new Entity(this, 135, 405, 165, 130));
        // Poke mart
        spawn.addWall(new Entity(this, 160, 630, 120, 110));
        // Corner house
        spawn.addWall(new Entity(this, 50, 900, 170, 110));
        // Corner house fences
        spawn.addWall(new Entity(this, 220, 900, 285, 5));
        spawn.addWall(new Entity(this, 500, 900, 5, 80));
        spawn.addWall(new Entity(this, 500, 1040, 5, 80));
        // Temple walls
        spawn.addWall(new Entity(this, 828, 878, 50, 132));
        spawn.addWall(new Entity(this, 850, 830, 228, 50));
        spawn.addWall(new Entity(this, 1020, 830, 50, 200));
        spawn.addWall(new Entity(this, 1075, 885, 20, 80));
        spawn.addWall(new Entity(this, 920, 805, 100, 170));
        spawn.addWall(new Entity(this, 882, 882, 30, 30));
        // Fountain wall
        spawn.addWall(new Entity(this, 600, 570, 82, 72));
        // Lamps
        spawn.addWall(new Entity(this, 510, 350, 40, 45));
        spawn.addWall(new Entity(this, 720, 350, 40, 45));
        spawn.addWall(new Entity(this, 835, 465, 40, 45));
        spawn.addWall(new Entity(this, 400, 465, 40, 45));
        spawn.addWall(new Entity(this, 395, 660, 40, 45));
        spawn.addWall(new Entity(this, 725, 770, 40, 45));
        spawn.addWall(new Entity(this, 510, 770, 40, 45));
        spawn.addWall(new Entity(this, 835, 670, 40, 45));
        // sign
        spawn.addWall(new Entity(this, 245, 265, 30, 7));
        /* END OF SPAWN WALLS */
        /* CREATE ALL THE WARPS FOR THE SPAWN MAP */
        // Create a warp to the pokecenter.
        spawn.addWarp(new Warp(this, 205, 510, 25, 42, pokecenter, 240, 235,
                "back"));
        // Create a warp into the player's home
        spawn.addWarp(new Warp(this, 955, 180, 35, 30, home, 185, 355, "back"));
        // Create a warp into the pokemart.
        spawn.addWarp(new Warp(this, 215, 730, 30, 32, pokemart, 310, 350,
                "back"));
        // Create a warp allowing player to enter the cave
        spawn.addWarp(new Warp(this, 955, 960, 30, 32, cave1, 537, 418, "back"));
        // Create a warp to the lab
        spawn.addWarp(new Warp(this, 205, 219, 25, 42, lab, 300, 506, "back"));
        // Create a warp to the house.
        spawn.addWarp(new Warp(this, 125, 1000, 30, 25, house, 110, 315, "back"));
        /* END OF WARPS FOR SPAWN MAP */
        /* DRAW ALL THE WILD AREAS FOR THE SPAWN MAP */
        spawn.addWildArea(new WildArea(this, 345, 315, 125, 85, 1, 8,
                "lairon,numel,gulpin,swablu,seviper,aggron"));
        spawn.addWildArea(new WildArea(this, 345, 315, 5, 5, 60, 70,
                "latios,latias,regice,registeel,rayquaza"));
        spawn.addWildArea(new WildArea(this, 1030, 330, 125, 420, 1, 20,
                "mudkip,torchic,treecko,pikachu"));
        spawn.addWildArea(new WildArea(this, 1010, 500, 20, 5, 70, 75,
                "groudon,mew,deoxys,jirachi,ho-oh,lugia"));
        spawn.addWildArea(new WildArea(this, 980, 510, 25, 200, 1, 10,
                "pichu,makuhita,ralts,kirlia"));
        spawn.addWildArea(new WildArea(this, 620, 930, 110, 170, 10, 20,
                "meditite,larvitar,electrike"));
        spawn.addWildArea(new WildArea(this, 560, 960, 180, 150, 10, 20,
                "poochyena,silcoon,surskit,taillow"));
        spawn.addWildArea(new WildArea(this, 550, 1020, 240, 80, 40, 60,
                "blaziken,gardevoir,ninetales,salamence"));
        spawn.addWildArea(new WildArea(this, 540, 120, 5, 113, 30, 40,
                "wailmer,wailord,feebas,sharpedo,starmie,dratini,milotic"));
        spawn.addWildArea(new WildArea(this, 540, 120, 1, 113, 70, 75, "kyogre"));
        cave1.addWildArea(new WildArea(this, 0, 0, 640, 640, 20, 40,
                "geodude,graveler,golem,mawile,sableye,duskull,banette"));
        cave2.addWildArea(new WildArea(this, 0, 0, 819, 640, 30, 50,
                "geodude,graveler,golem,mawile,sableye,duskull,banette"));
        /* END OF WILD AREAS FOR SPAWN MAP */

        // Cave 1 Parts
        cave1.addWall(new Entity(this, 0, 0, 640, 100));
        cave1.addWall(new Entity(this, 0, 100, 77, 20));
        cave1.addWall(new Entity(this, 0, 120, 55, 60));
        cave1.addWall(new Entity(this, 0, 180, 95, 58));
        cave1.addWall(new Entity(this, 0, 238, 55, 58));
        cave1.addWall(new Entity(this, 0, 280, 90, 120));
        cave1.addWall(new Entity(this, 90, 300, 24, 100));
        cave1.addWall(new Entity(this, 90, 330, 45, 70));
        cave1.addWall(new Entity(this, 0, 400, 70, 100));
        cave1.addWall(new Entity(this, 70, 443, 25, 100));
        cave1.addWall(new Entity(this, 0, 500, 640, 139));
        cave1.addWall(new Entity(this, 125, 462, 52, 45));
        cave1.addWall(new Entity(this, 180, 480, 460, 45));
        cave1.addWall(new Entity(this, 225, 470, 415, 50));
        cave1.addWall(new Entity(this, 225, 375, 250, 150));
        cave1.addWall(new Entity(this, 190, 383, 70, 35));
        cave1.addWall(new Entity(this, 208, 362, 248, 70));
        cave1.addWall(new Entity(this, 310, 340, 145, 70));
        cave1.addWall(new Entity(this, 330, 300, 125, 90));
        cave1.addWall(new Entity(this, 385, 280, 70, 30));
        cave1.addWall(new Entity(this, 480, 420, 30, 70));
        cave1.addWall(new Entity(this, 510, 440, 22, 70));
        cave1.addWall(new Entity(this, 567, 420, 42, 50));
        cave1.addWall(new Entity(this, 613, 440, 20, 20));
        cave1.addWall(new Entity(this, 640, 0, 1, 640));
        cave1.addWall(new Entity(this, 580, 401, 20, 20));
        cave1.addWall(new Entity(this, 507, 280, 103, 60));
        cave1.addWall(new Entity(this, 560, 339, 20, 20));
        cave1.addWall(new Entity(this, 505, 0, 88, 300));
        cave1.addWall(new Entity(this, 580, 0, 50, 210));
        cave1.addWall(new Entity(this, 560, 195, 50, 35));
        cave1.addWall(new Entity(this, 445, 0, 60, 230));
        cave1.addWall(new Entity(this, 410, 0, 20, 190));
        cave1.addWall(new Entity(this, 325, 0, 70, 230));
        cave1.addWall(new Entity(this, 165, 0, 200, 190));
        cave1.addWall(new Entity(this, 110, 0, 200, 130));
        cave1.addWall(new Entity(this, 165, 260, 90, 30));
        cave1.addWall(new Entity(this, 185, 240, 70, 30));
        // ------------

        // Cave 2 Parts
        cave2.addWall(new Entity(this, 0, 0, 95, 240));
        cave2.addWall(new Entity(this, 95, 180, 150, 60));
        cave2.addWall(new Entity(this, 245, 205, 180, 52));
        cave2.addWall(new Entity(this, 160, 240, 100, 20));
        cave2.addWall(new Entity(this, 265, 240, 115, 125));
        cave2.addWall(new Entity(this, 265, 240, 135, 80));
        cave2.addWall(new Entity(this, 265, 240, 160, 40));
        cave2.addWall(new Entity(this, 310, 360, 70, 30));
        cave2.addWall(new Entity(this, 0, 240, 35, 30));
        cave2.addWall(new Entity(this, 0, 0, 20, 640));
        cave2.addWall(new Entity(this, 20, 385, 55, 255));
        cave2.addWall(new Entity(this, 75, 470, 40, 170));
        cave2.addWall(new Entity(this, 115, 485, 230, 150));
        cave2.addWall(new Entity(this, 345, 560, 80, 80));
        cave2.addWall(new Entity(this, 425, 590, 394, 50));
        cave2.addWall(new Entity(this, 545, 540, 270, 80));
        cave2.addWall(new Entity(this, 570, 460, 249, 170));
        cave2.addWall(new Entity(this, 800, 0, 19, 640));
        cave2.addWall(new Entity(this, 0, 0, 819, 35));
        cave2.addWall(new Entity(this, 90, 40, 25, 20));
        cave2.addWall(new Entity(this, 495, 0, 400, 215));
        cave2.addWall(new Entity(this, 495, 215, 80, 50));
        cave2.addWall(new Entity(this, 260, 0, 250, 135));
        cave2.addWall(new Entity(this, 240, 40, 25, 20));
        cave2.addWall(new Entity(this, 595, 240, 12, 100));
        cave2.addWall(new Entity(this, 595, 340, 30, 5));
        cave2.addWall(new Entity(this, 625, 340, 5, 25));
        cave2.addWall(new Entity(this, 625, 365, 30, 5));
        cave2.addWall(new Entity(this, 720, 365, 55, 5));
        cave2.addWall(new Entity(this, 775, 240, 10, 130));
        // ------------

        // lab parts
        lab.addWall(new Entity(this, 25, 25, 1, 522));
        lab.addWall(new Entity(this, 25, 25, 440, 75));
        lab.addWall(new Entity(this, 25, 541, 450, 1));
        lab.addWall(new Entity(this, 465, 25, 1, 522));
        lab.addWall(new Entity(this, 370, 25, 80, 110));
        lab.addWall(new Entity(this, 70, 180, 120, 90));
        lab.addWall(new Entity(this, 190, 180, 285, 60));
        lab.addWall(new Entity(this, 230, 265, 60, 45));
        lab.addWall(new Entity(this, 360, 265, 60, 45));
        lab.addWall(new Entity(this, 420, 240, 40, 40));
        lab.addWall(new Entity(this, 30, 390, 200, 45));
        lab.addWall(new Entity(this, 390, 390, 60, 45));
        lab.addWall(new Entity(this, 30, 480, 40, 40));
        lab.addWall(new Entity(this, 420, 480, 40, 40));
        lab.addWall(new Entity(this, 70, 505, 55, 30));

        // Add the professor.
        lab.addNPC(new NPC(this, 450, 50, 54, 46, "Ok", "Pine", new File("NPCs"
                + File.separator + "professor.png")));
        // ------------

        // pokecenter parts
        pokecenter.addWall(new Entity(this, 5, 5, 490, 50));
        pokecenter.addWall(new Entity(this, 5, 5, 15, 280));
        pokecenter.addWall(new Entity(this, 5, 280, 490, 1));
        pokecenter.addWall(new Entity(this, 5, 255, 35, 30));
        pokecenter.addWall(new Entity(this, 470, 255, 35, 30));
        pokecenter.addWall(new Entity(this, 495, 5, 10, 280));
        pokecenter.addWall(new Entity(this, 150, 50, 210, 50));
        pokecenter.addWall(new Entity(this, 375, 190, 50, 30));
        // ------------

        // pokemart parts
        pokemart.addWall(new Entity(this, 60, 55, 450, 50));
        pokemart.addWall(new Entity(this, 60, 55, 5, 300));
        pokemart.addWall(new Entity(this, 515, 55, 5, 300));
        pokemart.addWall(new Entity(this, 60, 390, 440, 1));
        pokemart.addWall(new Entity(this, 490, 370, 40, 40));
        pokemart.addWall(new Entity(this, 495, 85, 40, 35));
        pokemart.addWall(new Entity(this, 60, 370, 30, 40));
        pokemart.addWall(new Entity(this, 60, 60, 140, 160));
        pokemart.addWall(new Entity(this, 140, 290, 60, 15));
        pokemart.addWall(new Entity(this, 350, 201, 50, 65));
        pokemart.addWall(new Entity(this, 485, 201, 30, 65));
        // ------------

        // home parts
        home.addWall(new Entity(this, 308, 255, 90, 130));
        home.addWall(new Entity(this, 280, 255, 30, 25));
        home.addWall(new Entity(this, 30, 20, 370, 50));
        home.addWall(new Entity(this, 23, 20, 1, 280));
        home.addWall(new Entity(this, 400, 20, 1, 330));
        home.addWall(new Entity(this, 23, 300, 60, 1));
        home.addWall(new Entity(this, 83, 300, 1, 90));
        home.addWall(new Entity(this, 84, 390, 230, 1));
        home.addWall(new Entity(this, 70, 195, 55, 38));
        home.addWall(new Entity(this, 113, 120, 150, 6));
        home.addWall(new Entity(this, 195, 169, 60, 8));
        home.addWall(new Entity(this, 200, 20, 90, 100));
        home.addWall(new Entity(this, 260, 20, 30, 170));
        home.addWall(new Entity(this, 290, 130, 30, 35));
        home.addWall(new Entity(this, 290, 120, 50, 5));
        // ------------

        // hallway parts
        hallway.addWall(new Entity(this, 60, 20, 410, 50));
        hallway.addWall(new Entity(this, 60, 20, 2, 200));
        hallway.addWall(new Entity(this, 60, 120, 26, 80));
        hallway.addWall(new Entity(this, 60, 220, 410, 1));
        hallway.addWall(new Entity(this, 470, 20, 1, 200));
        hallway.addWall(new Entity(this, 170, 70, 190, 30));
        hallway.addWall(new Entity(this, 410, 125, 60, 5));
        hallway.addWall(new Entity(this, 410, 185, 60, 5));
        // ------------

        // room parts
        room.addWall(new Entity(this, 45, 15, 130, 60));// 230
        room.addWall(new Entity(this, 45, 15, 1, 180));
        room.addWall(new Entity(this, 45, 195, 230, 1));
        room.addWall(new Entity(this, 275, 15, 1, 180));
        room.addWall(new Entity(this, 45, 15, 230, 10));
        // ------------

        // house parts
        house.addWall(new Entity(this, 0, 0, 430, 60));
        house.addWall(new Entity(this, 0, 0, 1, 350));
        house.addWall(new Entity(this, 430, 0, 1, 350));
        house.addWall(new Entity(this, 0, 350, 430, 1));
        house.addWall(new Entity(this, 180, 150, 65, 45));
        house.addWall(new Entity(this, 0, 240, 35, 40));
        house.addWall(new Entity(this, 395, 240, 35, 40));
        // ------------

        // end parts
        end.addWall(new Entity(this, 240, 135, 70, 55));
        end.addWall(new Entity(this, 20, 20, 510, 50));
        end.addWall(new Entity(this, 200, 80, 150, 50));
        end.addWall(new Entity(this, 20, 20, 5, 430));
        end.addWall(new Entity(this, 525, 20, 5, 430));
        end.addWall(new Entity(this, 20, 450, 510, 1));

        // ------------

        // warp
        cave2.addWarp(new Warp(this, 127, 35, 30, 50, cave1, 95, 455, "back"));
        cave1.addWarp(new Warp(this, 92, 457, 35, 50, cave2, 130, 37, "forward"));
        cave1.addWarp(new Warp(this, 530, 450, 35, 30, spawn, 955, 970,
                "forward"));
        pokecenter.addWarp(new Warp(this, 235, 272, 40, 25, spawn, 205, 535,
                "forward"));
        pokemart.addWarp(new Warp(this, 295, 370, 50, 25, spawn, 215, 732,
                "forward"));
        home.addWarp(new Warp(this, 185, 370, 40, 30, spawn, 960, 182,
                "forward"));
        home.addWarp(new Warp(this, 310, 90, 30, 20, hallway, 397, 130, "left"));
        hallway.addWarp(new Warp(this, 410, 140, 45, 12, home, 330, 70, "right"));
        hallway.addWarp(new Warp(this, 132, 70, 30, 35, room, 80, 145, "back"));
        room.addWarp(new Warp(this, 65, 180, 50, 30, hallway, 132, 72,
                "forward"));
        lab.addWarp(new Warp(this, 290, 525, 45, 25, spawn, 205, 233, "forward"));
        lab.addWarp(new Warp(this, 290, 205, 35, 60, end, 270, 410, "back"));
        house.addWarp(new Warp(this, 100, 320, 50, 30, spawn, 125, 1005,
                "forward"));
        end.addWarp(new Warp(this, 255, 430, 40, 20, home, 200, 200, "forward"));

        // Add all the maps into the double linked list holding all the maps.
        this.maps.add(spawn);
        this.maps.add(cave1);
        this.maps.add(cave2);
        this.maps.add(pokemart);
        this.maps.add(pokecenter);
        this.maps.add(home);
        this.maps.add(hallway);
        this.maps.add(room);
        this.maps.add(lab);
        this.maps.add(house);
        this.maps.add(end);
    }

    /**
     * Method to save the game & all the settings.
     * @param dialog true if a dialog box is to be shown to the player.
     */
    public void saveGame(boolean dialog) {
        // Save all the data for the player.
        PokemonDawn.saveData(player.getGame());
        // Log to console that data has been saved.
        System.out.println("> All data has been saved.");
        // Tell the player that data has been saved.
        if (dialog) {
            DialogBox saved = new DialogBox(this, "Game has been saved.",
                    getGlobalTextSpeed());
            saved.show(getMapPanel());
        }
    }

    /**
     * Method to get the game camera.
     * @return the game camera
     */
    public GameCamera getGameCamera() {
        return gc;
    }

    /**
     * Method to get the player object for the game
     * @return the player object
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Method to get the map panel for displaying maps
     * @return the map panel instance
     */
    public MapPanel getMapPanel() {
        return mp;
    }

    /**
     * Method to get all the maps
     * @return a double linked list of maps
     */
    public DoubleLinkedList<Map> getMaps() {
        return maps;
    }

    /**
     * Method to get the key listener being used for the game.
     * @return the instance of the key listener
     */
    public KeyPressed getKeyListener() {
        return kl;
    }

    /**
     * Method to check if the game is currently paused
     * @return true if the game is paused, false if it's not.
     */
    public boolean isPaused() {
        return this.paused;
    }

    /**
     * Method to set the game to be paused.
     * @param paused true if the game should be paused, false if it should be unpaused.
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Method to get all the options for the game
     * @return the instance of the options class
     */
    public Options getOptions() {
        return options;
    }

    /**
     * Method to get the bag
     * @return the bag instance
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * Method to get the pokemart
     * @return the pokemart class instance
     */
    public Pokemart getPokemart() {
        this.pokemart = new Pokemart(this, player.getMoney(), player);
        return pokemart;
    }

    /**
     * Method to get the party class
     * @return the party instance
     */
    public Party getParty() {
        return this.party;
    }

    /**
     * Method to get a map based on it's name.
     * @param name the name of the map that will be searched for
     * @return the map instance with the same name as the parameter
     */
    public Map getMap(String name) {
        // Loop through all the maps
        for (int i = 0; i < getMaps().size(); i++) {
            // Get the map at the index
            Map m = getMaps().get(i);
            // If the map names are the same...
            if (m.getName().equalsIgnoreCase(name)) {
                // Return the map
                return m;
            }
        }
        // No map was found, return null.
        return null;
    }

    /**
     * Method to get the text speed based on the options the player chose.
     * @return 3 for slow, 5 for medium, and 8 for fast.
     */
    public int getGlobalTextSpeed() {
        // Get the value of the text speed option.
        String textSpeed = getOptions().getTextSpeed();
        // Check if the text speed is equal to slow
        if (textSpeed.equalsIgnoreCase("slow")) {
            // If text speed is slow, return 3.
            return 3;
        } else if (textSpeed.equalsIgnoreCase("medium")) {
            // Return 5 if the speed is medium
            return 5;
        } else {
            // Return 8 if the speed is fast
            return 8;
        }
    }

    /**
     * Method to check if a dialog box is currently running
     * @return true if a box is running, false if not.
     */
    public boolean isDialogRunning() {
        return this.dialogRunning;
    }

    /**
     * Method to set if dialog is running
     * @param val the value that the variable will hold.
     */
    public void setDialogRunning(boolean val) {
        this.dialogRunning = val;
    }

}
