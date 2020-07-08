package executor;

import database_reader.DBChecker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import utils.DriverFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.testng.AssertJUnit.*;

public class PokeElement extends BaseTest {
    /**
     *
     * @param request
     * @throws ParseException
     * This method created to be scheduled by Jenkins,
     * It will read the data from database.
     * With Selenium requests will be executed
     */
    @Test(dataProvider = "unscheduled-request-data",dataProviderClass = DBChecker.class)
    public void poke(String[] request) throws ParseException, InterruptedException {
        Date currentDate=new Date();
        /*
             -method will work when,
             request_id!=null to get rid off the unwanted data
             -2. condition for the end data field in the request POJO

         */
        Date requestEndDate=null;
        if (request[0]!=null) {
            if(request[1]!=null)
               requestEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(request[1]);

                if (requestEndDate==null || currentDate.before(requestEndDate)) {
                    WebDriver driver = DriverFactory.getDriver();
                    driver.get(request[6]);
                    Thread.sleep(2000);
                    WebElement element = null;
                    try {
                        element = driver.findElement(By.xpath(request[3]));
                    } catch (NullPointerException exception) {
                        exception.printStackTrace();
                    } finally {
                /*if request is appear of the element
                 when element is there assertion won't work.
                The test will fail and the client will receive an email.
                Visa-versa for the disappear which is the else part.
                */
                        if (request[2].equals("appear")) {
                            assertNull(element);
                        } else {
                            assertNotNull(element);
                        }
                    }
                }
            }



    }

    /*
    data provider will be set up
     */

}
