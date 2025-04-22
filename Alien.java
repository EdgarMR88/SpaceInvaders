// Alien.java - Clase para los aliens
import java.awt.*;
import java.awt.image.BufferedImage;

public class Alien {
    private int x, y;
    private int width = 40;
    private int height = 30;
    private int direction = 1; // 1 derecha, -1 izquierda
    private int speed = 1;
    
    private BufferedImage image;
    
    public Alien(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }
    
    public void update() {
        x += speed * direction;
    }
    
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            // Dibujar alien básico si no hay imagen
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height - 10);
            
            // Antenas
            g.drawLine(x + 10, y, x + 10, y - 10);
            g.drawLine(x + width - 10, y, x + width - 10, y - 10);
            
            // Tentáculos
            g.drawLine(x + 5, y + height - 5, x, y + height);
            g.drawLine(x + 15, y + height - 5, x + 10, y + height);
            g.drawLine(x + width - 15, y + height - 5, x + width - 10, y + height);
            g.drawLine(x + width - 5, y + height - 5, x + width, y + height);
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    // Getters y setters
    public int getX() { return x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }
}

