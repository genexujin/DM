package com.weitouch.dm.controller;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.weitouch.dm.Constants;
import com.weitouch.dm.pojo.Item;
import com.weitouch.dm.pojo.Users;
import com.weitouch.dm.service.ItemService;
import com.weitouch.dm.service.UserService;

@Controller
public class ItemsController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(ItemsController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;

	@RequestMapping(value = "/listItems.do")
	public ModelAndView listItems(HttpServletRequest request, Locale locale,
			String pageNumInput) {

		logger.debug("Input param: pageNumInput=" + pageNumInput);

		ModelAndView mav = new ModelAndView("itemList");

		Long count = itemService.getCounts(Item.class);

		long totalPage = count.longValue() / Constants.QUERY_PAGE_SIZE;
		long mod = count.longValue() % Constants.QUERY_PAGE_SIZE;
		if (mod > 0) {
			totalPage++;
		}
		logger.debug("got the total item list size: " + count);

		int pageNum = 1;
		if (pageNumInput != null)
			pageNum = Integer.parseInt(pageNumInput);

		List<Item> list = itemService.listItems(pageNum, Item.class);
		logger.debug("got the item list!");

		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);

		mav.addObject("currentPage", pageNum);
		mav.addObject("isAdmin", u.isAdmin());
		mav.addObject("activeMenu", "item");
		mav.addObject("list", list);
		mav.addObject("totalPage", totalPage);

		return mav;
	}

	@RequestMapping(value = "/editItem.do")
	public ModelAndView editItem(HttpServletRequest request, Locale locale,
			String mode, String id) {

		logger.debug("start to editItem, params: mode=" + mode + " itemid = "
				+ id);

		ModelAndView mav = new ModelAndView("editItem");

		if (!mode.equals("new")) {
			Item item = itemService.findById(Integer.parseInt(id), Item.class);
			mav.addObject("item", item);
		}

		return mav;
	}

	@RequestMapping(value = "/deleteItem.do")
	public ModelAndView deleteItem(HttpServletRequest request, Locale locale,
			String id) {

		logger.debug("start to editItem, params: " + " itemid = " + id);

		this.beginTransaction();
		itemService.delete(Long.parseLong(id), Item.class);
		this.commitTransction();

		return listItems(request,locale,null);
	}

	@RequestMapping(value = "/saveItem.do")
	public ModelAndView save(HttpServletRequest request, Locale locale,
			Item item) {

		logger.debug("start to save Item, itemid = " + item.getId());
		logger.debug("start to save Item, name = " + item.getName());
		logger.debug("start to save Item, model = " + item.getModel());

		ModelAndView mav = new ModelAndView("editItem");

		this.beginTransaction();
		itemService.saveItem(item);
		this.commitTransction();

		mav.addObject("msg", "商品信息已保存！");
		mav.addObject("item", item);

		return mav;
	}

}
