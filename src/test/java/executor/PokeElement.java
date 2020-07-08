package executor;

import database_reader.DBChecker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import utils.ConfigurationReader;
import utils.DriverFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

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
                    System.out.println(request[6]);
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
                            if(element!=null){
                                sendEmail(request[4],request[6]);
                            }
                        } else {
                            if(element==null){
                                sendEmail(request[4],request[6]);
                            }
                        }
                    }
                }
            }



    }
    private void sendEmail(String sendEmailTo, String requestAddress){
        String from= ConfigurationReader.getProperty("email_sender");
        String host=ConfigurationReader.getProperty("host");
        String password=ConfigurationReader.getProperty("email_password");
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", ConfigurationReader.getProperty("email_port_on"));
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", ConfigurationReader.getProperty("email_port_out"));

        Session session = Session.getDefaultInstance(properties,new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from,password);
            }
        });

        try{
            MimeMessage mimeMessage=new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(sendEmailTo));
            mimeMessage.setSubject("HTML Element Change Notifier");
            mimeMessage.setText(String.format(
                    "Hi I'm a robot," +
                            "Your requested element has a change. " +
                            "The address of the element %s" +
                            "It was great to work for you." +
                            "Please reply this email if you have any questions" +
                            "Your loved robot being, 19ADS40",requestAddress));
            Transport.send(mimeMessage);
            System.out.printf("Message sent successfully to, %s",sendEmailTo);
        } catch (MessagingException mex){
            mex.printStackTrace();
        }

    }

}
