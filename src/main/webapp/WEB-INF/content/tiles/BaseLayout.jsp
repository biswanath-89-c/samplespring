<!--
	Maven, Struts2 Annotations and Tiles Integration Example via Convention / Codebhind / Zero Config plugin using Eclipse IDE
	http://codeoftheday.blogspot.com/2013/07/maven-struts2-annotations-and-tiles.html 
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Struts2 Annotations and Tiles Integration Example - <tiles:insertAttribute name="title" ignore="true" /></title>
	</head>
	<body>
		<tiles:insertAttribute name="header" />
		<tiles:insertAttribute name="body" />
		<tiles:insertAttribute name="footer" />
	</body>
</html>
