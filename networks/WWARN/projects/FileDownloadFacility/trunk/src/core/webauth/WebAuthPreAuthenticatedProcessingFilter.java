/**
 * 
 */
package core.webauth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.security.ui.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * @author raok
 *
 */
public class WebAuthPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {
	
	/* SQL query to obtain email address by userId */
	private static final String USER_EMAIL_BY_USERID = "SELECT email AS email "
												   + " FROM master_user "
												   + " WHERE user_id=? ";
	
	private DataSource dataSource;
	
	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.ui.preauth.AbstractPreAuthenticatedProcessingFilter#getPreAuthenticatedCredentials(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest arg0) {
		//Required override, not used, but must not return null.
		return "";
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.ui.preauth.AbstractPreAuthenticatedProcessingFilter#getPreAuthenticatedPrincipal(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest arg0) {
		//TODO throw more appropriate errors.
		
		//Get the WEBAUTH_USER WebAuth Header which should be added by a WebAuth proxy server.
		//This is of the form: 'user<webAuthUserId>/gmap@GMAP.NET'
		String webAuthUserString = (String) arg0.getHeader("WEBAUTH_USER");
		
		//Extract the webAuthUserId.
		String webAuthUserIdString;
		if (webAuthUserString != null)
		{
			Pattern regEx = Pattern.compile("^user(\\d+)/gmap@GMAP\\.NET$");
			Matcher matcher = regEx.matcher(webAuthUserString);
			
			//throw exception if webAuth attribute is invalid or not found
			if (!matcher.find())
			{
				throw new Error ("WebAuth attribute invalid: " + webAuthUserString);
			}
			
			//Store webAuthUserId
			webAuthUserIdString = matcher.group(1);
		}
		else
		{
			throw new Error ("WebAuth attribute not found.");
		}
		
		// Obtain email address of user and return as the userId
		String userEmail = "";
		try {
			Connection connection = dataSource.getConnection();
			
			PreparedStatement sql = connection.prepareStatement(USER_EMAIL_BY_USERID);
			sql.setString(1, webAuthUserIdString);
			
			ResultSet rs = sql.executeQuery();
			
			if (rs.next()) {
				userEmail = rs.getString("email");
			} else {
				throw new Error ("Error obtaining email address of user.");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userEmail;
	}

	/* (non-Javadoc)
	 * @see org.springframework.core.Ordered#getOrder()
	 */
	public int getOrder() {
        return org.springframework.security.ui.FilterChainOrder.PRE_AUTH_FILTER;
	}

}
