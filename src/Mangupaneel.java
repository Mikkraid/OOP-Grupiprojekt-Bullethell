import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Mangupaneel extends JPanel implements ActionListener, KeyListener {
    private final int LAIUS = 800, KORGUS = 300;
    private Timer taimer;
    private Mangija mangija;
    private final List<Takistus> takistused;
    private long viimaneTakistuseAeg = 0;
    private int takistuseIntervalMax = 2000;
    private int takistuseInterval = getRandomNumber(500, takistuseIntervalMax);

    private final JLabel aegSilt;
    private final long aegStart;
    private String aegLopp;
    private final Timer aeg;

    private final List<Pilv> pilved = new ArrayList<>();
    private long viimanePilveAeg = 0;

    private boolean plahvatusAktiveeritud = false;
    private int plahvatusX = 0, plahvatusY = 0;

    public Mangupaneel() {
        this.setPreferredSize(new Dimension(LAIUS, KORGUS));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        mangija = new Mangija();
        takistused = new ArrayList<>();
        taimer = new Timer(10, this);
        taimer.start();

        aegStart = System.nanoTime();
        aegLopp = "";
        aegSilt = new JLabel(aegLopp);
        aegSilt.setFont(new Font("DialogInput", Font.BOLD, 24));
        aegSilt.setForeground(Color.WHITE);
        add(aegSilt);

        aeg = new Timer(16, e -> uuendaAeg());
        aeg.start();
    }

    private void uuendaAeg() {
        long läbitudAeg = System.nanoTime() - aegStart;
        double sekundid = läbitudAeg / 1_000_000_000.0;
        aegLopp = String.format("%.3f s", sekundid);
        aegSilt.setText(aegLopp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        uuendaMangu();
        repaint();
    }

    private void uuendaMangu() {
        if (plahvatusAktiveeritud) return; // Peata uuendused kui plahvatus toimub
        mangija.uuenda();
        haldaTakistusi();
        haldaPilvi();
        kontrolliKokkuporgeid();
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void haldaTakistusi() {
        long praeguneAeg = System.currentTimeMillis();
        if (praeguneAeg - viimaneTakistuseAeg > takistuseInterval) {
            takistused.add(new Takistus(LAIUS, getRandomNumber(20, KORGUS - 60), 20, 20));
            viimaneTakistuseAeg = praeguneAeg;
            takistuseInterval = getRandomNumber(500, takistuseIntervalMax);
            takistuseIntervalMax = Math.max(500, takistuseIntervalMax - 20);
        }
        takistused.forEach(Takistus::uuenda);
        takistused.removeIf(t -> t.x < -30);
    }

    private void haldaPilvi() {
        long praeguneAeg = System.currentTimeMillis();
        if (praeguneAeg - viimanePilveAeg > 1000) {
            int y = getRandomNumber(10, KORGUS - 80);
            int laius = getRandomNumber(60, 120);
            int korgus = getRandomNumber(30, 60);
            pilved.add(new Pilv(LAIUS, y, laius, korgus));
            viimanePilveAeg = praeguneAeg;
        }
        pilved.forEach(Pilv::uuenda);
        pilved.removeIf(Pilv::onVäljas);
    }

    private void kontrolliKokkuporgeid() {
        Rectangle mangijaPiirid = mangija.saadaPiirid();
        for (Takistus takistus : takistused) {
            if (takistus.saadaPiirid().intersects(mangijaPiirid)) {
                plahvatusX = mangija.x + 20;
                plahvatusY = mangija.y + 20;
                plahvatusAktiveeritud = true;
                taimer.stop();
                aeg.stop();
                new Timer(600, evt -> System.exit(0)).start(); // Oota enne sulgemist
                break;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        joonista(g);
    }

    public void joonista(Graphics g) {
        g.setColor(new Color(135, 206, 235));
        g.fillRect(0, 0, LAIUS, KORGUS);
        pilved.forEach(p -> p.joonista(g));
        mangija.joonista(g);
        takistused.forEach(t -> t.joonista(g));

        g.setColor(Color.GREEN);
        g.fillRect(0, KORGUS - 20, LAIUS, 20);

        if (plahvatusAktiveeritud) {
            g.setColor(Color.ORANGE);
            g.fillOval(plahvatusX - 30, plahvatusY - 30, 60, 60);
            g.setColor(Color.RED);
            g.fillOval(plahvatusX - 15, plahvatusY - 15, 30, 30);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        mangija.klahviVajutus(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mangija.klahviVabastus(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
