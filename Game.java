// Game.java - Clase principal con sonido, imágenes y sistema de vidas
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class Game extends JPanel implements Runnable {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int DELAY = 16; // ~60 FPS

    private Thread gameThread;
    private boolean running = false;
    
    private Player player;
    private List<Alien> aliens = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<Explosion> explosions = new ArrayList<>();
    
    private int score = 0;
    private boolean gameOver = false;
    private boolean victory = false;
    
    // Sistema de vidas
    private int lives = 3;
    
    // Sistema de niveles
    private int currentLevel = 1;
    private int alienFireRate = 2; // Porcentaje de probabilidad de disparo
    
    // Contador para efectos de victoria
    private int victoryTimer = 0;
    private List<Firework> fireworks = new ArrayList<>();
    
    // Imágenes
    private BufferedImage playerImage;
    private BufferedImage alienImage;
    private BufferedImage backgroundImage;
    
    // Audio
    private Clip backgroundMusic;
    private Clip shootSound;
    private Clip explosionSound;
    private Clip gameOverSound;
    private Clip victorySound;
    
    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        loadResources();
        initGame();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player.setMovingLeft(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player.setMovingRight(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!gameOver && !victory) {
                        fireProjectile();
                    } else if (gameOver) {
                        // Reiniciar juego desde nivel 1
                        resetGame();
                    } else if (victory) {
                        // Ir al siguiente nivel
                        goToNextLevel();
                    }
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player.setMovingLeft(false);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player.setMovingRight(false);
                }
            }
        });
    }
    
    private void loadResources() {
        try {
            // Carga de imágenes - Para reemplazar imagen cambiar la ruta
            try {
                playerImage = ImageIO.read(getClass().getResource("/player.png"));
                alienImage = ImageIO.read(getClass().getResource("/alien.png"));
                backgroundImage = ImageIO.read(getClass().getResource("/background.png"));
            } catch (IOException e) {
                System.out.println("No se pudieron cargar las imágenes. Usando gráficos básicos.");
                // Si hay error al cargar las imágenes, el juego usará gráficos simples
                playerImage = null;
                alienImage = null;
                backgroundImage = null;
            }
            
            // Carga de sonidos
            try {
                // Música de fondo
                AudioInputStream musicStream = AudioSystem.getAudioInputStream(getClass().getResource("/music.wav"));

                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(musicStream);
                // No activamos el loop aquí, lo haremos en el initGame
                
                // Sonido de disparo
                AudioInputStream shootStream = AudioSystem.getAudioInputStream(getClass().getResource("/shoot.wav"));
                shootSound = AudioSystem.getClip();
                shootSound.open(shootStream);
                
                // Sonido de explosión
                AudioInputStream explosionStream = AudioSystem.getAudioInputStream(getClass().getResource("/explosion.wav"));
                explosionSound = AudioSystem.getClip();
                explosionSound.open(explosionStream);
                
                // Sonido de game over
                AudioInputStream gameOverStream = AudioSystem.getAudioInputStream(getClass().getResource("/gameover.wav"));

                gameOverSound = AudioSystem.getClip();
                gameOverSound.open(gameOverStream);
                
                // Sonido de victoria
                try {
                    AudioInputStream victoryStream = AudioSystem.getAudioInputStream(getClass().getResource("/victory.wav"));
                    victorySound = AudioSystem.getClip();
                    victorySound.open(victoryStream);
                } catch (Exception e) {
                    System.out.println("No se pudo cargar el sonido de victoria: " + e.getMessage());
                }
                
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println("No se pudieron cargar los archivos de audio: " + e.getMessage());
                // Si hay error al cargar los sonidos, el juego seguirá sin sonidos
                backgroundMusic = null;
                shootSound = null;
                explosionSound = null;
                gameOverSound = null;
                victorySound = null;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar recursos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initGame() {
        player = new Player(WIDTH / 2, HEIGHT - 60, playerImage);
        
        // Crear filas de aliens
        aliens.clear();
        projectiles.clear();
        explosions.clear();
        fireworks.clear();
        
        // En el nivel 1 la velocidad es normal, cada alien tiene su propia velocidad
        alienFireRate = 2 + currentLevel;
        
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                Alien alien = new Alien(100 + col * 60, 50 + row * 40, alienImage);
                // La clase Alien ya tiene una velocidad establecida,
                // no necesitamos modificarla aquí
                aliens.add(alien);
            }
        }
        
        // Iniciar música de fondo (asegurarse de que se reproduzca en bucle)
        startBackgroundMusic();
        
        gameOver = false;
        victory = false;
        victoryTimer = 0;
    }
    
    // Método específico para iniciar la música de fondo en bucle
    private void startBackgroundMusic() {
        if (backgroundMusic != null) {
            // Detener la música si ya está reproduciéndose
            if (backgroundMusic.isRunning()) {
                backgroundMusic.stop();
            }
            
            // Reiniciar la posición y configurar para reproducción en bucle
            backgroundMusic.setFramePosition(0);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    // Método para reiniciar completamente el juego
    private void resetGame() {
        currentLevel = 1;
        score = 0;
        lives = 3;
        initGame();
    }
    
    // Método para avanzar al siguiente nivel
    private void goToNextLevel() {
        currentLevel++;
        lives++; // Bonus: una vida extra por nivel
        initGame();
    }
    
    public void startGame() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }
    
    private void fireProjectile() {
        // Evitar que el jugador dispare demasiado rápido
        boolean canFire = true;
        for (Projectile p : projectiles) {
            if (p.isFromPlayer() && p.isActive()) {
                canFire = false;
                break;
            }
        }
        
        if (canFire) {
            Projectile p = new Projectile(player.getX() + player.getWidth() / 2, player.getY(), true);
            projectiles.add(p);
            
            // Reproducir sonido de disparo
            if (shootSound != null) {
                shootSound.setFramePosition(0);
                shootSound.start();
            }
        }
    }
    
    private void alienFire() {
        if (aliens.isEmpty() || gameOver || victory) return;
        
        // Un alien aleatorio dispara ocasionalmente
        Random random = new Random();
        if (random.nextInt(100) < alienFireRate) { // Probabilidad basada en nivel
            Alien shooter = aliens.get(random.nextInt(aliens.size()));
            Projectile p = new Projectile(shooter.getX() + shooter.getWidth() / 2, 
                    shooter.getY() + shooter.getHeight(), false);
            projectiles.add(p);
        }
    }
    
    private void addFirework() {
        Random random = new Random();
        int x = random.nextInt(WIDTH);
        int y = random.nextInt(HEIGHT / 2);
        Color color = new Color(
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256)
        );
        fireworks.add(new Firework(x, y, color));
    }
    
    private void update() {
        if (gameOver) return;
        
        if (victory) {
            // Actualizar temporizador de victoria
            victoryTimer++;
            
            // Crear fuegos artificiales aleatorios durante la celebración
            if (victoryTimer % 10 == 0) {
                addFirework();
            }
            
            // Actualizar fuegos artificiales
            for (Iterator<Firework> it = fireworks.iterator(); it.hasNext();) {
                Firework firework = it.next();
                firework.update();
                if (firework.isFinished()) {
                    it.remove();
                }
            }
            
            // Esperar 5 segundos antes de permitir ir al siguiente nivel
            if (victoryTimer > 300) { // 5 segundos a 60 FPS
                // Listo para ir al siguiente nivel
                victoryTimer = 301; // Mantener en este valor
            }
            
            return;
        }
        
        player.update();
        
        // Limitar movimiento del jugador a los bordes
        if (player.getX() < 0) {
            player.setX(0);
        } else if (player.getX() > WIDTH - player.getWidth()) {
            player.setX(WIDTH - player.getWidth());
        }
        
        // Mover aliens
        boolean changeDirection = false;
        for (Alien alien : aliens) {
            alien.update();
            
            // Verificar si algún alien llega al borde
            if ((alien.getX() <= 0 && alien.getDirection() < 0) || 
                (alien.getX() + alien.getWidth() >= WIDTH && alien.getDirection() > 0)) {
                changeDirection = true;
            }
            
            // Verificar si algún alien alcanza al jugador
            if (alien.getY() + alien.getHeight() >= player.getY()) {
                setGameOver();
            }
        }
        
        // Cambiar dirección y bajar todos los aliens
        if (changeDirection) {
            for (Alien alien : aliens) {
                alien.setDirection(-alien.getDirection());
                alien.setY(alien.getY() + 20);
            }
        }
        
        // Actualizar proyectiles
        for (Iterator<Projectile> it = projectiles.iterator(); it.hasNext();) {
            Projectile p = it.next();
            p.update();
            
            // Eliminar proyectiles que salgan de la pantalla
            if (p.getY() < 0 || p.getY() > HEIGHT) {
                it.remove();
                continue;
            }
            
            // Colisión de proyectil del jugador con aliens
            if (p.isFromPlayer()) {
                for (Iterator<Alien> alienIt = aliens.iterator(); alienIt.hasNext();) {
                    Alien alien = alienIt.next();
                    if (p.getBounds().intersects(alien.getBounds())) {
                        alienIt.remove();
                        it.remove();
                        score += 10 * currentLevel; // Más puntos en niveles superiores
                        
                        // Crear explosión
                        explosions.add(new Explosion(alien.getX(), alien.getY()));
                        
                        // Reproducir sonido de explosión
                        if (explosionSound != null) {
                            explosionSound.setFramePosition(0);
                            explosionSound.start();
                        }
                        break;
                    }
                }
            } 
            // Colisión de proyectil alienígena con jugador
            else {
                if (p.getBounds().intersects(player.getBounds())) {
                    lives--;
                    it.remove();
                    
                    // Crear explosión donde impactó el proyectil
                    explosions.add(new Explosion(p.getX(), p.getY()));
                    
                    // Reproducir sonido de explosión
                    if (explosionSound != null) {
                        explosionSound.setFramePosition(0);
                        explosionSound.start();
                    }
                    
                    if (lives <= 0) {
                        setGameOver();
                    }
                }
            }
        }
        
        // Actualizar explosiones
        for (Iterator<Explosion> it = explosions.iterator(); it.hasNext();) {
            Explosion explosion = it.next();
            explosion.update();
            if (explosion.isFinished()) {
                it.remove();
            }
        }
        
        // Disparos alienígenas
        alienFire();
        
        // Victoria si no quedan aliens
        if (aliens.isEmpty()) {
            setVictory();
        }
    }
    
    private void setGameOver() {
        gameOver = true;
        
        // Detener música de fondo
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
        
        // Reproducir sonido de game over
        if (gameOverSound != null) {
            gameOverSound.setFramePosition(0);
            gameOverSound.start();
        }
    }
    
    private void setVictory() {
        victory = true;
        victoryTimer = 0;
        
        // Nota: No detenemos la música de fondo en caso de victoria
        
        // Reproducir sonido de victoria
        if (victorySound != null) {
            victorySound.setFramePosition(0);
            victorySound.start();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujar fondo
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);
        } else {
            // Fondo negro con estrellas si no hay imagen
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            
            // Dibujar algunas estrellas
            g.setColor(Color.WHITE);
            Random random = new Random(42); // Semilla fija para que las estrellas no parpadeen
            for (int i = 0; i < 100; i++) {
                int starX = random.nextInt(WIDTH);
                int starY = random.nextInt(HEIGHT);
                g.fillRect(starX, starY, 2, 2);
            }
        }
        
        // Dibujar jugador
        if (!victory) {
            player.draw(g);
        }
        
        // Dibujar aliens
        for (Alien alien : aliens) {
            alien.draw(g);
        }
        
        // Dibujar proyectiles
        for (Projectile p : projectiles) {
            p.draw(g);
        }
        
        // Dibujar explosiones
        for (Explosion explosion : explosions) {
            explosion.draw(g);
        }
        
        // Dibujar fuegos artificiales en victoria
        for (Firework firework : fireworks) {
            firework.draw(g);
        }
        
        // Mostrar puntuación y vidas
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.drawString("Puntuación: " + score, 20, 30);
        
        // Mostrar vidas
        g.drawString("Vidas: " + lives, WIDTH - 120, 30);
        
        // Mostrar nivel actual
        g.drawString("Nivel: " + currentLevel, WIDTH / 2 - 50, 30);
        
        // Mensaje de Game Over
        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 150)); // Negro semi-transparente
            g.fillRect(0, 0, WIDTH, HEIGHT);
            
            g.setColor(Color.RED);
            g.setFont(new Font("SansSerif", Font.BOLD, 50));
            String message = "¡GAME OVER!";
            int messageWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (WIDTH - messageWidth) / 2, HEIGHT / 2);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            String restart = "Presiona ESPACIO para reiniciar";
            int restartWidth = g.getFontMetrics().stringWidth(restart);
            g.drawString(restart, (WIDTH - restartWidth) / 2, HEIGHT / 2 + 50);
            
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
            String finalScore = "Puntuación final: " + score;
            int scoreWidth = g.getFontMetrics().stringWidth(finalScore);
            g.drawString(finalScore, (WIDTH - scoreWidth) / 2, HEIGHT / 2 - 50);
        }
        
        // Mensaje de Victoria
        if (victory) {
            // Capa semi-transparente
            g.setColor(new Color(0, 0, 100, 150)); // Azul semi-transparente
            g.fillRect(0, 0, WIDTH, HEIGHT);
            
            // Título con efecto parpadeante
            g.setColor(Color.YELLOW);
            if (victoryTimer % 20 < 10) {
                g.setColor(Color.ORANGE);
            }
            g.setFont(new Font("SansSerif", Font.BOLD, 50));
            String message = "¡NIVEL COMPLETADO!";
            int messageWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (WIDTH - messageWidth) / 2, HEIGHT / 3);
            
            // Mostrar puntuación
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
            String scoreText = "Puntuación: " + score;
            int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
            g.drawString(scoreText, (WIDTH - scoreWidth) / 2, HEIGHT / 2);
            
            // Mostrar nivel actual
            String levelText = "Nivel completado: " + currentLevel;
            int levelWidth = g.getFontMetrics().stringWidth(levelText);
            g.drawString(levelText, (WIDTH - levelWidth) / 2, HEIGHT / 2 + 50);
            
            // Instrucciones
            if (victoryTimer > 120) { // Después de 2 segundos
                g.setFont(new Font("SansSerif", Font.BOLD, 25));
                String nextLevel = "Presiona ESPACIO para ir al siguiente nivel";
                int nextLevelWidth = g.getFontMetrics().stringWidth(nextLevel);
                g.drawString(nextLevel, (WIDTH - nextLevelWidth) / 2, HEIGHT / 2 + 120);
                
                // Mostrar info de bonus
                g.setFont(new Font("SansSerif", Font.BOLD, 20));
                String bonus = "¡Bonus! +1 vida para el siguiente nivel";
                int bonusWidth = g.getFontMetrics().stringWidth(bonus);
                g.drawString(bonus, (WIDTH - bonusWidth) / 2, HEIGHT / 2 + 160);
            }
        }
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
            
            // Pequeña pausa para ahorrar CPU
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Clase interna para los fuegos artificiales
    private class Firework {
        private List<Particle> particles;
        private int lifespan;
        private int currentLife;
        
        public Firework(int x, int y, Color color) {
            this.lifespan = 60; // 1 segundo a 60 FPS
            this.currentLife = 0;
            this.particles = new ArrayList<>();
            
            // Crear partículas
            Random random = new Random();
            for (int i = 0; i < 50; i++) {
                double angle = random.nextDouble() * Math.PI * 2;
                double speed = 1 + random.nextDouble() * 3;
                double dx = Math.cos(angle) * speed;
                double dy = Math.sin(angle) * speed;
                particles.add(new Particle(x, y, dx, dy, color));
            }
        }
        
        public void update() {
            currentLife++;
            for (Particle p : particles) {
                p.update();
            }
        }
        
        public boolean isFinished() {
            return currentLife >= lifespan;
        }
        
        public void draw(Graphics g) {
            for (Particle p : particles) {
                p.draw(g);
            }
        }
    }
    
    // Clase interna para las partículas de los fuegos artificiales
    private class Particle {
        private double x, y;
        private double dx, dy;
        private Color color;
        private double gravity = 0.1;
        
        public Particle(int x, int y, double dx, double dy, Color color) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.color = color;
        }
        
        public void update() {
            x += dx;
            y += dy;
            dy += gravity; // Simular gravedad
        }
        
        public void draw(Graphics g) {
            g.setColor(color);
            g.fillRect((int) x, (int) y, 2, 2);
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        Game game = new Game();
        
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        game.startGame();
    }
}