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

    boolean firstCrash;
    boolean level1;
    boolean level2;
    boolean level3;
    Bob [] bobNumber = new Bob[5];
    boolean pressingKey;
    boolean background1;
    boolean background2;

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
        backgroundImg = Toolkit.getDefaultToolkit().getImage("background.jpg");
        for (int x = 0; x < bobNumber.length; x++){
            bobNumber[x]=new Bob ("bob"+x,((int)(Math.random()*WIDTH)),((int)(Math.random()*HEIGHT)));
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
        bart.move();
        for (int x = 0; x < bobNumber.length; x++){
            bobNumber[x].move();
        }
        checkCrash();
    }

    public void checkCrash(){
        for (int x = 0; x < bobNumber.length; x++){

            if (bart.rect.intersects(bobNumber[x].rect)){

                bart.dx = -bart.dx;
                bart.dy = -bart.dy;

                bobNumber[x].dx = -bobNumber[x].dx;
                bobNumber[x].dy = -bobNumber[x].dy;

                bart.health -= 10;
                bart.isAlive = false;
            }
            else if (bart.health<=0&&!bart.isAlive){
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
        g.setColor(new Color(0, 0, 0));
        g.drawString("For LEVEL 1, click 1 on your keyboard",20,50);

        g.setColor(new Color(214, 0, 0));
        g.fillRect(850, 30, bart.health, 15);
        //draw the image
        g.drawImage(bartImg, bart.xpos, bart.ypos, bart.width, bart.height, null);

        for(int x =0; x<bobNumber.length; x++){
            g.drawImage(bobImg,bobNumber[x].xpos,bobNumber[x].ypos,bobNumber[x].width,bobNumber[x].height,null);
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
        if (e.getKeyCode() == 68) {//right arrow
            bart.dx=10;
        }
        if (e.getKeyCode() == 87) {//up arrow
            bart.dy=-10;
        }
        if (e.getKeyCode() == 65) {//left arrow
            bart.dx=-10;
        }
        if (e.getKeyCode() == 83) { //down arrow
            bart.dy=10;
        }

        if (e.getKeyCode() == 49) { // level 1
            System.out.println("LEVEL 1");
        }
        if (level1) { // level 1
            Bob [] bobNumber = new Bob[1];
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 68) { //right arrow
            bart.dy = 0;
        }
        if (e.getKeyCode() == 65) { //left arrow
            bart.dy = 0;
        }
        if (e.getKeyCode() == 83) { //down arrow
            bart.dx = 0;
        }
        if (e.getKeyCode() == 87) {//up arrow
            bart.dx = 0;
        }
        bart.dx = 0;
        bart.dy = 0;
    }
}
