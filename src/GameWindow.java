import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;


public class GameWindow extends JFrame implements KeyListener {
    private GameController game;
    private long nextMoveTime;
    private static final int INTERVAL = 10000;
    private Image background;
    private Image gameOver;


    public GameWindow(GameController game) {
        this.game = game;
        setTitle("Bullseye");
        background = new ImageIcon("resources/background.png").getImage();
        gameOver = new ImageIcon("resources/gameOver.png").getImage();
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        setVisible(true);
    }


    private void drawTimer(Graphics g, int timeLeft) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString("Time left: " + timeLeft + " seconds", 50, 50);  // Display the passed timeLeft
    }


    private void drawGameOverScreen(Graphics g) {
        g.drawImage(gameOver, 0, 0, this);
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 36));
        g.drawString("Game Over", 600, 300);
        int score = game.getScore();
        g.drawString("Score: " + score, 600, 350);
        g.drawString("Press 'P' to Play Again", 600, 400);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P && game.isGameOver()) {
            game.resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void paint(Graphics g) {
        if (!game.isGameOver()) {  // Check if the game is still running
            g.drawImage(background, 0, 0, this);  // Draw the background image
            drawTimer(g, game.getTimeLeft());  // Draw the game timer

            Arrow arrow = game.getArrow();

            // Check if the arrow is not flying and has not hit the target to simulate its trajectory
            if (!arrow.isFlying() && !arrow.hasHitTarget()) {
                List<Point> trajectory = arrow.simulateTrajectory();
                for (Point p : trajectory) {  // Draw each point of the trajectory
                    g.setColor(Color.BLACK);
                    if (p.x < 300){  // Limit trajectory drawing to part of the screen
                        g.fillOval(p.x, p.y, 3, 3);
                    }
                }
            }

            // Draw the bow, target, and arrow
            game.getBow().draw(g);
            game.getTarget().draw(g);
            game.getArrow().draw(g);

            // Calculate and display the time left until the next move
            long currentTime = System.currentTimeMillis();
            long timeLeft = (game.getNextMoveTime() - currentTime) / 1000;
            if (timeLeft < 0) {
                timeLeft = INTERVAL / 1000;
            }
            g.setColor(Color.BLACK);
            g.drawString("Score: " + game.getScore(), 10, 130);
            g.drawString("Next move in: " + timeLeft + " seconds", 650, 80);
            g.drawString("Speed: " + String.format("%.1f", game.getArrow().getSpeed()), 10, 100);
        } else {
            drawGameOverScreen(g);  // Draw game over screen if the game has ended
        }
    }
}
