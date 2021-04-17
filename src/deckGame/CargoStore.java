package deckGame;

import java.util.ArrayList;

public class CargoStore extends Store {
	/**
	 * The items the store has available.
	 */
	private ArrayList<Cargo> cargoStock = new ArrayList<Cargo>();
	
	CargoStore(String islandName){
		super(islandName);
	}

}
