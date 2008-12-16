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
<h1>Aula: ${actionBean.aula.titulo}</h1>
<h2>${actionBean.passo.nome}</h2>
<div>
${actionBean.passo.explicacao_html_formatada} </div><br />

<br />

<c:if test="${actionBean.valoresSaida!=null}" >
<h2>Resultados</h2>
<c:forEach var="parametro" items="${actionBean.passo.parametrosSaida}">
	${parametro.nome}:
	<pre>${actionBean.valoresSaida[parametro.ordem]}</pre>
</c:forEach>
Você pode tentar novamente.
</c:if>

Experimente!<br />
<br />
	<div class="erros_stripes"><stripes:errors globalErrorsOnly="true" /></div>
	<div class="mensagens_stripes"><stripes:messages /></div>
<h2>Dados do Problema</h2>
<stripes:form beanclass="${actionBean.usuario.professor?'tf.action.AulasProfessorActionBean':'tf.action.AulasActionBean'}" focus="passo.titulo">
<input type="hidden" name="aula.id" value="${actionBean.aula.id}" />
<input type="hidden" name="passo.id" value="${actionBean.passo.id}" />

<c:forEach var="parametro" items="${actionBean.passo.parametrosEntrada}">
	${parametro.nome}: 
	<c:if test="${parametro.classe=='java.lang.Integer'}"><stripes:text name="valoresEntrada[${parametro.ordem}]"></stripes:text></c:if>	
	<c:if test="${parametro.classe=='java.lang.Double'}"><stripes:text name="valoresEntrada[${parametro.ordem}]"></stripes:text></c:if>
	<c:if test="${parametro.classe=='Jama.Matrix'}"><stripes:textarea name="valoresEntrada[${parametro.ordem}]"></stripes:textarea></c:if>
	<br/>	
</c:forEach>
<!--  
<table class="tb_matriz">
		<tr>
			<td colspan="5"><input type="button"
				value="importar (Excel, txt)" />
			<td>
		</tr>
	</table>
	-->
	<div class="botoes_submit"><stripes:submit name="executarPasso" value="Executar"></stripes:submit> <input type="button"
		value="Salvar Dados" /> <input type="button" value="Carregar Dados" />
		<stripes:submit name="${actionBean.usuario.professor?'editarPasso':'listar'}" value="Voltar"></stripes:submit>
	</div>
</stripes:form>
</body>
</html>