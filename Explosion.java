// Clase Explosion para efectos visuales
import java.awt.*;

public class Explosion {
    private int x, y;
    private int radius = 5;
    private int maxRadius = 30;
    private int growthRate = 2;
    private Color[] colors = {Color.WHITE, Color.YELLOW, Color.ORANGE, Color.RED};
    private int currentColorIndex = 0;
    private int frameCount = 0;
    private boolean finished = false;
    
    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void update() {
        radius += growthRate;
        frameCount++;
        
        // Cambiar color cada ciertos frames
        if (frameCount % 5 == 0) {
            currentColorIndex = (currentColorIndex + 1) % colors.length;
        }
        
        // Finalizar explosión cuando llegue al tamaño máximo
        if (radius >= maxRadius) {
            finished = true;
        }
    }
    
    public void draw(Graphics g) {
        g.setColor(colors[currentColorIndex]);
        g.drawOval(x - radius/2, y - radius/2, radius, radius);
        g.drawOval(x - radius/2 + 2, y - radius/2 + 2, radius - 4, radius - 4);
    }
    
    public boolean isFinished() {
        return finished;
    }
}

