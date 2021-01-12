package com.yura.mbom.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yura.mbom.service.MbomSapIF;
import com.yura.mbom.service.MbomService;
import com.yura.mbom.service.MbomVO;
import com.yura.mbom.service.yuraemxEngrReviseIntermediate;

import egovframework.let.uss.umt.service.UserManageVO;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.util.EgovFileTool;
import egovframework.let.utl.fcc.service.EgovDateUtil;
import egovframework.let.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;

/**
 * mbom에 관한 요청을 받아 서비스 클래스로 요청을 전달하고 서비스클래스에서 처리한 결과를 웹
 * 화면으로 전달을 위한 Controller를 정의한다.
 */
@Controller
public class MbomController {
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    @Resource(name = "MbomService")
    private MbomService mbomService;

	/** 부자재 마스터 IdGnrService */
	@Resource(name="mbmtIdGnrService")
	private EgovIdGnrService mbmtIdGnrService;

	/** 부자재 IdGnrService */
	@Resource(name="mbsmIdGnrService")
	private EgovIdGnrService mbsmIdGnrService;

    /**
     * ebom 리스트를 조회한다.
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectEbomSearch.do")
    public String selectEbomSearch(HttpServletRequest request, ModelMap model) throws Exception {

        return "/com/yura/mbom/EbomSearch";
    }

    /**
	 * ebom 리스트 Data를 조회한다.
	 * @param paramMap
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/com/yura/mbom/selectEbomSearchData.do")
	public String selectEbomSearchData(HttpServletRequest request,
			@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {

		//정렬조건 처리
		/*Object sSortCol = paramMap.get("columns["+paramMap.get("order[0][column]")+"][data]");
		Object sSortDir = paramMap.get("order[0][dir]");
		paramMap.put("sSortCol", sSortCol);
		paramMap.put("sSortDir", sSortDir); */

		//paging 처리
		paramMap.put("iDisplayStart", paramMap.get("start"));
		if("-1".equals(paramMap.get("sampleGrid_length")))
			paramMap.put("iDisplayLength", "ALL");
		else
			paramMap.put("iDisplayLength", paramMap.get("length"));

		List<?> resultList = mbomService.selectEbomSearch(paramMap);
		model.addAttribute("resultList", resultList);

		int resultCnt = mbomService.selectEbomSearchTotCnt(paramMap);
		model.addAttribute("resultCnt", resultCnt);
		model.addAttribute("checkHtmlView", false);

		return "cmm/grid/resultData";
	}

	/**
     * ebom 상세항목을 조회한다.
     * @param  paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectEbomSearchDetail.do")
    public String selectEbomSearchDetail(
    		@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
    	HashMap<String,Object>resultMap = new HashMap<String,Object>();

    	String addyn = (String)paramMap.get("addyn");
    	String etcKind = (String)paramMap.get("etcKind");
	    if(etcKind != null && etcKind.equals("update")) {
    		resultMap = mbomService.selectEbomSearchDetail(paramMap);
    		resultMap.put("etcKind", "update");
	    }
	    resultMap.put("addyn", addyn);
	    model.addAttribute("ebomInfo", resultMap);
		return "/com/yura/mbom/EbomSearchDetail";
    }

    /**
     * ebom 관련 설변대상아이템 리스트 Data를 조회한다.
     * @param paramMap
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/com/yura/mbom/selectEffectiveItemSearch.do")
	public String selectEffectiveItemSearch(HttpServletRequest request,
			@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {

		//정렬조건 처리
		/*Object sSortCol = paramMap.get("columns["+paramMap.get("order[0][column]")+"][data]");
		Object sSortDir = paramMap.get("order[0][dir]");
		paramMap.put("sSortCol", sSortCol);
		paramMap.put("sSortDir", sSortDir); */

		//paging 처리
		paramMap.put("iDisplayStart", paramMap.get("start"));
		if("-1".equals(paramMap.get("length")))
			paramMap.put("iDisplayLength", "ALL");
		else
			paramMap.put("iDisplayLength", paramMap.get("length"));

		List<?> resultList = mbomService.selectEffectiveItemSearch(paramMap);
		model.addAttribute("resultList", resultList);

		int resultCnt = mbomService.selectEffectiveItemSearchTotCnt(paramMap);
		model.addAttribute("resultCnt", resultCnt);
		model.addAttribute("checkHtmlView", false);

		return "cmm/grid/resultData";
	}

	/**
     * 부자재 마스터를 조회한다.
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectSubMaterialsSearch.do")
    public String selectSubMaterialsSearch(HttpServletRequest request, ModelMap model) throws Exception {

        return "/com/yura/mbom/SubMaterialsSearch";
    }

    /**
   	 * 부자재 마스터 Data를 조회한다.
   	 * @param paramMap
   	 * @return String
   	 * @throws Exception
   	 */
   	@RequestMapping(value = "/com/yura/mbom/selectMbomMasterSearchData.do")
   	public String selectMbomMasterSearchData(HttpServletRequest request,
   			@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {

   		//정렬조건 처리
   		/*Object sSortCol = paramMap.get("columns["+paramMap.get("order[0][column]")+"][data]");
   		Object sSortDir = paramMap.get("order[0][dir]");
   		paramMap.put("sSortCol", sSortCol);
   		paramMap.put("sSortDir", sSortDir); */

   		//paging 처리
   		paramMap.put("iDisplayStart", paramMap.get("start"));
   		if("-1".equals(paramMap.get("sampleGrid_length")))
   			paramMap.put("iDisplayLength", "ALL");
   		else
   			paramMap.put("iDisplayLength", paramMap.get("length"));

   		List<?> resultList = mbomService.selectMbomMasterSearch(paramMap);
   		model.addAttribute("resultList", resultList);

   		int resultCnt = mbomService.selectMbomMasterSearchTotCnt(paramMap);
   		model.addAttribute("resultCnt", resultCnt);
   		model.addAttribute("checkHtmlView", false);

   		return "cmm/grid/resultData";
   	}

   	/**
   	 * 상위품번 Data를 조회한다.
   	 * @param paramMap
   	 * @return String
   	 * @throws Exception
   	 */
   	@RequestMapping(value = "/com/yura/mbom/selectParentPnoSearch.do")
   	public String selectParentPnoSearch(HttpServletRequest request,
   			@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {

   		//paging 처리
   		paramMap.put("iDisplayStart", paramMap.get("start"));
   		if("-1".equals(paramMap.get("length")))
   			paramMap.put("iDisplayLength", "ALL");
   		else
   			paramMap.put("iDisplayLength", paramMap.get("length"));

   		List<?> resultList = mbomService.selectParentPnoSearch(paramMap);
   		model.addAttribute("resultList", resultList);

   		int resultCnt = mbomService.selectParentPnoSearchTotCnt(paramMap);
   		model.addAttribute("resultCnt", resultCnt);
   		model.addAttribute("checkHtmlView", false);

   		return "cmm/grid/resultData";
   	}

   	/**
     * 부자재 마스터를 등록/수정 하기위한 페이지이동.
     * @param  paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectMbomMasterData.do")
    public String selectMbomMasterData(
    		@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
    	HashMap<String,Object>resultMap = new HashMap<String,Object>();

    	String etcKind = (String)paramMap.get("etcKind");
	    if(etcKind != null && etcKind.equals("update")) {
    		resultMap = mbomService.selectMbomMasterData(paramMap);
    		resultMap.put("etcKind", "update");
	    }else {
	    	resultMap.put("etcKind", "insert");
	    }
	    model.addAttribute("mbmtInfo", resultMap);
		return "/com/yura/mbom/MbomMasterData";
    }

    /**
     * 부자재 마스터를 등록/수정 한다.
     * @param paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/modifyMbomMasterData.do")
    public String modifyMbomMasterData(@RequestParam HashMap<String, Object> paramMap,
    		HttpServletRequest request, ModelMap model) throws Exception {
    	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String etcKind = (String)paramMap.get("etcKind");
    	paramMap.put("reguserid", user.getId());
    	paramMap.put("regdate", EgovDateUtil.getCurrentDate(""));
    	if(etcKind != null && etcKind.equals("insert")) {
    		paramMap.put("oid", mbmtIdGnrService.getNextStringId());
    		mbomService.insertMbomMasterData(paramMap);
        } else {
        	System.out.println((String)paramMap.get("oid"));
        	mbomService.updateMbomMasterData(paramMap);
        }

		return "/cmm/result/resultSuccess";
    }

    /**
     * 부자재마스터를 삭제한다.
     * @param paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/deleteMbomMasterData.do")
    public String deleteMbomMasterData(@RequestParam HashMap<String, Object> paramMap,
    		HttpServletRequest request, ModelMap model) throws Exception {

    	mbomService.deleteMbomMasterData(paramMap);

		return "/cmm/result/resultSuccess";
    }

    /**
     * 부자재 마스터로 페이지 이동.
     * @param  paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectSubMaterialsSearchDetail.do")
    public String selectSubMaterialsSearchDetail(
    		@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
    	HashMap<String,Object>resultMap = new HashMap<String,Object>();

    	String etcKind = (String)paramMap.get("etcKind");
	    if(etcKind != null && etcKind.equals("update")) {
    		resultMap = mbomService.selectMbomMasterData(paramMap);		///
    		resultMap.put("etcKind", "update");
	    }else {
	    	resultMap.put("etcKind", "insert");
	    }
	    model.addAttribute("mbsmInfo", resultMap);
		return "/com/yura/mbom/SubMaterialsSearchDetail";
    }

    /**
   	 * 부자재 Data를 조회한다.
   	 * @param paramMap
   	 * @return String
   	 * @throws Exception
   	 */
   	@RequestMapping(value = "/com/yura/mbom/selectSubMaterialsSearchData.do")
   	public String selectSubMaterialsSearchData(HttpServletRequest request,
   			@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {

   		//정렬조건 처리
   		/*Object sSortCol = paramMap.get("columns["+paramMap.get("order[0][column]")+"][data]");
   		Object sSortDir = paramMap.get("order[0][dir]");
   		paramMap.put("sSortCol", sSortCol);
   		paramMap.put("sSortDir", sSortDir); */

   		//paging 처리
   		paramMap.put("iDisplayStart", paramMap.get("start"));
   		if("-1".equals(paramMap.get("sampleGrid_length")))
   			paramMap.put("iDisplayLength", "ALL");
   		else
   			paramMap.put("iDisplayLength", paramMap.get("length"));

   		List<?> resultList = mbomService.selectSubMaterialsSearch(paramMap);
   		model.addAttribute("resultList", resultList);

   		int resultCnt = mbomService.selectSubMaterialsSearchTotCnt(paramMap);
   		model.addAttribute("resultCnt", resultCnt);
   		model.addAttribute("checkHtmlView", false);

   		return "cmm/grid/resultData";
   	}

    /**
     * 부자재 상세 정보 (수정/등록)
     * @param  paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectSubMaterialsDetail.do")
    public String selectSubMaterialsDetail(
    		@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
    	HashMap<String,Object>resultMap = new HashMap<String,Object>();

    	String etcKind = (String)paramMap.get("etcKind");
	    if(etcKind != null && etcKind.equals("update")) {
    		resultMap = mbomService.selectSubMaterialsDetail(paramMap);
    		resultMap.put("etcKind", "update");
    		resultMap.put("smoid", paramMap.get("smoid"));
    		resultMap.put("mtoid", paramMap.get("mtoid"));
	    }else {
	    	resultMap.put("etcKind", "insert");
	    	resultMap.put("pno", paramMap.get("pno"));
    		resultMap.put("mtoid", paramMap.get("mtoid"));
	    }
	    model.addAttribute("mbsmdInfo", resultMap);
		return "/com/yura/mbom/SubMaterialsDetail";
    }

    /**
     * 부자재 상세정보를 등록/수정 한다.
     * @param paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/modifyMbomSubMaterials.do")
    public String modifyMbomSubMaterials(@RequestParam HashMap<String, Object> paramMap,
    		HttpServletRequest request, ModelMap model) throws Exception {
    	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String etcKind = (String)paramMap.get("etcKind");
    	paramMap.put("reguserid", user.getId());
    	paramMap.put("regdate", EgovDateUtil.getCurrentDate(""));
    	if(etcKind != null && etcKind.equals("insert")) {
    		paramMap.put("smoid", mbsmIdGnrService.getNextStringId());
        	mbomService.insertMbomSubMaterialsData(paramMap);
        } else {
        	mbomService.updateMbomSubMaterialsData(paramMap);
        }

		return "/cmm/result/resultSuccess";
    }

    /**
     * 부자재 상세정보를 삭제한다.
     * @param paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/deleteMbomSubMaterials.do")
    public String deleteMbomSubMaterials(@RequestParam HashMap<String, Object> paramMap,
    		HttpServletRequest request, ModelMap model) throws Exception {

    	mbomService.deleteMbomSubMaterials(paramMap);

		return "/cmm/result/resultSuccess";
    }

    /**
     * mbom 리스트를 조회한다.
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectMbomSearch.do")
    public String selectMbomSearch(HttpServletRequest request, ModelMap model) throws Exception {

        return "/com/yura/mbom/MbomSearch";
    }

    /**
     * ebom 정보를 조회/추가 하기위한 페이지이동.
     * @param  paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectPlmEbomData.do")
    public String selectPlmEbomData(
    		@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
    	HashMap<String,Object>resultMap = new HashMap<String,Object>();
    	String etcKind = (String)paramMap.get("etcKind");
    	String econo = (String)paramMap.get("econo");
    	String tmpaddoid = (String)paramMap.get("tmpaddoid");
    	String parentpno = (String)paramMap.get("parentpno");
    	String parentrev = (String)paramMap.get("parentrev");
    	String ecosuboid = (String)paramMap.get("ecosuboid");
    	String accumulatedata = (String)paramMap.get("accumulatedata");
    	String oripno = (String)paramMap.get("oripno");
    	String orirev = (String)paramMap.get("orirev");

    	if(etcKind != null && etcKind.equals("search")) {
    		resultMap.put("etcKind", "search");
	    }else {		// etcKind == add
	    	resultMap.put("etcKind", "add");
	    }

	    if(tmpaddoid != null) {
	    	resultMap.put("tmpaddoid", tmpaddoid);
	    }
	    if(parentpno != null && parentrev != null) {
	    	resultMap.put("parentpno", parentpno);
	    	resultMap.put("parentrev", parentrev);
	    	resultMap.put("ecosuboid", ecosuboid);
	    	resultMap.put("accumulatedata", accumulatedata);
	    }

	    resultMap.put("oripno", oripno);
	    resultMap.put("orirev", orirev);
	    resultMap.put("econo", econo);
	    model.addAttribute("ebomInfo2", resultMap);
		return "/com/yura/mbom/PlmEbomData";
    }

    /**
     * 상위품번 선택 하기위한 페이지이동.
     * @param  paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectPlmEbomData3.do")
    public String selectPlmEbomData3(
    		@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
    	HashMap<String,Object>resultMap = new HashMap<String,Object>();
    	String etcKind = (String)paramMap.get("etcKind");
    	String econo = (String)paramMap.get("econo");
    	String tmpaddoid = (String)paramMap.get("tmpaddoid");
    	String ecosuboid = (String)paramMap.get("ecosuboid");
    	String accumulatedata = (String)paramMap.get("accumulatedata");
    	String oripno = (String)paramMap.get("oripno");
    	String orirev = (String)paramMap.get("orirev");

    	if(etcKind != null && etcKind.equals("search")) {
    		resultMap.put("etcKind", "search");
	    }else {		// etcKind == add
	    	resultMap.put("etcKind", "add");
	    }

	    if(tmpaddoid != null) {
	    	resultMap.put("tmpaddoid", tmpaddoid);
	    	resultMap.put("ecosuboid", ecosuboid);
	    	resultMap.put("accumulatedata", accumulatedata);
	    }

	    resultMap.put("oripno", oripno);
	    resultMap.put("orirev", orirev);
	    resultMap.put("econo", econo);
	    model.addAttribute("ebomInfo3", resultMap);
		return "/com/yura/mbom/PlmEbomData3";
    }

    /**
     * ebom 정보를 조회/추가 하기위한 페이지이동2.
     * @param  paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectPlmEbomData2.do")
    public String selectPlmEbomData2(
    		@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
    	HashMap<String,Object>resultMap = new HashMap<String,Object>();

    	String econo = (String)paramMap.get("econo");
    	String oripno = (String)paramMap.get("oripno");
    	String orirev = (String)paramMap.get("orirev");
    	String ecosuboid = (String)paramMap.get("ecosuboid");
    	String accumulatedata = (String)paramMap.get("accumulatedata");

    	resultMap.put("oripno", oripno);
    	resultMap.put("orirev", orirev);
    	resultMap.put("econo", econo);
    	resultMap.put("ecosuboid", ecosuboid);
    	resultMap.put("accumulatedata", accumulatedata);
    	System.out.println(resultMap.get("accumulatedata"));

	    model.addAttribute("submInfo", resultMap);
		return "/com/yura/mbom/PlmEbomData2";
    }

    /**
     * ebom 정보를 조회한다.
     * @param paramMap
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/com/yura/mbom/selectPlmEbomDataSearch.do")
	public String selectPlmEbomDataSearch(HttpServletRequest request,
			@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
		//정렬조건 처리
		/*Object sSortCol = paramMap.get("columns["+paramMap.get("order[0][column]")+"][data]");
		Object sSortDir = paramMap.get("order[0][dir]");
		paramMap.put("sSortCol", sSortCol);
		paramMap.put("sSortDir", sSortDir); */

		//paging 처리
		/*paramMap.put("iDisplayStart", paramMap.get("start"));
		if("-1".equals(paramMap.get("sampleGrid_length")))
			paramMap.put("iDisplayLength", "ALL");
		else
			paramMap.put("iDisplayLength", paramMap.get("length"));*/

		List<?> resultList = mbomService.selectPlmEbomDataSearch(paramMap);
		model.addAttribute("resultList", resultList);

		int resultCnt = mbomService.selectPlmEbomDataSearchTotCnt(paramMap);
		model.addAttribute("resultCnt", resultCnt);
		model.addAttribute("checkHtmlView", false);

		return "cmm/grid/resultData";
	}

	/**
     * ebom 정보를 조회한다.
     * @param paramMap
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/com/yura/mbom/selectAddPlmEbomData.do")
	public String selectAddPlmEbomData(HttpServletRequest request,
			@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
		//정렬조건 처리
		/*Object sSortCol = paramMap.get("columns["+paramMap.get("order[0][column]")+"][data]");
		Object sSortDir = paramMap.get("order[0][dir]");
		paramMap.put("sSortCol", sSortCol);
		paramMap.put("sSortDir", sSortDir); */

		//paging 처리
		paramMap.put("iDisplayStart", paramMap.get("start"));
		if("-1".equals(paramMap.get("sampleGrid_length")))
			paramMap.put("iDisplayLength", "ALL");
		else
			paramMap.put("iDisplayLength", paramMap.get("length"));

		List<?> resultList = mbomService.selectAddPlmEbomData(paramMap);
		model.addAttribute("resultList", resultList);

		int resultCnt = mbomService.selectAddPlmEbomDataTotCnt(paramMap);

		model.addAttribute("resultCnt", resultCnt);
		model.addAttribute("checkHtmlView", false);

		return "cmm/grid/resultData";
	}

	/**
	 * mbom 리스트 Data를 조회한다.
	 * @param paramMap
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/com/yura/mbom/selectMbomSearchData.do")
	public String selectMbomSearchData(HttpServletRequest request,
			@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {

		//정렬조건 처리
		/*Object sSortCol = paramMap.get("columns["+paramMap.get("order[0][column]")+"][data]");
		Object sSortDir = paramMap.get("order[0][dir]");
		paramMap.put("sSortCol", sSortCol);
		paramMap.put("sSortDir", sSortDir); */

		//paging 처리
		paramMap.put("iDisplayStart", paramMap.get("start"));
		if("-1".equals(paramMap.get("sampleGrid_length")))
			paramMap.put("iDisplayLength", "ALL");
		else
			paramMap.put("iDisplayLength", paramMap.get("length"));

		List<?> resultList = mbomService.selectMbomSearch(paramMap);
		model.addAttribute("resultList", resultList);

		int resultCnt = mbomService.selectMbomSearchTotCnt(paramMap);
		model.addAttribute("resultCnt", resultCnt);
		model.addAttribute("checkHtmlView", false);

		return "cmm/grid/resultData";
	}

	/**
     * MBOM Final 데이터를 등록 한다.
     * @param paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/insertMbomFinalData.do")
    public String insertMbomFinalData(@RequestParam HashMap<String, Object> paramMap,
    		HttpServletRequest request, ModelMap model) throws Exception {
    	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	paramMap.put("reguserid", user.getId());

        mbomService.insertMbomFinalData(paramMap);

		return "/cmm/result/resultSuccess";
    }

    /**
     * mbom 상세조회  페이지이동.
     * @param  paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectMbomSearchDetail.do")
    public String selectMbomSearchDetail(
    		@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
    	HashMap<String,Object>resultMap = new HashMap<String,Object>();
    	String mbo = (String)paramMap.get("mbo");
    	String eco = (String)paramMap.get("eco");
    	String partno = (String)paramMap.get("partno");
    	String partrev = (String)paramMap.get("partrev");
    	String sapyn = (String)paramMap.get("sapyn");

	    resultMap.put("mbo", mbo);
	    resultMap.put("eco", eco);
	    resultMap.put("partno", partno);
	    resultMap.put("partrev", partrev);
	    resultMap.put("sapyn", sapyn);
	    model.addAttribute("mbomInfo", resultMap);

		return "/com/yura/mbom/MbomSearchDetail";
    }

    /**
     * mbom 상세정보를 조회한다.
     * @param paramMap
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/com/yura/mbom/selectMbomDetailData.do")
	public String selectMbomDetailData(HttpServletRequest request,
			@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
		List<?> resultList = mbomService.selectMbomDetail(paramMap);
		model.addAttribute("resultList", resultList);

		int resultCnt = mbomService.selectMbomDetailTotCnt(paramMap);
		model.addAttribute("resultCnt", resultCnt);
		model.addAttribute("checkHtmlView", false);

		return "cmm/grid/resultData";
	}

	/**
     * 부자재 등록 파트번호 선택하기 위한 페이지이동.
     * @param  paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectSubPartNo.do")
    public String selectSubPartNo(
    		@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
    	HashMap<String,Object>resultMap = new HashMap<String,Object>();
    	resultMap.put("mtoid", paramMap.get("mtoid"));
    	model.addAttribute("pnoInfo", resultMap);

		return "/com/yura/mbom/SubMaterialsSelectPno";
    }

    /**
   	 * 파트정보를 조회한다.
   	 * @param paramMap
   	 * @return String
   	 * @throws Exception
   	 */
   	@RequestMapping(value = "/com/yura/mbom/selectPnoSearch.do")
   	public String selectPnoSearch(HttpServletRequest request,
   			@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {

   		//paging 처리
   		paramMap.put("iDisplayStart", paramMap.get("start"));
   		if("-1".equals(paramMap.get("sampleGrid_length")))
   			paramMap.put("iDisplayLength", "ALL");
   		else
   			paramMap.put("iDisplayLength", paramMap.get("length"));

   		List<?> resultList = mbomService.selectPnoSearch(paramMap);
   		model.addAttribute("resultList", resultList);

   		int resultCnt = mbomService.selectPnoSearchTotCnt(paramMap);
   		model.addAttribute("resultCnt", resultCnt);
   		model.addAttribute("checkHtmlView", false);

   		return "cmm/grid/resultData";
   	}

   	/**
     * MBOM 부자재조합 관계 리스트 Data를 조회한다.
     * @param paramMap
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/com/yura/mbom/selectMbomRelationData.do")
	public String selectMbomRelationData(HttpServletRequest request,
			@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {

		//paging 처리
		paramMap.put("iDisplayStart", paramMap.get("start"));
		if("-1".equals(paramMap.get("sampleGrid_length")))
			paramMap.put("iDisplayLength", "ALL");
		else
			paramMap.put("iDisplayLength", paramMap.get("length"));

		List<?> resultList = mbomService.selectMbomRelationData(paramMap);
		model.addAttribute("resultList", resultList);

		int resultCnt = mbomService.selectMbomRelationDataTotCnt(paramMap);
		model.addAttribute("resultCnt", resultCnt);
		model.addAttribute("checkHtmlView", false);

		return "cmm/grid/resultData";
	}

	/**
     * mbom 부자재 조합 조회  페이지이동.
     * @param  paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/selectMbomFinalSearch.do")
    public String selectMbomFinalSearch(
    		@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
    	HashMap<String,Object>resultMap = new HashMap<String,Object>();
    	String mbo = (String)paramMap.get("mbo");
    	String eco = (String)paramMap.get("eco");
	    resultMap.put("mbo", mbo);
	    resultMap.put("eco", eco);
	    model.addAttribute("mbomFinal", resultMap);

		return "/com/yura/mbom/MbomFinalSearch";
    }

    /**
     * MBOM 부자재 조합을 삭제한다.
     * @param paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/deleteMbomSubItemRel.do")
    public String deleteMbomSubItemRel(@RequestParam HashMap<String, Object> paramMap,
    		HttpServletRequest request, ModelMap model) throws Exception {

    	mbomService.deleteMbomSubItemRel(paramMap);

		return "/cmm/result/resultSuccess";
    }

    /**
     * MBOM SAP 데이터 인터페이스.
     * @param paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/exeSapInterface.do")
    public String exeSapInterface(@RequestParam HashMap<String, Object> paramMap,
    		HttpServletRequest request, ModelMap model) throws Exception {

    	String strmbrno = (String)paramMap.get("strmbrno");
    	String[] arr = strmbrno.split(";");
    	String strecorev = (String)paramMap.get("strecorev");
    	String[] arr2 = strecorev.split(";");
    	String resultMsg = "";

		for (int i=0; i < arr.length; i++){
			List<Map<String, Object>>ecoInfo = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>>partInfo = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>>affectitemInfo = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>>mbomInfo = new ArrayList<Map<String, Object>>();

			try {

				String mbrno = arr[i];
				ecoInfo = mbomService.selectSAPEcoData(mbrno);

				String ecorev = arr2[i];
				String strBUKRS = (String)ecoInfo.get(0).get("erperpsite");
				String econo = (String)ecoInfo.get(0).get("econo");
				HashMap<String, Object>para = new HashMap<String,Object>();
				para.put("econo", econo);
				para.put("ecorev", ecorev);
				partInfo = mbomService.selectSAPPartData(para);
				affectitemInfo = mbomService.selectSAPAffectItemData(para);

				HashMap<String, Object>numbers = new HashMap<String,Object>();
		    	numbers.put("mbo", mbrno);
		    	numbers.put("eco", econo);
				mbomInfo = mbomService.selectSAPMbomData(numbers);

				System.out.println(partInfo);
				System.out.println(affectitemInfo);
				System.out.println(mbomInfo);

				// SAP IF
				MbomSapIF mbomif = new MbomSapIF();
				if (strBUKRS.indexOf("|") > -1) {
					// erpsite 여러개
					String[] sites = strBUKRS.split("\\|");
					for (int k=0; k < sites.length; k++) {
						if (sites[k].equals("1000") || sites[k].equals("3000") || sites[k].equals("2150")) {		// 국내 / 유럽 / 하택
							resultMsg = mbomif.executeSapInterface(sites[k], ecoInfo, partInfo, affectitemInfo, mbomInfo, econo, mbrno);
						}
					}
				} else {
					// erpsite 한개
					if (strBUKRS.equals("1000") || strBUKRS.equals("3000") || strBUKRS.equals("2150")) {		// 국내 / 유럽 / 하택
						resultMsg = mbomif.executeSapInterface(strBUKRS, ecoInfo, partInfo, affectitemInfo, mbomInfo, econo, mbrno);
					}
				}

				if (resultMsg.equals("Success")) {
					mbomService.updateMbomSapState(numbers);
				}

			}catch(Exception e){
				System.out.println("*************** MBOM IF [ error ] ***************");
				resultMsg = "Fail";
				e.printStackTrace();
			}
		}

		model.addAttribute("resultMsg", resultMsg);
    	return "/cmm/result/result";
    }

    /**
     * 파트 개정을 위한 페이지이동.
     * @param  paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/revisionPart.do")
    public String revisionPart(
    		@RequestParam HashMap<String, Object> paramMap, ModelMap model) throws Exception {
    	HashMap<String,Object>resultMap = new HashMap<String,Object>();
    	String econo = (String)paramMap.get("econo");
    	String oripno = (String)paramMap.get("oripno");
    	String orirev = (String)paramMap.get("orirev");

	    resultMap.put("econo", econo);
	    resultMap.put("oripno", oripno);
	    resultMap.put("orirev", orirev);
	    model.addAttribute("revPart", resultMap);
		return "/com/yura/mbom/RevisionPart";
    }

    /**
     * PLM 파트를 개정 한다.
     * @param paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/revisionPLMPart.do")
    public String revisionPLMPart(@RequestParam HashMap<String, Object> paramMap,
    		HttpServletRequest request, ModelMap model) throws Exception {

    	String econo = (String)paramMap.get("econo");
    	String level = (String)paramMap.get("level");
    	String oripno = (String)paramMap.get("oripno");
    	String orirev = (String)paramMap.get("orirev");
    	String pno = (String)paramMap.get("pno");
    	String rev = (String)paramMap.get("rev");
    	String[] arrpno = pno.split(";");
    	String[] arrrev = rev.split(";");
    	String resultMsg = "";
    	String flag = "F";

    	try {
	    	for (int i=0; i < arrpno.length; i++){
    			List<Map<String, Object>> partInfo = new ArrayList<Map<String, Object>>();
    			HashMap<String, Object>param = new HashMap<String,Object>();
    			param.put("pno", arrpno[i]);
    			param.put("rev", arrrev[i]);
    			param.put("level", level);
    			param.put("econo", econo);
    			partInfo = mbomService.selectRevisionPartInfo(param);

    			yuraemxEngrReviseIntermediate emx = new yuraemxEngrReviseIntermediate();
    	    	resultMsg = emx.executePLMPartRevision(partInfo, level);

    	    	if (resultMsg.equals("Success")) {
    	    		flag = "T";
    	    		mbomService.insertSelectPlmEffectiveitem(param);
        	    	mbomService.insertSelectPlmPart(param);
        	    	mbomService.insertSelectPlmEbom(param);
    	    	}
	    	}
	    	if (flag.equals("T")) {
	    		HashMap<String, Object>ecoMap = new HashMap<String,Object>();
		    	ecoMap.put("econo", econo);
		    	ecoMap.put("pno", oripno);
		    	ecoMap.put("pnorev", orirev);
		    	ecoMap.put("level", level);
		    	mbomService.insertSelectPlmEco(ecoMap);
	    	}
    	}catch(Exception e){
			System.out.println("*************** PART REVISION [ ERROR ] ***************");
			resultMsg = "Fail";
			e.printStackTrace();
		}
    	model.addAttribute("resultMsg", resultMsg);
    	return "/cmm/result/result";
    }

    /**
     * PLM 파트를 개정 한다.	----------------- 테스트
     * @param paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/revisionPLMPartTest.do")
    public String revisionPLMPartTest(@RequestParam HashMap<String, Object> paramMap,
    		HttpServletRequest request, ModelMap model) throws Exception {

    	String econo = "ECO200910-01";
    	String level = "07";
    	String oripno = "91950-R0000-19";
    	String mat = "03";
    	String pno = "ZKA4PCBASM10;";
    	String rev = "03;";
    	String[] arrpno = pno.split(";");
    	String[] arrrev = rev.split(";");
    	String resultMsg = "";
    	String flag = "F";

    	try {
	    	for (int i=0; i < arrpno.length; i++){
	    		System.out.println("--- "+ arrpno[i]);
    			List<Map<String, Object>> partInfo = new ArrayList<Map<String, Object>>();
    			HashMap<String, Object>param = new HashMap<String,Object>();
    			param.put("pno", arrpno[i]);
    			param.put("rev", arrrev[i]);
    			param.put("level", level);
    			param.put("econo", econo);
    			partInfo = mbomService.selectRevisionPartInfo(param);

    			yuraemxEngrReviseIntermediate emx = new yuraemxEngrReviseIntermediate();
    	    	resultMsg = emx.executePLMPartRevision(partInfo, level);

    	    	if (resultMsg.equals("Success")) {
    	    		flag = "T";
    	    		mbomService.insertSelectPlmEffectiveitem(param);
        	    	mbomService.insertSelectPlmPart(param);
        	    	mbomService.insertSelectPlmEbom(param);
        	    	System.out.println("Success");
    	    	}
	    	}
	    	/*if (flag.equals("T")) {
	    		HashMap<String, Object>ecoMap = new HashMap<String,Object>();
		    	ecoMap.put("econo", econo);
		    	ecoMap.put("pno", oripno);
		    	ecoMap.put("pnorev", orirev);
		    	ecoMap.put("level", level);
		    	mbomService.insertSelectPlmEco(ecoMap);
	    	}*/
    	}catch(Exception e){
			System.out.println("*************** PART REVISION [ ERROR ] ***************");
			resultMsg = "Fail";
			e.printStackTrace();
		}
    	model.addAttribute("resultMsg", resultMsg);
    	return "/cmm/result/result";
    }


    /**
     * 유저 비밀번호 전체 초기화.
     * @param paramMap
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/com/yura/mbom/userPasswordInit.do")
    public String userPasswordInit(@RequestParam HashMap<String, Object> paramMap,
    		HttpServletRequest request, ModelMap model) throws Exception {

    	String resultMsg = "Success";

    	System.out.println("--- start");

    	List<Map<String, Object>>humInfo = new ArrayList<Map<String, Object>>();
    	humInfo = mbomService.selecthum("a");

    	for (int i=0; i < humInfo.size(); i++) {
    		String uniqId = (String) humInfo.get(i).get("esntlid");
    		String emplyrId = (String) humInfo.get(i).get("emplyrid");
    		String newPassword = "y"+emplyrId;
    		//패스워드 암호화
    		String encryptPass = EgovFileScrty.encryptPassword(newPassword, emplyrId);
    		HashMap<String, Object>para = new HashMap<String,Object>();
    		para.put("uniqId", uniqId);
			para.put("encryptPass", encryptPass);
			mbomService.updatehum(para);
    	}

    	System.out.println("--- end");

    	model.addAttribute("resultMsg", resultMsg);
    	return "/cmm/result/result";
    }

}