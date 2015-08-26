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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import nl.progaia.progress.exception.AppserverClientException;
import nl.progaia.progress.valueholder.BooleanHolder;
import nl.progaia.progress.valueholder.DateHolder;
import nl.progaia.progress.valueholder.DecimalHolder;
import nl.progaia.progress.valueholder.IntegerHolder;
import nl.progaia.progress.valueholder.LongHolder;
import nl.progaia.progress.valueholder.MemptrHolder;
import nl.progaia.progress.valueholder.StringHolder;
import nl.progaia.progress.valueholder.ValueHolder;

import com.progress.open4gl.Memptr;
import com.progress.open4gl.Open4GLException;
import com.progress.open4gl.javaproxy.ParamArray;
import com.progress.open4gl.javaproxy.ParamArrayMode;

public final class Mapper {

	private static final Logger logger = Logger.getAnonymousLogger();
	
	public static Map<Integer, ValueHolder<?>> from(Procedure procedure, ParamArray paramArray) throws AppserverClientException{
		try {
			Map<Integer, ValueHolder<?>> valueMap = new HashMap<Integer, ValueHolder<?>>();
			Map<Integer, Parameter> paramMap = procedure.getParameters();
			Set<Integer> paramIndexSet = paramMap.keySet();
			for (Integer index : paramIndexSet) {
				if(paramMap.get(index).getInputOuputType()==ParameterModeType.INPUT_OUTPUT || paramMap.get(index).getInputOuputType()==ParameterModeType.OUTPUT)
					valueMap.put(index, getValueHolder(paramMap.get(index).getDataType(), paramArray.getOutputParameter(index)));	
			}		
			print(valueMap);
			return valueMap;
		} catch (Open4GLException e) {
			throw new AppserverClientException(e);
		}
	}
	
	public static ParamArray from(Procedure procedure, Map<Integer, ValueHolder<?>> values) throws AppserverClientException{
		try {		
			print(values);
			ParamArray paramArray = new ParamArray(procedure.size());
			Map<Integer, Parameter> paramMap = procedure.getParameters();
			Set<Integer> paramIndexSet = paramMap.keySet();
			if(values!=null && values.size()>0){
				for (Integer index : paramIndexSet) {	
					if(values.get(index)!=null){
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
				}

			}else{
				logger.info("No values to map to valueholer.");
			}
		
			
			
			return paramArray;
		} catch (Open4GLException e) {
			throw new AppserverClientException(e);
		}
	}

	public static ValueHolder<?> getValueHolder(ParameterType type, Object value) throws AppserverClientException{
		
		switch(type){
					case STRING:
						if(value!=null){
							if(value instanceof Character){							 
								 return new StringHolder(String.valueOf((Character)value));
							}else if(value instanceof String){
								return new StringHolder((String)value);
							}
						}
						return new StringHolder();	
					case BLOB:															
						if(value!=null){
							if(value instanceof byte[]){				
								return new MemptrHolder(new Memptr((byte[])value));
							}else if(value instanceof Memptr){					
								return new MemptrHolder((Memptr)value);							
							}else{
								throw new AppserverClientException("Cannot convert value to Memptr.");
							}
						}
						return new MemptrHolder();
					case BOOLEAN:
						if(value!=null){
								if(value instanceof Boolean){	
									return new BooleanHolder((Boolean) value);									
								}else{
									throw new AppserverClientException("Cannot convert value to Boolean.");
								}
							}
						return  new BooleanHolder();
					case CLOB:
						if(value!=null){
							if(value instanceof String){			
								return new StringHolder((String)value);
							}else{
								throw new AppserverClientException("Cannot convert value to String.");
							}
						}
						return new StringHolder();
					case DATE:
						if(value!=null){
							return resolveDate(value);							
						}
						return new DateHolder();												
					case DATETIME:
						if(value!=null){
							return resolveDate(value);							
						}
						return new DateHolder();																	
					case DATETIMETZ:
						if(value!=null){
							return resolveDate(value);							
						}
						return new DateHolder();																							
					case DECIMAL:
						if(value!=null){
							if(value instanceof BigDecimal){		
								return new DecimalHolder((BigDecimal) value);
							}if(value instanceof Double){
								return new DecimalHolder((BigDecimal) BigDecimal.valueOf((Double)value));
							}if(value instanceof Float){
								return new DecimalHolder((BigDecimal) BigDecimal.valueOf((Float)value));
							}else{
								throw new AppserverClientException("Cannot convert value to BigDecimal.");
							}
						}
						return new DecimalHolder();	
					case INT:
						if(value!=null){
							if(value instanceof Integer){		
								return new IntegerHolder((Integer)value);
							}else if(value instanceof Short){		
								int shortTemp = ((Short)value).intValue();
								return new IntegerHolder(shortTemp);
							}else if(value instanceof Byte){		
								int byteTemp = ((Byte)value).intValue();
								return new IntegerHolder(byteTemp);
							}else{
								throw new AppserverClientException("Cannot convert value to Integer.");
							}
						}
						return  new IntegerHolder();							
					case LONG:
						if(value!=null){
							if(value instanceof Long){		
								return new LongHolder((Long)value);
							}else{
								throw new AppserverClientException("Cannot convert value to Long.");
							}
						}
						return new LongHolder();

					default:
						logger.warning("No mapping for: " + type);
						return null;											
					}
	}

	private static ValueHolder<?> resolveDate(Object value)
			throws AppserverClientException {
		if(value instanceof GregorianCalendar){				
			return new DateHolder((GregorianCalendar) value);
		}else if(value instanceof Date){				
			GregorianCalendar calendar = new GregorianCalendar ();
			calendar.setTime(((Date) value));
			return new DateHolder(calendar);
		}else if(value instanceof Long){				
			GregorianCalendar calendar = new GregorianCalendar ();
			calendar.setTime((new Date((Long)value)));
			return new DateHolder(calendar);
		}else{
			throw new AppserverClientException("Cannot convert value to GregorianCalendar.");
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
	
	public static void print(Map<Integer, ValueHolder<?>> values){
		StringBuffer valueHolderSet = new StringBuffer();
		Set<Integer> keys = values.keySet();
		for (Integer index : keys) {
			valueHolderSet.append("Index: ").append(index).append(", Value: ").append(values.get(index).toString()).append("\n");
		}
		logger.info(valueHolderSet.toString());
	} 
			
}
