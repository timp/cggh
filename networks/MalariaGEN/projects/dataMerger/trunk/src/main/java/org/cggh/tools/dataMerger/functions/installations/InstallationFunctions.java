package org.cggh.tools.dataMerger.functions.installations;

import org.cggh.tools.dataMerger.data.installation.InstallationModel;

public class InstallationFunctions {

	public String determineVersionAsStringUsingInstallationModel(InstallationModel installationModel) {

		String versionAsString = null;
		
		versionAsString = installationModel.getMajorVersionNumber().toString() + "." + installationModel.getMinorVersionNumber().toString() + "." + installationModel.getRevisionVersionNumber().toString();
		
		return versionAsString;
	}

}
