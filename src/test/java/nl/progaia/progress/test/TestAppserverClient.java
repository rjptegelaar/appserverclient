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
package nl.progaia.progress.test;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import nl.progaia.progress.client.AppserverClient;
import nl.progaia.progress.client.Mapper;
import nl.progaia.progress.client.ParameterModeType;
import nl.progaia.progress.client.ParameterType;
import nl.progaia.progress.client.Procedure;
import nl.progaia.progress.exception.AppserverClientException;
import nl.progaia.progress.valueholder.StringHolder;
import nl.progaia.progress.valueholder.ValueHolder;

import org.junit.Before;
import org.junit.Test;

import com.progress.open4gl.javaproxy.ParamArray;

public class TestAppserverClient {

	private static final Logger logger = Logger.getAnonymousLogger();
	private AppserverClient ac;
	private Procedure procedure;
	
	@Before
	public void init() {
		logger.info("Start init.");
		ac = new AppserverClient("AppServer://localhost:5162/esbbroker1", "user1", "", "esbbroker1", "", 1);
		procedure = new Procedure("test.p");
		procedure.addParameter(ParameterModeType.INPUT, ParameterType.STRING);
		procedure.addParameter(ParameterModeType.OUTPUT, ParameterType.STRING);
		logger.info("Done init.");		
	}
	
	@Test
	public void testCall() throws AppserverClientException{
		ValueHolder<String> inputHolder = new StringHolder("toch?");
		ValueHolder<String> outputHolder = new StringHolder();
		Map<Integer, ValueHolder<?>> values = new HashMap<Integer, ValueHolder<?>>();
		values.put(0, inputHolder);
		values.put(1, outputHolder);
		ParamArray output = ac.callProcedure(Mapper.from(procedure, values),procedure);
		Map<Integer, ValueHolder<?>> returnValues = Mapper.from(procedure, output);
		System.out.println(((ValueHolder<String>)returnValues.get(1)).getValue());
	}

}
