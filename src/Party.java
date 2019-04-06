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

	private Game game;
	private boolean choose;
	private Party instance;
	private JPanel remove;
	private Pokemon selected;

	public Party(Game game, boolean choose) {
		this.game = game;
		this.instance = this;
		this.selected = null;
		this.addKeyListener(game.getKeyListener());
		this.choose = choose;
		this.remove = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setFont(new Font("Monospaced", Font.BOLD, 25));
				g.setColor((new Color(138, 182, 252)));
				g.fillRect(0, 0, 320, 200);
				g.setColor(Color.black);
				g.drawRect(0, 0, 319, 199);

				g.setColor(Color.white);
				if (selected != null) {
					g.drawString("Release " + selected.getName().toUpperCase()
							+ "?", 5, 30);
				}

			}
		};
		this.remove.addKeyListener(game.getKeyListener());
		this.remove.setLayout(null);
		this.remove.setVisible(false);
		this.remove.setBounds(142, 159, 320, 200);
		this.add(remove);

		// REMOVE PANEL BUTTONS.

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

		this.add(exit);
	}

	public Pokemon getNextAvailable() {
		for (int i = 0; i < game.getPlayer().getParty().size(); i++) {
			Pokemon pokemon = game.getPlayer().getParty().get(i);
			if (!pokemon.isFainted()) {
				return pokemon;
			}
		}

		return null;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// Draw the background image.
		try {
			BufferedImage background = ImageIO.read(new File("party.png"));
			g.drawImage(background, -5, 0, 600, 600, null);

			g.setFont(new Font("Monospaced", Font.BOLD, 20));

			double row = 0;
			double col = 0;

			// Draw all the pokemon.
			for (int i = 1; i <= game.getPlayer().getParty().size(); i++) {
				Pokemon pokemon = game.getPlayer().getParty().get(i - 1);
				if (i == 1 || i == 2) {
					row = 0;
				} else if (i == 3 || i == 4) {
					row = 1;
				} else {
					row = 2;
				}
				if (i % 2.0 == 0.0 && i != 0) {
					col = 1;
				} else {
					col = 0;
				}
				// Draw the party in the proper spots.
				int xBox = 25 + 285 * (int) col;
				int yBox = 45 + 145 * (int) row;
				g.drawImage(pokemon.getSprites()[0], xBox, yBox, 64, 64, null);
				if (pokemon.isFainted()) {
					// Draw red over the sprite.
					g.setColor(new Color(255, 20, 51, 100));
					g.fillRoundRect(xBox, yBox, 65, 65, 10, 10);
				}
				g.setColor(Color.white);
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
				g.drawString(
						pokemon.getHealth() + "/" + pokemon.getMaxHealth(),
						xBox + 185, yBox + 65);
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
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton button = (JButton) e.getSource();
			button.setForeground(Color.green);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton button = (JButton) e.getSource();
			button.setForeground(Color.white);
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}
}
