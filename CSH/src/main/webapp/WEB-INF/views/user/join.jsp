<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- header.jsp 추가 -->
<%@ include file="../include/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
</head>
<body>
<form method="post" id="joinform" enctype="multipart/form-data">
	<img id="img" width="100" height="100" border="1"><br/>
	<input type="file" id="image" name="image" accept=".png, .gif, .jpg" /></div><br/><br/>

	<label for="id">아이디</label>
	<input type="text" id="id" placeholder="아이디는 10자 이하" name="id" />
	<button type="button" id="idbtn">중복검사</button>
	<div id="idtext"></div><br/>
	
	<label for="pw">비밀번호</label>
	<input type="password" id="pw" name="pw" placeholder="영문+숫자로 8자 이상" />
	<div id="pwtext"></div><br/>
	
	<label for="pwcheck">비밀번호 확인</label>
	<input type="password" id="pwcheck" placeholder="비밀번호와 같게 입력" />
	<div id="pwchecktext"></div><br/>
	
	<label for="gender">성별</label>
	<input type="radio" id="gender" name="gender" value="man"  checked="checked">남자&nbsp;&nbsp;
	<input type="radio" id="gender" name="gender" value="woman" >여자<br/><br/>
	
	<label for="year">생일</label>
	<select name="year">
		<%for(int y=2020; y>=1900; y--){ %>
       <option value="<%=y %>"><%=y %></option>
       <%} %>
     </select>년&nbsp;
     <select name="month">
		<%for(int m=1; m<=12; m++){ %>
       <option value="<%=m %>"><%=m %></option>
       <%} %>
     </select>월&nbsp;
     <select name="day">
		<%for(int d=1; d<=31; d++){ %>
       <option value="<%=d %>"><%=d %></option>
       <%} %>
     </select>일<br/><br/><br/>
     <input type="hidden" value="common" name="type" readOnly="readOnly" />
     <button>회원가입</button>
</form>

<script>
//유효성 체크 변수
var idflag = false;
var pwflag = false;

//입력창 및 버튼 변수로 받아오기
var image = document.getElementById("image");
var img = document.getElementById("img");
var idplace = document.getElementById("id");
var idbtn = document.getElementById("idbtn");
var idtext = document.getElementById("idtext");
var pwplace = document.getElementById("pw");
var pwtext = document.getElementById("pwtext");
var pwcheckplace = document.getElementById("pwcheck");
var pwchecktext = document.getElementById("pwchecktext");
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

//아이디 중복검사
idbtn.addEventListener('click',function(e){
	var id = idplace.value;		//눌렀을 때의 값 가져오기
	
	if(id.length == 0){				//id를 적지 않고 버튼을 눌렀을 때
		idtext.innerHTML = "id를 먼저 입력해주세요";
		idtext.style.color = "RED";
		return;
	}else if(id.length > 10){	//id가 11자 이상일 때
		idtext.innerHTML = "id는 10자 이하로 입력해주세요";
		idtext.style.color = 'red';
		return;
	}
	
 	//비동기로 디비에서 중복검사
	//순수 자바스크립트
	var request = new XMLHttpRequest();		//요청 객체 생성
	request.open("GET","/user/join/idcheck/"+id);		//요청 주소 생성
	request.send('');										//요청 보내기
	
	//비동기이기 때문에 콜백함수 필요
	request.onreadystatechange = function(){
		//정상응답이 오면
		if(request.readyState == 4){
			if(request.status >= 200 && request.status < 300){
				var obj = request.responseText;		//결과 받기
				var json = JSON.parse(obj);			//json파싱
				
				if(json.result == true){
					idtext.innerHTML = "사용 가능한 아이디입니다";
					idtext.style.color = 'blue';
					idflag = true;
				}else{
					idtext.innerHTML = "사용 불가능한 아이디입니다";
					idtext.style.color = 'red';
					idflag = false;
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

//전체 유효성
joinform.addEventListener('submit',function(e){	
	
	var pwalert = pwtext.innerHTML;		//보안강도 여부 확인하기 위해서 가져오기
	
	//id 유효성을 통과하지 못한 경우
	if(idflag == false){
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
	//보안강도가 낮을 경우 정말 이 비밀번호를 사용할지 다시 선택할지 선택권 주기
	else if(pwalert == "보안강도 낮음"){		//보안강도가 낮다고 적혀있으면
		var result = confirm("비밀번호의 보안강도가 낮습니다. 이대로 진행하시겠습니까?");		//선택권 주기

		if(result){			//이대로 진행한다고 한 경우 회원가입 진행
			alert("비밀번호 보안강도로 인한 책임은 홈페이지에서 지지 않습니다. 회원가입을 진행합니다.");
		}else{				//아닌 경우 다시 비밀번호를 작성하도록
			alert("비밀번호의 강도를 높이시길 원한다면 영문,숫자 혼합으로 8글자 이상으로 작성해주세요");
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