package topcoder.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
BridgeSort

  A deck of cards contains 52 cards. Each card has a suit (Clubs, Diamonds, Hearts, or Spades) and a value (2, 3, ..., 9, 10, Jack, Queen, King, or Ace).

  In the game of bridge a hand consists of 13 or fewer cards from the deck. The values of the cards are ordered as shown above, with Ace having the highest value. Suppose that hand is given as a String giving the cards in the hand. Each card is represented by a suit character C, D, H, or S followed by a value character 2, 3, ..., 9, T, J, Q, K, or A. There are no spaces separating adjacent cards in hand.

  Create a class BridgeSort that contains a method sortedHand that is given a String hand and that returns the String that represents the hand in sorted order. The proper order is to list all the cards that are Clubs, then all the Diamonds, then all the Hearts, and finally all the Spades, with the cards within each suit listed in order of ascending value.
 */
public class BridgeSort {

  public String sortedHand(String hand) {
    char[] suitOrder = { 'C', 'D', 'H', 'S' };

    Map<Character, Integer> valueRank = new HashMap<>();
    String values = "23456789TJQKA";
    for (int i = 0; i < values.length(); i++) {
      valueRank.put(values.charAt(i), i);
    }

    Map<Character, List<String>> suitToCards = new HashMap<>();
    for (char suit : suitOrder) {
      suitToCards.put(suit, new ArrayList<>());
    }

    for (int i = 0; i < hand.length(); i += 2) {
      char suit = hand.charAt(i);
      char value = hand.charAt(i + 1);
      String card = "" + suit + value;
      suitToCards.get(suit).add(card);
    }

    for (List<String> cards : suitToCards.values()) {
      cards.sort(Comparator.comparingInt(
          card -> valueRank.get(card.charAt(1))));
    }

    StringBuilder sorted = new StringBuilder();
    for (char suit : suitOrder) {
      for (String card : suitToCards.get(suit)) {
        sorted.append(card);
      }
    }

    return sorted.toString();
  }

  public static void main(String[] args) {
    BridgeSort sort = new BridgeSort();
    System.out.println(sort.sortedHand("HAH2H3C4D5ST"));
    System.out.println(sort.sortedHand("H3SAHA"));
    System.out.println(sort.sortedHand("C2"));
    System.out.println(sort.sortedHand("C2C3C4C5C6C7C8C9CTCJCQCKCA"));
    System.out.println(sort.sortedHand("HASADACA"));
    System.out.println(sort.sortedHand("SAS2S3S4S5S6S7S8S9STSJSQSK"));
    System.out.println(sort.sortedHand("DAS2S3S4S5S6S7S8S9STSJSQSK"));

    System.out.println(sort.sortedHand("HTH9H8H7C3S6S9DASAHACA"));
  }

}
