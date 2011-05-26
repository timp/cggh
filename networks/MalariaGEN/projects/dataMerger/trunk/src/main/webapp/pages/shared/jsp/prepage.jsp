<%@ page import="org.cggh.tools.dataMerger.data.databases.DatabasesCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.data.databases.DatabaseModel" %>
<%@ page import="org.cggh.tools.dataMerger.data.users.UsersCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.data.users.UserModel" %>
<%

DatabasesCRUD databasesCRUD = new DatabasesCRUD();
DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());

//Note: Made available in the page scope.
UserModel userModel = null;

if (databaseModel.isConnectable()) {
	
	if (databaseModel.isInitialized()) {
		
		UsersCRUD usersCRUD = new UsersCRUD();
		usersCRUD.setDatabaseModel(databaseModel);
		userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(request.getRemoteUser());
			
		if (userModel.getId() == null) {
		
			usersCRUD.createUserUsingUsername(userModel.getUsername());
			userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(userModel.getUsername());
			
		}
		
	} else {
		response.sendRedirect("/dataMerger/pages/guides/configuration/errors/database-initialization");
	}
	
} else {
	response.sendRedirect("/dataMerger/pages/guides/configuration/errors/database-connection");
}

%>