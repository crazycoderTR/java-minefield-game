package minefield;

import javax.swing.*;

public class MayinTarlasi {
    private static Girdiler girdiler;
    private static Oyun yeni_oyun;
    private static MayinTarlasi mayin_tarlasi;

    public void baslat(MayinTarlasi mayin_tarlasi) {
        girdiler = new Girdiler(mayin_tarlasi);
        girdiler.main(girdiler);
    }

    public void oyun (int boyut) {
        int zorluk = 1;
        Object[] ayarlar = {"Kolay", "Orta", "Babaaa"};
        zorluk = JOptionPane.showOptionDialog(null, "Zorluk Derecesi Seçiniz!", "Zorluk", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, ayarlar, ayarlar[1]);
        if (zorluk == -1) {
            System.exit(0); // ben boyle bir secenek sunmadim hoop disarii
        }
        yeni_oyun = new Oyun(boyut, zorluk); // yeni oyun ayarlarini iletiyoruz
        yeni_oyun.main(yeni_oyun, boyut); // yeni oyun baslatma
    }

    public static void main(String[] args) {
        mayin_tarlasi = new MayinTarlasi(); // oyun instance imizi olusturduk
        mayin_tarlasi.baslat(mayin_tarlasi); // oyun baslattik
    }
}
