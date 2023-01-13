<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
 <head>
    <meta charset="UTF-8">
    <title>fileList.jsp</title>
    <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
    <style>
		div.gallery {
		  margin: 5px;
		  border: 1px solid #ccc;
		  float: left;
		  width: 175px;
		}
		
		div.gallery:hover {
		  border: 1px solid #777;
		}
		
		div.gallery img {
		  width: 100%;
		  height: 145px;
		}
		
		div.desc {
		  padding: 15px;
		  text-align: center;
		  
		}
		div.desc p{
		 display: -webkit-box;
	     -webkit-line-clamp: 2;
	     /* text-overflow: ellipsis; */
	     overflow: hidden;
	     -webkit-box-orient: vertical;
		}
		footer{
			clear: both;
		}
		.chk_box { display: block; position: relative; padding-left: 25px; margin-bottom: 10px; cursor: pointer; font-size: 14px; -webkit-user-select: none; -moz-user-select: none; -ms-user-select: none; user-select: none; }

		/* 기본 체크박스 숨기기 */
		.chk_box input[type="checkbox"] { display: none; }
		
		/* 선택되지 않은 체크박스 스타일 꾸미기 */
		.on { width: 20px; height: 20px; background: #ddd; position: absolute; top: 0; left: 0; }
		
		/* 선택된 체크박스 스타일 꾸미기 */
		.chk_box input[type="checkbox"]:checked + .on { background: #f86480; }
		.on:after { content: ""; position: absolute; display: none; }
		.chk_box input[type="checkbox"]:checked + .on:after { display: block; }
		.on:after { width: 6px; height: 10px; border: solid #fff; border-width: 0 2px 2px 0; -webkit-transform: rotate(45deg); -ms-transform: rotate(45deg); transform: rotate(45deg); position: absolute; left: 6px; top: 2px; }
	</style>
	<script>
		'use strict'
		// 전체선택
		 $(function(){
	    	$(".checkAll").click(function(){
	    		if($(".checkAll").prop("checked")) {
		    		$(".chk").prop("checked", true);
	    		}
	    		else {
		    		$(".chk").prop("checked", false);
	    		}
	    	});
	    });
		
		// 선택항목 반전
	    $(function(){
	    	$("#reverseAll").click(function(){
	    		$(".chk").prop("checked", function(){
	    			return !$(this).prop("checked");
	    		});
	    	});
	    });
	
	</script>
	
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"/>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"/>
<p><br/></p>
	<div class="container">
    <h2>서버 파일 리스트</h2>
    <hr/>
    <p>서버의 파일 경로 : ${ctp }/admin/ckeditor/~~~파일명</p>
    <hr/>
    <div id="btnView" class="row text-center bg-light p-2">
	  <div class="col custom-control custom-switch">
		  <input type="checkbox" class="custom-control-input checkAll" id="switch1">
		  <label class="custom-control-label btn btn-success btn-sm" for="switch1">전체선택/해제</label>
	  </div>
	  <div class="col"><input type="button" value="선택반전" id="reverseAll" class="btn btn-primary btn-sm"/></div>
	  <div class="col"><input type="button" value="선택삭제" onclick="selectDelCheck()" class="btn btn-danger btn-sm"/></div>
	</div>
	<hr/>
    <c:forEach var="file" items="${files}" varStatus="st">
    	<div class="gallery">
		  <a target="_blank" href="${ctp}/data/ckeditor/${file}">
		    <img src="${ctp}/data/ckeditor/${file}"  width="600" height="400">
		  </a>
		  <div class="desc">
		  	<p style="word-wrap: break-word;">${file}</p>
		  	<label for="agree${st.count}" class="chk_box">
			  	<input type="checkbox" name="chk" id="agree${st.count}" class="chk" value="${file}">
			  	<span class="on"></span>
		  	</label>
		  	<input type="button" value="삭제" onclick="fileDel('${file}')" class="btn btn-danger btn-sm"/>
		  </div>
		</div>
    </c:forEach>
    </div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>