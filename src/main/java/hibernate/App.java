package hibernate;

import hibernate.entity.*;
import hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class App {
    public static void main(String[] args) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Pracownicy pracownik = new Pracownicy();
            Pracownicy pracownik2 = new Pracownicy();
            Pracownicy pracownik3 = new Pracownicy();
            Klienci klient = new Klienci();
            Zlecenia zlecenie = new Zlecenia();
            Zamowienia zamowienie = new Zamowienia();
            Magazyn magazyn = new Magazyn();
            transaction = session.beginTransaction();
            pracownik = session.get(Pracownicy.class, 1);
            pracownik2 = session.get(Pracownicy.class, 2);
            pracownik3 = session.get(Pracownicy.class, 3);
//            klient = session.get(Klienci.class,1);
//            zlecenie = session.get(Zlecenia.class,1);
//            zamowienie = session.get(Zamowienia.class,1);
//            magazyn = session.get(Magazyn.class,1);
            System.out.println("\n\n\n" + pracownik + "\n" + pracownik2 + "\n" + pracownik3 + "\n\n\n" + klient + "\n" + zlecenie + "\n" + zamowienie + "\n" + magazyn + "\n\n\n");
            session.clear();
            session.disconnect();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
