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

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.progaia.progress.exception.AppserverClientException;
import nl.progaia.progress.valueholder.ValueHolder;

import com.progress.open4gl.Open4GLException;
import com.progress.open4gl.javaproxy.Connection;
import com.progress.open4gl.javaproxy.OpenAppObject;
import com.progress.open4gl.javaproxy.ParamArray;


public class AppserverClient {
	private static final Logger logger = Logger.getAnonymousLogger();

	private String appserverUrl;
	private String username;
	private String password;
	private String appServerInfo;
	private String service;
	private int session;
	private long delay = 0;

	public AppserverClient(String appserverUrl, String username,
			String password, String appServerInfo, String service, int session) {
		this.appServerInfo = appServerInfo;
		this.appserverUrl = appserverUrl;
		this.username = username;
		this.password = password;
		this.service = service;
		this.session = session;
	}

	public synchronized ParamArray callProcedure(ParamArray paramArray,
			Procedure procedure) throws AppserverClientException {
		
		Connection connection = null;
		OpenAppObject openAppObject = null;
		
		try {

			connection = new Connection(appserverUrl, username, password,
					appServerInfo);
			connection.setSessionModel(session);								
			openAppObject = new OpenAppObject(connection, service);
			
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Calling procedure: " + procedure.getName());
				logger.fine(procedure.toString());
			}
			openAppObject.runProc(procedure.getName(), paramArray);
			
			//Check is sleep is enabled
			if(delay>0){
				Thread.sleep(5);	
			}
			
									
			return paramArray;
		} catch (Exception e) {
			logger.severe(e.getMessage());
			throw new AppserverClientException(e);
		} finally {
			try {				
				connection.releaseConnection();				
				connection = null;
				openAppObject._release();
				openAppObject = null;
			} catch (Open4GLException e) {
				logger.severe(e.getMessage());
				throw new AppserverClientException(e);
			}
		}

	}
		

	public synchronized Map<Integer, ValueHolder<?>> callProcedure(
			Map<Integer, ValueHolder<?>> values, Procedure procedure)
			throws AppserverClientException {
		ParamArray paramArray = Mapper.from(procedure, values);
		return Mapper.from(procedure, callProcedure(paramArray, procedure));
	}
	

	public String getAppserverUrl() {
		return appserverUrl;
	}

	public void setAppserverUrl(String appserverUrl) {
		this.appserverUrl = appserverUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAppServerInfo() {
		return appServerInfo;
	}

	public void setAppServerInfo(String appServerInfo) {
		this.appServerInfo = appServerInfo;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	
	
	

}
