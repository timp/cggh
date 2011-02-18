package org.cggh.tools.dataMerger.data.joins;

import org.cggh.tools.dataMerger.data.DataModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;
import org.cggh.tools.dataMerger.data.users.UserModel;

public class JoinModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4112178863119955390L;
	private DataModel dataModel;
	private UserModel userModel;
	private MergeModel mergeModel;

	public JoinModel() {
	
		this.setDataModel(new DataModel());
		this.setUserModel(new UserModel());
		this.setMergeModel(new MergeModel());
	}


    public void setDataModel (final DataModel dataModel) {
        this.dataModel  = dataModel;
    }
    public DataModel getDataModel () {
        return this.dataModel;
    }       
    
    public void setUserModel (final UserModel userModel) {
        this.userModel  = userModel;
    }
    public UserModel getUserModel () {
        return this.userModel;
    }     
    
    public void setMergeModel (final MergeModel mergeModel) {
        this.mergeModel  = mergeModel;
    }
    public MergeModel getMergeModel () {
        return this.mergeModel;
    }  
    
    
    	
	
 




 
}
