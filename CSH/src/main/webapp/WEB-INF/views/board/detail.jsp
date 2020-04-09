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
  width: 98%;
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
/* 메인으로 버튼 */
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
/* 댓글 입력하는 input */
#replyContent{
	width:30%;
}
/* 댓글 작성완료 버튼 */
.replyBtn{
	background-color: #B0C4DE;
	color: white;
	padding: 10px;
	border: none;
	cursor: pointer;
	width: 8%;
}
td {
  text-align: center;
  padding:16px;
}
img{
	width:20px;
	heigth:20px;
}
.rebtn{
	width:25%;
	height:8%;
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
    <input type="text" value="${detail.title}" name="title" id="title" readonly="readonly"><br>

    <label for="writer"><b>작성자</b></label>
    <input type="text" value="${detail.writer}" name="writer" readonly="readonly"><br>

    <label for="regdate"><b>작성일</b></label>
    <input type="text" value="${detail.regdate}" name="regdate" readonly="readonly"><br>
    <hr>
	<textarea name="content" id="content" readonly>${detail.content}</textarea>
  </div>
</form><br><br>


<!-- 댓글 부분 -->
<div class="btndiv">
	<div id="replyHead"></div>			<!-- 댓글 개수 표시 -->
		<input type="text" id="replyContent" name="replyContent" placeholder="댓글 내용을 입력하세요" />&nbsp;&nbsp;
		<button type="button" class="replyBtn" onClick="replyInsert()">작성완료</button>
		<input type="checkbox" id="secretbtn"> 비밀댓글로 설정
	<div id="replyDiv" align="center"></div>
</div>

<div class="btndiv">
	<button type="button" onClick="location.href='/'">메인으로</button>
	<c:if test="${userNickname == detail.writer}">
	<button type="button" onClick="location.href='/board/update/'+${detail.bno}">수정하기</button>
	<button type="button" onClick="del('${detail.bno}')">삭제하기</button>
	</c:if>
</div>

<script>
//댓글 목록
//문서가 시작되자마자 비동기로 불러오기
window.addEventListener('load',function(){
	//재활용을 위해 함수로 만든다
	replyList();		//댓글 목록
	replyCount();		//댓글 개수
});

//댓글 목록을 비동기로 가져오는 함수
function replyList(){
	var bno = ${detail.bno};		//글번호
	
	$.ajax({
		url:"/reply/list/"+bno,
		success:function(result){
			var list = result.replyList;
			var replyDiv = document.getElementById("replyDiv");		//댓글 출력할 부분
			var len = list.length;
			
			var user = "${userNickname}";
			var boardWriter = "${detail.writer}";
			
			console.log("현재 접속한 유저 "+user);
			console.log("게시글 작성자 "+boardWriter);
			
			console.log(len);
			
			var output = '<table class="replyTable">';
			for(var i=0;i<len;i++){
				output += '<tr>';
				output += '<td width="5%">'+list[i].nickname+'</td>';
				//secret이 1인 댓글만
				if(list[i].secret == 1){
					//접속한 유저가 댓글을 단 유저거나 글을 작성한 유저면 content가 고대로 보이기
					if(user == list[i].nickname || user == boardWriter){
						console.log("조건문 통과");
						output += '<td width="20%">';
						output += '<img src="/resources/images/unlock.png" />';
						output +='<input type="text" value="'+list[i].content+'" id="t'+list[i].rno+'" readOnly /></td>';
					}
					//아니면 비밀댓글이라고 보이게
					else{
						output += '<td width="20%">';
						output += '<img src="/resources/images/lock.png" />';
						output += '<input type="text" value="비밀 댓글 입니다" id="t'+list[i].rno+'" readOnly /></td>';
					}
				}
				//secret이 1이 아니면 그냥 content 보이게
				else{
					output += '<td width="20%"><input type="text" value="'+list[i].content+'" id="t'+list[i].rno+'" readOnly /></td>';
				}
				output += '<td width="10%">'+list[i].regdate+'</td>';
				output += '<td width="10%"><img src="/resources/images/goodbtn.png" onClick="goodPress('+list[i].rno+')" />&nbsp;'+list[i].good;
				output += '/&nbsp;<img src="/resources/images/badbtn.png" onClick="badPress('+list[i].rno+')" />&nbsp;'+list[i].bad+'</td>';
				//접속한 유저가 댓글을 단 유저면 수정 삭제 버튼 보이게
				if(user == list[i].nickname){
					console.log("두번째 조건문 통과");
					output += '<td width="20%"><button class="rebtn" onClick="replyEdit('+list[i].rno+')" id="edit'+list[i].rno+'" >수정</button>&nbsp;&nbsp;';
					output += '<button class="rebtn" onClick="replyDel('+list[i].rno+')" id="del'+list[i].rno+'">삭제</button>';
					output += '<button class="rebtn" onClick="replyEditExec('+list[i].rno+')" id="editExec'+list[i].rno+'" style="display:none" >수정완료</button>&nbsp;&nbsp;';
					output += '<button class="rebtn" onClick="replyCancel('+list[i].rno+')" id="cancel'+list[i].rno+'" style="display:none">취소</button></td>';
				}
				output += '</tr>';
			}
			output += '</table>';
			
			replyDiv.innerHTML = output;
		},
		error:function(e){
			console.log(e.responseText);
		}
	});
};
</script>

<script>
//댓글 개수 가져오기
function replyCount(){
	var bno = ${detail.bno};
	
	$.ajax({
		url:"/reply/count/"+bno,
		success:function(result){
			//id가 replyHead인 div에 출력
			var replyHead = document.getElementById("replyHead");
			replyHead.innerHTML = "<b>댓글 목록 ("+result.replyCount+")</b>";
		},
		error:function(e){
			console.log(e.responseText);
		}
	});
}
</script>

<script>
//댓글 작성
//비동기로 댓글 입력 후 댓글 목록 reload
function replyInsert(){
	var bno = ${detail.bno};		//글번호
	var contentplace = document.getElementById("replyContent");
	var content = contentplace.value;
	var check = document.getElementById("secretbtn");
	var secret;
	
	if(check.checked){
		secret = 1;
	}else{
		secret = 0;	
	}
	
	var param = {
			"bno":bno,
			"content":content,
			"secret":secret
	};
	
	$.ajax({
		url:"/reply/insert",
		type:"post",
		data:param,
		success:function(result){
			//console.log("replyInsert()의 result ",result.replyInsert);
			//댓글 입력창 리셋
			contentplace.value = "";
			//댓글 목록 불러오는 함수 
			replyList();
			//댓글 개수 불러오는 함수
			replyCount();
		},
		error:function(e){
			console.log(e);
		}
	}); 
}
</script>

<script>
//댓글 수정 시작 함수
//버튼 바뀌는 용도
function replyEdit(rno){
	//버튼을 클릭하면 수정 완료, 취소 버튼이 보이고 수정, 삭제버튼은 안보이게
	var editExec = document.getElementById("editExec"+rno);		//수정완료
	var cancelbtn = document.getElementById("cancel"+rno);		//취소
	var editbtn = document.getElementById("edit"+rno);				//수정
	var delbtn = document.getElementById("del"+rno);				//삭제
	
	//visibility:hidden는 버튼 공간은 남아있지만 버튼이 안보이는 것
	//display:none은 버튼도 안보이고 공간 조차도 없음
	editExec.style.display = 'inline';					//보이게
	cancelbtn.style.display = 'inline';					//보이게
	editbtn.style.display = 'none';						//안보이게
	delbtn.style.display = 'none';						//안보이게
	
	//해당 댓글의 input readonly 해제 및 포커스
	var t_input = document.getElementById("t"+rno);
	t_input.readOnly = false;
	t_input.focus();
}

//댓글 수정 요청 보내는 함수
function replyEditExec(rno){
	//변경된 댓글 내용을 비동기로 전송해서 수정
	var t_input = document.getElementById("t"+rno);
	var t_val = t_input.value;
	var param = {
			"rno":rno,
			"content":t_val
	};
	
	$.ajax({
		url:"/reply/update",
		type:"post",
		data:param,
		success:function(result){
			//console.log(result.replyUpdate);
			replyList();
			replyCount();
		},
		error:function(e){
			console.log(e.responseText);
		}
	});
}
</script>

<script>
//댓글 삭제 함수
function replyDel(rno){
	var ajax = new XMLHttpRequest();
	ajax.open("get","/reply/delete/"+rno);
	ajax.send();
	
	ajax.onreadystatechange = function(){
		if(ajax.readyState == 4){
			if(ajax.status>=200 && ajax.status<300){
				var obj = ajax.responseText;
				var json = JSON.parse(obj);
				
				if(json.replyDelete == true){
					alert("삭제 성공!");
					replyList();
					replyCount();
				}else{
					alert("삭제 실패ㅠㅠ");
				}
			}
		}
	}
}
</script>

<script>
//좋아요 함수
function goodPress(rno){
	var param = {
			"rno":rno
	}
	
	//비동기로 좋아요 눌렀는지 확인하고 진행 (트랜잭션)
	$.ajax({
		url:"/reply/good",
		data:param,
		success:function(result){
			//console.log(result.replyGood)
			if(result.replyGood == "success"){
				alert("댓글에 좋아요가 성공적으로 등록되었습니다");
			}else if(result.replyGood == "cancel"){
				alert("좋아요가 취소되었습니다");
			}else if(result.replyGood == "alreadyBad"){
				alert("이미 해당 댓글에 싫어요를 누르셨습니다");
			}
			//댓글 목록 reload
			replyList();
		},
		error:function(e){
			console.log(e.responseText);
		}
	});
}

//싫어요 함수
function badPress(rno){
	var param = {
			"rno":rno
	}
	
	//비동기로 싫어요 눌렀는지 확인하고 진행 (트랜잭션)
	$.ajax({
		url:"/reply/bad",
		data:param,
		success:function(result){
			//console.log(result.replyBad)
			if(result.replyBad == "success"){
				alert("댓글에 싫어요가 성공적으로 등록되었습니다");
			}else if(result.replyBad == "cancel"){
				alert("싫어요가 취소되었습니다");
			}else if(result.replyBad == "alreadyGood"){
				alert("이미 해당 댓글에 좋아요를 누르셨습니다");
			}
			//댓글 목록 reload
			replyList();
		},
		error:function(e){
			console.log(e.responseText);
		}
	});
}
</script>
</body>
</html>