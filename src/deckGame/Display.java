package deckGame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import enums.Actions;

import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.util.ArrayList;

public class Display {

	private JFrame frmDeckgame;
	private JLabel lblCurrentDay = new JLabel("Current day: 0", JLabel.CENTER);
	private JLabel lblMoney = new JLabel("Gold: 25", JLabel.CENTER);
	private JPanel statsPanel = new JPanel();
	private JTextArea outputArea = new JTextArea("This area will display the system messages. They could be quite long, so this should wrap text. Hence, I'm just going to babble on and on and on until I have enough text to test that. Hopefully I do by now.", 3, 65);
	private JLabel lblHealthStat = new JLabel("Error ");
	private JLabel lblSpeedStat = new JLabel("Error ");
	private JLabel lblLuckStat = new JLabel("Error ");
	private JLabel lblWeaknessStat = new JLabel("Error ");
	private JLabel lblCapacityStat = new JLabel("Error ");
	private JTextArea lblResistances = new JTextArea("E \nr \nr \no \nr ");
	private ImagePanel displayPanel = new ImagePanel(new ImageIcon("./src/resources/Images/sea-background.png").getImage());
	private JPanel mainDisplayPanel = new JPanel();
	private ArrayList<ChangingButton> mainDisplays = new ArrayList<ChangingButton>();
	private Game game;
	private JPanel dialogPanel = new JPanel();
	private boolean logOpen = false;
	private String currentState = "Island";

	/**
	 * Create the application.
	 */
	public Display(Game game) {
		initialize(game);
		this.frmDeckgame.setVisible(true);
	}
	
	/**
	 * Updates the amount of gold displayed
	 * @param amount the amount of gold to show
	 */
	public void updateGold(String amount) {
		this.lblMoney.setText("Gold: " + amount);
	}
	
	/**
	 * Updates the day shown
	 * @param day the current day to display
	 */
	public void updateDay(String day) {
		this.lblCurrentDay.setText("Current Day: " + day);
	}
	
	/**
	 * Updates the system dialogue being shown
	 * @param message the string to display
	 */
	public void updateDialogue(String message) {
		this.outputArea.setText(message);
	}
	
	public void changeBackground(String source) {
		this.displayPanel.setImage(new ImageIcon(source).getImage());
		this.dialogPanel.repaint();
	}
	
	/**
	 * Updates the health stat that is displayed
	 * @param health the new health to display
	 */
	public void updateHealth(String health) {
		this.lblHealthStat.setText(health);
	}
	
	/**
	 * Updates the speed stat that is displayed
	 * @param speed the new speed to display
	 */
	public void updateSpeed(String speed) {
		this.lblSpeedStat.setText(speed);
	}
	
	/**
	 * Updates the luck stat that is displayed
	 * @param luck the new luck to display
	 */
	public void updateLuck(String luck) {
		this.lblLuckStat.setText(luck);
	}
	
	/**
	 * Updates the weakness stat that is displayed
	 * @param weakness the new weakness to display
	 */
	public void updateWeakness(String weakness) {
		this.lblWeaknessStat.setText(weakness);
	}
	
	/**
	 * Updates the capacity stat that is displayed
	 * @param capacity the new capacity to display
	 */
	public void updateCapacity(String capacity) {
		this.lblCapacityStat.setText(capacity);
	}
	
	/**
	 * Updates the graphical display of all stats
	 * @param health the new amount of health to display
	 * @param speed the new speed to display
	 * @param luck the new luck to display
	 * @param weakness the new weakness to display
	 * @param capacity the new capacity to display
	 */
	public void updateStats(String health, String speed, String luck, String weakness, String capacity) {
		updateHealth(health);
		updateSpeed(speed);
		updateLuck(luck);
		updateWeakness(weakness);
		updateCapacity(capacity);
	}
	
	public void updateMainDisplay(int index, String input, boolean enabled, boolean show) {
		if(input.length() > 0 && input.substring(0,1).equals(".")) {
			ImageIcon img = new ImageIcon(input);
			this.mainDisplays.get(index).setIcon(img);
		} else {
			this.mainDisplays.get(index).setText(input);
		}
		this.mainDisplays.get(index).setEnabled(enabled);
		this.mainDisplays.get(index).setVisible(show);
	}
	
	public void updateDisplayFunction(int index, Actions action) {
		this.mainDisplays.get(index).setAction(action);
	}
	
	public void setGameState(String s) {
		this.currentState = s;
		for (int i = 0; i < 15; i++) {
			updateMainDisplay(i, "", false, false);
		}
		switch(s) {
		case "Island":
			changeBackground("./src/resources/Images/IslandBackground.png");
			break;
		case "Sea":
			changeBackground("./src/resources/Images/ShopBackground.png");
			ArrayList<Island> islands = new ArrayList<Island>();
			islands.addAll(game.getIslands());
			for(Island island : islands) {
				updateMainDisplay(island.getDisplayLocation(), "./src/resources/Images/Island_2.png", true, true);
			}
			break;
		case "Store":
			//openStore();
			break;
		}
	}
	
	/**
	 * Fills the stats panel of the ship with labels.
	 */
	private void fillStatsPanel() {
		JLabel lblPanelTitle = new JLabel(" Ship Stats ");
		lblPanelTitle.setFont(new Font("Lucida Handwriting", Font.PLAIN, 16));
		statsPanel.add(lblPanelTitle, "cell 2 0 2 1, alignx center, aligny center");
		
		JLabel lblStatsDivider = new JLabel();
		lblStatsDivider.setIcon(new ImageIcon(new ImageIcon("./src/resources/Images/parchment-divider-horizontal.png").getImage().getScaledInstance(90, 15, Image.SCALE_DEFAULT)));
		statsPanel.add(lblStatsDivider, "cell 2 1 2 1,alignx center,aligny center");
		
		JLabel lblAnnounceHealth = new JLabel("Health:");
		lblAnnounceHealth.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblAnnounceHealth, "cell 0 2 2 1, alignx left, aligny center");
		
		JLabel lblAnnounceSpeed = new JLabel("Speed:");
		lblAnnounceSpeed.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblAnnounceSpeed, "cell 0 3 2 1, alignx left, aligny center");
		
		JLabel lblAnnounceLuck = new JLabel("Luck: ");
		lblAnnounceLuck.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblAnnounceLuck, "cell 0 4 2 1, alignx left, aligny center");
		
		JLabel lblAnnounceWeakness = new JLabel("Weakness:");
		lblAnnounceWeakness.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblAnnounceWeakness, "cell 0 5 2 1, alignx left, aligny center");
		
		JLabel lblAnnounceCapacity = new JLabel("Capacity:");
		lblAnnounceCapacity.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblAnnounceCapacity, "cell 0 6 2 1,alignx left,aligny center");
		
		JLabel lblAnnounceResistances = new JLabel("Resistances:");
		lblAnnounceResistances.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblAnnounceResistances, "cell 0 7 2 1, alignx left, aligny top");
		
		lblHealthStat.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblHealthStat, "cell 5 2, alignx right, aligny center");
		
		lblSpeedStat.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblSpeedStat, "cell 5 3, alignx right, aligny center");
		
		lblLuckStat.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblLuckStat, "cell 5 4, alignx right, aligny center");
		
		lblWeaknessStat.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblWeaknessStat, "cell 5 5, alignx right, aligny center");
		
		lblCapacityStat.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblCapacityStat, "cell 5 6, alignx right, aligny center");
		
		lblResistances.setOpaque(false);
		lblResistances.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblResistances, "cell 5 7, alignx right, aligny top");
	}
	
	/**
	 * Creates the dialogue panel at the bottom of the screen
	 */
	private void createDialogPanel() {
		dialogPanel.setBounds(0, 700, 1540, 122);
		dialogPanel.setOpaque(false);
		dialogPanel.setBorder(BorderFactory.createMatteBorder(15, 0, 0, 0, new ImageIcon("./src/resources/Images/parchment-top.png")));
		frmDeckgame.getContentPane().add(dialogPanel);
		dialogPanel.setLayout(null);
		
		JPanel goldPanel = new JPanel();
		goldPanel.setBackground(Color.decode("#F0DD8D"));
		goldPanel.setBounds(0, 15, 200, 107);
		dialogPanel.add(goldPanel);
		goldPanel.setLayout(new MigLayout("", "[151px]", "[26px][26px][]"));
		
		lblCurrentDay.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
		goldPanel.add(lblCurrentDay, "cell 0 0,alignx left,aligny center");
		
		lblMoney.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
		goldPanel.add(lblMoney, "cell 0 1,alignx left,aligny center");
		
		JPanel outputPanel = new JPanel();
		outputPanel.setBackground(Color.decode("#F0DD8D"));
		outputPanel.setBounds(200, 15, 1340, 107);
		outputPanel.setBorder(BorderFactory.createMatteBorder(0, 15, 0, 0, new ImageIcon("./src/resources/Images/parchment-divider-vertical.png")));
		dialogPanel.add(outputPanel);
			
		outputArea.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
		outputArea.setBackground(Color.decode("#F0DD8D"));
		outputArea.setLineWrap(true);
		outputArea.setWrapStyleWord(true);
		outputPanel.add(outputArea);
	}
	
	private void openLog() {
		this.logOpen = true;
		showLog(0);
	}
	
	private void showLog(int index) {
		for (int i = 0; i < 15; i++) {
			updateMainDisplay(i, "", false, false);
		}
		ArrayList<Item> items = new ArrayList<Item>();
		items.addAll(game.getLogItems());
		for (int reference = 0; reference < 10; reference++) {
			if(index + reference < items.size()) {
				updateMainDisplay(reference, items.get(reference).toString(), true, true);
			}
			reference++;
		}
		if (index == 0) {
			updateMainDisplay(11, "Previous Page", false, true);
		} else {
			updateMainDisplay(11, "Previous Page", true, true);
		}
		if (items.size() > index + 10) {
			updateMainDisplay(13, "Next Page", true, true);
		} else {
			updateMainDisplay(13, "Next Page", false, true);
		}
	}
	
	private void closeLog() {
		this.logOpen = false;
		setGameState(currentState);
	}
	
	/**
	 * Fills in the main display area of the game
	 */
	private void fillMainDisplay() {
		for(int i=0; i < 15; i++) {
			ChangingButton temp_button = new ChangingButton("", this.game);
			mainDisplayPanel.add(temp_button);
			mainDisplays.add(temp_button);
		}
		
		for(ChangingButton display : mainDisplays) {
			display.setFocusPainted(false);
			display.setMargin(new Insets(0, 0, 0, 0));
			display.setContentAreaFilled(false);
			display.setBorderPainted(false);
			display.setOpaque(false);
			display.setBorder(new EmptyBorder(0,0,0,0));
			display.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			display.setEnabled(false);
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Game game) {
		this.game = game;
		frmDeckgame = new JFrame();
		frmDeckgame.setTitle("Deckgame");
		frmDeckgame.setBounds(50, 50, 2000, 2000);
		frmDeckgame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDeckgame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frmDeckgame.setUndecorated(false);
		frmDeckgame.getContentPane().setLayout(null);
		
		createDialogPanel();
		
		displayPanel.setBounds(0, 0, 1540, 715);
		displayPanel.setBackground(Color.decode("#A2E2F2"));
		frmDeckgame.getContentPane().add(displayPanel);
		
		mainDisplayPanel.setOpaque(false);
		
		JPanel sideBarPanel = new JPanel();
		sideBarPanel.setOpaque(false);
		
		GroupLayout gl_displayPanel = new GroupLayout(displayPanel);
		gl_displayPanel.setHorizontalGroup(
			gl_displayPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_displayPanel.createSequentialGroup()
					.addComponent(mainDisplayPanel, GroupLayout.PREFERRED_SIZE, 1236, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sideBarPanel, GroupLayout.PREFERRED_SIZE, 297, GroupLayout.PREFERRED_SIZE)
					.addGap(1))
		);
		gl_displayPanel.setVerticalGroup(
			gl_displayPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_displayPanel.createSequentialGroup()
					.addGroup(gl_displayPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(sideBarPanel, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(mainDisplayPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		mainDisplayPanel.setLayout(new GridLayout(3, 5, 0, 0));
		
		fillMainDisplay();
		
		displayPanel.setLayout(gl_displayPanel);
		
		JButton openLog = new JButton(new ImageIcon("./src/resources/Images/captains_log.png"));
		openLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				if (!logOpen) {
					openLog();
				} else {
					closeLog();
				}
			} 
		});
		openLog.setFocusPainted(false);
		openLog.setMargin(new Insets(0, 0, 0, 0));
		openLog.setContentAreaFilled(false);
		openLog.setBorderPainted(false);
		openLog.setOpaque(false);
		openLog.setBorder(new EmptyBorder(0,0,0,0));
		openLog.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		statsPanel.setBackground(Color.decode("#F0DD8D"));
		statsPanel.setBounds(0, 0, 300, 300);
		statsPanel.setLayout(new MigLayout("", "[45px][][45px][45px][][45px,grow]", "[13px][][][][][][][grow][][][][]"));
		GroupLayout gl_sideBarPanel = new GroupLayout(sideBarPanel);
		gl_sideBarPanel.setHorizontalGroup(
			gl_sideBarPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sideBarPanel.createSequentialGroup()
					.addGroup(gl_sideBarPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(statsPanel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addComponent(openLog, GroupLayout.PREFERRED_SIZE, 297, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_sideBarPanel.setVerticalGroup(
			gl_sideBarPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sideBarPanel.createSequentialGroup()
					.addGroup(gl_sideBarPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(statsPanel, GroupLayout.PREFERRED_SIZE, 479, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_sideBarPanel.createSequentialGroup()
							.addGap(480)
							.addComponent(openLog, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		sideBarPanel.setLayout(gl_sideBarPanel);
		fillStatsPanel();
		
		frmDeckgame.setVisible(true);
	}
}
