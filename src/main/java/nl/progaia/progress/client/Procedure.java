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

import java.util.HashMap;
import java.util.Map;

public class Procedure {
	private Map<Integer, Parameter> parameters;
	private String name;
	
	public Procedure(String name){
		this.name = name;
	}
	
	public Map<Integer, Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(Map<Integer, Parameter> parameters) {
		this.parameters = parameters;
	}
	public int size(){
		if(parameters!=null){
			return parameters.size();
		}else{
			return 0;
		}		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void addParameter(ParameterModeType inputOuputType, ParameterType dataType){		
		if(parameters==null)
			parameters = new HashMap<Integer, Parameter>();
		
		parameters.put(parameters.size(), new Parameter(inputOuputType, dataType));
	}
	
}
