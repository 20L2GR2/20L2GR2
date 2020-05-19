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
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dataRozpoczecia;
    @Column(name = "data_zakonczenia")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dataZakonczenia;
    @Column(name = "stan_zlecenia", nullable = false)
    private int stanZlecenia;
    @Column(name = "opis_naprawy")
    private String opisNaprawy;
    @Column(name = "uzyte_czesci")
    private String uzyteCzesci;
    @Column(name = "cena")
    private Float cena;

    @ManyToOne
    @JoinColumn(name = "id_klienta")
    private Klienci klientZlecenie;

    @ManyToOne
    @JoinColumn(name = "id_mechanika")
    private Pracownicy pracownikMechanik;

    @ManyToOne
    @JoinColumn(name = "id_obslugaklientastart")
    private Pracownicy pracownikObslugaStart;

    @ManyToOne
    @JoinColumn(name = "id_obslugaklientakoniec")
    private Pracownicy pracownikObslugaKoniec;

    public Zlecenia() {
    }

    public int getIdZlecenia() {
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

    public int getStanZlecenia() {
        return stanZlecenia;
    }

    public void setStanZlecenia(int stanZlecenia) {
        this.stanZlecenia = stanZlecenia;
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

    public Float getCena() {
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

    public void setPracownikObslugaKoniec(Pracownicy pracownikObslugaKoniec) {
        this.pracownikObslugaKoniec = pracownikObslugaKoniec;
    }

    public Pracownicy getPracownikObslugaKoniec() {
        return pracownikObslugaKoniec;
    }


    public String getImieNazwisko() {
        return klientZlecenie.getImie() + " " + klientZlecenie.getNazwisko();
    }

    public String getNrReje() {
        return klientZlecenie.getNrReje();
    }

    public int getIdKlienta() {
        return klientZlecenie.getIdKlienta();
    }

    public String getMarkaModel() {
        return klientZlecenie.getMarka() + " " + klientZlecenie.getModel();
    }

    public String getImieNazwiskoMechanik() {
        return pracownikMechanik.getImie() + " " + pracownikMechanik.getNazwisko();
    }

    public String getImieNazwiskoObslugaPoczatek() {
        return pracownikObslugaStart.getImie() + " " + pracownikObslugaStart.getNazwisko();
    }

    @Override
    public String toString() {
        return "Zlecenia{" +
                "idZlecenia=" + idZlecenia +
                ", opisUsterki='" + opisUsterki + '\'' +
                ", dataRozpoczecia=" + dataRozpoczecia +
                ", dataZakonczenia=" + dataZakonczenia +
                ", stanZlecenia=" + stanZlecenia +
                ", opisNaprawy='" + opisNaprawy + '\'' +
                ", uzyteCzesci='" + uzyteCzesci + '\'' +
                ", cena=" + cena +
                ", klientZlecenie=" + klientZlecenie +
                ", pracownikMechanik=" + pracownikMechanik +
                ", pracownikObslugaStart=" + pracownikObslugaStart +
                ", pracownikObslugaKoniec=" + pracownikObslugaKoniec +
                '}';
    }

    public String getKlientImie(){
        return klientZlecenie.getImie();
    }

    public String getMechanikLogin(){
        return pracownikMechanik.getLogin();
    }

    public String getSZleceniaLogin(){
        return pracownikObslugaStart.getLogin();
    }

    public String getEZleceniaLogin(){
        return pracownikObslugaKoniec.getLogin();
    }

    public String getImieNazwiskoObslugaKoniec() {
        return pracownikObslugaKoniec.getImie() + " " + pracownikObslugaKoniec.getNazwisko();
    }

    public String getStanZleceniaToString(){
        switch(stanZlecenia){
            case 0: return "zlecenie utworzone i oczekujące do przyjęcia przez mechanika";
            case 1: return "zlecenie przyjęte przez mechanika i w trakcie realizacji";
            case 2: return "zlecenie oczekujące do wyceny (mechanik wykonał naprawę)";
            case 3: return "zlecenie zakończone";
            case 4: return "zlecenie anulowane";
            default: return "ZŁY STAN";
        }
    }
}