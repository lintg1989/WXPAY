package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;

import com.utils.DBConn;

/**
 * Created by Lin on 2017/3/22.
 */
public class ResultDao {

    private ResultDao(){

    }
    private static ResultDao dao = null;

    public static ResultDao getInstance(){
        if(dao == null){
            dao = new ResultDao();
        }
        return dao;
    }

    public boolean saveResult(String code,String openId,String timeStr){
        boolean flag = false;
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "insert into t_result (openId,code,timeStr,status) values (?,?,?,?)";
        try {
            conn = DBConn.getConn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, openId);
            ps.setString(2, code);
            ps.setString(3, timeStr);
            ps.setInt(4, 0);
            int i = ps.executeUpdate();
            if(i != 0){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConn.closeAll(conn, ps, null);
        }
        return flag;
    }

    public List<String> getResult(String openId){

        List<String> list = new ArrayList<String>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from t_result where openId='"+openId+"'";
        try{
            conn = DBConn.getConn();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                JSONObject json = new JSONObject();
                json.put("code", rs.getString("code"));
                json.put("timeStr", rs.getString("timeStr"));
                json.put("status", rs.getInt("status"));
                list.add(json.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConn.closeAll(conn, ps, rs);
        }
        return list;
    }

    public int getNum(){

        int num = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select count(*) from t_result where status=0";
        try{
            conn = DBConn.getConn();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                num = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConn.closeAll(conn, ps, rs);
        }
        return num;
    }

    public int getRemNum(){

        int num = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select num from t_num where id=1";
        try{
            conn = DBConn.getConn();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                num = rs.getInt("num");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConn.closeAll(conn, ps, rs);
        }
        return num;
    }

    public boolean updateRemNum(int num){
        boolean flag = false;
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "update t_num set num=? where id=1";
        try{
            conn = DBConn.getConn();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, num);
            int i = ps.executeUpdate();
            if(i != 0){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConn.closeAll(conn, ps, null);
        }
        return flag;
    }

}
