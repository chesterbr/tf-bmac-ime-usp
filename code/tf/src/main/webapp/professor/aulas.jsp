<%@ page pageEncoding="utf-8"   contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>
<html>
<head>
<link rel='stylesheet' href='/tf/stylesheets/layout.css' type='text/css' />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<stripes:form beanclass="tf.action.AulasProfessorActionBean"
	focus="aula.id">
	<div class="cabecalho_id">${actionBean.usuario.nome} [Professor]</div>
	<h1>Aulas</h1>
	<div class="erros_stripes"><stripes:errors /></div>
	<div class="mensagens_stripes"><stripes:messages /></div>
	<div class="lista_esq">
	<stripes:select name="aula.id" size="10" ondblclick="document.forms[0].editar.click()">
		<stripes:options-collection collection="${actionBean.aulas}"
			label="titulo" value="id" />
	</stripes:select></div>
	<div class="botoes_dir"><stripes:submit name="nova"
		value="Criar Nova" /><br />
	<input type="button" value="Testar" /><br />
	<stripes:submit name="editar" value="Editar" /><br />
	<stripes:submit name="apagar" value="Apagar" /><br />
	</div>
</stripes:form>
</body>
</html>