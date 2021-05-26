package deckGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import enums.Actions;

public class ChangingButton extends JButton {
	private Actions action;
	private int value;
	
	/**
	 * Constructs a button with an actionlistener that can do different things
	 * @param s the string to display in the button
	 * @param display the display instance this button is in
	 */
	ChangingButton(String s, Display display) {
		super(s);
		this.action = Actions.NONE;
		this.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				switch(action) {
				case START:
					display.setGameState("Beginning");
					break;
				case VIEW_SELL:
					display.viewSell(value);
					break;
				case SELL:
					display.sellItem(value);
					break;
				case BUY:
					display.buyItem(value);
					break;
				case GET_ITEM:
					display.getItem();
					break;
				case OPEN_DUMP:
					display.setGameState("Dump");
					break;
				case VIEW:
					display.viewItem(value);
					break;
				case SAIL:
					display.sailShip(value);
					break;
				case CONTINUE:
					display.executeSail();
					break;
				case GO_TO_ISLAND:
					display.setIsland(value);
					break;
				case END:
					display.showFinalScore();
					break;
				case VIEW_REWARD:
					display.showReward();
				case CHOOSE_ROUTE:
					display.setGameState("Sea");
					break;
				case LOG_NEXT:
					display.changeLogPage(1);
					display.showLog();
					break;
				case LOG_PREV:
					display.changeLogPage(-1);
					display.showLog();
					break;
				case NEXT_INV:
					display.changeCurrentPage(1);
					display.showInventory();
					break;
				case PREV_INV:
					display.changeCurrentPage(-1);
					display.showInventory();
					break;
				case NEXT_SELL:
					display.changeCurrentPage(1);
					display.openSell();
					break;
				case PREV_SELL:
					display.changeCurrentPage(-1);
					display.openSell();
					break;
				case OPEN_STORE:
					display.setGameState("Store");
					break;
				case OPEN_INVENTORY:
					display.setGameState("Inventory");
					break;
				case OPEN_DECK:
					display.setGameState("Deck");
					break;
				case MAIN_MENU:
					display.setGameState("Menu");
					break;
				case SHOW_REWARD:
					display.setGameState("Reward");
					break;
				case DUMP:
					display.viewDump(value);
					break;
				case CONFIRM_DUMP:
					display.dumpItem(value);
					break;
				case CLOSE_STORE:
					display.changeCurrentPage(-display.getCurrentPage());
					display.setGameState("Island");
					break;
				case TALK:
					display.talk();
					break;
				case CONFIRM_MENU:
					display.setGameState("Confirm");
					display.updateDialogue("Are you sure you wish to return to the main menu? All progress will be lost.");
					display.updateDisplayFunction(11, Actions.MAIN_MENU);
					display.updateDisplayFunction(13, Actions.CLOSE_STORE);
					break;
				case OPEN_BUY:
					display.openBuy();
					break;
				case OPEN_SELL:
					display.openSell();
					break;
				case CLOSE_BUY_SELL:
					display.changeCurrentPage(-display.getCurrentPage());
					display.setGameState("Store");
					break;
				case REPAIR:
					display.repairShip(value);
					break;
				case PAY:
					display.payCrew(value);
					break;
				case FIGHT:
					display.pirateFight();
					break;
				case FLEE:
					display.pirateFlee();
					break;
				case VIEW_SHIP:
					display.pirateView();
					break;
				case SURRENDER:
					display.surrenderCheck();
					break;
				case CONFIRM_SURRENDER:
					display.surrenderItem();
					break;
				case PIRATE_MENU:
					display.pirateEncounter();
					break;
				case NONE:
					break;
				}
			} 
		} );
	}
	
	/**
	 * Sets the value that the button can pass on
	 * @param value the value that will be passed
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Sets the action which the button carries out.
	 * @param action the action to perform
	 */
	public void setAction(Actions action) {
		this.action = action;
	}
}
