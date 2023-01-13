<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>adminMemberList.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <script>
    'use strict';
    
    function midSearch() {
      let search = myform.search.value;
      if(search.trim() == "") {
    	  alert("검색어를 입력하세요!");
    	  myform.search.focus();
      }
      else {
    	  myform.submit();
      }
    }
    
    function delCheck(idx) {
    	let ans = confirm("탈퇴처리 시키겠습니까?");
    	if(ans) location.href='${ctp}/admin/member/adminMemberDel?pag=${pageVO.pag}&pageSize=${pageVO.pageSize}&part=${pageVO.part}&search=${pageVO.searchString}&idx='+idx;
    }
    
    function searchCheck(e) {
    	let ans = confirm("등급을 수정하시겠습니까?");
    	if(!ans) return false;
    	
    	let items = e.value.split("/");
    	
    	let query = {
    			idx : items[1],
    			level : items[0]
    	}
    	
    	$.ajax({
    		type  : "post",
    		url   : "${ctp}/admin/member/adminMemberLevel",
    		data  : query,
    		success:function(res) {
    			if(res == "1") alert("등급 변경 완료")
    			else alert("등급 변경 실패")
    			location.reload();
    		},
    		error : function() {
    			alert("전송 오류~~");
    		}
    	});
    }
  </script>
</head>
<body>
<p><br/></p>
<div class="container">
<c:if test="${empty pageVO.searchString}">
  <h2 class="text-center">전체 회원 리스트</h2>
</c:if>
<c:if test="${!empty pageVO.searchString}">
	<h2 class="text-center"><font color='blue'><b>${pageVO.searchString}</b></font> 검색 리스트(총<font color='red'>${pageVO.totRecCnt}</font>건)</h2>
</c:if>
  <br/>
  <form name="myform" method="get" >
  	<div class="row mb-2">
  	  <div class="col form-inline">
  	  	<select name="part" id="part" style="margin-right: 5px;height: 35px;">
			<option value="mid"  ${pageVO.part == 'mid'  ? 'selected' : ''}>아이디</option>
			<option value="nickName" ${pageVO.part == 'nickName' ? 'selected' : ''}>닉네임</option>
			<option value="name" ${pageVO.part == 'name' ? 'selected' : ''}>이름</option>
			<option value="userDel" ${pageVO.part == 'userDel' ? 'selected' : ''}>탈퇴신청여부</option>
			<option value="level" ${pageVO.part == 'level' ? 'selected' : ''}>회원등급</option>
		</select>
  	    <input type="text" name="search" class="form-control" autofocus />&nbsp;
  	    <input type="button" value="회원검색" onclick="midSearch();" class="btn btn-secondary" />
  	    <input type="hidden" name="pag" value="1" />
  	    <input type="hidden" name="pageSize" value="${pageVO.pageSize}" />
  	  </div>
  	  <div class="col text-right">
  	  	<button type="button" onclick="location.href='${ctp}/admin/member/adminMemberList?pag=${pageVO.pag}&pageSize=${pageVO.pageSize}&sMid=${sMid}&part=${pageVO.part}';" class="btn btn-secondary">전체검색</button>
  	  </div>
  	</div>
  </form>
  <table class="table table-hover text-center">
    <tr class="table-dark text-dark">
      <th>번호</th>
      <th>아이디</th>
      <th>별명</th>
      <th>성명</th>
      <th>최초가입일</th>
      <th>마지막접속일</th>
      <th>등급</th>
      <th>탈퇴유무</th>
    </tr>
    <c:forEach var="vo" items="${vos}" varStatus="st">
      <tr>
        <td>${vo.idx}</td>
        <td><a href="${ctp}/admin/member/adminMemInfor?part=${pageVO.part}&search=${pageVO.searchString}&pag=${pageVO.pag}&pageSize=${pageVO.pageSize}">${vo.mid}</a></td>
        <td>${vo.nickName}</td>
        <td>${vo.name}<c:if test="${sLevel == 0 && vo.userInfor == '비공개'}"><font color='red'>(비공개)</font></c:if></td>
        <td>${fn:substring(vo.startDate,0,19)}</td>
        <td>${fn:substring(vo.lastDate,0,19)}</td>
        <td>
          <form name="levelForm" method="post">
            <select name="level" onchange="searchCheck(this)">
              <option value="0/${vo.idx}" <c:if test="${vo.level==0}">selected</c:if>>관리자</option>
              <option value="1/${vo.idx}" <c:if test="${vo.level==1}">selected</c:if>>운영자</option>
              <option value="2/${vo.idx}" <c:if test="${vo.level==2}">selected</c:if>>우수회원</option>
              <option value="3/${vo.idx}" <c:if test="${vo.level==3}">selected</c:if>>정회원</option>
              <option value="4/${vo.idx}" <c:if test="${vo.level==4}">selected</c:if>>준회원</option>
            </select>
          </form>
        </td>
        <td>
          <c:if test="${vo.userDel=='ok'}"><a href="javascript:delCheck(${vo.idx})"><font color="red">탈퇴신청</font></a></c:if>
          <c:if test="${vo.userDel!='ok'}">활동중</c:if>
        </td>
      </tr>
    </c:forEach>
    <tr><td colspan="8" class="m-0 p-0"></td></tr>
  </table>
</div>
<br/>
<!-- 블록 페이지 시작 -->
<div class="text-center">
  <ul class="pagination justify-content-center">
    <c:if test="${pageVO.pag > 1}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=1&part=${pageVO.part}&search=${pageVO.searchString}">첫페이지</a></li>
    </c:if>
    <c:if test="${pageVO.curBlock > 0}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${(pageVO.curBlock-1)*pageVO.blockSize + 1}&part=${pageVO.part}&search=${pageVO.searchString}">이전블록</a></li>
    </c:if>
    <c:forEach var="i" begin="${(pageVO.curBlock)*pageVO.blockSize + 1}" end="${(pageVO.curBlock)*pageVO.blockSize + pageVO.blockSize}" varStatus="st">
      <c:if test="${i <= pageVO.totPage && i == pageVO.pag}">
    		<li class="page-item active"><a class="page-link bg-secondary border-secondary" href="${ctp}/admin/member/adminMemberList?pag=${i}&part=${pageVO.part}&search=${pageVO.searchString}">${i}</a></li>
    	</c:if>
      <c:if test="${i <= pageVO.totPage && i != pageVO.pag}">
    		<li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${i}&part=${pageVO.part}&search=${pageVO.searchString}">${i}</a></li>
    	</c:if>
    </c:forEach>
    <c:if test="${pageVO.curBlock < pageVO.lastBlock}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${(pageVO.curBlock+1)*pageVO.blockSize + 1}&part=${pageVO.part}&search=${pageVO.searchString}">다음블록</a></li>
    </c:if>
    <c:if test="${pageVO.pag < pageVO.totPage}">
      <li class="page-item"><a class="page-link text-secondary" href="${ctp}/admin/member/adminMemberList?pag=${pageVO.totPage}&part=${pageVO.part}&search=${pageVO.searchString}">마지막페이지</a></li>
    </c:if>
  </ul>
</div>
<!-- 블록 페이지 끝 -->
<p><br/></p>
</body>
</html>