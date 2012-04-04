<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" href="/css/base.css" type="text/css"/>
    <link rel="stylesheet" href="/css/common.css" type="text/css"/>

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
    <script type="text/javascript" src="/script/common.js"></script>

</head>
<body onload='document.login.j_username.focus();'>
<div class="form" id="msg403">
    <h1>Access denied</h1>
    <div>You do not have permissions to access this page. Please <a href="<c:url value='j_spring_security_logout' />">re-login.</a></div>
</div>
</body>
</html>
