package database_reader;

import utils.ConfigurationReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBChecker {

    private static final String SELECTALLQUERY="SELECT * FROM request;";


    public static Map<Integer,List<String>> getTheRequests() {
        try (Connection connection = DriverManager.getConnection(
                ConfigurationReader.getProperty("url")
                , ConfigurationReader.getProperty("username")
                , ConfigurationReader.getProperty("password"));
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECTALLQUERY);
        ) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            /*
            A loop will initialize the result to resultMap
            Map contains <ID of request,
                List of: date,dissappear|appear,xPath, email, adress>
             */
            Map<Integer,List<String>> resultMap=new HashMap<>();
            int coulumnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                List<String> resultList=new ArrayList<>();
                for (int i = coulumnCount; i>1; i--) {
                    String result = resultSet.getString(i);
                    resultList.add(result);
                }
                Integer key=Integer.parseInt(resultSet.getString(1));
                resultMap.put(key,resultList);
            }
            return resultMap;

        } catch (SQLException exception) {
            System.out.println("Jenkins package couldn't connect to the DB");
            System.out.println(exception.getMessage());
        }
        //if result is null
        System.out.println("resultmap is empty");
        return null;
    }
}
