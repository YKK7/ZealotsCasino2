package io.zipcoder.zealotscasino;

/**
 * Created by aaronlong on 5/12/17.
 */
public class Bet {

    private int bet;
    public final static int MINIMUM_BET = 20;

    public boolean makeBet(int betValue, Player player) {
        boolean flag = true;
        while(flag) {
            if (MINIMUM_BET > betValue) {
                displayMinimumBet();
                return false;
            } else if (player.getWallet() < betValue) {
                UserInput.display("You do not have enough chips!\n" + player.printWallet());
                return false;
            }
        }
        this.bet = betValue;
        player.setWallet(player.getWallet() - betValue);
        return true;
    }

    public int getBetValue() {
        return this.bet;
    }

    public static void promptBet() {
        UserInput.display("How much would you like to bet?");
    }

    public static void displayMinimumBet() {
        UserInput.display("Minimum bet is $20.");
    }

}
