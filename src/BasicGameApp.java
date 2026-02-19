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
    Image bobImg1;

    boolean firstCrash;
    Bob [] bobShower = new Bob[5];

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
        bart1 = new Bart("bart1.jpg", 300, 300);
        bartImg1 = Toolkit.getDefaultToolkit().getImage("bart.jpg");
        bobImg1 = Toolkit.getDefaultToolkit().getImage("bob.png");
        for (int x = 0; x < bobShower.length; x++){
            bobShower[x]=new Bob ("bob"+x,((int)(Math.random()*WIDTH)),((int)(Math.random()*HEIGHT)));
        }


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
            render();  // paint the graphics
            pause(30); // sleep for 10 ms
        }
    }

    public void moveThings() {
        bart1.move();
        for (int x = 0; x < bobShower.length; x++){
            bobShower[x].move();
        }
        checkCrash();
    }

    public void checkCrash(){
        for (int x = 0; x < bobShower.length; x++){

            if (bart1.rect.intersects(bobShower[x].rect)){

                bart1.dx = -bart1.dx;
                bart1.dy = -bart1.dy;

                bobShower[x].dx = -bobShower[x].dx;
                bobShower[x].dy = -bobShower[x].dy;

                bart1.health -= 50;
                bart1.isAlive = false;
            }
            else if (!bart1.rect.intersects(bobShower[x].rect)&& !firstCrash){
                bart1.dx=0;
                bart1.dy=0;
            }
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


        for(int x =0; x<bobShower.length; x++){
            g.drawImage(bobImg1,bobShower[x].xpos,bobShower[x].ypos,bobShower[x].width,bobShower[x].height,null);
        }
        bufferStrategy.show();
        g.dispose();
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
