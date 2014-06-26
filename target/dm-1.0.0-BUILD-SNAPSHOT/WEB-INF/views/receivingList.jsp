<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<h3>入库单列表</h3>
		<br />
	<button type="button" class="btn btn-primary btn-sm"
		onclick="javascript:window.location.href='editReceipt.do?mode=new'">入库</button>
	<br />
	<br />
	<table
		class="table table-striped table-bordered table-hover table-responsive table-condensed">
		<thead>
			<tr>
				
				<th>日期</th>
				<th>总入库金额</th>
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
								<button class="btn btn-primary btn-xs" onclick="javascript:window.location.href='editReceipt.do?mode=edit&id=${item.id}'">明细</button>

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
				href="listReceipt.do?pageNumInput=${i}">${i}</a></li>
		</c:forEach>
	</ul>


	<script>
	function confirmDelete(oid){
  		var confirmed = window.confirm("您确认要删除该项吗？");
  		if(confirmed){
  			window.location="deleteReceipt.do?id="+oid;
  		}
  	}
		
	</script>

</div>
<%@ include file="footer.jspf"%>