import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Graphics2D;
import java.util.Random;

public class Target {
    private int x, y;
    private BufferedImage image;
    private GameController game;

    public Target(int x, int y, GameController game) {
        this.x = x;
        this.y = y;
        this.game = game;
        try {
            BufferedImage originalImage = ImageIO.read(new File("resources/target.png"));
            double scale = 0.5; // Scale down to 50% of the original size
            this.image = scaleImage(originalImage, scale);
        } catch (IOException e) {
            System.err.println("Error loading target image: " + e.getMessage());
            this.image = null;
        }
    }

    private BufferedImage scaleImage(BufferedImage originalImage, double scale) {
        int newWidth = (int) (originalImage.getWidth() * scale);
        int newHeight = (int) (originalImage.getHeight() * scale);
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        scaledImage = scaleOp.filter(originalImage, scaledImage);
        return scaledImage;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        int imageCenterX = image.getWidth() / 2;
        int imageCenterY = image.getHeight() / 2;

        // Create an AffineTransform for rotation centered on the image
        AffineTransform transform = new AffineTransform();

        // First, translate to the center of where we want the image to appear
        transform.translate(x + 105, y + 85);

        // Rotate around the center of the image
        transform.rotate(Math.toRadians(-12), imageCenterX, imageCenterY);

        // Apply the transformation and draw the image
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        g2d.drawImage(image, op, -imageCenterX, -imageCenterY);
        g2d.dispose();
    }

    public void moveToRandomPosition(int maxWidth, int maxHeight) {
        Random rand = new Random();
        // Subtract image dimensions to avoid clipping at the edges
        int newX = rand.nextInt(maxWidth - image.getWidth());
        if (newX < 500){
            x = newX + 500;
        }
        else
        {
            x = newX;
        }
        y = rand.nextInt(maxHeight - image.getHeight());
        if (game.getArrow().hasHitTarget()){
            game.resetArrowToBow();;
        }
    }

    public Rectangle getBounds() {

        int size = Math.min(image.getWidth(), image.getHeight());
        int targetDiameter = (int)(size * 0.4);

        int adjustedX = x + 90;
        int adjustedY = y + 15;
        return new Rectangle(adjustedX, adjustedY, 30, targetDiameter + 40);

    }
}

