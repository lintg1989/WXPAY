package com.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.ResultDao;
import com.utils.TenpayUtil;

/**
 * Created by Lin on 2017/3/22.
 */
public class ResultServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String num = request.getParameter("num");
        String openId = request.getParameter("openId");

        for(int i=1;i <= Integer.parseInt(num); i++){
            //用于获取随机数
            String currTime = TenpayUtil.getCurrTime();//获取当前时间
            String strTime = currTime.substring(8, currTime.length());//8位日期
            String strRandom = TenpayUtil.buildRandom(4) + "";//四位随机数
            String code = strRandom + strTime;//10位序列号,可以自行调整

//            boolean flag = ResultDao.getInstance().saveResult(code, openId, currTime);

        }

        response.sendRedirect("mindex.jsp");
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);

    }

}
