<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>adLeft.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <style>
  	.border1{
	    border-bottom: 2px solid #dfdfdf;
	}
  </style>
</head>
<body style="background-color:#ddd">
<div class="container-fluid text-center" style="margin-top:1em;">
  <div id="accordion">
  
	<div class="card">
	  <div class="card-header">
	  	<a href="${ctp}/" target="_top">홈으로</a>
  	  </div>
 	</div> 	
 	
    <div class="card">
      <div class="card-header">
        <a class="card-link" data-toggle="collapse" href="#collapseOne">
         관리자메뉴
        </a>
      </div>
      <div id="collapseOne" class="collapse" data-parent="#accordion">
        <div class="card-body border1 ">
        	<a href="#">방명록리스트</a>
        </div>
        <div class="card-body">
        	<a href="${ctp}/admin/member/adminMemberList" target="adminContent">회원리스트</a>
        </div>
      </div>
    </div>
    <div class="card">
      <div class="card-header">
        <a class="collapsed card-link" data-toggle="collapse" href="#collapseTwo">
        기타작업
      	</a>
      </div>
      <div id="collapseTwo" class="collapse" data-parent="#accordion">
        <div class="card-body">
        	<a href="${ctp}/admin/file/fileList" target="adminContent">임시파일</a>
        </div>
      </div>
    </div>
  </div>
</div>
<p><br/></p>
</body>
</html>