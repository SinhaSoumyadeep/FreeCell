package freecell;

import java.io.Serializable;
import java.util.HashMap;

public final class Card implements Comparable, Serializable {

  private final Suits suit;
  private final Numbers number;

  private static final HashMap<String, String> symbol = new HashMap<String, String>(){
    {
      put("DIAMONDS","\u25C6");
      put("HEARTS","\u2764");
      put("CLUBS","\u2663");
      put("SPADES","\u2660");

    }
  };

  private static final HashMap<String, String> numbers = new HashMap<String, String>(){
    {
      put("ACE","A");
      put("KING","K");
      put("QUEEN","Q");
      put("JACK","J");
      put("TEN","10");
      put("NINE","9");
      put("EIGHT","8");
      put("SEVEN","7");
      put("SIX","6");
      put("FIVE","5");
      put("FOUR","4");
      put("THREE","3");
      put("TWO","2");



    }
  };

  public Card(Suits suit, Numbers number)
  {
    this.suit = suit;
    this.number = number;
  }

  public Integer getCardNumber()
  {
    if(numbers.get(number.name()).equals("A"))
    {
      return 1;
    }
    else if(numbers.get(number.name()).equals("K"))
    {
      return 13;
    }
    else if(numbers.get(number.name()).equals("Q"))
    {
      return 12;
    }
    else if(numbers.get(number.name()).equals("J"))
    {
      return 11;
    }
    else {
      return Integer.parseInt(numbers.get(number.name()));
    }
  }

  public String getCardColor()
  {
    if(this.suit.name().equals("DIAMONDS")||this.suit.name().equals("HEARTS"))
    {
      return "RED";
    }
    else{
      return "BLACK";
    }
  }

  public String getCardSuit()
  {
    return this.suit.name();
  }

  public String toString()
  {
   /* return "\n" +
            "|"+numbers.get(number)+"\t\t|\n" +
            "|  \t"+symbol.get(suit)+"\t|\n" +
            "|_______|";*/
   return numbers.get(number.name())+" "+symbol.get(suit.name());
  }


  @Override
  public int compareTo(Object o) {
    if(o instanceof Card)
    {
      Card obj = (Card) o;
      if(this.getCardNumber()>((Card) o).getCardNumber())
      {
        return 1;
      }
      if(this.getCardNumber()==((Card) o).getCardNumber())
      {
        return 0;
      }
      else{
        return -1;
      }
    }
    return -1;
  }
}
