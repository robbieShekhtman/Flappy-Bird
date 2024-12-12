import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        int gameWidth  = 360;
        int gameHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(gameWidth,gameHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        flappyBird flappy = new flappyBird();
        frame.add(flappy);
        frame.pack();
        flappy.requestFocus();
        frame.setVisible(true);
    }
}