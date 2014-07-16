<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<h3>商品列表</h3>
		<br />
	
	<br />
	
	<form id="itemForm" role="form" action="listItems.do" method="post">
		<input	type="hidden" class="form-control" id="pageNumInput" name="pageNumInput" value="${currentPage}">
		<div class="form-group">
			<label for="exampleInputEmail1">商品编号</label> <input
				type="text" class="form-control" id="code" name="code" value="${code}"
				placeholder="请输入商品编号，然后点击查询">
		</div>
		<button type="submit" class="btn btn-success btn-sm">查询</button>
		<button type="button" class="btn btn-primary btn-sm"
		onclick="javascript:window.location.href='editItem.do?mode=new'">新增商品</button>
	</form>
	<br />
	<table
		class="table table-striped table-bordered table-hover table-responsive table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>类别</th>
				<th>名称</th>
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
					<td><c:out value="${item.code}"></c:out></td>
					<td><c:out value="${item.model}"></c:out></td>
					<td><c:out value="${item.name}"></c:out></td>
					<td><c:out value="${item.remark}"></c:out></td>
					<c:choose>
						<c:when test="${isAdmin}">
							<td>
								<button class="btn btn-primary btn-xs" onclick="javascript:window.location.href='editItem.do?mode=edit&id=${item.id}'">修改</button>

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
				href="listItems.do?pageNumInput=${i}&code=${code}">${i}</a></li>
		</c:forEach>
	</ul>


	<script>
	function confirmDelete(oid){
  		var confirmed = window.confirm("您确认要删除该项吗？");
  		if(confirmed){
  			window.location="deleteItem.do?id="+oid;
  		}
  	}
		
	</script>

</div>
<%@ include file="footer.jspf"%>