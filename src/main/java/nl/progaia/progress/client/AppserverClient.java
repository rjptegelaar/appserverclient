//Copyright 2014 Paul Tegelaar
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
package nl.progaia.progress.client;

import nl.progaia.progress.exception.AppserverClientException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progress.open4gl.javaproxy.Connection;
import com.progress.open4gl.javaproxy.OpenAppObject;
import com.progress.open4gl.javaproxy.ParamArray;

public class AppserverClient {
	private static final Logger logger = LoggerFactory.getLogger(AppserverClient.class);
	
	private String appserverUrl;
	private String username;
	private String password;
	private String appServerInfo;
	private String service;
	private int session;
	private Connection connection;
	private OpenAppObject openAppObject;
	
	public AppserverClient(){
		
	}
	
	public AppserverClient(String appserverUrl, String username, String password, String appServerInfo, String service, int session){
		this.appServerInfo = appServerInfo;
		this.appserverUrl = appserverUrl;
		this.username = username;
		this.password = password;
		this.service = service;
		this.session = session;
	}
	
	public ParamArray callProcedure(ParamArray paramArray, Procedure procedure) throws AppserverClientException{
		try {
			connect();
			if(openAppObject!=null){
				openAppObject.runProc(procedure.getName(), paramArray);	
				return paramArray;
			}else{
				throw new AppserverClientException("Connection to appserver not established.");
			}								
		} catch (Exception e) {
			throw new AppserverClientException(e);
		}finally{
			try {
				disconnect();
			} catch (Exception e) {
				logger.warn(e.getMessage());
				if(logger.isDebugEnabled())
					e.printStackTrace();
			} 
		}
	}
	
	private void connect() throws AppserverClientException{
		
		try {
			
			logger.info("Connecting to appserver.");
			connection = new Connection(appserverUrl, username, password, appServerInfo);
			connection.setSessionModel(session);
			openAppObject = new OpenAppObject(connection, service);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new AppserverClientException(e);
		}
	}	
	
	
	private void disconnect() throws AppserverClientException{
		try {
			logger.info("Disconnecting from appserver.");
			if (openAppObject != null) {
				openAppObject._release();
				openAppObject = null;
			}
			
			if (connection != null) {
				connection.releaseConnection();
				connection = null;
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new AppserverClientException(e);
		}
	}	
	
}