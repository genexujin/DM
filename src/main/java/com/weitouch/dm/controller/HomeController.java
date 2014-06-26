package com.weitouch.dm.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.weitouch.dm.Constants;
import com.weitouch.dm.pojo.Distributor;
import com.weitouch.dm.pojo.Users;
import com.weitouch.dm.service.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends BaseController {

	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/login.do")
	public ModelAndView login(HttpServletRequest request, Locale locale,
			Model model, String username, String password) {
		String msg = null;
		ModelAndView mav = new ModelAndView();
		Users u = userService.findByName(username);
		// logger.info("login user is :" + u.getName());
		// logger.info("user is admin ?" + u.isAdmin());

		if (u == null) {
			msg = "输入的用户名和密码不匹配，请重试！";
			mav.addObject("msg", msg);
			mav.addObject("title", "用户登录");
			mav.setViewName("home");
		} else if (!u.getPassword().equals(password)) {
			msg = "输入的用户名和密码不匹配，请重试！";
			mav.addObject("msg", msg);
			mav.addObject("title", "用户登录");
			mav.setViewName("home");
		} else {
			HttpSession session = request.getSession();
			session.setAttribute(Constants.LOGIN_USER, u);
			session.setAttribute("isAdmin", u.isAdmin());
			String targetURL = (String) session.getAttribute("openURL");
			if (StringUtils.isEmpty(targetURL)) {
				mav.addObject("user", u);
				mav.addObject("title", "个人信息");
				mav.setViewName("redirect:home.jsp");
			} else {
				mav.setViewName("redirect:" + targetURL);
			}

		}
		return mav;
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/enterLogin.do")
	public String home() {

		return "home";
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/listUser.do")
	public ModelAndView listUsers(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		if(!u.isAdmin())
			return new ModelAndView("home");
		
		ModelAndView mav = new ModelAndView("UserList");

		List<Users> users = userService.findAll(Users.class);

		mav.addObject("users", users);

		return mav;
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/editUser.do")
	public ModelAndView editUsers(HttpServletRequest request, String mode,
			String id) {

		ModelAndView mav = new ModelAndView("editUser");

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		mav.addObject("isAdmin", u.isAdmin());

		// 获取所有distributor列表
		List<Distributor> distributors = userService.findAll(Distributor.class);
		mav.addObject("distributors", distributors);

		if (!mode.equals("new")) {
			Users user = userService
					.findById(Integer.parseInt(id), Users.class);
			mav.addObject("user", user);
		} else {
			Users user = new Users();
			mav.addObject("user", user);
		}

		return mav;
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/editPass.do")
	public ModelAndView editPass(HttpServletRequest request) {

		ModelAndView mav = new ModelAndView("editUser");

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		mav.addObject("isAdmin", u.isAdmin());

		// 获取所有distributor列表
		List<Distributor> distributors = userService.findAll(Distributor.class);
		mav.addObject("distributors", distributors);

		Users user = userService.findById(u.getId().intValue(), Users.class);
		mav.addObject("user", user);
		mav.addObject("editPass", true);

		return mav;
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/saveUser.do")
	public ModelAndView saveUser(HttpServletRequest request,
			@ModelAttribute Users user, String distId) {

		ModelAndView mav = new ModelAndView("editUser");

		logger.debug("user id is: " + user.getId());

		// 获取所有distributor列表
		List<Distributor> distributors = userService.findAll(Distributor.class);
		mav.addObject("distributors", distributors);
		
		if(distId!=null)
			user.setDistributor(userService.findById(new Integer(distId),
					Distributor.class));
		this.beginTransaction();
		userService.save(user);
		this.commitTransction();

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		mav.addObject("isAdmin", u.isAdmin());
		mav.addObject("msg", "用户信息已经成功保存！");
		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping(value = "/deleteUser.do")
	public ModelAndView deleteUser(String id,HttpServletRequest request) {

		this.beginTransaction();
		userService.delete(userService.findById(new Integer(id), Users.class));
		this.commitTransction();

		return listUsers(request);
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/listDistributor.do")
	public ModelAndView listDistributor(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		if(!u.isAdmin())
			return new ModelAndView("home");
		
		ModelAndView mav = new ModelAndView("DistList");

		List<Distributor> dists = userService.findAll(Distributor.class);

		mav.addObject("dists", dists);

		return mav;
	}
	
	@RequestMapping(value = "/editDist.do")
	public ModelAndView editDist(HttpServletRequest request, String mode,
			String id) {

		ModelAndView mav = new ModelAndView("editDist");

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		mav.addObject("isAdmin", u.isAdmin());
	

		if (!mode.equals("new")) {
			Distributor dist = userService
					.findById(Integer.parseInt(id), Distributor.class);
			mav.addObject("dist", dist);
		} else {
			Distributor dist = new Distributor();
			mav.addObject("dist", dist);
		}

		return mav;
	}
	
	@RequestMapping(value = "/saveDist.do")
	public ModelAndView saveDist(HttpServletRequest request,
			@ModelAttribute Distributor dist) {

		ModelAndView mav = new ModelAndView("editDist");

		logger.debug("dist id is: " + dist.getId());

		this.beginTransaction();
		userService.save(dist, Distributor.class);
		this.commitTransction();

		mav.addObject("msg", "经销商信息已经成功保存！");
		mav.addObject("dist", dist);
		return mav;
	}

	@RequestMapping(value = "/deleteDist.do")
	public ModelAndView deleteDist(String id,HttpServletRequest request) {

		this.beginTransaction();
		userService.delete(new Long(id), Distributor.class);		
		this.commitTransction();

		return listDistributor(request);
	}
	

}
