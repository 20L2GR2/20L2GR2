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
    public void destroyMethod(){
        DumpData.deleteDataFromDatabase();
    }

    @AfterClass
    public static void initializeMethodAfter(){
        DumpData.dumpDataToDatabase();
    }

    @Test
    public void czyMoznaZakonczycZlecenieJesliNiePodanoCeny(){
        String wynik = controller.canOrderBeFinalizedIfPriceIsSet("");
        assertTrue(wynik.equals("Nie podano ceny"));
    }

    @Test
    public void czyMoznaZakonczycZlecenieJesliCenaToNull(){
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
