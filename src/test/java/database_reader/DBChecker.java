package database_reader;

import org.testng.annotations.DataProvider;
import utils.ConfigurationReader;

import java.sql.*;
import java.util.*;

public class DBChecker {

    private static final String SELECTALLQUERY="SELECT * FROM request ORDER BY request_id;";
    private static final String GETROWCOUNT="SELECT COUNT(*) FROM request;";
    private static final String UPDATEASSCHEDULED="UPDATE request SET is_scheduled = 1 WHERE request_id = %d ;";

    public static void main(String[] args) {
        getTheRequests();
    }

    @DataProvider(name="unscheduled-request-data")
    public static Object[][] getTheRequests() {
        try (Connection connection = DriverManager.getConnection(
                ConfigurationReader.getProperty("url")
                , ConfigurationReader.getProperty("username")
                , ConfigurationReader.getProperty("password"));
             Statement statement1 = connection.createStatement();
             Statement statement2 = connection.createStatement();
             Statement statement3=connection.createStatement(); //statement 3 executed in the method for updating the is_scheduled column
             /*
             resultSet1 for row count
             resultSet2 for querying requests
              */
             ResultSet resultSet1=statement1.executeQuery(GETROWCOUNT);
             ResultSet resultSet2 =statement2.executeQuery(SELECTALLQUERY);
        ) {
            ResultSetMetaData metaData = resultSet2.getMetaData();
            /*
            A loop will initialize the result to resultMap
            Map contains <ID of request,
                List of: date,disappear|appear,xPath, email, address>
             */

            Map<Integer,List<String>> resultMap=new HashMap<>();
            int columnCount = metaData.getColumnCount();
            System.out.println("column label: "+metaData.getColumnLabel(6));
            resultSet1.next();
            int rowCount=resultSet1.getInt(1);
            Object[][] resultArray=new Object[rowCount][columnCount];
            int row=0;
            while (resultSet2.next()) {
                    if(resultSet2.getInt("is_scheduled")==0) {  //checking the ones that are not scheduled yet
                        for (int i = 0; i < columnCount; i++) {
                            resultArray[row][i] = resultSet2.getString(i+1);
                        }
                        String isScheduledQuery = String.format(UPDATEASSCHEDULED, row + 1); //updating isScheduled after assigning
                        statement3.execute(isScheduledQuery);
                    }
                    row++;

            }
            System.out.println("new data scheduling: \n "+Arrays.deepToString(resultArray));
            return resultArray;

        } catch (SQLException exception) {
            System.out.println("Jenkins package couldn't connect to the DB");
            System.out.println(exception.getMessage());
        }
        //if result is null
        System.out.println("resultmap is empty");
        return null;
    }
}
