//Cette classe "Balle" étend la classe "Structure". C'est pour le ballon utilisé dans le jeu.

//Imports
import java.awt.*;

//Class definition
public class Balle extends Structure implements Constants {
	//Variables
	private boolean onScreen;
	private int xDir = 1, yDir = -1;

	//Constructor
	public Balle(int x, int y, int width, int height, Color color) {
		super(x, y, width, height, color);
		setOnScreen(true);
	}

	//dessiner la bal
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, width, height);
	}

	//deplacer la balle
	public void move() {
		x += xDir;
		y += yDir;
	}

	//Réinitialise la balle à sa position d'origine au centre de l'écran
	public void reset() {
		x = BALL_X_START;
		y = BALL_Y_START;
		xDir = 1;
		yDir = -1;
	}

	//Méthodes mutatrices
	public void setXDir(int xDir) {
		this.xDir = xDir;
	}

	public void setYDir(int yDir) {
		this.yDir = yDir;
	}

	public void setOnScreen(boolean onScreen) {
		this.onScreen = onScreen;
	}

	//Méthodes d'accès
	public int getXDir() {
		return xDir;
	}

	public int getYDir() {
		return yDir;
	}

	public boolean isOnScreen() {
		return onScreen;
	}
}
