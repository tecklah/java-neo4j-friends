<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>Neo4j Friends graph database</h2>
<form method="post" name="form" action="./Index">
<%= request.getParameter("message") != null ? request.getParameter("message") : "" %>
<table>
	<tr>
		<td>My Name</td>
		<td>
			<input type="text" name="myName" size="20" />
		</td>
	</tr>
	<tr>
		<td>Friend Name</td>
		<td>
			<input type="text" name="friendName" size="20" />
		</td>
	</tr>
	<tr>
		<td>Distance</td>
		<td>
			<input type="text" name="distance" size="20" />
		</td>
	</tr>
	<tr>
		<td>
			<input type="submit" name="submit" value="Add" /> 
			<input type="submit" name="submit" value="List" />
		</td>
	</tr>
</table>
<c:forEach var="entry" items="${ result }">
   <br>${entry.value}
</c:forEach>
</form>
</body>
</html>
