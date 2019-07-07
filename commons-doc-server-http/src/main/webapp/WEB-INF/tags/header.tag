<%@ tag language="java" pageEncoding="UTF-8"%>
<%--最近发现在Tomcat运行的项目，出现Jsp EL表达式访问失效问题。--%>
<%--原因：Jsp默认是忽略EL表达式，isELIgnored="true"--%>
<%--解决：在Jsp顶部加上<%@ page isELIgnored="false" %>，表示不忽略EL表达式，这样就可以正常访问了。--%>
<%@ tag isELIgnored="false" %>
<%--页眉--%>
<%@attribute name="title" required="false" type="java.lang.String" description="页面标题"%>
<head>
<%
request.setAttribute("base", request.getContextPath());
request.setAttribute("res", request.getContextPath()+"/resources");
request.setAttribute("js",request.getContextPath()+"/resources/js");
request.setAttribute("jscommon",request.getContextPath()+"/resources/js/common");
request.setAttribute("plugins",request.getContextPath()+"/resources/js/plugins");
request.setAttribute("jquery",request.getContextPath()+"/resources/js/plugins/jquery");
request.setAttribute("images",request.getContextPath()+"/resources/images");
request.setAttribute("icons",request.getContextPath()+"/resources/icons");
request.setAttribute("css",request.getContextPath()+"/resources/css");
request.setAttribute("bootstrap",request.getContextPath()+"/resources/bootstrap335");
%>
    <script type="text/javascript" src="${jquery}/jquery.js"></script>
    <script type="text/javascript" src="${bootstrap}/js/bootstrap.js"></script>
    <script type="text/javascript" src="${jscommon}/json2.js"></script>
    <link href="${bootstrap}/css/bootstrap.css" rel="stylesheet" type="text/css"/>

<title>${title }</title>	
<jsp:doBody/>
</head>
