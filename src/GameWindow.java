import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GameWindow extends JFrame {
    private GameController game;

    public GameWindow(GameController game) {
        super("2D Archery Game");
        this.game = game;
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        Arrow arrow = game.getArrow();
        if (!arrow.isFlying() && !arrow.hasHitTarget()) {
            List<Point> trajectory = arrow.simulateTrajectory();
            for (Point p : trajectory) {
                g.setColor(Color.GRAY);
                g.fillOval(p.x, p.y, 3, 3);
                g.setColor(Color.WHITE);
                g.fillRect(300, 0, 500, 600);
            }
        }
        game.getBow().draw(g);
        game.getTarget().draw(g);
        game.getArrow().draw(g);

        // Display the angle
        g.setColor(Color.BLACK);
        g.drawString("Angle: " + String.format("%.1f", game.getArrow().getAngle()), 10, 50);
        g.drawString("Speed: " + String.format("%.1f", game.getArrow().getSpeed()), 10, 70);
    }
}
