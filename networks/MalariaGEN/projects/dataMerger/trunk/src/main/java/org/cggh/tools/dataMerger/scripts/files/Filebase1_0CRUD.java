package org.cggh.tools.dataMerger.scripts.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.cggh.tools.dataMerger.data.users.UserModel;

public class Filebase1_0CRUD {

	
	private final Logger logger = Logger.getLogger("org.cggh.tools.dataMerger.scripts.data");
	
	private UserModel userModel = null;
	
	public Filebase1_0CRUD () {
		
		this.setUserModel(new UserModel());
		
	}
	
	public Boolean create () {

		File filebaseServerPath = new File(this.getUserModel().getHttpServletRequest().getSession().getServletContext().getInitParameter("filebaseServerPath"));
		if (filebaseServerPath.mkdirs()) {
			
			String installationFilePath = this.getUserModel().getHttpServletRequest().getSession().getServletContext().getInitParameter("filebaseServerPath") + "installation.csv";
			String installationFileHeadings = "major_version_number,minor_version_number,revision_version_number,created_by_user_id,created_datetime"; 
			String installationFileRecord = "1,0,0," + this.getUserModel().getId() + ",created_datetime";
			
			try {
				FileWriter installationFileWriter = new FileWriter(installationFilePath); //,true will append
				BufferedWriter installationFileBufferedWriter = new BufferedWriter(installationFileWriter);
				
				installationFileBufferedWriter.write(installationFileHeadings);
				installationFileBufferedWriter.newLine();
				
				installationFileBufferedWriter.write(installationFileRecord);
				installationFileBufferedWriter.newLine();
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
			
			
			
			
		} else {
			return false;
		}		
		
		File uploadsFileRepositoryBasePath = new File(this.getUserModel().getHttpServletRequest().getSession().getServletContext().getInitParameter("uploadsFileRepositoryBasePath"));
		if (!uploadsFileRepositoryBasePath.mkdirs()) {
			return false;
		}

        File exportsFileRepositoryBasePath = new File(this.getUserModel().getHttpServletRequest().getSession().getServletContext().getInitParameter("exportsFileRepositoryBasePath"));
        if (!exportsFileRepositoryBasePath.mkdirs()) {
        	return false;
        }
        
        return true;
	}


	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public UserModel getUserModel() {
		return userModel;
	}
}
