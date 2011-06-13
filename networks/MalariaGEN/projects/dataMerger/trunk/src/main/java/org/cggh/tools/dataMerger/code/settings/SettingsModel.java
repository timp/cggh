package org.cggh.tools.dataMerger.code.settings;

import java.util.HashMap;

public class SettingsModel {

	private HashMap<String, String> settingsAsHashMap;

	public void setSettingsAsHashMap(HashMap<String, String> settingsAsHashMap) {
		this.settingsAsHashMap = settingsAsHashMap;
	}

	public HashMap<String, String> getSettingsAsHashMap() {
		return settingsAsHashMap;
	}


}
