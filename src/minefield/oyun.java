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

public class oyun extends JFrame {

    private JButton[][] bombaalar; // mayin olacak butonlar
    private JButton duygu_butonum; // duygu butonum
    private JLabel bayrak_label; // bayrak sayimiz label
    private JLabel zaman_label; // zaman sayac label

    private int mayin_yok = 0; // Alanda mayin olmayacak buton sayisi
    public static final int SABIT_BOYUTLANDIRICI = 30; // Son deger olarak 30 kabul ettigimiz sabit boyutlandiricim
    private int[][] tarla; // mayin doseme alanimiz
    private JPanel ust_panel, oyun_paneli; // oyun panellerim
    private boolean[][] buldum; // tiklanip tiklanmama olayi
    private boolean[][] bayrak_kontrol; // bayrak kontrolu
    private int patlatilmayan; // bulunamayan mayinlar
    private boolean gulumseme; // oyun bitti mi

    private Image gulumse_foto, gulumse_2_foto, bayrak_foto, bayrak_2_foto, mayin_foto, mayin_2_foto, kayip_foto, kayip_2_foto;


    public oyun(int size, int difficulty) {
        mayin_yok = size*(1 + difficulty/2);
        this.setSize(size* SABIT_BOYUTLANDIRICI, size* SABIT_BOYUTLANDIRICI);
        this.setTitle("Kerkük Sınırı :D");
        setLocationRelativeTo(null); // Ortadan acil
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Sonlandirilma
    }

    private void mayinDoseme(int size) {
        Random rnd = new Random();

        tarla = new int[size][size]; // Kullanicidan aldigimiz veriler ile oyun alani olusturduk
        for (int i = 0; i < size; i++) {
            for (int j = 0; i < size; j++) {
                tarla[i][j] = 0; // Varsayilan bos kabul ediyoruz
            }
        }

        int adet = 0;
        int xNokta, yNokta;

        while (adet < mayin_yok) {
            xNokta = rnd.nextInt(size);
            yNokta = rnd.nextInt(size);

            if (tarla[xNokta][yNokta] != -1) {
                tarla[xNokta][yNokta] = -1; // Mayin doseme
                adet++;
            }
        }

        // Sinirlardaki alanlara bomba ve bayrak atma
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tarla[i][j] == -1) {
                    for (int k = -1; k <= 1; k++) {
                        for (int l = -1; l <= 1; l++) {
                            try {
                                if (tarla[i+k][j+l] != -1) {
                                    tarla[i+k][j+l] += 1; // Bayraklik temiz alan
                                }
                            } catch (Exception e) {
                                // Hata yakalama
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public void main(oyun frame, int size) {
        OyunMotoru oyun_motoru = new OyunMotoru(frame); // Oyun motorumu olusturuyorum
        FareyiIzle fareyi_izle = new FareyiIzle(frame); // Fare takibim
        JPanel ana_panelim = new JPanel();

        ust_panel = new JPanel();
        oyun_paneli = new JPanel();

        this.patlatilmayan = 0;

        buldum = new boolean[size][size];
        bayrak_kontrol = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // varsayilan false ile dolduruyoruz
                buldum[i][j] = false;
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
        } catch (Exception e) {
            // Hata yakalama
            e.printStackTrace();
        }

        ana_panelim.setLayout(new BoxLayout(ana_panelim, BoxLayout.Y_AXIS));

        BoxLayout g1 = new BoxLayout(ust_panel, BoxLayout.X_AXIS);
        ust_panel.setLayout(g1);

        JLabel jLabel1 = new JLabel("Bayrak = "); // bayrak sayimizi tutan label
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

        JLabel jLabel2 = new JLabel("Zaman: ");
        zaman_label = new JLabel("0");
        zaman_label.setAlignmentX(Component.RIGHT_ALIGNMENT); // saga yasla
        zaman_label.setHorizontalAlignment(JLabel.RIGHT);

        ust_panel.add(jLabel1);
        ust_panel.add(bayrak_label);
        ust_panel.add(Box.createRigidArea(new Dimension((size - 1)*15 - 80, 50)));
        ust_panel.add(duygu_butonum, BorderLayout.PAGE_START);
        ust_panel.add(Box.createRigidArea(new Dimension((size - 1)*15 - 85, 50)));
        ust_panel.add(jLabel2);
        ust_panel.add(zaman_label);

        GridLayout g2 = new GridLayout(size, size);
        oyun_paneli.setLayout(g2);

        bombaalar = new JButton[size][size];

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
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

        mayinDoseme(size);
    }

    public void sagTiklama (int x, int y) {
        if(!buldum[x][y]) {
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

    // herhangi bir butona tiklanma olayi
    public void butonTikla(int x, int y) {

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
}

class OyunMotoru implements ActionListener {
    oyun parent;

    OyunMotoru(oyun parent) { this.parent = parent; }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object kaynak_noktam = actionEvent.getSource();
        JButton tiklanan = (JButton) kaynak_noktam;
        String isim = tiklanan.getName();
        if (isim.equals("duygu_butonum")) {
            parent.durumDegistir();
        } else {
            String[] xy = tiklanan.getName().split(" ", 2); // tiklananin ismini al
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            parent.butonTikla(x, y);
        }
    }
}

class FareyiIzle implements MouseListener {
    oyun parent;

    FareyiIzle(oyun parent) { this.parent = parent; }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
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
