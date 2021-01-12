package com.yura.mbom.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class yuraSapConnectionProperties{

	private String sapDataSourceName = null;
    private DocumentBuilderFactory docFactory = null;
    private DocumentBuilder cbdConf = null;

	public yuraSapConnectionProperties(){}

	private String fileName = "sap-config.xml";
	Map sapConnConfigInfoMap = null;

	public yuraSapSystem sapSystemInfo(String sapDataSourceName) throws Exception {

		this.sapDataSourceName = sapDataSourceName;

        System.out.println("##### sapDataSourceName :"+sapDataSourceName);

        sapConnConfigInfoMap = new HashMap();		// dbc 초기화

		getSapConnConfigInfo(sapDataSourceName);
		yuraSapSystem system = getSystemInfo(sapConnConfigInfoMap);

		return system;
	}

	public void getSapConnConfigInfo(String dbName) throws Exception {

		try {
			//String sWebPath = "/usr/apache-tomcat-8.5.15-yuPVQ/webapps/yuPVQ/WEB-INF/config/datasource/sap/";	// 운영 경로 old
			//String sWebPath = "/usr/apache-tomcat-8.5.5-yuRMS/webapps/yuRMS/WEB-INF/config/datasource/sap/";	// 운영 경로 new

	 		String sWebPath = "D:\\util\\workspace3.8\\yuPVQ\\src\\main\\webapp\\WEB-INF\\config\\datasource\\sap\\";	// 로컬 경로

		 	//String sWebPath = "D:\\WebContent\\enovia\\WEB-INF\\config\\datasource\\sap\\";				// 운영서버 경로 (enovia 기준)

			File file = new File(sWebPath+fileName);

            docFactory = DocumentBuilderFactory.newInstance();
            cbdConf = docFactory.newDocumentBuilder();

            if(file.exists())
            {
                 System.out.println("sap-config.xml file exist");

                 FileInputStream fis = new FileInputStream(file);

                 InputSource is = new InputSource(fis);
                 Document config = cbdConf.parse(is);
                 analysisConfig(config.getDocumentElement(), 0, "");

                 fis.close();
            }else{
            	System.out.println("sap-config.xml file not exist");
            }

		}catch (Exception e) {
			System.out.println("DB 연결 실패\n"+e);
			e.printStackTrace();
		}
	}


    private void analysisConfig(Node root, int depth, String parent)
    {
         int childrenLength = -1;
//         String nodeName = null;
//         String nodeValue;
         NodeList children = null;

         if(root.getNodeType() == Node.ELEMENT_NODE || root.getNodeType() == Node.ATTRIBUTE_NODE)
         {
              if(root.getNodeName().equalsIgnoreCase("site"))
              {
                   if(root.hasAttributes())
                   {
                        NamedNodeMap attrib = root.getAttributes();
                        int attribLength = attrib.getLength();
                        String id = attrib.getNamedItem("id").getNodeValue();

                        if( sapDataSourceName.equals(id)){
                        	NodeList tmpNL = root.getChildNodes();
                            int tmpLength = tmpNL.getLength();
                            for(int i = 0; i < tmpLength; i++)
                            {
                            	Node childNode = tmpNL.item(i);
                            	if(childNode.hasAttributes())
                                {
                                    NamedNodeMap tmpNNM = childNode.getAttributes();

                                    String key = tmpNNM.getNamedItem("name").getNodeValue();
                                    String val = tmpNNM.getNamedItem("value").getNodeValue();
                                    //System.out.println("### name :"+key);
                                    //System.out.println("### value :"+val);
                                    sapConnConfigInfoMap.put(key, val);
                                }
                            }
                        }
                   }
              }
         }

         if(root.hasChildNodes())
         {
             children = root.getChildNodes();
             childrenLength = children.getLength();
             for(int i = 0; i < childrenLength; i++)
             {
                  analysisConfig(children.item(i), depth + 1, root.getNodeName());
             }
         }
    }

	public yuraSapSystem getSystemInfo(Map<String, String> dbcConfigInfo) throws Exception {

		  yuraSapSystem system = new yuraSapSystem();
		  system.setClient(				dbcConfigInfo.get("client")	);
		  system.setHost(					dbcConfigInfo.get("host")	);
		  system.setLanguage(			dbcConfigInfo.get("language")	);
		  system.setSystemNumber(	dbcConfigInfo.get("systemNumber")	);
		  system.setUser(					dbcConfigInfo.get("user")	);
		  system.setPassword(			dbcConfigInfo.get("password")	);
		  system.setCodePage(			dbcConfigInfo.get("codePage"));
		  system.setGroup(				dbcConfigInfo.get("group"));
		  system.setMsserv(				dbcConfigInfo.get("msserv"));
		  system.setR3name(			dbcConfigInfo.get("r3name"));

		return system;
	}



}