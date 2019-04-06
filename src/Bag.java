import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.*;
import java.io.IOException;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Bag extends JPanel implements ActionListener {

	private Game game;
	private JList<String> list;
	private static LineBorder selected = new LineBorder(Color.red, 2, true);
	private String pBall, gBall, uBall, mBall;
	private int numP;
	public static String[] balls, desc;
	private int which;

	public Bag(final Game game, String pBall, String gBall, String uBall,
			String mBall) {
		// Set a null layout
		setLayout(null);
		// Set the panel to be visible.
		this.setVisible(true);
		// Create game
		this.game = game;

		if (pBall == null) {
			pBall = "0";
		}
		if (gBall == null) {
			gBall = "0";
		}
		if (uBall == null) {
			uBall = "0";
		}
		if (mBall == null) {
			mBall = "0";
		}

		// create pokeballs
		this.pBall = pBall;
		this.gBall = gBall;
		this.uBall = uBall;
		this.mBall = mBall;

		this.numP = Integer.parseInt(this.pBall);
		int numG = Integer.parseInt(this.gBall);
		int numU = Integer.parseInt(this.uBall);
		int numM = Integer.parseInt(this.mBall);

		balls = new String[4];
		balls[0] = "Pokeball x" + numP;
		balls[1] = "Great Ball x" + numG;
		balls[2] = "Ultra Ball x" + numU;
		balls[3] = "Masterball x" + numM;

		desc = new String[4];
		desc[0] = "Poor catch rate";
		desc[1] = "Decent catch rate";
		desc[2] = "Good catch rate";
		desc[3] = "100% catch rate";

		// Create the font for options.
		Font font = new Font("Monospaced", Font.BOLD, 15);

		// Description Field
		final JTextField text = new JTextField();
		text.setBounds(40, 450, 220, 80);
		text.setEditable(false);
		text.setBackground(Color.WHITE);
		text.setFont(font);
		add(text);

		// create Jlist
		list = new JList<String>(balls);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.setBounds(330, 80, 250, 200);
		list.setFont(new Font("Monospaced", Font.BOLD, 24));
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				text.setText(desc[list.getSelectedIndex()]);
				which = list.getSelectedIndex();
				game.repaint();
			}
		});
		JScrollPane listScroller = new JScrollPane(list);
		add(list);

		// Toss Button
		JButton toss = new JButton("Toss");
		toss.setContentAreaFilled(false);
		toss.setFocusPainted(false);
		toss.setFont(font);
		toss.addActionListener(this);
		toss.setBounds(100, 380, 100, 50);
		add(toss);

		// Close Bag Button
		JButton exit = new JButton("Close Bag");
		exit.setContentAreaFilled(false);
		exit.setBorder(new LineBorder(Color.black, 3, true));
		exit.setFocusPainted(false);
		exit.setFont(font);
		exit.addActionListener(this);
		exit.setBounds(396, 400, 100, 50);

		add(exit);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof JButton) {
			JButton button = (JButton) e.getSource();

			if (button.getText().equalsIgnoreCase("Close Bag")) {
				// Exit options.
				game.setContent(game.getMapPanel());
				game.requestFocus();
				return;
			} else if (button.getText().equalsIgnoreCase("Toss")) {
				if (list.getSelectedIndex() == 0) {
					toss();
				}
			}

		}
	}

	public int getNumP() {
		return this.numP;
	}

	public void setNumP(int num){
		this.numP = num;
	}

	public void toss() {
		int num = Integer
				.parseInt(balls[0].substring(balls[0].indexOf("x") + 1)) - 1;
		if (num >= 0) {
			String reduced = balls[0].substring(0, balls[0].indexOf("x") + 1)
					+ Integer.toString(num);
			balls[0] = reduced;
			game.repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		// Paint the picture
		try {
			BufferedImage border = ImageIO.read(new File("border.png"));
			BufferedImage bag = ImageIO.read(new File("bag.png"));
			BufferedImage one = ImageIO.read(new File("pokeball.png"));
			BufferedImage two = ImageIO.read(new File("greatball.png"));
			BufferedImage three = ImageIO.read(new File("ultraball.png"));
			BufferedImage four = ImageIO.read(new File("masterball.png"));
			g.drawImage(border, 295, 0, 300, 570, null);
			g.drawImage(border, 0, 330, 295, 240, null);
			g.drawImage(bag, -10, 0, 305, 330, null);

			switch (which) {
			case 0:
				g.drawImage(one, 13, 240, 65, 70, null);
				break;
			case 1:
				g.drawImage(two, 13, 240, 65, 70, null);
				break;
			case 2:
				g.drawImage(three, 13, 240, 65, 70, null);
				break;
			case 3:
				g.drawImage(four, 13, 240, 65, 70, null);
				break;
			}

		} catch (IOException e) {
		}
		;
	}
}