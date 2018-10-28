package freecell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;



public class Deck {

  private final ArrayList<Card> deckOfCards;
  private static Deck instance = null;

  private Deck() {
    deckOfCards = new ArrayList<>();
    for (Suits suit : Suits.values()) {
      for (Numbers number : Numbers.values()) {
        Card card = new Card(suit, number);
        deckOfCards.add(card);
      }
    }
  }


  public static Deck getDeck(){
    if (instance == null) {
      synchronized(Deck.class) {
        if (instance == null) {
          instance = new Deck();
        }
      }
    }

    return instance;
  }



  public List<Card> getDeckOfCards(){
    return Collections.unmodifiableList(deckOfCards);
  }


}
