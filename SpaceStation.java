///////////////////////////////////////////////////////////////////////////////
// Title: P02_Among_Us
// Course: CS 300 Fall 2023
//
// Author: Iris Xu
// Lecturer: Mark Mansi

///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons: None
// Online Sources: https://www.geeksforgeeks.org/find-two-rectangles-overlap/
//

import java.io.File;
import processing.core.PImage;

/**
 * This class implements a Space station where could conducts an Among Us game
 * 
 * @author Iris Xu
 *
 */

public class SpaceStation {
  private static PImage background; // background
  private static Amogus[] players; // a array contains all the players
  private static final int NUM_PLAYERS = 8; // constant of numbers of players
  private static int impostorIndex; // the index of impostor

  /**
   * Main method that launches this game application
   * 
   * @param args list of input arguments if any
   */
  public static void main(String[] args) {
    Utility.runApplication(); // starts the application
  }

  /**
   * called automatically when the program begins. All data field initialization should happen here,
   * any program configuration actions.
   */
  public static void setup() {
    background = Utility.loadImage("images" + File.separator + "background.jpeg"); // initialize
                                                                                   // background
    players = new Amogus[NUM_PLAYERS]; // initialize number of players
    Amogus player1 = new Amogus(Utility.randGen.nextInt(3) + 1); // initialize the first player
    players[0] = player1; // assign the first player to the array of players

    impostorIndex = Utility.randGen.nextInt(NUM_PLAYERS - 1) + 1; // initialize impostor index
    System.out.println("Impostor index: " + impostorIndex);
  }

  /**
   * runs continuously as long as the application window is open. Draws the window and the current
   * state of its contents to the screen.
   */
  public static void draw() {
    Utility.image(background, 600, 500);// draw background

    for (int i = 0; i < players.length; i++) {
      if (players[i] != null) {
        players[i].draw(); // draw non-null players

        // if two non-null players overlap and one is impostor, unalive the other
        for (int j = 0; j < players.length; j++) {
          if (i != j && players[j] != null && overlap(players[i], players[j])
              && players[i].isImpostor()) {
            players[j].unalive();
          }
        }
      }
    }
  }

  /**
   * called automatically whenever 'a' key is pressed. create new Amogus object and assign it to the
   * array of players
   */
  public static void keyPressed() {
    char enter = Utility.key(); // get the key that pressed
    if (enter == 'a') {
      int locX = Utility.randGen.nextInt(Utility.width() + 1); // initialize x coordinate
      int locY = Utility.randGen.nextInt(Utility.height() + 1); // initialize y coordinate
      int color = Utility.randGen.nextInt(3) + 1; // initialize color

      // use the randomly initialized value to create a Amogus object and save it to players[]
      for (int i = 0; i < players.length; i++) {
        if (players[i] == null) {
          if (i == impostorIndex) // the amogus is the impostor if its index equals to impostor
                                  // index
            players[i] = new Amogus(color, locX, locY, true);
          else
            players[i] = new Amogus(color, locX, locY, false);
          break;
        }
      }
    }
  }

  /**
   * determine whether the mouse is currently hovering over any part of one of the Amogus image
   * 
   * @param player an Amogus object
   * @return true if the mouse hovering the Amogus image
   */
  public static boolean isMouseOver(Amogus player) {
    int amogusW = player.image().width; // width of Amogus image
    int amogusH = player.image().height; // height of Amogus image
    boolean check = true; // assume the condition is true

    // the x and y range of the Amogus image on the screen
    double minX = player.getX() - amogusW / 2;
    double maxX = player.getX() + amogusW / 2;
    double minY = player.getY() - amogusH / 2;
    double maxY = player.getY() + amogusH / 2;

    // check whether the mouse exceed the range of the x and y coordinates
    if (Utility.mouseX() <= minX || Utility.mouseX() >= maxX || Utility.mouseY() <= minY
        || Utility.mouseY() >= maxY) {
      check = false; // set to false if mouse exceed the range
    }
    return check;
  }

  /**
   * allow the Amogus image to be dragged around the screen
   */
  public static void mousePressed() {
    for (int i = 0; i < players.length; i++) {
      // the image is able to drag if it is non-null and mouse is hovering it
      if (players[i] != null && isMouseOver(players[i])) {
        System.out.println("Mouse is over amogus");
        players[i].startDragging();
      }
    }
  }

  /**
   * stop dragging if the mouse release
   */
  public static void mouseReleased() {
    for (int i = 0; i < players.length; i++) {
      if (players[i] != null) {
        players[i].stopDragging();
      }
    }
  }

  /**
   * determine whether one Amogus image is overlapping the other
   * 
   * @param player1 a Amogus object
   * @param player2 a Amogus object
   * @return true if player1 overlap player2
   */
  public static boolean overlap(Amogus player1, Amogus player2) {
    int amogusW = player1.image().width; // width of Amogus image
    int amogusH = player1.image().height; // height of Amogus image

    // x and y coordinates of player1 and player2
    float x1 = player1.getX(); // x coordinate of player1
    float y1 = player1.getY(); // y coordinate of player1
    float x2 = player2.getX(); // x coordinate of player2
    float y2 = player2.getY(); // y coordinate of player2

    // find the top left coordinates and down right coordinate of player1 and player2
    float topLeftX1 = x1 - amogusW / 2;
    float topLeftY1 = y1 - amogusH / 2;
    float downRightX1 = x1 + amogusW / 2;
    float downRightY1 = y1 + amogusH / 2;

    float topLeftX2 = x2 - amogusW / 2;
    float topLeftY2 = y2 - amogusH / 2;
    float downRightX2 = x2 + amogusW / 2;
    float downRightY2 = y2 + amogusH / 2;

    // return true if the two players overlap
    if (topLeftX1 <= downRightX2 && topLeftX2 <= downRightX1 && downRightY1 >= topLeftY2
        && downRightY2 >= topLeftY1) {
      return true;
    }
    // return false otherwise
    return false;
  }

}
