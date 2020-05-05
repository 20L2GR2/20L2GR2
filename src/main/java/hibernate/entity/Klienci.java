package hibernate.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "klienci")
public class Klienci {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_klienta", unique = true, nullable = false)
    private int idKlienta;
    @Column(name = "imie_klienta", nullable = false)
    private String imie;
    @Column(name = "nazwisko_klienta", nullable = false)
    private String nazwisko;
    @Column(name = "nr_kontaktowy", nullable = false)
    private int nrKontakt;
    @Column(name = "marka_samochodu", nullable = false)
    private String marka;
    @Column(name = "model_samochodu", nullable = false)
    private String model;
    @Column(name = "numer_rejestracyjny", nullable = false)
    private String nrReje;

    @OneToMany(mappedBy = "klientZlecenie", cascade = CascadeType.ALL)
    private Set<Zlecenia> zlecenieKlient;

    public Klienci() {
    }

    public int getIdKlienta() {
        return idKlienta;
    }

    public void setIdKlienta(int idKlienta) {
        this.idKlienta = idKlienta;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public long getNrKontakt() {
        return nrKontakt;
    }

    public void setNrKontakt(int nrKontakt) {
        this.nrKontakt = nrKontakt;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNrReje() {
        return nrReje;
    }

    public void setNrReje(String nrReje) {
        this.nrReje = nrReje;
    }

    public Set<Zlecenia> getZlecenieKlient() {
        return zlecenieKlient;
    }

    public void setZlecenieKlient(Set<Zlecenia> zlecenieKlient) {
        this.zlecenieKlient = zlecenieKlient;
    }

    @Override
    public String toString() {
        return "Klienci{" +
                "idKlienta=" + idKlienta +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", nrKontakt=" + nrKontakt +
                ", marka='" + marka + '\'' +
                ", model='" + model + '\'' +
                ", nrReje='" + nrReje + '\'' +
                '}';
    }
}