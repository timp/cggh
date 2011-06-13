package org.cggh.tools.dataMerger.functions.data.users;

import org.cggh.tools.dataMerger.data.users.UserModel;

public class UserFunctions {

	public String getUserAsDecoratedXHTMLTableUsingUserModel(UserModel userModel) {
		
		String userAsDecoratedXHTMLTable = "";
		
		userAsDecoratedXHTMLTable += "<table class=\"user-table\">";
		
		userAsDecoratedXHTMLTable += "<thead>";
		
		userAsDecoratedXHTMLTable += "<tr>";
		
		userAsDecoratedXHTMLTable += "	<th>Username</th>";
		userAsDecoratedXHTMLTable += "	<th>Password</th>";
		userAsDecoratedXHTMLTable += "	<th><!-- Above Add button --></th>";
			
		userAsDecoratedXHTMLTable += "</tr>";
		
		userAsDecoratedXHTMLTable += "</thead>";
		userAsDecoratedXHTMLTable += "<tbody>";
		
		userAsDecoratedXHTMLTable += "<tr>";

		userAsDecoratedXHTMLTable += "	<td><input type=\"text\" name=\"username\" value=\"\"/></td>";
		
		userAsDecoratedXHTMLTable += "	<td><input type=\"password\" name=\"password\" value=\"\"/></td>";
		
		
		//FIXME: The src reference is hard-coded, convert to soft-code.
		userAsDecoratedXHTMLTable += "<td>";
		userAsDecoratedXHTMLTable += "<button class=\"createUserButton\">Add</button><img class=\"creating-indicator\" src=\"/dataMerger/pages/shared/gif/loading.gif\" style=\"display:none\" title=\"Creating...\"/>";
		userAsDecoratedXHTMLTable += "</td>";
		
		userAsDecoratedXHTMLTable += "</tr>";
		
		userAsDecoratedXHTMLTable += "</tbody>";
		
		userAsDecoratedXHTMLTable += "</table>";
		
		
		
		
		return userAsDecoratedXHTMLTable;
	}

}
