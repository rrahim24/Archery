import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.AffineTransformOp;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Arrow {
    private double x, y;
    private double vx, vy;
    private boolean isFlying;
    private final double gravity = 9.81;
    private double angle = 45.0;
    private double speed = 120.0;
    private BufferedImage image;
    private boolean hasHitTarget = false;

    public Arrow(double x, double y) {
        this.x = x;
        this.y = y;
        this.isFlying = false;
        try {
            BufferedImage originalImage = ImageIO.read(new File("resources/arrow.png"));
            double scale = 0.4;
            int newWidth = (int) (originalImage.getWidth() * scale);
            int newHeight = (int) (originalImage.getHeight() * scale);
            this.image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = new AffineTransform();
            at.scale(scale, scale);
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            this.image = scaleOp.filter(originalImage, this.image);
        } catch (IOException e) {
            this.image = null;
        }
        increaseSpeed();
        decreaseSpeed();
    }

    public List<Point> simulateTrajectory() {
        List<Point> trajectory = new ArrayList<>();
        double simX = x;
        double simY = y;
        double simVx = vx;
        double simVy = vy;
        double simDeltaTime = 0.1;

        for (int i = 0; i < 50; i++) {
            simX += simVx * simDeltaTime;
            simY += simVy * simDeltaTime + 0.5 * gravity * Math.pow(simDeltaTime, 2);
            simVy += gravity * simDeltaTime;

            trajectory.add(new Point((int) simX, (int) simY));
        }
        return trajectory;
    }

    public void reset(int x, int y, double angle) {
        this.x = x;
        this.y = y;
        isFlying = false;
        this.angle = angle;
        vx = 0;
        vy = 0;
        increaseSpeed();
        decreaseSpeed();
    }

    public void setVelocity(double angle, double speed) {
        this.vx = speed * Math.cos(Math.toRadians(angle));
        this.vy = -speed * Math.sin(Math.toRadians(angle));
    }

    public void updatePosition(double deltaTime) {
        if (isFlying) {
            x += vx * deltaTime;
            y += vy * deltaTime + 0.5 * gravity * Math.pow(deltaTime, 2);
            vy += gravity * deltaTime;

            angle = Math.toDegrees(Math.atan2(-vy, vx));

        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform arrow = new AffineTransform();

        arrow.translate(x - image.getWidth() / 2.0, y - image.getHeight() / 2.0);

        arrow.rotate(Math.toRadians(-angle), image.getWidth() / 2.0, image.getHeight() / 2.0);

        g2d.drawImage(image, arrow, null);
        g2d.dispose();
    }
    public Point getTipPosition() {
        double radianAngle = Math.toRadians(angle);
        double tipX = x + (double) image.getWidth() / 2 * Math.cos(-radianAngle);
        double tipY = y + (double) image.getWidth() / 2 * Math.sin(-radianAngle);
        return new Point((int) tipX, (int) tipY);
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }


    public void increaseSpeed() {
        setSpeed(this.speed + 2.5);
    }

    public void decreaseSpeed() {
        setSpeed(Math.max(10, this.speed - 2.5));
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        if (!isFlying) {
            setVelocity(angle, speed);
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    public void setAngle(double angle) {
        this.angle = angle;
        if (!isFlying) {
            setVelocity(angle, Math.hypot(vx, vy));
        }
    }
    public boolean hasHitTarget() {
        return hasHitTarget;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void changeAngleBy(double delta) {
        setAngle(this.angle + delta);
    }

    public double getAngle() {
        return angle;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setHasHitTarget(boolean hasHitTarget) {
        this.hasHitTarget = hasHitTarget;
    }

    public void stop() {
        vx = 0;
        vy = 0;
        isFlying = false;
        hasHitTarget = true;
    }

    public boolean isFlying() {
        return isFlying;
    }
    public void shoot() {
        if (!isFlying) {
            isFlying = true;
        }
    }

}
