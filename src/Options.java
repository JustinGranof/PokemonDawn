/**
 * Options.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * Options JPanel class.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Options extends JPanel implements ActionListener {

    // Variables to store for all the options
    private Game game;
    private JButton textSpeed;
    private JButton autoSave;
    private JButton hitboxes;
    private static LineBorder selected = new LineBorder(Color.red, 2, true);

    /**
     * Constructor for the options
     * @param game the instance of the game
     * @param textSpeed the text speed that the options will be set to.
     * @param autoSave whether or not auto save is active or not.
     * @param hitboxes whether or not hitboxes are to be displayed or not.
     */
    public Options(Game game, String textSpeed, String autoSave, String hitboxes){
        // Set a null layout
        setLayout(null);
        // Set the panel to be visible.
        this.setVisible(true);
        // Create game
        this.game = game;
        // Create the font for options.
        Font font = new Font("Monospaced", Font.BOLD, 20);
        /* Text Speed Options */
        // Button for slow text speed
        JButton slow = new JButton("Slow");
        // Format the button to remove all styling.
        slow.setBorderPainted(false);
        slow.setContentAreaFilled(false);
        slow.setFocusPainted(false);
        // Set the font of the button's text.
        slow.setFont(font);
        // Add the action listener to the button.
        slow.addActionListener(this);
        // Position the button on the JPanel.
        slow.setBounds(220, 167, 100, 50);

        // Button for medium text speed
        JButton medium = new JButton("Medium");
        // Format the button to remove all styling.
        medium.setContentAreaFilled(false);
        medium.setBorderPainted(false);
        medium.setFocusPainted(false);
        // Set the font of the button's text.
        medium.setFont(font);
        // Add the action listener for the button
        medium.addActionListener(this);
        // Position the button on the JPanel.
        medium.setBounds(330, 167, 110, 50);

        // Button for fast text speed
        JButton fast = new JButton("Fast");
        // Format the button to remove all styling.
        fast.setContentAreaFilled(false);
        fast.setBorderPainted(false);
        fast.setFocusPainted(false);
        // Set the font of the button's text.
        fast.setFont(font);
        // button
        fast.addActionListener(this);
        // Position the button on the JPanel.
        fast.setBounds(450, 167, 100, 50);

        // Determine which text speed is currently selected.
        switch(textSpeed){
            // If the text speed is slow
            case "Slow":
                // Set the border of the button to show that it's selected.
                slow.setBorderPainted(true);
                slow.setBorder(selected);
                // Save the option for text speed
                this.textSpeed = slow;
                break;
            case "Medium":
                // Set the border of the button to show that it's selected.
                medium.setBorderPainted(true);
                medium.setBorder(selected);
                // Save the option for text speed
                this.textSpeed = medium;
                break;
            case "Fast":
                // Set the border of the button to show that it's selected.
                fast.setBorderPainted(true);
                fast.setBorder(selected);
                // Save the option for text speed
                this.textSpeed = fast;
                break;
        }

        // Add all the buttons to the JPanel for options (text-speed)
        add(slow);
        add(medium);
        add(fast);
        /* End Of Text Speed Options */

        /* Auto Save Option */
        // Create a button for user to select yes to auto save.
        JButton yes = new JButton("Yes");
        // Format the button, get rid of all default styling.
        yes.setContentAreaFilled(false);
        yes.setBorderPainted(false);
        yes.setFocusPainted(false);
        // Set the font of the button's text
        yes.setFont(font);
        // Add an action listener to the button
        yes.addActionListener(this);
        // Set the location and dimensions of the button.
        yes.setBounds(270, 267, 100, 50);

        // Create a button for user to select no to auto save.
        JButton no = new JButton("No");
        // Format the button, get rid of all default styling.
        no.setContentAreaFilled(false);
        no.setBorderPainted(false);
        no.setFocusPainted(false);
        // Set the font of the button's text
        no.setFont(font);
        // Add an action listener to the button
        no.addActionListener(this);
        // Set the location and dimensions of the button.
        no.setBounds(400, 267, 100, 50);

        // Determine which option is currently selected.
        switch(autoSave){
            // If the case is yes...
            case "Yes":
                // Draw a red border around the button.
                yes.setBorderPainted(true);
                yes.setBorder(selected);
                // Set the current option to be the yes button.
                this.autoSave = yes;
                break;
            // If the case is no...
            case "No":
                // Add a red border around the button to show that it is selected
                no.setBorderPainted(true);
                no.setBorder(selected);
                // Set the current option of auto save to be the no button.
                this.autoSave = no;
                break;
        }

        // Add the buttons onto the JPanel.
        add(yes);
        add(no);
        /* End Of Auto Save Option */

        /* Hitboxes Option */
        JButton on = new JButton("On");
        on.setContentAreaFilled(false);
        on.setBorderPainted(false);
        on.setFocusPainted(false);
        on.setFont(font);
        on.addActionListener(this);
        on.setBounds(270, 367, 100, 50);

        JButton off = new JButton("Off");
        off.setContentAreaFilled(false);
        off.setBorderPainted(false);
        off.setFocusPainted(false);
        off.setFont(font);
        off.addActionListener(this);
        off.setBounds(400, 367, 100, 50);

        switch(hitboxes){
            case "On":
                on.setBorderPainted(true);
                on.setBorder(selected);
                this.hitboxes = on;
                break;
            case "Off":
                off.setBorderPainted(true);
                off.setBorder(selected);
                this.hitboxes = off;
                break;
        }

        add(on);
        add(off);
        /* End Of Hitboxes Option */
        
        /* Exit Options Button */
        JButton exit = new JButton("Exit");
        exit.setContentAreaFilled(false);
        exit.setBorder(new LineBorder(Color.black, 3, true));
        exit.setFocusPainted(false);
        exit.setFont(font);
        exit.addActionListener(this);
        exit.setBounds(240, 450, 100, 50);

        add(exit);
    }

    /**
     * Method to paint all the background for the options Panel.
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Paint the picture & options.
        try{
            // Get the border picture from the files.
            BufferedImage text = ImageIO.read(new File("border.png"));
            // Set the font of the text
            g.setFont(new Font("Monospaced", Font.BOLD, 25));
            // Set the colour
            g.setColor(Color.black);
            // Draw the background for the text
            g.drawImage(text, -10, 0, 600, 80, null);
            // Draw the text to the screen.
            g.drawString("Options", 28, 47);

            // Draw the actual options box
            g.drawImage(text, -10, 90, 600, 495, null);
        }catch(IOException e){}

        // Draw all the different options
        g.drawString("Text Speed", 50, 200);
        g.drawString("Auto Save", 50, 300);
        g.drawString("Show Hitboxes", 50, 400);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Make sure that the source is a button.
        if(e.getSource() instanceof JButton){
            JButton button = (JButton) e.getSource();

            // If the user wants to exit...
            if(button.getText().equalsIgnoreCase("Exit")){
                // Return to the map panel.
                game.setContent(game.getMapPanel());
                game.requestFocus();
                return;
            }

            // If the user clicks any of the already selected options, just return.
            if(button.getText().equalsIgnoreCase(getTextSpeed())){
                return;
            }

            if(button.getText().equalsIgnoreCase(getAutoSave())){
                return;
            }

            if(button.getText().equalsIgnoreCase(getHitboxes())){
                return;
            }
            // -------------------------------------------------------------------

            // If the player selected a text speed...
            if(button.getText().equalsIgnoreCase("slow")
                    || button.getText().equalsIgnoreCase("medium")
                    || button.getText().equalsIgnoreCase("fast")){
                // Add the border to the newly selected button, and store the new selected text speed.
                this.textSpeed.setBorderPainted(false);
                this.textSpeed = button;
                this.textSpeed.setBorderPainted(true);
                this.textSpeed.setBorder(selected);
            }

            // If the player selected an auto save option...
            if(button.getText().equalsIgnoreCase("yes")
                    || button.getText().equalsIgnoreCase("no")){
                // Indicate the newly chosen option with a red border and save the new auto save option.
                this.autoSave.setBorderPainted(false);
                this.autoSave = button;
                this.autoSave.setBorderPainted(true);
                this.autoSave.setBorder(selected);
            }

            // If the user wants to change hit boxes
            if(button.getText().equalsIgnoreCase("on")
                    || button.getText().equalsIgnoreCase("off")){
                // Indicate the newly chosen option with a red border, and store the option.
                this.hitboxes.setBorderPainted(false);
                this.hitboxes = button;
                this.hitboxes.setBorderPainted(true);
                this.hitboxes.setBorder(selected);
            }

        }

    }

    /**
     * Method to get the text speed option.
     * @return the string version of the option.
     */
    public String getTextSpeed() {
        return textSpeed.getText();
    }

    /**
     * Method to get the auto save option.
     * @return the saved string "yes" or "no".
     */
    public String getAutoSave(){
        return autoSave.getText();
    }

    /**
     * Method to get the hit boxes option
     * @return the saved string "on" or "off".
     */
    public String getHitboxes(){
        return hitboxes.getText();
    }

}
