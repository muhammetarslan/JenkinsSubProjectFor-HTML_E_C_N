package executor;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import utils.DriverFactory;

public class BaseTest {
    @BeforeTest
    public void setup(){

    }

    @AfterTest
    public void tearDown(){
        DriverFactory.closeDriver();
    }
}
