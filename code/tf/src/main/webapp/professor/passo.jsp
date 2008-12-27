<%@ page pageEncoding="utf-8"   contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>
<html>
<head>
<link rel='stylesheet' href='/tf/stylesheets/layout.css' type='text/css' />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="/tf/javascript/jquery.js"></script>
<script type="text/javascript" src="/tf/javascript/json2.js"></script>
<script type="text/javascript">
	parametros = ${actionBean.passo.parametrosAsJSON};
	classes = ${actionBean.classesAsJSON};
	
	function campos(isEntrada) {
		$.each(parametros, function(i, parametro) {
			if (isEntrada==parametro.tipoEntrada)
				document.write(html_parametro(parametro));
		});
		document.write('<input id="btnNovo'+(isEntrada ? 'E' : 'S')+'" type="button" '+
					   'value="Criar Novo" onClick="novo_parametro('+isEntrada+')"/><br/>');
	}

	function novo_parametro(isEntrada) {		
		$((isEntrada ? '#btnNovoE' : '#btnNovoS')).before(
				html_parametro({"classe":"","descricao":"descricao","nome":"nome","tipoEntrada":isEntrada}));
		$("#param_nome"+(seq-1)).focus();
	}

	seq = 1;

	function html_parametro(parametro) {
		var n = seq++;
		var s = "";
		s = s + '<select id="param_classe'+n+'">';
		$.each(classes, function(classe_value, classe_text) {
			s = s + '<option value="'+classe_value+'"';
			if (classe_value == parametro.classe)
				s = s + ' selected="selected" ';
			s = s + '>'+ classe_text+ '</option>';
		});
		s = s + '</select>';
		s = s + '<input type="text" id="param_nome'+n+'" value="' + parametro.nome      + '" onFocus="this.select()" />';
		s = s + '<input type="text" id="param_desc'+n+'" value="' + parametro.descricao + '" onFocus="this.select()" />';
		s = s + '<input type="hidden" id="param_tipo'+n+'" value="' + parametro.tipoEntrada + '" />';
		//s = s + '<input type="button" value="Mover Acima"/><input type="button" value="Mover Abaixo"/><input type="button" value="Apagar"/>'
		s = s + '<br/>';
		return s;
	}

	function atualiza_preview_html() {
		$("#preview_html").html($("#campo_html").attr("value").replace(/\n/g, "<br/>"));
		return true;
	}

	function atualiza_dados_submit() {
		ordem_e = ordem_s = 1;
		dados = new Array();
		for (i=1; i<seq; i++) {
			$("#param_nome"+i).each(function(a) {
				if ((this.value!="nome") && (this.value!=""))
					dados.push({"nome":this.value,
						"descricao": $("#param_desc"+i).attr("value"),
						"classe": $("#param_classe"+i).attr("value"),
						"ordem": ($("#param_tipo"+i).attr("value")=="true" ? ordem_e++ : ordem_s++),
						"tipoEntrada": $("#param_tipo"+i).attr("value")=="true"});
			});
		}
		$("#params").attr("value",JSON.stringify(dados));
		return true;
	}

	
</script>
</head>
<body>
<stripes:form beanclass="tf.action.AulasProfessorActionBean"
	focus="passo.titulo" onsubmit="return atualiza_dados_submit();">
	<input type="hidden" name="aula.id" value="${actionBean.aula.id}" />
	<input type="hidden" name="passo.id" value="${actionBean.passo.id}" />
	<input type="hidden" name="passo.ordem" value="${actionBean.passo.ordem}" />
	<input type="hidden" name="passo.parametrosAsJSON" id="params" value="x" />
	<div class="cabecalho_id">Professor: Sicrano</div>
	<h1>Aula: ${actionBean.passo.aula.titulo} - Passo:
	${actionBean.passo.nome}</h1>
	<div class="erros_stripes"><stripes:errors globalErrorsOnly="true" /></div>
	<div class="mensagens_stripes"><stripes:messages /></div>
<div style="float:right; width:50%;">Visualizar:<br/><div id="preview_html" style="overflow:auto; border-width:1px; border-style:solid; width:400px; height:100px;"></div></div>	
	<div class="campos_lista"><label for="passo.nome">Nome</label> <stripes:text
		name="passo.nome" /><br />
	<label for="passo.explicacao_html">Explicação</label> <stripes:textarea
		name="passo.explicacao_html" rows="8" id="campo_html" onkeyup="atualiza_preview_html()"></stripes:textarea> <br />
		<script>atualiza_preview_html();</script>
	</div>
	<div class="algoritmo_campos">
	<h2>Dados de Entrada</h2>
	<script>
	campos(true);
</script>
	<h2>Dados de Saída</h2>
	<script>
	campos(false);
</script>
	</div>
	<h2><label for="passo.codigo_java">Código</label><br />
	</h2>
	<div class="erros_stripes"><stripes:errors field="passo.codigo_java"/></div>
	<div class="algoritmo_codigo"><stripes:textarea
		name="passo.codigo_java" rows="8" cols="80"></stripes:textarea></div>
	<div class="botoes_dir"><stripes:submit name="checarPasso" value="Verificar Sintaxe"/> <stripes:submit name="testarPasso" value="Testar" />
	</div>
	<div class="botoes_submit"><stripes:submit name="salvarPasso"
		value="Salvar" /> <stripes:submit name="editar" value="Cancelar" />
	</div>
</stripes:form>
</body>
</html>