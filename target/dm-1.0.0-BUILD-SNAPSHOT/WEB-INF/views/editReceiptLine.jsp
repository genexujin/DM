<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<div class="btn-group">
		<button type="button" class="btn btn-primary btn-sm"
			onclick="javascript:window.location.href='editReceipt.do?mode=edit&id=${receiptId}'">&lt; &lt; 返回</button>
		<button type="button" class="btn btn-primary btn-sm"
			onclick="javascript:window.location.href='editReceiptLine.do?mode=new&receiptId=${receiptId}'">添加下一条</button>
	</div>
	<br />
	<h3>入库项明细</h3>
	<br />
	<form id="receiptLineForm" role="form" action="saveReceiptLine.do"
		method="post">
		<input type="hidden" class="form-control" id="receiptId"
			name="receiptId" value="${receiptId}"> 
		<input type="hidden"
			class="form-control" id="id" name="id" value="${id}">

		<div class="form-group">
			<label for="itemId" class="col-sm-2 control-label">商品名称</label>
			<div class="col-sm-10">
			    <select class="form-control" id="itemId" name="itemId" value="${line.item.id}">
			       <c:forEach items="${items}" var="item">
						<option value="${item.id}" <c:if test="${line.item.id == item.id}"> selected </c:if>						
						>${item.name}&nbsp;${item.model}</option>
			       </c:forEach>
			    </select>
				
			</div>
		</div>
		<div class="form-group">
			<label for="amount" class="col-sm-2 control-label">数量</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="amount" placeholder="数量"
					name="amount" value="${line.amount}">
			</div>
		</div>
		<div class="form-group">
			<label for="price" class="col-sm-2 control-label">价格</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="price" placeholder="价格"
					name="price" value="${line.price}">
			</div>
		</div>
		<div class="form-group">
			<label for="remark" class="col-sm-2 control-label">备注</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="remark" placeholder="备注"
					name="remark" value="${line.remark}">
			</div>
		</div>
		<div class="col-sm-offset-2 col-sm-10">
			<button type="submit" class="btn btn-default">保存</button>
			
		</div>
	</form>
    
</div>
<br />
<br />
<%@ include file="footer.jspf"%>
