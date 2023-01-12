//Cette classe "Objet" étend la classe "Structure". Il est utilisé pour les objets qui tombent de certaines briques.

//Imports
import java.awt.*;

//Class definition
public class Objet extends Structure implements Constants {
	//Variables
	private int type;

	//Constructor
	public Objet(int x, int y, int width, int height, Color color, int type) {
		super(x, y, width, height, color);
		setType(type);
	}

	//Draw an objet
	public void draw(Graphics g) {
		if(type == 3) {
			return;
		}
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}

	//Drop the objet down towards the paddle at slow pace
	public void drop() {
		y += 1;
	}

	//Resize the paddle, depending on which objet is caught. Changes in increments of 15 until min/max width is reached.
	public void resizePaddle(Raquette p) {
		if (getType() == 1 && p.getWidth() < PADDLE_MAX) {
			p.setWidth(p.getWidth() + 15);
		}
		else if (getType() == 2 && p.getWidth() > PADDLE_MIN) {
			p.setWidth(p.getWidth() - 15);
		}
	}

	//Set the objet's type
	public void setType(int type) {
		this.type = type;
	}

	//Get the objet's type
	public int getType() {
		return type;
	}
}
