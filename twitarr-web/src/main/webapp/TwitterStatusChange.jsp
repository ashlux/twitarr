<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Change your twitter status here.</title>
</head>
<body>
<h1>Change your twitter status:</h1>

<form action="TwitterStatusChangeServlet" name="twitterStatusChangeForm" method="post">
  Status: <input type="text" name="status" maxlength="140"/><br>
  Username: <input type="text" name="username" maxlength="20"/><br>
  Password: <input type="password" name="password" maxlength="30"/><br>
  <input type="submit" name="submit"/><br>
</form>

<font size="1">* This is really just used for testing twitter4j with google app engine.</font>
</body>
</html>