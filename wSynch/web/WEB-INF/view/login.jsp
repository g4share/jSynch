<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" href="/css/base.css" type="text/css"/>
    <link rel="stylesheet" href="/css/login.css" type="text/css"/>

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
    <script type="text/javascript" src="/script/common.js"></script>

    <title>Login to Our Website</title>
</head>

<body onload='document.login.j_username.focus();'>

<form class="form" id="login"  action="<c:url value='j_spring_security_check' />" method='POST'/>
<h1>Log in to your <strong>jSynch</strong> account!</h1>
<p class="register">Not a member? <a href="#">Register here!</a></p>

<div>
    <label for="j_username">Username</label>
    <input type="text"
           name="j_username"
           id="j_username"
           value='<c:out value="${SPRING_SECURITY_LAST_USERNAME}"/>'
           class="field"
           req="Please provide your username" />
</div>

<div>
    <label for="j_password">Password</label>
    <input type="password"
           name="j_password"
           id="j_password"
           class="field"
           req="This field is required."/>
</div>

<p class="forgot"><a href="#">Forgot your password?</a></p>

<div class="submit">
    <button type="submit">Log in</button>

    <label>
        <input type="checkbox" name="remember" id="login_remember" value="yes" />
        Remember my login on this computer
    </label>
</div>
<c:if test="${not empty param['failed']}">
    <span class="error">${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</span>
</c:if>
</form>
</body>
</html>

