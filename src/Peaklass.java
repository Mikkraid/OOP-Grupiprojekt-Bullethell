import javax.swing.*;
import java.awt.*;

public class Peaklass extends JFrame {
    public Peaklass() {
        this.add(new Mangupaneel());
        this.setTitle("Mäng");
        this.setResizable(false);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Peaklass::new);
    }
}
