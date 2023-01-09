<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>memberPwdSearch.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
  	'use strict';
  	
  	function imsiCodeOk(){
  		let mid= $("#mid").val();
  		let email= $("#email").val();
  		$.ajax({
  			type:"post",
  			url:"${ctp}/member/memberPwdSearch",
  			data:{
  				mid : mid,
  				email : email
  				},
  			success:function(res){
	  			let str="";
  				if(res != 0){
  					str+= "<hr/>"
	  				str+= "임시 비밀번호가 발송되었습니다.<br/>";
	  				str+= "비밀 번호를 변경해주세요.";
  					str+= "<hr/>"
  				}
  				else{
  					str+= "<hr/>"
  					str+="해당되는 회원이 없습니다.";
  					str+= "<hr/>"
  				}
	  			$("#demo").html(str);
  			},
  			error:function(){
  				alert("전송오류");
  			}
  		});
  		
  	}
  	
  
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
			    <h2>회원 비밀번호찾기</h2>
			    <p>아이디와 이메일을 입력해 주세요</p>
			    <div class="form-group">
			      <label for="mid">회원 아이디 :</label>
			      <input type="text" class="form-control" name="mid" id="mid" value="${mid}" placeholder="아이디를 입력하세요." required autofocus />
			      <div class="valid-feedback">입력성공!!</div>
			      <div class="invalid-feedback">회원 아이디는 필수 입력사항입니다.</div>
			    </div>
			    <div class="form-group">
			      <label for="email">이메일 :</label>
			      <input type="text" class="form-control" name="email" id="email" placeholder="이메일을 입력하세요." required />
			      <div class="valid-feedback">입력성공!!</div>
			      <div class="invalid-feedback">회원 이메일은 필수 입력사항입니다.</div>
			    </div>
			  	<div>
		      	    <span id="demo"></span>
		     	 </div>
			    <div class="form-group">
				    <button type="button" onclick="imsiCodeOk()" class="btn btn-primary">임시 비밀번호 이메일전송</button>
				    <button type="reset" class="btn btn-primary">다시입력</button>
			    </div>
			  </form>
		   	  <div>
			      <button type="button" onclick="location.href='${ctp}/member/memberLogin';" class="btn btn-primary">로그인</button>
			      <button type="button" onclick="location.href='${ctp}/member/memberJoin';" class="btn btn-primary">회원가입</button>
			  </div>
		  </div>
		</div>
	</div>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>