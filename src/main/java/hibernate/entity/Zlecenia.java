package hibernate.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "zlecenia")
public class Zlecenia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zlecenie", unique = true, nullable = false)
    private int idZlecenia;
    @Column(name = "opis_usterki", nullable = false)
    private String opisUsterki;
    @Column(name = "data_rozpoczecia")
    private java.util.Date dataRozpoczecia;
    @Column(name = "data_zakonczenia")
    private java.util.Date dataZakonczenia;
    @Column(name = "opis_naprawy")
    private String opisNaprawy;
    @Column(name = "uzyte_czesci")
    private String uzyteCzesci;
    @Column(name = "cena", nullable = false)
    private Float cena;

    public int getStanZlecenia() {
        return stanZlecenia;
    }

    public void setStanZlecenia(int stanZlecenia) {
        this.stanZlecenia = stanZlecenia;
    }

    @Column(name = "stan_zlecenia", nullable = false)
    private int stanZlecenia;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_klienta")
    private Klienci klientZlecenie;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_mechanika")
    private Pracownicy pracownikMechanik;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_obslugaklientastart")
    private Pracownicy pracownikObslugaStart;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_obslugaklientakoniec")
    private Pracownicy pracownikObslugaKoniec;

    public Zlecenia() {
    }

    public long getIdZlecenia() {
        return idZlecenia;
    }

    public void setIdZlecenia(int idZlecenia) {
        this.idZlecenia = idZlecenia;
    }

    public String getOpisUsterki() {
        return opisUsterki;
    }

    public void setOpisUsterki(String opisUsterki) {
        this.opisUsterki = opisUsterki;
    }

    public Date getDataRozpoczecia() {
        return dataRozpoczecia;
    }

    public void setDataRozpoczecia(Date dataRozpoczecia) {
        this.dataRozpoczecia = dataRozpoczecia;
    }

    public Date getDataZakonczenia() {
        return dataZakonczenia;
    }

    public void setDataZakonczenia(Date dataZakonczenia) {
        this.dataZakonczenia = dataZakonczenia;
    }

    public String getOpisNaprawy() {
        return opisNaprawy;
    }

    public void setOpisNaprawy(String opisNaprawy) {
        this.opisNaprawy = opisNaprawy;
    }

    public String getUzyteCzesci() {
        return uzyteCzesci;
    }

    public void setUzyteCzesci(String uzyteCzesci) {
        this.uzyteCzesci = uzyteCzesci;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(Float cena) {
        this.cena = cena;
    }

    public Klienci getKlientZlecenie() {
        return klientZlecenie;
    }

    public void setKlientZlecenie(Klienci klientZlecenie) {
        this.klientZlecenie = klientZlecenie;
    }

    public Pracownicy getPracownikMechanik() {
        return pracownikMechanik;
    }

    public void setPracownikMechanik(Pracownicy pracownikMechanik) {
        this.pracownikMechanik = pracownikMechanik;
    }

    public Pracownicy getPracownikObslugaStart() {
        return pracownikObslugaStart;
    }

    public void setPracownikObslugaStart(Pracownicy pracownikObslugaStart) {
        this.pracownikObslugaStart = pracownikObslugaStart;
    }

    public Pracownicy getPracownikObslugaKoniec() {
        return pracownikObslugaKoniec;
    }

    public void setPracownikObslugaKoniec(Pracownicy pracownikObslugaKoniec) {
        this.pracownikObslugaKoniec = pracownikObslugaKoniec;
    }

    @Override
    public String toString() {
        return "Zlecenia{" +
                "idZlecenia=" + idZlecenia +
                ", opisUsterki='" + opisUsterki + '\'' +
                ", dataRozpoczecia=" + dataRozpoczecia +
                ", dataZakonczenia=" + dataZakonczenia +
                ", opisNaprawy='" + opisNaprawy + '\'' +
                ", uzyteCzesci='" + uzyteCzesci + '\'' +
                ", cena=" + cena +
                '}';
    }
}