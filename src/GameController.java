import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController implements KeyListener, ActionListener {
    private Bow bow;
    private Target target;
    private GameWindow gameWindow;
    private Timer timer;

    public GameController() {
        bow = new Bow(100, 300);
        target = new Target(700, 300);
        gameWindow = new GameWindow(this);
        gameWindow.addKeyListener(this);
        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if (getArrow().hasHitTarget()) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                if (!getArrow().isFlying()) {
                    double initialSpeed = getArrow().getSpeed();
                    getArrow().setVelocity(getArrow().getAngle(), initialSpeed);
                    getArrow().shoot();
                }
                break;
            case KeyEvent.VK_DOWN:
                getArrow().changeAngleBy(-5.0);
                bow.setAngle(bow.getAngle() - 5);
                break;
            case KeyEvent.VK_UP:
                getArrow().changeAngleBy(5.0);
                bow.setAngle(bow.getAngle() + 5);
                break;
            case KeyEvent.VK_LEFT:
                getArrow().decreaseSpeed();
                break;
            case KeyEvent.VK_RIGHT:
                getArrow().increaseSpeed();
                break;
        }
        gameWindow.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        Arrow arrow = getArrow();
        if (arrow.isFlying()) {
            arrow.updatePosition(0.1);

            Point tip = arrow.getTipPosition();
            if (target.getBounds().contains(tip)) {
                arrow.stop();
                arrow.setFlying(false);
            } else if (isArrowOutOfBounds(arrow)) {
                arrow.reset(bow.getX(), bow.getY(), bow.getAngle());
            }

            gameWindow.repaint();
        }
    }

    private boolean isArrowOutOfBounds(Arrow arrow) {
        // Check if the arrow is out of the window bounds
        int windowWidth = gameWindow.getWidth();
        int windowHeight = gameWindow.getHeight();
        return (arrow.getX() < 0 || arrow.getX() > windowWidth || arrow.getY() > windowHeight);
    }

    public Bow getBow() {
        return bow;
    }

    public void setBow(Bow bow) {
        this.bow = bow;
    }

    public Target getTarget() {
        return target;
    }

    public Arrow getArrow() {
        return bow.getArrow();
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public void playGame() {
    }

    public static void main(String[] args) {
        GameController game = new GameController();
        game.playGame();
    }
}
