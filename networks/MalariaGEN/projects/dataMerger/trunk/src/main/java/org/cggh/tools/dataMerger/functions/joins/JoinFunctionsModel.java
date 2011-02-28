package org.cggh.tools.dataMerger.functions.joins;


import java.sql.SQLException;

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
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<table class=\"new column\">");
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<thead>");
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<tr>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<th><!-- Above key label --></th>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<th><!-- Above key checkbox --></th>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<th colspan=\"2\"><a href=\"/dataMerger/files/uploads?id=" + this.getMergeModel().getUpload1Model().getId() + "\">" + this.getMergeModel().getUpload1Model().getOriginalFilename() + "</a></th>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<th colspan=\"2\"><a href=\"/dataMerger/files/uploads?id=" + this.getMergeModel().getUpload2Model().getId() + "\">" + this.getMergeModel().getUpload2Model().getOriginalFilename() + "</a></th>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<th>Column name</th>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<th><!-- Above insert column selector --></th>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<th><!-- Above add column selector --></th>");
			
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("</tr>");
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("</thead>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<tbody>");
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<tr>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<th>Key?</th>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><input type=\"checkbox\" name=\"key\" value=\"false\"/></td>");

		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><select name=\"datatable_1_column_name\">");

		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<option selected=\"selected\" value=\"NULL\">NULL</option>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<option value=\"CONSTANT\">CONSTANT</option>");

		//TODO: Increase efficiency here.
		for (int i = 0; i < this.getMergeModel().getDatatable1Model().getColumnNamesAsStringList().size(); i++) {
			joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<option value=\"" + this.getMergeModel().getDatatable1Model().getColumnNamesAsStringList().get(i) + "\">" + this.getMergeModel().getDatatable1Model().getColumnNamesAsStringList().get(i) + "</option>");
		}
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	</select></td>");
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td>[TODO: data sample]</td>");
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><select name=\"datatable_2_column_name\">");
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<option selected=\"selected\" value=\"NULL\">NULL</option>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<option value=\"CONSTANT\">CONSTANT</option>");
		
		//TODO: Increase efficiency here.
		for (int i = 0; i < this.getMergeModel().getDatatable2Model().getColumnNamesAsStringList().size(); i++) {
			joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<option value=\"" + this.getMergeModel().getDatatable2Model().getColumnNamesAsStringList().get(i) + "\">" + this.getMergeModel().getDatatable2Model().getColumnNamesAsStringList().get(i) + "</option>");
		}
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	</select></td>");
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td>[TODO: data sample]</td>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><input name=\"column_name\" value=\"\"/></td>");
		
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><select name=\"column_number\">");
		//TODO: Increase efficiency here.
		
		
		for (int i = 1; i <= this.getMergeModel().getJoinsAsCachedRowSet().size() + 1; i++) {
			if (i == 1) {
				joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<option value=\"" + i + "\">place first</option>");
			}
			else if (i == this.getMergeModel().getJoinsAsCachedRowSet().size() + 1) {
				joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<option selected=\"selected\" value=\"" + i + "\">place last</option>");
			} else {
				joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<option value=\"" + i + "\">" + (i-1) + " --&gt; &lt;-- " + i + "</option>");
			}
			
		}
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("</select></td>");
		
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><button class=\"add join\">Add</button></td>");
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("</tr>");
		//<!-- TODO: Only if choose constant from selector -->
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("<tr>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><!-- Below key label --></td>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><!-- Below key checkbox --></td>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<th><label for=\"constant_1\">Constant:</label></th>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><input name=\"constant_1\" value=\"\"/></td>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<th><label for=\"constant_2\">Constant:</label></th>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><input name=\"constant_2\" value=\"\"/></td>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><!-- Below column name --></td>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><!-- Below insertion selector --></td>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("	<td><!-- Below add button --></td>");
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("</tr>");
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("</tbody>");
		
		joinAsDecoratedXHTMLTable = joinAsDecoratedXHTMLTable.concat("</table>");
		
		
		this.setJoinAsDecoratedXHTMLTable(joinAsDecoratedXHTMLTable);
	}




	
}
