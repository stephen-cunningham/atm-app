package com.sc.atm.model;

public class SavingsAccount extends Account{
	//default constructor
	public SavingsAccount(){
		super();
	}

	public SavingsAccount(String accountNum){
		super(accountNum);
	}

	public SavingsAccount(String accountNum, float withdrawLimit) {
		super(accountNum, withdrawLimit);
	}

	public SavingsAccount(String accountNum, float withdrawLimit, float overdraft) {
		super(accountNum, withdrawLimit, overdraft);
	}
}
