package com.sc.atm.model;

import java.util.Random;

public class CurrentAccount extends Account {
	private String pin; //only current account has access to ATMs
	private byte incorrectPins = 0; 
	private boolean lockedAccount;

	//default constructor
	public CurrentAccount(){
		super();
		this.pin = generatePin();
	}

	public CurrentAccount(String accountNum){
		super(accountNum);
		this.pin = generatePin();
	}

	public CurrentAccount(String accountNum, float withdrawLimit) {
		super(accountNum, withdrawLimit);
		this.pin = generatePin();
	}
	
	public CurrentAccount(String accountNum, float withdrawLimit, float overdraft) {
		super(accountNum, withdrawLimit, overdraft);
		this.pin = generatePin();
	}

	public String getPin(){
		return pin;
	}

	public void setLockedAccount(boolean locked) {
		this.lockedAccount = locked;
		if(!locked) {resetIncorrectPins();} //resets pin attempts if lock is removed
	}
	
	public boolean getLockedAccount() {
		return lockedAccount;
	}

	/**
	 * resets incorrect number of pin inputs
	 */
	public void resetIncorrectPins() {
		this.incorrectPins = 0;
	}

	/**
	 * checks whether the pin is correct. Locks the account after 3 consecutive incorrect attempts
	 * @param pinAttempt the attempted pin
	 * @return whether the user passed the pin challenge
	 */
	public boolean passPinChallenge(String pinAttempt) {
		if(!pinAttempt.equals(this.pin)) {
			this.incorrectPins += 1;

			if(this.incorrectPins == 3) {
				setLockedAccount(true);
			}

			return false;
		}else {
			resetIncorrectPins();
			return true;
		}
	}

	/**
	 * generates a random 4 digit pin
	 * @return pin for user account
	 */
	public String generatePin(){
		Random rand = new Random();
		String randNum = String.valueOf(rand.nextInt(10000));
		return ("0000" + randNum).substring(randNum.length());
	}

	/**
	 * resets the user's pin to the given (new) pin
	 * @param newPin the new pin
	 */
	public void resetPin(String newPin){
		if(pinNumIsValid(newPin)){
			this.pin = newPin;
		}
	}

	/**
	 * checks if the pin number conforms to the valid format (4 numeric characters)
	 * @param pinNum the pin to check
	 * @return whether the pin is valid
	 */
	public boolean pinNumIsValid(String pinNum){
		if(pinNum.length() != 4){
			return false;
		}

		return pinNum.chars().allMatch(Character::isDigit);
	}
}
