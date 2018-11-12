package mysns.util;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.tomcat.jdbc.pool.DataSource;

public class DBManager{
	public static Connection getConnection() {
		Connection conn;
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mysns");
			conn = ds.getConnection();
		}catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
		return conn;
	}
}
