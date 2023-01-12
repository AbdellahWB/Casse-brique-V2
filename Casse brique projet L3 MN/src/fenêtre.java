//Imports

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

//Classe definition
public class fenêtre extends JPanel implements Runnable, Constants {
	//Items on-screen
	private Raquette raquette;
	private Balle balle;
	private Brique[][] brique = new Brique[10][5];

	//Valeurs initiales pour certaines variables importantes
	private int score = 0, lives = MAX_LIVES, bricksLeft = MAX_BRICKS, waitTime = 3, xSpeed, withSound, level = 1;

	//nom du joueur
	private String playerName;

	//le jeu
	private Thread game;

	//on peut ajouter une musique de fond si on le souhaite il faute juste cree un ficher wav et mettre les pistes audio
	private String songOne = "Casse brique projet L3 MN/src/wav/One.wav";
	private String songTwo = "Casse brique projet L3 MN/src/wav/One.wav";
	private String songThree = "Casse brique projet L3 MN/src/wav/One.wav";
	private String songFour = "Casse brique projet L3 MN/src/wav/Four.wav";
	private String songFive = "Casse brique projet L3 MN/src/wav/Five.wav";
	private String songSix = "Casse brique projet L3 MN/src/wav/Six.wav";
	private String songSeven = "Casse brique projet L3 MN/src/wav/Seven.wav";
	private String songEight = "Casse brique projet L3 MN/src/wav/Eight.wav";
	private String songNine = "Casse brique projet L3 MN/src/wav/Nine.wav";
	private String songTen = "Casse brique projet L3 MN/src/wav/Ten.wav";
	private String[] trackList = {songOne, songTwo, songThree, songFour, songFive, songSix, songSeven, songEight, songNine, songTen};
	private AudioInputStream audio;
	private Clip clip;

	// Structures de données pour gérer les scores élevés

	private ArrayList<Objet> objets = new ArrayList<Objet>();
	private AtomicBoolean isPaused = new AtomicBoolean(true);

	//couleur des donnes
	private Color[] blueColors = {BLUE_BRICK_ONE, BLUE_BRICK_TWO, BLUE_BRICK_THREE, Color.BLACK};
	private Color[] redColors = {RED_BRICK_ONE, RED_BRICK_TWO, RED_BRICK_THREE, Color.BLACK};
	private Color[] purpleColors = {PURPLE_BRICK_ONE, PURPLE_BRICK_TWO, PURPLE_BRICK_THREE, Color.BLACK};
	private Color[] yellowColors = {YELLOW_BRICK_ONE, YELLOW_BRICK_TWO, YELLOW_BRICK_THREE, Color.BLACK};
	private Color[] pinkColors = {PINK_BRICK_ONE, PINK_BRICK_TWO, PINK_BRICK_THREE, Color.BLACK};
	private Color[] grayColors = {GRAY_BRICK_ONE, GRAY_BRICK_TWO, GRAY_BRICK_THREE, Color.BLACK};
	private Color[] greenColors = {GREEN_BRICK_ONE, GREEN_BRICK_TWO, GREEN_BRICK_THREE, Color.BLACK};
	private Color[][] colors = {blueColors, redColors, purpleColors, yellowColors, pinkColors, grayColors, greenColors};

	//Constructor
	public fenêtre(int width, int height) {
		super.setSize(width, height);
		addKeyListener(new BoardListener());
		setFocusable(true);

		makeBricks();
		raquette = new Raquette(PADDLE_X_START, PADDLE_Y_START, PADDLE_WIDTH, PADDLE_HEIGHT, Color.BLACK);
		balle = new Balle(BALL_X_START, BALL_Y_START, BALL_WIDTH, BALL_HEIGHT, Color.BLACK);

		//get le nom du joueur
		playerName = JOptionPane.showInputDialog(null, "entrer votre nom :", "casse-brique v2", JOptionPane.QUESTION_MESSAGE);
		if (playerName == null) {
			System.exit(0);
		}
		if (playerName.toUpperCase().equals("TY") || playerName.toUpperCase().equals("TYKELLEY") || playerName.toUpperCase().equals("TYLUCAS") || playerName.toUpperCase().equals("TYLUCASKELLEY") || playerName.toUpperCase().equals("TY-LUCAS") || playerName.toUpperCase().equals("TY-LUCAS KELLEY") || playerName.toUpperCase().equals("TY KELLEY")) {
			score += 1000;
			JOptionPane.showMessageDialog(null, " 1,000 point bonus! joli nom ", "1,000 Points", JOptionPane.INFORMATION_MESSAGE);
		}

		//Écran de démarrage qui affiche des informations et demande si l'utilisateur veut de la musique ou non, stocke ce choix
		String[] options = {"Oui", "Non"};
		withSound = JOptionPane.showOptionDialog(null, "Casse Brique version 2 \nL3 MN \nAbdellah WAHBI \n\nControls\n    Espace: lancer une game, Pause/Resume en jeu.\n    gauche/droite fleche touche: bouger la raquette\nItems\n    objet vert: expand la raquette\n    objet rouge: retreci la raquette\nScoring\n    Block: 50 points\n    Level-up: 100 points\n\n\n PS: Espace: lancer une game \n\n   vous voulez la musique de fond? ", "A propos du jeu ", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		playMusic(trackList, withSound, level);

		game = new Thread(this);
		game.start();
		stop();
		isPaused.set(true);
	}


	//remplit le tableau de briques
	public void makeBricks() {
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 5; j++) {
				Random rand = new Random();
				int itemType = rand.nextInt(3) + 1;
				int numLives = 3;
				Color color = colors[rand.nextInt(7)][0];
				brique[i][j] = new Brique((i * BRICK_WIDTH), ((j * BRICK_HEIGHT) + (BRICK_HEIGHT / 2)), BRICK_WIDTH - 5, BRICK_HEIGHT - 5, color, numLives, itemType);
			}
		}
	}

	//commence le fil
	public void start() {
		game.resume();
		isPaused.set(false);
	}

	//arrête le fil
	public void stop() {
		game.suspend();
	}

	//ends the thread
	public void destroy() {
		game.resume();
		isPaused.set(false);
		game.stop();
		isPaused.set(true);
	}

	//lance le jeu
	public void run() {
		xSpeed = 1;
		while(true) {
			int x1 = balle.getX();
			int y1 = balle.getY();

			//S'assure que la vitesse ne devient pas trop rapide/lente
			if (Math.abs(xSpeed) > 1) {
				if (xSpeed > 1) {
					xSpeed--;
				}
				if (xSpeed < 1) {
					xSpeed++;
				}
			}

			checkPaddle(x1, y1);
			checkWall(x1, y1);
			checkBricks(x1, y1);
			checkLives();
			checkIfOut(y1);
			balle.move();
			dropItems();
			checkItemList();
			repaint();

			try {
				game.sleep(waitTime);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}

	public void addItem(Objet i) {
		objets.add(i);
	}

	public void dropItems() {
		for (int i = 0; i < objets.size(); i++) {
			Objet tempObjet = objets.get(i);
			tempObjet.drop();
			objets.set(i, tempObjet);
		}
	}

	public void checkItemList() {
		for (int i = 0; i < objets.size(); i++) {
			Objet tempObjet = objets.get(i);
			if (raquette.caughtItem(tempObjet)) {
				objets.remove(i);
			}
			else if (tempObjet.getY() > WINDOW_HEIGHT) {
				objets.remove(i);
			}
		}
	}

	public void checkLives() {
		if (bricksLeft == NO_BRICKS) {
			try {
				clip.stop();
				clip.close();
				audio.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			balle.reset();
			bricksLeft = MAX_BRICKS;
			makeBricks();
			lives++;
			level++;
			score += 100;
			playMusic(trackList, withSound, level);
			repaint();
			stop();
			isPaused.set(true);
		}
		if (lives == MIN_LIVES) {
			repaint();
			stop();
			isPaused.set(true);
		}
	}

	public void checkPaddle(int x1, int y1) {
		if (raquette.hitPaddle(x1, y1) && balle.getXDir() < 0) {
			balle.setYDir(-1);
			xSpeed = -1;
			balle.setXDir(xSpeed);
		}
		if (raquette.hitPaddle(x1, y1) && balle.getXDir() > 0) {
			balle.setYDir(-1);
			xSpeed = 1;
			balle.setXDir(xSpeed);
		}

		if (raquette.getX() <= 0) {
			raquette.setX(0);
		}
		if (raquette.getX() + raquette.getWidth() >= getWidth()) {
			raquette.setX(getWidth() - raquette.getWidth());
		}
	}

	public void checkWall(int x1, int y1) {
		if (x1 >= getWidth() - balle.getWidth()) {
			xSpeed = -Math.abs(xSpeed);
			balle.setXDir(xSpeed);
		}
		if (x1 <= 0) {
			xSpeed = Math.abs(xSpeed);
			balle.setXDir(xSpeed);
		}
		if (y1 <= 0) {
			balle.setYDir(1);
		}
		if (y1 >= getHeight()) {
			balle.setYDir(-1);
		}
	}

	public void checkBricks(int x1, int y1) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				if (brique[i][j].hitBottom(x1, y1)) {
					balle.setYDir(1);
					if (brique[i][j].isDestroyed()) {
						bricksLeft--;
						score += 50;
						addItem(brique[i][j].objet);
					}
				}
				if (brique[i][j].hitLeft(x1, y1)) {
					xSpeed = -xSpeed;
					balle.setXDir(xSpeed);
					if (brique[i][j].isDestroyed()) {
						bricksLeft--;
						score += 50;
						addItem(brique[i][j].objet);
					}
				}
				if (brique[i][j].hitRight(x1, y1)) {
					xSpeed = -xSpeed;
					balle.setXDir(xSpeed);
					if (brique[i][j].isDestroyed()) {
						bricksLeft--;
						score += 50;
						addItem(brique[i][j].objet);
					}
				}
				if (brique[i][j].hitTop(x1, y1)) {
					balle.setYDir(-1);
					if (brique[i][j].isDestroyed()) {
						bricksLeft--;
						score += 50;
						addItem(brique[i][j].objet);
					}
				}
			}
		}
	}

	public void checkIfOut(int y1) {
		if (y1 > PADDLE_Y_START + 10) {
			lives--;
			score -= 100;
			balle.reset();
			repaint();
			stop();
			isPaused.set(true);
		}
	}

	//joue une musique différente tout au long du jeu si l'utilisateur le souhaite
	public void playMusic(String[] songs, int yesNo, int level) {
		if (yesNo == 1) {
			return;
		}
		else if (yesNo == -1) {
			System.exit(0);
		}
		if (level == 10) {
			level = 1;
		}
		try {
			audio = AudioSystem.getAudioInputStream(new File(songs[level-1]).getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audio);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//remplit le tableau
	@Override
	public void paintComponent(Graphics g) {
		Toolkit.getDefaultToolkit().sync();
		super.paintComponent(g);
		raquette.draw(g);
		balle.draw(g);

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				brique[i][j].draw(g);
			}
		}
		g.setColor(Color.BLACK);
		g.drawString("Lives: " + lives, 10, getHeight() - (getHeight()/10));
		g.drawString("Score: " + score, 10, getHeight() - (2*(getHeight()/10)) + 25);
		g.drawString("Level: " + level, 10, getHeight() - (3*(getHeight()/10)) + 50);
		g.drawString("Player: " + playerName, 10, getHeight() - (4*(getHeight()/10)) + 75);

		for (Objet i: objets) {
			i.draw(g);
		}

		if (lives == MIN_LIVES) {
			g.setColor(Color.BLACK);
			g.fillRect(0,0,getWidth(),getHeight());
			g.setColor(Color.WHITE);
			g.drawString("Name: " + playerName + ", Score: " + score + ", Level: " + level, getWidth()/5, 20);
			g.drawString("Jeu terminé! Avez-vous atteint le tableau des meilleurs scores ?", getWidth()/5, 50);
			try {
				printScores(g);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			g.drawString("Appuyez deux fois sur la barre d'espace pour rejouer.\n", getWidth()/5, getHeight()-20);
		}
	}

	//S'assure que le fichier HighScores.txt existe
	public void makeTable() throws IOException {
		String filename = "HighScores";
		File f = new File(filename + ".txt");
		if (f.createNewFile()) {
			try {
				writeFakeScores();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		else {
			//fait rien
		}
	}

	//s'il n'y avait pas de tableau des meilleurs scores précédent, celui-ci entre 10 faux joueurs et marque pour le remplir
	public void writeFakeScores() throws IOException {
		Random rand = new Random();

		int numLines = 10;
		File f = new File("HighScores.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f.getAbsoluteFile()));
		for (int i = 1; i <= numLines; i++) {
			int score = rand.nextInt(2000);
			if (numLines - i >= 1) {
				bw.write("Name: " + "Player" + i + ", " + "Score: " + score + "\n");
			}
			else {
				bw.write("Name: " + "Player" + i + ", " + "Score: " + score);
			}
		}
		bw.close();
		try {
			sortTable();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	//Renvoie le nom et le score du joueur correctement formatés
	public String playerInfo() {
		return "Name: " + playerName + ", Score: " + score;
	}

	//returns the number of lines in the high score file
	public int linesInFile(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f.getAbsoluteFile()));
		int lines = 0;
		while (br.readLine() != null) {
			lines++;
		}
		br.close();
		return lines;
	}

	//renvoie le nombre de lignes dans le fichier des meilleurs scores
	public void saveGame() throws IOException {
		File f = new File("HighScores.txt");
		FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.append("\n" + playerInfo());
		bw.close();
	}

	//trie le tableau des meilleurs scores de haut en bas en utilisant des cartes et d'autres choses amusantes
	public void sortTable() throws IOException {
		File f = new File("HighScores.txt");
		File temp = new File("temp.txt");
		TreeMap<Integer, ArrayList<String>> topTen = new TreeMap<Integer, ArrayList<String>>();
		BufferedReader br = new BufferedReader(new FileReader(f.getAbsoluteFile()));
		BufferedWriter bw = new BufferedWriter(new FileWriter(temp.getAbsoluteFile()));


		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.isEmpty()) {
				continue;
			}
				String[] scores = line.split("Score: ");
				Integer score = Integer.valueOf(scores[1]);
				ArrayList<String> players = null;
				
				//assurez-vous que deux joueurs avec le même score sont traités
				if ((players = topTen.get(score)) == null) {
					players = new ArrayList<String>(1);
					players.add(scores[0]);
					topTen.put(Integer.valueOf(scores[1]), players);
				}
				else {
					players.add(scores[0]);
				}

		}

		for (Integer score : topTen.descendingKeySet()) {
			for (String player : topTen.get(score)) {
				try {
					bw.append(player + "Score: " + score + "\n");
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		br.close();
		bw.close();
		try {
			makeNewScoreTable();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	//enregistrer le tableau trié dans le fichier des meilleurs scores
	public void makeNewScoreTable() throws IOException {
		File f = new File("HighScores.txt");
		File g = new File("temp.txt");
		f.delete();
		g.renameTo(f);
	}
	
	//Imprime les 10 meilleurs scores, mais exécute d'abord toutes les autres méthodes liées aux fichiers
	public void printScores(Graphics g) throws IOException {
		try {
			makeTable();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		try {
			saveGame();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		try {
			sortTable();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		int h = 100;
		File fileToRead = new File("HighScores.txt");
		LineNumberReader lnr = new LineNumberReader(new FileReader(fileToRead));
		String line = lnr.readLine();
		while (line != null && lnr.getLineNumber() <= 10) {
			int rank = lnr.getLineNumber();
			g.drawString(rank + ". " + line, getWidth()/5, h);
			h += 15;
			line = lnr.readLine();
		}
		lnr.close();
	}

	//Classe privée qui gère le gameplay et les contrôles
	private class BoardListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent ke) {
			int key = ke.getKeyCode();
			if (key == KeyEvent.VK_SPACE) {
				if (lives > MIN_LIVES) {
					if (isPaused.get() == false) {
						stop();
						isPaused.set(true);
					}
					else {
						start();
					}
				}
				else {
					raquette.setWidth(getWidth()/7);
					lives = MAX_LIVES;
					score = 0;
					bricksLeft = MAX_BRICKS;
					level = 1;
					makeBricks();
					isPaused.set(true);
					for (int i = 0; i < 10; i++) {
						for (int j = 0; j < 5; j++) {
							brique[i][j].setDestroyed(false);
						}
					}
				}
			}
			if (key == KeyEvent.VK_LEFT) {
				raquette.setX(raquette.getX() - 50);
			}
			if (key == KeyEvent.VK_RIGHT) {
				raquette.setX(raquette.getX() + 50);
			}
		}
		@Override
		public void keyReleased(KeyEvent ke) {
			int key = ke.getKeyCode();
			if (key == KeyEvent.VK_LEFT) {
				raquette.setX(raquette.getX());
			}
			if (key == KeyEvent.VK_RIGHT) {
				raquette.setX(raquette.getX());
			}
		}
	}
}
