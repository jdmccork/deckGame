package deckGame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import enums.Actions;
import enums.ItemType;

import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.JRadioButton;

public class Display {

	private JFrame frmDeckgame;
	private JLabel lblCurrentDay;
	private JLabel lblMoney;
	private JPanel statsPanel;
	private JTextArea outputArea;
	private JLabel lblHealthStat;
	private JLabel lblSpeedStat;
	private JLabel lblLuckStat;
	private JLabel lblCapacityStat;
	private ImagePanel displayPanel;
	private JPanel mainDisplayPanel;
	private ArrayList<ChangingButton> mainDisplays;
	private Game game;
	private JPanel dialogPanel;
	private boolean logOpen;
	private String currentState;
	private int currentLogPage;
	private int currentPage;
	private JButton openLog;

	/**
	 * Create the application.
	 */
	public Display(Game game) {
		welcome(game);
		//initialize();
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
	
	private void changeForegroundColour(Color color) {
		for (ChangingButton button : this.mainDisplays) {
			button.setForeground(color);
		}
	}
	
	/**
	 * Updates the luck stat that is displayed
	 * @param luck the new luck to display
	 */
	public void updateLuck(String luck) {
		this.lblLuckStat.setText(luck);
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
		updateCapacity(capacity);
	}
	
	public void updateMainDisplay(int index, String input, boolean enabled, boolean show) {
		if(input.length() == 0) {
			this.mainDisplays.get(index).setIcon(null);
			this.mainDisplays.get(index).setText(null);
		} else if(input.substring(0,1).equals(".")) {
			ImageIcon img = new ImageIcon(input);
			this.mainDisplays.get(index).setIcon(img);
		} else {
			this.mainDisplays.get(index).setText(input);
		}
		this.mainDisplays.get(index).setEnabled(enabled);
		this.mainDisplays.get(index).setVisible(show);
	}
	
	public void updateDisplayValue(int index, int value) {
		this.mainDisplays.get(index).setValue(value);
	}
	
	public void updateDisplayFunction(int index, Actions action) {
		this.mainDisplays.get(index).setAction(action);
	}
	
	public void openStore() {
		//Gives player options to buy, sell, close, or ask for advice
		updateMainDisplay(10, "Buy Items", true, true);
		updateDisplayFunction(10, Actions.OPEN_BUY);
		updateMainDisplay(11, "Sell Items", true, true);
		updateDisplayFunction(11, Actions.OPEN_SELL);
		updateMainDisplay(12, "Talk to Shopkeeper", true, true);
		updateDisplayFunction(12, Actions.TALK);
		updateMainDisplay(13, "Close Store", true, true);
		updateDisplayFunction(13, Actions.CLOSE_STORE);
	}
	
	public void openBuy() {
		this.statsPanel.setVisible(false);
		clearButtons();
		//Create an exit button
		updateMainDisplay(0, "Close Buy Menu", true, true);
		updateDisplayFunction(0, Actions.CLOSE_BUY_SELL);
		//Create an icon and label for each item in stock
		ArrayList<Item> stock = game.getPlayer().getLocation().getStore().getStock();
		for(int i = 0; i < 5; i++) {
			if(i < stock.size()) {
				String source = "./src/resources/Images/Crate.png";
				if(stock.get(i).getType() == ItemType.CARD) {
					source = "./src/resources/Images/Card.png";
				}
				updateMainDisplay(i + 5, source, true, true);
				updateMainDisplay(i + 10, stock.get(i).getName(), true, true);
				updateDisplayFunction(i + 5, Actions.BUY);
				updateDisplayValue(i + 5, i + 1);
			} else {
				updateMainDisplay(i + 5, "./src/resources/Images/Crate.png", false, true);
			}
		}
	}
	
	private void clearButtons() {
		for (int i = 0; i < 15; i++) {
			updateMainDisplay(i, "", false, false);
		}
	}
	
	public void closeBuy() {
		this.statsPanel.setVisible(true);
		setGameState("Store");
	}
	
	private void makeIslandButtons() {
		updateMainDisplay(10, "Interact with Store", true, true);
		updateDisplayFunction(10, Actions.OPEN_STORE);
		updateMainDisplay(11, "Set Sail", true, true);
		updateDisplayFunction(11, Actions.CHOOSE_ROUTE);
		updateMainDisplay(12, "See Inventory", true, true);
		updateDisplayFunction(12, Actions.OPEN_INVENTORY);
		updateMainDisplay(13, "See Deck", true, true);
		updateDisplayFunction(13, Actions.OPEN_DECK);
		updateMainDisplay(14, "Return to Main Menu", true, true);
		updateDisplayFunction(14, Actions.MAIN_MENU);
	}
	
	public int getCurrentPage() {
		return this.currentPage;
	}
	
	public void showInventory() {
		clearButtons();
		ArrayList<Item> items = new ArrayList<Item>();
		items.addAll(game.getPlayer().getInventory());
		for (int reference = 0; reference < 5; reference++) {
			if(this.currentPage * 5 + reference < items.size()) {
				updateMainDisplay(reference, items.get(reference).getName(), true, true);
				updateMainDisplay(reference + 5, items.get(reference).getDescription(), true, true);
			}
			reference++;
		}
		updateDisplayFunction(11, Actions.PREV_INV);
		updateDisplayFunction(13, Actions.NEXT_INV);
		if (this.currentPage == 0) {
			updateMainDisplay(11, "Previous Page", false, true);
		} else {
			updateMainDisplay(11, "Previous Page", true, true);
		}
		if (items.size() > this.currentPage * 10 + 10) {
			updateMainDisplay(13, "Next Page", true, true);
		} else {
			updateMainDisplay(13, "Next Page", false, true);
		}
		updateMainDisplay(12, "Close Inventory", true, true);
		updateDisplayFunction(12, Actions.CLOSE_STORE);
	}
	
	public void showDeck() {
		clearButtons();
		ArrayList<Item> items = new ArrayList<Item>();
		items.addAll(game.getPlayer().getCards());
		for (int reference = 0; reference < 5; reference++) {
			if(this.currentPage * 5 + reference < items.size()) {
				updateMainDisplay(reference, items.get(reference).getName(), true, true);
				updateMainDisplay(reference + 5, items.get(reference).getDescription(), true, true);
			}
			reference++;
		}
		updateDisplayFunction(11, Actions.PREV_INV);
		updateDisplayFunction(13, Actions.NEXT_INV);
		if (this.currentPage == 0) {
			updateMainDisplay(11, "Previous Page", false, true);
		} else {
			updateMainDisplay(11, "Previous Page", true, true);
		}
		if (items.size() > this.currentPage * 10 + 10) {
			updateMainDisplay(13, "Next Page", true, true);
		} else {
			updateMainDisplay(13, "Next Page", false, true);
		}
		updateMainDisplay(12, "Close Inventory", true, true);
		updateDisplayFunction(12, Actions.CLOSE_STORE);
	}
	
	public void setGameState(String s) {
		this.currentState = s;
		clearButtons();
		switch(s) {
		case "Island":
			changeForegroundColour(Color.WHITE);
			changeBackground("./src/resources/Images/IslandBackground.png");
			makeIslandButtons();
			break;
		case "Sea":
			changeBackground("./src/resources/Images/SeaBackground.png");
			changeForegroundColour(Color.BLACK);
			updateMainDisplay(10, "Return to Island", true, true);
			updateDisplayFunction(10, Actions.CLOSE_STORE);
			ArrayList<Island> islands = new ArrayList<Island>();
			islands.addAll(game.getIslands());
			for(Island island : islands) {
				updateMainDisplay(island.getDisplay(), "./src/resources/Images/Island.png", true, true);
				updateDisplayFunction(island.getDisplay(), Actions.SAIL);
			}
			break;
		case "Store":
			changeForegroundColour(Color.WHITE);
			changeBackground("./src/resources/Images/ShopBackground.png");
			openStore();
			break;
		case "Inventory":
			changeForegroundColour(Color.BLACK);
			changeBackground("./src/resources/Images/InventoryBackground.png");
			showInventory();
			break;
		case "Deck":
			changeForegroundColour(Color.BLACK);
			changeBackground("./src/resources/Images/InventoryBackground.png");
			break;
		case "Menu":
			welcome(game);
			this.frmDeckgame.dispose();
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
		lblStatsDivider.setIcon(new ImageIcon(new ImageIcon("./src/resources/Images/ParchmentDividerHorizontal.png").getImage().getScaledInstance(90, 15, Image.SCALE_DEFAULT)));
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
		
		JLabel lblAnnounceCapacity = new JLabel("Capacity:");
		lblAnnounceCapacity.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblAnnounceCapacity, "cell 0 5 2 1,alignx left,aligny center");
		
		lblHealthStat = new JLabel("Error ");
		lblHealthStat.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblHealthStat, "cell 5 2, alignx right, aligny center");
		
		lblSpeedStat = new JLabel("Error ");
		lblSpeedStat.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblSpeedStat, "cell 5 3, alignx right, aligny center");
		
		lblLuckStat = new JLabel("Error ");
		lblLuckStat.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblLuckStat, "cell 5 4, alignx right, aligny center");
		
		lblCapacityStat = new JLabel("Error ");
		lblCapacityStat.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		statsPanel.add(lblCapacityStat, "cell 5 5, alignx right, aligny center");
	}
	
	/**
	 * Creates the dialogue panel at the bottom of the screen
	 */
	private void createDialogPanel() {
		dialogPanel = new JPanel();
		dialogPanel.setBounds(0, 700, 1540, 122);
		dialogPanel.setOpaque(false);
		dialogPanel.setBorder(BorderFactory.createMatteBorder(15, 0, 0, 0, new ImageIcon("./src/resources/Images/ParchmentTop.png")));
		frmDeckgame.getContentPane().add(dialogPanel);
		dialogPanel.setLayout(null);
		
		JPanel goldPanel = new JPanel();
		goldPanel.setBackground(Color.decode("#F0DD8D"));
		goldPanel.setBounds(0, 15, 200, 107);
		dialogPanel.add(goldPanel);
		goldPanel.setLayout(new MigLayout("", "[151px]", "[26px][26px][]"));
		
		lblCurrentDay = new JLabel("Current day: 0", JLabel.CENTER);
		lblCurrentDay.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
		goldPanel.add(lblCurrentDay, "cell 0 0,alignx left,aligny center");
		
		lblMoney = new JLabel("Gold: 25", JLabel.CENTER);
		lblMoney.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
		goldPanel.add(lblMoney, "cell 0 1,alignx left,aligny center");
		
		JPanel outputPanel = new JPanel();
		outputPanel.setBackground(Color.decode("#F0DD8D"));
		outputPanel.setBounds(200, 15, 1340, 107);
		outputPanel.setBorder(BorderFactory.createMatteBorder(0, 15, 0, 0, new ImageIcon("./src/resources/Images/ParchmentDividerVertical.png")));
		dialogPanel.add(outputPanel);
			
		outputArea = new JTextArea("", 3, 65);
		outputArea.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
		outputArea.setBackground(Color.decode("#F0DD8D"));
		outputArea.setLineWrap(true);
		outputArea.setWrapStyleWord(true);
		outputArea.setEditable(false);
		outputPanel.add(outputArea);
	}
	
	private void openLog() {
		this.logOpen = true;
		changeForegroundColour(Color.BLACK);
		changeBackground("./src/resources/Images/LogBackground.png");
		//TODO update background sprite for log
		this.statsPanel.setVisible(false);
		showLog();
	}
	
	public void changeCurrentPage(int change) {
		this.currentPage += change;
	}
	
	public void changeLogPage(int change) {
		this.currentLogPage += change;
	}
	
	public void showLog() {
		clearButtons();
		ArrayList<Entry> entries = new ArrayList<Entry>();
		entries.addAll(game.getLogItems());
		for (int reference = 0; reference < 10; reference++) {
			if(this.currentLogPage * 10 + reference < entries.size()) {
				updateMainDisplay(reference, entries.get(reference).toString(), true, true);
			}
			reference++;
		}
		updateDisplayFunction(11, Actions.LOG_PREV);
		updateDisplayFunction(13, Actions.LOG_NEXT);
		if (this.currentLogPage == 0) {
			updateMainDisplay(11, "Previous Page", false, true);
		} else {
			updateMainDisplay(11, "Previous Page", true, true);
		}
		if (entries.size() > this.currentLogPage * 10 + 10) {
			updateMainDisplay(13, "Next Page", true, true);
		} else {
			updateMainDisplay(13, "Next Page", false, true);
		}
	}
	
	private void closeLog() {
		this.logOpen = false;
		setGameState(currentState);
		this.statsPanel.setVisible(true);
		this.currentLogPage = 0;
	}
	
	/**
	 * Fills in the main display area of the game
	 */
	private void fillMainDisplay() {
		for(int i=0; i < 15; i++) {
			ChangingButton temp_button = new ChangingButton("", this);
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
			display.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
			display.setForeground(Color.WHITE);
		}
	}
	
	private void welcome(Game game) {
		this.game = game;
		ArrayList<JComponent> info = new ArrayList<JComponent>();
		JFrame frmMenu = new JFrame();
		frmMenu.setTitle("Main Menu");
		frmMenu.setBounds(0, 0, 500, 500);
		frmMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMenu.setUndecorated(false);
		frmMenu.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(108, 10, 378, 453);
		frmMenu.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblTitle = new JLabel("Welcome to DeckGame");
		lblTitle.setBounds(90, 10, 134, 20);
		panel.add(lblTitle);
		
		JTextField userName = new JTextField();
		userName.setBounds(136, 109, 96, 19);
		panel.add(userName);
		userName.setColumns(15);
		info.add(userName);
		
		JLabel lblUsername = new JLabel("Username: Captain");
		lblUsername.setBounds(10, 112, 116, 13);
		panel.add(lblUsername);
		info.add(lblUsername);
		
		JTextField shipName = new JTextField();
		shipName.setBounds(136, 138, 96, 19);
		panel.add(shipName);
		shipName.setColumns(15);
		info.add(shipName);
		
		JLabel lblShipName = new JLabel("Ship Name: The");
		lblShipName.setBounds(24, 141, 102, 13);
		panel.add(lblShipName);
		info.add(lblShipName);
		
		JSlider duration = new JSlider(JSlider.HORIZONTAL, 20, 50, 20);
		duration.setBounds(78, 188, 137, 20);
		duration.setMajorTickSpacing(10);
		duration.setMinorTickSpacing(1);
		panel.add(duration);
		info.add(duration);
		
		JLabel lblDuration = new JLabel("Duration:");
		lblDuration.setBounds(126, 165, 89, 13);
		panel.add(lblDuration);
		info.add(lblDuration);
		
		JLabel lblShipType = new JLabel("Ship Class:");
		lblShipType.setBounds(119, 218, 74, 13);
		panel.add(lblShipType);
		info.add(lblShipType);
		
		JRadioButton rdbtnShip1 = new JRadioButton("Sloop");
		rdbtnShip1.setBounds(90, 247, 103, 21);
		rdbtnShip1.setActionCommand("1");
		rdbtnShip1.setToolTipText("A ship with 10 crew which has a balance of all stats.");
		rdbtnShip1.setSelected(true);
		panel.add(rdbtnShip1);
		info.add(rdbtnShip1);
		
		JRadioButton rdbtnShip2 = new JRadioButton("Brig");
		rdbtnShip2.setBounds(90, 270, 103, 21);
		rdbtnShip2.setActionCommand("2");
		rdbtnShip2.setToolTipText("A slow moving ship with 10 crew that has more cargo space.");
		panel.add(rdbtnShip2);
		info.add(rdbtnShip2);
		
		JRadioButton rdbtnShip3 = new JRadioButton("Frigate");
		rdbtnShip3.setBounds(90, 293, 103, 21);
		rdbtnShip3.setActionCommand("3");
		rdbtnShip3.setToolTipText("A slow moving ship with 20 crew that has high health and damage.");
		panel.add(rdbtnShip3);
		info.add(rdbtnShip3);
		
		JRadioButton rdbtnShip4 = new JRadioButton("Clipper");
		rdbtnShip4.setBounds(90, 316, 103, 21);
		rdbtnShip4.setActionCommand("4");
		rdbtnShip4.setToolTipText("A fast ship with 15 crew that has lower heath and strength.");
		panel.add(rdbtnShip4);
		info.add(rdbtnShip4);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnShip1);
		group.add(rdbtnShip2);
		group.add(rdbtnShip3);
		group.add(rdbtnShip4);
		
		JLabel lblError1 = new JLabel("No special symbols.");
		lblError1.setBounds(242, 112, 126, 13);
		panel.add(lblError1);
		lblError1.setVisible(false);
		
		JLabel lblError2 = new JLabel("Must be between 3 and 15 characters.");
		lblError2.setBounds(242, 141, 126, 13);
		panel.add(lblError2);
		lblError2.setVisible(false);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(game.getNames(userName.getText(), shipName.getText())) {
					game.sessionSetup(userName.getText(), shipName.getText(), duration.getValue(), group.getSelection().getActionCommand());
					initialize();
					frmMenu.dispose();
				}else {
					lblError1.setVisible(true);
					lblError2.setVisible(true);
				}
			}
		});
		btnSubmit.setBounds(93, 369, 100, 21);
		panel.add(btnSubmit);
		info.add(btnSubmit);
		
		for (JComponent component : info) {
			component.setVisible(false);
		}
		
		JButton btnNewGame = new JButton("New Game");
		btnNewGame.setBounds(97, 58, 96, 21);
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(JComponent component : info) {
					component.setVisible(true);
				}
			}
		});
		panel.add(btnNewGame);
		
		frmMenu.setVisible(true);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		logOpen = false;
		currentPage = 0;
		currentLogPage = 0;
		mainDisplays = new ArrayList<ChangingButton>();
		frmDeckgame = new JFrame();
		frmDeckgame.setTitle("Deckgame");
		frmDeckgame.setBounds(50, 50, 2000, 2000);
		frmDeckgame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDeckgame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frmDeckgame.setUndecorated(false);
		frmDeckgame.getContentPane().setLayout(null);
		
		createDialogPanel();
		
		displayPanel = new ImagePanel(new ImageIcon("./src/resources/Images/SeaBackground.png").getImage());
		displayPanel.setBounds(0, 0, 1540, 715);
		displayPanel.setBackground(Color.decode("#A2E2F2"));
		frmDeckgame.getContentPane().add(displayPanel);
		
		mainDisplayPanel = new JPanel();
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
		
		openLog = new JButton(new ImageIcon("./src/resources/Images/CaptainsLog.png"));
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
		
		statsPanel = new JPanel();
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
		
		setGameState("Island");
		
		frmDeckgame.setVisible(true);
	}
}
