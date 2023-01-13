<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
 <head>
    <meta charset="UTF-8">
    <title>title</title>
    <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
    <style>
    	ul{list-style:none;}
		   
    </style>
    <script>
    	'use strict'
    	function memberDelete() {
    		let pwd = $("#pwd").val();
    		if(pwd.trim()==""){
    			alert("비밀번호를 입력하세요");
    			$("#pwd").focus();
    			return false;
    		}
    		if(confirm("회원을 탈퇴하시겠습니까?")){
    			myform.submit();
    		}
    		else{
    			alert("취소 하셨습니다.");
    			return false;
    		}
    		
    	}
    </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"/>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"/>
<p><br/></p>
	<div class="container">
    	<h2>회원 탈퇴</h2>
    	<hr/>
    	<form name="myform" method="post" class="was-validated">
    		<div class="form-group">
    		<ul>
	    		<li>
	    			<label for="pwd" >비밀번호 :</label>
	    			<input type="password" class="form-control" name="pwd" id="pwd" placeholder="비밀번호를 입력해주세요." required autofocus/>
	    		</li>
	    		<li>
		    		<br/>
	   				<input type="button" onclick="memberDelete()" value="회원탈퇴" class="btn btn-danger"/>
	   			</li>
    		</ul>
    		</div>
    		<input type="hidden" name="mid" value="${sMid}"/>
    	</form>
    </div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>