<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Home Page</title>
</head>
<body>
    <h3>Hello</h3>
    <c:out value="${username}"/>
    <br/>

    <table border="1">
        <tr><td>Interval:</td><td>${config.getInterval()}</td></tr>
        <c:forEach var="pInfo" items="${config.getPointInfo()}">
            <tr>
            <td>${pInfo.getName()}</td>

            <c:forEach var="path" items="${pInfo.getStorePaths()}"  varStatus="status">
                <c:choose>
                    <c:when test="${status.count == 1}">
                        <td>${path}</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr><td></td><td>${path}</td></tr>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:forEach>
    </table>

    <a href="<c:url value='j_spring_security_logout' />" > Logout</a>
</body>
</html>