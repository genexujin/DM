package com.weitouch.dm.controller;

import java.text.SimpleDateFormat;
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
import com.weitouch.dm.pojo.Item;
import com.weitouch.dm.pojo.Return;
import com.weitouch.dm.pojo.Sale;
import com.weitouch.dm.pojo.ShipLine;
import com.weitouch.dm.pojo.Users;
import com.weitouch.dm.service.InventoryService;
import com.weitouch.dm.service.ReturnService;
import com.weitouch.dm.service.UserService;

@Controller
public class ReturnController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(ReturnController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ReturnService returnService;

	@Autowired
	private InventoryService inventoryService;

	@RequestMapping(value = "/listReturn.do")
	public ModelAndView listReturn(HttpServletRequest request, Locale locale,
			Long distributorId, String pageNumInput) {

		logger.debug("Input param: pageNumInput=" + pageNumInput);

		ModelAndView mav = new ModelAndView("ReturnList");

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		mav.addObject("isAdmin", u.isAdmin());

		// 获取所有distributor列表
		List<Distributor> distributors = returnService
				.findAll(Distributor.class);
		mav.addObject("distributors", distributors);

		// 如果已经选择了distributor则获取该distributor的销售列表
		if (distributorId == null) {// 如果没有选择，则默认显示登录用户自己的库存
			distributorId = u.getDistributor().getId();
		}

		Distributor distributor = inventoryService.findById(new Long(
				distributorId).intValue(), Distributor.class);
		Long count = returnService.getDistributorReturnCounts(distributor);

		long totalPage = count.longValue() / Constants.QUERY_PAGE_SIZE;
		long mod = count.longValue() % Constants.QUERY_PAGE_SIZE;
		if (mod > 0) {
			totalPage++;
		}
		logger.debug("got the total return list size: " + count);

		int pageNum = 1;
		if (pageNumInput != null)
			pageNum = Integer.parseInt(pageNumInput);

		List<Return> list = returnService.listReturns(pageNum, distributor);
		logger.debug("got the Return list!");

		mav.addObject("distributorId", distributorId);
		mav.addObject("currentPage", pageNum);
		mav.addObject("isAdmin", u.isAdmin());
		mav.addObject("activeMenu", "return");
		mav.addObject("list", list);
		mav.addObject("totalPage", totalPage);

		return mav;
	}

	/**
	 * 编辑入库单
	 * 
	 * @param mode
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/editReturn.do")
	public ModelAndView editReturn(String mode, String id,
			HttpServletRequest request, Long fromDistId) {

		logger.debug("start to edit Return, params: mode=" + mode
				+ " return id = " + id);

		ModelAndView mav = new ModelAndView("editReturn");
		List<Distributor> distributors = returnService
				.findAll(Distributor.class);
		mav.addObject("distributors", distributors);

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		mav.addObject("isAdmin", u.isAdmin());

		// 如果已经选择了distributor则获取该distributor的销售列表
		if (fromDistId == null) {// 如果没有选择，则默认显示登录用户自己的销售
			fromDistId = u.getDistributor().getId();
		}

		if (!mode.equals("new")) {
			Return rt = returnService.findById(Integer.parseInt(id),
					Return.class);
//			Distributor distributor = inventoryService.findById(new Long(
//					fromDistId).intValue(), Distributor.class);
//			rt.setFromDistributor(distributor);
//			
			mav.addObject("returnorder", rt);

		} else {
			Return rt = new Return();
			Distributor distributor = inventoryService.findById(new Long(
					fromDistId).intValue(), Distributor.class);
			
			rt.setFromDistributor(distributor);
			rt.setToDistributor(inventoryService.findById(1, Distributor.class));
			mav.addObject("returnorder", rt);
			logger.debug("added return object while new");
		}

		mav.addObject("activeMenu", "return");
		return mav;
	}

	/**
	 * 删除入库单
	 * 
	 * @param request
	 * @param locale
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteReturn.do")
	public ModelAndView deleteReturn(HttpServletRequest request, Locale locale,
			String id) {

		logger.debug("start to delete Return , params: " + " Return Id = " + id);

		this.beginTransaction();
		Return rt = returnService.findById(new Integer(id), Return.class);
		for (ShipLine line : rt.getLines()) {// 反冲库存
			inventoryService.receivGoods(rt.getFromDistributor(),
					line.getItem(), line.getAmount());
			inventoryService.receivGoods(line.getItem(), -line.getAmount());
		}
		returnService.delete(Long.parseLong(id), Return.class);
		this.commitTransction();

		return listReturn(request, locale, null, null);
	}

	/**
	 * 保存销售单
	 * 
	 * @param id
	 * @param shipDate
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "/saveReturn.do")
	public ModelAndView saveSale(String id, String shipDate, String remark,
			Long fromDistId, HttpServletRequest request) {

		logger.debug("start to save Return, Return id = " + id);
		logger.debug("start to save Return, ship date = " + shipDate);
		logger.debug("start to save Return, fromDistId = " + fromDistId);

		ModelAndView mav = new ModelAndView("editReturn");
		Return rt = new Return();
		if (id != null && id.length() > 0)
			rt.setId(new Long(id));

		if (shipDate != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				rt.setShipDate(sdf.parse(shipDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		rt.setRemark(remark);

		this.beginTransaction();

		Distributor fromDist = returnService.findById(fromDistId.intValue(),
				Distributor.class);
		rt.setFromDistributor(fromDist);
		rt.setToDistributor(returnService.findById(1, Distributor.class));

		rt = returnService.save(rt, Return.class);
		this.commitTransction();

		List<Distributor> distributors = returnService
				.findAll(Distributor.class);
		mav.addObject("distributors", distributors);

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		mav.addObject("isAdmin", u.isAdmin());

		mav.addObject("msg", "退货信息已保存！");
		mav.addObject("returnorder", rt);
		mav.addObject("activeMenu", "return");

		return mav;
	}

	/**
	 * 编辑入库行
	 * 
	 * @param mode
	 * @param receiptId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/editReturnLine.do")
	public ModelAndView editReturnLine(String mode, String returnId, String id) {

		logger.debug("start to edit return line, returnId = " + returnId);

		ModelAndView mav = new ModelAndView("editReturnLine");

		List<Item> items = returnService.findAll(Item.class);
		mav.addObject("items", items);

		if (!mode.equals("new")) {
			ShipLine shipLine = returnService.findById(Integer.parseInt(id),
					ShipLine.class);
			mav.addObject("line", shipLine);
		} else {
			ShipLine shipLine = new ShipLine();
			mav.addObject("line", shipLine);
		}

		mav.addObject("returnId", returnId);
		mav.addObject("id", id);
		mav.addObject("activeMenu", "return");

		return mav;
	}

	/**
	 * 保存入库行
	 * 
	 * @param receiptId
	 * @param id
	 * @param itemId
	 * @param amount
	 * @param price
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "/saveReturnLine.do")
	public ModelAndView saveReturnLine(String returnId, String id,
			String itemId, int amount, double price, String remark) {

		logger.debug("start to save saveReturnLine line, returnId = "
				+ returnId);
		logger.debug("start to save saveReturnLine line, line id = " + id);

		ModelAndView mav = new ModelAndView("editReturnLine");

		List<Item> items = returnService.findAll(Item.class);
		mav.addObject("items", items);

		ShipLine line = new ShipLine();
		if (id != null && id.length() > 0)
			line.setId(new Long(id));

		Item item = null;
		if (itemId != null && itemId.length() > 0) {
			item = returnService.findById(new Integer(itemId), Item.class);
			line.setItem(item);
		}
		if (returnId != null && returnId.length() > 0) {
			Return rt = returnService.findById(new Integer(returnId),
					Return.class);
			line.setShipment(rt);
		}
		line.setAmount(amount);
		line.setPrice(price);
		line.setRemark(remark);

		try {
			this.beginTransaction();
			line = returnService.save(line, ShipLine.class);
			inventoryService.receivGoods(line.getShipment()
					.getFromDistributor(), item, -amount);
			inventoryService.receivGoods(item, amount);
			this.commitTransction();
			mav.addObject("msg", "退货项已保存,库存已更新！");
			mav.addObject("success",true);
		} catch (Exception e) {
			this.rollbackTransction();
			mav.addObject("msg", "退货项保存时发生错误，请检查录入信息！");
		}
		mav.addObject("returnId", returnId);
		mav.addObject("line", line);
		mav.addObject("activeMenu", "return");
		

		return mav;
	}

	/**
	 * 删除入库行
	 * 
	 * @param receiptId
	 * @param lineId
	 * @return
	 */
	@RequestMapping(value = "/deleteReturnLine.do")
	public ModelAndView deleteSaleLine(String returnId, String lineId,
			HttpServletRequest request) {

		logger.debug("start to del return line, saleId = " + returnId);
		logger.debug("start to del return line, line ie = " + lineId);
		this.beginTransaction();

		ShipLine line = returnService.findById(new Integer(lineId),
				ShipLine.class);

		inventoryService.receivGoods(line.getShipment().getFromDistributor(),
				line.getItem(), line.getAmount());
		inventoryService.receivGoods(line.getItem(), -line.getAmount());
		returnService.delete(Long.parseLong(lineId), ShipLine.class);
		this.commitTransction();

		return editReturn("edit", returnId, request, null);
	}

}
