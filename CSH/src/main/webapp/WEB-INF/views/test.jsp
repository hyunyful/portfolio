<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="/test" method="post">
		<input type="text" value="1" readOnly name="aa" />
		<input type="text" value="1" readOnly name="bb" />
		<button>전송</button>
	</form>
	
	<h2>${result}</h2>
</body>
</html>