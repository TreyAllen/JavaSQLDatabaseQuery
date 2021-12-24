# JavaSQLDatabaseQuery
Console Java application for SQL databases users with read only access to be able to submit a query to the database and output the results to a file. Uses Java, SQL.

The application creates a database connection using JDBC:SQL server.  Then it passes the supplied queries using the connection to get a ResultSet.  The ResultSet is then passed to the writeToFile function with an additional parameter of the name of the file to save as.  The writeToFile function  iterates over the ResultSet and for each column of the each result, it utilizes a String Builder to output the information in to a string. Once it has iterated through all items in the ResultSet, it writes the file as a .csv using the passed file name.

The code here just contains one query, but it can be updated to include as many queries as needed.  For each additional query, you need only copy/paste the query/result/writeToFile lines, and update the query variable, and then update update the writeToFile call with the appropriate file name.
