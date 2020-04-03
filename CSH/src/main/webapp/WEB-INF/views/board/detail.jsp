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
<script>
//게시글 삭제 함수
function del(bno){
	var result = confirm("정말 삭제하시겠습니까?");
	
	//삭제하시겠다고 하면
	if(result){
		location.href="/board/delete/"+bno;
	}
}
</script>
</head>
<body>
	<!-- 게시글 삭제 실패 메세지 -->
	<div style="color:red;text-align:center;margin:0 auto">
	<c:if test="${delete == false}">
		삭제에 실패하셨습니다.<br/>
		다시 시도해주세요.
	</c:if>
	</div>

<form method="post">
  <div class="container">
    <hr>
    <label for="title"><b>글 제목</b></label>
    <input type="text" value="${detail.title}" name="title" id="title" readonly="readonly">

    <label for="writer"><b>작성자</b></label>
    <input type="text" value="${detail.writer}" name="writer" readonly="readonly">

    <label for="regdate"><b>작성일</b></label>
    <input type="text" value="${detail.regdate}" name="regdate" readonly="readonly">
    <hr>
	<textarea name="content" id="content" readonly>${detail.content}</textarea>
  </div>
</form>
<div class="btndiv">
	<button type="button" onClick="location.href='/'">메인으로</button>
	<c:if test="${userNickname == detail.writer}">
	<button type="button" onClick="location.href='/board/update/'+${detail.bno}">수정하기</button>
	<button type="button" onClick="del('${detail.bno}')">삭제하기</button>
	</c:if>
</div>
</body>
</html>