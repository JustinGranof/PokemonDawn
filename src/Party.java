/**
 * Party.java
 * Justin Granofsky & Bill Wu
 * 6/13/2018
 * Party JPanel class
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Party extends JPanel implements ActionListener, MouseListener {

	// Private variables for the party JPanel
	private Game game;
	private boolean choose;
	private Party instance;
	private JPanel remove;
	private Pokemon selected;

	/**
	 * Constructor for a party
	 * @param game the instance of the game
	 * @param choose whether or not player will release
	 */
	public Party(Game game, boolean choose) {
		// Initialize all the variables
		this.game = game;
		this.instance = this;
		this.selected = null;
		this.choose = choose;
		// Add the key listener to the JPanel
		this.addKeyListener(game.getKeyListener());
		// Create a new JPanel for releasing pokemon
		this.remove = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// Set the font of the text in the JPanel
				g.setFont(new Font("Monospaced", Font.BOLD, 25));
				// Set the colour of the background
				g.setColor((new Color(138, 182, 252)));
				// Draw the background of the box
				g.fillRect(0, 0, 320, 200);
				// Set the colour for the border
				g.setColor(Color.black);
				// Draw a black border
				g.drawRect(0, 0, 319, 199);
				// Set the colour for the text
				g.setColor(Color.white);
				// Make sure a pokemon is selected
				if (selected != null) {
					// Draw string for releasing a pokemon.
					g.drawString("Release " + selected.getName().toUpperCase()
							+ "?", 5, 30);
				}

			}
		};
		// Add the key listener for the remove panel
		this.remove.addKeyListener(game.getKeyListener());
		// Set the layout to null
		this.remove.setLayout(null);
		// Make the panel not visible at first
		this.remove.setVisible(false);
		// Position the panel in the center of the screen
		this.remove.setBounds(142, 159, 320, 200);
		// Add the panel to the party panel.
		this.add(remove);

		// REMOVE PANEL BUTTONS.

		// Button for if the player wishes to release the pokemon.
		JButton yes = new JButton("YES");
		yes.setFont(new Font("Monospaced", Font.BOLD, 28));
		yes.setBorderPainted(false);
		yes.setBounds(80, 76, 85, 50);
		yes.setContentAreaFilled(false);
		yes.setFocusPainted(false);
		yes.setForeground(Color.white);
		yes.addActionListener(this);
		yes.addMouseListener(this);
		this.remove.add(yes);

		// Button for if the player wishes to cancel releasing the pokemon
		JButton no = new JButton("NO");
		no.setFont(new Font("Monospaced", Font.BOLD, 28));
		no.setBorderPainted(false);
		no.setBounds(160, 76, 80, 50);
		no.setContentAreaFilled(false);
		no.setFocusPainted(false);
		no.setForeground(Color.white);
		no.addActionListener(this);
		no.addMouseListener(this);
		this.remove.add(no);
		// ---------------------

		// Create a null layout for absolute positioning.
		this.setLayout(null);

		// Create the exit button
		Font hidden = new Font("TimesNewRoman", Font.PLAIN, 1);
		JButton exit = new JButton("exit");
		exit.setFont(hidden);
		exit.setBounds(70, 450, 430, 110);
		exit.setContentAreaFilled(false);
		exit.setBorderPainted(false);
		exit.setFocusPainted(false);
		exit.addActionListener(this);
		// Add exit button the panel
		this.add(exit);
	}

	/**
	 * Method to get the next available pokemon in the players party.
	 * @return the pokemon instance of the non-fainted pokemon.
	 */
	public Pokemon getNextAvailable() {
		// Loop thruogh the entire player's pokemon party
		for (int i = 0; i < game.getPlayer().getParty().size(); i++) {
			// Get the pokemon at the index
			Pokemon pokemon = game.getPlayer().getParty().get(i);
			// Ensure the pokemon is not fainted
			if (!pokemon.isFainted()) {
				// Return the pokemon
				return pokemon;
			}
		}
		// Return null since no pokemon were found that were alive.
		return null;
	}

	@Override
	protected void paintComponent(Graphics g) {
		try {
			// Draw the background image
			BufferedImage background = ImageIO.read(PokemonDawn.getResource("party.png"));
			g.drawImage(background, -5, 0, 600, 600, null);
			// Set text font
			g.setFont(new Font("Monospaced", Font.BOLD, 20));

			// Variables to save row and column of pokemon
			double row;
			double col ;

			// Loop through player's party
			for (int i = 1; i <= game.getPlayer().getParty().size(); i++) {
				// Get the pokemon at the index
				Pokemon pokemon = game.getPlayer().getParty().get(i - 1);
				// Determine which row the pokemon should be in.
				if (i == 1 || i == 2) {
					row = 0;
				} else if (i == 3 || i == 4) {
					row = 1;
				} else {
					row = 2;
				}
				// If the number is odd, set the column to be 0
				if (i % 2.0 == 0.0 && i != 0) {
					col = 1;
				} else {
					col = 0;
				}
				// Determine x and y positions based on the row and column.
				int xBox = 25 + 285 * (int) col;
				int yBox = 45 + 145 * (int) row;
				// Draw the pokemon sprite
				g.drawImage(pokemon.getSprites()[0], xBox, yBox, 64, 64, null);
				// if the pokemon is fainted...
				if (pokemon.isFainted()) {
					// Draw red over the sprite.
					g.setColor(new Color(255, 20, 51, 100));
					g.fillRoundRect(xBox, yBox, 65, 65, 10, 10);
				}
				// change colour of graphics
				g.setColor(Color.white);
				// draw level and pokemon name
				g.drawString(
						pokemon.getName().toUpperCase() + "  Lv"
								+ pokemon.getLevel(), xBox + 75, yBox + 29);
				// Draw the health bar
				g.setColor(Color.red);
				g.fillRoundRect(xBox + 80, yBox + 40, 160, 10, 5, 5);
				double healthPercent = (pokemon.getHealth() * 1.0
						/ pokemon.getMaxHealth() * 1.0) * 100.0;
				g.setColor(Color.green);
				g.fillRoundRect(xBox + 80, yBox + 40,
						(160 * (int) healthPercent) / 100, 10, 5, 5);
				g.setColor(Color.white);
				g.setFont(new Font("Monospaced", Font.PLAIN, 17));
				// draw text-based health
				g.drawString(
						pokemon.getHealth() + "/" + pokemon.getMaxHealth(),
						xBox + 185, yBox + 65);
				// create a jbutton over the box where the pokemon resides.
				JButton button = new JButton(i + "");
				button.setContentAreaFilled(false);
				button.setFont(new Font("Monospaced", Font.PLAIN, 0));
				button.setBounds(xBox - 5, yBox, 272, 75);
				button.setBorderPainted(false);
				button.setForeground(new Color(163, 187, 226));
				button.setFocusPainted(false);
				button.addActionListener(this);
				add(button);
				g.setFont(new Font("Monospaced", Font.BOLD, 20));
			}
		} catch (IOException e) {

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (game.isDialogRunning()) {
			return;
		}

		if (e.getSource() instanceof JButton) {
			JButton button = (JButton) e.getSource();
			if (button.getText().equalsIgnoreCase("exit")) {
				// Return player to the game
				if (!choose) {
					this.remove.setVisible(false);
					game.setContent(game.getMapPanel());
					game.requestFocus();
				}
			} else {
				// Check if player has pokemon to release
				if (game.getPlayer().getParty().size() > 1) {
					if (!this.remove.isVisible()) {
						// Player clicked a pokemon!
						this.selected = game.getPlayer().getParty()
								.get(Integer.parseInt(button.getText()) - 1);
						this.remove.repaint();
						this.remove.setVisible(true);
					} else {
						// Person clicked either yes, no, or cancel.
						if (button.getText().equalsIgnoreCase("yes")) {
							game.getPlayer()
									.getParty()
									.remove(game.getPlayer().getParty()
											.indexOf(selected));
						}
						this.remove.setVisible(false);
						this.repaint();
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// make sure the source is a jbutton
		if (e.getSource() instanceof JButton) {
			// cast source to jbutton
			JButton button = (JButton) e.getSource();
			// set colour of button to be green.
			button.setForeground(Color.green);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// make sure the source is a jbutton
		if (e.getSource() instanceof JButton) {
			// cast source to jbutton
			JButton button = (JButton) e.getSource();
			// set colour of button to be white.
			button.setForeground(Color.white);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}
}
