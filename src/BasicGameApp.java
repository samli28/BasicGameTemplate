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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//*******************************************************************************

public class BasicGameApp implements Runnable, KeyListener {

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



    Bart bart;
    Image bartImg;
    Image bobImg;
    Image backgroundImg;
    Image endscreenImg;

    boolean firstCrash;
    ArrayList<Bob> bobs = new ArrayList<>();
    boolean pressingKey;
    int bartSpeed;
    int seconds;

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
        bart = new Bart("bart.jpg", 300, 300);
        bartImg = Toolkit.getDefaultToolkit().getImage("bart.jpg");
        bobImg = Toolkit.getDefaultToolkit().getImage("bob.png");
        backgroundImg = Toolkit.getDefaultToolkit().getImage("background1.jpg");
        endscreenImg = Toolkit.getDefaultToolkit().getImage("end screen.jpg");


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
        bart.move();
        for (int x = 0; x < bobs.size(); x++){
            bobs.get(x).move();
        }
        checkCrash();
    }

    public void loadLevel(int level){
        bobs.clear();

        int bobNumber = 0;

        if (level == 1){
            bobNumber = 1;
        }
        else if (level == 2){
            bobNumber = 3;
        }
        else if (level == 3){
            bobNumber = 5;
        }

        for (int x = 0; x < bobNumber; x++){
            bobs.add(new Bob("bob" + x,50,50));
        }
    }

    public void checkCrash(){
        for (int x = 0; x < bobs.size(); x++){
            Bob BOB = bobs.get(x);

            if (bart.rect.intersects(BOB.rect)){

                bart.dx = -bart.dx;
                bart.dy = -bart.dy;

                BOB.dx = -BOB.dx;
                BOB.dy = -BOB.dy;

                bart.health -= 10;
                bart.isAlive=false;
            }
            if (bart.health<=0&&!bart.isAlive){
                bartImg = null;
            }
        }
    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0,0,WIDTH,HEIGHT);

        g.drawImage(backgroundImg, 0, 0, WIDTH, HEIGHT, null);

        g.setFont(new Font("Arial",Font.BOLD,20));
        g.setColor(new Color(255, 255, 0));
        g.drawString("CLICK 1 FOR EASY MODE",20,50);
        g.setColor(new Color(255, 165, 0));
        g.drawString("CLICK 2 FOR MEDIUM MODE",20,75);
        g.setColor(new Color(255, 0, 0));
        g.drawString("CLICK 3 FOR HARD MODE",20,100);
        g.setColor(new Color(0, 255, 234));
        g.drawString("Controls = WASD",20,125);
        g.setColor(new Color(49, 255, 0));
        g.drawString("Shift for Speed Boost",20,150);

        if (bart.health<=0&&!bart.isAlive){
            g.setColor(new Color(255, 255, 255));
            g.drawImage(endscreenImg, 0, 0, WIDTH, HEIGHT, null);
            g.drawString("YOU LOST!",450,400);
        }

        g.setColor(new Color(136, 50, 50));
        g.fillRect(850, 30, bart.health, 15);

        if (bart.isAlive) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                int seconds = 0;

                @Override
                public void run() {
                    seconds++;
                }
            };
            timer.scheduleAtFixedRate(task, 0, 1000);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(new Color(98, 0, 189));
            System.out.println("Time: " + seconds);
        }

        //draw the image
        g.drawImage(bartImg, bart.xpos, bart.ypos, bart.width, bart.height, null);

        for(int x =0; x<bobs.size(); x++){
            Bob BOB = bobs.get(x);
            g.drawImage(bobImg,BOB.xpos,BOB.ypos,115,115,null);
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
        canvas.addKeyListener(this);
        System.out.println("DONE graphic setup");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        pressingKey = true;
        if(e.getKeyCode()==KeyEvent.VK_SHIFT){
            bartSpeed=15;
            bart.dx = -bartSpeed;
            bart.dy = bartSpeed;
        }
        if (e.getKeyCode() == 68) {//right arrow
            bart.dx = 10;
        }
        if (e.getKeyCode() == 87) {//up arrow
            bart.dy = -10;
        }
        if (e.getKeyCode() == 65) {//left arrow
            bart.dx = -10;
        }
        if (e.getKeyCode() == 83) { //down arrow
            bart.dy = 10;
        }
        if (e.getKeyCode() == 49) { // level 1
            System.out.println("Difficulty: EASY");
            loadLevel(1);
        }
        else if (e.getKeyCode() == 50) { // level 2
            System.out.println("Difficulty: MEDIUM");
            loadLevel(2);
        }
        else if (e.getKeyCode() == 51) { // level 3
            System.out.println("Difficulty: HARD");
            loadLevel(3);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressingKey = true;
        if(e.getKeyCode()==KeyEvent.VK_SHIFT){
            bartSpeed=15;
        }
        bart.dx = 0;
        bart.dy = 0;
    }
}
