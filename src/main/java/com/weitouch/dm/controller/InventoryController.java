package com.weitouch.dm.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.weitouch.dm.Constants;
import com.weitouch.dm.pojo.Distributor;
import com.weitouch.dm.pojo.Inventory;
import com.weitouch.dm.pojo.Item;
import com.weitouch.dm.pojo.Users;
import com.weitouch.dm.service.InventoryService;
import com.weitouch.dm.service.ItemService;
import com.weitouch.dm.service.UserService;

@Controller
public class InventoryController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(InventoryController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private InventoryService inventoryService;

	@RequestMapping(value = "/listInventory.do")
	public ModelAndView listInventory(HttpServletRequest request, Locale locale,
			Long distributorId, String pageNumInput) {

		logger.debug("Input param: distributorId=" + distributorId);

		ModelAndView mav = new ModelAndView("invList");
		
		//传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		mav.addObject("isAdmin", u.isAdmin());
		
		//获取所有distributor列表
		List<Distributor> distributors = itemService.findAll(Distributor.class);
		mav.addObject("distributors", distributors);
		
		//如果已经选择了distributor则获取该distributor的库存列表
		if(distributorId == null){//如果没有选择，则默认显示登录用户自己的库存
			distributorId = u.getDistributor().getId();			
		}		
		Distributor distributor = inventoryService.findById(new Long(distributorId).intValue(), Distributor.class);
		Long count = inventoryService.getDistributorItemsCounts(distributor);
						
		//计算一共有几页
		long totalPage = count.longValue() / Constants.QUERY_PAGE_SIZE;
		long mod = count.longValue() % Constants.QUERY_PAGE_SIZE;
		if (mod > 0) {
			totalPage++;
		}
		logger.debug("got the total inventory item list size: " + count);

		int pageNum = 1;
		if (pageNumInput != null)
			pageNum = Integer.parseInt(pageNumInput);

		List<Inventory> list = inventoryService.listItems(pageNum, distributor);
		logger.debug("got the inventory list!");

		
		mav.addObject("currentPage", pageNum);		
		mav.addObject("activeMenu", "inventory");
		mav.addObject("inventories", list);
		mav.addObject("totalPage", totalPage);
		mav.addObject("distributorId",distributorId);
		mav.addObject("currentDist",distributor);

		return mav;
	}

	

}
