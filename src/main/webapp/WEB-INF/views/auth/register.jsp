<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fn.tld" prefix="fn" %>
<!DOCTYPE html><html><head><title>Register</title></head><body>
<h2>Register</h2>
<c:if test="${not empty errors}">
  <ul class="error"><c:forEach items="${errors}" var="e"><li>${e}</li></c:forEach></ul>
</c:if>
<form method="post" action="${pageContext.request.contextPath}/register">
  <label>Username</label><input name="username" required />
  <label>Email</label><input type="email" name="email" required />
  <label>Password</label><input type="password" name="password" required />
  <button type="submit">Create account</button>
</form>
<p><a href="${pageContext.request.contextPath}/login">Back to login</a></p>
</body></html>