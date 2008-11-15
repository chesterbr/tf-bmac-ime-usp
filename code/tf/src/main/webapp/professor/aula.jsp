<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>
<html>
<head>
<link rel='stylesheet' href='/tf/stylesheets/layout.css' type='text/css' />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<stripes:form beanclass="tf.action.AulasProfessorActionBean"
	focus="aula.titulo">
	<input type="hidden" name="aula.id" value="${actionBean.aula.id}" />
	<div class="cabecalho_id">${actionBean.usuario.nome} [Professor]</div>
	<h1>Aula: ${actionBean.aula.titulo}</h1>
	<div class="erros_stripes"><stripes:errors /></div>
	<div class="mensagens_stripes"><stripes:messages /></div>

	<div class="campos_lista"><label for="aula.titulo">Título</label>
	<stripes:text name="aula.titulo" /><br />
	</div>
	<div class="botoes_submit"><stripes:submit name="salvar"
		value="Salvar" /> <stripes:submit name="listar" value="Cancelar" /></div>
	<h2>Passos</h2>
	<div class="lista_esq"><stripes:select name="passo.id" size="10"
		ondblclick="document.forms[0].editarPasso.click()">>
		<stripes:options-collection collection="${actionBean.aula.passos}"
			label="nome" value="id" />
	</stripes:select></div>
	<div class="botoes_dir"><input type="button" value="Mover Acima" /><br />
	<input type="button" value="Mover Abaixo" /><br />
	<stripes:submit name="novoPasso" value="Criar Novo" /><br />
	<input type="button" value="Testar" /><br />
	<stripes:submit name="editarPasso" value="Editar" /><br />
	<stripes:submit name="apagarPasso" value="Apagar" /><br />
	</div>
</stripes:form>
</body>
</html>