package com.weitouch.dm.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.weitouch.dm.Constants;
import com.weitouch.dm.ExcelUtil;
import com.weitouch.dm.pojo.Distributor;
import com.weitouch.dm.pojo.ShipLine;
import com.weitouch.dm.pojo.Transaction;
import com.weitouch.dm.pojo.Users;
import com.weitouch.dm.service.TransactionService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ReportController extends BaseController {

	@Autowired
	private TransactionService txnService;

	private static final Logger logger = LoggerFactory
			.getLogger(ReportController.class);

	@RequestMapping(value = "/enterReport.do")
	public ModelAndView enterReport(String txnType, HttpServletRequest request) {

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		if (!u.isAdmin())
			return new ModelAndView("redirect:login.do");

		ModelAndView mav = new ModelAndView("TxnReport");
		String reportTitle;
		if (txnType == null) {
			reportTitle = "收货";
			txnType = "receipt";
		} else if (txnType.equals("shipment")) {
			reportTitle = "出货";
		} else if (txnType.equals("sale")) {
			reportTitle = "销售";
		} else {
			reportTitle = "退货";
		}

		mav.addObject("reportTitle", reportTitle);
		mav.addObject("txnType", txnType);

		return mav;
	}

	@RequestMapping(value = "/enterSumReport.do")
	public ModelAndView enterSumReport(String txnType, String distributorId,
			HttpServletRequest request) {

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		if (!u.isAdmin())
			return new ModelAndView("redirect:login.do");

		ModelAndView mav = new ModelAndView("TxnSumReport");
		String reportTitle = null;
		reportTitle = "供应商出货/退货 汇总";

		// // 获取所有distributor列表
		// List<Distributor> distributors =
		// txnService.findAll(Distributor.class);
		// mav.addObject("distributors", distributors);
		mav.addObject("distributorId", distributorId);
		mav.addObject("reportTitle", reportTitle);
		mav.addObject("txnType", txnType);

		return mav;
	}

	@RequestMapping(value = "/generateSumReport.do")
	public ModelAndView generateSumReport(String txnType, String startDate,
			String endDate, String fileName, HttpServletRequest request,
			HttpServletResponse response) {

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		if (!u.isAdmin())
			return new ModelAndView("redirect:login.do");

		if (txnType == null || startDate == null || endDate == null
				|| fileName == null) {
			ModelAndView mav = new ModelAndView("TxnSumReport");
			mav.addObject("reportTitle", "供应商出货/退货 汇总报告");
			mav.addObject("txnType", "shipment");
			return mav;
		}

		logger.debug("start to parse start and end date...");
		Date start = null, end = null;
		if (startDate != null && endDate != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				start = sdf.parse(startDate);
				end = sdf.parse(endDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		logger.debug("start to generate excel report...");

		// List shipments =
		// txnService.sumByDateTxnTypeWithDistributor("shipment",
		// start, end, new Long(distributorId));
		// List returns = txnService.sumByDateTxnTypeWithDistributor("returns",
		// start, end, new Long(distributorId));

		// logger.debug("get query result, transaction size : " + txns.size());

		response.setContentType("application/binary;charset=UTF-8");

		ServletOutputStream outputStream = null;

		try {
			fileName = new String((fileName).getBytes(), "ISO8859_1");

			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName + ".xlsx");// 组装附件名称和格式
			outputStream = response.getOutputStream();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		exportSumExcel(fileName, start, end, outputStream);

		return null;
	}

	private void exportSumExcel(String fileName, Date start, Date end,
			ServletOutputStream outputStream) {

		logger.debug("start in export Excel methods");

		// 创建一个workbook 对应一个excel应用文件
		XSSFWorkbook workBook = new XSSFWorkbook();

		// Get all distributors
		List<Distributor> distributors = txnService.findAll(Distributor.class);
		for (Distributor distributor : distributors) {
			if (distributor.getId() > 1) {
				// 在workbook中添加一个sheet,对应Excel文件中的sheet
				XSSFSheet sheet = workBook.createSheet(distributor.getName());
				ExcelUtil exportUtil = new ExcelUtil(workBook, sheet);
				XSSFCellStyle headStyle = exportUtil.getHeadStyle();
				XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();

				// 构建表头
				XSSFRow headRow = sheet.createRow(0);
				XSSFCell cell = null;
				int i = 0;

				cell = headRow.createCell(i++);
				cell.setCellStyle(headStyle);
				cell.setCellValue("商品编号");

				cell = headRow.createCell(i++);
				cell.setCellStyle(headStyle);
				cell.setCellValue("名称");

				cell = headRow.createCell(i++);
				cell.setCellStyle(headStyle);
				cell.setCellValue("类别");

				cell = headRow.createCell(i++);
				cell.setCellStyle(headStyle);
				cell.setCellValue("单价");

				cell = headRow.createCell(i++);
				cell.setCellStyle(headStyle);
				cell.setCellValue("出货总数");

				cell = headRow.createCell(i++);
				cell.setCellStyle(headStyle);
				cell.setCellValue("退货总数");

				cell = headRow.createCell(i++);
				cell.setCellStyle(headStyle);
				cell.setCellValue("销售额");

				List shipments = txnService.sumByDateTxnTypeWithDistributor(
						"shipment", start, end, distributor.getId());
				List returns = txnService.sumByDateTxnTypeWithDistributor(
						"returns", start, end, distributor.getId());

				HashMap returnMap = null;

				if (returns != null && returns.size() > 0) {
					returnMap = new HashMap();
					for (Object returnSums : returns) {
						Object[] returnLine = (Object[]) returnSums;
						returnMap.put(returnLine[0].toString(),
								returnLine[1].toString());
					}
				}
				
				int j = 1;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				// 构建表体数据
				if (shipments != null && shipments.size() > 0) {
					for (Object line : shipments) {
						Object[] itemSum = (Object[]) line;
						int h = 0;
						XSSFRow dataRow = sheet.createRow(j++);
						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(itemSum[0].toString());

						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(itemSum[2].toString());

						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(itemSum[3].toString());

						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(itemSum[4].toString());

						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(itemSum[1].toString());

						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						if (returnMap != null
								&& returnMap.get(itemSum[0].toString()) != null) {
							cell.setCellValue(returnMap.get(
									itemSum[0].toString()).toString());
							returnMap.remove(itemSum[0].toString());// Remove
																	// from the
																	// return
																	// list.
						} else {
							cell.setCellValue("0");
						}

					}
				}

				// start to loop over return list
				if (returnMap != null && returnMap.size() > 0) {
					Iterator it = returnMap.keySet().iterator();
					while (it.hasNext()) {
						Object key = it.next();
						int h = 0;
						XSSFRow dataRow = sheet.createRow(j++);
						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(key.toString());

						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue("");

						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue("");
						
						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue("");

						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue("0");

						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(returnMap.get(key).toString());

					}
				}
				//start to calculate the income
				for(int k=1;k<j;k++){
					XSSFRow dataRow = sheet.getRow(k);
					int idx = k+1;
					cell = dataRow.createCell(6);
					cell.setCellStyle(bodyStyle);
					String strFormula= "(E"+idx+"-F"+idx+")*D"+idx;
					cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					cell.setCellFormula(strFormula);					
				}

			}
		}

		try {
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@RequestMapping(value = "/generateReport.do")
	public ModelAndView generateReport(String txnType, String startDate,
			String endDate, String fileName, HttpServletRequest request,
			HttpServletResponse response) {

		// 传递是否是admin到页面
		HttpSession session = request.getSession();
		Users u = (Users) session.getAttribute(Constants.LOGIN_USER);
		if (!u.isAdmin())
			return new ModelAndView("redirect:login.do");

		if (txnType == null || startDate == null || endDate == null
				|| fileName == null) {
			ModelAndView mav = new ModelAndView("TxnReport");
			mav.addObject("reportTitle", "销售");
			mav.addObject("txnType", "sale");
			return mav;
		}

		logger.debug("start to parse start and end date...");
		Date start = null, end = null;
		if (startDate != null && endDate != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				start = sdf.parse(startDate);
				end = sdf.parse(endDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		logger.debug("start to generate excel report...");

		List<Transaction> txns = txnService.queryByDateRangeAndTxnType(txnType,
				start, end);

		// logger.debug("get query result, transaction size : " + txns.size());

		response.setContentType("application/binary;charset=UTF-8");

		ServletOutputStream outputStream = null;

		try {
			fileName = new String((fileName).getBytes(), "ISO8859_1");

			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName + ".xlsx");// 组装附件名称和格式
			outputStream = response.getOutputStream();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		exportExcel(txns, txnType, fileName, outputStream);

		return enterReport(txnType, request);
	}

	private void exportExcel(List<Transaction> txns, String txnType,
			String fileName, ServletOutputStream outputStream) {

		logger.debug("start in export Excel methods, get transaction size : "
				+ txns.size());

		// 创建一个workbook 对应一个excel应用文件
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 在workbook中添加一个sheet,对应Excel文件中的sheet
		XSSFSheet sheet = workBook.createSheet(fileName);
		ExcelUtil exportUtil = new ExcelUtil(workBook, sheet);
		XSSFCellStyle headStyle = exportUtil.getHeadStyle();
		XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
		// 构建表头
		XSSFRow headRow = sheet.createRow(0);
		XSSFCell cell = null;
		int i = 0;
		if (!txnType.equals("receipt")) {
			cell = headRow.createCell(i++);
			cell.setCellStyle(headStyle);
			cell.setCellValue("经销商");
		}

		cell = headRow.createCell(i++);
		cell.setCellStyle(headStyle);
		cell.setCellValue("日期");

		cell = headRow.createCell(i++);
		cell.setCellStyle(headStyle);
		cell.setCellValue("编号");

		cell = headRow.createCell(i++);
		cell.setCellStyle(headStyle);
		cell.setCellValue("商品名");

		cell = headRow.createCell(i++);
		cell.setCellStyle(headStyle);
		cell.setCellValue("类别");

		cell = headRow.createCell(i++);
		cell.setCellStyle(headStyle);
		cell.setCellValue("数量");

		cell = headRow.createCell(i++);
		cell.setCellStyle(headStyle);
		cell.setCellValue("金额");

		cell = headRow.createCell(i++);
		cell.setCellStyle(headStyle);
		cell.setCellValue("备注");

		// 构建表体数据
		if (txns != null && txns.size() > 0) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			int j = 1;

			for (Transaction txn : txns) {
				logger.debug("txn id: " + txn.getId());

				String distributorName = null;

				if (!txnType.equals("receipt")) {
					distributorName = txnType.equals("shipment") ? txn
							.getToDistributor().getName() : txn
							.getFromDistributor().getName();
				}

				logger.debug("distributor: " + distributorName);

				String remark = txn.getRemark();
				String shipDate = sdf.format(txn.getShipDate());

				Set<ShipLine> lines = txn.getLines();
				for (ShipLine line : lines) {
					XSSFRow dataRow = sheet.createRow(j++);
					logger.debug("j value is :" + j);
					int h = 0;

					if (!txnType.equals("receipt")) {
						cell = dataRow.createCell(h++);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(distributorName);
					}

					cell = dataRow.createCell(h++);
					cell.setCellStyle(bodyStyle);
					cell.setCellValue(shipDate);

					cell = dataRow.createCell(h++);
					cell.setCellStyle(bodyStyle);
					cell.setCellValue(line.getItem().getCode());

					cell = dataRow.createCell(h++);
					cell.setCellStyle(bodyStyle);
					cell.setCellValue(line.getItem().getName());

					cell = dataRow.createCell(h++);
					cell.setCellStyle(bodyStyle);
					cell.setCellValue(line.getItem().getModel());

					cell = dataRow.createCell(h++);
					cell.setCellStyle(bodyStyle);
					cell.setCellValue(line.getAmount());

					cell = dataRow.createCell(h++);
					cell.setCellStyle(bodyStyle);
					cell.setCellValue(line.getPrice());

					cell = dataRow.createCell(h++);
					cell.setCellStyle(bodyStyle);
					cell.setCellValue(remark);
				}

			}
		}
		try {
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
