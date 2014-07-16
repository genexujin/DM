<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<div class="btn-group">
		<button type="button" class="btn btn-primary btn-sm"
			onclick="javascript:window.location.href='listReceipt.do'">&lt;
			&lt; 返回列表</button>
	</div>
	<br />
	<h3>入库明细</h3>
	<br />
	<form id="receiptForm" role="form" action="saveReceipt.do"
		method="post">
		<input type="hidden" class="form-control" id="id" name="id"
			value="${receipt.id}">

		<div class="form-group">
			<label for="shipDate" class="col-sm-2 control-label">入库日期</label>
			<div class="col-sm-10">
				<input type="date" class="form-control" id="shipDate"
					name="shipDate" placeholder="入库日期 "
					value="<fmt:formatDate value="${receipt.shipDate}" pattern="yyyy-MM-dd"/>">
			</div>
		</div>
		<div class="form-group">
			<label for="remark" class="col-sm-2 control-label">备注</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="remark" placeholder="备注"
					name="remark" value="${receipt.remark}">
			</div>
		</div>
		<div class="form-group">
			<label for="totalPrice" class="col-sm-2 control-label">总金额(元)</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="totalPrice"
					readonly="true" placeholder="总金额" value="${receipt.totalPrice}">
			</div>
		</div>



		<div class="col-sm-offset-2 col-sm-10">
			<button type="submit" class="btn btn-default">保存</button>
			<button type="button" class="btn btn-primary btn-sm"
				onclick="addNewLine(${receipt.id})">添加入库项</button>

		</div>
	</form>


	<br />
	<h3>入库项目</h3>
	<table
		class="table table-striped table-bordered table-hover table-responsive table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>类别</th>
				<th>名称</th>
				<th>价格</th>
				<th>数量</th>
				<th>备注</th>

				<th style="width: 10%;">操作</th>


			</tr>
		</thead>
		<tbody>
			<c:forEach items="${receipt.lines}" var="line">
				<tr>
					<td><c:out value="${line.item.code}"></c:out></td>
					<td><c:out value="${line.item.model}"></c:out></td>
					<td><c:out value="${line.item.name}"></c:out></td>
					<td><c:out value="${line.price}"></c:out></td>
					<td><c:out value="${line.amount}"></c:out></td>
					<td><c:out value="${line.remark}"></c:out></td>

					<td>
						<!-- button class="btn btn-primary btn-xs"
							onclick="javascript:window.location.href='editReceiptLine.do?mode=edit&receiptId=${receipt.id}&id=${line.id}'">编辑</button>
 						-->
						<button class="btn btn-primary btn-xs"
							onclick="confirmDeleteLine(${receipt.id},${line.id})">删除</button>

					</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>

	<script>
       function addNewLine(receiptId){
    	   if(receiptId!=null){
    	   	window.location='editReceiptLine.do?mode=new&receiptId='+receiptId;
    	   }else{
    		   alert("请先保存收货单！");
    	   }
    	   			
       }
       
       function confirmDeleteLine(receiptId,oid){
     		var confirmed = window.confirm("您确认要删除该项吗？");
     		if(confirmed){
     			window.location="deleteShipLine.do?receiptId="+receiptId+"&lineId="+oid;
     		}
     	}
    
    </script>


</div>
<%@ include file="footer.jspf"%>
