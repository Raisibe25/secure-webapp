<%
    if (session != null && session.getAttribute("user") != null) {
        // Already logged in → go to profile
        response.sendRedirect(request.getContextPath() + "/profile");
    } else {
        // Not logged in → go to login
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>