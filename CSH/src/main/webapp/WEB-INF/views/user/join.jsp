<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- header.jsp 추가 -->
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<style>
/* input 공간 */
input[type=text], input[type=password] {
	width: 30%;
	padding: 15px;
	margin: 5px 0 22px 0;
	display: inline-block;
	border: none;
	background: #f1f1f1;
}
/* 이메일 입력공간 */
#e,#mail{
	width: 5%;
	padding: 15px;
	margin: 5px 0 22px 0;
	display: inline-block;
	border: none;
	background: #f1f1f1;
}
/* label */
label{
	text-size:30px;
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
/* 이메일, 닉네임 중복검사 버튼 */
#emailbtn,#nicknamebtn{
	background-color: #1abc9c;
	color: white;
	padding: 5px 5px;
	margin: 8px 0;
	border: none;
	cursor: pointer;
	width: 8%;
	opacity: 0.9;
}
/* select 태그 너비 */
.mailSelect{
	width:100px;
}
</style>
<script>
function mailChange(self){
	var mail = document.getElementById("mail");
	mail.readOnly = true;
	mail.value="";
	
	//직접 입력을 선택한 경우
	if(self.value === "n"){		
		mail.value="";						//input값 초기회
		mail.readOnly = false;			//readOnly 해제
		mail.focus();							//input에 포커스
		return;
	}else if(self.value != "a"){
		mail.value = self.value;
	}
}
</script>
</head>
<body align="center">
<h2 style="background:#1abc9c;">회원가입</h2>
<form method="post" id="joinform" enctype="multipart/form-data">
	<img id="img" width="100" height="100" border="1"><br/>
	<input type="file" id="image" name="image" accept=".png, .gif, .jpg" /><br/><br/>

	<label for="e">이메일</label><br/>
	<input type="text" id="e" name="e" placeholder="이메일 입력" />@
	<input type="text" id="mail" name="mail" readonly="readonly" placeholder="이메일 선택"/>
	<select onchange="mailChange(this)" class="mailSelect">
		<option value="a">이메일 선택</option>
		<option value="naver.com">naver.com</option>
		<option value="gmail.com">gmail.com</option>
		<option value="daum.net">daum.net</option>
		<option value="n">직접 입력</option> 
	</select>
	<button type="button" id="emailbtn">이메일 중복검사</button>
	<div id="emailtext"></div><br/>
	
	<label for="pw">비밀번호</label><br/>
	<input type="password" id="pw" name="pw" placeholder="영문+숫자로 8자 이상" />
	<div id="pwtext"></div><br/>
	
	<label for="pwcheck">비밀번호 확인</label><br/>
	<input type="password" id="pwcheck" placeholder="비밀번호와 같게 입력" />
	<div id="pwchecktext"></div><br/>
	
	<label for="nickname">닉네임</label><br/>
	<input type="text" id="nickname" name="nickname" placeholder="닉네임을 설정해주세요"/><br/>
	<button type="button" id="nicknamebtn">닉네임 중복검사</button>
	<div id="nicknamektext"></div><br/>
	
     <input type="hidden" value="common" name="type" readOnly="readOnly" /><br/>
     <input type="hidden" value="1" name="authority" readOnly="readOnly" />
     <button type="submit">회원가입</button>&nbsp;&nbsp;
	<button type="button" onClick="location.href='/'">메인으로</button>
</form>

<script>
//유효성 체크 변수
var emailflag = false;
var pwflag = false;
var nicknameflag = false;

//입력창 및 버튼 변수로 받아오기
var image = document.getElementById("image");
var img = document.getElementById("img");
var eplace = document.getElementById("e");
var mailplace = document.getElementById("mail");
var emailbtn = document.getElementById("emailbtn");
var emailtext = document.getElementById("emailtext");
var pwplace = document.getElementById("pw");
var pwtext = document.getElementById("pwtext");
var pwcheckplace = document.getElementById("pwcheck");
var pwchecktext = document.getElementById("pwchecktext");
var nicknameplace = document.getElementById("nickname");
var nicknamebtn = document.getElementById("nicknamebtn");
var nicknamektext = document.getElementById("nicknamektext");
var joinform = document.getElementById("joinform");
var joinbtn = document.getElementById("joinbtn");

//파일 선택하면 img태그에 보이도록
image.addEventListener("change",function(e){
	//선택한 파일이 있다면
	if(this.files && this.files[0]){
		//파일의 내용읽기
		var reader=new FileReader();
		reader.readAsDataURL(this.files[0]);
			
		//파일을 읽는 동안은 비동기적으로 동작하기 때문에
		//파일을 읽는 동작이 끝나면 img 태그에 설정하도록
		reader.addEventListener("load",function(e){
			img.src=e.target.result;
		});
	}
});

//이메일 중복검사
emailbtn.addEventListener('click',function(e){
	var e = eplace.value;
	var mail = mailplace.value;	//눌렀을 때의 값 가져오기
	
	if(e.length == 0 || mail.length == 0){			//이메일을 적지 않고 버튼을 눌렀을 때
		emailtext.innerHTML = "이메일을 먼저 입력해주세요";
		emailtext.style.color = "RED";
		return;
	}else if(mail == "a"){
		emailtext.innerHTML = "메일을 선택해주세요";
		emailtext.style.color = "RED";
		return;
	}
	
	emailtext.innerHTML = "";
	var email = e+"@"+mail;
	
 	//비동기로 디비에서 중복검사
	//순수 자바스크립트
	var request = new XMLHttpRequest();		//요청 객체 생성
	request.open("GET","/user/join/emailcheck?email="+email);		//요청 주소 생성
	request.send('');										//요청 보내기
	
	request.onerror = function(e){
		console.log(e);
	}
	
	//비동기이기 때문에 콜백함수 필요
	request.onreadystatechange = function(){
		//정상응답이 오면
		if(request.readyState == 4){
			if(request.status >= 200 && request.status < 300){
				var obj = request.responseText;		//결과 받기
				var json = JSON.parse(obj);			//json파싱
				
				if(json.result == true){
					emailtext.innerHTML = "사용 가능한 이메일입니다";
					emailtext.style.color = 'blue';
					emailflag = true;
				}else{
					emailtext.innerHTML = "사용 불가능한 이메일입니다";
					emailtext.style.color = 'red';
					emailflag = false;
				}
			}
		}
	}
});

//비밀번호 유효성		//영문+숫자로 8자 이상		//대소문자 구분X
pwplace.addEventListener('keyup',function(){
	//키를 누를때마다 적힌 값 갱신
	var pwval = pwplace.value;
	
	//정규식 패턴 설정
	var p1=/[0-9]/;			//숫자
	var p2=/[a-zA-Z]/;	//영문
	
	//8글자 안에서 영어와 숫자가 혼용인지 확인
	if(pwval.length >= 8){
		if(p1.test(pwval) && p2.test(pwval)){
			pwtext.innerHTML = "보안강도 높음";
			pwtext.style.color = 'blue';
		}else{
			pwtext.innerHTML = "보안강도 낮음";
			pwtext.style.color = 'red';
		}	
	}else{
		pwtext.innerHTML = "비밀번호는 영문과 숫자를 혼합하여 8글자 이상으로 작성해주세요";
		pwtext.style.color = 'red';
		pwfalg = false;
	}

});

//비밀번호와 비밀번호 확인 유효성
pwcheckplace.addEventListener('focusout',function(){
	//console.log("비번확인 유효성 시작");
	var pw = pwplace.value.trim();
	var pw2 = pwcheckplace.value.trim();
	
	if(pw != pw2){
		pwchecktext.innerHTML = "비밀번호가 일치하지 않습니다";
		pwchecktext.style.color = 'red';
		pwflag = false;
	}else{
		pwchecktext.innerHTML = "비밀번호가 일치합니다";
		pwchecktext.style.color = 'blue';
		pwflag = true;
	}
});

//닉네임 중복검사
nicknamebtn.addEventListener('click',function(){
	var nickname = nicknameplace.value;
	
	//입력하지 않고 버튼을 누르면 리턴
	if(nickname.length == 0){
		nicknamektext.innerHTML = "닉네임을 먼저 입력해주세요";
		nicknamektext.style.color = "red";
		return;
	}
	
	var url = "/user/join/nicknamecheck?nickname="+nickname;
	//jquery로 보내보기
	$.ajax({
		url:url,
		type:"get",
		success:function(result){
			if(result.result == true){
				nicknamektext.innerHTML = "사용 가능한 닉네임 입니다.";
				nicknamektext.style.color = "blue";
				nicknameflag = true;
			}else{
				nicknamektext.innerHTML = "사용 불가능한 닉네임 입니다.";
				nicknamektext.style.color = "red";
				nicknameflag = false;
			}
		},
		error:function(e){
			console.log(e);
		}
	});
});

//전체 유효성
joinform.addEventListener('submit',function(e){	
	
	var pwalert = pwtext.innerHTML;		//보안강도 여부 확인하기 위해서 가져오기
	
	//id 유효성을 통과하지 못한 경우
	if(emailflag == false){
		alert("id가 올바르지 않습니다");
		idplace.focus();
		e.preventDefault();
		return;
	}
	//비밀번호 확인이 일치하지 않는 경우
	else if(pwflag == false){
		alert("비밀번호와 확인이 일치하지 않습니다");
		pwcheckplace.focus();
		e.preventDefault();
		return;
	}
	//닉네임 유효성 통과했는지
	else if(nicknameflag == false){
		alert("사용가능한 닉네임을 입력해주세요");
		nicknameplace.focus();
		e.preventDefault();
		return;
	}
	//보안강도가 낮을 경우 정말 이 비밀번호를 사용할지 다시 선택할지 선택권 주기
	else if(pwalert == "보안강도 낮음"){		//보안강도가 낮다고 적혀있으면
		var result = confirm("비밀번호의 보안강도가 낮습니다.\n이대로 진행하시겠습니까?");		//선택권 주기

		if(result){			//이대로 진행한다고 한 경우 회원가입 진행
			alert("비밀번호 보안강도로 인한 책임은 홈페이지에서 지지 않습니다.\n회원가입을 진행합니다.");
		}else{				//아닌 경우 다시 비밀번호를 작성하도록
			alert("비밀번호의 강도를 높이시길 원한다면\n영문,숫자 혼합으로 8글자 이상으로 작성해주세요");
			pwplace.focus();
			e.preventDefault();
			return;
		}
	}
	
	alert("유효성 다 통과~!");
});

</script>
</body>
</html>