<%@ page import="org.cggh.tools.dataMerger.data.databases.DatabasesCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.data.databases.DatabaseModel" %>
<%@ page import="org.cggh.tools.dataMerger.data.users.UsersCRUD" %>
<%@ page import="org.cggh.tools.dataMerger.data.users.UserModel" %>
<%

DatabasesCRUD databasesCRUD = new DatabasesCRUD();
DatabaseModel databaseModel = databasesCRUD.retrieveDatabaseAsDatabaseModelUsingServletContext(request.getSession().getServletContext());

UsersCRUD usersCRUD = new UsersCRUD();
usersCRUD.setDatabaseModel(databaseModel);
UserModel userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(request.getRemoteUser());
	
if (userModel.getId() == null) {

	usersCRUD.createUserUsingUsername(userModel.getUsername());
	userModel = usersCRUD.retrieveUserAsUserModelUsingUsername(userModel.getUsername());
	
}
%>