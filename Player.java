// Player.java - Clase para el jugador
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {
    private int x, y;
    private int width = 50;
    private int height = 30;
    private int speed = 5;
    
    private boolean movingLeft = false;
    private boolean movingRight = false;
    
    private BufferedImage image;
    
    public Player(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }
    
    public void update() {
        if (movingLeft) {
            x -= speed;
        }
        if (movingRight) {
            x += speed;
        }
    }
    
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            // Dibujar nave básica si no hay imagen
            g.setColor(Color.GREEN);
            int[] xPoints = {x + width/2, x, x + width};
            int[] yPoints = {y, y + height, y + height};
            g.fillPolygon(xPoints, yPoints, 3);
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getters y setters
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }
    
    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
}
