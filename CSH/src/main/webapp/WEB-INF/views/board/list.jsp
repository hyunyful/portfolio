<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <!-- header.jsp 추가 -->
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
table {
  border-collapse: collapse;
  border-spacing: 0;
  width: 90%;
  border: 1px solid #ddd;
}

th, td {
  text-align: center;
  padding: 16px;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}
/* 회원가입, 메인으로 버튼 */
button{
  background-color: #1abc9c;
  color: white;
  padding: 16px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 20%;
  opacity: 0.9;
}
</style>
</head>
<body align="center">
<h2>게시글 목록</h2>
<table align="center">
	<tr>
		<th width="10%">No</th>
		<th width="35%">제목</th>
    	<th width="15%">작성자</th>
    	<th width="15%">작성일</th>
    	<th width="10%">조회수</th>
  </tr>
<%for(int i=0;i<5;i++){ %>
  <tr>
    <td>번호</td>
    <td>으어</td>
    <td>Smith</td>
    <td>오늘</td>
    <td>0</td>
  </tr>
 <%} %>
</table>
<button type="button" onClick="location.href='/'">메인으로</button>
</body>
</html>