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
  margin-top:20px;
  border: none;
  cursor: pointer;
  width: 20%;
  opacity: 0.9;
}
/* 작은 버튼 */
.btn{
	background-color: #1abc9c;
	color: white;
	padding: 5px 5px;
	border: none;
	cursor: pointer;
	width: 8%;
	opacity: 0.9;
}
/* 작은 버튼이 있는 div */
.btndiv{
	margin-right:80px;
	margin-bottom:20px;
	text-align:right;
}
</style>
<script>
function myList(email){
	//내가 쓴 글 보는 요청 보내기
	location.href = "/board/myList/"+email;
}
</script>
</head>
<body align="center">
<h2>게시글 목록</h2>
<div class="btndiv">
	<button class="btn" onClick="location.href='/board/write'">글 작성</button>
	<button class="btn" onClick="myList('${userEmail}')">내가 쓴 글</button>
</div>
<table align="center">
	<tr>
		<th width="10%">No</th>
		<th width="35%">제목</th>
    	<th width="15%">작성자</th>
    	<th width="15%">작성일</th>
    	<th width="10%">조회수</th>
	</tr>
	<c:forEach var="l" items="${list}">
	<tr>
		<td>${l.bno}</td>
		<td><a href="/board/detail/${l.bno}">${l.title}</a></td>		<!-- 상세보기 -->
		<td><a href="/board/list/${l.writer}">${l.writer}</a></td>		<!-- 해당 작성자의 글 보기 -->
		<td>${l.regdate}</td>
		<td>${l.hit}</td>
	</tr>
	</c:forEach>

</table>
<button type="button" onClick="location.href='/'">메인으로</button>
</body>
</html>