import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameWindow extends JFrame {
    private GameController game;
    private long nextMoveTime;
    private static final int INTERVAL = 10000;
    private Image background;

    public GameWindow(GameController game) {
        super("2D Archery Game");
        this.game = game;
        background = new ImageIcon("resources/background.png").getImage();
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nextMoveTime = System.currentTimeMillis() + 10000;
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, this);

        Arrow arrow = game.getArrow();
        if (!arrow.isFlying() && !arrow.hasHitTarget()) {
            List<Point> trajectory = arrow.simulateTrajectory();
            for (Point p : trajectory) {
                g.setColor(Color.BLACK);
                g.fillOval(p.x, p.y, 3, 3);

            }
        }
        game.getBow().draw(g);
        game.getTarget().draw(g);
        game.getArrow().draw(g);

        long currentTime = System.currentTimeMillis();
        long timeLeft = (game.getNextMoveTime() - currentTime) / 1000;

        // Prevent displaying negative time; reset to 10 if it reaches 0
        if (timeLeft < 0) {
            timeLeft = INTERVAL / 1000;
        }

        g.setColor(Color.BLACK);
        g.drawString("Score: " + game.getScore(), 10, 50);

        // Draw the countdown timer
        g.setColor(Color.BLACK);
        g.drawString("Next move in: " + timeLeft + " seconds", 650, 80);
        g.drawString("Speed: " + String.format("%.1f", game.getArrow().getSpeed()), 10, 70);
    }
}
