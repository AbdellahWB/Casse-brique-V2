//Cette classe "Raquette" étend la classe "Structure". Il est utilisé pour la pagaie du joueur dans le jeu.

//Imports
import java.awt.*;

//Class definition
public class Raquette extends Structure implements Constants {
	//Variables
	private int xSpeed;

	//Constructor
	public Raquette(int x, int y, int width, int height, Color color) {
		super(x, y, width, height, color);
	}

	//dessin de la raquette
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}

	//Position de debut
	public void reset() {
		x = PADDLE_X_START;
		y = PADDLE_Y_START;
	}

	//Check si la balle touche la raquette
	public boolean hitPaddle(int ballX, int ballY) {
		if ((ballX >= x) && (ballX <= x + width) && ((ballY >= y) && (ballY <= y + height))) {
			return true;
		}
		return false;
	}
	public boolean caughtItem(Objet i) {
		if ((i.getX() < x + width) && (i.getX() + i.getWidth() > x) && (y == i.getY() || y == i.getY() - 1)) {
			i.resizePaddle(this);
			return true;
		}
		return false;
	}
}
