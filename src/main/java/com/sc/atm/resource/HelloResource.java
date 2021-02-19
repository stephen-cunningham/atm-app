package com.sc.atm.resource;

import com.sc.atm.model.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/atm")
public class HelloResource {

    /**
     * landing site for loading the required information, accounts, etc.
     * @param session the Http session, for storing objects to be used throughout the app
     */
    @GetMapping("/")
    @ResponseBody
    public void loadAccounts(HttpSession session){
        Bank bank = new Bank();
        //following the given requirements
        CurrentAccount current1 = new CurrentAccount("123456789", 500, 200);
        CurrentAccount current2 = new CurrentAccount("987654321", 500, 150);
        current1.resetPin("1234");
        current1.addMoney(800);
        current2.resetPin("4321");
        current2.addMoney(1230);
        bank.addAccount(current1);
        bank.addAccount(current2);
        session.setAttribute("currentAccounts", bank.getCurrentsAccounts());

        Atm atm = new Atm();
        session.setAttribute("atm", atm);
    }

    /**
     * check the balance of the account at the given account number. Check if the pin is correct before doing so
     * @param accNum the user's account number
     * @param pin the user's (attempted) pin
     * @param session the Http session, for storing objects to be used throughout the app
     * @return the user's balance, if the pin is correct
     */
    @GetMapping(value = "/balance/{accNum}/{pin}")
    @ResponseBody
    public String checkBalance(@PathVariable("accNum") String accNum,
                               @PathVariable("pin") String pin,
                               HttpSession session) {
        ArrayList<CurrentAccount> currentAccs = (ArrayList<CurrentAccount>)session.getAttribute("currentAccounts");

        CurrentAccount account = findAccount(currentAccs, accNum);

        /**
         * NOTE!!! this is a back-door to override the pin lock - would be removed in production environment
         */
        if(account.getLockedAccount()){
            if(pin.equals("****")){
                account.setLockedAccount(false);
            }
            return "You are locked out of your account - contact customer support";
        }

        if(!account.passPinChallenge(pin)){
            return "Incorrect pin - try again!";
        }

        return "Balance is " + account.getBalance();
    }

    /**
     * check the maximum withdrawal the user can avail of
     * @param accNum the user's account number
     * @param pin the user's (attempted) pin
     * @param session the Http session, for storing objects to be used throughout the app
     * @return the max withdrawal
     */
    @GetMapping(value = "/maxWithdrawal/{accNum}/{pin}")
    @ResponseBody
    public String checkMaxWithdrawal(@PathVariable("accNum") String accNum,
                                     @PathVariable("pin") String pin,
                                     HttpSession session) {
        ArrayList<CurrentAccount> currentAccs = (ArrayList<CurrentAccount>)session.getAttribute("currentAccounts");
        Atm atm = (Atm)session.getAttribute("atm");

        CurrentAccount account = findAccount(currentAccs, accNum);

        return "Max withdrawal is €" + Math.min(Math.min(account.getWithdrawLimit(), account.calcAvailableFunds()),
                                                atm.getFunds());
    }

    /**
     * withdraw cash for the user, if funds are sufficient (in account and atm)
     * @param accNum the user's account number
     * @param pin the user's (attempted) pin
     * @param amount the amount the user wants to withdraw
     * @param session the Http session, for storing objects to be used throughout the app
     * @return the amount withdrawn (or error)
     */
    @GetMapping(value = "/withdraw/{accNum}/{pin}/{amount}")
    @ResponseBody
    public String withdrawMoney(@PathVariable("accNum") String accNum,
                                @PathVariable("pin") String pin,
                                @PathVariable("amount") float amount,
                                HttpSession session) {
        ArrayList<CurrentAccount> currentAccs = (ArrayList<CurrentAccount>)session.getAttribute("currentAccounts");
        Atm atm = (Atm)session.getAttribute("atm");

        if(!atm.isSufficientNotes(amount)){
            return "Insufficient ATM funds - maybe try a smaller amount.";
        }

        CurrentAccount account = findAccount(currentAccs, accNum);

        float withdrawn = account.withdrawMoney(amount);
        String returnVal = "€" + withdrawn + " issued";
        if(withdrawn == 0.0){
            if(amount > account.calcAvailableFunds()){
                returnVal += "\nYou only have " + account.calcAvailableFunds() + " in available funds";
            }
            if(amount > account.getWithdrawLimit()){
                returnVal += "\nYou have a withdrawal limit of " + account.getWithdrawLimit();
            }
        }

        return returnVal;
    }

    /**
     * find the user's account in the ArrayList based on their account number and return it
     * @param accounts the ArrayList containing all accounts
     * @param accNum the account number to look for
     * @return the user's account
     */
    public CurrentAccount findAccount(ArrayList<CurrentAccount> accounts, String accNum){
        CurrentAccount account = accounts.stream()
                .filter(acc -> acc.getAccountNum().equals(accNum))
                .findFirst().orElse(null);
        return account;
    }
}