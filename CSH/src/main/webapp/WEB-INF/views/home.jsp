<%@ page pageEncoding="UTF-8" isELIgnored="false"  %>
<!-- header.jsp 추가 -->
<%@ include file="include/header.jsp" %>
<html>
<head>
<title>Home</title>
<style>
.col {
 	/*text-align:right;*/
 	margin: 0px 40px 0px 20px;		/*위 오른쪽 아래 왼쪽 순*/
	/*border: 4px dashed #1abc9c;*/
}
input,
.btn {
 	width: 100%;
  	padding: 12px;
  	border:none;
  	border-radius: 4px;
  	margin: 5px 0;
  	opacity: 0.85;
  	display: inline-block;
  	font-size: 17px;
  	line-height: 20px;
  	text-decoration: none; /* remove underline from anchors */
}
/* 아이디, 비밀번호 */
input:hover,
.btn:hover {
  	opacity: 1;
}
/* 로그인 버튼 */
input[type=submit] {
  	background-color: #1abc9c;
  	color: white;
  	cursor: pointer;
}
/* 로그인 버튼 */
input[type=submit]:hover {
  	background-color: #1abc9c;
}
/* 최신글 공간과 유저 정보 나누는 div에서 최신글쪽 */
div.left {
	width: 70%;
	float: left;
	box-sizing: border-box;
}
/* 최신글 공간과 유저 정보 나누는 div에서 유저정보쪽 */
div.right {
	width: 30%;
	float: right;
	box-sizing: border-box;       
}
/* 메인에 최신글 나오는 공간 테두리 */
.board{
	border:solid 1px;
	height:220px;
	margin:0px 20px 0px 0px;	
}
/* 로그아웃, 마이페이지에 영향 */
button{
	background-color: #1abc9c;
  	color: white;
  	cursor: pointer;
  	width: 49%;
  	padding: 12px;
  	border:none;
  	border-radius: 4px;
  	margin: 5px 0;
  	opacity: 0.85;
  	display: inline-block;
  	font-size: 17px;
  	line-height: 20px;
  	text-decoration: none; /* remove underline from anchors */
}
/* 유저 정보 공간 */
.userInfo{
	width: 50%;
	float: right;
	box-sizing: border-box;      
}
</style>
<script>
//로딩되면 비동기로 게시판 글 가져오기
window.addEventListener('load',function(){
	var boarddiv = document.querySelector('.board');
	boarddiv.innerText = "게시판 글 5개 보여질 예정";
	//getBoard();	
});
</script>
</head>
<body>
<div class="col">
	<div class="left">		<!-- col div를 left, right로 분할 -->
	<!-- 최신 글 보기 -->
		<div>최신글 보기 <a href="/board/list">more</a></div>
			<div class="board"></div>
		</div>
	<div class="right">
	<!-- 회원가입 성공 메세지 -->
	<div style="color:orange;text-align:center;margin:0 auto">
	<c:if test="${joinResult != null}">
		회원가입에 성공하셨습니다.<br/>
		로그인을 진행해주세요.
	</c:if>
	</div>

	<!-- 로그인 실패 메세지 -->
	<div style="color:red;text-align:center;margin:0 auto">
	<c:if test="${loginResult != null}">
		로그인에 실패하셨습니다.<br/>
		다시 시도해주세요.
	</c:if>
	</div>
	
	<!-- 비밀번호 재설정 메세지 -->
	<div style="color:orange;text-align:center;margin:0 auto">
	<c:if test="${resetPw == true}">		<!-- 비밀번호 재설정 성공 -->
		비밀번호가 성공적으로 재설정되었습니다.<br/>
		변경된 비밀번호로 다시 로그인해주세요.
	</c:if>
	</div>
	<div style="color:red;text-align:center;margin:0 auto">
	<c:if test="${resetPw == false}">		<!-- 비밀번호 재설정 실패 -->
		비밀번호 재설정에 실패하였습니다.<br/>
		다시 시도해주세요ㅠㅠ.
	</c:if>
	</div>

	<c:if test="${userNickname == null}">
		<form method="post" action="/user/login">
			<input type="text" name="email" placeholder="email" required><br><br>
			<input type="password" name="pw" placeholder="Password" required><br><br>
			<input type="submit" value="Login">
		</form>
		<!-- 카카오 계정으로 로그인 하기 버튼 -->
		<img src="../resources/images/kakao_login_btn.png" onClick="kakaoLogin()" width="49%" height="40px">
		<!-- 네이버 계정으로 로그인하기 버튼 -->
		<img src="../resources/images/naver_login_greenbtn_all.PNG" onClick="naverLogin()" width="49%" height="40px">
		<a href="/user/join">회원가입</a>
	</c:if>

	<c:if test="${userNickname != null}">
		<%-- <img src="../resources/userimage/${userImage}" width="200px" height="200px"> --%>
		<img src="${userImage}" width="200px" height="200px">
		<div class="userInfo">
			<a href="/user/mypage">${userNickname}</a>님 환영합니다!<br/>
			<a href="/user/pwreset">비밀번호 재설정</a><br/><br/>
			<a href="/board/myList/${userEmail}">내가 쓴 글 보러가기</a><br/>
			<a href="#">오늘 날씨 보러가기</a><br/><br/>
		</div>
		<button type="button" onClick="location.href='/user/logout'">로그아웃</button>
		<button type="button" onClick="location.href='/user/mypage'">마이페이지</button>
	</c:if>
	</div>
</div>

<div>
<!-- 뉴스 기사 또는 지도 등 -->
</div>

<script>
//카카오 로그인 함수
function kakaoLogin(){
	var uri = "https://kauth.kakao.com/oauth/authorize?";
	uri += "client_id=a4145c3d7c70775e9b0ab558aee4ee92";
	uri += "&redirect_uri=http://localhost:8080/user/kakaologin&response_type=code";
	location.href=uri;
}

//네이버 로그인 함수
function naverLogin(){
	//var stateToken;		//상태토큰을 저장해둘 변수
	
	//비동기로 상태 토큰을 생성해서 받아오기
	//jquery로 해보기
	$.ajax({
		url:"/user/naverLogin",
		type:"GET",
		success:function(result){
			//console.log("받아온 상태 토큰의 값은 "+result.stateToken);
			stateToken = result.stateToken;
			
			//받아온 상태토큰과 다른 파라미터들을 가지고 네이버에서 요구하는 url로 이동
			var uri = "https://nid.naver.com/oauth2.0/authorize?client_id=Pkyzx4PwdKj4LrNvkqXS";
			uri += "&response_type=code&redirect_uri=http://localhost:8080/user/naverLogin/getToken";
			uri += "&state="+stateToken;
			//alert(uri);
			location.href=uri;
		},
		error:function(){
			alert("에러 발생");
		}
	}); 
	/* var url = "/user/naverLogin";
	location.href=url; */
}
</script>
</body>
</html>
