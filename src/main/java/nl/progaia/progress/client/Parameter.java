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

public class Parameter {

	private ParameterModeType inputOuputType;
	private ParameterType dataType;
	private int extent = -1;
	
	public Parameter(ParameterModeType inputOuputType, ParameterType dataType){
		this.inputOuputType = inputOuputType;
		this.dataType = dataType;
	}
	
	public Parameter(ParameterModeType inputOuputType, ParameterType dataType, int extent){
		this.inputOuputType = inputOuputType;
		this.dataType = dataType;
		this.extent = extent;
	}
	
	public ParameterModeType getInputOuputType() {
		return inputOuputType;
	}
	public void setInputOuputType(ParameterModeType inputOuputType) {
		this.inputOuputType = inputOuputType;
	}
	public ParameterType getDataType() {
		return dataType;
	}
	public void setDataType(ParameterType dataType) {
		this.dataType = dataType;
	}
	public int getExtent() {
		return extent;
	}
	public void setExtent(int extent) {
		this.extent = extent;
	}
	
}
