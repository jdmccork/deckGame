package deckGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import enums.Actions;

public class ChangingButton extends JButton {
	private Actions action;
	private int value;
	ChangingButton(String s, Display display) {
		super(s);
		this.action = Actions.NONE;
		this.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				switch(action) {
				case START:
					display.setGameState("Beginning");
					break;
				case SELL:
					//display.game.sellItem(this.value);
					break;
				case BUY:
					//display.game.buyItem(this.value);
					break;
				case SAIL:
					//display.game.selectRoute(this.value);
					break;
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
				case CLOSE_STORE:
					display.changeCurrentPage(-display.getCurrentPage());
					display.setGameState("Island");
					break;
				case TALK:
					break;
				case OPEN_BUY:
					display.openBuy();
				case OPEN_SELL:
					break;
				case CLOSE_BUY_SELL:
					display.changeCurrentPage(-display.getCurrentPage());
					display.setGameState("Store");
				case NONE:
					break;
				}
			} 
		} );
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void setAction(Actions action) {
		this.action = action;
	}
}
