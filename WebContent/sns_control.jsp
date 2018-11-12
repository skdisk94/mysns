<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="mysns.sns.*,mysns.member.*,java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% request.setCharacterEncoding("UTF-8"); %>

<jsp:useBean id="msg" class="mysns.sns.Message"/>
<jsp:useBean id="msgdao" class="mysns.sns.MessageDAO"/>
<jsp:useBean id="reply" class="mysns.sns.Reply"/>"

<jsp:setProperty name="msg" property="*"/>
<jsp:setProperty name ="reply" property="*"/>

<%
	String action = request.getParameter("action");
	String cnt = request.getParameter("cnt");
	String suid = request.getParameter("suid");
	
	String home;
	int mcnt;
	if((cnt!=null)&&(suid!=null)){
		home="sns_control.jsp?action=getall&cnt="+cnt+"&suid="+suid;
		mcnt = Integer.parseInt(request.getParameter("cnt"));
	}
	else{
		home = "sns_control.jsp?action=getall";
		mcnt=5;
	}

	request.setAttribute("curmsg", request.getParameter("curmsg"));
	
	switch(action){
	case "newmsg":
		//새로운 게시글 등록
		if(msgdao.newMsg(msg))
			response.sendRedirect(home);
		else
			throw new Exception("메세지 등록 오류!!");
		break;
		
	case "delmsg":
		//게시글 삭제
		if(msgdao.delMsg(msg.getMid()))
			response.sendRedirect(home);
		else
			throw new Exception("메세지 삭제 오류!!");
		break;
		
	case "newreply":
		//댓글 등록
		if(msgdao.newReply(reply))
			pageContext.forward(home);
		else
			throw new Exception("댓글 등록 오류!!");
		break;
		
	case "delreply":
		//댓글 삭제
		if(msgdao.delReply(reply.getRid()))
			pageContext.forward(home);
		else
			throw new Exception("댓글 삭제 오류!!");
		break;
		
	case "fav" :
		//좋아요 추가
		msgdao.favorite(msg.getMid());
		pageContext.forward(home);
		break;
		
	case "getall":
		//전체 게시글 가져오기
		ArrayList<MessageSet> datas = msgdao.getAll(mcnt, suid);
		ArrayList<String> nusers = new MemberDAO().getNewMembers();
		request.setAttribute("datas", datas);
		request.setAttribute("nusers", nusers);
		request.setAttribute("suid", suid);
		request.setAttribute("cnt",mcnt);
		pageContext.forward("sns_main.jsp");
		break;
	}	
%>