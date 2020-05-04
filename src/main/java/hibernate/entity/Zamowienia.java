package hibernate.entity;

import javax.persistence.*;

@Entity
@Table(name = "zamowienia")
public class Zamowienia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zamowienia", unique = true, nullable = false)
    private int idZamowienia;
    @Column(name = "nazwa_czesci", nullable = false)
    private String nazwaCzesci;
    @Column(name = "kometarz", nullable = false)
    private String komentarz;
    @Column(name = "stan_zamowienia", nullable = false)
    private short stanZamowienia;

    @ManyToOne
    @JoinColumn(name = "id_mechanika")
    private Pracownicy pracownik;


    public Zamowienia() {
    }

    public long getIdZamowienia() {
        return idZamowienia;
    }

    public void setIdZamowienia(int idZamowienia) {
        this.idZamowienia = idZamowienia;
    }

    public String getNazwaCzesci() {
        return nazwaCzesci;
    }

    public void setNazwaCzesci(String nazwaCzesci) {
        this.nazwaCzesci = nazwaCzesci;
    }

    public String getKomentarz() {
        return komentarz;
    }

    public void setKomentarz(String komentarz) {
        this.komentarz = komentarz;
    }

    public short getStanZamowienia() {
        return stanZamowienia;
    }

    public void setStanZamowienia(short stanZamowienia) {
        this.stanZamowienia = stanZamowienia;
    }

    public Pracownicy getPracownik() {
        return pracownik;
    }

    public void setPracownik(Pracownicy pracownik) {
        this.pracownik = pracownik;
    }

    @Override
    public String toString() {
        return "Zamowienia{" +
                "idZamowienia=" + idZamowienia +
                ", nazwaCzesci='" + nazwaCzesci + '\'' +
                ", komentarz='" + komentarz + '\'' +
                ", stanZamowienia=" + stanZamowienia +
                '}';
    }


    public String getPracownikImie() {
        return pracownik.getImie();
    }

    public String getImieNazwisko() {
        return pracownik.getImie() + " " + pracownik.getNazwisko();
    }


}