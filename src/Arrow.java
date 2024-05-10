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
    private double speed = 170.0;
    private BufferedImage image;
    private boolean hasHitTarget = false;

    public Arrow(double x, double y) {
        this.x = x;
        this.y = y;
        this.isFlying = false;
        try {
            BufferedImage originalImage = ImageIO.read(new File("resources/arrow.png"));
            int newWidth = (int) (originalImage.getWidth() * 0.4);  // Calculate the new width
            int newHeight = (int) (originalImage.getHeight() * 0.4);  // Calculate the new height
            this.image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);  // Create a new buffered image
            // Setup the scaling transformation
            AffineTransform at = new AffineTransform();
            at.scale(0.4, 0.4);  // Apply scaling
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);  // Create a scaling operation
            this.image = scaleOp.filter(originalImage, this.image);  // Apply the scaling operation to the original image
        } catch (IOException e) {
            this.image = null; // If an error occurs, set the image to null
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

        // Loop to simulate the trajectory for 50 steps
        for (int i = 0; i < 50; i++) {
            // Update position based on velocity and gravity
            simX += simVx * simDeltaTime;
            simY += simVy * simDeltaTime + 0.5 * gravity * Math.pow(simDeltaTime, 2);

            // Update vertical velocity due to gravity
            simVy += gravity * simDeltaTime;

            // Add the new position to the trajectory list
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
            // Update the x and y coordinates based on velocity and gravity
            x += vx * deltaTime;
            y += vy * deltaTime + 0.5 * gravity * Math.pow(deltaTime, 2);

            // Update the vertical velocity with gravity's effect
            vy += gravity * deltaTime;

            // Calculate the new angle of the object based on its velocity
            angle = Math.toDegrees(Math.atan2(-vy, vx));

        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();  // Create a copy of the graphics context for safe modifications
        AffineTransform arrow = new AffineTransform();  // Create a transformation for the arrow image

        // Translate the image to the specified position, adjusting so the center of the image is at (x, y)
        arrow.translate(x - image.getWidth() / 2.0, y - image.getHeight() / 2.0);

        // Rotate the image around its center based on the given angle
        arrow.rotate(Math.toRadians(-angle), image.getWidth() / 2.0, image.getHeight() / 2.0);

        // Draw the transformed image on the screen
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

    public void setAngle(double angle) {
        this.angle = angle;
        if (!isFlying) {
            setVelocity(angle, Math.hypot(vx, vy));
        }
    }
    public boolean hasHitTarget() {
        return hasHitTarget;
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
