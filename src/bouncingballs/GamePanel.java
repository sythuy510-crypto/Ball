package bouncingballs;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener {

    private final Timer timer;
    private final ArrayList<Balls> balls;
    private int predictedBall;
    private int finishedBall = -1;
    private boolean running = false;

    private GameMap map;
    private int mapId;
    private int ballCount;

    private final int BALL_SIZE = 18;
    private final Random rnd = new Random();

    public GamePanel(int mapId, int predictedBall, int ballCount) {

        this.mapId = mapId;
        this.predictedBall = predictedBall;
        this.ballCount = ballCount;
        this.balls = new ArrayList<>();

        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(0, 130, 0));

        map = new GameMap(mapId, 800, 600);

        timer = new Timer(16, this);
        initBalls();
    }

    /* =============================
     *  Tạo bóng
     ============================== */
    private void initBalls() {

        balls.clear();

        for (int i = 0; i < ballCount; i++) {

            int x, y;
            double vx, vy;

            /* ===== MAP 1 — rơi từ 4 góc ===== */
            if (mapId == 1) {
                switch (i % 4) {
                    case 0 -> { x = 10;  y = 10; }
                    case 1 -> { x = 770; y = 10; }
                    case 2 -> { x = 10;  y = 560; }
                    default -> { x = 770; y = 560; }
                }

                vx = randSpeed();
                vy = randSpeed();
            }

            /* ===== MAP 2 — rơi từ 2 góc trên ===== */
            else if (mapId == 2) {
                if (i % 2 == 0) {
                    x = 20;   y = 10;
                    vx = randSpeed();
                } else {
                    x = 760;  y = 10;
                    vx = -randSpeed();
                }
                vy = randSpeed();
            }

            /* ===== MAP 3 — rơi từ 4 góc + bóng to hơn ===== */
            else {
                switch (i % 4) {
                    case 0 -> { x = 10;  y = 10; }
                    case 1 -> { x = 770; y = 10; }
                    case 2 -> { x = 10;  y = 560; }
                    default -> { x = 770; y = 560; }
                }

                vx = randSpeed();
                vy = randSpeed();
            }

            Balls b = new Balls(x, y, vx, vy, i + 1);
            b.size = (mapId == 3 ? BALL_SIZE + 8 : BALL_SIZE);

            balls.add(b);
        }
    }

    private double randSpeed() {
        return (rnd.nextDouble() * 3 + 2) * (rnd.nextBoolean() ? 1 : -1);
    }

    public void setBallCount(int c) {
        ballCount = c;
        initBalls();
        repaint();
    }

    public void setPredictedBall(int id) {
        predictedBall = id;
    }

    public void startGame() {
        running = true;
        timer.start();
    }

    public void pauseGame() {
        running = false;
        timer.stop();
    }


    /* =============================
     *  GAME LOOP
     ============================== */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!running) return;

        ArrayList<Balls> removeList = new ArrayList<>();

        for (Balls b : balls) {
            b.move();
            checkWalls(b);

            if (checkHole(b)) {
                removeList.add(b);
                finish(b.id);
                break;
            }
        }

        balls.removeAll(removeList);
        repaint();
    }



    /* =============================
     *  Va chạm tường
     ============================== */
    private void checkWalls(Balls b) {

        int maxX = getWidth();
        int maxY = getHeight();

        if (b.x < 0) {
            b.x = 0;
            b.bounceX();
        }
        if (b.x + b.size > maxX) {
            b.x = maxX - b.size;
            b.bounceX();
        }
        if (b.y < 0) {
            b.y = 0;
            b.bounceY();
        }
        if (b.y + b.size > maxY) {
            b.y = maxY - b.size;
            b.bounceY();
        }

        // ===== Va chạm tường trong map =====
        Rectangle R = b.getBounds();
        for (Rectangle w : map.walls) {
            if (w.intersects(R)) {
                Rectangle inter = w.intersection(R);

                if (inter.width < inter.height) {
                    if (b.x < w.x) {
                        b.x = w.x - b.size - 1;
                    } else {
                        b.x = w.x + w.width + 1;
                    }
                    b.bounceX();
                } else {
                    if (b.y < w.y) {
                        b.y = w.y - b.size - 1;
                    } else {
                        b.y = w.y + w.height + 1;
                    }
                    b.bounceY();
                }
            }
        }
    }


    /* =============================
     *  Kiểm tra rơi vào lỗ
     ============================== */
    private boolean checkHole(Balls b) {

        double cx = b.x + b.size / 2.0;
        double cy = b.y + b.size / 2.0;

        return Math.hypot(cx - map.holeX, cy - map.holeY) < map.holeR;
    }




    private void finish(int id) {
        finishedBall = id;
        running = false;
        timer.stop();
        checkPrediction();
    }

    private void checkPrediction() {

        String msg = (finishedBall == predictedBall)
                ? "✅ Ball #" + finishedBall + " entered the hole first!\nYou guessed right!"
                : "❌ Ball #" + finishedBall + " entered the hole first!\nYou guessed wrong!";

        JOptionPane.showMessageDialog(this, msg);
    }




    /* =============================
     *  Vẽ
     ============================== */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        map.draw(g2d);

        for (Balls b : balls) {

            if (b.id == predictedBall) {
                Stroke old = g2d.getStroke();
                g2d.setStroke(new BasicStroke(3));
                g2d.setColor(Color.YELLOW);

                g2d.drawOval(
                        (int) (b.x - 2),
                        (int) (b.y - 2),
                        (int) (b.size + 4),
                        (int) (b.size + 4)
                );

                g2d.setStroke(old);
            }

            b.draw(g2d);
        }
    }
}
