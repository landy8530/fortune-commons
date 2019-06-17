<%@ tag language="java" pageEncoding="UTF-8"%>
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
<link href="${bootstrap}/css/bootstrap.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${jquery}/jquery.js"></script>
<script type="text/javascript" src="${bootstrap}/js/bootstrap.js"></script>
<script type="text/javascript" src="${jscommon}/json2.js"></script>

<title>${title }</title>	
<jsp:doBody/>
</head>
