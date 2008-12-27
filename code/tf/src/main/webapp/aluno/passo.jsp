<%@ page pageEncoding="utf-8"   contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<link rel='stylesheet' href='/tf/stylesheets/layout.css' type='text/css' />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<div class="cabecalho_id">${actionBean.usuario.nome} [Aluno]</div>
<div>
<h1>Aula: ${actionBean.passo.aula.titulo}</h1>
<h2>${actionBean.passo.nome}</h2>
<div>
${actionBean.passo.explicacao_html_formatada} </div><br />

<br />

<c:if test="${actionBean.valoresSaida!=null}" >
<h3>Resultado:</h3>
<c:forEach var="parametro" items="${actionBean.passo.parametrosSaida}">
	${parametro.nome}:
	<pre>${actionBean.valoresSaida[parametro.ordem]}</pre>
</c:forEach>
Você pode repetir/continuar a experiência:<br/>
</c:if>

<br />
	<div class="erros_stripes"><stripes:errors globalErrorsOnly="true" /></div>
	<div class="mensagens_stripes"><stripes:messages /></div>
<stripes:form beanclass="${actionBean.usuario.professor?'tf.action.AulasProfessorActionBean':'tf.action.AulasActionBean'}" focus="passo.titulo">
<input type="hidden" name="aula.id" value="${actionBean.aula.id}" />
<input type="hidden" name="passo.id" value="${actionBean.passo.id}" />

<c:if test="${not empty actionBean.passo.parametrosEntrada}">
<c:if test="${actionBean.valoresSaida==null}" >
<h3>Experimente:</h3>
</c:if>
<c:forEach var="parametro" items="${actionBean.passo.parametrosEntrada}">
	${parametro.nome}: 
	<c:if test="${parametro.classe=='java.lang.Integer'}"><stripes:text name="valoresEntrada[${parametro.ordem}]"></stripes:text></c:if>	
	<c:if test="${parametro.classe=='java.lang.Double'}"><stripes:text name="valoresEntrada[${parametro.ordem}]"></stripes:text></c:if>
	<c:if test="${parametro.classe=='Jama.Matrix'}"><stripes:textarea name="valoresEntrada[${parametro.ordem}]"></stripes:textarea></c:if>
	<br/>	
</c:forEach><br/>
	<stripes:submit name="executarPasso" value="Executar"></stripes:submit> 
</c:if>
	<!--  <input type="button"
		value="Salvar Dados" /> <input type="button" value="Carregar Dados" />-->
		<br/><br/>
		<hr/>
		<c:if test="${actionBean.passo.anterior != null}">
		<stripes:submit name="abrirPassoAnterior" value="<< Passo Anterior" />
		</c:if>
		<c:if test="${actionBean.passo.proximo != null}">
		<stripes:submit name="abrirProximoPasso" value="Proximo Passo >>" />
		</c:if>
		
		<stripes:submit name="${actionBean.usuario.professor?'editarPasso':'listar'}" value="Editar/Voltar"></stripes:submit>
	
</stripes:form>

</body>
</html>