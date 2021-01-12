package com.yura.mbom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import com.matrixone.apps.common.Part;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;

import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.util.SelectList;
import matrix.util.StringList;

public class yuraemxEngrReviseIntermediate {

	public String executePLMPartRevision(List<Map<String, Object>> partInfo, String level) throws Exception {
		String retMessage = "Success";
		String ori_type = (String)partInfo.get(0).get("type");
		String ori_name = (String)partInfo.get(0).get("pno");
		String ori_revision = (String)partInfo.get(0).get("revision");
		Context context = new Context("http://172.21.0.177:9300/enovia");		// 운영서버 반영시
		//Context context = new Context("http://localhost:9500/enovia");		// 로컬 개발시
		context.setUser("Test Everything");
		context.setPassword("");
		context.connect();

		//String objectId = MqlUtil.mqlCommand(context, "print bus "+ty+" "+na+" "+ri+" select id dump");

		// 마지막 개정 찾기 기능 추가 ----- 2020.12.17
		String a = MqlUtil.mqlCommand(context, "temp query bus Part " + ori_name + " * where (revision==last)");
		ori_revision = a.substring(a.length()-2, a.length());

		String strPartId = MqlUtil.mqlCommand(context, "print bus "+ori_type+" "+ori_name+" "+ori_revision+" select id dump");
		String sCustomRevisionLevel = level.trim();
		String sPolicy = "EC Part";
		String sVault = "eService Production";
		String FACTId = (String)partInfo.get(0).get("yuraproductionarea");

		//일괄처리코드 복사안되게 수정
		DomainObject partObj = DomainObject.newInstance(context, strPartId);
		String existyuraMatnrNo = (String)partInfo.get(0).get("yuramatnrno");

		HashMap<String, String> attributeMap = new HashMap<String, String>();
        attributeMap.put("yuraMatnrNo","");
        partObj.setAttributeValues(context, attributeMap);

		Map returnMap = new HashMap();
		boolean bAutoName = false;

		if(FACTId != null && !"".equals(FACTId)){

			com.matrixone.apps.engineering.Part part = new com.matrixone.apps.engineering.Part(strPartId);
			//DomainObject nextRev = new DomainObject(part.revisePart(context, sCustomRevisionLevel, sVault, true, false));
			DomainObject nextRev = new DomainObject(part.revisePart(context, sCustomRevisionLevel, sVault, true, false));
			HashMap<String, String> nextRevAttributes	=	new HashMap();
			nextRevAttributes.put("yuraProductionArea", FACTId);
			nextRev.setAttributeValues(context, nextRevAttributes);
			returnMap.put("id", nextRev.getId(context));

			//기존 part원상복귀
			HashMap<String, String> afterMap	=	new HashMap<String, String>();
			afterMap.put("yuraMatnrNo",existyuraMatnrNo);
	        partObj.setAttributeValues(context, afterMap);
		}

		String copyObjectId = strPartId;
		Part part = new Part(copyObjectId);

	  	DomainObject copyObj = DomainObject.newInstance(context);
	  	if(copyObjectId != null){
	  		copyObj.setId(copyObjectId);
	  	}

	  	String newRevId = (String) returnMap.get("id");
	  	Part revPart = new Part(newRevId);
	  	System.out.println("newRevId > "+newRevId);

	  	try{
			/*if("yuraWHProduct".equals(revPart.getInfo(context,DomainConstants.SELECT_TYPE))){
			       String strProjectobid = revPart.getInfo(context,"from["+ yuraDomainConstants.RELATIONSHIP_YURARELPARTTOPROJECT + "].to.id");
			       Map myuraVehicleData = (Map) JPO.invoke(context, "yuraPartUtil", null, "getyuraVehicleData",  new String[]{yuraDomainConstants.TYPE_YURA_WHPRODUCT,strProjectobid}, Map.class);

			       Map mapAttributes = new HashMap();
			       mapAttributes.put(yuraDomainConstants.ATTRIBUTE_YURAOEMCOMPANY,(String)myuraVehicleData.get("YURAOEMCOMPANY"));
			       //mapAttributes.put(yuraDomainConstants.ATTRIBUTE_YURACARNAME,(String)myuraVehicleData.get("YURAOEMPROJECTCODE"));
			       mapAttributes.put(yuraDomainConstants.ATTRIBUTE_YURACARCODE,(String)myuraVehicleData.get("YURAVEHICLEMODELCODE"));
			       mapAttributes.put(yuraDomainConstants.ATTRIBUTE_YURAVEHICLEYEAR,(String)myuraVehicleData.get("YURAMODELYEAR"));
			       mapAttributes.put(yuraDomainConstants.ATTRIBUTE_YURAANNUALOUTPUT,(String)myuraVehicleData.get("YURAANNUALOUTPUT"));
			       mapAttributes.put(yuraDomainConstants.ATTRIBUTE_YURAVEHICLEGRADE,(String)myuraVehicleData.get("YURAVEHICLECLASS"));
			       mapAttributes.put(yuraDomainConstants.ATTRIBUTE_YURACARDERIVEDTYPE,(String)myuraVehicleData.get("YURAVEHICLEMODELCHANGE"));
			       mapAttributes.put("yuraCarCode",copyObj.getInfo(context, "attribute[yuraCarCode].value") );
			       revPart.setAttributeValues(context, mapAttributes);
			}else if("yuraSubAssy".equals(revPart.getInfo(context,DomainConstants.SELECT_TYPE))){
			    String strWHProductName = revPart.getInfo(context,DomainUtil.getSelectAttribute(yuraDomainConstants.ATTRIBUTE_YURAASSEMBLY));
			    System.out.println("strWHProductName > "+strWHProductName);
				Map myuraVehicleData = (Map) JPO.invoke(context, "yuraPartUtil", null, "getyuraVehicleData",  new String[]{yuraDomainConstants.TYPE_YURA_SUBASSY,strWHProductName}, Map.class);

				Map mapAttributes = new HashMap();
				mapAttributes.put(yuraDomainConstants.ATTRIBUTE_YURAOEMCOMPANY,(String)myuraVehicleData.get("YURAOEMCOMPANY"));
				mapAttributes.put(yuraDomainConstants.ATTRIBUTE_YURACARNAME,(String)myuraVehicleData.get("YURAOEMPROJECTCODE"));
				mapAttributes.put(yuraDomainConstants.ATTRIBUTE_YURAVEHICLEYEAR,(String)myuraVehicleData.get("YURAMODELYEAR"));
				mapAttributes.put(yuraDomainConstants.ATTRIBUTE_YURACARDERIVEDTYPE,(String)myuraVehicleData.get("YURAVEHICLEMODELCHANGE"));
				revPart.setAttributeValues(context, mapAttributes);
			}*/

			String yuraIsRepresentativePart = (String)partInfo.get(0).get("yuraisrepresentativepart");

            if("TRUE".equals(yuraIsRepresentativePart)){
				StringList slBusSelect = new StringList();
	            slBusSelect.addElement(DomainObject.SELECT_ID);

				MapList mSpecList = part.getRelatedObjects(context
	                            , DomainConstants.RELATIONSHIP_PART_SPECIFICATION     // relationship pattern
	                            , "*"                // object pattern
	                            , slBusSelect                                       // object selects
	                            , null                                       // relationship selects
	                            , false                                         // to direction
	                            , true                                      // from direction
	                            , (short) 1                                     // recursion level
	                            , ""                               // object where clause
	                            , ""                         // relationship where clause
	                            , 0);

	            if(mSpecList != null && mSpecList.size() > 0){
	                DomainObject doSpecObj = new DomainObject();
	                DomainObject doSpecObjlast = new DomainObject();
	                for(int k = 0 ;k < mSpecList.size() ; k ++){
	                    Map mSpec = (Map) mSpecList.get(k);
	                    String specoid = (String) mSpec.get(DomainObject.SELECT_ID);
	                    doSpecObj.setId(specoid);
	                    BusinessObject lastRevObj  = doSpecObj.getLastRevision(context);
	                    String objectId = lastRevObj.getObjectId();
	                    doSpecObjlast.setId(objectId);
	                    if(!DomainUtil.isAlreadyConnected(context, newRevId, objectId, DomainConstants.RELATIONSHIP_PART_SPECIFICATION))
	                        DomainRelationship.connect(context, revPart, DomainConstants.RELATIONSHIP_PART_SPECIFICATION, doSpecObjlast);
	                }
	            }
            }

            // 개별품번의 커넥터, Sub품 일경우 이전 Rev의 하위 BOM을 신규Rev의 하위에 붙인다.

            if("yuraConnectorPart".equals(revPart.getInfo(context,DomainConstants.SELECT_TYPE)) || "yuraBoxPart".equals(revPart.getInfo(context,DomainConstants.SELECT_TYPE)) ){
            	if(!"TRUE".equals(yuraIsRepresentativePart)){
            		StringList slBusSelect     = new StringList();
                    slBusSelect.addElement(DomainObject.SELECT_ID);

                    MapList ebomList = part.getRelatedObjects(context
                                    , DomainConstants.RELATIONSHIP_EBOM     // relationship pattern
                                    , "*"                // object pattern
                                    , slBusSelect                                       // object selects
                                    , null                                       // relationship selects
                                    , false                                         // to direction
                                    , true                                      // from direction
                                    , (short) 1                                     // recursion level
                                    , ""                               // object where clause
                                    , ""                         // relationship where clause
                                    , 0);

                    if(ebomList != null && ebomList.size() > 0){
                        DomainObject doSpecObj = new DomainObject();
                        for(int k = 0 ;k < ebomList.size() ; k ++){
                            Map mEbom = (Map) ebomList.get(k);
                            String ebomOid = (String) mEbom.get(DomainObject.SELECT_ID);
                            System.out.println("ebomOid > "+ebomOid);
                            doSpecObj.setId(ebomOid);
                            if(!DomainUtil.isAlreadyConnected(context, newRevId, ebomOid, DomainConstants.RELATIONSHIP_EBOM))
                                DomainRelationship.connect(context, revPart, DomainConstants.RELATIONSHIP_EBOM, doSpecObj);
                        }
                    }

            	}
            }

			revPart.promote(context);
			revPart.promote(context);

        }catch(Exception e){
        	retMessage = "fail";
            e.printStackTrace();
        }

		return retMessage;

	}
}
