<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<div class="btn-group">
		<button type="button" class="btn btn-primary btn-sm"
			onclick="javascript:window.location.href='listUser.do'">&lt;
			&lt; 返回列表</button>
	</div>
	<br />
	<h3>用户信息</h3>
	<br />
	<form id="returnForm" role="form" action="saveUser.do" method="post">
		<input type="hidden" class="form-control" id="id" name="id"
			value="${user.id}">

		<div class="form-group">
			<label for="username" class="col-sm-2 control-label">用户名</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="username"
					<c:if test="${sessionScope['LOGIN_USER'].isMaster=='N'}"> readonly </c:if>
					placeholder="用户名" name="name" value="${user.name}">
			</div>
		</div>

		<div class="form-group">
			<label for="password" class="col-sm-2 control-label">密码</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="password"
					placeholder="备注" name="password" value="${user.password}">
			</div>
		</div>

		<c:choose>
			<c:when test="${sessionScope['LOGIN_USER'].isMaster=='N'}">
				<div class="form-group">
					<label for="distId" class="col-sm-2 control-label">分销商</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="distributorName"
							placeholder="分销商" name="distributorName" readonly
							value="${user.distributor.name}" /> <input type="hidden"
							class="form-control" id="distId" placeholder="分销商" name="distId"
							value="${user.distributor.id}" />
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="form-group">
					<label for="distId" class="col-sm-2 control-label">经销商</label>
					<div class="col-sm-10">
						<select class="form-control" id="distId" name="distId"
							<c:if test="${sessionScope['LOGIN_USER'].isMaster=='N'}"> readonly </c:if>
							value="${user.distributor.id}">
							<c:forEach items="${distributors}" var="distributor">
								<c:choose>
									<c:when test="${distributor.id==user.distributor.id}">
										<option selected value="${distributor.id}">${distributor.name}</option>
									</c:when>
									<c:otherwise>
										<option value="${distributor.id}">${distributor.name}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</div>
				</div>
			</c:otherwise>
		</c:choose>


		<div class="form-group">
			<label for="totalPrice" class="col-sm-2 control-label">是否管理员</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="isMaster"
					<c:if test="${sessionScope['LOGIN_USER'].isMaster=='N'}"> readonly </c:if>
					name="isMaster" placeholder="请输入Y或者N" value="${user.isMaster}">
			</div>
		</div>



		<div class="col-sm-offset-2 col-sm-10">
			<button type="submit" class="btn btn-default">保存</button>
		</div>
	</form>



</div>
<%@ include file="footer.jspf"%>
