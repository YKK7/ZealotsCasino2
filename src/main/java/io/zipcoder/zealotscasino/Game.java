package io.zipcoder.zealotscasino;

import javax.jws.soap.SOAPBinding;

/**
 * Created by luisgarcia on 5/10/17.
 */
public class Game
{
    private Player player;
    private CardDealer dealer;
    UserInput user;

    public Game(){
        player = new Player();
        user = new UserInput();
    }

    public void playBlackJack()
    {
        dealer = new BlackJackDealer();
    }

    public void playWar()
    {
        dealer = new WarDealer();

    }

    /*public void playPoker()
    {
        dealer = new PokerDealer();
    }

    public void playRoulette()
    {
        dealer = new RouletteDealer();
    }

    public void playSlots()
    {
        dealer = new SlotDealer();
    } */

    public void instantiatePlayer(double money)
    {
        player.initializeWallet(money);
    }

    public void chooseGame()
    {
        double gameChoice = user.getDoubleInput("Choose your # of choice: ");


        if(gameChoice == 1)
        {
            this.playWar();
        }
        else if(gameChoice == 2)
        {
            this.playBlackJack();
        }
/*        else if(gameChoice == 3)
        {
            this.playPoker();
        }
        else if(gameChoice == 4)
        {
            this.playRoulette();
        }
        else if(gameChoice == 5)
        {
            this.playSlots();
        }*/
        else
        {
            System.out.println("Invalid Entry. Try Again");
            this.chooseGame();
        }
    }


    public void displayMenu()
    {
        System.out.println("----------------------------Welcome to Zealot's Casino----------------------------");
        System.out.println("What will you like to play?");
        System.out.println("(1) War");
        System.out.println("(2) Blackjack");
        System.out.println("(3) Poker");
        System.out.println("(4) Roulette");
        System.out.println("(5) Slots");

        this.chooseGame();

        double startWallet = user.getDoubleInput("How much money do you want to start your wallet with?");
        this.instantiatePlayer(startWallet);





    }

}
