package databaseextraction;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class DatabaseExtraction {
    
    //update dealer name here for reference
    final static String DEALER_NAME = "DealerName";
    
    //update dealer number here for reference enclosed in single quotes
    //If more than one, separate by commas i.e. '101234','104567'
    final static String DEALER_NUM = "'100001'";
        
    //update database server name here based off which isn't active server
    final static String DB = "database_name";

    public static void main(String[] args) {
        
        Connection conn = databaseConnection(DB);
        String query = "";
        ResultSet result = null;
        
        // Sql Query to dispaly all the values under Site table then write to file           
        query = "select sgl.sitegroupnum, x.xmit, s.*\n" +
            "from Site s\n" +
            "left join xmit x on x.sitenum = s.sitenum\n" +
            "left join sitegrouplink sgl on sgl.sitenum = s.SiteNum\n" +
            "where sgl.SiteGroupNum not like '4%'\n" +
            "and sgl.SiteGroupNum not like '8%'\n" +
            "and sgl.SiteGroupNum in (" + DEALER_NUM + ")";
        result = query(conn, query);
        writeToFile(result, "Site");

    }
    
    //public ResultSet databaseConnection() {
    public static Connection databaseConnection(String db) {
        
        Connection conn = null;
        
        try {
            
            //update database name as needed
            String url="jdbc:sqlserver://" + db + ";databaseName=databaseName";

            //update username/password as needed
            String username = "username";
            String password = "password";
            
            //Initialize Sqldriver instance 
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Creating the connection providing URL and username password
            conn = DriverManager.getConnection(url, username, password);

            return conn;

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        }

        return conn;
        
    }
    
    public static ResultSet query(Connection conn, String query) {
        
        ResultSet result = null;
        
        try {
            // Providing the query under prepareStatement parameter
            PreparedStatement pst = conn.prepareStatement(query);

            //Command to execute query and capturing all the result under Result set
            result = pst.executeQuery();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        }

        return result;
                
    }
    
    private static void writeToFile(ResultSet result, String title) {

        java.util.Date d = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd_HHmm");
        String date = sdf.format(d);
        
        try {
            
            String outputName = "C:\\Users\\" + System.getProperty("user.name") + "\\Downloads\\" + DEALER_NAME + "_" + title + "_" + date + ".csv";

            FileWriter myWriter = new FileWriter(outputName);

            StringBuilder sb = new StringBuilder();

            int columnCount = result.getMetaData().getColumnCount();
            
            //create headers
            for (int h = 0; h < columnCount; h++) {
                sb.append("\"");
                sb.append(result.getMetaData().getColumnName(h+1));
                sb.append("\"");
                sb.append(",");
            }

            //remove last comma from header and add new line
            sb.setLength(sb.length() - 1);
            sb.append("\n");

            
            while(result.next()) {
                
                for (int i = 0; i < columnCount; i++) {

                    sb.append("\"\t");
                    sb.append(result.getString(i+1));
                    sb.append("\"");
                    sb.append(",");

                }

                if (sb.length() > 0) {
                       sb.setLength(sb.length() - 1);
                       sb.append("\n");
                }

            }
            
            myWriter.write(sb.toString());
            myWriter.close();
            
            System.err.println("" + title + " file saved!");
            
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound Exception: " + e.getMessage());
            System.err.println("" + title + " file NOT saved!");
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            System.err.println("" + title + " file NOT saved!");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            System.err.println("" + title + " file NOT saved!");
        }
        finally {
        }
           
    }
    
    public static void output(ResultSet result) {
        
        try {
                        
            int columnCount = result.getMetaData().getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                    System.out.print("" + result.getMetaData().getColumnName(i+1) + "\t");
            }
            System.out.println();

            while(result.next()) {

                //StringBuilder sb = new StringBuilder();

                for (int i = 0; i < columnCount; i++) {
                    System.out.print("" + result.getString(i+1)+ "\t");
                }
                System.out.println();

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        }
        
    }
}
