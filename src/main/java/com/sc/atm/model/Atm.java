package com.sc.atm.model;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Atm {
	private LinkedHashMap<Integer, Integer> noteCounts; //a count of how many of each note are in the atm
	private float funds;

	public Atm() {
		this.funds = 1500; //setting funds to what is set out in requirements
		this.noteCounts = new LinkedHashMap<>();
		//setting the counts to those set out in requirements
		this.noteCounts.put(50, 10);
		this.noteCounts.put(20, 30);
		this.noteCounts.put(10, 30);
		this.noteCounts.put(5, 20);
	}

	//overloaded constructor
	public Atm(float funds) {
		this.funds = funds;
	}

	public float getFunds(){
		return funds;
	}

	public LinkedHashMap<Integer, Integer> getNoteCounts(){
		return noteCounts;
	}

	/**
	 * adds funds to the ATM. Note: not implemented in this app but useful for future extension
	 * Note: will have to be adapted to account for number of notes being added
	 * @param amount the amount of funds to add
	 */
	public void addFunds(float amount) {
		this.funds += amount;
	}

	/**
	 * counts the notes required for a user's withdrawal
	 * @param amount the amount the user is requesting
	 * @return a LinkedHashMap containing each note denomination as key and its respective count as value
	 */
	public synchronized LinkedHashMap<Integer, Integer> countNotes(float amount){
		//setting up a data structure to count the required notes to complete transaction
		LinkedHashMap<Integer, Integer> counting = new LinkedHashMap<>();
		counting.put(50, 0);
		counting.put(20, 0);
		counting.put(10, 0);
		counting.put(5, 0);

		Set<Integer> keys = getNoteCounts().keySet();
		for(Integer note: keys){//for each note denomination
			if(note <= amount){
				//if there are enough of this denomination to complete the transaction
				if(getNoteCounts().get(note) > (int)(amount/note)) {
					counting.put(note, (int) (amount / note));
					this.noteCounts.put(note, getNoteCounts().get(note) - (int) (amount / note));
					break; //can jump out of loop since transaction is satisfied
				}else{
					//if there aren't enough of this denomination, store the number of required notes and try to
					//	satisfy the request with the next (smaller) denomination (if any) during the next iteration
					counting.put(note, getNoteCounts().get(note));
					amount -= (note * getNoteCounts().get(note));
					this.noteCounts.put(note, 0);
				}
			}
		}
		return counting;
	}

	/**
	 * compares the required number for each note with its corresponding availability and checks if there are sufficient
	 * 	notes available to satisfy the user's withdrawal request
	 * @param amount the amount of money requested
	 * @return boolean indicating if there are sufficient notes
	 */
	public boolean isSufficientNotes(float amount){
		LinkedHashMap<Integer, Integer> requiredNotes = countNotes(amount);
		Set<Integer> keys = getNoteCounts().keySet();
		for(Integer note: keys){
			System.out.println(requiredNotes.get(note) + " ||| " + this.getNoteCounts().get(note));
			if(requiredNotes.get(note) > this.getNoteCounts().get(note)){
				return false;
			}
		}

		return true;
	}
}
