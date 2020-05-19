package minefield;

import javax.swing.*;

public class MayinTarlasi {
    private static Girdiler Girdiler_;
    private static Oyun YeniOyun;
    private static MayinTarlasi MayinTarlasi_;

    public void baslat(MayinTarlasi mayinTarlasi) {
        Girdiler_ = new Girdiler(mayinTarlasi);
        Girdiler_.main(Girdiler_);
    }

    public void oyun (int boyut) {
        int zorluk = 1;
        Object[] ayarlar = {"Kolay", "Orta", "Babaaa"};
        zorluk = JOptionPane.showOptionDialog(null, "Zorluk Derecesi Seçiniz!", "Zorluk", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, ayarlar, ayarlar[1]);
        if (zorluk == -1) {
            System.exit(0); // ben boyle bir secenek sunmadim hoop disarii
        }
        YeniOyun = new Oyun(boyut, zorluk); // yeni oyun ayarlarini iletiyoruz
        YeniOyun.main(YeniOyun, boyut); // yeni oyun baslatma
    }

    public static void main(String[] args) {
        MayinTarlasi_ = new MayinTarlasi(); // oyun instance imizi olusturduk
        MayinTarlasi_.baslat(MayinTarlasi_); // oyun baslattik
    }
}
