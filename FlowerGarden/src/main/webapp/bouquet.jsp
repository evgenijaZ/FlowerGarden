<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Place</title>
</head>
<body>
<%
    List <String> result = (List <String>) request.getAttribute("result");
    if (result != null) {
        for (String line : result) {
%>
<h5>
    <%=line%>
</h5>
<%
    }
} else {
%>
<h>There is no bouquets with id <%=request.getParameter("id")%></h>
<%
    }
%>
<a href="/">BACK</a>
</body>
</html>
