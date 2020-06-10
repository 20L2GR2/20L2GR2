import hibernate.DumpData;
import main.controllers.ObslugaKlientaController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ObslugaKlientaTest {

    ObslugaKlientaController controller = new ObslugaKlientaController();

    @Before
    public void initializeMethod(){
        DumpData.dumpDataToDatabase();
    }

    @After
    public void destroyMethod() {
        DumpData.deleteDataFromDatabase();
    }

    @AfterClass
    public static void initializeMethodAfter() {
        DumpData.dumpDataToDatabase();
    }

    @Test
    public void czyMoznaDodacZlecenieJesliNiePodanoNrRej() {
        String wynik = controller.canAddOrderIfAllFieldsAreSet("", "Imie", "Nazwisko", "123456789", "Marka", "Model", "Jakiś opis naprawy");
        assertTrue(wynik.equals("Nie podano wszystkich danych"));
    }

    @Test
    public void czyMoznaDodacZlecenieJesliNiePodanoImienia() {
        String wynik = controller.canAddOrderIfAllFieldsAreSet("RKR-1234", "", "Nazwisko", "123456789", "Marka", "Model", "Jakiś opis naprawy");
        assertTrue(wynik.equals("Nie podano wszystkich danych"));
    }

    @Test
    public void czyMoznaDodacZlecenieJesliNiePodanoNazwiska() {
        String wynik = controller.canAddOrderIfAllFieldsAreSet("RKR-1234", "Imie", "", "123456789", "Marka", "Model", "Jakiś opis naprawy");
        assertTrue(wynik.equals("Nie podano wszystkich danych"));
    }

    @Test
    public void czyMoznaDodacZlecenieJesliNiePodanoNumeruTelefonu() {
        String wynik = controller.canAddOrderIfAllFieldsAreSet("RKR-1234", "Imie", "Nazwisko", "", "Marka", "Model", "Jakiś opis naprawy");
        assertTrue(wynik.equals("Nie podano wszystkich danych"));
    }

    @Test
    public void czyMoznaDodacZlecenieJesliNiePodanoMarkiSamochodu() {
        String wynik = controller.canAddOrderIfAllFieldsAreSet("RKR-1234", "Imie", "Nazwisko", "123456789", "", "Model", "Jakiś opis naprawy");
        assertTrue(wynik.equals("Nie podano wszystkich danych"));
    }

    @Test
    public void czyMoznaDodacZlecenieJesliNiePodanoModeluSamochodu() {
        String wynik = controller.canAddOrderIfAllFieldsAreSet("RKR-1234", "Imie", "Nazwisko", "123456789", "Marka", "", "Jakiś opis naprawy");
        assertTrue(wynik.equals("Nie podano wszystkich danych"));
    }

    @Test
    public void czyMoznaDodacZlecenieJesliNiePodanoOpisuNaprawy() {
        String wynik = controller.canAddOrderIfAllFieldsAreSet("RKR-1234", "Imie", "Nazwisko", "123456789", "Marka", "Model", "");
        assertTrue(wynik.equals("Nie podano wszystkich danych"));
    }

    @Test
    public void czyMoznaDodacZlecenieJeslPodanoWszystkieDane() {
        String wynik = controller.canAddOrderIfAllFieldsAreSet("RKR-1234", "Imie", "Nazwisko", "123456789", "Marka", "Model", "Jakiś opis naprawy");
        assertTrue(wynik.equals("Zlecenie utworzone"));
    }

    @Test
    public void czyMoznaZakonczycZlecenieJesliNiePodanoCeny() {
        String wynik = controller.canOrderBeFinalizedIfPriceIsSet("");
        assertTrue(wynik.equals("Nie podano ceny"));
    }

    @Test
    public void czyMoznaZakonczycZlecenieJesliCenaToNull() {
        String wynik = controller.canOrderBeFinalizedIfPriceIsSet(null);
        assertTrue(wynik.equals("Nie podano ceny"));
    }

    @Test
    public void czyMoznaZakonczycZlecenieJesliPodanoCene(){
        String wynik = controller.canOrderBeFinalizedIfPriceIsSet("123");
        assertTrue(wynik.equals("Zlecenie zakonczone"));
    }

    @Test
    public void czyMoznaZakonczycZlecenieJesliPodanoNieprawidlowaCene(){
        boolean wynik = controller.canOrderBeFinalizedIfPriceIsSetProperly("asd");
        assertTrue(!wynik);
    }

    @Test
    public void czyMoznaZakonczycZlecenieJesliPodanoNieprawidlowySeparatorCeny(){
        boolean wynik = controller.canOrderBeFinalizedIfPriceIsSetProperly("123,21");
        assertTrue(!wynik);
    }

    @Test
    public void czyMoznaZakonczycZlecenieJesliPodanoPrawidlowySeparatorCeny(){
        boolean wynik = controller.canOrderBeFinalizedIfPriceIsSetProperly("123.21");
        assertTrue(wynik);
    }

    @Test
    public void czyMoznaZakonczycZlecenieJesliPodanoPrawidlowaCene(){
        boolean wynik = controller.canOrderBeFinalizedIfPriceIsSetProperly("123");
        assertTrue(wynik);
    }

}
