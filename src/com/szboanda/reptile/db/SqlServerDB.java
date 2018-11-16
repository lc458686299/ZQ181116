package com.szboanda.reptile.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.szboanda.reptile.util.Config;



public class SqlServerDB {
	public static Connection getConn(){
		try {
			 Class.forName(Config.DRIVER_CLASS);
			 Connection coon=DriverManager.getConnection(Config.CONNECTION_URL,Config.USERNAME,Config.PASSWORD);
			 return coon;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void close(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
}
