<%@ page import="edu.kpi.jee.labs.entities.Place" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Place</title>
</head>
<body>
<%
    Place place = (Place) request.getAttribute("place");
    if (place != null) {
%>
<h5>
    <%=place.toString()%>
</h5>
<%
    }else {%>
<h>There is no places with id <%=request.getParameter("place_id")%> </h>
<%
    }
%>
<a href="/index">BACK</a>
</body>
</html>
