package com.yura.mbom.service.impl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.yura.bm.service.BmVO;
import com.yura.mbom.service.MbomVO;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

 /**
  * MBOM 데이터 접근 클래스를 정의한다.
  */
@Repository("MbomDAO")
public class MbomDAO extends EgovComAbstractDAO {
	/**
     * ebom 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    public List<?> selectEbomSearch(HashMap<String, Object> paramMap) throws Exception {
		return selectList("MbomDAO.selectEbomSearch", paramMap);
    }

    /**
     * ebom 리스트 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     * @throws Exception
     */
    public int selectEbomSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return (Integer) selectOne("MbomDAO.selectEbomSearchTotCnt", paramMap);
    }

    /**
     * ebom 상세항목을 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    public HashMap<String,Object> selectEbomSearchDetail(HashMap<String,Object> parmMap) throws Exception{
   	 return (HashMap<String,Object>) selectOne("MbomDAO.selectEbomSearchDetail", parmMap);
    }

    /**
     * 설변대상아이템 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    public List<?> selectEffectiveItemSearch(HashMap<String, Object> paramMap) throws Exception {
		return selectList("MbomDAO.selectEffectiveItemSearch", paramMap);
    }

    /**
     * 설변대상아이템 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     * @throws Exception
     */
    public int selectEffectiveItemSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return (Integer) selectOne("MbomDAO.selectEffectiveItemSearchTotCnt", paramMap);
    }

    /**
     * 부자재마스터 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    public List<?> selectMbomMasterSearch(HashMap<String, Object> paramMap) throws Exception {
		return selectList("MbomDAO.selectMbomMasterSearch", paramMap);
    }

    /**
     * 부자재마스터 리스트 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     * @throws Exception
     */
    public int selectMbomMasterSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return (Integer) selectOne("MbomDAO.selectMbomMasterSearchTotCnt", paramMap);
    }

    /**
     * 상위품번 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    public List<?> selectParentPnoSearch(HashMap<String, Object> paramMap) throws Exception {
		return selectList("MbomDAO.selectParentPnoSearch", paramMap);
    }

    /**
     * 상위품번 리스트 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     * @throws Exception
     */
    public int selectParentPnoSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return (Integer) selectOne("MbomDAO.selectParentPnoSearchTotCnt", paramMap);
    }

    /**
     * 부자재 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    public List<?> selectSubMaterialsSearch(HashMap<String, Object> paramMap) throws Exception {
		return selectList("MbomDAO.selectSubMaterialsSearch", paramMap);
    }

    /**
     * 부자재 리스트 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     * @throws Exception
     */
    public int selectSubMaterialsSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return (Integer) selectOne("MbomDAO.selectSubMaterialsSearchTotCnt", paramMap);
    }

    /**
     * 부자재마스터를 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    public HashMap<String,Object> selectMbomMasterData(HashMap<String,Object> parmMap) throws Exception{
    	return (HashMap<String,Object>) selectOne("MbomDAO.selectMbomMasterData", parmMap);
    }

    /**
     * 코드 제너레이션 샘플 데이터 중복체크를 한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    public int selectDupleChk(MbomVO parmVO) throws Exception {
  		return (Integer) selectOne("MbomDAO.selectDupleChk", parmVO);
    }

    /**
     * 부자재마스터 항목을 등록한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertMbomMasterData(HashMap<String,Object> parmMap) throws Exception{
   	 	insert("MbomDAO.insertMbomMasterData", parmMap);
    }

    /**
     * 부자재마스터 항목을 수정한다.
     * @param paramMap
     * @throws Exception
     */
    public void updateMbomMasterData(HashMap<String,Object> parmMap) throws Exception{
    	update("MbomDAO.updateMbomMasterData", parmMap);
    }

    /**
     * 부자재마스터 항목을 삭제한다.
     * @param paramMap
     * @throws Exception
     */
    public void deleteMbomMasterData(HashMap<String,Object> parmMap) throws Exception{
   	 	delete("MbomDAO.deleteMbomMasterData", parmMap);
    }
    public void deleteMbomMasterData2(HashMap<String,Object> parmMap) throws Exception{
   	 	delete("MbomDAO.deleteMbomMasterData2", parmMap);
    }

    /**
     * 부자재 항목을 등록한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertMbomSubMaterialsData(HashMap<String,Object> parmMap) throws Exception{
   	 	insert("MbomDAO.insertMbomSubMaterialsData", parmMap);
    }

    /**
     * 부자재 항목을 수정한다.
     * @param paramMap
     * @throws Exception
     */
    public void updateMbomSubMaterialsData(HashMap<String,Object> parmMap) throws Exception{
    	update("MbomDAO.updateMbomSubMaterialsData", parmMap);
    }

    /**
     * 부자재 상세항목을 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    public HashMap<String,Object> selectSubMaterialsDetail(HashMap<String,Object> parmMap) throws Exception{
   	 return (HashMap<String,Object>) selectOne("MbomDAO.selectSubMaterialsDetail", parmMap);
    }

    /**
     * 부자재 상세항목을 삭제한다.
     * @param paramMap     * @throws Exception
     */
    public void deleteMbomSubMaterials(HashMap<String,Object> parmMap) throws Exception{
   	 	delete("MbomDAO.deleteMbomSubMaterials", parmMap);
    }

    /**
     * ebom 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    public List<?> selectPlmEbomDataSearch(HashMap<String, Object> paramMap) throws Exception {
		return selectList("MbomDAO.selectPlmEbomDataSearch", paramMap);
    }

    /**
     * ebom 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     * @throws Exception
     */
    public int selectPlmEbomDataSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return (Integer) selectOne("MbomDAO.selectPlmEbomDataSearchTotCnt", paramMap);
    }

    /**
     * ebom 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    public List<?> selectAddPlmEbomData(HashMap<String, Object> paramMap) throws Exception {
		return selectList("MbomDAO.selectAddPlmEbomData", paramMap);
    }

    /**
     * ebom 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     * @throws Exception
     */
    public int selectAddPlmEbomDataTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return (Integer) selectOne("MbomDAO.selectAddPlmEbomDataTotCnt", paramMap);
    }

    /**
     * mbom 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    public List<?> selectMbomSearch(HashMap<String, Object> paramMap) throws Exception {
		return selectList("MbomDAO.selectMbomSearch", paramMap);
    }

    /**
     * ebom 리스트 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     * @throws Exception
     */
    public int selectMbomSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return (Integer) selectOne("MbomDAO.selectMbomSearchTotCnt", paramMap);
    }

    /**
     * MBOM Final 데이터를 등록한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertMbomFinalData(HashMap<String,Object> parmMap) throws Exception{
   	 	insert("MbomDAO.insertMbomFinalData", parmMap);
    }

    /**
     * MBOM ECO REL 데이터를 등록한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertMbomEcoRelData(HashMap<String,Object> parmMap) throws Exception{
   	 	insert("MbomDAO.insertMbomEcoRelData", parmMap);
    }

    /**
     * eco 상태를 수정한다.
     * @param paramMap
     * @throws Exception
     */
    public void updateEcoState(HashMap<String,Object> parmMap) throws Exception{
    	update("MbomDAO.updateEcoState", parmMap);
    }

    /**
     * mbom 상세 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    public List<?> selectMbomDetail(HashMap<String, Object> paramMap) throws Exception {
		return selectList("MbomDAO.selectMbomDetail", paramMap);
    }

    /**
     * mbom 상세 리스트 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     * @throws Exception
     */
    public int selectMbomDetailTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return (Integer) selectOne("MbomDAO.selectMbomDetailTotCnt", paramMap);
    }

    /**
     * 파트정보를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    public List<?> selectPnoSearch(HashMap<String, Object> paramMap) throws Exception {
		return selectList("MbomDAO.selectPnoSearch", paramMap);
    }

    /**
     * 파트정보 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     * @throws Exception
     */
    public int selectPnoSearchTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return (Integer) selectOne("MbomDAO.selectPnoSearchTotCnt", paramMap);
    }

    /**
     * MBOM 부자재조합 관계 리스트를 조회한다.
     * @param paramMap
     * @return List
     * @throws Exception
     */
    public List<?> selectMbomRelationData(HashMap<String, Object> paramMap) throws Exception {
		return selectList("MbomDAO.selectMbomRelationData", paramMap);
    }

    /**
     * MBOM 부자재조합 관계 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     * @throws Exception
     */
    public int selectMbomRelationDataTotCnt(HashMap<String, Object> paramMap) throws Exception {
		return (Integer) selectOne("MbomDAO.selectMbomRelationDataTotCnt", paramMap);
    }

    /**
     * MBOM 부자재 조합을 삭제한다.
     * @param paramMap
     * @throws Exception
     */
    public void deleteMbomEcoRelData(HashMap<String,Object> parmMap) throws Exception{
   	 	delete("MbomDAO.deleteMbomEcoRelData", parmMap);
    }
    public void deleteMbomFinalData(HashMap<String,Object> parmMap) throws Exception{
   	 	delete("MbomDAO.deleteMbomFinalData", parmMap);
    }
    public void updateMbomEcoState(HashMap<String,Object> parmMap) throws Exception{
    	update("MbomDAO.updateMbomEcoState", parmMap);
    }

    /**
     * SAP 인터페이스 데이터를 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    public List<Map<String, Object>> selectSAPEcoData(String mbrno) throws Exception{
   	 	return selectList("MbomDAO.selectSAPEcoData", mbrno);
    }
    public List<Map<String, Object>> selectSAPPartData(HashMap<String,Object> parmMap) throws Exception{
    	return selectList("MbomDAO.selectSAPPartData", parmMap);
    }
    public List<Map<String, Object>> selectSAPAffectItemData(HashMap<String,Object> parmMap) throws Exception{
    	return selectList("MbomDAO.selectSAPAffectItemData", parmMap);
    }
    public List<Map<String, Object>> selectSAPMbomData(HashMap<String,Object> parmMap) throws Exception{
    	return selectList("MbomDAO.selectSAPMbomData", parmMap);
    }

    /**
     * SAP 상태를 수정한다.
     * @param paramMap
     * @throws Exception
     */
    public void updateMbomSapState(HashMap<String,Object> parmMap) throws Exception{
    	update("MbomDAO.updateMbomSapState", parmMap);
    }

    /**
     * 개정할 파트를 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    public List<Map<String, Object>> selectRevisionPartInfo(HashMap<String,Object> parmMap) throws Exception{
   	 	return selectList("MbomDAO.selectRevisionPartInfo", parmMap);
    }

    /**
     * 개정 ECO를 insert select 한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertSelectPlmEco(HashMap<String,Object> parmMap) throws Exception{
   	 	insert("MbomDAO.insertSelectPlmEco", parmMap);
    }

    /**
     * 개정 PART를 insert select 한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertSelectPlmPart(HashMap<String,Object> parmMap) throws Exception{
   	 	insert("MbomDAO.insertSelectPlmPart", parmMap);
    }

    /**
     * 개정 effectiveitem를 insert select 한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertSelectPlmEffectiveitem(HashMap<String,Object> parmMap) throws Exception{
   	 	insert("MbomDAO.insertSelectPlmEffectiveitem", parmMap);
    }

    /**
     * 개정할 ebom를 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    public List<Map<String, Object>> selectPreRevisionEbomInfo(HashMap<String,Object> parmMap) throws Exception{
   	 	return selectList("MbomDAO.selectPreRevisionEbomInfo", parmMap);
    }

    /**
     * 개정 ebom를 insert select 한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    public void insertSelectPlmEbom(HashMap<String,Object> parmMap) throws Exception{
   	 	insert("MbomDAO.insertSelectPlmEbom", parmMap);
    }


    // 비밀번호 전체 초기화
    public List<Map<String, Object>> selecthum(String temp) throws Exception{
    	return selectList("MbomDAO.selecthum", temp);
    }
    public void updatehum(HashMap<String,Object> parmMap) throws Exception{
    	update("MbomDAO.updatehum", parmMap);
    }
}