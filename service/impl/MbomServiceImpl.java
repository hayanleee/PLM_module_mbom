package com.yura.mbom.service.impl;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yura.bm.service.BmVO;
import com.yura.mbom.service.MbomService;
import com.yura.mbom.service.MbomVO;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.impl.FileManageDAO;
import egovframework.com.cmm.util.EgovFileTool;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

 /**
  * mbom 서비스 구현클래스를 정의한다.
  */
@Service("MbomService")
public class MbomServiceImpl extends EgovAbstractServiceImpl implements MbomService {
    String storePath = EgovProperties.getProperty("Globals.fileStorePath");

    @Resource(name = "MbomDAO")
    private MbomDAO mbomDAO;

	@Resource(name = "FileManageDAO")
	private FileManageDAO fileMngDAO;

	/** mbom final IdGnrService */
	@Resource(name="mboIdGnrService")
	private EgovIdGnrService mboIdGnrService;

	/**
     * ebom 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    @Override
	public List<?> selectEbomSearch(HashMap<String, Object> paramMap) throws Exception {
 		return mbomDAO.selectEbomSearch(paramMap);
    }

    /**
     * ebom 리스트 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectEbomSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return mbomDAO.selectEbomSearchTotCnt(paramMap);
    }

    /**
     * ebom 상세항목을 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    @Override
	public HashMap<String,Object> selectEbomSearchDetail(HashMap<String,Object> paramMap) throws Exception{
   	 return mbomDAO.selectEbomSearchDetail(paramMap);
    }

    /**
     * 설변대상아이템 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    @Override
	public List<?> selectEffectiveItemSearch(HashMap<String, Object> paramMap) throws Exception {
 		return mbomDAO.selectEffectiveItemSearch(paramMap);
    }

    /**
     * 설변대상아이템 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectEffectiveItemSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return mbomDAO.selectEffectiveItemSearchTotCnt(paramMap);
    }

    /**
     * 부자재마스터 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    @Override
	public List<?> selectMbomMasterSearch(HashMap<String, Object> paramMap) throws Exception {
 		return mbomDAO.selectMbomMasterSearch(paramMap);
    }

    /**
     * 부자재마스터 리스트 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectMbomMasterSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return mbomDAO.selectMbomMasterSearchTotCnt(paramMap);
    }

    /**
     * 상위품번 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    @Override
	public List<?> selectParentPnoSearch(HashMap<String, Object> paramMap) throws Exception {
 		return mbomDAO.selectParentPnoSearch(paramMap);
    }

    /**
     * 상위품번 리스트 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectParentPnoSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return mbomDAO.selectParentPnoSearchTotCnt(paramMap);
    }

    /**
     * 부자재 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    @Override
	public List<?> selectSubMaterialsSearch(HashMap<String, Object> paramMap) throws Exception {
 		return mbomDAO.selectSubMaterialsSearch(paramMap);
    }

    /**
     * 부자재 리스트 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectSubMaterialsSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return mbomDAO.selectSubMaterialsSearchTotCnt(paramMap);
    }

    /**
     * 부자재마스터를 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    @Override
	public HashMap<String,Object> selectMbomMasterData(HashMap<String,Object> parmMap) throws Exception{
    	return mbomDAO.selectMbomMasterData(parmMap);
    }

    /**
     * MBOM 데이터 중복체크를 한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectDupleChk(MbomVO parmVO) throws Exception {
 		return mbomDAO.selectDupleChk(parmVO);
    }

    /**
     * 부자재마스터 항목을 등록한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void insertMbomMasterData(HashMap<String,Object> paramMap) throws Exception{
    	mbomDAO.insertMbomMasterData(paramMap);
    }

    /**
     * 부자재마스터 항목을 수정한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void updateMbomMasterData(HashMap<String,Object> paramMap) throws Exception{
    	mbomDAO.updateMbomMasterData(paramMap);
    }

    /**
     * 부자재마스터 항목을 삭제한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void deleteMbomMasterData(HashMap<String,Object> paramMap) throws Exception{
    	mbomDAO.deleteMbomMasterData(paramMap);
    	mbomDAO.deleteMbomMasterData2(paramMap);
    }

    /**
     * 부자재 항목을 등록한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void insertMbomSubMaterialsData(HashMap<String,Object> paramMap) throws Exception{
    	String strUsage = (String)paramMap.get("usage");
    	BigDecimal usage = new BigDecimal(strUsage);
    	paramMap.put("usage", usage);

    	mbomDAO.insertMbomSubMaterialsData(paramMap);
    }

    /**
     * 부자재 항목을 수정한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void updateMbomSubMaterialsData(HashMap<String,Object> paramMap) throws Exception{
    	mbomDAO.updateMbomSubMaterialsData(paramMap);
    }

    /**
     * 부자재 상세항목을 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    @Override
	public HashMap<String,Object> selectSubMaterialsDetail(HashMap<String,Object> paramMap) throws Exception{
   	 return mbomDAO.selectSubMaterialsDetail(paramMap);
    }

    /**
     * 부자재 상세항목을 삭제한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void deleteMbomSubMaterials(HashMap<String,Object> paramMap) throws Exception{
    	mbomDAO.deleteMbomSubMaterials(paramMap);
    }

    /**
     * ebom 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    @Override
	public List<?> selectPlmEbomDataSearch(HashMap<String, Object> paramMap) throws Exception {
 		return mbomDAO.selectPlmEbomDataSearch(paramMap);
    }

    /**
     * ebom 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectPlmEbomDataSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return mbomDAO.selectPlmEbomDataSearchTotCnt(paramMap);
    }

    /**
     * ebom 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    @Override
	public List<?> selectAddPlmEbomData(HashMap<String, Object> paramMap) throws Exception {
 		return mbomDAO.selectAddPlmEbomData(paramMap);
    }

    /**
     * ebom 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectAddPlmEbomDataTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return mbomDAO.selectAddPlmEbomDataTotCnt(paramMap);
    }

    /**
     * mbom 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    @Override
	public List<?> selectMbomSearch(HashMap<String, Object> paramMap) throws Exception {
 		return mbomDAO.selectMbomSearch(paramMap);
    }

    /**
     * mbom 리스트 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectMbomSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return mbomDAO.selectMbomSearchTotCnt(paramMap);
    }

    /**
     * MBOM Final 데이터를 등록한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void insertMbomFinalData(HashMap<String,Object> paramMap) throws Exception{
    	String econo = (String)paramMap.get("econo");
    	String humid = (String)paramMap.get("reguserid");
    	String oids = (String)paramMap.get("ecosuboid");
    	String mbrno = mboIdGnrService.getNextStringId();
    	String[] arr = oids.split("\\|");

    	paramMap.put("mbrno", mbrno);
    	mbomDAO.insertMbomFinalData(paramMap);
    	mbomDAO.updateEcoState(paramMap);

    	List<String> oidList = new ArrayList<String>();
		for (int i=0; i < arr.length; i++){
			String[] oidpnorev = arr[i].split(",");
			HashMap<String,Object> putMap = new HashMap<String,Object>();
			putMap.put("humid", humid);
			putMap.put("econo", econo);
			putMap.put("mbrno", mbrno);
			putMap.put("mstoid", oidpnorev[0]);
			putMap.put("parentpno", oidpnorev[1]);
			putMap.put("parentrev", oidpnorev[2]);
			mbomDAO.insertMbomEcoRelData(putMap);
		}
    }

    /**
     * mbom 상세 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    @Override
	public List<?> selectMbomDetail(HashMap<String, Object> paramMap) throws Exception {
 		return mbomDAO.selectMbomDetail(paramMap);
    }

    /**
     * mbom 상세 리스트 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectMbomDetailTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return mbomDAO.selectMbomDetailTotCnt(paramMap);
    }

    /**
     * 파트정보를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    @Override
	public List<?> selectPnoSearch(HashMap<String, Object> paramMap) throws Exception {
 		return mbomDAO.selectPnoSearch(paramMap);
    }

    /**
     * 파트정보 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectPnoSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return mbomDAO.selectPnoSearchTotCnt(paramMap);
    }

    /**
     * MBOM 부자재조합 관계 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    @Override
	public List<?> selectMbomRelationData(HashMap<String, Object> paramMap) throws Exception {
 		return mbomDAO.selectMbomRelationData(paramMap);
    }

    /**
     * MBOM 부자재조합 관계 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    @Override
	public int selectMbomRelationDataTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return mbomDAO.selectMbomRelationDataTotCnt(paramMap);
    }

    /**
     * MBOM 부자재 조합을 삭제한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void deleteMbomSubItemRel(HashMap<String,Object> paramMap) throws Exception{
    	mbomDAO.deleteMbomEcoRelData(paramMap);
    	mbomDAO.deleteMbomFinalData(paramMap);
    	mbomDAO.updateMbomEcoState(paramMap);			// state 'N' 변경
    }

    /**
     * SAP 인터페이스 데이터를 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    @Override
	public List<Map<String, Object>> selectSAPEcoData(String mbrno) throws Exception{
   	 return mbomDAO.selectSAPEcoData(mbrno);
    }
    @Override
   	public List<Map<String, Object>> selectSAPPartData(HashMap<String,Object> paramMap) throws Exception{
      	 return mbomDAO.selectSAPPartData(paramMap);
    }
    @Override
   	public List<Map<String, Object>> selectSAPAffectItemData(HashMap<String,Object> paramMap) throws Exception{
      	 return mbomDAO.selectSAPAffectItemData(paramMap);
    }
    @Override
   	public List<Map<String, Object>> selectSAPMbomData(HashMap<String,Object> paramMap) throws Exception{
      	 return mbomDAO.selectSAPMbomData(paramMap);
    }

    /**
     * SAP 상태를 수정한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void updateMbomSapState(HashMap<String,Object> paramMap) throws Exception{
    	mbomDAO.updateMbomSapState(paramMap);
    }

    /**
     * 개정할 파트를 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    @Override
	public List<Map<String, Object>> selectRevisionPartInfo(HashMap<String,Object> paramMap) throws Exception{
   	 return mbomDAO.selectRevisionPartInfo(paramMap);
    }

    /**
     * 개정 ECO를 insert select 한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void insertSelectPlmEco(HashMap<String,Object> paramMap) throws Exception{
    	mbomDAO.insertSelectPlmEco(paramMap);
    }

    /**
     * 개정 PART를 insert select 한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void insertSelectPlmPart(HashMap<String,Object> paramMap) throws Exception{
    	mbomDAO.insertSelectPlmPart(paramMap);
    }

    /**
     * 개정 effectiveitem를 insert select 한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void insertSelectPlmEffectiveitem(HashMap<String,Object> paramMap) throws Exception{
    	mbomDAO.insertSelectPlmEffectiveitem(paramMap);
    }

    /**
     * 개정 ebom를 insert select 한다.
     * @param paramMap
     * @throws Exception
     */
    @Override
	public void insertSelectPlmEbom(HashMap<String,Object> paramMap) throws Exception{
    	String level = (String)paramMap.get("level");
    	List<Map<String, Object>> ebomInfo = new ArrayList<Map<String, Object>>();
    	ebomInfo = mbomDAO.selectPreRevisionEbomInfo(paramMap);

    	for (int i=0; i<ebomInfo.size(); i++) {
    		HashMap<String, Object>param = new HashMap<String,Object>();
    		param.put("econo", ebomInfo.get(i).get("econo"));
			param.put("parentpno", ebomInfo.get(i).get("parentpno"));
			param.put("parentrev", ebomInfo.get(i).get("parentrev"));
			param.put("pno", ebomInfo.get(i).get("pno"));
			param.put("usage", ebomInfo.get(i).get("usage"));
			param.put("unit", ebomInfo.get(i).get("unit"));
			param.put("level", level);
			mbomDAO.insertSelectPlmEbom(param);
    	}

    	// mbomDAO.insertSelectPlmEbom(paramMap);
    }


    // 비밀번호 전체 초기화
    @Override
   	public List<Map<String, Object>> selecthum(String temp) throws Exception{
      	 return mbomDAO.selecthum(temp);
    }
    @Override
	public void updatehum(HashMap<String,Object> paramMap) throws Exception{
    	mbomDAO.updatehum(paramMap);
    }

}