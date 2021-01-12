package com.yura.mbom.service;

import java.util.HashMap;
import java.util.Properties;

import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class yuraSapMyDestinationDataProvider implements DestinationDataProvider {
	private HashMap<String, Properties> secureDBStorage = new HashMap<String, Properties>();
	private DestinationDataEventListener eventListener;

	@Override
	public Properties getDestinationProperties(String destinationName){
        try{
            //read the destination from DB
            Properties properties = secureDBStorage.get(destinationName);

            if(properties!=null){
                //check if all is correct, for example
                if(properties.isEmpty()){
                    throw new DataProviderException(DataProviderException.Reason.INVALID_CONFIGURATION, "destination configuration is incorrect", null);
                }
                return properties;
            }

            return null;
        }catch(RuntimeException re){
            throw new DataProviderException(DataProviderException.Reason.INTERNAL_ERROR, re);
        }
    }

	@Override
	public void setDestinationDataEventListener(DestinationDataEventListener eventListener){
		this.eventListener = eventListener;
	}

	@Override
	public boolean supportsEvents(){
		return true;
	}

	//implementation that saves the properties in a very secure way
    void changeProperties(String destName, Properties properties){
        synchronized(secureDBStorage){
            if(properties==null){
                if(secureDBStorage.remove(destName)!=null){
                	eventListener.deleted(destName);
                }
            }
            else {
                secureDBStorage.put(destName, properties);
                eventListener.updated(destName); // create or updated
            }
        }
    }
}
