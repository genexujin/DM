<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<div class="btn-group">
		<button type="button" class="btn btn-primary btn-sm"
			onclick="javascript:window.location.href='listSale.do'">&lt;
			&lt; 返回列表</button>
	</div>
	<br />
	<h3>销售明细</h3>
	<br />
	<form id="receiptForm" role="form" action="saveSale.do" method="post">
		<input type="hidden" class="form-control" id="id" name="id"
			value="${sale.id}">
		<c:choose>
			<c:when test="${!isAdmin}">
				<input type="hidden" name="fromDistId"
					value="${sale.fromDistributor.id}" />
			</c:when>
		</c:choose>
		<div class="form-group">
			<label for="shipDate" class="col-sm-2 control-label">销售日期</label>
			<div class="col-sm-10">
				<input type="date" class="form-control" id="shipDate"
					name="shipDate" placeholder="销售日期 "
					value="<fmt:formatDate value="${sale.shipDate}" pattern="yyyy-MM-dd"/>">
			</div>
		</div>

		<c:choose>
			<c:when test="${!isAdmin}">
				<div class="form-group">
					<label for="distId" class="col-sm-2 control-label">分销商</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="distributorName"
							placeholder="分销商" name="distributorName"
							value="${sale.fromDistributor.name}" />
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="form-group">
					<label for="distId" class="col-sm-2 control-label">分销商</label>
					<div class="col-sm-10">
						<select class="form-control" id="fromDistId" name="fromDistId"
							value="${sale.fromDistributor.id}">
							<c:forEach items="${distributors}" var="distributor">
								<c:if
									test="${distributor.id > 1 && distributor.id == sale.fromDistributor.id}">
									<option value="${distributor.id}" selected>${distributor.name}</option>
								</c:if>
								<c:if
									test="${distributor.id > 1 && distributor.id != sale.fromDistributor.id}">
									<option value="${distributor.id}">${distributor.name}</option>
								</c:if>
								<c:if
									test="${distributor.id ==1}">
									<option value="${distributor.id}">${distributor.name}</option>
								</c:if>
							</c:forEach>
						</select>
					</div>
				</div>
			</c:otherwise>
		</c:choose>

		<div class="form-group">
			<label for="remark" class="col-sm-2 control-label">备注</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="remark" placeholder="备注"
					name="remark" value="${sale.remark}">
			</div>
		</div>
		<div class="form-group">
			<label for="totalPrice" class="col-sm-2 control-label">总金额(元)</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="totalPrice"
					readonly="true" placeholder="总金额" value="${sale.totalPrice}">
			</div>
		</div>



		<div class="col-sm-offset-2 col-sm-10">
			<button type="submit" class="btn btn-default">保存</button>
			<button type="button" class="btn btn-primary btn-sm"
				onclick="addNewLine(${sale.id})">添加销售项</button>

		</div>
	</form>


	<br />
	<h3>销售项目</h3>
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
			<c:forEach items="${sale.lines}" var="line">
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
							onclick="confirmDeleteLine(${sale.id},${line.id})">删除</button>

					</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br /> <br />

	<script>
       function addNewLine(shipmentId){
    	   if(shipmentId!=null){
    	   	window.location='editSaleLine.do?mode=new&saleId='+shipmentId;
    	   }else{
    		   alert("请先保存销售单！");
    	   }
    	   			
       }
       
       function confirmDeleteLine(shipmentId,oid){
     		var confirmed = window.confirm("您确认要删除该项吗？");
     		if(confirmed){
     			window.location="deleteSaleLine.do?saleId="+shipmentId+"&lineId="+oid;
     		}
     	}
    
    </script>


</div>
<%@ include file="footer.jspf"%>
