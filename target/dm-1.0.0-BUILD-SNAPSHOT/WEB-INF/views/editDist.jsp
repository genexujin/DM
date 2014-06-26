<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<div class="btn-group">
		<button type="button" class="btn btn-primary btn-sm"
			onclick="javascript:window.location.href='listDistributor.do'">&lt;
			&lt; 返回列表</button>
	</div>
	<br />
	<h3>经销商信息</h3>
	<br />
	<form id="returnForm" role="form" action="saveDist.do" method="post">
		<input type="hidden" class="form-control" id="id" name="id"
			value="${dist.id}">

		<div class="form-group">
			<label for="name" class="col-sm-2 control-label">名称</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="name"
					<c:if test="${sessionScope['LOGIN_USER'].isMaster=='N'}"> readonly </c:if>
					placeholder="名称" name="name" value="${dist.name}">
			</div>
		</div>

		<div class="form-group">
			<label for="address" class="col-sm-2 control-label">地址</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="address"
					placeholder="地址" name="address" value="${dist.address}">
			</div>
		</div>

		<div class="form-group">
			<label for="phone" class="col-sm-2 control-label">电话</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="phone"
					placeholder="电话" name="phone" value="${dist.phone}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="remark" class="col-sm-2 control-label">备注</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="remark"
					placeholder="备注" name="remark" value="${dist.remark}">
			</div>
		</div>



		<div class="col-sm-offset-2 col-sm-10">
			<button type="submit" class="btn btn-default">保存</button>
		</div>
	</form>



</div>
<%@ include file="footer.jspf"%>
