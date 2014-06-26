<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<h3>经销商列表</h3>
	<br />
	<button type="button" class="btn btn-primary btn-sm"
		onclick="javascript:window.location.href='editDist.do?mode=new'">新建经销商</button>
	<br /> <br />	

	<table
		class="table table-striped table-bordered table-hover table-responsive table-condensed">
		<thead>
			<tr>
				<th>名称</th>
				<th>地址</th>
				<th>电话</th>
				<th>备注</th>
				<th style="width: 20%;">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${dists}" var="dist">
				<tr>
					<td><c:out value="${dist.name}"></c:out></td>					
					<td><c:out value="${dist.address}"></c:out></td>
					<td><c:out value="${dist.phone}"></c:out></td>
					<td><c:out value="${dist.remark}"></c:out></td>

					<td>
						<button class="btn btn-primary btn-xs"
							onclick="javascript:window.location.href='editDist.do?mode=edit&id=${dist.id}'">修改</button>

						<button class="btn btn-primary btn-xs"
							onclick="confirmDelete(${dist.id})">删除</button>
				</tr>
			</c:forEach>
		</tbody>
	</table>



	<script>
	function confirmDelete(oid){
  		var confirmed = window.confirm("您确认要删除该项吗？");
  		if(confirmed){
  			window.location="deleteDist.do?id="+oid;
  		}
  	}
		
	</script>

</div>
<%@ include file="footer.jspf"%>