package com.ujiuye.service;

import javax.servlet.http.HttpSession;

import com.aliyuncs.exceptions.ClientException;
import com.ujiuye.bean.User;
import com.ujiuye.dao.UserDao;
import com.ujiuye.utils.SMSSendUtils;

public class UserService { 
	UserDao dao = new UserDao();
	// 处理保存用户的业务
	public boolean saveUser(User user) {
	
		return dao.insertUser(user)>0?true:false;
	}

	// 根据数据库查询 用户
		public boolean findUserByPhone(String phone_number,HttpSession session) {
			
			User user = dao.selectUserByPhone(phone_number);
			
			if(user!=null) {
				// 手机号已被注册  
				// TODO 发送手机短信验证码   使用的阿里 云通讯
			//	try {
					
					// 返回验证码
				//	String sendSms = SMSSendUtils.sendSms(phone_number, null);
					
					
					// 将验证码 保存到session
					//session.setAttribute("smsCode", sendSms);
					
					// 将用户信息保存到共享域中
					session.setAttribute("user", user);

					
					
			//	} catch (ClientException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				//}
				
				return true;
			}else {
				return false;
			}
		}

}
