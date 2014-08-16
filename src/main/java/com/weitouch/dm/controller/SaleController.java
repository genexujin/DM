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
import com.weitouch.dm.pojo.Sale;
import com.weitouch.dm.pojo.ShipLine;
import com.weitouch.dm.pojo.Shipment;
import com.weitouch.dm.pojo.Users;
import com.weitouch.dm.service.InventoryService;
import com.weitouch.dm.service.ItemService;
import com.weitouch.dm.service.SaleService;
import com.weitouch.dm.service.UserService;

@Controller
public class SaleController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(SaleController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private SaleService saleService;

	@Autowired
	private InventoryService inventoryService;

	@RequestMapping(value = "/listSale.do")
	public ModelAndView listSale(HttpServletRequest request, Locale locale,
			Long distributorId, String pageNumInput) {

		logger.debug("Input param: pageNumInput=" + pageNumInput);

		ModelAndView mav = new ModelAndView("SaleList");

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		mav.addObject("isAdmin", u.isAdmin());

		// 获取所有distributor列表
		List<Distributor> distributors = saleService.findAll(Distributor.class);
		mav.addObject("distributors", distributors);

		// 如果已经选择了distributor则获取该distributor的销售列表
		if (distributorId == null) {// 如果没有选择，则默认显示登录用户自己的库存
			distributorId = u.getDistributor().getId();
		}

		Distributor distributor = inventoryService.findById(new Long(
				distributorId).intValue(), Distributor.class);
		Long count = saleService.getDistributorSaleCounts(distributor);

		long totalPage = count.longValue() / Constants.QUERY_PAGE_SIZE;
		long mod = count.longValue() % Constants.QUERY_PAGE_SIZE;
		if (mod > 0) {
			totalPage++;
		}
		logger.debug("got the total sale list size: " + count);

		int pageNum = 1;
		if (pageNumInput != null)
			pageNum = Integer.parseInt(pageNumInput);

		List<Sale> list = saleService.listSales(pageNum, distributor);
		logger.debug("got the Sale list!");

		mav.addObject("distributorId", distributorId);
		mav.addObject("currentPage", pageNum);
		mav.addObject("isAdmin", u.isAdmin());
		mav.addObject("activeMenu", "sale");
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
	@RequestMapping(value = "/editSale.do")
	public ModelAndView editSale(String mode, String id,
			HttpServletRequest request, Long fromDistId) {

		logger.debug("start to edit Sale, params: mode=" + mode + " sale id = "
				+ id);

		ModelAndView mav = new ModelAndView("editSale");
		List<Distributor> distributors = saleService.findAll(Distributor.class);
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
			Sale sale = saleService.findById(Integer.parseInt(id), Sale.class);
//			Distributor distributor = inventoryService.findById(new Long(
//					fromDistId).intValue(), Distributor.class);
//			sale.setFromDistributor(distributor);
			mav.addObject("sale", sale);

		} else {
			Sale sale = new Sale();
			Distributor distributor = inventoryService.findById(new Long(
					fromDistId).intValue(), Distributor.class);
			sale.setFromDistributor(distributor);
			mav.addObject("sale", sale);
		}
		
		

		mav.addObject("activeMenu", "sale");
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
	@RequestMapping(value = "/deleteSale.do")
	public ModelAndView deleteSale(HttpServletRequest request, Locale locale,
			String id) {

		logger.debug("start to delete Sale, params: " + " Sale Id = " + id);

		this.beginTransaction();
		Sale sale = saleService.findById(new Integer(id), Sale.class);
		for (ShipLine line : sale.getLines()) {// 反冲库存
			inventoryService.receivGoods(sale.getFromDistributor(),
					line.getItem(), line.getAmount());
		}
		saleService.delete(Long.parseLong(id), Sale.class);
		this.commitTransction();

		return listSale(request, locale, null, null);
	}

	/**
	 * 保存销售单
	 * 
	 * @param id
	 * @param shipDate
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "/saveSale.do")
	public ModelAndView saveSale(String id, String shipDate, String remark,
			Long fromDistId, HttpServletRequest request) {

		logger.debug("start to save Sale, Sale id = " + id);
		logger.debug("start to save Sale, ship date = " + shipDate);
		logger.debug("start to save Sale, distId = " + fromDistId);

		ModelAndView mav = new ModelAndView("editSale");
		Sale sale = new Sale();
		if (id != null && id.length() > 0)
			sale.setId(new Long(id));

		if (shipDate != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sale.setShipDate(sdf.parse(shipDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sale.setRemark(remark);

		this.beginTransaction();

		Distributor fromDist = saleService.findById(fromDistId.intValue(),
				Distributor.class);
		sale.setFromDistributor(fromDist);

		sale = saleService.save(sale, Sale.class);
		this.commitTransction();

		List<Distributor> distributors = saleService.findAll(Distributor.class);
		mav.addObject("distributors", distributors);

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		mav.addObject("isAdmin", u.isAdmin());

		mav.addObject("msg", "销售单信息已保存！");
		mav.addObject("sale", sale);
		mav.addObject("activeMenu", "sale");

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
	@RequestMapping(value = "/editSaleLine.do")
	public ModelAndView editSaleLine(String mode, String saleId, String id) {

		logger.debug("start to add sale line, saleId = " + saleId);

		ModelAndView mav = new ModelAndView("editSaleLine");

		List<Item> items = saleService.findAll(Item.class);
		mav.addObject("items", items);

		if (!mode.equals("new")) {
			ShipLine shipLine = saleService.findById(Integer.parseInt(id),
					ShipLine.class);
			mav.addObject("line", shipLine);
		} else {
			ShipLine shipLine = new ShipLine();
			mav.addObject("line", shipLine);

		}

		mav.addObject("saleId", saleId);
		mav.addObject("id", id);
		mav.addObject("activeMenu", "sale");

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
	@RequestMapping(value = "/saveSaleLine.do")
	public ModelAndView saveSaleLine(String saleId, String id, String itemId,
			int amount, double price, String remark) {

		logger.debug("start to save saveSaleLine line, saleId = " + saleId);
		logger.debug("start to save saveSaleLine line, line id = " + id);

		ModelAndView mav = new ModelAndView("editSaleLine");

		List<Item> items = saleService.findAll(Item.class);
		mav.addObject("items", items);

		ShipLine line = new ShipLine();
		if (id != null && id.length() > 0)
			line.setId(new Long(id));

		Item item = null;
		if (itemId != null && itemId.length() > 0) {
			item = saleService.findById(new Integer(itemId), Item.class);
			line.setItem(item);
		}
		if (saleId != null && saleId.length() > 0) {
			Sale sale = saleService.findById(new Integer(saleId), Sale.class);
			line.setShipment(sale);
		}
		line.setAmount(amount);
		line.setPrice(price);
		line.setRemark(remark);

		try {
			this.beginTransaction();
			line = saleService.save(line, ShipLine.class);
			inventoryService.receivGoods(line.getShipment()
					.getFromDistributor(), item, -amount);
			this.commitTransction();
			mav.addObject("msg", "销售项已保存,库存已更新！");
			mav.addObject("success",true);
		} catch (Exception e) {
			this.rollbackTransction();
			mav.addObject("msg", "销售项保存时发生错误，请检查录入信息！");
		}
		mav.addObject("saleId", saleId);
		mav.addObject("line", line);
		mav.addObject("activeMenu", "sale");
		

		return mav;
	}

	/**
	 * 删除入库行
	 * 
	 * @param receiptId
	 * @param lineId
	 * @return
	 */
	@RequestMapping(value = "/deleteSaleLine.do")
	public ModelAndView deleteSaleLine(String saleId, String lineId,
			HttpServletRequest request) {

		logger.debug("start to del sale line, saleId = " + saleId);
		logger.debug("start to del sale line, line ie = " + lineId);
		this.beginTransaction();

		ShipLine line = saleService.findById(new Integer(lineId),
				ShipLine.class);

		inventoryService.receivGoods(line.getShipment().getFromDistributor(),
				line.getItem(), line.getAmount());
		saleService.delete(Long.parseLong(lineId), ShipLine.class);
		this.commitTransction();

		return editSale("edit", saleId, request, null);
	}

}
