<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="header.jspf"%>
<div class="row-fluid">
	<br />
	<div class="span12">
		<form class="form-inline" action="login.do" method="post">
			<fieldset>
				<legend>登录系统</legend>
				<br /> <br /> <label>用户名: </label><input type="text"
					name="username" />
				<p>
					<label>密 码： </label><input type="password" name="password" /> <br />
					<br /> <br />
					<button class="btn" type="submit">提交</button>
			</fieldset>
		</form>
	</div>
</div>
<%@ include file="footer.jspf"%>