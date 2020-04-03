<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <!-- header.jsp 추가 -->
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 찾기</title>
<style>
/* input 공간 */
input[type=password] {
	width: 30%;
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
/* 비번 일치 여부 확인 버튼 */
#pwbtn{
	background-color: #1abc9c;
	color: white;
	padding: 5px 5px;
	margin: 8px 0;
	border: none;
	cursor: pointer;
	width: 8%;
	opacity: 0.9;
}
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
<script>
//문서가 로드되면 비동기로 비밀번호 확인하는 작업
window.addEventListener('load',function(){
	var pw = prompt("아아- 비밀번호 확인이 있겠습니다");
	var data = {
			"pw":pw
	};
	
	//비동기로 맞는지 확인
	$.ajax({
		url:"/user/pwcheck",
		type:"post",
		data:data,
		success:function(result){
			//sns 로그인한 사람이면
			if(result.result == "sns"){
				alert("sns로 가입하신 유저는 비밀번호를 재설정하실 수 없습니다.\n자세한 사항은 02-1234-5678로 전화해주십시오.");
				location.href="/";
			}else if(result.result == "fail"){
				alert("비밀번호가 일치하지 않습니다.\n다시 시도해주세요.");
				location.href="/";
			}else if(result.result == "success"){
				alert("비밀번호 확인에 성공하셨습니다.");
			}
		},
		error:function(e){
			console.log(e.responseText);
		}
	});
});
</script>
</head>
<body align="center">
<h3>비밀번호 찾기 페이지</h3>
<form id="pwform" method="post">
	<label for="pw">새로운 비밀번호</label><br/>
	<input type="password" name="pw" id="pw" />
	<div id="pwtext"></div><br/>
	
	<label for="pw2">새로운 비밀번호 확인</label><br/>
	<input type="password" name="pw2" id="pw2" /><br/>
	<button type="button" id="pwbtn">비밀번호 일치 여부 확인</button>
	<div id="pw2text"></div><br/><br/>
	<button type="submit">변경</button>
</form>

<script>
var pwflag = false;
var pw2flag = false;

var pwplace = document.getElementById("pw");
var pwtext = document.getElementById("pwtext");
var pwbtn = document.getElementById("pwbtn");
var pw2place = document.getElementById("pw2");
var pw2text = document.getElementById("pw2text");
var pwform = document.getElementById("pwform");

//비밀번호 유효성		//영문+숫자로 8자 이상		//대소문자 구분X
pwplace.addEventListener('focusout',function(){
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
			pwflag = true;
		}else{
			pwtext.innerHTML = "보안강도 낮음";
			pwtext.style.color = 'red';
			pwflag = true;
		}	
	}else{
		pwtext.innerHTML = "비밀번호는 영문과 숫자를 혼합하여 8글자 이상으로 작성해주세요";
		pwtext.style.color = 'red';
		pwfalg = false;
	}

});

//비밀번호와 비밀번호 확인 유효성
pwbtn.addEventListener('click',function(){
	var pw = pwplace.value.trim();
	var pw2 = pw2place.value.trim();
	
	console.log("1 ",pw);
	console.log("2 ",pw2);
	
	if(pw != pw2){
		pw2text.innerHTML = "비밀번호가 일치하지 않습니다";
		pw2text.style.color = 'red';
		pw2flag = false;
	}else{
		pw2text.innerHTML = "비밀번호가 일치합니다";
		pw2text.style.color = 'blue';
		pw2flag = true;
	}
});

//전체 유효성
pwform.addEventListener('submit',function(e){
	console.log("1 "+pwflag);
	console.log("2 "+pw2flag);
	var pwalert = pwtext.innerHTML;		//보안강도 여부 확인하기 위해서 가져오기
	
	if(pwflag == false){
		alert("비밀번호가 너무 짧습니다");
		pwplace.focus();
		e.preventDefault();
		return;
	}else if(pw2flag == false){
		alert("비밀번호와 비밀번호 확인이 일치하지 않습니다");
		pw2place.focus();
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
});
</script>
</body>
</html>