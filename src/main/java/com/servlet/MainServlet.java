package com.servlet;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import com.utils.CommonUtil;
import org.apache.commons.logging.Log;

/**
 * Created by Lin on 2017/3/22.
 */
public class MainServlet extends HttpServlet {
    //网页授权获取用户信息
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appid = "wx0f5cc2bcb9b35ef8";//公众号appid
        String appsecret = "0e0d2965f0232b3fbb786e2e9e823173";//公众号秘钥
        //微信返回的code
        String code = request.getParameter("code");

        //获取openId和access_token（获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder）
        String openId = "";
        String access_token = "";
        String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code";
        JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);
        if (null != jsonObject) {
            openId = jsonObject.getString("openid");
            access_token = jsonObject.getString("access_token");
        }
        System.out.println("openId" + openId);

        request.getSession().setAttribute("openId", openId);

        //获取用户信息
        String nickname = "";
        String headimgurl = "";
        String userURL = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openId+"";
        JSONObject userJson = CommonUtil.httpsRequest(userURL, "GET", null);
        if (null != jsonObject) {
            nickname = userJson.getString("nickname");
            headimgurl = userJson.getString("headimgurl");
        }
        request.getSession().setAttribute("nickname", nickname);
        request.getSession().setAttribute("headimgurl", headimgurl);


        response.sendRedirect("mindex.jsp?openId="+openId);

//        request.getRequestDispatcher("shop/index.jsp").forward(request,response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
