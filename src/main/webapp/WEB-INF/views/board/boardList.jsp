<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>boList.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
  	'use strict';
  	function pageCheck() {
    	let pageSize = document.getElementById("pageSize").value;
    	location.href = "${ctp}/board/boardList?pageSize="+pageSize+"&pag=1&part=${pageVO.part}&searchString=${pageVO.searchString}";
    }
  	
  	function searchCheck(){
  		let searchString = $("#searchString").val();
  		
  		if(searchString.trim()==""){
  			alert("찾고자 하는 검색어를 입력하세요!");
  			searchForm.searchString.focus();
  		}
  		else{
  			searchForm.submit();
  		}
  	}
  </script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/include/nav.jsp"/>
	<jsp:include page="/WEB-INF/views/include/slide2.jsp"/>
	<p><br/></p>
	<div id="wrap">
		<div class="container">
		<c:if test="${empty pageVO.searchString }">
  			<h2 class="text-center"><b>게 시 판 리 스 트</b></h2>
  		</c:if>
		<c:if test="${!empty pageVO.searchString }">
  			<h2 class="text-center"><b><font color="blue">${pageVO.searchString}</font> 검 색 리 스 트 (${pageVO.totRecCnt }건)</b></h2>
  		</c:if>
  		<br/>
  		<table class="table table-borderless">
  			<tr>
  				<td class="text-left p-0">
  					<c:if test="${sLevel != 4}"><a href="${ctp}/board/boardInput?pag=${pageVO.pag}&pageSize=${pageVO.pageSize}&part=${pageVO.part}&searchString=${pageVO.searchString}" class="btn btn-secondary btn-sm">글쓰기</a></c:if>
  				</td>
  				<td class="text-right p-0">
  					<select name="pageSize" id="pageSize" onchange="pageCheck()">
  						<option value="5"  ${pageVO.pageSize==5  ? 'selected' : ''}>5건</option>
  						<option value="10" ${pageVO.pageSize==10 ? 'selected' : ''}>10건</option>
  						<option value="15" ${pageVO.pageSize==15 ? 'selected' : ''}>15건</option>
  						<option value="20" ${pageVO.pageSize==20 ? 'selected' : ''}>20건</option>
  					</select>
  				</td>
  			</tr>
  		</table>
  		<table class="table table-hover text-center">
  			<tr class="table-dark text-dark">
  				<th>글번호</th>
  				<th>글제목</th>
  				<th>글쓴이</th>
  				<th>글쓴날짜</th>
  				<th>조회수</th>
  				<th>좋아요</th>
  			</tr>
			<c:set var="curScrStartNo" value="${pageVO.curScrStartNo}"/> <%-- 넣는게 정석이고 좋지만 생략가능 --%>
  			<c:forEach var="vo" items="${vos}">
  				<tr>
  					<td>${curScrStartNo}</td>
  					<td class="text-left">
  						<a href="${ctp}/board/boardContent?partIdx=${vo.idx}&pag=${pageVO.pag}&pageSize=${pageVO.pageSize}&sMid=${sMid}&part=${pageVO.part}&searchString=${pageVO.searchString}">${vo.title}</a>
  						<c:if test="${vo.replyCnt != 0}"><b>(${vo.replyCnt})</b></c:if>
  						<c:if test="${vo.hour_diff <= 24}"><img src="${ctp}/images/new.gif"></c:if>
  					</td>
  					<td>${vo.nickName }</td>
  					<%-- <td>${fn:substring(vo.WDate,0,10)}(${vo.day_diff})</td> --%>
  					
  					<!-- 오늘날짜는 시간으로 나오고 오늘이 지나면 (년-월-일) 로 출력된다.  -->
  					<%-- <td>${vo.day_diff > 0 ? fn:substring(vo.WDate,0,10) :fn:substring(vo.WDate,11,19) }</td> --%>
  					
  					<!-- 24시간 이지나면 (년-월-일) 로 출력되고 그전이면 시간으로 출력된다.  -->
  					<td>${vo.hour_diff > 24 ? fn:substring(vo.WDate,0,10) : fn:substring(vo.WDate,11,19)}</td>
  					
  					<td>${vo.readNum }</td>
  					<td>${vo.good }</td>
  				</tr>
  				<c:set var="curScrStartNo" value="${curScrStartNo-1}"/>
  			</c:forEach>
  			<tr><td colspan="6" class="m-0 p-0"></td></tr>
  		</table> 
  		<div class="text-center">
				<ul class="pagination justify-content-center">
					<c:if test="${pageVO.pag > 1}">
						<li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=1&part=${pageVO.part}&searchString=${pageVO.searchString}">첫페이지</a></li>
					</c:if>
					<c:if test="${pageVO.curBlock > 0}">
						<li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=${(pageVO.curBlock-1)*pageVO.blockSize + 3}&part=${pageVO.part}&searchString=${pageVO.searchString}">이전페이지</a></li>
					</c:if>
					
					<c:forEach var="i" begin="${(pageVO.curBlock*pageVO.blockSize) + 1}" end="${(pageVO.curBlock*pageVO.blockSize) +pageVO.blockSize}" varStatus="st">
						<c:if test="${i <= pageVO.totPage && i == pageVO.pag}">
							<li class="page-item active"><a class="page-link bg-secondary border-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=${i}&part=${pageVO.part}&searchString=${pageVO.searchString}">${i}</a></li>
						</c:if>
						<c:if test="${i <= pageVO.totPage && i != pageVO.pag}">
							<li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=${i}&part=${pageVO.part}&searchString=${pageVO.searchString}">${i}</a></li>
						</c:if>
					</c:forEach> 
					<c:if test="${pageVO.curBlock < pageVO.lastBlock}">
						<li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=${(pageVO.curBlock+1)*pageVO.blockSize + 1}&part=${pageVO.part}&searchString=${pageVO.searchString}">다음페이지</a></li>
					</c:if>
					<c:if test="${pageVO.pag < pageVO.totPage}">
						<li class="page-item"><a class="page-link text-secondary" href="${ctp}/board/boardList?pageSize=${pageVO.pageSize}&pag=${pageVO.totPage}&part=${pageVO.part}&searchString=${pageVO.searchString}">마지막페이지</a></li>
					</c:if>
				</ul>
			</div>
			<!--  블록 페이지 끝 -->
			<br/>
			<!-- 검색기 처리 시작 -->
			<div class="container text-center">
				<form name="searchForm" method="get">
					<select name="part">
						<option value="title"  <c:if test="${pageVO.part == 'title'}">selected</c:if>>글제목</option>
						<option value="nickName" <c:if test="${pageVO.part == 'nickName'}">selected</c:if>>글쓴이</option>
						<option value="content" <c:if test="${pageVO.part == 'content'}">selected</c:if>>글내용</option>
					</select>						
					<input type="text" name="searchString" id="searchString"/>
					<input type="button" value="검색" onclick="searchCheck()" class="btn btn-secondary btn-sm"/>
					<input type="hidden" name="pag" value="${pageVO.pag}"/>
					<input type="hidden" name="pageSize" value="${pageVO.pageSize}"/>
				</form>
			</div>
		</div>
	</div>
	<p><br/></p>
	<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>