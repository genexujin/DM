<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<h3>出货单列表</h3>
		<br />
	<button type="button" class="btn btn-primary btn-sm"
		onclick="javascript:window.location.href='editShipment.do?mode=new'">出货</button>
	<br />
	<br />
	
	<c:choose>
		<c:when test="${isAdmin}">
			<form id="shipmentForm" class="form-inline" role="form"
				action="listShipment.do" method="post">
				<div class="form-group">
					<label for="distId" class="sr-only">分销商</label>
					<div>
						<select class="form-control" id="distId" name="distributorId"
							value="${distributorId}">
							<c:forEach items="${distributors}" var="distributor">
								<c:if test="${distributor.id == distributorId}">
									<option value="${distributor.id}" selected>${distributor.name}</option>
								</c:if>
								<c:if test="${distributor.id != distributorId}">
									<option value="${distributor.id}">${distributor.name}</option>
								</c:if>
							</c:forEach>
						</select>
					</div>
				</div>
				<button type="submit" class="btn btn-default">查看</button>
			</form>
			<br />
			<br />
		</c:when>
	</c:choose>
	
	<table
		class="table table-striped table-bordered table-hover table-responsive table-condensed">
		<thead>
			<tr>
				
				<th>日期</th>
				<th>总出库金额</th>
				<th>备注</th>						
				<c:choose>
					<c:when test="${isAdmin}">
						<th style="width: 20%;">操作</th>

					</c:when>
				</c:choose>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="item">
				<tr>					
					<td><fmt:formatDate value="${item.shipDate}" pattern="yyyy-MM-dd"/></td>
					<td><c:out value="${item.totalPrice}"></c:out></td>
					<td><c:out value="${item.remark}"></c:out></td>					
					<c:choose>
						<c:when test="${isAdmin}">
							<td>
								<button class="btn btn-primary btn-xs" onclick="javascript:window.location.href='editShipment.do?mode=edit&id=${item.id}'">明细</button>

								<button class="btn btn-primary btn-xs" onclick="confirmDelete(${item.id})">删除</button>

							</td>
						</c:when>
					</c:choose>

				</tr>
			</c:forEach>
		</tbody>
	</table>

	<ul class="pagination">
		<c:forEach begin="1" end="${totalPage}" var="i" step="1">
			<li class="${currentPage==i?'active':''}"><a
				href="listShipment.do?pageNumInput=${i}&distributorId=${distributorId}">${i}</a></li>
		</c:forEach>
	</ul>


	<script>
	function confirmDelete(oid){
  		var confirmed = window.confirm("您确认要删除该项吗？");
  		if(confirmed){
  			window.location="deleteShipment.do?id="+oid;
  		}
  	}
		
	</script>

</div>
<%@ include file="footer.jspf"%>