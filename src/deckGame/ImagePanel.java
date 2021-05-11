package deckGame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;
/**
 * This class sourced from http://www.java2s.com/Tutorials/Java/Swing_How_to/Basic/Add_Background_image_to_JPanel.htm
 * @author 17geo
 *
 */
public class ImagePanel extends JPanel {
	private Image background;
	
	public ImagePanel(Image img) {
		this.background = img;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    g.drawImage(background, 0, 0, null);
	}
}
