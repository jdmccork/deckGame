package deckGame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;

public class Display {

	private JFrame frmDeckgame;
	private JLabel lblCurrentDay = new JLabel("Current day: 0", JLabel.CENTER);
	private JLabel lblMoney = new JLabel("Gold: 25", JLabel.CENTER);
	private JTextArea outputArea = new JTextArea("This area will display the system messages. THey could be quite long, so this should wrap text. Hence, I'm just going to babbnle on and on and on until I have enough text to test that. Hopefully I do by now.", 3, 65);


	/**
	 * Create the application.
	 */
	public Display() {
		initialize();
		this.frmDeckgame.setVisible(true);
	}
	
	public void updateGold(String amount) {
		this.lblMoney.setText("Gold: " + amount);
	}
	
	public void updateDay(String day) {
		this.lblCurrentDay.setText("Current Day: " + day);
	}
	
	public void updateDialogue(String message) {
		this.outputArea.setText(message);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDeckgame = new JFrame();
		frmDeckgame.setTitle("Deckgame");
		frmDeckgame.setBounds(50, 50, 2000, 2000);
		frmDeckgame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDeckgame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frmDeckgame.setUndecorated(false);
		frmDeckgame.getContentPane().setLayout(null);
		
		JPanel displayPanel = new JPanel();
		displayPanel.setBounds(0, 0, 1540, 700);
		displayPanel.setBackground(Color.WHITE);
		frmDeckgame.getContentPane().add(displayPanel);
		displayPanel.setLayout(null);
		
		JPanel dialogPanel = new JPanel();
		dialogPanel.setBounds(0, 700, 1540, 107);
		frmDeckgame.getContentPane().add(dialogPanel);
		dialogPanel.setLayout(null);
		
		JPanel goldPanel = new JPanel();
		goldPanel.setBackground(Color.decode("#F0DD8D"));
		goldPanel.setBounds(0, 53, 200, 53);
		dialogPanel.add(goldPanel);
		goldPanel.setLayout(new BorderLayout(0, 0));
		
		lblMoney.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
		goldPanel.add(lblMoney);
		
		JPanel outputPanel = new JPanel();
		outputPanel.setBackground(Color.decode("#F0DD8D"));
		outputPanel.setBounds(200, 0, 1340, 107);
		Border compound;
		Border leftside = BorderFactory.createMatteBorder(15, 0, 0, 0, new ImageIcon("./src/resources/Images/parchment-top.png"));
		compound = BorderFactory.createMatteBorder(0, 15, 0, 0, new ImageIcon("./src/resources/Images/parchment-left.png"));
		compound = BorderFactory.createCompoundBorder(leftside, compound);
		outputPanel.setBorder(compound);
		dialogPanel.add(outputPanel);
			
		outputArea.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
		outputArea.setBackground(Color.decode("#F0DD8D"));
		outputArea.setLineWrap(true);
		outputArea.setWrapStyleWord(true);
		outputPanel.add(outputArea);
		
		JPanel dayPanel = new JPanel();
		dayPanel.setBackground(Color.decode("#F0DD8D"));
		dayPanel.setBounds(0, 0, 200, 53);
		dayPanel.setBorder(BorderFactory.createMatteBorder(15, 0, 0, 0, new ImageIcon("./src/resources/Images/parchment-top.png")));
		dialogPanel.add(dayPanel);
		dayPanel.setLayout(new BorderLayout(0, 0));
		
		lblCurrentDay.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
		dayPanel.add(lblCurrentDay);
		
		frmDeckgame.setVisible(true);
	}
}
