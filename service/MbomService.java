package com.yura.mbom.service;

import java.util.List;
import java.util.Map;

import com.yura.mbom.service.MbomVO;

import java.util.HashMap;

/**
 * mbom VO 서비스 인터페이스 클래스를 정의한다.
 */
public interface MbomService {
	/**
     * ebom(eco) 리스트를 조회한다.
     * @param parmVO
     * @return List
     * @throws Exception
     */
    public List<?> selectEbomSearch(HashMap<String, Object> paramMap) throws Exception;

	/**
     * ebom(eco) 리스트 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     */
    public int selectEbomSearchTotCnt(HashMap<String, Object> paramMap) throws Exception;

	/**
     * ebom 상세항목을 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    public HashMap<String,Object> selectEbomSearchDetail(HashMap<String,Object> parmMap) throws Exception;

    /**
     * 설변대상아이템 리스트를 조회한다.
     * @param parmVO
     * @return List
     * @throws Exception
     */
    public List<?> selectEffectiveItemSearch(HashMap<String, Object> paramMap) throws Exception;

    /**
     * 설변대상아이템 리스트 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     */
	public int selectEffectiveItemSearchTotCnt(HashMap<String, Object> paramMap) throws Exception;

	/**
     * 부자재마스터 리스트를 조회한다.
     * @param parmVO
     * @return List
     * @throws Exception
     */
    public List<?> selectMbomMasterSearch(HashMap<String, Object> paramMap) throws Exception;

    /**
     * 부자재마스터 리스트 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     */
    public int selectMbomMasterSearchTotCnt(HashMap<String, Object> paramMap) throws Exception;

    /**
     * 상위품번 리스트를 조회한다.
     * @param parmVO
     * @return List
     * @throws Exception
     */
    public List<?> selectParentPnoSearch(HashMap<String, Object> paramMap) throws Exception;

    /**
     * 상위품번 리스트 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     */
    public int selectParentPnoSearchTotCnt(HashMap<String, Object> paramMap) throws Exception;

    /**
     * 부자재마스터를 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    public HashMap<String,Object> selectMbomMasterData(HashMap<String,Object> parmMap) throws Exception;

    /**
     * 부자재 리스트를 조회한다.
     * @param parmVO
     * @return List
     * @throws Exception
     */
    public List<?> selectSubMaterialsSearch(HashMap<String, Object> paramMap) throws Exception;

    /**
     * 부자재 리스트 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     */
    public int selectSubMaterialsSearchTotCnt(HashMap<String, Object> paramMap) throws Exception;

    /**
     * 코드 제너레이션 샘플 데이터 중복체크를 한다.
     * @param parmVO
     * @return int
     * @throws Exception
     */
    public int selectDupleChk(MbomVO parmVO) throws Exception;

    /**
     * 부자재마스터 항목을 등록한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertMbomMasterData(HashMap<String,Object> parmMap) throws Exception;

    /**
     * 부자재마스터 항목을 수정한다.
     * @param paramMap
     * @throws Exception
     */
    public void updateMbomMasterData(HashMap<String,Object> parmMap) throws Exception;

    /**
     * 부자재마스터 항목을 삭제한다.
     * @param paramMap
     * @throws Exception
     */
    public void deleteMbomMasterData(HashMap<String,Object> parmMap) throws Exception;

    /**
     * 부자재 항목을 등록한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertMbomSubMaterialsData(HashMap<String,Object> parmMap) throws Exception;

    /**
     * 부자재 항목을 수정한다.
     * @param paramMap
     * @throws Exception
     */
    public void updateMbomSubMaterialsData(HashMap<String,Object> parmMap) throws Exception;

    /**
     * 부자재를 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    public HashMap<String,Object> selectSubMaterialsDetail(HashMap<String,Object> parmMap) throws Exception;

    /**
     * 부자재 상세정보를 삭제한다.
     * @param paramMap
     * @throws Exception
     */
    public void deleteMbomSubMaterials(HashMap<String,Object> parmMap) throws Exception;

    /**
     * ebom 리스트를 조회한다.
     * @param parmVO
     * @return List
     * @throws Exception
     */
    public List<?> selectPlmEbomDataSearch(HashMap<String, Object> paramMap) throws Exception;

    /**
     * ebom 리스트 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     */
	public int selectPlmEbomDataSearchTotCnt(HashMap<String, Object> paramMap) throws Exception;

	/**
     * ebom 리스트를 조회한다.
     * @param parmVO
     * @return List
     * @throws Exception
     */
    public List<?> selectAddPlmEbomData(HashMap<String, Object> paramMap) throws Exception;

    /**
     * ebom 리스트 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     */
	public int selectAddPlmEbomDataTotCnt(HashMap<String, Object> paramMap) throws Exception;

	/**
     * mbom 리스트를 조회한다.
     * @param parmVO
     * @return List
     * @throws Exception
     */
    public List<?> selectMbomSearch(HashMap<String, Object> paramMap) throws Exception;

	/**
     * mbom 리스트 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     */
    public int selectMbomSearchTotCnt(HashMap<String, Object> paramMap) throws Exception;

    /**
     * MBOM Final 데이터를 등록한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertMbomFinalData(HashMap<String,Object> parmMap) throws Exception;

    /**
     * mbom 상세 리스트를 조회한다.
     * @param parmVO
     * @return List
     * @throws Exception
     */
    public List<?> selectMbomDetail(HashMap<String, Object> paramMap) throws Exception;

    /**
     * mbom 상세 리스트 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     */
	public int selectMbomDetailTotCnt(HashMap<String, Object> paramMap) throws Exception;

	/**
     * 파트정보를 조회한다.
     * @param parmVO
     * @return List
     * @throws Exception
     */
    public List<?> selectPnoSearch(HashMap<String, Object> paramMap) throws Exception;

	/**
     * 파트정보 총 갯수를 조회한다.
     * @param parmVO
     * @return int
     */
    public int selectPnoSearchTotCnt(HashMap<String, Object> paramMap) throws Exception;

    /**
     * MBOM 부자재조합 관계 리스트를 조회한다.
     * @param parmVO
     * @return List
     * @throws Exception
     */
    public List<?> selectMbomRelationData(HashMap<String, Object> paramMap) throws Exception;

    /**
     * MBOM 부자재조합 관계 리스트 총 갯수를 조회한다.
     * @param paramMap
     * @return int
     */
	public int selectMbomRelationDataTotCnt(HashMap<String, Object> paramMap) throws Exception;

	/**
     * MBOM 부자재 조합을 삭제한다.
     * @param paramMap
     * @throws Exception
     */
    public void deleteMbomSubItemRel(HashMap<String,Object> parmMap) throws Exception;

    /**
     * SAP 인터페이스 데이터를 조회한다.
     * @param parmMap
     * @return HashMap
     * @throws Exception
     */
    public List<Map<String, Object>> selectSAPEcoData(String mbrno) throws Exception;
    public List<Map<String, Object>> selectSAPPartData(HashMap<String, Object> paramMap) throws Exception;
    public List<Map<String, Object>> selectSAPAffectItemData(HashMap<String, Object> paramMap) throws Exception;
    public List<Map<String, Object>> selectSAPMbomData(HashMap<String, Object> paramMap) throws Exception;

    /**
     * SAP 상태를 수정한다.
     * @param paramMap
     * @throws Exception
     */
    public void updateMbomSapState(HashMap<String, Object> paramMap) throws Exception;

    /**
     * 개정할 파트를 조회한다.
     * @param paramMap
     * @throws Exception
     */
    public List<Map<String, Object>> selectRevisionPartInfo(HashMap<String, Object> paramMap) throws Exception;

    /**
     * 개정 ECO를 insert select 한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertSelectPlmEco(HashMap<String,Object> parmMap) throws Exception;

    /**
     * 개정 PART를 insert select 한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertSelectPlmPart(HashMap<String,Object> parmMap) throws Exception;

    /**
     * 개정 effectiveitem를 insert select 한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertSelectPlmEffectiveitem(HashMap<String,Object> parmMap) throws Exception;

    /**
     * 개정 ebom를 insert select 한다.
     * @param paramMap
     * @throws Exception
     */
    public void insertSelectPlmEbom(HashMap<String,Object> parmMap) throws Exception;

    // 비밀번호 전체 초기화
    public List<Map<String, Object>> selecthum(String temp) throws Exception;
    public void updatehum(HashMap<String, Object> paramMap) throws Exception;
}