<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>
<html>
<head>
<link rel='stylesheet' href='/tf/stylesheets/layout.css' type='text/css' />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<script>
function campos(n) {
  s='<select><option>Real (double)</option><option>Inteiro (int)</option><option>Matriz (Matrix)</option></select><input type="text" value="nome"/><input type="text" value="descriÃ§Ã£o"/><input type="button" value="Mover Acima"/><input type="button" value="Mover Abaixo"/><input type="button" value="Apagar"/><br/>';
  for(i=0;i<n;i++) {
    document.write(s);
  }
document.write('<input type="button" value="Criar Novo" /><br/>');
}
</script>
</head>
<body>
<stripes:form beanclass="tf.action.AulasProfessorActionBean" 
	focus="passo.titulo">
<div class="cabecalho_id">Professor: Sicrano</div>
<h1>Aula:  - Passo: </h1>
<div class="campos_lista">
<label for="passo.titulo">Título</label>
<stripes:text name="passo.titulo" /><br/>
<label for="passo.descricao">Descrição</label>
<stripes:textarea name="passo.descricao" rows="2"></stripes:textarea><br/>
<label for="explicacao">Explicação</label>
<stripes:textarea name="passo.explicacao" rows="8"></stripes:textarea><br/>
</div>
<div class="algoritmo_campos">
<h2>Dados de Entrada</h2>
<script>campos(3);</script>
<h2>Dados de SaÃ­da<h2/>
<script>campos(2);</script>
</div>
<h2><label for="cÃ³digo">CÃ³digo</label><br/></h2>
<div class="algoritmo_codigo">
<textarea name="codigo" rows="8" cols="80"></textarea>
</div>
<div class="botoes_dir">
<input type="button" value="Verificar Sintaxe" />
<input type="button" value="Testar" />
</div>
<div class="botoes_submit">
<stripes:submit name="salvarPasso" value="Salvar" />
<input type="button" value="Cancelar" />
</div>
</stripes:form>
</body>
</html>