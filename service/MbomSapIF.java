package com.yura.mbom.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;

public class MbomSapIF {

	List<Map<String,Object>> ecoInfo = null;
	List<Map<String,Object>> partInfo = null;
	List<Map<String,Object>> affectitemInfo = null;
	List<Map<String,Object>> mbomInfo = null;
	String econo = null;
	String mbrno = null;
	String strBUKRS = null;

	//public LinkedList<LinkedHashMap<String, Object>> rtnTable = null;

	public MbomSapIF() {}

	public void yuraSapInterfaceForECOInit(List<Map<String,Object>> ecoInfo, List<Map<String,Object>> partInfo, List<Map<String,Object>> affectitemInfo
											  , List<Map<String,Object>> mbomInfo, String econo, String mbrno){
		this.ecoInfo = ecoInfo;
		this.partInfo = partInfo;
		this.affectitemInfo = affectitemInfo;
		this.mbomInfo = mbomInfo;
		this.econo = econo;
		this.mbrno = mbrno;
		//rtnTable = new LinkedList<LinkedHashMap<String, Object>>();
	}

	//boolean executeFlag = false;
	public static String ecoRfcFunc = "ZMD_RECEIVE_PLM_DATA";

	public static String[] plmRfcName = { "part", "tprocess", "sublist", "usage", "cir", "mh", "eco", "apply" };
	public static String[] sapRfcName = { "T_TAB1", "T_TAB2", "T_TAB3", "T_TAB4", "T_TAB5", "T_TAB6", "T_TAB7", "T_TAB8" };

	public static Map<String, String> rfcTableNameMap = getRfcNameMap();

	public static Map<String, String> getRfcNameMap() {

		Map<String, String> m = new HashMap<String, String>();

		for(int i=0; i < plmRfcName.length; i++){
			m.put(plmRfcName[i], sapRfcName[i]);
		}

		return m;
	}


	public String executeSapInterface(String strBUKRS, List<Map<String,Object>> ecoInfo, List<Map<String,Object>> partInfo
									  , List<Map<String,Object>> affectitemInfo, List<Map<String,Object>> mbomInfo, String econo, String mbrno) throws Exception {

		System.out.println("*************** MBOM IF [ start ] ***************");
		System.out.println("------------------->>>>> site : " + strBUKRS);

		yuraSapInterfaceForECOInit(ecoInfo, partInfo, affectitemInfo, mbomInfo, econo, mbrno);


		this.strBUKRS = strBUKRS;

		String retMessage = "Success";

		JCoDestination dest = null;

		boolean isErrorFlag = false;
		yuraSapConnection conn = null;

		try{
			conn = connection(strBUKRS);	// sap connection
			dest = conn.getDestination();

			JCoRepository repos = conn.getRepository();

	    	JCoFunction jf = conn.getFunction( ecoRfcFunc );

	    	if (partInfo.size() > 0) {
	    		insertListSapMapForPart_New("part", partInfo, strBUKRS, conn, jf);
	    	}

	    	updateSapMapAffectItemLoop("tprocess", strBUKRS, affectitemInfo, conn, jf, mbrno);
			updateSapMapAffectItemLoop("apply", strBUKRS, affectitemInfo, conn, jf, mbrno);

			updateSapMap("eco", ecoInfo , strBUKRS, conn, jf, mbrno);

			insertSapUsage("usage", mbomInfo , strBUKRS, econo, conn, jf, mbrno);

			if(true){
				conn.execute(jf);
				isErrorFlag = isError(jf);
				conn.removeProvider();

				String isSuccess = "";
				LinkedList<LinkedHashMap<String, Object>> rtnTable = new LinkedList<LinkedHashMap<String, Object>>();
				try{
					JCoTable t_data = jf.getTableParameterList().getTable("T_DATA");
					isSuccess = getTableParameter(t_data, rtnTable);
				}catch(Exception e){
					isSuccess = "F";
					conn.removeProvider();
				}

			}else{
				conn.removeProvider();
			}

			System.out.println("*************** MBOM IF [ success ] ***************");
		}catch(Exception e){
			System.out.println("*************** MBOM IF [ error ] ***************");
			retMessage = "fail";
			e.printStackTrace();
		}finally{
			System.out.println("*************** MBOM IF [ end ] ***************");
			conn.removeProvider();
		}

		return retMessage;
	}


	public yuraSapConnection connection(String __strBUKRS)  throws Exception{

		String site = getSiteDataSource(__strBUKRS);

		yuraSapConnectionProperties ssi = new yuraSapConnectionProperties();
		yuraSapSystem system = ssi.sapSystemInfo(site);

		// SAP Connection Start...
		yuraSapConnection connect = new yuraSapConnection(system);

		return connect;
	}


	public String getSiteDataSource(String _strBUKRS) throws Exception
	{
		String siteDataSourceName= "";

		if("2000".equals(_strBUKRS) || "2800".equals(_strBUKRS) || "2150".equals(_strBUKRS)){
			siteDataSourceName	=	"RealBeijingSapDataSource";
		}else if("3000".equals(_strBUKRS)){
			siteDataSourceName	=	"DefaultEroupeSapDataSource";
		}else if("4200".equals(_strBUKRS)){
			siteDataSourceName = "DefaultMexicoSapDataSource";
		}else if("4000".equals(_strBUKRS)){
			siteDataSourceName	=	"DefaultRussiaSapDataSource";
		}else if("5000".equals(_strBUKRS)){
			siteDataSourceName	=	"DefaultHarnessSapDataSource";
		}else{
			siteDataSourceName	=	"DefaultSapDataSource";
		}

		return siteDataSourceName;
	}



	public void insertListSapMapForPart_New(String tablename, List<Map<String, Object>> mapList, String strBUKRS
											, yuraSapConnection conn, JCoFunction function)  throws Exception{

		JCoTable jTable = function.getTableParameterList().getTable( rfcTableNameMap.get(tablename) );

		//executeFlag = false;

		for (int i=0; i < mapList.size(); i++) {
			Map<String, Object> resMap = mapList.get(i);

			String product = (String)resMap.get("product");
			if (product.equals("TRUE")) {
				product = "FERT";		// 제품
			} else if (product.equals("FALSE")) {
				product = "ROH";		// 부품
			}

			//executeFlag = true;

			try{
				System.out.println("------------ part (" + Integer.toString(i) + ")------------");
				System.out.println("BUKRS : " + strBUKRS);
				System.out.println("MATNR : " + (String)resMap.get("pno"));
				System.out.println("MAKTX : " + (String)resMap.get("name"));
				System.out.println("MTART : " + product);
				System.out.println("STLAL : " + (String)resMap.get("revision"));
				System.out.println("MEINS : " + (String)resMap.get("yuraunit"));
				System.out.println("MATKL : " + (String)resMap.get("erpcarcode"));
				System.out.println("EXTWG : " + (String)resMap.get("erptype"));
				System.out.println("PRCTY : " + "A");
				System.out.println("BRGEW : " + (String)resMap.get("yuraweight"));
				System.out.println("GEWEI : " + "G");
				System.out.println("NAME1 : " + (String)resMap.get("yurasupplier"));
				System.out.println("IDNLF : " + (String)resMap.get("pno"));
				System.out.println("SEIRES: " + (String)resMap.get("yuraseries"));
				System.out.println("DEGIN : " + (String)resMap.get("yuradesigngroupetc"));
				System.out.println("OWNER : " + (String)resMap.get("yuramatnrno"));
				System.out.println("ERNAM : " + (String)resMap.get("humname"));

				jTable.appendRow();

				jTable.setValue("BUKRS", strBUKRS);
				jTable.setValue("MATNR", (String)resMap.get("pno"));
				jTable.setValue("MAKTX", (String)resMap.get("name"));
				jTable.setValue("MTART", product);
				jTable.setValue("STLAL", (String)resMap.get("revision"));
				jTable.setValue("AENNR", "");
				jTable.setValue("DPCOD", "");
				jTable.setValue("CEONO", "");
				jTable.setValue("MEINS", (String)resMap.get("yuraunit"));
				jTable.setValue("MATKL", (String)resMap.get("erpcarcode"));
				jTable.setValue("EXTWG", (String)resMap.get("erptype"));
				jTable.setValue("PRCTY", "A");
				jTable.setValue("BRGEW", (String)resMap.get("yuraweight"));
				jTable.setValue("GEWEI", "G");
				jTable.setValue("GRADE", "");
				jTable.setValue("NAME1", (String)resMap.get("yurasupplier"));
				jTable.setValue("IDNLF", (String)resMap.get("pno"));
				jTable.setValue("MTYPE", "");
				jTable.setValue("COLOR", "");
				jTable.setValue("CPARTNO", "");
				jTable.setValue("SEIRES", (String)resMap.get("yuraseries"));
				jTable.setValue("POLEQT", "");
				jTable.setValue("WPROF", "");
				jTable.setValue("PLATING", "");
				jTable.setValue("WSQARE", "");
				jTable.setValue("COREQT", "");
				jTable.setValue("WIDTH", "");
				jTable.setValue("LENGTH", "");
				jTable.setValue("ADHEN", "");
				jTable.setValue("PIE", "");
				jTable.setValue("DEGIN", (String)resMap.get("yuradesigngroupetc"));
				jTable.setValue("PACKG", "");
				jTable.setValue("MEMSIZ", "");
				jTable.setValue("VYEAR", "");
				jTable.setValue("CDEVTP", "");
				jTable.setValue("MANAG", "");
				jTable.setValue("INEXT", "");
				jTable.setValue("LOEXP", "");
				jTable.setValue("LEPRICE", "");
				jTable.setValue("BRANCH", "");
				jTable.setValue("COMBINATION", "");
				jTable.setValue("ENDIT", "");
				jTable.setValue("OWNER", (String)resMap.get("yuramatnrno"));
				jTable.setValue("ERNAM", (String)resMap.get("humname"));

			}catch(Exception e){
				e.printStackTrace();
			}

		}
	}


	public void updateSapMapAffectItemLoop(String tablename, String strBUKRS, List<Map<String, Object>> mapList
										, yuraSapConnection conn, JCoFunction function, String mbrno) throws Exception{

		JCoTable jTable = function.getTableParameterList().getTable( rfcTableNameMap.get(tablename) );

		for (int i=0; i < mapList.size(); i++) {
			Map<String, Object> resMap = mapList.get(i);

			String yuraisrepresentativepart = (String)resMap.get("yuraisrepresentativepart");
			if (yuraisrepresentativepart.equals("TRUE") || yuraisrepresentativepart == "TRUE") {
				continue;
			}

			try{
				if ("tprocess".equals(tablename)){
					System.out.println("------------ AffectItem tprocess (" + Integer.toString(i) + ")------------");
					System.out.println("OWNER : " + mbrno);
					System.out.println("DELETE: " + "N");
					System.out.println("ERNAM : " + (String)resMap.get("humname"));
					System.out.println("AENNR : " + mbrno);
					System.out.println("DPCOD : " + (String)resMap.get("epdpno"));
					System.out.println("CEONO : " + (String)resMap.get("eono"));
					System.out.println("MATNR : " + (String)resMap.get("pno"));
					System.out.println("STLAL : " + (String)resMap.get("revision"));
				} else {
					System.out.println("------------ AffectItem apply (" + Integer.toString(i) + ")------------");
					System.out.println("AENNR : " + mbrno);
					System.out.println("DPCOD : " + (String)resMap.get("epdpno"));
					System.out.println("CEONO : " + (String)resMap.get("eono"));
					System.out.println("MATNR : " + (String)resMap.get("pno"));
					System.out.println("STLAL : " + (String)resMap.get("revision"));
				}


				jTable.appendRow();

				jTable.setValue("BUKRS", strBUKRS);
				if ("tprocess".equals(tablename)){
					// jTable.setValue("OWNER", (String)resMap.get("econo"));
					jTable.setValue("OWNER", mbrno);
					jTable.setValue("DELETE", "N");
					jTable.setValue("ERNAM", (String)resMap.get("humname"));
				}
				// jTable.setValue("AENNR", (String)resMap.get("econo"));
				jTable.setValue("AENNR", mbrno);
				jTable.setValue("DPCOD", (String)resMap.get("epdpno"));
				jTable.setValue("CEONO", (String)resMap.get("eono"));
				jTable.setValue("MATNR", (String)resMap.get("pno"));
				jTable.setValue("STLAL", (String)resMap.get("revision"));

			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}


	public void updateSapMap(String tablename, List<Map<String, Object>> mapList, String strBUKRS
							, yuraSapConnection conn, JCoFunction function, String mbrno) throws Exception{

		JCoTable jTable = function.getTableParameterList().getTable(  rfcTableNameMap.get("eco")  );

		for (int i=0; i < mapList.size(); i++) {
			Map<String, Object> resMap = mapList.get(i);

			try{
				System.out.println("------------ updateSapMap (" + Integer.toString(i) + ")------------");
				System.out.println("BUKRS : " + strBUKRS);
				System.out.println("AENNR : " + mbrno);
				System.out.println("DPCOD : " + (String)resMap.get("epdpno"));
				System.out.println("CEONO : " + (String)resMap.get("eono"));
				System.out.println("CHGUBT : " + (String)resMap.get("title"));
				System.out.println("CDATE : " + (String)resMap.get("regdate"));
				System.out.println("CARNAME : " + (String)resMap.get("erpcarcode"));
				System.out.println("PARTFAMILY : " + (String)resMap.get("erptype"));
				System.out.println("ORIGINATED : " + (String)resMap.get("regdateoriginal"));
				System.out.println("REFERENCE : " + (String)resMap.get("yuradesigngroup"));

				jTable.appendRow();

				jTable.setValue("BUKRS", strBUKRS);
				//jTable.setValue("AENNR", (String)resMap.get("econo"));
				jTable.setValue("AENNR", mbrno);
				jTable.setValue("DPCOD", (String)resMap.get("epdpno"));
				jTable.setValue("CEONO", (String)resMap.get("eono"));
				jTable.setValue("CHGUBT", (String)resMap.get("title"));
				jTable.setValue("LTEXT", "");
				jTable.setValue("CDATE", (String)resMap.get("regdate"));
				jTable.setValue("CARNAME", (String)resMap.get("erpcarcode"));
				jTable.setValue("PARTFAMILY", (String)resMap.get("erptype"));
				jTable.setValue("ORIGINATED", (String)resMap.get("regdateoriginal"));
				jTable.setValue("REFERENCE", (String)resMap.get("yuradesigngroup"));
				jTable.setValue("PRODAREA", "");
				jTable.setValue("FILEPATH", "");
				jTable.setValue("FILENAME", "");

			}catch(Exception e){
				e.printStackTrace();
			}
		}
    }


	public void insertSapUsage(String tablename, List<Map<String, Object>> mapList, String strBUKRS
								, String econo, yuraSapConnection conn, JCoFunction function, String mbrno)  throws Exception{

		JCoTable jTable = function.getTableParameterList().getTable( rfcTableNameMap.get("usage") );

		System.out.println("\n\n");
		System.out.println("original format -> parentpno-parentrev  pno   usage + unit");

		for (int i=0; i < mapList.size(); i++) {
			Map<String, Object> resMap = mapList.get(i);

			String parentpno = (String)resMap.get("parentpno");
			String parentrev = (String)resMap.get("parentrev");
			String pno = (String)resMap.get("pno");
			String work = "A";
			String len = "0";
			BigDecimal bigusage =(BigDecimal)resMap.get("usage");
			String usage = bigusage.toString();
			String unit = (String)resMap.get("unit");
			String groupNo = "";
			String original = "";
			original = StringUtils.rightPad(parentpno + "-" + parentrev, 22) + StringUtils.rightPad(work, 3) + StringUtils.rightPad(pno, 18) +
					   StringUtils.leftPad(len, 5) + StringUtils.leftPad(usage, 8) + StringUtils.rightPad(unit, 3) + StringUtils.rightPad(groupNo, 5);
			System.out.println(original);
			try{
				jTable.appendRow();

				jTable.setValue("BUKRS", strBUKRS);
				jTable.setValue("MATNR", parentpno);
				jTable.setValue("STLAL", parentrev);
				jTable.setValue("ZWORK", "A");
				jTable.setValue("IDNRK", pno);
				jTable.setValue("ORIGINAL", original);
				//jTable.setValue("OWNER", econo);
				jTable.setValue("OWNER", mbrno);

			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public boolean isError(JCoFunction function){

		String RFC_Result = function.getExportParameterList().getValue("E_RESULT").toString();
		String RFC_Message = function.getExportParameterList().getValue("E_MESSAGE").toString();

		System.out.println("#### 실행 결과 :"+RFC_Result+"   #### 실행 내용 :"+RFC_Message);

		boolean isFail = false;

		if( "E".equals(RFC_Result) ){
			isFail = true;
		}

		return isFail;
	}


	private String getTableParameter( JCoTable t, LinkedList<LinkedHashMap<String, Object>> rtnTable ) {

    	LinkedList<LinkedHashMap<String, Object>> lh = new LinkedList<LinkedHashMap<String, Object>>();
        JCoFieldIterator iter = null;
        LinkedHashMap<String, Object> m = null;
        JCoField f = null;
        String isSuccess = "S";

        for (int i = 0; i < t.getNumRows(); i++) {
            t.setRow(i);
            iter = t.getFieldIterator();
            m = new LinkedHashMap<String, Object>();

            while (iter.hasNextField()) {
                f = iter.nextField();
                m.put(f.getName(), f.getString().trim());
                System.out.print(i+"## "+f.getName()+":"+f.getString().trim()+"\t");
            }

            // return map에서 성공여부를 확인
            if( !"S".equals(m.get("MSGTY")) ){
            	isSuccess = "F";
            }

            rtnTable.add(m);

            System.out.print("\n");

        }
        return isSuccess;
    }

}
