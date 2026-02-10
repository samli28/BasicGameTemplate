//Basic Game Application
// Basic Object, Image, Movement
// Threaded

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import sun.print.BackgroundLookupListener;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

//*******************************************************************************

public class BasicGameApp implements Runnable {

    //Variable Definition Section
    //Declare the variables used in the program
    //You can set their initial values too

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 800;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;


    Bart bart1;
    Image bartImg1;
    Bob bob1;
    Image bobImg1;

    boolean firstCrash;

    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }


    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public BasicGameApp() { // BasicGameApp constructor

        setUpGraphics();
        firstCrash = true;
        bart1 = new Bart("bart1.jpg", 300, 300, 0.25);
        bartImg1 = Toolkit.getDefaultToolkit().getImage("bart.jpg");
        bob1 = new Bob("bob1.jpg", 500, 200,0.75);
        bobImg1 = Toolkit.getDefaultToolkit().getImage("bob.png");
        run();

    } // end BasicGameApp constructor


//*******************************************************************************
//User Method Section
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        //for the moment we will loop things forever.
        while (true) {
            moveThings();  //move all the game objects
            if (bart1.isAlive == false) {
                bart1.width = bart1.width + 10;
                bart1.height = bart1.height + 10;
            }
            if(bart1.width > 1000){

            }
            render();  // paint the graphics
            pause(30); // sleep for 10 ms
        }
    }

    public void moveThings() {
        bart1.move();
        bob1.move();

        checkCrash();
    }

    public void checkCrash(){
        if (bart1.rect.intersects(bob1.rect) && firstCrash == true){
            firstCrash = false;
            bart1.dx = -bart1.dx;
            bart1.dy = -bart1.dy;
            bob1.dx = -bob1.dx;
            bob1.dy = -bob1.dy;
            bob1.height += 5;
            bob1.width += 5;
            bart1.health = bart1.health - 5;
        }
        if (!bart1.rect.intersects(bob1.rect)){
            firstCrash = true;
        }
    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0,0,WIDTH,HEIGHT);

        g.setColor(new Color(120,150,200));
        g.fillRect(850, 30, bart1.health, 15);
        //draw the image
        g.drawImage(bartImg1, bart1.xpos, bart1.ypos, bart1.width, bart1.height, null);
        g.drawImage(bobImg1, bob1.xpos, bob1.ypos, bob1.width, bob1.height, null);

        g.dispose();
        bufferStrategy.show();
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time ) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");
    }

}
