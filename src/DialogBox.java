import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DialogBox extends JPanel {

    // Variables that every dialog box will have.
    private String dialog;
    private JLabel text;
    private JPanel panel;
    private DialogBox box;
    private Game game;
    private int speed;
    private static BufferedImage border = null;

    /**
     * Constructor for a dialog box
     * @param game the game instance
     * @param dialog the dialog that will be shown
     * @param speed the speed at which the type-writer will run
     */
    public DialogBox(Game game, String dialog, int speed) {
        // Save an instance of this JPanel
        this.box = this;
        // Save the game variable to a private variable that can be accessed throughout the class.
        this.game = game;
        // Set the layout of the panel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Set private variable
        this.dialog = dialog;
        // Set the size of the JPanel
        this.setPreferredSize(new Dimension(0, 100));
        // Set the border of the JPanel
        this.setBorder(new EmptyBorder(35, 30, 0, 30));
        // Create a JLabel to store the type-writer text.
        this.text = new JLabel();
        // Set the font of the JLabel
        this.text.setFont(new Font("Monospaced", Font.BOLD, 20));
        // Set the colour of the JLabel to black
        this.text.setForeground(Color.black);
        // Save the speed
        this.speed = speed;
        // If the static variable has not been defined yet...
        if (border == null) {
            try {
                // Load in the image from the files for the border/dialog box.
                border = ImageIO.read(new File("border.png"));
            } catch (IOException e) {
            }
        }
    }

    /**
     * Method to show the dialog box onto a panel
     * @param panel the panel that the dialog box will be put ontop of
     */
    public void show(final JPanel panel) {
        // Tell the game that there is currently a dialog box being shown
        game.setDialogRunning(true);
        // Disable the player from moving
        game.getPlayer().setCanMove(false);
        // Save the panel that is being drawn to.
        this.panel = panel;
        // Check if the panel's layout is null
        if (this.panel.getLayout() == null) {
            // If a null layout is found, position the JPanel at the bottom of the screen.
            this.setBounds(0, 461, 600, 200);
            // Add the dialog box to the panel
            this.panel.add(this);
        } else {
            // Add the dialog box to the panel
            this.panel.add(this, BorderLayout.SOUTH);
        }
        // Create a new thread to manage the type-writer
        Thread typewriter = new Thread() {
            @Override
            public void run() {
                // Loop until the thread is interrupted/stopped.
                while (!(this.isInterrupted())) {
                    // Loop through each line of text. (Separated by a '#')
                    for (String line : getDialog().split("#")) {
                        // SPECIAL CASE: This is used in the intro to print the name of the player.
                        if (line.equalsIgnoreCase("who's moving to my")) {
                            line = "Oh, you're " + Dialog.name + " who's moving to my";
                        }
                        // Save the pressed amount before the dialog is shown.
                        int previousPressed = KeyPressed.pressedAmount;
                        // Loop through each letter index in the sentence/line.
                        for (int i = 0; i < line.length(); i++) {
                            // Get the character at the given index.
                            char letter = line.charAt(i);
                            // Remove the JLabel from the dialog box
                            box.remove(text);
                            // Set the text of the JLabel, adding a letter to it.
                            text.setText(text.getText() + letter);
                            // Add the JLabel back to the dialog box
                            box.add(text);
                            // Refresh the dialog box.
                            box.revalidate();
                            try {
                                // Have the thread sleep for a small amount of time. This controls
                                // how fast the type-writer is.
                                sleep(100 / speed);
                            } catch (Exception e) {
                            }
                        }
                        // Line has been printed. Wait for player to press a key.
                        game.getPlayer().setDialog(true);
                        // Stay in the while loop until a key is pressed.
                        while (KeyPressed.pressedAmount == previousPressed) {
                            try {
                                sleep(50);
                            } catch (Exception e) {
                            }
                        }
                        // Play a sound when the player continues the dialog.
                        Sounds.DIALOG.play(false, true);
                        // Set the player to not be in dialog, allowing their pressed keys to not affect dialog box progress.
                        game.getPlayer().setDialog(false);
                        // Call the onLine() event, passing the current line through parameters.
                        onLine(line);
                        // Clear the JLabel containing the line.
                        text.setText("");
                    }
                    // Allow the player to move again
                    game.getPlayer().setCanMove(true);
                    // Remove the dialog box from the parent panel
                    panel.remove(box);
                    // Repaint and refresh the parent panel that the dialog box was drawn to.
                    panel.repaint();
                    panel.revalidate();
                    // Tell the game that dialog is no longer running
                    game.setDialogRunning(false);
                    // Call the onComplete() method now that the dialog is over.
                    onComplete();
                    // Interrupt the thread to avoid memory loss & infinite looping.
                    this.interrupt();
                }
            }
        };
        // Start the type-writer thread.
        typewriter.start();
    }

    /**
     * Method to get the dialog for the dialog box.
     * @return the string dialog (unchanged)
     */
    public String getDialog() {
        return dialog;
    }

    /**
     * Event called when a dialog box is completed.
     */
    public void onComplete() {

    }

    /**
     * Event called when a dialog box completes a line
     * @param line the line that was completed
     */
    public void onLine(String line) {

    }

    /**
     * Method to paint the border onto the panel.
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Paint the border onto the panel.
        g.drawImage(this.border, -10, 0, 605, 100, null);
    }
}
