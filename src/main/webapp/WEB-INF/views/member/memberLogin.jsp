<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>memberLogin.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
  	function fCheck(){
  		let email = $("#email").val();
  		let name = $("#name").val();
  		
  		if(email.trim() == ""){
  			alert("이메일을 입력해주세요");
  			$("#email").focus();
  			return false;
  		}
  		if(name.trim() == ""){
  			alert("이름을 입력해주세요");
  			$("#name").focus();
  			return false;
  		}
		$.ajax({
			type:"POST",
			url:"${ctp}/member/memberIdSearch",
			data:{
				email : email,
				name  : name
				},
			success:function(res){
				let str= "";
				if(res != ""){
					str+= "아이디는 <font color='orange'><b>"+res+"</b></font>입니다.";
				}
				else{
					str+= "아이디가 존재하지 않습니다.";
				}
				$("#demo").html(str);
			},
			error:function(){
				alert("전송오류");
			}
		});
  	}
  	$(document).ready(function(){
	  	$('#myModal').on('hidden.bs.modal', function (e) {
	  		// 모달 종료 시,
	  		$("#myform1")[0].reset();// 폼의 전체 값 초기화 처리
	  		$('#demo').html("");
	  	});
  	}); 
  	
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="container" style="padding:30px">
			  <form name="myform" method="post" class="was-validated">
			    <h2>회원 로그인</h2>
			    <p>회원 아이디와 비밀번호를 입력해 주세요</p>
			    <div class="form-group">
			      <label for="mid">회원 아이디 :</label>
			      <input type="text" class="form-control" name="mid" id="mid" value="${fn:trim(mid)}" placeholder="아이디를 입력하세요." required autofocus />
			      <div class="valid-feedback">입력성공!!</div>
			      <div class="invalid-feedback">회원 아이디는 필수 입력사항입니다.</div>
			    </div>
			    <div class="form-group">
			      <label for="pwd">비밀번호 :</label>
			      <input type="password" class="form-control" name="pwd" id="pwd" placeholder="비밀번호를 입력하세요." required />
			      <div class="invalid-feedback">회원 비밀번호는 필수 입력사항입니다.</div>
			    </div>
			    <div class="form-group">
				    <button type="submit" class="btn btn-primary">로그인</button>
				    <button type="reset" class="btn btn-primary">다시입력</button>
				    <button type="button" onclick="location.href='${ctp}/';" class="btn btn-primary">돌아가기</button>
				    <button type="button" onclick="location.href='${ctp}/member/memberJoin';" class="btn btn-primary">회원가입</button>
			    </div>
			    <div class="row" style="font-size:12px">
			      <span class="col"><input type="checkbox" name="idCheck" checked /> 아이디 저장</span>
			      <span class="col">
					[<a href="javascript:void(0);" data-toggle="modal" data-target="#myModal">아이디찾기</a>]			      
			        <%-- [<a href="${ctp}/member/memberIdSearch">아이디찾기</a>] --%> /
			        [<a href="${ctp}/member/memberPwdSearch">비밀번호찾기</a>]
			      </span>
			    </div>
			  </form>
		  </div>
		</div>
	</div>
</div>
<!--  주소록을 Modal로 출력하기 -->
<div class="modal fade" id="myModal" style="width:94%;top: 18%;">
   	<div class="modal-dialog">
		<div class="modal-content" style="width:602px">
			<div class="modal-header" style="width:600px;text-align:center;margin:0 auto;">
				<h4 class="modal-title" style="margin-left: auto;">☆ 아이디 찾기 ☆</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body" style="width:600px;height:350px;overflow:auto;">
				<form name="myform1" id="myform1" method="post" class="was-validated">
					<div class="form-group">
						<label for="email">이메일 :</label>
						<input type="text" class="form-control" name="email" id="email" placeholder="이메일을 입력하세요." required />
						<div class="valid-feedback">입력성공!!</div>
				     	<div class="invalid-feedback">이메일은 필수 입력사항입니다.</div>
			     	</div>
					<div class="form-group">
						<label for="name">성명 :</label>
						<input type="text" class="form-control" name="name" id="name" placeholder="성명을 입력하세요." required />
						<div class="valid-feedback">입력성공!!</div>
				     	<div class="invalid-feedback">이름은 필수 입력사항입니다.</div>
			     	</div>
				</form>
				<hr/>
				<div class="text-center" >
			 		<span id="demo"></span>
		 		</div>
			</div>
			<div class="modal-footer" style="width:600px;justify-content: space-around;">
				<input type="button" value="아이디찾기" onclick="fCheck();" class="btn btn-outline-secondary" />
				<button type="button" class="close btn-danger" data-dismiss="modal">Close</button>					
			</div>
		</div>    	
   	</div>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>