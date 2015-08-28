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
import java.util.Set;

import nl.progaia.progress.exception.AppserverClientException;

public class Procedure {
	private Map<Integer, Parameter> parameters;
	private String name;
	
	public Procedure(String name){
		this.name = name;
		parameters = new HashMap<Integer, Parameter>();
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
	
	public int addParameter(ParameterModeType inputOuputType, ParameterType dataType){		
					
		int size = parameters.size();
		parameters.put(size, new Parameter(inputOuputType, dataType));
		return size;
	}
	
	public Parameter getParameter(int index){
		if(parameters.size()>=index){
			return parameters.get(index);
		}else{
			return null;
		}
		 
	}

	@Override
	public String toString() {
		StringBuffer procedureStructure = new StringBuffer();
		procedureStructure.append("Name: ").append(name).append("\n");
		Set<Integer> keys = parameters.keySet();
		for (Integer index : keys) {			
			procedureStructure.append("Param index: ").append(index);
			procedureStructure.append(", Param datatype: ").append(parameters.get(index).toString());
			procedureStructure.append("\n");
		}				
		return procedureStructure.toString();
	}
	
	public void check() throws AppserverClientException{
		int in = 0;
		int out = 0;
		Set<Integer> keys = parameters.keySet();
		for (Integer index : keys) {			
			switch(parameters.get(index).getInputOuputType()){
			case INPUT:
				in = 1;
				break;
			case OUTPUT:
				out = 1;
				break;
			case INPUT_OUTPUT:
				in = 1;
				out = 1;
				break;			
			}
			
			if(in==1 && out==1){
				//No need to check further, everything OK
				break;
			}
		}
		
		if(in!=1 || out!=1){
			throw new AppserverClientException("Procedure must have at least one input and one output parameter or at least one input-output parameter");
		}
		
	}
	
}
