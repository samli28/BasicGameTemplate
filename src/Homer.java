import java.awt.*;

/**
 * Created by chales on 11/6/2017.
 * Edits by mblair on 10/27/2025
 */
public class Homer {

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
    public double successRate;
    public int health;

    //This is a constructor that takes 3 parameters.
    // This allows us to specify the hero's name and position when we build it.
    public Homer(String pName, int pXpos, int pYpos, double psuccess) {
        name = pName;
        xpos = pXpos;
        ypos = pYpos;
        dx = 5;
        dy = 5;
        width = 100;
        height = 100;
        isAlive = true;
        rect = new Rectangle(xpos, ypos, width, height);
        successRate = psuccess;
        health = 100;
    }

    public void move() { // move
        xpos = xpos + dx;
        ypos = ypos + dy;
        rect = new Rectangle(xpos, ypos, width, height);
    }

    public void bounce() {
        xpos = xpos + dx;
        ypos = ypos + dy;
        rect = new Rectangle(xpos, ypos, width, height);

        if (xpos >= 1000 - width) {
            dx = -dx;
        }
        else if (xpos <= 0) {
            dx = -dx;
        }
        if (ypos >= 800-height) {
            dy = -dy;
        }
        else if (ypos <= 0) {
            dy = -dy;
        }
    }


    public void wrap() {
        xpos = xpos + dx;
        ypos = ypos + dy;
        rect = new Rectangle(xpos, ypos, width, height);

        if (xpos > 1000){
            xpos = 0;
        }

        else if(xpos < 0){
            xpos = 1000;
        }

        if(ypos > 800) {
            ypos = 0;
        }

        else if(ypos < 0){
            ypos = 800;
        }



    }
}







