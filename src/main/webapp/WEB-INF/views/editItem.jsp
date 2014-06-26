<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
  

	<br />
	 <div class="btn-group">
		<button type="button" class="btn btn-primary btn-sm"
			onclick="javascript:window.location.href='listItems.do'">&lt; &lt; 返回列表</button>
	</div>
	<br />
	<h3>编辑商品信息</h3>
	
	<br />
	
	<form id="itemForm" role="form" action="saveItem.do" method="post">
	    <input	type="hidden" class="form-control" id="id" name="id" value="${item.id}">
		<div class="form-group">
			<label for="exampleInputEmail1">商品编号</label> <input
				type="text" class="form-control" id="code" name="code" value="${item.code}"
				placeholder="商品编号">
		</div>
		<div class="form-group">
			<label for="exampleInputPassword1">商品名称</label> <input
				type="text" class="form-control" id="name" name="name" value="${item.name}"
				placeholder="商品名称">
		</div>
		<div class="form-group">
			<label for="exampleInputPassword1">类别</label> <input
				type="text" class="form-control" id="model" name="model" value="${item.model}"
				placeholder="类别">
		</div>
		<div class="form-group">
			<label for="exampleInputPassword1">备注</label> <input
				type="text" class="form-control" id="remark" name="remark" value="${item.remark}"
				placeholder="备注">
		</div>
		
		<button type="submit" class="btn btn-default">保存</button>
	</form>



</div>
<%@ include file="footer.jspf"%>
