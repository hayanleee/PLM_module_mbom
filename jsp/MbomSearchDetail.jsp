<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui" %>
<%@ taglib prefix="sec" 	  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt"%>  
<%@ include file="/WEB-INF/jsp/cmm/grid/dataTables-header.jsp"%>        
<%@ include file="/WEB-INF/jsp/cmm/fileUpload-header.jsp" %>         
<!DOCTYPE html>
<html>      
<head>  
<meta charset="utf-8">
<title>MBOM 상세조회</title> 
<script type="text/javaScript">

jQuery('#mbomInfo').parsley();

jQuery(document).ready(function() {
	setMbomRelationDataGrid();
	
	if ($('#sapyn').val() == 'Y') {
		$('#mbomDelBtn').prop('disabled', true);
	}
});

function fnBack(){
	jLoding('show');
	jQuery.ajax({
		type:'post',
		url : "<c:url value='/com/yura/mbom/selectMbomSearch.do'/>",
		dataType: "html",
		success : function (data){
			if(data == 'fail'){
				resultMsg(data);
			} else {    
				jQuery('div#contents').html(data);
			}					
			jLoding('hide');
		},
		error : function(xhr, status, error){
			resultMsg("fail");
		}
	});		
}

// MBOM 부자재 조합 relation 데이터그리드 셋팅
function setMbomRelationDataGrid() {
	var table = jQuery('#mbomRelationDataGrid').on( 'processing.dt', function ( e, settings, processing ) {
		processing ? jLoding('show') : jLoding('hide');
    }).DataTable( { 
		ajax: {    
		    url: "<c:url value='/com/yura/mbom/selectMbomRelationData.do'/>",
		    dataSrc: function ( json ) {
                return json.data;
		    },
		    type: "POST",			/* 전송방식 */
            data: {
            	mbrno : $('#mbo').val()
            }              
		},
        serverSide: true,			/* 서버단에서 처리 */
        deferRender: true,			/* 대용량 데이타 화면출력시 속도 개선 옵션 */
		columns: [
			{ data: 'parentpno'},
			{ data: 'parentrev'},
			{ data: 'name'}
		],
		searching: false,			/* 필터검색 여부 */
		language: language_locale,
		scrollX: true,    
		scrollY: '200px',                                                                   
	    fnInitComplete : function() {
	    	jQuery("#mbomRelationDataGrid tbody tr").css('cursor', 'pointer');
	    	jLoding('hide');       
		}
	});
	
    jQuery('#mbomRelationDataGrid tbody').on('click', 'tr', function () {
        var data = table.row( this ).data();
        
    });
	
	//버튼 셋팅
	_buttonSetting(table);
}

// MBOM 조합 삭제
function mbomDelete(){
	if(confirm("MBOM 조합을 삭제 하시겠습니까?") == true){
		jQuery.ajax({
			url :"<c:url value='/com/yura/mbom/deleteMbomSubItemRel.do'/>",
			data: {
				mbo : $('#mbo').val(),
				eco : $('#eco').val()
			},
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			success : function (data){
				if(data == 'fail'){
					resultMsg(data);
				} else {
					alertMsg("<spring:message code='button.confirm'/>","<spring:message code='success.request.msg'/>", "info");
					fnBack();
				}
			},
			error : function(xhr, status, error){
				resultMsg("fail");
			}
		});
	}
}

// MBOM 상세 조회
function mbomDetailInfo(){
	jQuery.ajax({
		url :"<c:url value='/com/yura/mbom/selectMbomFinalSearch.do'/>",		
		data: {
			mbo : $('#mbo').val(),
			eco : $('#eco').val()
		},
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		success : function (data){
			if(data == 'fail'){
				resultMsg(data);
			} else {
				jQuery('div#modalLg').html(data);
				jQuery("#targetModalLg").modal("show");
			}
		},
		error : function(xhr, status, error){
			resultMsg("fail");
		}
	});
}

</script>
</head>
<body class="nav-md">
<form:form commandName="mbomInfo" id="mbomInfo" name="mbomInfo" method="post" data-parsley-validate="validate" class="form-horizontal form-bordered">
<input type="hidden" id="mbo" name="mbo" value="${mbomInfo.mbo}"/>
<input type="hidden" id="eco" name="eco" value="${mbomInfo.eco}"/>
<input type="hidden" id="sapyn" name="sapyn" value="${mbomInfo.sapyn}"/>
<div class="x_panel" style="min-height:calc(100vh - 100px);">
    <div class="x_title alert alert-success">
    	<h3>&nbsp;MBOM 조회</h3> 
 	</div>
 	<div class="clearfix"></div>   
  	<div class="x_content">
  		<div class="form-actions" align="right">
  			<button type="button" class="btn btn-info" id="mbomDelBtn" onclick="mbomDelete(); return false;"><span class='glyphicon glyphicon-trash'></span>&nbsp;MBOM 삭제</button>
  			<button type="button" class="btn btn-info" id="mbomInfoBtn" onclick="mbomDetailInfo(); return false;"><span class='glyphicon glyphicon-search'></span>&nbsp;MBOM 조회</button>
  			<button type="button" class="btn btn-info" id="closeBtn" onclick="fnBack(); return false;"><span class='glyphicon glyphicon-th-list'></span>&nbsp;목록</button>
  		</div>
  		<div class="form-group">    
  			<div class="col-md-12">        
  				<div class="form-group">        
  					<div class="x_title">
  						기본 정보
  					</div>
  				</div>   
  			</div> 
 			<div class="row-fluid"> 
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">ECO 번호</label>
						<input id="econo" name="econo" class="form-control" value="${mbomInfo.eco}"/>
	                </div>
	            </div> 
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">관리번호</label>
						<input id="mbrno" name="mbrno" class="form-control" value="${mbomInfo.mbo}"/>
	                </div>
	            </div> 
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">파트번호</label>
						<input id="pno" name="pno" class="form-control" value="${mbomInfo.partno}"/>
	                </div>
	            </div> 
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">개정</label>
						<input id="pnorev" name="pnorev" class="form-control" value="${mbomInfo.partrev}"/>
	                </div>
	            </div>
	            <div class="col-md-2">
                <div class="form-group">
                    <label class="control-label">ERP 전송 여부</label>
					<input id="sapyn" name="sapyn" class="form-control" value="${mbomInfo.sapyn}"/>
                </div>
            </div>
			</div>            
      
	       	<div class="col-md-12">          
  				<div class="form-group" style="margin-top:60px">
  					<div class="x_title">
  						MBOM 부자재 조합 관계 정보
  					</div>    
  				</div>         
  			</div>        
  			<div class="col-md-12">                                                       
				<table id="mbomRelationDataGrid" class="table table-striped table-bordered table-hover" style="width:100%">
				  <thead>
				  	<tr class="center aligned">
						<th>파트번호</th>
						<th>개정</th>
						<th>부자재 조합</th>
		  		  	</tr>
				  </thead>
				</table>
			</div>
    	</div>  
  	</div>    
</div>                  
</form:form>
</body>
</html>
