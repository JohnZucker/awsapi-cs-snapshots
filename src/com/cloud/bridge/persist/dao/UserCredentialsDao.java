/*
 * Copyright (C) 2011 Citrix Systems, Inc.  All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloud.bridge.persist.dao;

import java.sql.*;

import org.apache.log4j.Logger;

import com.cloud.bridge.model.UserCredentials;
import com.cloud.bridge.service.exception.NoSuchObjectException;


public class UserCredentialsDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(UserCredentialsDao.class);

	private Connection conn       = null;
	
	public UserCredentialsDao() {
	}
	
	public void setUserKeys( String cloudAccessKey, String cloudSecretKey ) 
	    throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		UserCredentials user = getByAccessKey( cloudAccessKey );
		PreparedStatement statement = null;
	
	    openConnection();	
        try {
		    if ( null == user ) {
			     // -> do an insert since the user does not exist yet
		         statement = conn.prepareStatement ( "INSERT INTO usercredentials (AccessKey, SecretKey) VALUES(?,?)" );
		         statement.setString( 1, cloudAccessKey );
		         statement.setString( 2, cloudSecretKey );
		    }
		    else {
			     // -> do an update since the user exists
			     statement = conn.prepareStatement ( "UPDATE usercredentials SET SecretKey=? WHERE AccessKey=?" );
		         statement.setString( 1, cloudSecretKey );
		         statement.setString( 2, cloudAccessKey );
		    }
	        int count = statement.executeUpdate();
	        statement.close();		    

	    } finally {
            closeConnection();
	    }
	}

	public void setCertificateId( String cloudAccessKey, String certId ) 
        throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
	    UserCredentials user = getByAccessKey( cloudAccessKey );
	    PreparedStatement statement = null;

	    if (null == user) throw new NoSuchObjectException( "Cloud API Access Key [" + cloudAccessKey + "] is unknown" );
	    
        openConnection();	
        try {
		    statement = conn.prepareStatement ( "UPDATE usercredentials SET CertUniqueId=? WHERE AccessKey=?" );
	        statement.setString( 1, certId );
	        statement.setString( 2, cloudAccessKey );
            int count = statement.executeUpdate();
            statement.close();		    
        
        } finally {
            closeConnection();
        }
    }

	public UserCredentials getByAccessKey( String cloudAccessKey ) 
	    throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		openConnection();
		
		UserCredentials user = null;
		
		try {
		    PreparedStatement statement = conn.prepareStatement ( "SELECT SecretKey, CertUniqueId FROM usercredentials WHERE AccessKey=?" );
		    statement.setString( 1, cloudAccessKey );
		    statement.executeQuery();
		    ResultSet rs = statement.getResultSet ();
		    if (rs.next()) {
		    	user = new UserCredentials();
		    	user.setAccessKey( cloudAccessKey );
		        user.setSecretKey( rs.getString( "SecretKey" ));
		        user.setCertUniqueId( rs.getString( "CertUniqueId" ));
		    }

		} finally {
             closeConnection();
		}	
		return user;
	}

	public UserCredentials getByCertUniqueId( String certId ) 
        throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
	    openConnection();
	
	    UserCredentials user = null;
	
	    try {
	        PreparedStatement statement = conn.prepareStatement ( "SELECT AccessKey, SecretKey FROM usercredentials WHERE CertUniqueId=?" );
	        statement.setString( 1, certId );
	        statement.executeQuery();
	        ResultSet rs = statement.getResultSet ();
	        if (rs.next()) {
	    	    user = new UserCredentials();
	    	    user.setAccessKey( rs.getString( "AccessKey" ));
	            user.setSecretKey( rs.getString( "SecretKey" ));
	            user.setCertUniqueId( certId );
	        }

	    } finally {
            closeConnection();
	    }	
	    return user;
    }

	private void openConnection() 
	    throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        if (null == conn) {
		    Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
            conn = DriverManager.getConnection( "jdbc:mysql://" + dbHost + "/" + awsapi_dbName, dbUser, dbPassword );
        }
	}
	
	private void closeConnection() throws SQLException {
		if (null != conn) conn.close();
		conn = null;
	}

	public static void preCheckTableExistence() throws Exception{
		UserCredentialsDao dao = new UserCredentialsDao();
		dao.checkTableExistence();
	}

	private void checkTableExistence() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
	    openConnection();
	
	    try {
	        PreparedStatement statement = conn.prepareStatement ( "SELECT * FROM usercredentials " );
	        statement.executeQuery();
	        ResultSet rs = statement.getResultSet ();
	        if (rs.next()) {
	        	return;
	        }
	        return;

	    } catch(Exception e) {
	    	Statement statement = conn.createStatement();
	    	statement.execute( "create table usercredentials(id integer auto_increment primary key, AccessKey varchar(1000), SecretKey varchar(1000), CertUniqueId varchar(1000))" );
	    	statement.close();
	    }
	    finally{
	    	closeConnection();
	    }
	}
}
