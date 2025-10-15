<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fn.tld" prefix="fn" %>
<!DOCTYPE html><html><head><title>Your Profile</title></head><body>
<h2>Your Profile</h2>
<c:if test="${not empty message}"><div class="message">${message}</div></c:if>
<form method="post" action="${pageContext.request.contextPath}/profile">
  <label>Username</label><input value="${sessionScope.user.username}" disabled />
  <label>Email</label><input name="email" value="${sessionScope.user.email}" />
  <label>First name</label><input name="firstName" value="${sessionScope.user.firstName}" />
  <label>Last name</label><input name="lastName" value="${sessionScope.user.lastName}" />
  <label>Phone</label><input name="phone" value="${sessionScope.user.phone}" />
  <label>Preferences (JSON)</label><input name="preferencesJson" value="${sessionScope.user.preferencesJson}" />
  <button type="submit">Save</button>
</form>
<p>Role: ${sessionScope.user.role}</p>
<p><a href="${pageContext.request.contextPath}/logout">Logout</a></p>
<c:if test="${sessionScope.user.role == 'ADMIN'}">
  <p><a href="${pageContext.request.contextPath}/admin">Admin dashboard</a></p>
</c:if>
</body></html>