package org.cggh.tools.dataMerger.functions.data.installations;

import org.cggh.tools.dataMerger.data.installations.InstallationModel;

public class InstallationFunctions {

	public String determineVersionAsStringUsingInstallationModel(InstallationModel installationModel) {

		String versionAsString = null;
		
		if (installationModel.getMajorVersionNumber() != null 
				&& installationModel.getMinorVersionNumber() != null
				&& installationModel.getRevisionVersionNumber() != null) {
			versionAsString = installationModel.getMajorVersionNumber().toString() + "." + installationModel.getMinorVersionNumber().toString() + "." + installationModel.getRevisionVersionNumber().toString();
		}
		
		return versionAsString;
	}

}
