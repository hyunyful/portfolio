<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" pageEncoding="UTF-8" %>
<!-- header.jsp 추가 -->
<%@ include file="include/header.jsp" %>
<html>
<head>
	<title>Home</title>
	<style>
	a{
		text-decoration:none;
	}
	</style>
</head>
<body>
<a href="/user/join">회원가입</a><br/>
<a href="/user/login">로그인</a>
<h4>${userInfo}</h4>
</body>
</html>
