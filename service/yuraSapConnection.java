package com.yura.mbom.service;

import java.util.Properties;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class yuraSapConnection {
	static String destName = "SAP_SERVER";
	private JCoRepository repos;
	private JCoDestination dest;
	private yuraSapMyDestinationDataProvider myProvider;
	private final Properties properties;

	public yuraSapConnection(yuraSapSystem system){
		properties = new Properties();
		properties.setProperty(DestinationDataProvider.JCO_SYSNR,  system.getSystemNumber());
		properties.setProperty(DestinationDataProvider.JCO_CLIENT, system.getClient());
		properties.setProperty(DestinationDataProvider.JCO_USER,  system.getUser());
		properties.setProperty(DestinationDataProvider.JCO_PASSWD, system.getPassword());
		properties.setProperty(DestinationDataProvider.JCO_LANG,   system.getLanguage());
		properties.setProperty(DestinationDataProvider.JCO_CODEPAGE, system.getCodePage());
		if(system.getGroup() != null){	// 도메인 형식과 IP형식의 config정보가 다르므로 처리
			properties.setProperty(DestinationDataProvider.JCO_MSHOST, system.getHost());
			properties.setProperty(DestinationDataProvider.JCO_GROUP, system.getGroup());
			properties.setProperty(DestinationDataProvider.JCO_MSSERV, system.getMsserv());
			properties.setProperty(DestinationDataProvider.JCO_R3NAME, system.getR3name());
		}else{
			properties.setProperty(DestinationDataProvider.JCO_ASHOST, system.getHost());
		}

		myProvider = new yuraSapMyDestinationDataProvider();

		try{
            com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
            System.out.println("############## DestinationDataProvider connection success !!!!!!! ");
        }catch(IllegalStateException providerAlreadyRegisteredException){
        	System.out.println("############## DestinationDataProvider connection Error !!!!!!! ");
            throw new Error(providerAlreadyRegisteredException);
        	//com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
        }

		myProvider.changeProperties(destName, properties);

		System.out.println("############## myProvider.changeProperties success !!!!!!! ");
		System.out.println("myProvider---> " + myProvider);
		System.out.println("destName---> " + destName);
		System.out.println("properties---> " + properties);

		try{
			dest = JCoDestinationManager.getDestination(destName);
			System.out.println("############## dest = JCoDestinationManager.getDestination success !!!!!!! ");
			System.out.println("dest---> " + dest);
			/*System.out.println("============ Attributes Info ============");
			System.out.println(dest.getAttributes());
			System.out.println();
			System.out.println("============ Repository Info ============");
			System.out.println(dest.getRepository());
			System.out.println();*/
			repos = dest.getRepository();
			System.out.println("############## repos = dest.getRepository(); success !!!!!!! ");



		} catch(JCoException e){
			throw new RuntimeException(e);
		}

	}

	public JCoFunction getFunction(String functionStr){
		JCoFunction function = null;

		try{
			function = repos.getFunction(functionStr);
		} catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Problem retrieving JCO.Function object.");
		}

		if(function == null){
			throw new RuntimeException("Not possible to receive function.");
		}

		return function;
	}

	public void execute(JCoFunction function) throws Exception{
		try{
			JCoContext.begin(dest);
			function.execute(dest);
			JCoContext.end(dest);
		} catch(JCoException e){
			e.printStackTrace();
		}finally{
			JCoContext.end(dest);
			removeProvider();
		}
	}

	public void execute(JCoFunction function, String tid) throws Exception{
		try{
			JCoContext.begin(dest);
			function.execute(dest, tid);
			JCoContext.end(dest);
		} catch(JCoException e){
			e.printStackTrace();
		}finally{
			JCoContext.end(dest);
			removeProvider();
		}
	}

	public JCoDestination getDestination(){
		return this.dest;
	}

	public JCoRepository getRepository(){
		return this.repos;
	}

	public void removeProvider(){
		com.sap.conn.jco.ext.Environment.unregisterDestinationDataProvider(myProvider);
	}
}