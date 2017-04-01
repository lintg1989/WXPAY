package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import com.dao.ResultDao;

/**
 * Created by Lin on 2017/3/22.
 */
public class RecodeServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String flag = request.getParameter("flag");
        switch(Integer.parseInt(flag)){

            case 1:
                getResult(request,response);//获取个人购买记录
                break;
            case 2:
                getNum(request,response);//获取已参与人数
                break;
            case 3:
                addNum(request,response);//点击购买时，订单量增加
                break;
            case 4:
                deleNum(request, response);//支付失败时，订单量减少
                break;

        }

    }

    //获取购买记录
    public void getResult(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String openId = request.getParameter("openId");
        JSONObject json = new JSONObject();
      //  List<String> list = ResultDao.getInstance().getResult(openId);
        List<String> list = new ArrayList<String>();
        json.put("list", list);
        response.getWriter().append(json.toString());
    }


    //获取已购买人数
    public void getNum(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JSONObject json = new JSONObject();
        //int num = ResultDao.getInstance().getNum();
        int num =1;
        json.put("num", num);
        response.getWriter().append(json.toString());
    }

    //防止并发超卖，每次购买前先增加订单数量，支付成功后再发放中奖码，若支付失败再减去相应订单数量
    public void addNum(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String num = request.getParameter("num");
        JSONObject json = new JSONObject();
        String error = "1";
//        int remNum = ResultDao.getInstance().getRemNum();
//        if(remNum+Integer.parseInt(num)<=10){
//            boolean flag = ResultDao.getInstance().updateRemNum(remNum+Integer.parseInt(num));
//        }else{
//            error = "0";
//        }
        json.put("error", 1);
        response.getWriter().append(json.toString());
    }

    public void deleNum(HttpServletRequest request,HttpServletResponse response) throws IOException{
//        String num = request.getParameter("num");
//        int remNum = ResultDao.getInstance().getRemNum();
//        boolean flag = ResultDao.getInstance().updateRemNum(remNum-Integer.parseInt(num));

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);

    }


}
