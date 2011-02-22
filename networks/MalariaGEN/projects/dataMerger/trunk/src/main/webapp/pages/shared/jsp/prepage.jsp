<jsp:useBean id="dataModel" class="org.cggh.tools.dataMerger.data.DataModel" scope="session"/>
<jsp:useBean id="userModel" class="org.cggh.tools.dataMerger.data.users.UserModel" scope="session"/>
<jsp:useBean id="usersModel" class="org.cggh.tools.dataMerger.data.users.UsersModel" scope="session"/>
<%

dataModel.setDataModelByServletContext(request.getSession().getServletContext());
userModel.setDataModel(dataModel);
userModel.setUserModelByUsername(request.getRemoteUser());

// Login/Register user early on.

usersModel.setDataModel(dataModel);
usersModel.setUserModel(userModel);

if (!usersModel.isUsernameCreated(usersModel.getUserModel().getUsername())) {
	
	usersModel.createUserByUsername(usersModel.getUserModel().getUsername());

	userModel.setUserModelByUsername(request.getRemoteUser());
}


%>