package minefield;

import javax.swing.*;
import java.util.Random;

public class oyun extends JFrame {

    private int mayin_yok = 0; // Alanda mayin olmayacak buton sayisi
    public static final int SABIT_BOYUTLANDIRICI = 30; // Son deger olarak 30 kabul ettigimiz sabit boyutlandiricim
    private int[][] tarla; // mayin doseme alanimiz


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


}


