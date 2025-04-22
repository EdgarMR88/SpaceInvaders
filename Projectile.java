// Projectile.java - Clase para los proyectiles
import java.awt.*;

public class Projectile {
    private int x, y;
    private int width = 4;
    private int height = 15;
    private int speed = 7;
    private boolean fromPlayer;
    private boolean active = true;
    
    public Projectile(int x, int y, boolean fromPlayer) {
        this.x = x - width / 2; // Centrar con el que dispara
        this.y = y;
        this.fromPlayer = fromPlayer;
    }
    
    public void update() {
        if (fromPlayer) {
            y -= speed; // Hacia arriba
        } else {
            y += speed; // Hacia abajo
        }
    }
    
    public void draw(Graphics g) {
        if (fromPlayer) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(x, y, width, height);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    // Getters
    public int getY() { return y; }
    public boolean isFromPlayer() { return fromPlayer; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public int getX() { return x; }
}
