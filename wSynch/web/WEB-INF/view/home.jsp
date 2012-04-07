<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Home Page</title>
</head>
<body>
    <p>
        Hello, <strong><c:out value="${userName}"/></strong>
        <a href="<c:url value='j_spring_security_logout' />" > Logout</a>
    </p>
    <br/>
    <span>Interval: </span><span>${config.getInterval()}</span>
    <table border="1">
        <tr>
            <th>Point name</th>
            <th>Status</th>
            <th>Path</th>
            <th>Number of folder</th>
            <th>Number of files</th>
            <th>Total File length</th>
        </tr>
        <c:forEach var="pointHash" items="${config.getPointHash()}">
            <tr>
                <td>${pointHash.getName()}</td>
                <td>${pointHash.getStatus().getDescription()}</td>

                <c:forEach var="pathHash" items="${pointHash.getPathHash()}"  varStatus="status">
                    <c:if test = "${status.count != 1}">
                        <tr>
                        <td colspan="2">&nbsp;</td>
                    </c:if>

                    <td>${pathHash.getPath()}</td>
                    <td>${pathHash.getFolders()}</td>
                    <td>${pathHash.getFiles()}</td>
                    <td>${pathHash.getSize()}</td>
                </tr>
            </c:forEach>
        </c:forEach>
    </table>


</body>
</html>