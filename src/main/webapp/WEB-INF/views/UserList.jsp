<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<h3>用户列表</h3>
	<br />
	<button type="button" class="btn btn-primary btn-sm"
		onclick="javascript:window.location.href='editUser.do?mode=new'">新建用户</button>
	<br /> <br />	

	<table
		class="table table-striped table-bordered table-hover table-responsive table-condensed">
		<thead>
			<tr>
				<th>用户名</th>
				<th>密码</th>
				<th>经销商</th>
				<th>超级用户</th>
				<th style="width: 20%;">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${users}" var="user">
				<tr>
					<td><c:out value="${user.name}"></c:out></td>					
					<td><c:out value="${user.password}"></c:out></td>
					<td><c:out value="${user.distributor.name}"></c:out></td>
					<td><c:out value="${user.isMaster}"></c:out></td>
					<td>
						<button class="btn btn-primary btn-xs"
							onclick="javascript:window.location.href='editUser.do?mode=edit&id=${user.id}'">修改</button>
						<button class="btn btn-primary btn-xs"
							onclick="confirmDelete(${user.id})">删除</button>
				</tr>
			</c:forEach>
		</tbody>
	</table>



	<script>
	function confirmDelete(oid){
  		var confirmed = window.confirm("您确认要删除该项吗？");
  		if(confirmed){
  			window.location="deleteUser.do?id="+oid;
  		}
  	}
		
	</script>

</div>
<%@ include file="footer.jspf"%>