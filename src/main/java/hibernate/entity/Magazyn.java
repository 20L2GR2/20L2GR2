package hibernate.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "magazyn")

/**
 * Klasa mapujaca tabele Magazyn.
 */

public class Magazyn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_czesci", unique = true, nullable = false)
    private int idCzesci;

    @Column(name = "nazwa_czesci", nullable = false)
    private String nazwaCzesci;

    @Column(name = "opis_czesci")
    private String opisCzesci;

    @Column(name = "ilosc", nullable = false)
    private int ilosc;

    @Column(name = "cena", nullable = false)
    private float cena;

    @OneToMany(mappedBy = "czesc")
    private List<Zamowienia> czesc;

    /**
     * Konstruktor bezparametrowy.
     */

    public Magazyn() {
    }

    public Magazyn(String nazwaCzesci, String opisCzesci, int ilosc, float cena) {
        this.nazwaCzesci = nazwaCzesci;
        this.opisCzesci = opisCzesci;
        this.ilosc = ilosc;
        this.cena = cena;
    }

    public long getIdCzesci() {
        return idCzesci;
    }

    public void setIdCzesci(int idCzesci) {
        this.idCzesci = idCzesci;
    }

    public String getNazwaCzesci() {
        return nazwaCzesci;
    }

    public void setNazwaCzesci(String nazwaCzesci) {
        this.nazwaCzesci = nazwaCzesci;
    }

    public String getOpisCzesci() {
        return opisCzesci;
    }

    public void setOpisCzesci(String opisCzesci) {
        this.opisCzesci = opisCzesci;
    }

    public long getIlosc() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }

    @Override
    public String toString() {
        return "Magazyn{" +
                "id=" + idCzesci +
                ", nazwa='" + nazwaCzesci + '\'' +
                ", opis='" + opisCzesci + '\'' +
                ", ilosc=" + ilosc +
                ", cena=" + cena +
                '}';
    }
}