<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
            <!-- header.jsp 추가 -->
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- ckeditor CDN (textarea 편집기) -->
<script src="https://cdn.ckeditor.com/4.14.0/standard/ckeditor.js"></script>
<script>
window.addEventListener('load',function(){
	//편집기 젹용
    CKEDITOR.replace('content');
});
</script>
<style>
<style>
* {
  box-sizing: border-box;
}

/* Add padding to containers */
.container {
  padding: 16px;
  background-color: white;
}

/* Full-width input fields */
input[type=text], input[type=password] {
  width: 100%;
  padding: 15px;
  margin: 5px 0 22px 0;
  display: inline-block;
  border: none;
  background: #f1f1f1;
}

input[type=text]:focus, input[type=password]:focus {
  background-color: #ddd;
  outline: none;
}

/* Overwrite default styles of hr */
hr {
  border: 1px solid #f1f1f1;
  margin-bottom: 25px;
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
  align:center;
}
.btndiv{
	text-align:center;
}
</style>

</head>
<body>
<h2>게시글 수정 페이지</h2>

	<!-- 게시글 수정 실패 메세지 -->
	<div style="color:red;text-align:center;margin:0 auto">
	<c:if test="${update == false}">
		수정에 실패하셨습니다.<br/>
		다시 시도해주세요.
	</c:if>
	</div>

<form method="post" action="/board/update">
  <div class="container">
    <hr>
    <input type="hidden" value="${detail.bno}" name="bno" />
    <label for="title"><b>글 제목</b></label>
    <input type="text" value="${detail.title}" name="title" required>

    <label for="writer"><b>작성자</b></label>
    <input type="text" value="${detail.writer}" name="writer" readonly="readonly">

    <label for="regdate"><b>작성일</b></label>
    <input type="text" value="${detail.regdate}" name="regdate" readonly="readonly">
    <hr>
	<textarea name="content" required>${detail.content}</textarea>
  </div>
  <div class="btndiv">
	<button type="button" onClick="location.href='/'">메인으로</button>
	<button type="submit">수정완료</button>
</div>
</form>
</body>
</html>