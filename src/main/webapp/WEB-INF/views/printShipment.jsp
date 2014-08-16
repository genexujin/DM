<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<html>
<head>
<title>${title}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->

<link rel="stylesheet" href="resources/jquery-ui.css">
<link rel="stylesheet"
	href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<!-- <script src="resources/jquery.js"></script> -->
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script
	src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script src="resources/jquery-ui.js"></script>

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
        <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>

<style>
html * {
	font-family: 'Microsoft YaHei', Arial, sans-serif !important;
}
</style>
<body>
	<div class="container">
		<br />
		<div class="container-fluid">
			<div class="row-fluid">
				<br /> <br />
				<h3>出货单</h3>
				<br />
				<p>
					<label>出货日期 :</label>
					<c:out value="${shipment.shipDate}" />
				</p>

				<p>
					<label>分销商 :</label>
					<c:out value="${shipment.toDistributor.name}" />
				</p>

				<p>
					<label>备注 :</label>
					<c:out value="${shipment.remark}" />
				</p>

				<p>
					<label>总金额(元) :</label>
					<c:out value="${shipment.totalPrice}" />
				</p>

				<h3>项目列表</h3>
				<table
					class="table table-striped table-bordered table-hover table-responsive table-condensed">
					<thead>
						<tr>

							<th>类别</th>
							<th>名称</th>
							<th>价格</th>
							<th>数量</th>
							<th>备注</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${shipment.lines}" var="line">
							<tr>
								<td><c:out value="${line.item.model}"></c:out></td>
								<td><c:out value="${line.item.name}"></c:out></td>
								<td><c:out value="${line.price}"></c:out></td>
								<td><c:out value="${line.amount}"></c:out></td>
								<td><c:out value="${line.remark}"></c:out></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>


	</div>

	<script type="text/javascript">
		$(document).ready(window.print());
	</script>

</body>
</html>
