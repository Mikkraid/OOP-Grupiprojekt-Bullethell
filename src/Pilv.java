import java.awt.*;

public class Pilv {
    int x, y, laius, korgus;

    public Pilv(int x, int y, int laius, int korgus) {
        this.x = x;
        this.y = y;
        this.laius = laius;
        this.korgus = korgus;
    }

    public void uuenda() {
        x -= 1;
    }

    public void joonista(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, laius, korgus);
    }

    public boolean onVäljas() {
        return x + laius < 0;
    }
}
