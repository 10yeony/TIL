<%@page import="servlet.model.UserVO"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>�α��� ����</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body>
<div align="center">
<p>${rvo.userId}���� �α��� �Ǿ����ϴ�.!!!<p><br/><br/>
<p><a href="book/Book.jsp">���� ���</a></p>
<p><a href="logout.jsp">�α׾ƿ�</a></p>
</div>
</body>
</html>