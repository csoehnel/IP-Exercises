package de.soehnel.christoph;

public class TestDriver {

	public static void main(String[] args) {
		// Desktop erzeugen
		ImageProcessingGUI ipgui = new ImageProcessingGUI();
		ipgui.setLocation(100, 100);
		ipgui.setSize(800, 600);
		ipgui.setVisible(true);
	}
}