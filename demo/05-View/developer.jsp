<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
	<title>Developer</title>
</head>
<body>
	<form:form modelAttribute="developer">
		<fieldset>
			<label for="name">Name:</label>
			<p><form:input path="name"/></p>
			<label for="languages">Languages:</label>
			<p><form:select path="languages" multiple="true" items="${languages}"/></p>
			<p>
			<button type="submit" id="proceed" name="_proceed">Submit</button>
			<button type="submit" name="_cancel" >Cancel</button>
			</p>
		</fieldset>
	</form:form>	
</body>
</html>
