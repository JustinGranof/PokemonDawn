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
        JButton yes = new JButton("Yes");
        yes.setContentAreaFilled(false);
        yes.setBorderPainted(false);
        yes.setFocusPainted(false);
        yes.setFont(font);
        yes.addActionListener(this);
        yes.setBounds(270, 267, 100, 50);
        
        JButton no = new JButton("No");
        no.setContentAreaFilled(false);
        no.setBorderPainted(false);
        no.setFocusPainted(false);
        no.setFont(font);
        no.addActionListener(this);
        no.setBounds(400, 267, 100, 50);

        switch(autoSave){
            case "Yes":
                yes.setBorderPainted(true);
                yes.setBorder(selected);
                this.autoSave = yes;
                break;
            case "No":
                no.setBorderPainted(true);
                no.setBorder(selected);
                this.autoSave = no;
                break;
        }

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

    @Override
    protected void paintComponent(Graphics g) {
        // Paint the picture & options.
        try{
            BufferedImage text = ImageIO.read(new File("border.png"));
            g.setFont(new Font("Monospaced", Font.BOLD, 25));
            g.setColor(Color.black);
            g.drawImage(text, -10, 0, 600, 80, null);
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

        if(e.getSource() instanceof JButton){
            JButton button = (JButton) e.getSource();

            if(button.getText().equalsIgnoreCase("Exit")){
                // Exit options.
                game.setContent(game.getMapPanel());
                game.requestFocus();
                return;
            }

            if(button.getText().equalsIgnoreCase(getTextSpeed())){
                return;
            }

            if(button.getText().equalsIgnoreCase(getAutoSave())){
                return;
            }

            if(button.getText().equalsIgnoreCase(getHitboxes())){
                return;
            }

            // Player selected a new option
            if(button.getText().equalsIgnoreCase("slow")
                    || button.getText().equalsIgnoreCase("medium")
                    || button.getText().equalsIgnoreCase("fast")){
                this.textSpeed.setBorderPainted(false);
                this.textSpeed = button;
                this.textSpeed.setBorderPainted(true);
                this.textSpeed.setBorder(selected);
            }

            if(button.getText().equalsIgnoreCase("yes")
                    || button.getText().equalsIgnoreCase("no")){
                this.autoSave.setBorderPainted(false);
                this.autoSave = button;
                this.autoSave.setBorderPainted(true);
                this.autoSave.setBorder(selected);
            }

            if(button.getText().equalsIgnoreCase("on")
                    || button.getText().equalsIgnoreCase("off")){
                this.hitboxes.setBorderPainted(false);
                this.hitboxes = button;
                this.hitboxes.setBorderPainted(true);
                this.hitboxes.setBorder(selected);
            }

        }

    }

    public String getTextSpeed() {
        return textSpeed.getText();
    }

    public String getAutoSave(){
        return autoSave.getText();
    }

    public String getHitboxes(){
        return hitboxes.getText();
    }

}
