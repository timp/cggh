package org.cggh.tools.dataMerger.functions.joins;


import org.cggh.tools.dataMerger.data.joins.JoinModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;



public class JoinFunctionsModel implements java.io.Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3839711221783954124L;
	private JoinModel joinModel;
	private MergeModel mergeModel;
	private String joinAsDecoratedXHTMLTable;

	public JoinFunctionsModel() {
		
		this.setJoinModel(new JoinModel());
		
	}

	public void setJoinModel(JoinModel joinModel) {
		this.joinModel = joinModel;
	}

	public JoinModel getJoinModel() {
		return this.joinModel;
	}

	public void setMergeModel(MergeModel mergeModel) {
		this.mergeModel = mergeModel;
	}

	public MergeModel getMergeModel() {
		return this.mergeModel;
	}
	
	
	
	public void setJoinAsDecoratedXHTMLTable(String joinAsDecoratedXHTMLTable) {
		this.joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable;
	}

	public String getJoinAsDecoratedXHTMLTable() {
		return this.joinAsDecoratedXHTMLTable;
	}
	
	public void setJoinAsDecoratedXHTMLTableByJoinModel () {
		
		String joinAsDecoratedXHTMLTable = "";
		
		joinAsDecoratedXHTMLTable += "<table class=\"join-table\">";
		
		joinAsDecoratedXHTMLTable += "<thead>";
		
		joinAsDecoratedXHTMLTable += "<tr>";
		joinAsDecoratedXHTMLTable += "	<th><!-- Above key label --></th>";
		joinAsDecoratedXHTMLTable += "	<th><!-- Above key checkbox --></th>";
		joinAsDecoratedXHTMLTable += "	<th class=\"file-link-container\"><a href=\"/dataMerger/files/uploads/" + this.getMergeModel().getUpload1Model().getId() + "\">" + this.getMergeModel().getUpload1Model().getOriginalFilename() + "</a></th>";
		joinAsDecoratedXHTMLTable += "	<th class=\"file-link-container\"><a href=\"/dataMerger/files/uploads/" + this.getMergeModel().getUpload2Model().getId() + "\">" + this.getMergeModel().getUpload2Model().getOriginalFilename() + "</a></th>";
		joinAsDecoratedXHTMLTable += "	<th class=\"column_name-heading\">Column name</th>";
		joinAsDecoratedXHTMLTable += "	<th><!-- Above insert column selector --></th>";
		joinAsDecoratedXHTMLTable += "	<th><!-- Above add column selector --></th>";
			
		joinAsDecoratedXHTMLTable += "</tr>";
		
		joinAsDecoratedXHTMLTable += "</thead>";
		joinAsDecoratedXHTMLTable += "<tbody>";
		
		joinAsDecoratedXHTMLTable += "<tr>";
		joinAsDecoratedXHTMLTable += "	<th class=\"key-heading\">Key?</th>";
		joinAsDecoratedXHTMLTable += "	<td><input type=\"checkbox\" name=\"key\" value=\"false\" disabled=\"disabled\"/></td>";

		joinAsDecoratedXHTMLTable += "	<td class=\"datatable_1_column_name-container\">";
		
		joinAsDecoratedXHTMLTable += "	<select name=\"datatable_1_column_name\">";

		joinAsDecoratedXHTMLTable += "<option selected=\"selected\" value=\"NULL\">NULL</option>";
		joinAsDecoratedXHTMLTable += "<option value=\"CONSTANT\">CONSTANT</option>";

		//TODO: Increase efficiency here.
		for (int i = 0; i < this.getMergeModel().getDatatable1Model().getColumnNamesAsStringList().size(); i++) {
			joinAsDecoratedXHTMLTable += "<option value=\"" + this.getMergeModel().getDatatable1Model().getColumnNamesAsStringList().get(i) + "\">" + this.getMergeModel().getDatatable1Model().getColumnNamesAsStringList().get(i) + "</option>";
		}
		joinAsDecoratedXHTMLTable += "	</select>";
		
		//TODO
		//joinAsDecoratedXHTMLTable += "	<textarea readonly=\"readonly\">[TODO: data sample]</textarea>";
		
		joinAsDecoratedXHTMLTable += "	</td>";
		
		joinAsDecoratedXHTMLTable += "	<td class=\"datatable_2_column_name-container\">";
		
		joinAsDecoratedXHTMLTable += "	<select name=\"datatable_2_column_name\">";
		
		joinAsDecoratedXHTMLTable += "<option selected=\"selected\" value=\"NULL\">NULL</option>";
		joinAsDecoratedXHTMLTable += "<option value=\"CONSTANT\">CONSTANT</option>";
		
		//TODO: Increase efficiency here.
		for (int i = 0; i < this.getMergeModel().getDatatable2Model().getColumnNamesAsStringList().size(); i++) {
			joinAsDecoratedXHTMLTable += "<option value=\"" + this.getMergeModel().getDatatable2Model().getColumnNamesAsStringList().get(i) + "\">" + this.getMergeModel().getDatatable2Model().getColumnNamesAsStringList().get(i) + "</option>";
		}
		joinAsDecoratedXHTMLTable += "	</select>";
		
		//TODO
		//joinAsDecoratedXHTMLTable += "	<textarea readonly=\"readonly\">[TODO: data sample]</textarea>";
		
		joinAsDecoratedXHTMLTable += "	</td>";
		
		joinAsDecoratedXHTMLTable += "	<td class=\"column_name-container\"><input name=\"column_name\" value=\"\"/></td>";
		
		
		joinAsDecoratedXHTMLTable += "	<td class=\"column_number-container\"><select name=\"column_number\">";
		//TODO: Increase efficiency here.
		
		
		for (int i = 1; i <= this.getMergeModel().getJoinsAsCachedRowSet().size() + 1; i++) {
			if (i == 1) {
				joinAsDecoratedXHTMLTable += "<option value=\"" + i + "\">place first</option>";
			}
			else if (i == this.getMergeModel().getJoinsAsCachedRowSet().size() + 1) {
				joinAsDecoratedXHTMLTable += "<option selected=\"selected\" value=\"" + i + "\">place last</option>";
			} else {
				joinAsDecoratedXHTMLTable += "<option value=\"" + i + "\">" + (i-1) + " --&gt; &lt;-- " + i + "</option>";
			}
			
		}
		joinAsDecoratedXHTMLTable += "</select></td>";
		
		
		joinAsDecoratedXHTMLTable += "	<td class=\"add-button-container\"><button class=\"add-join\">Add</button></td>";
		
		joinAsDecoratedXHTMLTable += "</tr>";

		joinAsDecoratedXHTMLTable += "<tr>";
		joinAsDecoratedXHTMLTable += "	<td><!-- Below key label --></td>";
		joinAsDecoratedXHTMLTable += "	<td><!-- Below key checkbox --></td>";
		joinAsDecoratedXHTMLTable += "	<td class=\"constant_1-container\"><label for=\"constant_1\" style=\"display:none;\">Constant:</label><input name=\"constant_1\" value=\"\"/ style=\"display:none;\"></td>";
		joinAsDecoratedXHTMLTable += "	<td class=\"constant_2-container\"><label for=\"constant_2\" style=\"display:none;\">Constant:</label><input name=\"constant_2\" value=\"\"/ style=\"display:none;\"></td>";
		joinAsDecoratedXHTMLTable += "	<td class=\"below-column_name\"><!-- Below column name --></td>";
		joinAsDecoratedXHTMLTable += "	<td><!-- Below insertion selector --></td>";
		joinAsDecoratedXHTMLTable += "	<td><!-- Below add button --></td>";
		joinAsDecoratedXHTMLTable += "</tr>";
		
		joinAsDecoratedXHTMLTable += "</tbody>";
		
		joinAsDecoratedXHTMLTable += "</table>";
		
		
		this.setJoinAsDecoratedXHTMLTable(joinAsDecoratedXHTMLTable);
	}




	
}
