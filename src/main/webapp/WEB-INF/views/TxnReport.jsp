<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	
	
	<br />
	<h3><c:out value="${reportTitle}"/>报表</h3>
	<br />
	<form id="reportForm" role="form" action="generateReport.do"
		method="post">
		<input type="hidden" class="form-control" id="txnType" name="txnType"
			value="${txnType}">
		<h4>请输入报表的时间范围，点击产生报表后请保存文件至本机。</h4>
		<div class="form-group">
			<label for="fileName" class="col-sm-2 control-label">文件名</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="fileName"
					name="fileName" placeholder="文件名，不需要有后缀，系统自动会产生Excel（.xlsx文件）"
					value="${fileName}"/>
			</div>
		</div>
		<div class="form-group">
			<label for="shipDate" class="col-sm-2 control-label">开始日期</label>
			<div class="col-sm-10">
				<input type="date" class="form-control" id="startDate"
					name="startDate" placeholder="开始日期 "
					value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>">
			</div>
		</div>
		<div class="form-group">
			<label for="endDate" class="col-sm-2 control-label">结束日期</label>
			<div class="col-sm-10">
				<input type="date" class="form-control" id="endDate"
					name="endDate" placeholder="入库日期 "
					value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>">
			</div>
		</div>		
		<div class="col-sm-offset-2 col-sm-10">
			<button type="submit" class="btn btn-default">产生报表</button>			
		</div>
	</form>


	

</div>
<%@ include file="footer.jspf"%>
