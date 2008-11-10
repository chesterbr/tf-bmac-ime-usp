<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="tf.model.data.*,tf.helpers.*,org.hibernate.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
Populando banco...
<%
	Session s = HibernateSessionHelper.getSession();
	Transaction t = s.beginTransaction();

	Usuario u = new Usuario();
	u.setEmail("professor@usp.br");
	u.setSenha("asdfgh");
	u.setProfessor(true);
	u.setNome("Professor Teste");
	s.save(u);
	
	u = new Usuario();
	u.setEmail("aluno@usp.br");
	u.setSenha("asdfgh");
	u.setProfessor(false);
	u.setNome("Aluno Teste");
	s.save(u);
	
	
	t.commit();
	s.close();
	
%>
</body>
</html>