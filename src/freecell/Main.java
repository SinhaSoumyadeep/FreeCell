package freecell;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

  public static void main(String [] args)
  {

    System.out.println("New Game? : Y/N");
    Scanner scanner = new Scanner(System.in);
    String ans = scanner.nextLine();

    if(ans.equalsIgnoreCase("y"))
    {
      FreeCellImpl fc = new FreeCellImpl();

      List<Card> sortedCards = new ArrayList<>(fc.getDeck());

      Collections.sort(sortedCards,Collections.reverseOrder());
      fc.startGame(fc.getDeck(), true);
      //fc.move(PileType.CASCADE,1,5,PileType.CASCADE,2);
      makeMove(fc);
    }
    else {
      FreeCellImpl fc  = retrieveGame();
      fc.relodeGame();
      makeMove(fc);
    }


  }

  public static void makeMove(FreeCellImpl fc)
  {
    try {
      while (true) {
        System.out.println("Enter Source Pile: ");
        Scanner scanner = new Scanner(System.in);
        String srcPile = scanner.nextLine();
        System.out.println("Enter Source: ");
        Scanner scanner1 = new Scanner(System.in);
        String source = scanner.nextLine();
        System.out.println("Enter Card Index: ");
        Scanner scanner2 = new Scanner(System.in);
        String cardIndex = scanner.nextLine();
        System.out.println("Enter Destination Pile: ");
        Scanner scanner3 = new Scanner(System.in);
        String destination = scanner.nextLine();
        System.out.println("Enter Destination Number: ");
        Scanner scanner4 = new Scanner(System.in);
        String dest = scanner.nextLine();

        PileType src = ret(srcPile);
        PileType dst = ret(destination);


        fc.move(src, Integer.parseInt(source), Integer.parseInt(cardIndex), dst, Integer.parseInt(dest));

        saveGame(fc);


      }

    }
    catch (Exception e)
    {
      System.out.println("*********************   try again!   ********************");
      e.printStackTrace();
      makeMove(fc);
    }

  }

  public static PileType ret(String srcStr)
  {
    PileType src = null;
    switch (srcStr) {
      case "c":
        src = PileType.CASCADE;
        break;
      case "f":
        src = PileType.FOUNDATION;
        break;
      case "o":
        src = PileType.OPEN;
        break;
        default:
          src = PileType.CASCADE;
    }

    return src;
  }


  public static void saveGame(FreeCellImpl fc)
  {
    String filename = "..\\..\\savedFile\\gamedata";
    // Serialization
    try
    {
      //Saving of object in a file
      FileOutputStream file = new FileOutputStream(filename);
      ObjectOutputStream out = new ObjectOutputStream(file);

      // Method for serialization of object
      out.writeObject(fc);

      out.close();
      file.close();

      System.out.println("Object has been serialized");

    }

    catch(IOException ex)
    {
      ex.printStackTrace();
      System.out.println("IOException is caught");
    }
  }

  public static FreeCellImpl retrieveGame()
  {
    FreeCellImpl savedObject = null;
    String filename = "..\\..\\savedFile\\gamedata";
    // Deserialization
    try
    {
      // Reading the object from a file
      FileInputStream file = new FileInputStream(filename);
      ObjectInputStream in = new ObjectInputStream(file);

      // Method for deserialization of object
      savedObject = (FreeCellImpl) in.readObject();

      in.close();
      file.close();

      return savedObject;

    }

    catch(IOException ex)
    {
      System.out.println("IOException is caught");
      ex.printStackTrace();
      System.exit(0);
    }

    catch(ClassNotFoundException ex)
    {
      System.out.println("ClassNotFoundException is caught");
      ex.printStackTrace();
      System.exit(0);
    }

    return savedObject;

  }

}
