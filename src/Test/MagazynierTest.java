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

}
