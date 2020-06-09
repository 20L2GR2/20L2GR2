import hibernate.DumpData;
import main.controllers.MagazynierController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MagazynierTest {

    MagazynierController controller = new MagazynierController();

    @Before
    public void initializeMethod() {
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
    public void czyMoznaDodacZlecenieJesliNiePodanoNazwy() {
        String wynik = controller.canAddPartIfAllFieldsAreSet("", "Opis", "123", "19.99");
        assertTrue(wynik.equals("Nie podano wszystkich danych"));
    }

    @Test
    public void czyMoznaDodacZlecenieJesliNiePodanoOpisu() {
        String wynik = controller.canAddPartIfAllFieldsAreSet("Nazwa", "", "123", "19.99");
        assertTrue(wynik.equals("Nie podano wszystkich danych"));
    }

    @Test
    public void czyMoznaDodacZlecenieJesliNiePodanoIlosci() {
        String wynik = controller.canAddPartIfAllFieldsAreSet("Nazwa", "Opis", "", "19.99");
        assertTrue(wynik.equals("Nie podano wszystkich danych"));
    }

    @Test
    public void czyMoznaDodacZlecenieJesliNiePodanoCeny() {
        String wynik = controller.canAddPartIfAllFieldsAreSet("Nazwa", "Opis", "123", "");
        assertTrue(wynik.equals("Nie podano wszystkich danych"));
    }

    @Test
    public void czyMoznaDodacZlecenieJesliPodanoWszystkieDane() {
        String wynik = controller.canAddPartIfAllFieldsAreSet("Nazwa", "Opis", "123", "19.99");
        assertTrue(wynik.equals("Dodano czesc"));
    }

    @Test
    public void addingNewGoodPart() {
        String wynik = controller.dodajCzescLogic("Olej", "Opis oleju", "2", "123");
        assertTrue(wynik.equals("Dodano"));
    }

    @Test
    public void addingExistingPart() {
        String wynik = controller.dodajCzescLogic("Świeca iskrowa", "Świeca iskrowa do silników benzynowych", "2", "123");
        assertTrue(wynik.equals("Istnieje już taka część"));
    }

    @Test
    public void addingWrongPart() {
        String wynik = controller.dodajCzescLogic("", "", "asd", "asd");
        assertTrue(wynik.equals("Podano zle dane"));
    }

    @Test
    public void czyMoznaDodacCzescJesliPodanoNieprawidlowaIlosc() {
        boolean wynik = controller.canAddPartBeFinalizedIfQuantityIsSetProperly("asd");
        assertTrue(!wynik);
    }

    @Test
    public void czyMoznaDodacCzescJesliPodanoSeparatorIlosciKropka() {
        boolean wynik = controller.canAddPartBeFinalizedIfQuantityIsSetProperly("123,21");
        assertTrue(!wynik);
    }

    @Test
    public void czyMoznaDodacCzescJesliPodanoSeparatorIlosciPrzecinek() {
        boolean wynik = controller.canAddPartBeFinalizedIfQuantityIsSetProperly("123.21");
        assertTrue(!wynik);
    }

    @Test
    public void czyMoznaDodacCzescJesliPodanoPrawidlowaIlosc() {
        boolean wynik = controller.canAddPartBeFinalizedIfQuantityIsSetProperly("123");
        assertTrue(wynik);
    }

    @Test
    public void czyMoznaDodacCzescJesliPodanoNieprawidlowaCene() {
        boolean wynik = controller.canAddPartBeFinalizedIfPriceIsSetProperly("asd");
        assertTrue(!wynik);
    }

    @Test
    public void czyMoznaDodacCzescJesliPodanoNieprawidlowySeparatorCeny() {
        boolean wynik = controller.canAddPartBeFinalizedIfPriceIsSetProperly("123,21");
        assertTrue(!wynik);
    }

    @Test
    public void czyMoznaDodacCzescJesliPodanoPrawidlowySeparatorCeny() {
        boolean wynik = controller.canAddPartBeFinalizedIfPriceIsSetProperly("123.21");
        assertTrue(wynik);
    }

    @Test
    public void czyMoznaDodacCzescJesliPodanoPrawidlowaCene() {
        boolean wynik = controller.canAddPartBeFinalizedIfPriceIsSetProperly("123");
        assertTrue(wynik);
    }

}
