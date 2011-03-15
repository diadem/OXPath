/*
 * Copyright (c)2011, DIADEM Team
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the DIADEM team nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DIADEM Team BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * 
 */
package uk.ac.ox.comlab.oxpath;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * when a database is used, these methods help process the queries
 * @author AndrewJSel
 *
 */
public class DatabaseHelper {
	
	/**
	 * creates new DatabaseHelper instance
	 */
	public DatabaseHelper() {}
	
	/**
	 * used to input database data, establish connection, and 
	 * @param inputFile name of file containing database (formatted for Properties class)
	 * @return connection to the database
	 */
	public Connection getNewConnection(String inputFile) throws IOException, ClassNotFoundException, SQLException {
		//load the input file into a Properties object for processing
		Properties pr = new Properties();
		FileReader in = new FileReader(inputFile);
		pr.load(in);
		
		//get the desired properties
		String driver = pr.getProperty("jdbc.driver");
		String url = pr.getProperty("jdbc.url");
		String username = pr.getProperty("jdbc.username");
		String password = pr.getProperty("jdbc.password");
		if (driver != null) Class.forName(driver);
		if (username == null) username="";
		if (password == null) password="";
		
		//establish a connection
		return DriverManager.getConnection(url, username, password);
		
	}

}
