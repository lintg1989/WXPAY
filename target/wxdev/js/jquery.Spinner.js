/* jQuery.Spinner V1.0 CopyRight (c) 2014 by:Loyaoo Taobao:http://isseven.taobao.com */

(function($) {

		var minnum;
		$.ajax({
			url:"http://sccyfs.net/weChatpay/recodeServlet?flag=2",
			type:"get",
			async:false, 
			dataType:"json",
			success:function(data){
				if(data.num > 10){minnum = 10;}
				else {minnum = data.num;}
			}
		});

	$.fn.Spinner = function (opts) {

	    var people = {min:minnum,max:10} //默认参与人数跟总人数
		if(minnum == 10){
			var defaults = {value:1, min:1, len:3, max:1}//根据上面的总人数来设置增加的最大限制
		} else {
			var defaults = {value:1, min:1, len:3, max:people.max-people.min}//根据上面的总人数来设置增加的最大限制
		}
		
		var options = $.extend(defaults, opts)
		var keyCodes = {up:38, down:40}
		
		return this.each(function() {
		
			var a = $('<a></a>'); f(a,0,"Decrease","-");	//减
			var c = $('<a></a>'); f(c,0,"Increase","+");	//加
			var b = $('<input/>');f(b,0,"Amount");cv(0);	//默认值
			var count = people.max;
			var jv = people.min;//获取默认值
			//获取进度条元素
			var progress = $("progress");
			progress.prop("max",count);
			progress.val(jv);
			
			//参与人数
			var canyu = $(".canshu_l font");
			    canyu.text(jv);
			//参与总人数
			var canyuz = $(".canshu_z font");
			    canyuz.text(count);
			//剩余人数
			var hat = $(".canshu_r font");
			    hat.text(count-jv);
			//获取购买按钮元素
			var menu = $(".canyu").children("p");
			
			$(this).append(a).append(b).append(c);
			
			a.click(function(){cv(-1)
			if(jv>people.min && jv<=people.max){
			   //jd(--jv);
			   canyu.text(jv);
			   hat.text(count-jv);
				}
			   
			});
			b.keyup(function(){cv(0)});
			c.click(function(){cv(+1)
			if(jv>=people.min && jv<people.max){
                //jd(++jv);
				canyu.text(jv);
			    hat.text(count-jv);
			}
			});
			b.bind('keyup paste change',function(e){
				e.keyCode==keyCodes.up&&cv(+1);
				e.keyCode==keyCodes.down&&cv(-1);
			});
			function cv(n){
				b.val(b.val().replace(/[^\d]/g,''));
				bv=parseInt(b.val()||options.min)+n;
				bv>=options.min&&bv<=options.max&&b.val(bv);
				if(bv<=options.min){b.val(options.min);f(a,2,"DisDe","Decrease");}else{f(a,2,"Decrease","DisDe");}
				if(bv>=options.max){b.val(options.max);f(c,2,"DisIn","Increase");}else{f(c,2,"Increase","DisIn");}
				
				
			}
			//function jd(v){
				//给进度条赋值
			    //progress.val(v);
				//判读人数是否达到总人数人
				if(minnum==count){
				   
				   //更改按钮的名称
				   menu.text("待开奖");
				   $("#d").next("p").text("次日工作时间9:00-12:00工作时间开奖！");
				}else{
				   //更改按钮的名称
				   menu.text("购买");
				   $("#d").next("p").text("人购买的次数越多获得几率越大哦！");
				}
			//}
			
		});

		function f(o,t,c,s){
			t==0&&o.addClass(c).attr("href","javascript:void(0)").append("<i></i>").find("i").append(s);
			t==1&&o.addClass(c).attr({"value":options.value,"autocomplete":"off","maxlength":options.len});
			t==2&&o.addClass(c).removeClass(s);
			
		}
	}
	
})(jQuery);