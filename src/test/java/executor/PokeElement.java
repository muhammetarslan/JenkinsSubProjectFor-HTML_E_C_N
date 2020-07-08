package executor;

import database_reader.DBChecker;
import org.testng.annotations.Test;
import utils.DriverFactory;

import java.util.List;
import java.util.Map;

public class PokeElement extends BaseTest {

    @Test
    public void poke(){
        Map<Integer, List<String>> resultMap=DBChecker.getTheRequests();
        System.out.println(resultMap);
    }

    /*
    data provider will be set up
     */

}
