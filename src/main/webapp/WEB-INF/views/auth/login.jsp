<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fn.tld" prefix="fn" %>
<!DOCTYPE html><html><head><title>Login</title></head><body>
<h2>Login</h2>
<c:if test="${not empty error}"><div class="error">${error}</div></c:if>
<form method="post" action="${pageContext.request.contextPath}/login">
  <label>Username</label><input name="username" required />
  <label>Password</label><input type="password" name="password" required />
  <button type="submit">Login</button>
</form>
<p><a href="${pageContext.request.contextPath}/register">Register</a></p>
</body></html>