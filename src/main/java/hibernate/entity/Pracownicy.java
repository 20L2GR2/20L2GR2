package hibernate.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pracownicy")
/**
 * Klasa mapujaca tabele Pracownicy.
 */

public class Pracownicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pracownika", unique = true, nullable = false)
    private int idPracownika;
    @Column(name = "imie", nullable = false)
    private String imie;
    @Column(name = "nazwisko", nullable = false)
    private String nazwisko;
    @Column(name = "stanowisko", nullable = false)
    private short stanowisko;
    @Column(name = "login", nullable = false)
    private String login;
    @Column(name = "haslo", nullable = false)
    private String haslo;

    @OneToMany(mappedBy = "pracownik", cascade = CascadeType.ALL)
    private List<Zamowienia> zamowienieMechanik;

    @OneToMany(mappedBy = "pracownikMechanik")
    private List<Zlecenia> zlecenieMechanik;

    @OneToMany(mappedBy = "pracownikObslugaStart")
    private List<Zlecenia> zlecenieObslugaStart;

    @OneToMany(mappedBy = "pracownikObslugaKoniec")
    private List<Zlecenia> zlecenieObslugaKoniec;

    @PreRemove
    private void removeAssociationsWithChildren() {
        for (Zlecenia zlecenie:
             zlecenieObslugaKoniec) {
            zlecenie.setPracownikObslugaKoniec(null);
        }
        for (Zlecenia zlecenie :
                zlecenieObslugaStart) {
            zlecenie.setPracownikObslugaStart(null);
        }
        for (Zlecenia zlecenie :
                zlecenieMechanik) {
            zlecenie.setPracownikMechanik(null);
        }
    }

    /**
     * Konstruktor bezparamtrowy.
     */

    public Pracownicy() {
    }

    public int getIdPracownika() {
        return idPracownika;
    }

    public void setIdPracownika(int idPracownika) {
        this.idPracownika = idPracownika;
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

    public short getStanowisko() {
        return stanowisko;
    }

    public void setStanowisko(short stanowisko) {
        this.stanowisko = stanowisko;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public List<Zamowienia> getZamowienieMechanik() {
        return zamowienieMechanik;
    }

    public void setZamowienieMechanik(List<Zamowienia> zamowienieMechanik) {
        this.zamowienieMechanik = zamowienieMechanik;
    }

    public List<Zlecenia> getZlecenieMechanik() {
        return zlecenieMechanik;
    }

    public void setZlecenieMechanik(List<Zlecenia> zlecenieMechanik) {
        this.zlecenieMechanik = zlecenieMechanik;
    }

    public List<Zlecenia> getZlecenieObslugaStart() {
        return zlecenieObslugaStart;
    }

    public void setZlecenieObslugaStart(List<Zlecenia> zlecenieObslugaStart) {
        this.zlecenieObslugaStart = zlecenieObslugaStart;
    }

    public List<Zlecenia> getZlecenieObslugaKoniec() {
        return zlecenieObslugaKoniec;
    }

    public void setZlecenieObslugaKoniec(List<Zlecenia> zlecenieObslugaKoniec) {
        this.zlecenieObslugaKoniec = zlecenieObslugaKoniec;
    }

    @Override
    public String toString() {
        return "Pracownicy{" +
                "idPracownika=" + idPracownika +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", stanowisko=" + stanowisko +
                ", login='" + login + '\'' +
                ", haslo='" + haslo + '\'' +
                '}';
    }

    public String getStanowiskoToString() {
        switch (stanowisko){
            case 0 : return "admin";
            case 1 : return "Obsługa klienta";
            case 2 : return "Mechanik";
            case 3 : return "Magazynier";
        }
        return "Obsługa klienta";
    }
}