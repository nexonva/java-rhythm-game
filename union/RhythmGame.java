import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

class Note {
    int x, y;
    int speed;

    public Note(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void move() {
        y += speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 50, 50);
    }
}

public class RhythmGame extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<Note> notes;
    private int score;
    private boolean spaceLock;

    public RhythmGame() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        notes = new ArrayList<>();
        score = 0;
        spaceLock = false;

        timer = new Timer(16, this);
        timer.start();

        // Create notes periodically
        new Timer(1000, e -> addNote()).start();

        // Key listener for note hitting
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE && !spaceLock) {
                    checkHit();
                    spaceLock = true;
                    new Timer(400, e -> spaceLock = false).start();
                }
            }
        });
    }

    public void addNote() {
        notes.add(new Note(375, 0, 5));
    }

    public void checkHit() {
        Iterator<Note> iter = notes.iterator();
        while (iter.hasNext()) {
            Note note = iter.next();
            if (note.y > 500 && note.y < 550) { // Hit zone
                iter.remove();
                score++;
                System.out.println("Hit! Score: " + score);
                return;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Note note : notes) {
            note.draw(g);
        }
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 10);

        // Draw hit zone
        g.setColor(Color.GREEN);
        g.drawRect(375, 500, 50, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Note note : notes) {
            note.move();
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rhythm Game");
        RhythmGame game = new RhythmGame();

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitItem);
        menuBar.add(gameMenu);

        frame.setJMenuBar(menuBar);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
