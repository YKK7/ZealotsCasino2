package io.zipcoder.zealotscasino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.zipcoder.zealotscasino.UserInput.getDoubleInput;
import static io.zipcoder.zealotscasino.UserInput.getStringInput;

/**
 * Created by luisgarcia on 5/11/17.
 */
public class PokerDealer implements Dealer
{
    private Deck deck;
    private Hand playerHand;
    private Bet bet;

    public PokerDealer()
    {
        bet = new Bet();
        deck = new Deck();
        playerHand = new Hand();
        deck.buildDeck();
    }


    public void dealCardTo(Player player)
    {
        playerHand.receiveCard(deck.surrenderCard());
    }


    public void dealHandTo(Player player)
    {
        for (int i = 0; i < 5; i++)
            dealCardTo(player);
    }


    public void pay(Player player, double payOut)
    {
        player.collectWinnings(payOut);
        UserInput.display("You win " + payOut + "!\nWallet: " + player.getWallet());
    }

    public void play(Player player)
    {
        deck.buildAnotherDeck();

        //get bet
        makeBet(player);

        //deal cards to player
        dealHandTo(player);

        //display
        userDisplayHand(player);

        //discard cards
        int numberToReplace = discardCards(player);

        //replace discarded cards
        replace(player, numberToReplace);

        //display new hand
        UserInput.display("Updated Hand: ");
        userDisplayHand(player);

        //calculate hand
        String rankOfHand = calculateHand(player);
        UserInput.display("You got a " + rankOfHand);

        // determine winnings and pay
        payPlayer(player, rankOfHand);

        playerHand.remove();

        askPlayAgain(player);
    }

    public void payPlayer(Player player, String rankOfHand)
    {
        switch (rankOfHand)
        {
            case "PAIR":
                pay(player, bet.getBetValue());
                break;
            case "TWO PAIR":
                pay(player, bet.getBetValue() * 2);
                break;
            case "THREE OF A KIND":
                pay(player, bet.getBetValue() * 3);
                break;
            case "STRAIGHT":
                pay(player, bet.getBetValue() * 4);
                break;
            case "FLUSH":
                pay(player, bet.getBetValue() * 6);
                break;
            case "FULL HOUSE":
                pay(player, bet.getBetValue() * 9);
                break;
            case "FOUR OF A KIND":
                pay(player, bet.getBetValue() * 25);
                break;
            case "STRAIGHT FLUSH":
                pay(player, bet.getBetValue() * 50);
                break;
            case "ROYAL FLUSH":
                pay(player, bet.getBetValue() * 976);
                break;
            case "NO PAIR":
                UserInput.display("Sorry, you lose!\nWallet: " + player.getWallet());
                break;
            default:
                break;
        }
    }

    public void askPlayAgain(Player player)
    {
        String choice = getStringInput("Would you like to play again? (Push 'Y' to play again, 'Any other key' to quit)");
        if (choice.equalsIgnoreCase("Y"))
            play(player);
        else
            UserInput.display("Thanks for playing!\n\n");
    }

    public String calculateHand(Player player)
    {
        int numberOfValues = returnNumberOfValuesInPlayerHand(player);
        if (numberOfValues == 5)
        {
            return evaluateFiveRanks(player);
        } else if (numberOfValues == 4)
        {
            return "PAIR";
        } else if (numberOfValues == 3)
        {
            return evaluateThreeRanks(player);
        } else
        {
            return evaluateTwoRanks(player);
        }
    }

    public String evaluateFiveRanks(Player player)
    {
        if (checkRoyalFlush(player))
            return "ROYAL FLUSH";
        else if (checkStraightFlush(player))
            return "STRAIGHT FLUSH";
        else if (checkFlush(player))
            return "FLUSH";
        else if (checkStraight(player))
            return "STRAIGHT";
        else
            return "NO PAIR";
    }

    public String evaluateTwoRanks(Player player)
    {
        ArrayList<Card> hand = playerHand.getCards();
        Map<Integer, Integer> mapOfHand = toHashMap(hand);
        for (Integer key : mapOfHand.keySet())
        {
            if (mapOfHand.get(key) == 4) return "FOUR OF A KIND";
        }
        return "FULL HOUSE";
    }

    public String evaluateThreeRanks(Player player)
    {
        ArrayList<Card> hand = playerHand.getCards();
        Map<Integer, Integer> mapOfHand = toHashMap(hand);
        for (Integer key : mapOfHand.keySet())
        {
            if (mapOfHand.get(key) == 3) return "THREE OF A KIND";
        }
        return "TWO PAIR";
    }

    public Map<Integer, Integer> toHashMap(ArrayList<Card> playerHand)
    {
        Map<Integer, Integer> mapOfValues = new HashMap<>();

        for (int i = 0; i < playerHand.size(); i++)
        {
            Integer key = playerHand.get(i).getValue();
            Integer frequency = mapOfValues.get(key);
            mapOfValues.put(key, frequency == null ? 1 : frequency + 1);
        }
        return mapOfValues;
    }

    public int returnNumberOfValuesInPlayerHand(Player player)
    {
        Map<Integer, Integer> mapOfValues = toHashMap(playerHand.getCards());
        Integer amountOfKey = mapOfValues.keySet().size();
        return amountOfKey;
    }

    public boolean checkRoyalFlush(Player player)
    {
        ArrayList<Card> playerCards = playerHand.getCards();
        Collections.sort(playerCards);

        if (returnNumberOfValuesInPlayerHand(player) == 5 && (playerCards.get(0).getValue() == 10) && checkFlush(player))
        {
            return true;
        }
        return false;
    }

    public boolean checkStraight(Player player)
    {
        ArrayList<Card> playerCards = playerHand.getCards();
        Collections.sort(playerCards);
        int value = 2;
        //int value2 = playerCards.get(0).getValue();

        for (int i = 1; i < 4; i++)
        {
            if ((playerCards.get(i).getValue() == value) && (playerCards.get(4).getFaceValue().equals("ACE")))
            {
                System.out.println("1: " + i);
                value++;
                if (value == 5) return true;
            }
        }

        for (int i = 0; i < 4; i++)
        {
            if (playerCards.get(i).getValue() == (playerCards.get(i+1).getValue()))
            {
                System.out.println("b" + i);
            }
        }
        return false;
    }

    public boolean checkStraightFlush(Player player)
    {

        if (checkFlush(player) && checkStraight(player))
        {
            return true;
        }
        return false;
    }

    public boolean checkFlush(Player player)
    {
        ArrayList<Card> playerCards = playerHand.getCards();
        String myString = playerCards.get(0).getSuit();

        for (int i = 1; i < 5; i++)
        {
            if (!playerCards.get(i).getSuit().equals(myString))
                return false;
        }
        return true;
    }

    public void replace(Player player, int numberToReplace)
    {
        for (int i = 0; i < numberToReplace; i++)
            dealCardTo(player);
    }


    public void makeBet(Player player)
    {
        try
        {
            bet.makeBet(getDoubleInput("Make a bet"), player);
        } catch (IllegalArgumentException e)
        {
            UserInput.display("Insufficient Funds.");
            play(player);
            return;

        } catch (SecurityException e) {
            Bet.displayMinimumBet();
            play(player);
            return;
        }
    }


    public int discardCards(Player player)
    {
        double numCardstoDiscard;
        do
        {
            numCardstoDiscard = getDoubleInput("How many cards do you want to discard? ");
            if (numCardstoDiscard > 6)
            {
                UserInput.display("You can only discard the card you have");
            }
        } while (numCardstoDiscard > 6);
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < numCardstoDiscard; i++)
        {
            double getDiscard = getDoubleInput("Please enter the index of the card that is to be discarded: ");
            indexes.add((int) getDiscard);
        }
        Collections.sort(indexes);
        //Hand cloneOfHand = playerHand.getCards();
        for (int j = indexes.size() - 1; j >= 0; j--)
        {
            playerHand.remove(indexes.get(j) - 1);
        }
        //player.setHand(cloneOfHand);
        return (int) numCardstoDiscard;
    }

    public void userDisplayHand(Player player)
    {
        StringBuilder outPut = new StringBuilder(1000);
        ArrayList<Card> cards = playerHand.getCards();
        for (int i = 0; i < cards.size(); i++)
        {
            outPut.append("[" + (i + 1) + "]: " + cards.get(i) + "\n");
        }
        UserInput.display(outPut);
    }

}
