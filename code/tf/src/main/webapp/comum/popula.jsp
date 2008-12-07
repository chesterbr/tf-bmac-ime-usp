<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="tf.model.data.*,tf.helpers.*,org.hibernate.*,java.util.*"%>
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

	Aula a1 = new Aula();
	a1.setTitulo("aula com dois passos");
	s.save(a1);
	
	Passo p1 = new Passo();
	p1.setNome("primeiro passo");
	p1.setExplicacao_html("bla ble <b>bli</b><br/>blo blu");
	p1.setCodigo_java("return 2+2;");
	p1.setAula(a1);
	s.save(p1);
	
	Passo p2 = new Passo();
	p2.setNome("segundo passo");
	p2.setExplicacao_html("asdfghjkl<img src=\"http://chester.blog.br/img/chester.gif\">");
	p2.setCodigo_java("return false;");
	p2.setAula(a1);
	s.save(p2);
	
	Parametro par1 = new Parametro();
	par1.setNome("dividendo");
	par1.setOrdem(1);
	par1.setClasse("java.lang.Double");
	par1.setTipoEntrada(true);
	par1.setDescricao("asdfg hjkl");
	par1.setPasso(p1);
	s.save(par1);	
	
	Parametro par2 = new Parametro();
	par2.setNome("divisor");
	par2.setOrdem(2);
	par2.setClasse("java.lang.Integer");
	par2.setTipoEntrada(true);
	par2.setDescricao("qwerty");
	par2.setPasso(p1);
	s.save(par2);	
	
	Parametro par3 = new Parametro();
	par3.setNome("quociente");
	par3.setOrdem(1);
	par3.setClasse("java.lang.Double");
	par3.setTipoEntrada(false);
	par3.setDescricao("whatever");
	par3.setPasso(p1);
	s.save(par3);	
	
	Aula a2 = new Aula();
	a2.setTitulo("aula 2");
	s.save(a2);

	t.commit();
	
%> pronto!
</body>
</html>
