package bouncingballs;

import java.awt.*;
import java.util.Random;

public class Balls {
    // vị trí (double cho mượt), vận tốc
    public double x, y;
    public double vx, vy;
    public int size = 18;   // mặc định (GamePanel có thể ghi đè)
    public int id;
    public Color color;

    private static final Random rnd = new Random();
    private static final double DAMPING = 0.98; // giảm nhẹ vận tốc khi bounce

    public Balls(int x, int y, double vx, double vy, int id) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.id = id;

        // màu ngẫu nhiên nhưng không quá tối
        this.color = new Color(
                60 + rnd.nextInt(196),
                60 + rnd.nextInt(196),
                60 + rnd.nextInt(196)
        );
    }

    // di chuyển theo vx, vy
    public void move() {
        x += vx;
        y += vy;
    }

    // đảo chiều theo trục X và giảm nhẹ, đồng thời đẩy ra khỏi chỗ chạm
    public void bounceX() {
        vx = -vx * DAMPING;
        // đẩy ra chút để tránh dính (tùy direction)
        if (vx > 0) x += 1;
        else x -= 1;
    }

    // đảo chiều theo trục Y và giảm nhẹ, đồng thời đẩy ra khỏi chỗ chạm
    public void bounceY() {
        vy = -vy * DAMPING;
        if (vy > 0) y += 1;
        else y -= 1;
    }

    // Nếu bạn dùng phiên bản GamePanel với resolveWallX/resolveWallY,
    // bạn có thể thêm các method đó; không bắt buộc ở đây.

    // trả về bounding box (dùng int vì Graphics yêu cầu)
    public Rectangle getBounds() {
        return new Rectangle((int)Math.round(x), (int)Math.round(y), size, size);
    }

    // khoảng cách từ tâm bóng tới điểm (tx,ty)
    public double distance(double tx, double ty) {
        double cx = x + size / 2.0;
        double cy = y + size / 2.0;
        return Math.hypot(cx - tx, cy - ty);
    }

    // vẽ bóng và số id
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int)Math.round(x), (int)Math.round(y), size, size);

        g.setColor(Color.BLACK);
        g.drawOval((int)Math.round(x), (int)Math.round(y), size, size);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        String s = String.valueOf(id);
        FontMetrics fm = g.getFontMetrics();
        int tx = (int) Math.round(x + (size - fm.stringWidth(s)) / 2.0);
        int ty = (int) Math.round(y + (size + fm.getAscent()) / 2.0) - 2;
        g.drawString(s, tx, ty);
    }
}
