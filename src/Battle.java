import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Battle extends JPanel implements KeyListener {

	private Game game;
	private Pokemon opponent;
	private Pokemon playerPokemon;
	private int row;
	private int col;
	private Battle instance;
	private boolean caught;
	private static BufferedImage background;

	public Battle(Game game, Pokemon opponent) {
		// Save the opponent
		this.opponent = opponent;
		// Save the game
		this.game = game;
		// Save an instance of this class
		this.instance = this;
		// Add the key listener to the frame.
		game.addKeyListener(this);
		// Set a null layout for absolute positioning
		this.setLayout(null);
		// Get the players first pokemon.
		this.playerPokemon = game.getParty().getNextAvailable();
		// Set starting position of selector
		this.row = 0;
		this.col = 0;

		this.caught = false;

		Sounds.BATTLE.play(true, false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// Draw the background image
		try {
			if (background == null) {
				background = ImageIO.read(PokemonDawn.getResource("grass-battle.png"));
			}
			g.drawImage(background, 0, 0, 600, 401, null);
		} catch (IOException e) {
		}

		// Create font for options.
		Font font = new Font("Monospaced", Font.BOLD, 25);
		g.setFont(font);
		// TODO paint all the pokemon.
		// Paint the bottom selection box.
		g.setColor(Color.black);
		g.fillRect(0, 400, 600, 200);
		g.setColor(Color.white);
		g.fillRect(10, 410, 565, 145);

		// Draw the options
		g.setColor(Color.black);
		g.drawString("ATTACK", 42, 465);
		g.drawString("CATCH", 188, 465);
		g.drawString("RUN", 60, 515);

		// Draw selected box
		g.setColor(Color.red);
		g.drawRect(27 + 135 * row, 432 + 50 * col, 125, 50);

		// If the pokemon are not null, draw their images.
		if (this.opponent != null && !this.caught) {
			g.drawImage(this.opponent.getSprites()[0], 365, 35, 154, 154, null);
		} else {
			try {
				BufferedImage img = ImageIO
						.read(PokemonDawn.getResource("pokeball-battle.png"));
				g.drawImage(img, 410, 115, 64, 64, null);
			} catch (IOException e) {
			}
		}
		// Draw the users pokemon
		if (this.playerPokemon != null) {
			g.drawImage(this.playerPokemon.getSprites()[1], 60, 245, 154, 154,
					null);
		}

		// Draw pokemon boxes
		Font names = new Font("Monospaced", Font.BOLD, 20);
		g.setFont(names);

		// OPPONENT BOX
		g.setColor(Color.black);
		g.fillRect(100, 40, 200, 70);
		g.setColor(Color.white);
		g.fillRect(102, 42, 196, 66);
		// Draw pokemon's name
		g.setColor(Color.black);
		g.drawString(this.opponent.getName().toUpperCase(), 105, 65);
		g.drawString("Lv" + this.opponent.getLevel(), 235, 65);
		g.setColor(Color.red);
		// TODO draw health based on remaining health.
		g.fillRoundRect(110, 77, 180, 10, 5, 5);
		// Draw the health lost by the pokemon
		g.setColor(new Color(32, 234, 59));
		double healthPercent = (this.opponent.getHealth() * 1.0
				/ this.opponent.getMaxHealth() * 1.0) * 100.0;
		g.fillRoundRect(110, 77, (180 * (int) healthPercent) / 100, 10, 5, 5);
		// ----------------------------------- //
		g.setColor(Color.black);
		g.setFont(new Font("Monospaced", Font.BOLD, 14));
		g.drawString(
				this.opponent.getHealth() + "/" + this.opponent.getMaxHealth(),
				232, 102);
		// -----------------------------------------------------------------------------------------------

		// PLAYER BOX
		g.setFont(names);
		g.setColor(Color.black);
		g.fillRect(310, 250, 200, 70);
		g.setColor(Color.white);
		g.fillRect(312, 252, 196, 66);
		// Draw pokemon's name
		g.setColor(Color.black);
		g.drawString(this.playerPokemon.getName().toUpperCase(), 315, 270);
		g.drawString("Lv" + this.playerPokemon.getLevel(), 445, 270);
		g.setColor(Color.red);
		// TODO draw health based on remaining health.
		g.fillRoundRect(320, 287, 180, 10, 5, 5);
		// Draw the health lost by the pokemon
		g.setColor(new Color(32, 234, 59));
		double playerPercent = (this.playerPokemon.getHealth() * 1.0
				/ this.playerPokemon.getMaxHealth() * 1.0) * 100.0;
		g.fillRoundRect(320, 287, (180 * (int) playerPercent) / 100, 10, 5, 5);
		// ----------------------------------- //
		g.setColor(Color.black);
		g.setFont(new Font("Monospaced", Font.BOLD, 14));
		g.drawString(
				this.playerPokemon.getHealth() + "/"
						+ this.playerPokemon.getMaxHealth(), 442, 313);
		// Draw experience bar.
		g.setColor(new Color(192, 193, 196));
		g.fillRoundRect(310, 325, 200, 10, 0, 0);
		double experiencePercent = (this.playerPokemon.getExp() * 1.0
				/ this.playerPokemon.expToLevel() * 1.0) * 100.0;
		g.setColor(new Color(102, 140, 255));
		g.fillRoundRect(310, 325, (200 * (int) experiencePercent) / 100, 10, 0,
				0);

	}

	public boolean attack(final Pokemon attacker, final Pokemon victim) {

		if (attacker == null || victim == null || game.isDialogRunning()) {
			return false;
		}
		boolean fainted = false;
		double modifier = 1;
		// TODO have super effective etc.
		final double damage = (((2 * attacker.getLevel() / 5 + 2) * attacker
				.getAttack()) / 50 + 2) * modifier;
		String msg = attacker.getName().toUpperCase() + " attacks!";
		final int originalHealth = victim.getHealth();
		final int oldLvl = attacker.getLevel();
		if (damage >= victim.getHealth()) {
			msg += "#" + victim.getName().toUpperCase() + " fainted!";
			victim.setHealth(0);
			fainted = true;
			if (attacker.equals(this.playerPokemon)) {
				msg += "#" + playerPokemon.getName().toUpperCase() + " gained "
						+ victim.getLevel() * 2 + " experience!";
				// Give EXP, check for level up.
				if (attacker.giveExp(victim.getLevel() * 2)) {
					msg += "#" + playerPokemon.getName().toUpperCase()
							+ " leveled up!";
				}
				int cash = PokemonDawn.generateRandom(100, 50);
				msg += "#You found $" + cash + " from battle!";
				game.getPlayer().setMoney(game.getPlayer().getMoney() + cash);
			}
		} else {
			victim.setHealth(victim.getHealth() - (int) damage);
		}
		DialogBox info = new DialogBox(game, msg, game.getGlobalTextSpeed()) {

			@Override
			public void onLine(String line) {
				if (line.contains("gained") && oldLvl < attacker.getLevel()) {
					Sounds.LEVEL_UP.play(false, true);
				}
			}

			@Override
			public void onComplete() {
				if (damage >= originalHealth) {
					if (victim.equals(opponent)) {
						// Give player money
						destroy();
					} else {
						// Player's pokemon died. Check if they have another to
						// send out.
						Pokemon alive = null;
						for (int i = 0; i < game.getPlayer().getParty().size(); i++) {
							Pokemon poke = game.getPlayer().getParty().get(i);
							if (!poke.isFainted()) {
								alive = poke;
							}
						}
						if (alive != null) {
							DialogBox send = new DialogBox(game, "Go "
									+ alive.getName().toUpperCase() + "!",
									game.getGlobalTextSpeed());
							send.show(instance);
							// Send out the next pokemon
							playerPokemon = alive;
						} else {
							// Take away $500 from the player.
							if (game.getPlayer().getMoney() < 500) {
								game.getPlayer().setMoney(0);
							} else {
								game.getPlayer().setMoney(
										game.getPlayer().getMoney() - 500);
							}
							// Player is out of pokemon, send player to house.
							DialogBox dead = new DialogBox(
									game,
									"You are out of usable pokemon!#You blacked out and lost $500!",
									game.getGlobalTextSpeed()) {
								@Override
								public void onComplete() {
									// Send player to their house, heal all
									// pokemon.
									game.getMapPanel().setMap(
											game.getMap("Home"));
									game.getPlayer().setX(210);
									game.getPlayer().setY(300);
									destroy();
									game.getPlayer().healParty();
								}
							};
							dead.show(instance);
						}
					}
				} else {
					// If the pokemon did not faint, have the opponent attack
					// the player.
					if (attacker.equals(playerPokemon)) {
						attack(opponent, playerPokemon);
					}
				}
			}
		};
		info.show(this);

		return fainted;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (game.isPaused()) {
			return;
		}

		if (game.getPlayer().isInDialog()) {
			KeyPressed.pressedAmount++;
			return;
		}

		if (game.isDialogRunning()) {
			return;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// Player selected something...
			if (row == 0) {
				if (col == 0) {
					// Player wants to attack
					boolean fainted = attack(playerPokemon, opponent);
				} else {
					// Player wants to run

					final int chance = PokemonDawn.generateRandom(100, 0);

					String msg = "";

					if (chance >= 80) {
						msg = "Could not escape!";
					} else {
						msg = "Got away safely!";
					}

					DialogBox run = new DialogBox(game, msg,
							game.getGlobalTextSpeed()) {
						@Override
						public void onComplete() {
							if (chance < 80) {
								destroy();
							} else {
								attack(opponent, playerPokemon);
							}
						}
					};
					run.show(instance);

				}
			} else {
				if (col == 0) {
					// Player wants to catch pokemon.
					// If player has a full party, don't let them catch!
					if (game.getPlayer().getParty().size() >= 6) {
						DialogBox full = new DialogBox(game,
								"Sorry, your Pokemon Party is full!",
								game.getGlobalTextSpeed());
						full.show(instance);
						// return;
					} else {

						if (game.getBag().getNumP() > 0) {
							final int chance = PokemonDawn.generateRandom(100,
									0);
							String msg = "You throw a pokeball!";
							if (chance >= 70) {
								Sounds.CATCH.play(false, true);
								msg += "#"
										+ this.opponent.getName().toUpperCase()
										+ " was caught!";
								game.getPlayer().getParty().add(this.opponent);
							} else {
								msg += "#"
										+ this.opponent.getName().toUpperCase()
										+ " broke free!";
							}

							caught = true;
							instance.repaint();

							DialogBox box = new DialogBox(game, msg,
									game.getGlobalTextSpeed()) {

								@Override
								public void onLine(String line) {
									if (line.contains("You throw a pokeball")
											&& chance < 70) {
										caught = false;
										instance.repaint();
									}
								}

								@Override
								public void onComplete() {
									if (chance < 70) {
										attack(opponent, playerPokemon);
									} else {
										destroy();
									}
								}
							};

							box.show(instance);

							game.getBag().toss();
						} else {
							DialogBox out = new DialogBox(game,
									"Sorry, you have no more pokeballs!",
									game.getGlobalTextSpeed());
							out.show(instance);
							// return;
						}
					}

				}
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (col == 1) {
				col = 0;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (col == 0 && row == 0) {
				col = 1;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (row == 1) {
				row = 0;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (row == 0 && col == 0) {
				row = 1;
			}
		}

		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void destroy() {
		// TODO check if player lost.
		game.removeKeyListener(this);
		game.setContent(game.getMapPanel());
		game.getPlayer().setCanMove(true);
		game.requestFocus();
		Sounds.GAME_MUSIC.play(true, false);
	}

}
