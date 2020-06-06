import hibernate.DumpData;
import javassist.tools.Dump;
import main.controllers.AdminController;
import main.controllers.LogowanieController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.AssertTrue;

import static org.junit.Assert.*;


public class LogowanieTest {


    LogowanieController controller = new LogowanieController();

    @Before
    public void initializeMethod(){
        DumpData.dumpDataToDatabase();
    }

    @After
    public void destroyMethod(){
        DumpData.deleteDataFromDatabase();
    }

    @AfterClass
    public static void initializeMethodAfter(){
        DumpData.dumpDataToDatabase();
    }

    @Test
    public void loggingWithWrongCredentialsWontAllowLogginIn(){
       String login = "UGABUGA";
       String haslo = "UGABUGA";
       int stanowisko = controller.zalogujLogic(login, haslo);
        switch (stanowisko) {
            case 0:
                fail();
                break;
            case 1:
                fail();
                break;
            case 2:
                fail();
                break;
            case 3:
                fail();
                break;
            default:
                assertTrue(true);
                break;
        }
    }

    @Test
    public void logginWithCorrectCredentialsWillLogYouInAdmin(){
        String login = "admin";
        String haslo = "admin";
        int stanowisko = controller.zalogujLogic(login, haslo);
        switch (stanowisko) {
            case 0:
                assertTrue(true);
                break;
            case 1:
                fail();
                break;
            case 2:
                fail();
                break;
            case 3:
                fail();
                break;
            default:
                fail();
                break;
        }
    }

    @Test
    public void logginWithCorrectCredentialsWillLogYouInObsluga(){
        String login = "obslugaklienta1";
        String haslo = "obslugaklienta1";
        int stanowisko = controller.zalogujLogic(login, haslo);
        switch (stanowisko) {
            case 0:
                fail();
                break;
            case 1:
                assertTrue(true);
                break;
            case 2:
                fail();
                break;
            case 3:
                fail();
                break;
            default:
                fail();
                break;
        }
    }

    @Test
    public void logginWithCorrectCredentialsWillLogYouInMechanik(){
        String login = "mechanik1";
        String haslo = "mechanik1";
        int stanowisko = controller.zalogujLogic(login, haslo);
        switch (stanowisko) {
            case 0:
                fail();
                break;
            case 1:
                fail();
                break;
            case 2:
                assertTrue(true);
                break;
            case 3:
                fail();
                break;
            default:
                fail();
                break;
        }
    }

    @Test
    public void logginWithCorrectCredentialsWillLogYouInMagazynier(){
        String login = "magazynier1";
        String haslo = "magazynier1";
        int stanowisko = controller.zalogujLogic(login, haslo);
        switch (stanowisko) {
            case 0:
                fail();
                break;
            case 1:
                fail();
                break;
            case 2:
                fail();
                break;
            case 3:
                assertTrue(true);
                break;
            default:
                fail();
                break;
        }
    }
}
