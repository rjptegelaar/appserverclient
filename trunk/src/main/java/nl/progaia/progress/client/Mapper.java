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

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nl.progaia.progress.exception.AppserverClientException;
import nl.progaia.progress.valueholder.DateHolder;
import nl.progaia.progress.valueholder.DecimalHolder;
import nl.progaia.progress.valueholder.IntegerHolder;
import nl.progaia.progress.valueholder.LongHolder;
import nl.progaia.progress.valueholder.MemptrHolder;
import nl.progaia.progress.valueholder.StringHolder;
import nl.progaia.progress.valueholder.ValueHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progress.open4gl.Memptr;
import com.progress.open4gl.Open4GLException;
import com.progress.open4gl.javaproxy.ParamArray;
import com.progress.open4gl.javaproxy.ParamArrayMode;

public final class Mapper {

	private static final Logger logger = LoggerFactory.getLogger(Mapper.class);
	
	public static Map<Integer, ValueHolder<?>> from(Procedure procedure, ParamArray paramArray) throws AppserverClientException{
		try {
			Map<Integer, ValueHolder<?>> valueMap = new HashMap<Integer, ValueHolder<?>>();
			Map<Integer, Parameter> paramMap = procedure.getParameters();
			Set<Integer> paramIndexSet = paramMap.keySet();
			for (Integer index : paramIndexSet) {
				if(paramMap.get(index).getInputOuputType()==ParameterModeType.INPUT_OUTPUT || paramMap.get(index).getInputOuputType()==ParameterModeType.OUTPUT)
					valueMap.put(index, getValueHolder(paramMap.get(index).getDataType(), paramArray.getOutputParameter(index)));	
			}		
			return valueMap;
		} catch (Open4GLException e) {
			throw new AppserverClientException(e);
		}
	}
	
	public static ParamArray from(Procedure procedure, Map<Integer, ValueHolder<?>> values) throws AppserverClientException{
		try {
			ParamArray paramArray = new ParamArray(procedure.size());
			Map<Integer, Parameter> paramMap = procedure.getParameters();
			Set<Integer> paramIndexSet = paramMap.keySet();
			for (Integer index : paramIndexSet) {

					switch(paramMap.get(index).getDataType()){
						case BLOB:
							paramArray.addMemptr(index.intValue(), (Memptr)values.get(index).getValue(),  from(paramMap.get(index).getInputOuputType()));	
							break;
						case BOOLEAN:								
							paramArray.addLogical(index.intValue(), (Boolean)values.get(index).getValue(),  from(paramMap.get(index).getInputOuputType()));
							break;
						case CLOB:
							paramArray.addLongchar(index.intValue(), (String)values.get(index).getValue(),  from(paramMap.get(index).getInputOuputType()));
							break;
						case DATE:
							paramArray.addDate(index.intValue(), (GregorianCalendar)values.get(index).getValue(),  from(paramMap.get(index).getInputOuputType()));
							break;
						case DATETIME:
							paramArray.addDatetime(index.intValue(), (GregorianCalendar)values.get(index).getValue(),  from(paramMap.get(index).getInputOuputType()));
							break;
						case DATETIMETZ:
							paramArray.addDatetimeTZ(index.intValue(), (GregorianCalendar)values.get(index).getValue(),  from(paramMap.get(index).getInputOuputType()));
							break;
						case DECIMAL:
							paramArray.addDecimal(index.intValue(), (BigDecimal)values.get(index).getValue(),  from(paramMap.get(index).getInputOuputType()));
							break;
						case INT:
							paramArray.addInteger(index.intValue(), (Integer)values.get(index).getValue(),  from(paramMap.get(index).getInputOuputType()));
							break;
						case LONG:
							paramArray.addInt64(index.intValue(), (Long)values.get(index).getValue(),  from(paramMap.get(index).getInputOuputType()));
							break;
						case STRING:
							paramArray.addCharacter(index, (String)values.get(index).getValue(), from(paramMap.get(index).getInputOuputType()));
							break;
						default:
							throw new AppserverClientException("Unsupported parameter type: " + paramMap.get(index).getDataType().name());
					
					}
			}
			
			
			return paramArray;
		} catch (Open4GLException e) {
			throw new AppserverClientException(e);
		}
	}

	private static ValueHolder<?> getValueHolder(ParameterType type, Object value) throws AppserverClientException{
		
		switch(type){
					case STRING:
							return new StringHolder((String)value);
					case BLOB:															
						if(value!=null){
							if(value instanceof Memptr){					
								return new MemptrHolder((Memptr)value);							
							}else{
								throw new AppserverClientException("Cannot convert value to Memptr.");
							}
						}
						return null;
					case BOOLEAN:
						if(value!=null){
							if(value instanceof String){	
								return new StringHolder((String) value);	
							}
							}else{
								throw new AppserverClientException("Cannot convert value to Boolean.");
							}
						return null;
					case CLOB:
						if(value!=null){
							if(value instanceof String){			
								return new StringHolder((String)value);
							}else{
								throw new AppserverClientException("Cannot convert value to String.");
							}
						}
						return null;
					case DATE:
						if(value!=null){
							if(value instanceof GregorianCalendar){				
								return new DateHolder((GregorianCalendar) value);
							}else{
								throw new AppserverClientException("Cannot convert value to GregorianCalendar.");
							}							
						}
						return null;												
					case DATETIME:
						if(value!=null){
							if(value instanceof GregorianCalendar){				
								return new DateHolder((GregorianCalendar) value);
							}else{
								throw new AppserverClientException("Cannot convert value to GregorianCalendar.");
							}							
						}
						return null;																	
					case DATETIMETZ:
						if(value!=null){
							if(value instanceof GregorianCalendar){				
								return new DateHolder((GregorianCalendar) value);
							}else{
								throw new AppserverClientException("Cannot convert value to GregorianCalendar.");
							}							
						}
						return null;																							
					case DECIMAL:
						if(value!=null){
							if(value instanceof BigDecimal){		
								return new DecimalHolder((BigDecimal) value);
							}else{
								throw new AppserverClientException("Cannot convert value to BigDecimal.");
							}
						}
						return null;	
					case INT:
						if(value!=null){
							if(value instanceof Integer){		
								return new IntegerHolder((Integer)value);
							}else{
								throw new AppserverClientException("Cannot convert value to Integer.");
							}
						}
						return null;							
					case LONG:
						if(value!=null){
							if(value instanceof Long){		
								return new LongHolder((Long)value);
							}else{
								throw new AppserverClientException("Cannot convert value to Long.");
							}
						}
						return null;

					default:
						logger.warn("No mapping for: " + type);
						return null;											
					}
	}
	
	public static int from(ParameterModeType type){
		switch(type){
		case INPUT:
			return ParamArrayMode.INPUT;
		case OUTPUT:
			return ParamArrayMode.OUTPUT;
		case INPUT_OUTPUT:
			return ParamArrayMode.INPUT_OUTPUT;
		default:
			return -1;
		}
	}
	
	public static Map<Integer, ValueHolder<?>> from(Procedure procedure, Map<Integer, ValueHolder<?>> requestValues, Map<Integer, ValueHolder<?>> responseValues){
		
			Map<Integer, ValueHolder<?>> mergedValues = new HashMap<Integer, ValueHolder<?>>();
			Map<Integer, Parameter> paramMap = procedure.getParameters();
			Set<Integer> paramIndexSet = paramMap.keySet();
			for (Integer index : paramIndexSet) {
				switch(paramMap.get(index).getInputOuputType()){
				case INPUT:
					mergedValues.put(index, requestValues.get(index));
					break;
				case INPUT_OUTPUT:
					mergedValues.put(index, responseValues.get(index));
					break;
				case OUTPUT:
					mergedValues.put(index, responseValues.get(index));
					break;
				}
						
			}		
			return mergedValues;

	}
	
}
