package com.sc.atm.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Bank {
    //creating ArrayLists to store current accounts and savings accounts respectively
    private ArrayList<CurrentAccount> currentAccounts;
    private ArrayList<SavingsAccount> savingsAccounts;

    public Bank(){
        currentAccounts = new ArrayList<CurrentAccount>();
        savingsAccounts = new ArrayList<SavingsAccount>();
    }

    /**
     * adds accounts to the appropriate ArrayList.
     * use of generics to accomodate both types of accounts (and in future, all potential types with minor edits to code)
     * @param account the account to be added to an ArrayList
     * @param <T> the type of the account (e.g. CurrentAccount, SavingsAccount)
     */
    public <T extends Account> void addAccount(T account){
        if(account instanceof CurrentAccount){
            currentAccounts.add((CurrentAccount) account);
        }else if(account instanceof SavingsAccount){
            savingsAccounts.add((SavingsAccount) account);
        }
    }

    public ArrayList<CurrentAccount> getCurrentsAccounts(){
        return currentAccounts;
    }

    public ArrayList<SavingsAccount> getSavingsAccounts(){
        return savingsAccounts;
    }
}
