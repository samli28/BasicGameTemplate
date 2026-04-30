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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.Toolkit;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
//*******************************************************************************

public class BasicGameApp implements Runnable, KeyListener, MouseListener {

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
    Image endScreenImg;

    boolean firstCrash;
    ArrayList<Bob> bobs = new ArrayList<>();
    boolean pressingKey;
    int bartSpeed;
    boolean firstPressed;
    boolean pickLevel;
    boolean showInstructions = false;
    int score;
    boolean startGame = false;
    Clip domer;

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
        domer();
        firstCrash = true;
        bart = new Bart("bart.jpg", 585, 250);
        bartImg = Toolkit.getDefaultToolkit().getImage("bart.jpg");
        bobImg = Toolkit.getDefaultToolkit().getImage("bob.png");
        backgroundImg = Toolkit.getDefaultToolkit().getImage("homepage.jpeg");
        endScreenImg = Toolkit.getDefaultToolkit().getImage("end screen.jpg");


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
        if(bart.isAlive&&startGame){
            score++;
        }
    }

    public void loadBackground(int Background){
        if(Background == 1){
            backgroundImg = Toolkit.getDefaultToolkit().getImage("background1.jpg");
        }
        else if(Background == 2){
            backgroundImg = Toolkit.getDefaultToolkit().getImage("background2.jpg");
        }
    }

    public void loadLevel(int level){
        bobs.clear();
        score = 0;
        startGame = true;
        showInstructions = false;

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
        else if (level == 4){
            bobNumber = 7;
        }
        else if (level == 5){
            bobNumber = 9;
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

                if(domer != null){
                    domer.setFramePosition(0);
                    domer.start();
                }

                bart.health -= 10;
                if(bart.health <= 0){
                    Toolkit.getDefaultToolkit().beep();
                    bart.isAlive=false;
                }

            }
            if (bart.health<=0&&!bart.isAlive){
                bartImg = null;
                for (int i = 0; i < bobs.size(); i++) {
                    BOB.dx = 0;
                    BOB.dy = 0;
                }
            }
        }
    }

    public void domer(){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File ("domer.wav"));
            domer = AudioSystem.getClip();
            domer.open(audioInputStream);
        } catch (Exception e){
            System.out.println("Couldn't load domer sound effect" + e.getMessage());
        }
    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0,0,WIDTH,HEIGHT);

        g.drawImage(backgroundImg, 0, 0, WIDTH, HEIGHT, null);
        if(!startGame){
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.setColor(new Color(255, 255, 255));
            g.drawString("Bart vs Bob(s)", 305, 150);
        }

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(new Color(14, 42, 104));
            g.fillRect(0,750 , 500, 60);
            g.setColor(new Color(0, 246, 154));
            g.drawString("CLICK HERE TO SEE INSTRUCTIONS MENU", 37,782);


        if(showInstructions) {
            g.setColor(new Color(14, 42, 104));
            g.fillRect(0,750 , 500, 60);
            g.setColor(new Color(0, 246, 154));
            g.drawString("CLICK HERE TO SEE INSTRUCTIONS MENU", 37,782);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(new Color(255, 0, 0));
            g.drawString("CLICK 'SPACE BUTTON' TO CLEAR INSTRUCTIONS", 20, 740);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(new Color(255, 255, 0));
            g.drawString("CLICK 1 FOR EASY MODE", 20, 50);
            g.setColor(new Color(255, 165, 0));
            g.drawString("CLICK 2 FOR MEDIUM MODE", 20, 75);
            g.setColor(new Color(255, 0, 0));
            g.drawString("CLICK 3 FOR HARD MODE", 20, 100);
            g.setColor(new Color(50, 13, 13));
            g.drawString("CLICK 4 FOR PRO MODE", 20, 125);
            g.setColor(new Color(0, 0, 0));
            g.drawString("CLICK 5 FOR HACKER MODE", 20, 150);
            g.setColor(new Color(5, 203, 255));
            g.drawString("Controls = WASD", 20, 175);
            g.setColor(new Color(0, 246, 154));
            g.drawString("Shift for Speed Boost", 20, 200);
            g.setColor(new Color(0, 0, 0));
            g.drawString("Click Q for Background 1", 350, 50);
            g.setColor(new Color(0, 0, 0));
            g.drawString("Click E for Background 2", 350, 75);
        }

        if (bart.health<=0&&!bart.isAlive){
            g.setColor(new Color(255, 255, 255));
            g.drawImage(endScreenImg, 0, 0, WIDTH, HEIGHT, null);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("YOU LOST!",400,400);
        }

        g.setColor(new Color(136, 50, 50));
        g.fillRect(850, 30, bart.health, 15);

        //draw the image
        g.drawImage(bartImg, bart.xpos, bart.ypos, bart.width, bart.height, null);

        for(int x =0; x<bobs.size(); x++){
            Bob BOB = bobs.get(x);
            g.drawImage(bobImg,BOB.xpos,BOB.ypos,115,115,null);
        }

        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(new Color(255, 255, 255));
        g.drawString("Score: " + score/33,848,23);

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
        canvas.addMouseListener(this);
        System.out.println("DONE graphic setup");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        pressingKey = true;
        firstPressed = true;

        if(e.getKeyCode()==KeyEvent.VK_SHIFT){//shift button
            bartSpeed=15;
            bart.dx = -bartSpeed;
            bart.dy = bartSpeed;
            pickLevel=false;
        }
        if (e.getKeyCode() == 68) {//right arrow
            bart.dx = 10;
            pickLevel=false;
        }
        if (e.getKeyCode() == 87) {//up arrow
            bart.dy = -10;
            pickLevel=false;
        }
        if (e.getKeyCode() == 65) {//left arrow
            bart.dx = -10;
            pickLevel=false;
        }
        if (e.getKeyCode() == 83) { //down arrow
            bart.dy = 10;
            pickLevel=false;
        }
        if (e.getKeyCode() == 49) { // level 1
            System.out.println("Difficulty: EASY");
            loadLevel(1);
            pickLevel=false;
        }
        else if (e.getKeyCode() == 50) { // level 2
            System.out.println("Difficulty: MEDIUM");
            loadLevel(2);
            pickLevel=false;
        }
        else if (e.getKeyCode() == 51) { // level 3
            System.out.println("Difficulty: HARD");
            loadLevel(3);
            pickLevel=false;
        }
        else if (e.getKeyCode() == 52) { // level 4
            System.out.println("Difficulty: PRO");
            loadLevel(4);
            pickLevel=false;
        }
        else if (e.getKeyCode() == 53) { // level 5
            System.out.println("Difficulty: HACKER");
            loadLevel(5);
            pickLevel=false;
        }
        else if (e.getKeyCode() == 81) { // q button
            System.out.println("Background 1");
            loadBackground(1);
            pickLevel=false;
        }
        else if (e.getKeyCode() == 69) { // e button
            System.out.println("Background 2");
            loadBackground(2);
            pickLevel=false;
        }
        else if (e.getKeyCode() == 32) { // space button
            System.out.println("Instructions Menu Cleared, Time to Play!");
            pickLevel = true;
            showInstructions = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressingKey = true;
        if(e.getKeyCode()==KeyEvent.VK_SHIFT){
            bartSpeed=20;
        }
        bart.dx = 0;
        bart.dy = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getY() >= 750 && e.getX() <= 500){
            firstPressed = true;
            pickLevel = false;
            showInstructions = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
