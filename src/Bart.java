import java.awt.*;

/**
 * Created by chales on 11/6/2017.
 * Edits by mblair on 10/27/2025
 */
public class Bart {

    //VARIABLE DECLARATION SECTION
    //Here's where you state which variables you are going to use.
    public String name;               //name of the hero
    public int xpos;                  //the x position
    public int ypos;                  //the y position
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public int width;                 //the width of the hero image
    public int height;                //the height of the hero image
    public boolean isAlive;           //a boolean to denote if the hero is alive or dead
    public Rectangle rect;
    public int health;

    //This is a constructor that takes 3 parameters.
    // This allows us to specify the hero's name and position when we build it.
    public Bart(String pName, int pXpos, int pYpos) {
        name = pName;
        xpos = pXpos;
        ypos = pYpos;
        dx = 0;
        dy = 0;
        width = 100;
        height = 100;
        isAlive = true;
        rect = new Rectangle(xpos, ypos, width, height);
        health = 100;
    }

    public void move() {
        xpos = xpos + dx;
        ypos = ypos + dy;
        rect = new Rectangle(xpos, ypos, width, height);

        if (xpos >= 1000 - width) {
            xpos = 0;
        }
        else if (xpos <= 0) {
            xpos = 1000-width;
        }
        if (ypos >= 800-height) {
            ypos = 0;
        }
        else if (ypos <= 0) {
            ypos = 800-height;
        }
    }
}






