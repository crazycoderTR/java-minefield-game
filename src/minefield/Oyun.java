package minefield;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.border.LineBorder;

public class Oyun extends JFrame {

    private JButton[][] bombaalar; // mayin olacak butonlar
    private JButton duygu_butonum; // duygu butonum
    private JLabel bayrak_label; // bayrak sayimiz label
    private JLabel zaman_label; // zaman sayac label

    private int mayin_yok = 0; // Alanda mayin olmayacak buton sayisi
    public static final int SABIT_BOYUTLANDIRICI = 30; // Son deger olarak 30 kabul ettigimiz sabit boyutlandiricim
    private int[][] tarla; // mayin doseme alanimiz
    private JPanel ust_panel, oyun_paneli; // oyun panellerim
    private boolean[][] tiklanan; // tiklanip tiklanmama olayi
    private boolean[][] bayrak_kontrol; // bayrak kontrolu
    private int patlatilmayan; // bulunamayan mayinlar
    private boolean gulumseme; // oyun bitti mi

    private Image gulumse_foto, gulumse_2_foto, bayrak_foto, bayrak_2_foto, mayin_foto, mayin_2_foto, kayip_foto, kayip_2_foto;


    public Oyun(int boyut, int zorluk) {
        mayin_yok = boyut*(1 + zorluk/2);
        this.setSize(boyut*SABIT_BOYUTLANDIRICI, boyut*SABIT_BOYUTLANDIRICI + 50);
        this.setTitle("Kerkük Sınırı :D");
        setLocationRelativeTo(null); // Ortadan acil
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Sonlandirilma
    }

    private void mayinDoseme(int boyut) {
        Random rnd = new Random();

        tarla = new int[boyut][boyut]; // Kullanicidan aldigimiz veriler ile oyun alani olusturduk
        for (int i = 0; i < boyut; i++) {
            for (int j = 0; j < boyut; j++) {
                tarla[i][j] = 0; // Varsayilan bos kabul ediyoruz
            }
        }

        int adet = 0;
        int xNokta, yNokta;

        while (adet < mayin_yok) {
            xNokta = rnd.nextInt(boyut);
            yNokta = rnd.nextInt(boyut);

            if (tarla[xNokta][yNokta] != -1) {
                tarla[xNokta][yNokta] = -1; // Mayin doseme
                adet++;
            }
        }

        // Sinirlardaki alanlara bomba ve bayrak atma
        for (int i = 0; i < boyut; i++) {
            for (int j = 0; j < boyut; j++) {
                if (tarla[i][j] == -1) {
                    for (int k = -1; k <= 1; k++) {
                        for (int l = -1; l <= 1; l++) {
                            try {
                                if (tarla[i+k][j+l] != -1) {
                                    tarla[i+k][j+l] += 1; // Bayraklik temiz alan
                                }
                            } catch (Exception e) {}
                        }
                    }
                }
            }
        }
    }

    public void main(Oyun frame, int boyut) {
        OyunMotoru oyun_motoru = new OyunMotoru(frame); // Oyun motorumu olusturuyorum
        FareyiIzle fareyi_izle = new FareyiIzle(frame); // Fare takibim
        JPanel ana_panelim = new JPanel();

        ust_panel = new JPanel();
        oyun_paneli = new JPanel();

        this.patlatilmayan = 0;

        tiklanan = new boolean[boyut][boyut];
        bayrak_kontrol = new boolean[boyut][boyut];
        for (int i = 0; i < boyut; i++) {
            for (int j = 0; j < boyut; j++) {
                // varsayilan false ile dolduruyoruz
                tiklanan[i][j] = false;
                bayrak_kontrol[i][j] = false;
            }
        }

        // Fotolar
        try {
            gulumse_foto = ImageIO.read(getClass().getResource("images/gulumse.png"));
            gulumse_2_foto = gulumse_foto.getScaledInstance(SABIT_BOYUTLANDIRICI, SABIT_BOYUTLANDIRICI, Image.SCALE_SMOOTH);

            kayip_foto = ImageIO.read(getClass().getResource("images/kayip.png"));
            kayip_2_foto = kayip_foto.getScaledInstance(SABIT_BOYUTLANDIRICI, SABIT_BOYUTLANDIRICI, Image.SCALE_SMOOTH);

            bayrak_foto = ImageIO.read(getClass().getResource("images/bayrak.png"));
            bayrak_2_foto = bayrak_foto.getScaledInstance(SABIT_BOYUTLANDIRICI, SABIT_BOYUTLANDIRICI, Image.SCALE_SMOOTH);

            mayin_foto = ImageIO.read(getClass().getResource("images/mayin.png"));
            mayin_2_foto = mayin_foto.getScaledInstance(SABIT_BOYUTLANDIRICI, SABIT_BOYUTLANDIRICI, Image.SCALE_SMOOTH);
        } catch (Exception e) {}

        ana_panelim.setLayout(new BoxLayout(ana_panelim, BoxLayout.Y_AXIS));

        BoxLayout g1 = new BoxLayout(ust_panel, BoxLayout.X_AXIS);
        ust_panel.setLayout(g1);

        JLabel jLabel1 = new JLabel(" Bayrak = "); // bayrak sayimizi tutan label
        jLabel1.setAlignmentX(Component.LEFT_ALIGNMENT); // sola yasla
        jLabel1.setHorizontalAlignment(JLabel.LEFT);
        bayrak_label = new JLabel("" + this.mayin_yok);

        gulumseme = true;
        duygu_butonum = new JButton(new ImageIcon(gulumse_2_foto));
        duygu_butonum.setPreferredSize(new Dimension(SABIT_BOYUTLANDIRICI, SABIT_BOYUTLANDIRICI));
        duygu_butonum.setMaximumSize(new Dimension(SABIT_BOYUTLANDIRICI, SABIT_BOYUTLANDIRICI));
        duygu_butonum.setBorderPainted(true);
        duygu_butonum.setName("duygu_butonum");
        duygu_butonum.addActionListener(oyun_motoru);

        JLabel jLabel2 = new JLabel(" Zaman: ");
        zaman_label = new JLabel("0");
        zaman_label.setAlignmentX(Component.RIGHT_ALIGNMENT); // saga yasla
        zaman_label.setHorizontalAlignment(JLabel.RIGHT);

        ust_panel.add(jLabel1);
        ust_panel.add(bayrak_label);
        ust_panel.add(Box.createRigidArea(new Dimension((boyut - 1)*15 - 90, 50)));
        ust_panel.add(duygu_butonum, BorderLayout.PAGE_START);
        ust_panel.add(Box.createRigidArea(new Dimension((boyut - 1)*15 - 95, 50)));
        ust_panel.add(jLabel2);
        ust_panel.add(zaman_label);

        GridLayout g2 = new GridLayout(boyut, boyut);
        oyun_paneli.setLayout(g2);

        bombaalar = new JButton[boyut][boyut];

        for (int i = 0; i < boyut; i++){
            for (int j = 0; j < boyut; j++) {
                bombaalar[i][j] = new JButton();
                bombaalar[i][j].setPreferredSize(new Dimension(12, 12));
                bombaalar[i][j].setBorder(new LineBorder(Color.BLACK));
                bombaalar[i][j].setBorderPainted(true);
                bombaalar[i][j].setName(i + " " + j);
                bombaalar[i][j].addActionListener(oyun_motoru);
                bombaalar[i][j].addMouseListener(fareyi_izle);
                oyun_paneli.add(bombaalar[i][j]);
            }
        }

        // panellerimiz hazir; birisi ust birisi oyun paneli

        ana_panelim.add(ust_panel);
        ana_panelim.add(oyun_paneli);
        frame.setContentPane(ana_panelim);
        this.setVisible(true);

        mayinDoseme(boyut);

        // timer baslat
        Zamanlayici zamanlayici = new Zamanlayici(this);
        zamanlayici.baslat();
    }

    public void sagTiklama (int x, int y) {
        if(!tiklanan[x][y]) {
            if (bayrak_kontrol[x][y]) {
                bombaalar[x][y].setIcon(null);
                bayrak_kontrol[x][y] = false;
                int old = Integer.parseInt(this.bayrak_label.getText());
                ++old;
                this.bayrak_label.setText("" + old);
            } else {
                if (Integer.parseInt(this.bayrak_label.getText()) > 0) {
                    bombaalar[x][y].setIcon(new ImageIcon(bayrak_2_foto));
                    bayrak_kontrol[x][y] = true;
                    int old = Integer.parseInt(this.bayrak_label.getText());
                    --old;
                    this.bayrak_label.setText("" + old);
                }
            }
        }
    }

    private boolean oyunKazanma() {
        // patlatilmayan + mayin_yok toplami toplam alandaki buton sayisina esit olmali ki bitsin
        return (this.patlatilmayan) == (Math.pow(this.tarla.length, 2) - this.mayin_yok);
    }

    // herhangi bir butona tiklanma olayi
    public void butonTikla(int x, int y) {
        if (!tiklanan[x][y] && !bayrak_kontrol[x][y]) {
            tiklanan[x][y] = true;

            switch (tarla[x][y]) {
                case -1:
                    // mayin olma durumu
                    try {
                        bombaalar[x][y].setIcon(new ImageIcon(mayin_2_foto)); // mayina denk geldi
                    } catch (Exception e) {}
                    bombaalar[x][y].setBackground(Color.RED);
                    try {
                        duygu_butonum.setIcon(new ImageIcon(kayip_2_foto));
                    } catch (Exception e) {}

                    JOptionPane.showMessageDialog(this, "Kaybettiniz, başka zaman!!", null, JOptionPane.ERROR_MESSAGE);

                    System.exit(0);

                    break;

                case 0:
                    bombaalar[x][y].setBackground(Color.lightGray);
                    ++this.patlatilmayan;

                    // patlamadi alan acildi

                    if (oyunKazanma()) {
                        // belkide oyun kazanildi
                        JOptionPane.showMessageDialog(rootPane, "Kazandınız oyun sizindir :D"); // kazanma durumu
                        System.exit(0);
                    }

                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            try {
                                butonTikla(x + i, y + j);
                            } catch (Exception e) {}
                        }
                    }
                    break;

                default:
                    // varsayilan durum
                    bombaalar[x][y].setText(Integer.toString(tarla[x][y]));
                    bombaalar[x][y].setBackground(Color.lightGray);
                    ++this.patlatilmayan;

                    if (oyunKazanma()) {
                        JOptionPane.showMessageDialog(rootPane, "Kazandınız oyun sizindir :D"); // kazanma durumu
                        System.exit(0);
                    }

                    break;
            }
        }
    }

    // Ikon butonuna tiklanma durumunda duygu degistirme
    public void durumDegistir() {
        if (gulumseme) {
            gulumseme = false;
            duygu_butonum.setIcon(new ImageIcon(kayip_2_foto));
        } else {
            gulumseme = true;
            duygu_butonum.setIcon(new ImageIcon(gulumse_2_foto));
        }
    }

    // Zamani arttir
    public void zamanlayici() {
        String[] zaman_text = this.zaman_label.getText().split(" ");
        int zaman = Integer.parseInt(zaman_text[0]);
        ++zaman;
        this.zaman_label.setText(Integer.toString(zaman) + " s");
    }
}

class OyunMotoru implements ActionListener {
    Oyun parent;

    OyunMotoru(Oyun parent) { this.parent = parent; }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object kaynak_noktam = actionEvent.getSource();
        JButton tiklanan_buton = (JButton) kaynak_noktam;
        String isim = tiklanan_buton.getName();
        if (isim.equals("duygu_butonum")) {
            parent.durumDegistir();
        } else {
            String[] xy = tiklanan_buton.getName().split(" ", 2); // tiklananin ismini al
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            parent.butonTikla(x, y);
        }
    }
}

class FareyiIzle implements MouseListener {
    Oyun parent;

    FareyiIzle(Oyun parent) { this.parent = parent; }

    public void mouseClicked(MouseEvent mouseEvent) {}
    public void mousePressed(MouseEvent mouseEvent) {}
    public void mouseEntered(MouseEvent mouseEvent) {}
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(SwingUtilities.isRightMouseButton(mouseEvent)) {
            Object kaynak_noktam = mouseEvent.getSource();
            JButton tiklanan_buton = (JButton) kaynak_noktam;
            String[] xy = tiklanan_buton.getName().split(" ", 2); // tiklananin ismini al
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            parent.sagTiklama(x, y);
        }
    }
}

class Zamanlayici implements Runnable {
    private Thread t;
    private Oyun yeni_oyun;

    Zamanlayici(Oyun yeni_oyun) { this.yeni_oyun = yeni_oyun; }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // 1 saniye
                yeni_oyun.zamanlayici();
            } catch (Exception e) {
                System.exit(0);
            }
        }
    }

    public void baslat() {
        if (t == null) {
            t = new Thread(this); // burayi isaret goster oyun alanini
            t.start();
        }
    }
}
