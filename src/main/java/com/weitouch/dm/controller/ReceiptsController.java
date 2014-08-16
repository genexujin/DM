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
import com.weitouch.dm.pojo.Item;
import com.weitouch.dm.pojo.Receipt;
import com.weitouch.dm.pojo.ShipLine;
import com.weitouch.dm.pojo.Users;
import com.weitouch.dm.service.InventoryService;
import com.weitouch.dm.service.ItemService;
import com.weitouch.dm.service.ReceiptService;
import com.weitouch.dm.service.UserService;

@Controller
public class ReceiptsController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(ReceiptsController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ReceiptService receiptService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private InventoryService inventoryService;

	@RequestMapping(value = "/listReceipt.do")
	public ModelAndView listReceipt(HttpServletRequest request, Locale locale,
			String pageNumInput) {

		logger.debug("Input param: pageNumInput=" + pageNumInput);

		ModelAndView mav = new ModelAndView("receivingList");

		Long count = receiptService.getCounts(Receipt.class);

		long totalPage = count.longValue() / Constants.QUERY_PAGE_SIZE;
		long mod = count.longValue() % Constants.QUERY_PAGE_SIZE;
		if (mod > 0) {
			totalPage++;
		}
		logger.debug("got the total receipt list size: " + count);

		int pageNum = 1;
		if (pageNumInput != null)
			pageNum = Integer.parseInt(pageNumInput);

		List<Receipt> list = receiptService.listItems(pageNum, Receipt.class);
		logger.debug("got the receipt list!");

		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);

		mav.addObject("currentPage", pageNum);
		mav.addObject("isAdmin", u.isAdmin());
		mav.addObject("activeMenu", "receipt");
		mav.addObject("list", list);
		mav.addObject("totalPage", totalPage);

		return mav;
	}

	/**
	 * 编辑入库单
	 * @param mode
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/editReceipt.do")
	public ModelAndView editReceipt(String mode, String id) {

		logger.debug("start to editReceipt, params: mode=" + mode
				+ " receipt id = " + id);

		ModelAndView mav = new ModelAndView("editReceipt");

		if (!mode.equals("new")) {
			Receipt receipt = receiptService.findById(Integer.parseInt(id),
					Receipt.class);
			mav.addObject("receipt", receipt);
		} else {
			Receipt receipt = new Receipt();
			mav.addObject("receipt", receipt);
		}

		mav.addObject("activeMenu", "receipt");
		return mav;
	}

	/**
	 * 删除入库单
	 * @param request
	 * @param locale
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteReceipt.do")
	public ModelAndView deleteReceipt(HttpServletRequest request,
			Locale locale, String id) {

		logger.debug("start to delete receipt, params: " + " receiptid = " + id);

		this.beginTransaction();
		Receipt receipt = receiptService.findById(new Integer(id),
				Receipt.class);
		for (ShipLine line : receipt.getLines()) {//反冲库存
			inventoryService.receivGoods(line.getItem(), -line.getAmount());
		}
		receiptService.delete(Long.parseLong(id), Receipt.class);
		this.commitTransction();

		return listReceipt(request, locale, null);
	}

	/**
	 * 保存入库单
	 * @param id
	 * @param shipDate
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "/saveReceipt.do")
	public ModelAndView save(String id, String shipDate, String remark) {

		logger.debug("start to save receipt, receiptid = " + id);
		logger.debug("start to save receipt, ship date = " + shipDate);
		logger.debug("start to save receipt, total price = " + remark);

		ModelAndView mav = new ModelAndView("editReceipt");
		Receipt receipt = new Receipt();
		if (id != null && id.length() > 0)
			receipt.setId(new Long(id));

		if (shipDate != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				receipt.setShipDate(sdf.parse(shipDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		receipt.setRemark(remark);

		this.beginTransaction();
		receipt = receiptService.save(receipt, Receipt.class);
		this.commitTransction();

		mav.addObject("msg", "入库信息已保存！");
		mav.addObject("receipt", receipt);
		mav.addObject("receiptId", receipt.getId());
		mav.addObject("activeMenu", "receipt");

		return mav;
	}

	/**
	 * 编辑入库行
	 * @param mode
	 * @param receiptId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/editReceiptLine.do")
	public ModelAndView editReceiptLine(String mode, String receiptId, String id) {

		logger.debug("start to add receipt line, receiptid = " + receiptId);

		ModelAndView mav = new ModelAndView("editReceiptLine");

		List<Item> items = itemService.findAll(Item.class);
		mav.addObject("items", items);

		if (!mode.equals("new")) {
			ShipLine shipLine = receiptService.findById(Integer.parseInt(id),
					ShipLine.class);
			mav.addObject("line", shipLine);
		} else {
			ShipLine shipLine = new ShipLine();
			mav.addObject("line", shipLine);

		}

		mav.addObject("receiptId", receiptId);
		mav.addObject("id", id);
		mav.addObject("activeMenu", "receipt");

		return mav;
	}

	/**
	 * 保存入库行
	 * @param receiptId
	 * @param id
	 * @param itemId
	 * @param amount
	 * @param price
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "/saveReceiptLine.do")
	public ModelAndView saveReceiptLine(String receiptId, String id,
			String itemId, int amount, double price, String remark) {

		logger.debug("start to save receipt line, receiptid = " + receiptId);
		logger.debug("start to save receipt line, line ie = " + id);

		ModelAndView mav = new ModelAndView("editReceiptLine");

		List<Item> items = itemService.findAll(Item.class);
		mav.addObject("items", items);

		ShipLine line = new ShipLine();
		if (id != null && id.length() > 0)
			line.setId(new Long(id));

		Item item = null;
		if (itemId != null && itemId.length() > 0) {
			item = itemService.findById(new Integer(itemId), Item.class);
			line.setItem(item);
		}
		if (receiptId != null && receiptId.length() > 0) {
			Receipt receipt = receiptService.findById(new Integer(receiptId),
					Receipt.class);
			line.setShipment(receipt);
		}
		line.setAmount(amount);
		line.setPrice(price);
		line.setRemark(remark);
        
		try {
			this.beginTransaction();
			line = receiptService.save(line, ShipLine.class);
			inventoryService.receivGoods(item, amount);
			this.commitTransction();
			mav.addObject("msg", "入库商品已保存！");
			mav.addObject("success",true);
		} catch (Exception e) {
			this.rollbackTransction();
			mav.addObject("msg", "保存时出现错误，请检查录入信息！");
		}
		mav.addObject("receiptId", receiptId);
		mav.addObject("line", line);
		mav.addObject("activeMenu", "receipt");
		

		return mav;
	}

	/**
	 * 删除入库行
	 * @param receiptId
	 * @param lineId
	 * @return
	 */
	@RequestMapping(value = "/deleteShipLine.do")
	public ModelAndView deleteShipLine(String receiptId, String lineId) {

		logger.debug("start to del receipt line, receiptid = " + receiptId);
		logger.debug("start to del receipt line, line ie = " + lineId);
		this.beginTransaction();

		ShipLine line = receiptService.findById(new Integer(lineId),
				ShipLine.class);
		inventoryService.receivGoods(line.getItem(), -line.getAmount());
		receiptService.delete(Long.parseLong(lineId), ShipLine.class);

		this.commitTransction();

		return editReceipt("edit", receiptId);
	}

}
