package com.yura.mbom.service;

import java.util.HashMap;

import egovframework.com.cmm.ComDefaultVO;

/**
 * MBOM VO 클래스를 정의한다.
 */
@SuppressWarnings("serial")
public class MbomVO extends ComDefaultVO {
   
	/*
	 * ECO 번호 
 	 */   
	private String econo;
   
	/*
	 * 파트 번호
 	 */   
	private String pno;
   
	/*
	 * 제목
 	 */   
	private String title;
   
	/*
	 * 차종
 	 */   
	private String carcode;
   
	/*
	 * erpsite
 	 */   
	private String erpsite;
   
	/*
	 * 설명
 	 */   
	private String description;
   
	/*
	 * 개정 전
 	 */   
	private String erprevbefore;
   
	/*
	 * 개정 후
 	 */   
	private String erprevafter;
   
	/*
	 * eono
 	 */   
	private String eono;
   
	/*
	 * 개발 단계 
 	 */   
	private String devstep;
   
	/*
	 * 파트 이름
 	 */   
	private String partname;
   
	/*
	 * epdpno
 	 */   
	private String epdpno;
   
	/*
	 * 등록자
 	 */   
	private String humname;
   
	/*
	 * 등록일
 	 */   
	private String regdate;
   
	/*
	 * Map결과 정보
 	 */   
	private HashMap<String,Object> resultMap;
	
	public String getEcono() {
		return econo;
	}
   
	public String getPno() {
		return pno;
	}
   
	public String getTitle() {
		return title;
	}
   
	public String getCarcode() {
		return carcode;
	}
   
	public String getErpsite() {
		return erpsite;
	}
   
	public String getDescription() {
		return description;
	}
   
	public String getErprevbefore() {
		return erprevbefore;
	}
   
	public String getErprevafter() {
		return erprevafter;
	}
   
	public String getEono() {
		return eono;
	}
   
	public String getDevstep() {
		return devstep;
	}
   
	public String getPartname() {
		return partname;
	}
   
	public String getEpdpno() {
		return epdpno;
	}
   
	public String getHumname() {
		return humname;
	}
   
	public String getRegdate() {
		return regdate;
	}
	
	public HashMap<String,Object> getResultMap() {
		return resultMap;
	}
   
	public void setEcono(String econo) {
		this.econo = econo;
	}
   
	public void setPno(String pno) {
		this.pno = pno;
	}
   
	public void setTitle(String title) {
		this.title = title;
	}
   
	public void setCarcode(String carcode) {
		this.carcode = carcode;
	}
   
	public void setErpsite(String erpsite) {
		this.erpsite = erpsite;
	}
   
	public void setDescription(String description) {
		this.description = description;
	}
   
	public void setErprevbefore(String erprevbefore) {
		this.erprevbefore = erprevbefore;
	}
   
	public void setErprevafter(String erprevafter) {
		this.erprevafter = erprevafter;
	}
   
	public void setEono(String eono) {
		this.eono = eono;
	}
   
	public void setDevstep(String devstep) {
		this.devstep = devstep;
	}
   
	public void setPartname(String partname) {
		this.partname = partname;
	}
   
	public void setEpdpno(String epdpno) {
		this.epdpno = epdpno;
	}
   
	public void setHumname(String humname) {
		this.humname = humname;
	}
   
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
   
	public void setResultMap(HashMap<String,Object> resultMap) {
		this.resultMap = resultMap;
	}  
}