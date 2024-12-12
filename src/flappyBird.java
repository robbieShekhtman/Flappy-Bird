import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

// Main class for the Flappy Bird game
public class flappyBird extends JPanel implements ActionListener, KeyListener {

    // Game dimensions
    int gameWidth = 360;
    int gameHeight = 640;

    // Game images
    Image back;
    Image birdImg;
    Image topPipeImg;
    Image botPipeImg;


    // Bird's properties
    int birdX = gameWidth/8;
    int birdY = gameHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if(gameover){
            placePipesTmr.stop();
            gameLoop.stop();
        }

    }

    // Check collision between bird and pipe
    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused but required for KeyListener
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            velY = -9;

            // Reset game 
            if(gameover){
                bird.y = birdY;
                velY = 0;
                pipes.clear();
                score = 0;
                if(highScore > finalHighScore){finalHighScore = highScore;}
                highScore = 0;
                doMessage = false;
                gameover = false;
                gameLoop.start();
                placePipesTmr.start();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Unused but required for KeyListener
    }

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;

        }
    }

    // Pipe properties
    int pipeX = gameWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;
        }
    }

    // Game logic 
    Bird bird;
    int velY = 0;
    int velX = -4;
    int grav = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    // Add new pipes at random vertical position
    public void placePipes(){
        int randomPipeY =  (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int opening = gameHeight/4;



        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(botPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + opening;
        pipes.add(bottomPipe);

    }

    // Game timers
    Timer gameLoop;
    Timer placePipesTmr;

    boolean gameover = false;
    double score = 0;

    // Constructor for game
    flappyBird(){
        setPreferredSize(new Dimension(gameWidth,gameHeight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        back = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        botPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        placePipesTmr = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        placePipesTmr.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();


    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    //Scorekeeping
    double highScore;
    double finalHighScore;
    boolean doMessage;


    // Draw game elements
    public void draw(Graphics g){
        g.drawImage(back,0,0,gameWidth,gameHeight,null);
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);
        for (int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameover){
            g.drawString("Game Over: " + (int)score, 10,35);
            g.drawString("HIGH SCORE: "+ (int)finalHighScore, 10,120);
            g.setFont(Font.getFont("Roboto"));
            if(doMessage){
                g.drawString("NEW HIGH SCORE",10,200);

            }

        }else{
            g.drawString(String.valueOf((int)score), 10,35);
        }
    }


    // Move game elements and update logic
    public void move(){
        velY += grav;
        bird.y += velY;
        bird.y = Math.max(bird.y,0);

        for (int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5;
                highScore += 0.5;
                if(highScore >finalHighScore){finalHighScore += 0.5;
                doMessage = true;}

            }

            if (collision(bird,pipe)){
                gameover = true;
            }


        }

        if (bird.y > gameHeight){
            gameover = true;
        }
    }
}
