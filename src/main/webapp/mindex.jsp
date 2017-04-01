<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<% String path=request.getContextPath(); String basePath=request.getScheme()+ "://"+request.getServerName()+ ":"+request.getServerPort()+path+ "/"; %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width,initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no" />
  <title>微信商城</title>

  <link href="css/style.css" rel="stylesheet">
  <link rel="stylesheet" type="text/css" href="touchTouch/touchTouch.css">
  <link rel="stylesheet" href="css/pintuer.css">
  <script type="text/javascript" src="js/jquery.min.js"></script>
  <script src="js/script.js"></script>
  <script src="js/jquery.js"></script>
  <script src="js/jquery-1.7.1.min.js"></script>
  <script src="touchTouch/touchTouch.jquery.js"></script>
  <script type="text/javascript" src="js/jquery.Spinner.js"></script>

  <script type="text/javascript">//参与人数加减
  $(function() {
    $("#d").Spinner();
    $('.thumbs-img').each(function() {
      $(this).children('a').touchTouch();
    });
  });

  //点击图片
  $('.thumbs-img').each(function() {
    9 * $(this).children('a').touchTouch();
  });
  </script>
</head>

<body>
<section>
  </div>
  <div class="centerq">
    <!--参与人数加减 开始-->
    <div class="center">
      <p>我要参与</p>
      <div id="d" class="Spinner"></div>
      <p>人购买人次越多获得几率越大哦！</p>
    </div>
    <!--参与人数加减 结束-->
    <div class="canyu">
      <p>购买</p>
    </div>
  </div>
</section>
</body>


<script type="text/javascript">
  //点击购买按钮调用方法
  $(".canyu").click(function() {
    //获取进度条元素
    var progress = $("progress");
    alert("openId=="+openId +progress.val());
    //进度条不满10人时执行下面代码，若等于10为待开奖状态，则什么也不执行
    if (progress.val() != 10) {
      var openId = '<%=request.getParameter("openId")%>'
      //var openId = '<%=session.getAttribute("openId")%>';
      //num购买数量，money总金额（单位分）
      var num = 1;
      var money = 1;
      //防止并发超卖，购买前先增加订单数量判断是否小于10，支付成功后再发放中奖码，若支付失败再减去相应订单数量
      $.ajax({
        url: "http://www.zheft.cn/wxpay/recodeServlet?flag=3&num=" + num,
        type: "get",
        dataType: "json",
        success: function(data) {
          //error=1 增加订单后总订单数小于等于10
          if (data.error == 1) {
            alert("openId=="+openId);
            //调用支付接口
            $.ajax({
              url:"http://www.zheft.cn/wxpay/topayServlet?openId="+openId+"&money="+money,
              type: "get",
              dataType: "json",
              success: function(json) {
                alert('success'+"appId="+ json.appId,
                        "timeStamp="+ json.timeStamp,
                        "nonceStr="+ json.nonceStr,
                        "package="+ json.packages,
                        "signType="+ "MD5",
                        "paySign="+ json.sign);
                WeixinJSBridge.invoke('getBrandWCPayRequest', {
                          "appId": json.appId,
                          "timeStamp": json.timeStamp,
                          "nonceStr": json.nonceStr,
                          "package": json.packages,
                          "signType": "MD5",
                          "paySign": json.sign
                        },
                        //调起微信支付成功
                        function(res) {
                          alert('调起微信支付成功' + res.err_msg);
                          WeixinJSBridge.log(res.err_msg);
                          if (res.err_msg == "get_brand_wcpay_request:ok") {
                            alert("微信支付成功!");
                            //跳转到生成开奖编码的servlet
                            //window.location.href = "http://www.szhengbinct.com/test/resultServlet?openId=" + openId + "&num=" + num;

                          }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                            alert("用户取消支付!");
                          } else {
                            //支付失败或取消，恢复到原来的订单量
                            alert('支付失败');
                            alert(JSON.stringify(res));
//                            $.ajax({
//                              url: "http://www.szhengbinct.com/test/recodeServlet?flag=4&num=" + num,
//                              type: "get",
//                              dataType: "json"
//                            });
                          }
                        });
              },
              //调起微信支付失败，恢复到原来的订单量
              error:function(){
                alert('调起微信支付失败，恢复到原来的订单量');
                $.ajax({
                  url: "http://www.zheft.cn/wxpay/recodeServlet?flag=4&num=" + num,
                  type: "get",
                  dataType: "json"
                });
              }
            });


            alert("结束");
            //error=0 表示增加订单后总订单数大于10
          } else {
            alert("库存不足");
          }

        }

      });

    }

  });
</script>
</html>