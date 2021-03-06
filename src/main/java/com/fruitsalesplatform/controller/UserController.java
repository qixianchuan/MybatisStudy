package com.fruitsalesplatform.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fruitsalesplatform.entity.User;
import com.fruitsalesplatform.service.UserService;

@Controller
public class UserController extends BaseController {

	@Resource
	UserService userService;

	/**
	 * 跳转至首页 显得多余，虽然在web.xml中有指向 但是会报错 "Did not find handler method for [/]" 我能怎么办？
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public String toIndex() {
		return "/index.jsp";
	}

	/**
	 * 跳转至登录页面
	 * 
	 * @return
	 */
	@RequestMapping("/user/toLogin.action")
	public String toLogin() {
		return "/login.jsp";
	}

	@RequestMapping("/user/login.action")
	public String login(User user, Model model, HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", user.getUsername());
		map.put("password",  user.getPassword());
		List<User> userList = userService.find(map);
		if(userList!=null&&userList.size()>0)
		{
			request.getSession().setAttribute("user", userList.get(0));
			model.addAttribute("user", userList.get(0));
			System.out.println(userList.get(0));
			return "/home.jsp";
		}
		model.addAttribute("errorMsg", "登录失败！账号或密码错误！");
		return "/login.jsp";
	}
	
	@RequestMapping("/user/registerPage.action")
	public String toRegister() {
		return "/register.jsp";
	}
	

	@RequestMapping("/user/register.action")
	public String register(User user, Model model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = new HashMap<String,String>();
		map.put("username", user.getUsername());
		List<User> userList = userService.find(map);
		// 用户已注册
		if(userList!=null&&userList.size()>0)
		{
			model.addAttribute("errorMsg", "注册失败，用户名已被占用！");
			return "/register.jsp";
		}
		// 为用户设置UUID主键
		user.setUserid(UUID.randomUUID().toString());
		userService.insert(user);
		// 提示信息
		model.addAttribute("noticeMsg", "注册成功！请输入账号密码登录");
		return "/login.jsp";
	}
}
