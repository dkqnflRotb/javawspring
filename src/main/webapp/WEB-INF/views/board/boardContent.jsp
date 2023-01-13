<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>boContent.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <style>
	th {
		text-align: center !important;
		background-color : #eee;
	}
	#nReply{
		cursor: pointer;
		padding: 5px;
	}
	#nReply:hover{
		background-color : #ff5722;
    	border-radius: 16px;
	}
  </style>
  <script>
    'use strict';
    
    // 전체 댓글(보이기/가리기)
    $(document).ready(function(){
    	$("#reply").show();
    	$("#replyViewBtn").hide();
    	
    	$("#replyHiddenBtn").click(function(){
    		$("#reply").slideUp(500);
    		$("#replyViewBtn").show();
    		$("#replyHiddenBtn").hide();
    	});
    	
    	$("#replyViewBtn").click(function(){
    		$("#reply").slideDown(500);
    		$("#replyViewBtn").hide();
    		$("#replyHiddenBtn").show();
    	});
    	
    });
    
    $(document).ready(function(){
    	$(".mReply").show();
    	$(".cReply1").hide();
    	
    	$(".nReply").click(function(){
    		$(".cReply1").slideDown(500);
    		
    	});
    });
    
    function goodCheck(su) {
    	let sMid = $("#sMid").val();
    	
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/board/boardGoodCheck",
    		data  : {
    			partIdx : ${vo.idx},
    			su  :  su,
    			sMid : sMid
    		},
    		success:function(res) {
    			if(res == 1) {
    				alert("좋아요 버튼을 클릭하셨습니다.");
    				location.reload();
    			}
    			else {
    				alert("좋아요 버튼을 해제 하셨습니다.");
    				location.reload();
    			}
    		},
    		error : function() {
    			alert("전송 오류~~");
    		}
    	});
    }
    
    function goodCheckPlus() {
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/boGoodPlusMinus.bo",
    		data  : {
    			idx : ${vo.idx},
    			goodCnt : 1
    		},
    		success:function() {
    			location.reload();
    		}
    	});
    }
    
    function goodCheckMinus() {
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/boGoodPlusMinus.bo",
    		data  : {
    			idx : ${vo.idx},
    			goodCnt : -1
    		},
    		success:function() {
    			location.reload();
    		}
    	});
    }
    
    // 게시글 삭제처리
    function boDelCheck() {
    	let ans = confirm("현 게시글을 삭제하시겠습니까?");
    	if(ans) location.href = "boardDeleteOk?idx=${vo.idx}&pag=${pag}&pageSize=${pageSize}";
    }
    
    // 댓글 달기
    function replyCheck() {
    	let content = $("#content").val();
    	if(content.trim() == "") {
    		alert("댓글을 입력하세요"); 
    		$("#content").focus();
    		return false;
    	}
    	let query = {
    			boardIdx  : ${vo.idx},
    			mid		  : '${sMid}',
    			nickName  : '${sNickName}',
    			content   : content,
    			hostIp    : '${pageContext.request.remoteAddr}'
    	}
    	
    	$.ajax({
    		type : "POST",
    		url  : "${ctp}/board/boardReplyInput",
    		data : query,
    		success:function(res) {
    			if(res == "1") {
    				alert("댓글이 입력되었습니다.");
    				location.reload();
    			}
    			else {
    				alert("댓글 입력 실패~~~");
    			}
    		},
  			error  : function() {
  				alert("전송 오류!!");
  			}
    	});
    }
    
    // 댓글 삭제하기
    function replyDelCheck(idx) {
    	let ans = confirm("현재 댓글을 삭제하시겠습니까?");
    	if(!ans) return false;
    	
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/board/boardReplyDeleteOk",
    		data  : {idx : idx},
    		success:function(res) {
    			if(res == "1") {
    				alert("댓글이 삭제되었습니다.");
    				location.reload();
    			}
    			else {
    				alert("댓글이 삭제 실패~~");
    			}
    		},
    		error  : function() {
    			alert("전송 오류~~");
    		}
    	});
    }
    
    // 답변글(부모댓글의 댓글-대댓글)
    function insertReply(idx, level, levelOrder, nickName, dcheck) {
    	let insReply = '';
    	insReply += '<div class= "container">';
    	insReply += '<table class="m-2 p-0" style="width:100%">';
    	insReply += '<tr>';
    	insReply += '<td class="p-0 text-left">';
    	insReply += '<div>';
    	insReply += '답변 댓글 달기 : &nbsp;';
    	insReply += '<input type="text" name="nickName" value="${sNickName}" size="6" readonly class="p-0" />';
    	insReply += '</div>';
    	insReply += '</td>';
    	insReply += '</tr>';
    	insReply += '<tr>';
    	insReply += '<td colspan="2" class="text-center p-0">';
    	insReply += '<textarea rows="3" class="form-control p-0" name="content" id="content'+idx+'"/>';
    	insReply += '@'+nickName+'\n';
    	insReply += '</textarea>';
    	insReply += '</td>';
    	insReply += '<td style="width: 177px;border-top: hidden; padding: 2em;">';
    	insReply += '<input type="button" value="답글달기" onclick="replyCheck2('+idx+','+level+','+levelOrder+','+dcheck+')"/>';
    	insReply += '</td>';
    	insReply += '</tr>';
    	insReply += '</table>';
    	insReply += '</div>';
    	
    	$("#replyBox"+idx).html(insReply);
    	$("#replyBoxOpenBtn"+idx).hide();
    	$("#replyBoxCloseBtn"+idx).show();
    	$("#replyBox"+idx).slideDown(500);
    }
    
    // 댓글 닫기 버튼
    function closeReply(idx){
    	$("#replyBox"+idx).html()
    	$("#replyBoxOpenBtn"+idx).show();
    	$("#replyBoxCloseBtn"+idx).hide();
    	$("#replyBox"+idx).slideUp(500);
    }
    
    function replyCheck2(idx, level, levelOrder,dcheck){
    	let boardIdx = "${vo.idx}";
    	let mid = "${sMid}";
    	let nickName = "${sNickName}";
    	/* let content = "#content"+idx;
    	let contentVal = $(content).val(); */
    	let content = $("#content"+idx).val();
    	
    	let hostIp = "${pageContext.request.remoteAddr}";
    	alert(content);
    	if(content == ""){
    		alert("답변글(대댓글)을 입력하세요!");
    		$("#content"+idx).focus();
    		return false;
    	}
    	
    	let query = {
    			boardIdx : boardIdx,
    			mid		 : mid,
    			nickName : nickName,
    			content  : content,
    			hostIp   : hostIp,
    			level    : level,
    			levelOrder:levelOrder,
    			dcheck   : dcheck
    	}
    	
    	$.ajax({
    		type : "post",
    		url  : "${ctp}/board/boardReplyInput2",
    		data : query,
    		success:function(res){
    			alert("res = "+ res);
    			location.reload();
    		},
    		error : function(){
    			alert("전송오류!!");
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
  <h2 class="text-center">글 내 용 보 기</h2>
  <br/>
  <table class="table table-borderless">
    <tr>
      <td class="text-right">hostIp : ${vo.hostIp}</td>
    </tr>
  </table>
  <table class="table table-bordered">
    <tr>
      <th style="min-width:12em">글쓴이.${vo.idx}</th>
      <td>${vo.nickName}</td>
      <th>글쓴날짜</th>
      <td>${fn:substring(vo.WDate,0,fn:length(vo.WDate)-2)}</td>
    </tr>
    <tr>
      <th>글제목</th>
      <td colspan="3">${vo.title}</td>
    </tr>
    <tr>
      <th>전자메일</th>
      <td>${vo.email}</td>
      <th>조회수</th>
      <td>${vo.readNum}</td>
    </tr>
    <tr>
      <th>홈페이지</th>
      <td>${vo.homePage}</td>
      <th>좋아요</th>
      <td>
      	<c:if test="${goodCheck != 0}">
      		<a href="javascript:goodCheck(0)"><font color="red">❤</font></a>
        </c:if>
        <c:if test="${goodCheck == 0}">
        	<a href="javascript:goodCheck(1)">❤</a>
        </c:if>
          ${vo.good} ,
          <a href="javascript:goodCheckPlus()">👍</a>
          <a href="javascript:goodCheckMinus()">👎</a>
      </td>
    </tr>
    <tr>
      <th>글내용</th>
      <td colspan="3" style="height:220px">${fn:replace(vo.content, newLine, "<br/>")}</td>
    </tr>
    <tr>
      <td colspan="4" class="text-center">
        <c:if test="${flag == 'search'}"><input type="button" value="돌아가기" onclick="location.href='${ctp}/board/boardSearch.bo?search=${search}&searchString=${searchString}&pageSize=${pageSize}&pag=${pag}';" class="btn btn-secondary"/></c:if>
        <c:if test="${flag != 'search'}">
          <input type="button" value="돌아가기" onclick="location.href='${ctp}/board/boardList.bo?pageSize=${pageSize}&pag=${pag}';" class="btn btn-secondary"/>
	        <c:if test="${sMid == vo.mid || sLevel == 0}">
		        <input type="button" value="수정하기" onclick="location.href='${ctp}/board/boardUpdate?idx=${vo.idx}&pageSize=${pageSize}&pag=${pag}';" class="btn btn-success"/>
		        <input type="button" value="삭제하기" onclick="boDelCheck()" class="btn btn-danger"/>
	        </c:if>
        </c:if>
      </td>
    </tr>
  </table>
  
  <c:if test="${flag != 'search'}">
	  <!-- 이전글/다음글 처리 -->
	  <table class="table table-borderless">
	    <tr>
	      <td>
	      	<%-- <c:if test="${preVo.preIdx != 0}">
	           👈 <a href="${ctp}/board/boardContent?idx=${preVo.preIdx}&pageSize=${pageSize}&pag=${pag}">이전글 : ${preVo.preTitle}</a><br/>
	        </c:if>
	        <c:if test="${nextVo.nextIdx != 0}">
	           👉 <a href="${ctp}/board/boardContent?idx=${nextVo.nextIdx}&pageSize=${pageSize}&pag=${pag}">다음글 : ${nextVo.nextTitle}</a>
	        </c:if> --%>
	         
	        <c:if test="${!empty pnVos[1]}">
	          다음글 : <a href="${ctp}/board/boardContent?partIdx=${pnVos[1].idx}&pageSize=${pageSize}&pag=${pag}&sMid=${sMid}">${pnVos[1].title}</a><br/>
	        </c:if>
	      </td>
	      <td style="text-align: right;">
	        <!-- 아래는 이전글 처리때문에 추가된 루틴.... -->
	        <c:if test="${vo.idx < pnVos[0].idx}">
	          다음글 : <a href="${ctp}/board/boardContent?partIdx=${pnVos[0].idx}&pageSize=${pageSize}&pag=${pag}&sMid=${sMid}">${pnVos[0].title}</a><br/>
	        </c:if>
	        <c:if test="${vo.idx > pnVos[0].idx}">
	          이전글 : <a href="${ctp}/board/boardContent?partIdx=${pnVos[0].idx}&pageSize=${pageSize}&pag=${pag}&sMid=${sMid}">${pnVos[0].title}</a><br/>
	        </c:if>
	        
	      </td>
	    </tr>
	  </table>
  </c:if>
</div>
<br/>
<!-- 댓글(대댓글) 처리  -->
<!-- 댓글 리스트보여주기 -->
<div class="text-center mb-3">
	<input type="button" value="댓글보이기" id="replyViewBtn" class="btn btn-secondary"/>
	<input type="button" value="댓글가리기" id="replyHiddenBtn" class="btn btn-info"/>
</div>
<div class="container" id="reply">
	<table class="table table-hover text-left">
	  <tr style="background-color:#eee">
	    <th>작성자</th>
	    <th>댓글내용</th>
	    <th class="text-center">작성일자</th>
	    <th class="text-center">접속IP</th>
	    <th class="text-center">답글</th>
	  </tr>
	  <c:set var="i" value="0"/>
	  <c:forEach var="replyVo" items="${replyVos}" varStatus="st">
	    <tr <c:if test="${replyVo.level <= 0}"> class="mReply" style="background: blanchedalmond;"</c:if><c:if test="${replyVo.level > 0}">class="cReply${replyVo.dcheck}"</c:if>>
	      <td class="text-left" style="padding-left:${replyVo.level}em;">
	        <c:if test="${sMid == replyVo.mid || sLevel == 0}">
	          (<a href="javascript:replyDelCheck(${replyVo.idx})" title="삭제하기">x</a>)
	        </c:if>
	      	<c:if test="${replyVo.level <=0}">${replyVo.nickName}
		      		<font id="nReply" class="nReply" color="blue" size="0.5px">▼답글 : ${dchecks[i].dcheckSu -1}</font>
		      		<c:set var="i" value="${i+1}" />
	      	</c:if> <!-- 부모댓글의 경우 들여쓰기 하지 않는다. -->  
	      	
	      	
	      	<c:if test="${replyVo.level >0}">
	      		└▶ ${replyVo.nickName}
	      	</c:if>
	         
	      </td>
	      <td style="padding-left:${replyVo.level}em;">
	        ${fn:replace(replyVo.content, newLine, "<br/>")}
	      </td>
	      <td class="text-center">${replyVo.WDate}</td>
	      <td class="text-center">${replyVo.hostIp}</td>
	      <td class="text-center">
	      	<input type="button" value="답글" onclick="insertReply('${replyVo.idx}','${replyVo.level}','${replyVo.levelOrder}','${replyVo.nickName}',${replyVo.dcheck})" id="replyBoxOpenBtn${replyVo.idx}" class="btn btn-sm btn-info" />
	      	<input type="button" value="닫기" onclick="closeReply('${replyVo.idx}')" id="replyBoxCloseBtn${replyVo.idx}" class="btn btn-sm btn-warning" style="display:none;" />
	      </td>
	    </tr>
	    <tr>
	    	<td colspan="5" class="m-0 p-0" style="border-top:none;"><div id="replyBox${replyVo.idx }"></div></td>
	  </c:forEach>
	</table>
	<!-- 댓글 입력창 -->
	<%-- <form name="replyForm" method="post" action="${ctp}/boReplyInput.bo"> --%>
	<form name="replyForm">
	  <table class="table text-center">
	    <tr>
	      <td style="width:85%" class="text-left">
	        글내용 :
	        <textarea rows="4" name="content" id="content" class="form-control"></textarea>
	      </td>
	      <td style="width:15%">
	        <br/>
	        <p>작성자 : ${sNickName}</p>
	        <p>
	          <input type="button" value="댓글달기" onclick="replyCheck()" class="btn btn-info btn-sm"/>
	        </p>
	      </td>
	    </tr>
	  </table>
	  <%-- 
	  <input type="hidden" name="boardIdx" value="${vo.idx}"/>
	  <input type="hidden" name="hostIp" value="${pageContext.request.remoteAddr}"/>
	  <input type="hidden" name="mid" value="${sMid}"/>
	   --%>
	  <input type="hidden" name="sMid" id="sMid" value="${sMid}"/>
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>