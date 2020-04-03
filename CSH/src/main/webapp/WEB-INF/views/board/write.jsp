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
body {
  font-family: Arial, Helvetica, sans-serif;
  background-color: black;
}

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

/* Set a style for the submit button */
.registerbtn {
  background-color: #4CAF50;
  color: white;
  padding: 16px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 100%;
  opacity: 0.9;
}

.registerbtn:hover {
  opacity: 1;
}

/* Add a blue text color to links */
a {
  color: dodgerblue;
}

/* Set a grey background color and center the text of the "sign in" section */
.signin {
  background-color: #f1f1f1;
  text-align: center;
}
</style></style>
</head>
<body>
<form method="post">
  <div class="container">
    <hr>
    <label for="title"><b>글 제목</b></label>
    <input type="text" placeholder="글 제목을 입력하세요" name="title" id="title" required>

    <label for="writer"><b>작성자</b></label>
    <input type="text" value="${userNickname}" name="writer" readonly="readonly">

    <label for="regdate"><b>작성일</b></label>
    <input type="text" value="오늘" name="regdate" readonly="readonly">
    <hr>
	<textarea name="content" required></textarea>
    <button type="submit" class="registerbtn">Register</button>
  </div>
</form>

</body>
</html>