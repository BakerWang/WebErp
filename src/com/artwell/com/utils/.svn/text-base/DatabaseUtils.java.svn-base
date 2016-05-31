package com.artwell.com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
    
public class DatabaseUtils {
    private static Connection con = null;
    public enum DataBase{
    	SAMPLEDB,ERPDB;
    }
    public static Connection getConnection() throws SQLException {
    	if (con == null) {
    		con = connectDatabaseByName();
    	}
    	return con;
    }
	/**
	 * SAMPLEDB,ERPDB
	 * @param databaseName
	 * @return
	 * @throws SQLException 
	 */
	private static Connection connectDatabaseByName() throws SQLException{
		//need connect to 2 database
		//find way to solve this issue
		//keystone
		String connectionUrl = "jdbc:sqlserver://10.17.0.1:1433;autoReconnect=true;user=sample_track;password=DOsqQjZ05h0yjRtI1YaI";
		try {
			 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
		} catch (ClassNotFoundException e) {
			Log4jUtils.logException(e);
		}
		return DriverManager.getConnection(connectionUrl);
	}
	
	public static void closeDatabaseConnections(){
			if (con != null) {
	    		con = null;
	    	}
	}
}    