package org.cggh.tools.dataMerger.functions.data.joins;


import org.cggh.tools.dataMerger.data.joins.JoinModel;
import org.cggh.tools.dataMerger.data.merges.MergeModel;



public class JoinFunctions implements java.io.Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3839711221783954124L;
	private JoinModel joinModel;
	private MergeModel mergeModel;
	private String joinAsDecoratedXHTMLTable;

	public JoinFunctions() {
		
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
		
		StringBuffer joinAsDecoratedXHTMLTable = new StringBuffer();
		
		joinAsDecoratedXHTMLTable.append("<table class=\"join-table\">");
		
		joinAsDecoratedXHTMLTable.append("<thead>");
		
		joinAsDecoratedXHTMLTable.append("<tr>");
		joinAsDecoratedXHTMLTable.append("	<th><!-- Above key label --></th>");
		joinAsDecoratedXHTMLTable.append("	<th><!-- Above key checkbox --></th>");
		joinAsDecoratedXHTMLTable.append("	<th class=\"file-link-container\"><a href=\"/dataMerger/files/").append(this.getMergeModel().getFile1Model().getId()).append("\">").append(this.getMergeModel().getFile1Model().getFilename()).append("</a></th>");
		joinAsDecoratedXHTMLTable.append("	<th class=\"file-link-container\"><a href=\"/dataMerger/files/").append(this.getMergeModel().getFile2Model().getId()).append("\">").append(this.getMergeModel().getFile2Model().getFilename()).append("</a></th>");
		joinAsDecoratedXHTMLTable.append("	<th class=\"column_name-heading\">Column name</th>");
		joinAsDecoratedXHTMLTable.append("	<th><!-- Above insert column selector --></th>");
		joinAsDecoratedXHTMLTable.append("	<th><!-- Above add column selector --></th>");
			
		joinAsDecoratedXHTMLTable.append("</tr>");
		
		joinAsDecoratedXHTMLTable.append("</thead>");
		joinAsDecoratedXHTMLTable.append("<tbody>");
		
		joinAsDecoratedXHTMLTable.append("<tr>");
		joinAsDecoratedXHTMLTable.append("	<th class=\"key-heading\">Key?</th>");
		joinAsDecoratedXHTMLTable.append("	<td><input type=\"checkbox\" name=\"key\" value=\"false\" disabled=\"disabled\"/></td>");

		joinAsDecoratedXHTMLTable.append("	<td class=\"datatable_1_column_name-container\">");
		
		joinAsDecoratedXHTMLTable.append("	<select name=\"datatable_1_column_name\">");

		joinAsDecoratedXHTMLTable.append("<option selected=\"selected\" value=\"NULL\">NULL</option>");
		joinAsDecoratedXHTMLTable.append("<option value=\"CONSTANT\">CONSTANT</option>");

		//TODO: Increase efficiency here.
		for (int i = 0; i < this.getMergeModel().getDatatable1Model().getColumnNamesAsStringList().size(); i++) {
			joinAsDecoratedXHTMLTable.append("<option value=\"").append(this.getMergeModel().getDatatable1Model().getColumnNamesAsStringList().get(i)).append("\">").append(this.getMergeModel().getDatatable1Model().getColumnNamesAsStringList().get(i)).append("</option>");
		}
		joinAsDecoratedXHTMLTable.append("	</select>");
		
		//TODO
		//joinAsDecoratedXHTMLTable.append("	<textarea readonly=\"readonly\">[TODO: data sample]</textarea>");
		
		joinAsDecoratedXHTMLTable.append("	</td>");
		
		joinAsDecoratedXHTMLTable.append("	<td class=\"datatable_2_column_name-container\">");
		
		joinAsDecoratedXHTMLTable.append("	<select name=\"datatable_2_column_name\">");
		
		joinAsDecoratedXHTMLTable.append("<option selected=\"selected\" value=\"NULL\">NULL</option>");
		joinAsDecoratedXHTMLTable.append("<option value=\"CONSTANT\">CONSTANT</option>");
		
		//TODO: Increase efficiency here.
		for (int i = 0; i < this.getMergeModel().getDatatable2Model().getColumnNamesAsStringList().size(); i++) {
			joinAsDecoratedXHTMLTable.append("<option value=\"").append(this.getMergeModel().getDatatable2Model().getColumnNamesAsStringList().get(i)).append("\">").append(this.getMergeModel().getDatatable2Model().getColumnNamesAsStringList().get(i)).append("</option>");
		}
		joinAsDecoratedXHTMLTable.append("	</select>");
		
		//TODO
		//joinAsDecoratedXHTMLTable.append("	<textarea readonly=\"readonly\">[TODO: data sample]</textarea>");
		
		joinAsDecoratedXHTMLTable.append("	</td>");
		
		joinAsDecoratedXHTMLTable.append("	<td class=\"column_name-container\"><input name=\"column_name\" value=\"\"/></td>");
		
		
		joinAsDecoratedXHTMLTable.append("	<td class=\"column_number-container\"><select name=\"column_number\">");
		//TODO: Increase efficiency here.
		
		
		for (int i = 1; i <= this.getMergeModel().getJoinsAsCachedRowSet().size() + 1; i++) {
			if (i == 1) {
				joinAsDecoratedXHTMLTable.append("<option value=\"").append(i).append("\">place first</option>");
			}
			else if (i == this.getMergeModel().getJoinsAsCachedRowSet().size() + 1) {
				joinAsDecoratedXHTMLTable.append("<option selected=\"selected\" value=\"").append(i).append("\">place last</option>");
			} else {
				joinAsDecoratedXHTMLTable.append("<option value=\"").append(i).append("\">").append((i-1)).append(" --&gt; &lt;-- ").append(i).append("</option>");
			}
			
		}
		joinAsDecoratedXHTMLTable.append("</select></td>");
		
		
		joinAsDecoratedXHTMLTable.append("	<td class=\"add-button-container\"><button class=\"add-join\">Add</button></td>");
		
		joinAsDecoratedXHTMLTable.append("</tr>");

		joinAsDecoratedXHTMLTable.append("<tr>");
		joinAsDecoratedXHTMLTable.append("	<td><!-- Below key label --></td>");
		joinAsDecoratedXHTMLTable.append("	<td><!-- Below key checkbox --></td>");
		joinAsDecoratedXHTMLTable.append("	<td class=\"constant_1-container\"><label for=\"constant_1\" style=\"display:none;\">Constant:</label><input name=\"constant_1\" value=\"\"/ style=\"display:none;\"></td>");
		joinAsDecoratedXHTMLTable.append("	<td class=\"constant_2-container\"><label for=\"constant_2\" style=\"display:none;\">Constant:</label><input name=\"constant_2\" value=\"\"/ style=\"display:none;\"></td>");
		joinAsDecoratedXHTMLTable.append("	<td class=\"below-column_name\"><!-- Below column name --></td>");
		joinAsDecoratedXHTMLTable.append("	<td><!-- Below insertion selector --></td>");
		joinAsDecoratedXHTMLTable.append("	<td><!-- Below add button --></td>");
		joinAsDecoratedXHTMLTable.append("</tr>");
		
		joinAsDecoratedXHTMLTable.append("</tbody>");
		
		joinAsDecoratedXHTMLTable.append("</table>");
		
		
		this.setJoinAsDecoratedXHTMLTable(joinAsDecoratedXHTMLTable.toString());
	}




	
}
