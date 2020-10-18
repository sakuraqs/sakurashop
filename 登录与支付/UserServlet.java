package com.ujiuye.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.ujiuye.bean.User;
import com.ujiuye.service.UserService;
import com.ujiuye.utils.FileUploadUtils;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	UserService service = new UserService();
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		// 1. 根据携带的参数确定执行 哪个方法
		String method = request.getParameter("method");

		if ("register".equals(method)) {
			// 注册
			try {
				register(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if("checkPhone".equals(method)){
			
			// 校验手机号
			checkPhone(request,response);
		}else if("checkCode".equals(method)) {
			
			// 验证码校验的方法
			checkCode(request,response);
		}else if("loginOut".contentEquals(method)) {
			loginOut(request,response);
		}


	}
//退出登录
	private void loginOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		//1、将session中用户信息删除
		request.getSession().removeAttribute("user");
		//2、跳回首页
		response.sendRedirect("welcome.jsp");
	}

	// 校验手机号
		private void checkPhone(HttpServletRequest request, HttpServletResponse response) throws IOException {
			
			// 1. 获取手机号
			String phone_number = request.getParameter("phone_number");
			HttpSession session = request.getSession();
			// 2. 调用业务层  处理业务 
			boolean b = service.findUserByPhone(phone_number,session);
		
			// 3. 响应
			response.getWriter().print(b);
			
		}

		// 校验验证码
		private void checkCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
			// 1. 获取用户输入的验证码
			String code = request.getParameter("code");
			
			// 2. 获取生成的
			//String smsCode = (String) request.getSession().getAttribute("smsCode");
			
			// 3. 判断
			PrintWriter writer = response.getWriter();
			//if(code.equals(smsCode)) {
			if(code.equals("111")) {
				writer.print(true);
			}else {
				writer.print(false);
			}
			
			
		}


	private void register(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = FileUploadUtils.fileupload(request, response);

		User user = new User();

		BeanUtils.populate(user, map);
		
		user.setCreate_time(new Date());
		//System.out.println(user);
		
		// 2. 调用业务类 处理业务
				//UserService service = new UserService();
				
				boolean b = service.saveUser(user);
				
				// 3. 转发或者重定向 
				if(b) {
					// 注册成功  重定向到登录页面
					response.sendRedirect(request.getContextPath()+"/login.html");
				}else {
					// 注册失败 返回注册页面
					request.getRequestDispatcher("register.html").forward(request, response);
				}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
       doGet(request, response);
	}

}
