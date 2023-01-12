import java.awt.*;

//Définition de la classe
public class Structure implements Constants {
	//Variables
	protected int x, y, width, height;
	protected Color color;

	//Constructeur
	public Structure(int x, int y, int width, int height, Color color) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setColor(color);
	}

	//Dessiner une structure
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}

	//Méthodes mutatrices
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	// Méthodes d'accès
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Color getColor() {
		return color;
	}
}
