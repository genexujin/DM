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
import com.weitouch.dm.pojo.Receipt;
import com.weitouch.dm.pojo.ShipLine;
import com.weitouch.dm.pojo.Shipment;
import com.weitouch.dm.pojo.Users;
import com.weitouch.dm.service.InventoryService;
import com.weitouch.dm.service.ItemService;
import com.weitouch.dm.service.ReceiptService;
import com.weitouch.dm.service.UserService;

@Controller
public class ShipmentController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(ShipmentController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ReceiptService receiptService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private InventoryService inventoryService;

	@RequestMapping(value = "/listShipment.do")
	public ModelAndView listShipment(HttpServletRequest request, Locale locale,
			String pageNumInput) {

		logger.debug("Input param: pageNumInput=" + pageNumInput);

		ModelAndView mav = new ModelAndView("ShipmentList");

		Long count = receiptService.getCounts(Shipment.class);

		long totalPage = count.longValue() / Constants.QUERY_PAGE_SIZE;
		long mod = count.longValue() % Constants.QUERY_PAGE_SIZE;
		if (mod > 0) {
			totalPage++;
		}
		logger.debug("got the total Shipment list size: " + count);

		int pageNum = 1;
		if (pageNumInput != null)
			pageNum = Integer.parseInt(pageNumInput);

		List<Shipment> list = receiptService.listItems(pageNum, Shipment.class);
		logger.debug("got the Shipment list!");

		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);

		mav.addObject("currentPage", pageNum);
		mav.addObject("isAdmin", u.isAdmin());
		mav.addObject("activeMenu", "shipment");
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
	@RequestMapping(value = "/editShipment.do")
	public ModelAndView editShipment(String mode, String id) {

		logger.debug("start to editReceipt, params: mode=" + mode
				+ " shipment id = " + id);

		ModelAndView mav = new ModelAndView("editShipment");
		List<Distributor> distributors = receiptService
				.findAll(Distributor.class);
		mav.addObject("distributors", distributors);

		if (!mode.equals("new")) {
			Shipment shipment = receiptService.findById(Integer.parseInt(id),
					Shipment.class);
			mav.addObject("shipment", shipment);

		} else {
			Shipment shipment = new Shipment();
			mav.addObject("shipment", shipment);
		}

		mav.addObject("activeMenu", "shipment");
		return mav;
	}

	/**
	 * 编辑入库单
	 * 
	 * @param mode
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/printShipment.do")
	public ModelAndView printShipment(String shipmentId) {

		logger.debug("start to print shipment, params: shipmentId="
				+ shipmentId);

		ModelAndView mav = new ModelAndView("printShipment");

		Shipment shipment = receiptService.findById(
				Integer.parseInt(shipmentId), Shipment.class);
		mav.addObject("shipment", shipment);
		mav.addObject("activeMenu", "shipment");
		
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
	@RequestMapping(value = "/deleteShipment.do")
	public ModelAndView deleteShipment(HttpServletRequest request,
			Locale locale, String id) {

		logger.debug("start to delete Shipment, params: " + " Shipmentid = "
				+ id);

		this.beginTransaction();
		Shipment shipment = receiptService.findById(new Integer(id),
				Shipment.class);
		for (ShipLine line : shipment.getLines()) {// 反冲库存
			inventoryService.receivGoods(line.getItem(), line.getAmount());
			inventoryService.receivGoods(shipment.getToDistributor(),
					line.getItem(), -line.getAmount());
		}
		receiptService.delete(Long.parseLong(id), Shipment.class);
		this.commitTransction();

		return listShipment(request, locale, null);
	}

	/**
	 * 保存入库单
	 * 
	 * @param id
	 * @param shipDate
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "/saveShipment.do")
	public ModelAndView saveShipment(String id, String shipDate, String remark,
			String distId) {

		logger.debug("start to save Shipment, Shipment = " + id);
		logger.debug("start to save Shipment, ship date = " + shipDate);
		logger.debug("start to save Shipment, distId = " + distId);

		ModelAndView mav = new ModelAndView("editShipment");
		Shipment shipment = new Shipment();
		if (id != null && id.length() > 0)
			shipment.setId(new Long(id));

		if (shipDate != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				shipment.setShipDate(sdf.parse(shipDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		shipment.setRemark(remark);

		this.beginTransaction();

		Distributor toDist = receiptService.findById(new Integer(distId),
				Distributor.class);
		shipment.setToDistributor(toDist);

		Distributor fromDist = receiptService.findById(new Integer(1),
				Distributor.class);

		shipment.setFromDistributor(fromDist);

		shipment = receiptService.save(shipment, Shipment.class);
		this.commitTransction();

		List<Distributor> distributors = receiptService
				.findAll(Distributor.class);
		mav.addObject("distributors", distributors);

		mav.addObject("msg", "入库信息已保存！");
		mav.addObject("shipment", shipment);
		mav.addObject("activeMenu", "shipment");

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
	@RequestMapping(value = "/editShipmentLine.do")
	public ModelAndView editShipmentLine(String mode, String shipmentId,
			String id) {

		logger.debug("start to add receipt line, shipmentId = " + shipmentId);

		ModelAndView mav = new ModelAndView("editShipmentLine");

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

		mav.addObject("shipmentId", shipmentId);
		mav.addObject("id", id);
		mav.addObject("activeMenu", "shipment");

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
	@RequestMapping(value = "/saveShipmentLine.do")
	public ModelAndView saveShipmentLine(String shipmentId, String id,
			String itemId, int amount, double price, String remark) {

		logger.debug("start to save saveShipmentLine line, shipmentId = "
				+ shipmentId);
		logger.debug("start to save saveShipmentLine line, line id = " + id);

		ModelAndView mav = new ModelAndView("editShipmentLine");

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
		if (shipmentId != null && shipmentId.length() > 0) {
			Shipment receipt = receiptService.findById(new Integer(shipmentId),
					Shipment.class);
			line.setShipment(receipt);
		}
		line.setAmount(amount);
		line.setPrice(price);
		line.setRemark(remark);

		try {
			this.beginTransaction();
			line = receiptService.save(line, ShipLine.class);
			inventoryService.receivGoods(item, -amount);
			inventoryService.receivGoods(line.getShipment().getToDistributor(),
					item, amount);
			this.commitTransction();
			mav.addObject("msg", "出货项已保存,库存已更新！");
			mav.addObject("success",true);
		} catch (Exception e) {
			this.rollbackTransction();
			mav.addObject("msg", "出货项保存时发生错误，请检查录入数据！");
		}
		mav.addObject("shipmentId", shipmentId);
		mav.addObject("line", line);
		mav.addObject("activeMenu", "shipment");
		

		return mav;
	}

	/**
	 * 删除入库行
	 * 
	 * @param receiptId
	 * @param lineId
	 * @return
	 */
	@RequestMapping(value = "/deleteShipmentLine.do")
	public ModelAndView deleteShipmentLine(String shipmentId, String lineId) {

		logger.debug("start to del receipt line, shipmentId = " + shipmentId);
		logger.debug("start to del receipt line, line ie = " + lineId);
		this.beginTransaction();

		ShipLine line = receiptService.findById(new Integer(lineId),
				ShipLine.class);
		inventoryService.receivGoods(line.getItem(), line.getAmount());
		inventoryService.receivGoods(line.getShipment().getToDistributor(),
				line.getItem(), -line.getAmount());
		receiptService.delete(Long.parseLong(lineId), ShipLine.class);
		this.commitTransction();

		return editShipment("edit", shipmentId);
	}

}
