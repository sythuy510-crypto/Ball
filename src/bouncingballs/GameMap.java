package bouncingballs;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameMap {

    public List<Rectangle> walls = new ArrayList<>();
    public double holeX, holeY;
    public int holeR = 25;

    public GameMap(int mapId, int width, int height) {

        walls.clear();

        // ========== Tường bao xung quanh ==========
        int W = 14;
        walls.add(new Rectangle(0, 0, width, W));                 // top
        walls.add(new Rectangle(0, height - W, width, W));        // bottom
        walls.add(new Rectangle(0, 0, W, height));                // left
        walls.add(new Rectangle(width - W, 0, W, height));        // right


        switch (mapId) {

            /* ===================================
                  ✅ MAP 1: chỉ 1 lỗ ở giữa
            =================================== */
            case 1 -> {
                holeX = width / 2.0;
                holeY = height / 2.0;
            }


            /* ===================================
                  ✅ MAP 2:
                  - 2 tường NẰM NGANG sole
                  - Hố dưới giữa
            =================================== */
            case 2 -> {

                holeX = width / 2.0;
                holeY = height * 0.80;

                int TW = 200;   // chiều dài tường
                int TH = 16;    // độ dày tường

                // Tường 1: bên trên – lệch trái
                walls.add(new Rectangle(
                        (int) (width * 0.10),
                        (int) (height * 0.30),
                        TW,
                        TH
                ));

                // Tường 2: bên dưới – lệch phải
                walls.add(new Rectangle(
                        (int) (width * 0.60),
                        (int) (height * 0.50),
                        TW,
                        TH
                ));
            }


            /* ===================================
                  ✅ MAP 3:
                  - 2 tường DỌC sole
                  - (hố giữ nguyên từ map 1)
            =================================== */
            case 3 -> {

                // lỗ đặt giữa
                holeX = width / 2.0;
                holeY = height / 2.0;

                int TW = 16;   // độ dày tường
                int TH = 250;  // chiều cao tường

                // Tường dọc 1 – lệch trên
                walls.add(new Rectangle(
                        (int) (width * 0.30),
                        (int) (height * 0.10),
                        TW,
                        TH
                ));

                // Tường dọc 2 – lệch dưới
                walls.add(new Rectangle(
                        (int) (width * 0.60),
                        (int) (height * 0.45),
                        TW,
                        TH
                ));
            }


            /* =================================== */
            default -> {
                holeX = width / 2.0;
                holeY = height - 50;
            }
        }
    }


    public void draw(Graphics2D g2d) {

        // Vẽ tường bao + tường khác
        g2d.setColor(Color.WHITE);
        for (Rectangle w : walls) {
            g2d.fill(w);
        }

        // Hố đen
        g2d.setColor(Color.BLACK);
        g2d.fillOval(
                (int) (holeX - holeR),
                (int) (holeY - holeR),
                holeR * 2,
                holeR * 2
        );
    }
}
