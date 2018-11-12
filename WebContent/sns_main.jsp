<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sns" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My SNS</title>
<link rel="stylesheet" href="css/styles.css" type="text/css" media="screen" />
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script>
	$(function() {
		$("#accordion").accordion({
			heightstyle : "content",
			active : parseInt("${curmsg == null ? 0:curmsg}")
		});
	});
	
	function newuser() {
		window
				.open(
						"new_user.jsp",
						"newuser",
						"titlebar=no, location=no, scrollbars=no, resizeable=no, menubar=no, toolbar=no, width=300, heught=240");
	}
</script>
</head>
<body>

	<header>
		<div class="container1">
			<h1 class="fontface" id="title">My Simple SNS</h1>
		</div>
	</header>

	<nav>
		<div class="menu">
			<ul>
				<li><a href="#">Home</a></li>
				<li><a href="javascript:newuser()">New User</a></li>
				<li><a href="sns_control.jsp?action=getall">전체글보기</a>
				<li><sns:login /></li>
			</ul>
		</div>
	</nav>
	<div id=wrapper>
		<section id="main">
			<section id="content">
				<b>내소식 업데이트</b>
				<form class="m_form" method="post" action="sns_control.jsp?action=newmsg">
					<input type="hidden" name="uid" value="${uid}">
					<sns:write type="msg" />
					<button class="submit" type="submit">등록</button>
				</form>
				<h3>친구들의 최신 소식</h3>
				<div id="accordion">
					<c:forEach varStatus="mcnt" var="msgs" items="${datas}">
						<c:set var="m" value="${msgs.message}" />
						<h3>[${m.uid}]${m.msg}::[좋아요${m.favcount} | 댓글 ${m.reply}]</h3>
						<div>
							<p></p>
							<p>
								<sns:smenu mid="${m.mid}" auid="${m.uid}" curmsg="${mcnt.index}" />/${m.date}에 작성된 글입니다.
							</p>
							<ul class="reply"><c:forEach var="r" items="${msgs.rlist}">
								<li>${r.uid}::${r.rmsg}-${r.date}<sns:rmenu curmsg="${mcnt.index}" rid="${r.rid}" ruid="${r.uid}" /></li>
							</c:forEach> </ul>
							<form action="sns_control.jsp?action=newreply&cnt=${cnt}"
								method="post">
								<input type="hidden" name="mid" value="${m.mid}"> 
								<input type="hidden" name="uid" value="${uid}">
								<input type="hidden" name="suid" value="${suid}">
								<input type="hidden" name="curmsg" value="${mcnt.index}">
								<sns:write type="rmsg" />
							</form>
						</div>
					</c:forEach>
				</div>
				<div align="center">
					<a href="sns_control.jsp?action=getall&cnt=${cnt+5}&suid=${suid}">더보기&gt;&gt;</a>
				</div>
			</section>
			<aside id="sidebar2">
				<h2>새로운 친구들.!!</h2>
				<c:forEach items="${nusers}" var="n">
				<ul>
				<li><a href="sns_control.jsp?action=getall&suid=${n}">${n}</a></li>
				</ul>
				</c:forEach>
				<br> <br>
				<h3>We're Social Too!!</h3>
				<img src="img/facebook_32.png"> 
				<img src="img/twitter_32.png">
				<img src="img/youtube_32.png">
				<br> <br>
				<h3>Links</h3>
				<ul>
					<li><a href="http://www.naver.com">네이버</a></li>
					<li><a href="http://www.google.com">구글</a></li>
					<li><a href="http://dj.ezenac.co.kr/">이젠컴퓨터학원</a></li>
				</ul>
			</aside>
		</section>
	</div>

	<footer>
		<div class="container1">
			<aside></aside>
		</div>
	</footer>

</body>
</html>