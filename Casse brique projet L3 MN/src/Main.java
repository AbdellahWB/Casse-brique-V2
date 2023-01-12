//le classe "Main" lance le jeu

//Imports
import javax.swing.*;
import java.awt.*;

//Class definition
public class Main extends JFrame implements Constants {
	//Variables
	private static JFrame frame;
	private static fenêtre fenêtre;
	private static Container pane;
	private static Dimension dim;

	//Construire et exécuter le jeu
	public static void main(String[] args) {
		//Définir l'aspect et la convivialité de celui du système d'exploitation
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame = new JFrame("Casse Brique V2");
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		fenêtre = new fenêtre(WINDOW_WIDTH, WINDOW_HEIGHT);

		pane = frame.getContentPane();
		pane.add(fenêtre);

		//Placer le cadre au milieu de l'écran
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		
		//Définit l'icône du programme
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("Casse brique projet L3 MN/src/img/Icon.png"));
		
		frame.setVisible(true);
	}
}
