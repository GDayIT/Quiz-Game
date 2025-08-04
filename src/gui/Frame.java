package gui;


import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import dbbl.DataBusiness;
import gui.FormingPanel;


public class Frame extends JFrame {

	/**
	 * Creates a frame based on the project appearance
	 * 
	 * @throws HeadlessException
	 */
	public Frame() throws HeadlessException {
		super("ProtoQUIZZ");

//		setBounds(500, 800, 400, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		DataBusiness db = new DataBusiness();
		add(new FormingPanel("QUIZZ!", db));
		setVisible(true);
	}

	public static void main(String[] args) {
		try {
            // Set Windows Look & Feel (falls verfügbar)
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("Windows Look and Feel konnte nicht gesetzt werden: " + e.getMessage());
        }

		// Danach erst das Fenster erstellen
		new Frame();
		}
}
