import hibernate.DumpData;
import main.controllers.MechanikController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MechanikTest {

    MechanikController controller = new MechanikController();

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
    public void nieMoznaZakonczycGdyIdPuste() {
        String wynik = controller.zlecenieZakonczCzyMozna("", "qwerty");
        assertTrue(wynik.equals("Nie wybrano zlecenia"));
    }

    @Test
    public void nieMoznaZakonczycGdyOpisPusty() {
        String wynik = controller.zlecenieZakonczCzyMozna("1", "");
        assertTrue(wynik.equals("Dodaj opis naprawy"));
    }

    @Test
    public void nieMoznaZakonczycGdyOpisNull() {
        String wynik = controller.zlecenieZakonczCzyMozna("2", null);
        assertTrue(wynik.equals("Dodaj opis naprawy"));
    }

    @Test
    public void nieMoznaZakonczycGdyIdNull() {
        String wynik = controller.zlecenieZakonczCzyMozna(null, "fafsd");
        assertTrue(wynik.equals("Nie wybrano zlecenia"));
    }

    @Test
    public void nieMoznaZakonczycGdyObaNull() {
        String wynik = controller.zlecenieZakonczCzyMozna(null, null);
        assertTrue(wynik.equals("Nie wybrano zlecenia"));
    }

    @Test
    public void nieMoznaZakonczycGdyObaPuste() {
        String wynik = controller.zlecenieZakonczCzyMozna("", "");
        assertTrue(wynik.equals("Nie wybrano zlecenia"));
    }

    @Test
    public void moznaZakonczyc() {
        String wynik = controller.zlecenieZakonczCzyMozna("1", "fsfsdf");
        assertTrue(wynik.equals("Zlecenie zostało zakończone"));
    }
}
