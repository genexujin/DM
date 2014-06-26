<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<h3>库存清单</h3>
	
	<c:choose>
		<c:when test="${isAdmin}">
			<form id="inventoryForm" class="form-inline" role="form"
				action="listInventory.do" method="post">
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
		</c:when>
	</c:choose>
	
	<br /> <br />
	<h4 class="text-primary">
		<c:out value='${currentDist.name}' />
		的库存清单
	</h4>
	<table
		class="table table-striped table-bordered table-hover table-responsive table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>类别</th>
				<th>名称</th>
				<th>库存</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${inventories}" var="inv">
				<tr>
					<td><c:out value="${inv.item.code}"></c:out></td>
					<td><c:out value="${inv.item.model}"></c:out></td>
					<td><c:out value="${inv.item.name}"></c:out></td>
					<td><c:out value="${inv.amount}"></c:out></td>
					<td><c:out value="${inv.item.remark}"></c:out></td>

				</tr>
			</c:forEach>
		</tbody>
	</table>

	<ul class="pagination">
		<c:forEach begin="1" end="${totalPage}" var="i" step="1">
			<li class="${currentPage==i?'active':''}"><a
				href="listInventory.do?pageNumInput=${i}&distributorId=${currentDist.id}">${i}</a></li>
		</c:forEach>
	</ul>



</div>
<%@ include file="footer.jspf"%>