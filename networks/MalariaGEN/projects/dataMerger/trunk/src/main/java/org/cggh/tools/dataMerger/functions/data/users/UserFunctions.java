package org.cggh.tools.dataMerger.functions.data.users;

import org.cggh.tools.dataMerger.data.users.UserModel;

public class UserFunctions {

	public String getUserAsDecoratedXHTMLTableUsingUserModel(UserModel userModel) {
		
		StringBuffer userAsDecoratedXHTMLTable = new StringBuffer();
		
		userAsDecoratedXHTMLTable.append("<table class=\"user-table\">");
		
		userAsDecoratedXHTMLTable.append("<thead>");
		
		userAsDecoratedXHTMLTable.append("<tr>");
		
		userAsDecoratedXHTMLTable.append("	<th>Username</th>");
		userAsDecoratedXHTMLTable.append("	<th>Password</th>");
		userAsDecoratedXHTMLTable.append("	<th><!-- Above Add button --></th>");
			
		userAsDecoratedXHTMLTable.append("</tr>");
		
		userAsDecoratedXHTMLTable.append("</thead>");
		userAsDecoratedXHTMLTable.append("<tbody>");
		
		userAsDecoratedXHTMLTable.append("<tr>");

		userAsDecoratedXHTMLTable.append("	<td><input type=\"text\" name=\"username\" value=\"\"/></td>");
		
		userAsDecoratedXHTMLTable.append("	<td><input type=\"password\" name=\"password\" value=\"\"/></td>");
		
		
		//FIXME: The src reference is hard-coded, convert to soft-code.
		userAsDecoratedXHTMLTable.append("<td>");
		userAsDecoratedXHTMLTable.append("<button class=\"createUserButton\">Add</button><img class=\"creating-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Creating...\"/>");
		userAsDecoratedXHTMLTable.append("</td>");
		
		userAsDecoratedXHTMLTable.append("</tr>");
		
		userAsDecoratedXHTMLTable.append("</tbody>");
		
		userAsDecoratedXHTMLTable.append("</table>");
		
		
		
		
		return userAsDecoratedXHTMLTable.toString();
	}

}
