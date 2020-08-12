package com.limitlessmobility.iVendGateway.dao;

import javax.sql.*;

import org.springframework.jdbc.core.*;

import com.limitlessmobility.iVendGateway.paytm.model.TerminalEventModel;

public class TerminalEventLogDao {

	private DataSource datasource;
	private JdbcTemplate jdbcTemplate;
	private String insertSql,updateSql;

	    {
	        insertSql="insert into terminal_event_log values(?,?)";
	        updateSql="update terminal_event_log set terminal_id=?,telemetry_id=?";
	    }

	    public void setDatasource(DataSource datasource)
	    {
	        this.datasource=datasource;
	        jdbcTemplate=new JdbcTemplate(datasource);
	    }

	    public void insert(TerminalEventModel terminalEventLog)
	    {
	        jdbcTemplate.update(insertSql,new Object[]{terminalEventLog.getTerminalId(),terminalEventLog.getTelemetryId()});
	    }

	    public void update(TerminalEventModel terminalEventLog)
	    {
	        jdbcTemplate.update(updateSql,new Object[]{terminalEventLog.getTerminalId(),terminalEventLog.getTelemetryId()});
	    }
	    
	    public static void main(String[] args) {
	    	TerminalEventModel t = new TerminalEventModel();
	    	t.setTerminalId("t1");
	    	t.setTelemetryId("tel1");
			new TerminalEventLogDao().insert(t);
		}
}
