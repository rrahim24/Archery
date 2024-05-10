import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Bow {
    private int x, y;
    private double angle;
    private Arrow arrow;
    private BufferedImage image;



    public Bow(int x, int y) {
        this.x = x;
        this.y = y;
        this.angle = 45;
        this.arrow = new Arrow(x,y);
        try {
            this.image = ImageIO.read(new File("resources/bow.png"));
        } catch (IOException e) {
            e.printStackTrace();
            this.image = null; // Handle the case where the image is not found
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform at = new AffineTransform();
        int centerX = image.getWidth() / 2;
        int centerY = image.getHeight() / 2;

        at.translate(x - centerX, y - centerY);
        at.rotate(Math.toRadians(-angle), centerX, centerY);

        g2d.drawImage(image, at, null);
        g2d.dispose();
    }



    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public void shootArrow() {
        arrow.shoot();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
