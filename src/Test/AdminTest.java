import hibernate.DumpData;
import main.controllers.AdminController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AdminTest {

    AdminController controller = new AdminController();

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
    public void addingUserWithLoginThatExistsShouldntWork() {
        String wynik = controller.utworzUzytkownikaLogic("admin", "qwerty", "1", "Rafal", "Kowalski");
        assertTrue(wynik.equals("Istnieje użytkownik z takim Loginem"));
    }

    @Test
    public void addingUserWithWrongDataShouldntWork() {
        String wynik = controller.utworzUzytkownikaLogic("", "", "1", "Rafal", "Kowalski");
        assertTrue(wynik.equals("Podano zle dane"));
    }

    @Test
    public void addingUserWithGoodDataShouldWork() {
        String wynik = controller.utworzUzytkownikaLogic("NowyUser", "qwerty", "2", "Rafal", "Kowalski");
        assertTrue(wynik.equals("DODANO"));
    }
}
