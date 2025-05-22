import java.awt.*;

public class Takistus {
    int x, y, laius, korgus;

    public Takistus(int x, int y, int laius, int korgus) {
        this.x = x;
        this.y = y;
        this.laius = laius;
        this.korgus = korgus;
    }

    public void uuenda() {
        x -= 5;
    }

    public void joonista(Graphics g) {
        g.setColor(new Color(50, 50, 50, 100));
        g.fillOval(x + laius, y + korgus / 2 - 5, 10, 10);

        g.setColor(Color.BLACK);
        g.fillOval(x, y, laius, korgus);
    }

    public Rectangle saadaPiirid() {
        return new Rectangle(x, y, laius, korgus);
    }
}
