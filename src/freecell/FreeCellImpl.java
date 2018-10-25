package freecell;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class FreeCellImpl implements Serializable {
  private static final long serialVersionUID = 6033262243162979644L;
  private List<Card>[] cascadePile;
  private ArrayList<Card>[] foundationPile = new ArrayList[4];
  private Card[] openPile;
  private HashMap<Integer,String> house = new HashMap<>();

  public List<Card> getDeck()
  {
    Deck deck = Deck.getDeck();
    return deck.getDeckOfCards();
  }

  public void startGame(List<Card> deck, boolean shuffle)
  {
    if(shuffle == true)
    {
      deck = suffleCards(new ArrayList<>(deck));
    }

    System.out.println(deck);

    openPile = new Card[4];
    cascadePile = new ArrayList[8];

    distributeCards(deck, cascadePile.length);

    printGame();


  }

  /**
   * To be deleted!!
   */
  public void relodeGame()
  {
    System.out.println("*****>> house: "+house);
    printGame();
  }


  public void move(PileType sourceType, int sourcePileNumber, int cardIndex, PileType destinationType, int destPileNumber){


    if(sourceType.name().equals(PileType.CASCADE.name())&&destinationType.name().equals(PileType.CASCADE.name()))
      moveCascadeToCascade(sourcePileNumber,cardIndex,destPileNumber);
    if(sourceType.name().equals(PileType.CASCADE.name())&&destinationType.name().equals(PileType.OPEN.name()))
      moveCascadeToOpen(sourcePileNumber,cardIndex);
    if(sourceType.name().equals(PileType.OPEN.name())&&destinationType.name().equals(PileType.CASCADE.name()))
      moveOpenToCascade(cardIndex,destPileNumber);
    if(sourceType.name().equals(PileType.CASCADE.name())&&destinationType.name().equals(PileType.FOUNDATION.name()))
      moveCascadeToFoundation(sourcePileNumber,cardIndex,destPileNumber);
    if(sourceType.name().equals(PileType.OPEN.name())&&destinationType.name().equals(PileType.FOUNDATION.name()))
      moveOpenToFoundation(cardIndex,destPileNumber);

    printGame();

  }

  private void moveOpenToFoundation(int cardIndex, int destPileNumber) {
    Card movingCard = openPile[cardIndex];

    //System.out.println(house.get(destPileNumber)+" ........  "+ movingCard.getCardSuit());
    //System.out.println(foundationPile[destPileNumber].get(foundationPile[destPileNumber].size()-1).getCardNumber()+"  ........  "+ (movingCard.getCardNumber()-1));

    if(house.get(destPileNumber).equals(movingCard.getCardSuit())  && foundationPile[destPileNumber].get(foundationPile[destPileNumber].size()-1).getCardNumber() == movingCard.getCardNumber()-1)
    {

      foundationPile[destPileNumber].add(movingCard);
      openPile[cardIndex] = null;
    }
    else
    {
      System.out.println("Sorry cannot be moved to foundation pile!");
    }
  }

  private void moveCascadeToFoundation(int sourcePileNumber, int cardIndex, int destPileNumber) {

    Card moveCard = cascadePile[sourcePileNumber].get(cardIndex);
    //System.out.println("hey i am here in move c to f!!");
    if(cascadePile[sourcePileNumber].size()-1 != cardIndex)
    {
      System.out.println("Trying to insert ACE from the middle.");
      return;
    }

    if(foundationPile[destPileNumber] == null)
    {

      if(moveCard.getCardNumber() == 1) {
        house.put(destPileNumber,moveCard.getCardSuit());
        //System.out.println(house);
        ArrayList<Card> cardList = new ArrayList<>();
        cardList.add(moveCard);
        foundationPile[destPileNumber] = cardList;
        cascadePile[sourcePileNumber].remove(cardIndex);
      }
      else{
        System.out.println("Cannot place card other than Ace");
      }

    }
    else
    {

      //System.out.println(house.get(destPileNumber)+" ----> "+moveCard.getCardSuit());
      if(house.get(destPileNumber).equals(moveCard.getCardSuit()))
      {
        // proceed to add


        if(foundationPile[destPileNumber].get(foundationPile[destPileNumber].size()-1).getCardNumber() == moveCard.getCardNumber()-1)
        {
          foundationPile[destPileNumber].add(moveCard);
          cascadePile[sourcePileNumber].remove(cardIndex);
        }
        else {
          System.out.println("The Card cannot be placed in foundation pile.");
        }
      }
      else
      {
        System.out.println("Sorry different house!");
      }
    }
  }

  private void moveOpenToCascade(int cardIndex,int destPileNumber) {
    Card card = openPile[cardIndex];

    if(cascadePile[destPileNumber].size() == 0)
    {
      cascadePile[destPileNumber].add(card);
      openPile[cardIndex] = null;
    }
    else {
      Card card1 = cascadePile[destPileNumber].get(cascadePile[destPileNumber].size() - 1);

      System.out.println(card + " --->> " + card1);
      if (checkTheCardsCanBeMoved(card, card1)) {
        cascadePile[destPileNumber].add(card);
        openPile[cardIndex] = null;
      } else {
        System.out.println("cannot make a move");
      }
    }

  }

  private void moveCascadeToOpen(int sourceCascadePileNumber,int cardIndex)
  {
    Integer index = -1;
    for(int i=0;i<openPile.length;i++)
    {
      if(openPile[i] == null)
      {
        index = i;
        break;
      }

    }

    if(index != -1) {
      //fix this it can insert from the middle of the pile.
      if(cascadePile[sourceCascadePileNumber].size()-1 == cardIndex) {
        openPile[index] = cascadePile[sourceCascadePileNumber].get(cardIndex);
        cascadePile[sourceCascadePileNumber].remove(cardIndex);
      }
      else
      {
        System.out.println("Trying to insert from the middle!!!");
      }
    }
    else
    {
      System.out.println("The open pile is full!");
    }
  }

  private void moveCascadeToCascade(int sourceCascadePileNumber,int cardIndex, int destCascadePileNumber){
    List<Card> movingCardList = cascadePile[sourceCascadePileNumber].subList(cardIndex,cascadePile[sourceCascadePileNumber].size());

    if(movingCardList.size() >1)
    {
      System.out.println(" trying to move multiple cards!");
      return;
    }

    if(!isMovingCardValid(movingCardList))
    {
      System.out.println("the moving card is invalid!!");
      return;
    }

    System.out.println(movingCardList);
    System.out.println("destinationPileSize: ----->>>> "+cascadePile[destCascadePileNumber].size());


    if(cascadePile[destCascadePileNumber].size() ==0)
    {
      System.out.println("trying to move to the empty destination pile!!!!!!!!!!!!!!!!!!!!!");
      if(checkIfCardIsInOrder(movingCardList)) {
        cascadePile[destCascadePileNumber].addAll(movingCardList);
        cascadePile[sourceCascadePileNumber].subList(cardIndex, cascadePile[sourceCascadePileNumber].size()).clear();
      }
      else {
        System.out.println("the cards cannot be moved!!");
      }
    }

    else if(cascadePile[destCascadePileNumber].size() !=0) {
      Card dest = cascadePile[destCascadePileNumber].get(cascadePile[destCascadePileNumber].size() - 1);

      if (checkTheCardsCanBeMoved(movingCardList, dest)) {
        cascadePile[destCascadePileNumber].addAll(movingCardList);
        cascadePile[sourceCascadePileNumber].subList(cardIndex, cascadePile[sourceCascadePileNumber].size()).clear();

      } else {
        System.out.println("the cards cannot be moved!!");
      }
    }

  }

  private boolean isMovingCardValid(List<Card> movingCardList) {

    ArrayList<Card> sortedArray = new ArrayList<>(movingCardList);
    Collections.sort(sortedArray, Collections.reverseOrder());
    //check for suits aswell.
    for(int i=0;i<movingCardList.size();i++)
    {
      if(sortedArray.get(i).getCardNumber() != movingCardList.get(i).getCardNumber()){
        return false;
      }
    }

    return true;
  }

  private boolean checkTheCardsCanBeMoved(List<Card> moveList, Card card1) {

    if(checkIfCardIsInOrder(moveList)) {
      Card card = moveList.get(0);
      System.out.println("the card number is: " + card.getCardNumber() + " the card 2 number is " + card1.getCardNumber());
      System.out.println("the card suit is: " + card.getCardColor() + " the card 2 suit is " + card1.getCardColor());
      if (card.getCardNumber() == card1.getCardNumber() - 1 && !card.getCardColor().equals(card1.getCardColor())) {

        return true;

      }
    }

    //fix this... check all the cards after the index with the last card of the destination. not just the first card of the sublist.

    return false;

  }

  private boolean checkTheCardsCanBeMoved(Card card, Card card1) {

    if(card.getCardNumber() == card1.getCardNumber()-1 && !card.getCardColor().equals(card1.getCardColor()))
    {
        return true;
    }

    //fix this... check all the cards after the index with the last card of the destination. not just the first card of the sublist.

    return false;

  }

  private boolean checkIfCardIsInOrder(List<Card> moveList) {

    if(moveList.size() == 1)
    {
      return true;
    }
    else {
      for (int i = 0; i < moveList.size() - 1; i++) {
        if (moveList.get(i).getCardNumber() - 1 != moveList.get(i + 1).getCardNumber()) {
          return false;
        }
      }
      return true;
    }
  }


  private List<Card> suffleCards(List<Card> deckOfCards)
  {

   List<Card> suffledDeck = deckOfCards;
    Random rand = new Random();

    for (int i = deckOfCards.size() - 1; i > 0; i--)
    {
      int n = rand.nextInt(i + 1);
      Card temp = suffledDeck.get(i);
      suffledDeck.set(i, suffledDeck.get(n));
      suffledDeck.set(n, temp);
    }

    return suffledDeck;
  }

  public void distributeCards(List<Card> shuffledCards, Integer numberOfPiles)
  {

    int pile = numberOfPiles;
    for (Card card: shuffledCards) {
      int num = pile--;

      List<Card> cardPile = cascadePile[numberOfPiles-num];
      if(cardPile == null)
      {
        cardPile = new ArrayList<Card>();
      }
      cardPile.add(card);
      cascadePile[numberOfPiles-num] = cardPile;

      if(pile == 0)
      {
        pile = numberOfPiles;
      }

    }

  }

  private void printGame()
  {
    System.out.println("**********************************    OPEN PILE    *******************************\t\t");
    for (int i =0;i<openPile.length;i++ ) {
      System.out.print(openPile[i] + "\t");
    }
    System.out.println();
    System.out.println("********************************** FOUNDATION PILE *******************************");
    for (int i =0;i<foundationPile.length;i++ ) {
      System.out.print(foundationPile[i] + "\t");
    }
    System.out.println();
    System.out.println("**********************************  CASCADE PILE   *******************************");
    for (int i =0;i<cascadePile.length;i++ ) {
      System.out.println(i+" "+cascadePile[i]);
    }
  }


}
