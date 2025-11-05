package bouncingballs;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private GamePanel panel;
    private JComboBox<Integer> ballChooser;
    private JComboBox<Integer> countChooser;
    private JButton startBtn, pauseBtn, resetBtn, nextMapBtn, exitBtn;
    private int mapId = 1;

    public MainFrame() {

        setTitle("🎱 Bouncing Balls Prediction Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel cp = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        cp.add(new JLabel("Predict Ball #:"));
        ballChooser = new JComboBox<>();
        for (int i = 1; i <= 200; i++) ballChooser.addItem(i);
        cp.add(ballChooser);

        cp.add(new JLabel("Ball Count:"));
        countChooser = new JComboBox<>();
        for (int i = 8; i <= 200; i += 8) countChooser.addItem(i);
        cp.add(countChooser);

        startBtn = new JButton("Start");
        pauseBtn = new JButton("Pause");
        resetBtn = new JButton("Reset");
        nextMapBtn = new JButton("Next Map");
        exitBtn = new JButton("Exit");

        cp.add(startBtn);
        cp.add(pauseBtn);
        cp.add(resetBtn);
        cp.add(nextMapBtn);
        cp.add(exitBtn);

        add(cp, BorderLayout.NORTH);

        panel = new GamePanel(mapId, -1, 8);
        add(panel, BorderLayout.CENTER);

        startBtn.addActionListener(e -> {
            int predicted = (Integer) ballChooser.getSelectedItem();
            panel.setPredictedBall(predicted);
            panel.startGame();
        });

        pauseBtn.addActionListener(e -> panel.pauseGame());

        resetBtn.addActionListener(e -> {
            int count = (Integer) countChooser.getSelectedItem();
            panel.setBallCount(count);
            panel.setPredictedBall(-1);
            JOptionPane.showMessageDialog(this,
                    "Game reset!\nSelect your predicted ball again before pressing Start.");
        });

        nextMapBtn.addActionListener(e -> {
            mapId = mapId % 3 + 1;
            int count = (Integer) countChooser.getSelectedItem();
            panel.pauseGame();
            getContentPane().remove(panel);
            panel = new GamePanel(mapId, -1, count);
            add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();
            JOptionPane.showMessageDialog(this,
                    "Switched to Map #" + mapId + ".\nSelect your predicted ball again!");
        });

        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?", "Exit Game", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
