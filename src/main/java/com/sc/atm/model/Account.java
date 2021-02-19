package com.sc.atm.model;

import java.util.Date;
import java.util.Random;

//this class is kept abstract to avoid instantiation. Allows for extension (currently for current and savings accounts)
public abstract class Account {
	private final String accountNum;
	private float balance = 0; //money in account (not including overdraft)
	private float overdraft = 0;
	private float overdraftUsed = 0;
	private float withdrawLimit = 500;
	private final Date dateCreated;

	//default constructor
	public Account(){
		this.accountNum = generateAccNum();
		this.dateCreated = new Date();
	}

	public Account(String accountNum){
		this.accountNum = accountNum;
		this.dateCreated = new Date();
	}
	
	public Account(String accountNum, float withdrawLimit) {
		this.accountNum = accountNum;
		this.dateCreated = new Date();

		this.withdrawLimit = withdrawLimit;
	}

	public Account(String accountNum, float withdrawLimit, float overdraft){
		this.accountNum = accountNum;
		this.dateCreated = new Date();

		this.withdrawLimit = withdrawLimit;
		this.overdraft = overdraft;
	}
	
	public String getAccountNum() {
		return accountNum;
	}
	
	public float getBalance() {
		return balance;
	}

	public float getOverdraft(){
		return overdraft;
	}

	public void setOverdraft(float overdraft){
		this.overdraft = overdraft;
	}

	public float getOverdraftUsed(){
		return overdraftUsed;
	}

	public void setOverdraftUsed(float amountUsed){
		this.overdraftUsed += amountUsed;
	}

	public void setWithdrawLimit(float withdrawLimit) {
		this.withdrawLimit = withdrawLimit;
	}
	
	public float getWithdrawLimit() {
		return withdrawLimit;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * Checks if the account number is valid. Deemed to be valid only if it has nine numeric characters
	 * @return whether the account number is valid
	 */
	public boolean accNumIsValid() {
		String accountNum = this.getAccountNum();
		if(accountNum.length() != 9) { return false;}

		//check if all characters are numeric
		return accountNum.chars().allMatch(Character::isDigit);
	}

	/**
	 * Calculates the available funds for the account (balance + any overdraft)
	 * @return available funds
	 */
	public float calcAvailableFunds(){
		return getBalance() + getOverdraft();
	}

	/**
	 * Adds money to the user's account. Note: not implemented in API - not in requirements but useful for extending app
	 * @param amount the amount of money to add to the account
	 */
	public void addMoney(float amount) {
		//calculate if there is any overdraft used. If so, clear some (or all) of it before adding to main balance
		if(this.getOverdraftUsed() > 0){
			float surplus = amount - this.getOverdraftUsed();
			if(surplus > 0){
				this.balance += surplus;
			}
			setOverdraftUsed(surplus - amount);
		}

		this.balance += amount;
	}

	/**
	 * withdraw money from the user's account
	 * @param amount the amount of money to withdraw
	 * @return the withdrawn money
	 */
	public float withdrawMoney(float amount) {
		//returns nothing if the request is unsuitable - this is handled elsewhere
		if(amount > calcAvailableFunds() || amount > this.getWithdrawLimit()){
			return 0;
		}else{
			if(this.getBalance() < amount){
				float remainder = amount - this.getBalance();
				if(remainder > (this.getOverdraft() - this.getOverdraftUsed())){
					return 0;
				}

				this.balance = 0;
				setOverdraftUsed(remainder);
			}else{
				this.balance -= amount;
			}
			return amount;
		}
	}

	/**
	 * generates a random nine-digit account number
	 * @return the account number
	 */
	public String generateAccNum(){
		Random rand = new Random();
		String randNum = String.valueOf(rand.nextInt(1000000000));
		return ("000000000" + randNum).substring(randNum.length());
	}
}
