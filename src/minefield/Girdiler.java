package minefield;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Girdiler extends JFrame {

    final private MayinTarlasi mayin_tarlasi; // oyuna referans etmemiz lazim
    private GirdiTut girdi_tut;
    private int boyut;
    private JPanel panel;
    private JLabel label;
    private JTextField text;


    public Girdiler(MayinTarlasi mayin_tarlasi) {
        this.mayin_tarlasi = mayin_tarlasi;
        this.setSize(400, 100);
        this.setTitle("Girdi");
        setLocationRelativeTo(null); // ekranin ortasinda baslat
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // ekranin kapatilma durumu
    }

    // Getter & Setter
    public void set(int n) {
        boyut = n;
        mayin_tarlasi.oyun(boyut);
    }

    public int get() { return boyut; }

    public void main(Girdiler girdiler) {
        girdi_tut = new GirdiTut(girdiler); // girdi instance mizi olusturduk

        boyut = 0;

        panel = new JPanel();

        label = new JLabel("Kaçlık Tarla İstersiniz?");
        panel.add(label);

        text = new JTextField(30);
        text.addActionListener(girdi_tut);
        panel.add(text);

        girdiler.setContentPane(panel); // girdi panelimizi olusturuyoruz
        this.setVisible(true);
    }
}

class GirdiTut implements ActionListener {
    Girdiler parent;

    GirdiTut(Girdiler parent) { this.parent = parent; }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object kaynak_noktam = actionEvent.getSource();
        JTextField text = (JTextField) kaynak_noktam;
        String girdi = "0";
        int boyut = 0;

        while (true) {
            try {
                girdi = text.getText();
                boyut = Integer.parseInt(girdi);
                if (boyut <= 6) {
                    JOptionPane.showMessageDialog(parent,
                            "Büyük veriler giriniz, çocuk parkı mı burası", "Hatalı Girdi!",
                            JOptionPane.ERROR_MESSAGE);
                    text.setText("");
                    break;
                } else {
                    parent.setVisible(false);
                    parent.set(boyut);
                }
                break;
            } catch (NumberFormatException | HeadlessException e) {
                JOptionPane.showMessageDialog(parent, "Tutarlı Veriler Giriniz!", "Hatalı Girdi", JOptionPane.ERROR_MESSAGE);
                text.setText("");
                break;
            }
        }
    }
}
