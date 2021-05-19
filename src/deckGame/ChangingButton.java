package deckGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import enums.Actions;

public class ChangingButton extends JButton {
	private Actions action;
	private int value;
	ChangingButton(String s, Game game) {
		super(s);
		this.action = Actions.NONE;
		this.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				switch(action) {
				case SELL:
					//game.sellItem(this.value);
					break;
				case BUY:
					//game.buyItem(this.value);
					break;
				case SAIL:
					//game.selectRoute(this.value);
					break;
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
