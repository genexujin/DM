<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<script>
 
  $(function() {     
    $("#itemCode").autocomplete({
      source: "listItemName.do",
      minLength: 2,
      select: function( event, ui ) {       
        
        $("#itemId").val(ui.item.id);
        $("#itemName").val(ui.item.itemName);
      }
    });
  });
  </script>
<div class="row-fluid">
	<br />
	<div class="btn-group">
		<button type="button" class="btn btn-primary btn-sm"
			onclick="javascript:window.location.href='editSale.do?mode=edit&id=${saleId}'">&lt;
			&lt; 返回</button>			
		<button type="button" class="btn btn-primary btn-sm"
			onclick="javascript:window.location.href='editSaleLine.do?mode=new&saleId=${saleId}'">添加下一条</button>
	</div>
	<br />
	<h3>销售项明细</h3>

	<form id="saleLineForm" role="form" action="saveSaleLine.do"
		method="post">
		<input type="hidden" class="form-control" id="saleId"
			name="saleId" value="${saleId}"> <input type="hidden"
			class="form-control" id="id" name="id" value="${id}">

		<div class="form-group">
			<label for="itemCode" class="col-sm-2 control-label">商品编号</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="itemCode" placeholder="请输入编号"
					name="itemCode" value="${line.item.code}">
			</div>
		</div>
		<div class="form-group">
			<label for="itemId" class="col-sm-2 control-label">商品名称</label>
			<div class="col-sm-10">
				<input type="hidden" class="form-control" id="itemId" 
					name="itemId" value="${line.item.id}">
					<input type="text" class="form-control" id="itemName" readonly placeholder="选择编号后，商品名称会自动填写到这里"
					name="itemName" value="${line.item.name}">
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
			<c:if test="${!success}">
				<button type="submit" class="btn btn-default">保存</button>
			</c:if>
		</div>
	</form>
	<br />
	<br />
</div>

<%@ include file="footer.jspf"%>
