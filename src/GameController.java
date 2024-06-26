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
    private Timer moveTimer;
    private Timer repaintTimer;
    private static final int INTERVAL = 7000;
    private long nextMoveTime;
    private int score;
    private int timeLeft = 60;
    private Timer gameTimer;
    private boolean gameOver = false;

    public GameController() {
        bow = new Bow(100, 500);
        target = new Target(600, 150, this);
        gameWindow = new GameWindow(this);
        setupGameTimer();
        gameWindow.addKeyListener(this);
        timer = new Timer(20, this);
        repaintTimer = new Timer(1000, e -> gameWindow.repaint());
        repaintTimer.start();
        setupMoveTimer();
        timer.start();
    }

    private void setupGameTimer() {
        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            gameWindow.repaint();
            if (timeLeft <= 0) {
                gameOver = true;
                gameTimer.stop();
                gameWindow.repaint();
            }
        });
        gameTimer.start();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void resetGame() {
        score = 0;
        getArrow().setSpeed(170.0);
        timeLeft = 60;
        gameOver = false;
        gameTimer.restart();
        gameWindow.repaint();
    }
    public int getTimeLeft() {
        return timeLeft;
    }

    private void setupMoveTimer() {
        nextMoveTime = System.currentTimeMillis() + INTERVAL;

        // Timer to update and check every second
        moveTimer = new Timer(1000, e -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= nextMoveTime) {
                target.moveToRandomPosition(gameWindow.getWidth(), gameWindow.getHeight());
                // Reset next move time
                nextMoveTime = currentTime + INTERVAL;
            }
            gameWindow.repaint();
        });
        moveTimer.start();
    }
    public long getNextMoveTime() {
        return nextMoveTime;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:

                if (!getArrow().isFlying() && !getArrow().hasHitTarget()) {
                    double initialSpeed = getArrow().getSpeed();
                    getArrow().setVelocity(getArrow().getAngle(), initialSpeed);
                    getArrow().shoot();
                }
                break;
            case KeyEvent.VK_DOWN:
                if (!getArrow().hasHitTarget()) {
                    getArrow().changeAngleBy(-5.0);
                    bow.setAngle(bow.getAngle() - 5);
                }
                break;
            case KeyEvent.VK_UP:
                if (!getArrow().hasHitTarget()) {
                    getArrow().changeAngleBy(5.0);
                    bow.setAngle(bow.getAngle() + 5);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (!getArrow().hasHitTarget()) {
                    getArrow().decreaseSpeed();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (!getArrow().hasHitTarget()) {
                    getArrow().increaseSpeed();
                }
                break;
        }
        gameWindow.repaint();
    }
    public void increaseScore(int points) {
        this.score += points;
    }

    public int getScore() {
        return score;
    }

    public void resetArrowToBow() {
        Arrow arrow = getArrow();
        arrow.reset(bow.getX(), bow.getY(), bow.getAngle());
        arrow.setFlying(false);
        arrow.setHasHitTarget(false);
    }

    public void actionPerformed(ActionEvent e) {
        Arrow arrow = getArrow();

        if (arrow.hasHitTarget()) {  // Check if arrow has hit the target
            resetArrowToBow();       // Reset arrow to bow if it has hit
        }

        if (arrow.isFlying()) {      // If the arrow is flying Update its position
            arrow.updatePosition(0.1);

            Point tip = arrow.getTipPosition();
            if (target.getBounds().contains(tip)) {  // Check if arrow tip is within target bounds
                arrow.stop();
                // Score based on precision of hit
                if (arrow.getY() > target.getBounds().getY() + (target.getBounds().getHeight() / 2 - 15) &&
                        arrow.getY() < target.getBounds().getY() + (target.getBounds().getHeight() / 2 + 11)){
                    increaseScore(20);  // Higher score for more central hits
                }
                else {
                    increaseScore(10);  // Lower score otherwise
                }
                arrow.setFlying(false); // Mark arrow as no longer flying
            } else if (isArrowOutOfBounds(arrow)) { // Reset if arrow is out of bounds
                arrow.reset(bow.getX(), bow.getY(), bow.getAngle());
            }

            gameWindow.repaint();  // Refresh game window
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
