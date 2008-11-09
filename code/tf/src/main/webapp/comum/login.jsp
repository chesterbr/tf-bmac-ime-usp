<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<link rel='stylesheet' href='/tf/stylesheets/layout.css' type='text/css' />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<h1>Bem-vindo!</h1>
<stripes:form beanclass="tf.action.AutenticacaoActionBean" focus="email">
	<div class="erros_stripes"><stripes:errors /></div>
	<label for="email">e-mail</label>
	<stripes:text name="email" />
	<br />
	<label for="senha">senha</label>
	<stripes:text name="senha" />
	<br />
	<stripes:submit name="login" value="Entrar" />
</stripes:form>
<br />
</div>
</body>
</html>