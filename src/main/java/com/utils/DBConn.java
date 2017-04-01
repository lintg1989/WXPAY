package com.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by Lin on 2017/3/22.
 */
public class DBConn {

    private static String jdbcDriver = "";

    private static String jdbcUrl = "";

    private static String jdbcUser = "";

    private static String jdbcPasswd = "";

    static {
        InputStream is = null;
        try {
            is = DBConn.class.getClassLoader().getResourceAsStream("database.properties");
            Properties p = new Properties();
            p.load(is);
            jdbcDriver = p.getProperty("jdbcDriver");
            jdbcUrl = p.getProperty("jdbcUrl");
            jdbcUser = p.getProperty("jdbcUser");
            jdbcPasswd = p.getProperty("jdbcPasswd");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPasswd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeAll(Connection conn , Statement st , ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(st != null){
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeAll(Connection conn , PreparedStatement ps , ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
