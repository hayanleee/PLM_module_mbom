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
<title>EBOM 조회/등록</title> 
<script type="text/javaScript">

jQuery('#ebomInfo').parsley();

jQuery(document).ready(function() {
	//chkBtnAuthor();
	//fn_attch_file('<c:url value='/com/yura/mbom/selectEbomSearchDetail.do'/>',fn_callback);
	fn_init();
	
	if ($('#addyn').val() == 'N') {
		$('#revisionProductBtn').prop('disabled', true);
		$('#addSubMaterialBtn').prop('disabled', false);
	} else {
		$('#revisionProductBtn').prop('disabled', false);
		$('#addSubMaterialBtn').prop('disabled', true);
	}
});
       
function fn_init(){
	 if($('#etcKind').val() == 'update'){
		setEffectiveItemDataGrid();
	 }
}      

/* function fn_callback(msg){
	if(msg.result === 'success'){
		resultMsg(msg.result);
	}
} */

function fnBack(){
	jLoding('show');
	jQuery.ajax({
		type:'post',
		url : "<c:url value='/com/yura/mbom/selectEbomSearch.do'/>",
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

//관련 설변대상아이템 데이터그리드 셋팅
function setEffectiveItemDataGrid(){
	var table = jQuery('#effectiveItemGrid').on( 'processing.dt', function ( e, settings, processing ) {
		processing ? jLoding('show') : jLoding('hide');
    }).DataTable( { 
		ajax: {    
		    url: "<c:url value='/com/yura/mbom/selectEffectiveItemSearch.do'/>",
		    dataSrc: function ( json ) {
                return json.data;
		    },
		    type: "POST",			/* 전송방식 */
            data: {
            	econo : $('#econo').val(),
            	oripno : $('#oripno').val(),
            	orirev : $('#orirev').val()
            }              
		},
        serverSide: true,			/* 서버단에서 처리 */
        deferRender: true,			/* 대용량 데이타 화면출력시 속도 개선 옵션 */
		columns: [
			{ data: 'pno'},
			{ data: 'revision'},
			{ data: 'name'},
			{ data: 'plmtype'},
			{ data: 'product'},
			{ data: 'state'}
		],
		columnDefs: [
	        {
	        	"targets": [ 5 ],
	        	"visible": false
	        }
		],
		searching: false,			/* 필터검색 여부 */
		language: language_locale,
		scrollX: true,    
		scrollY: '200px',                                                                   
	    fnInitComplete : function() {
	    	jQuery("#effectiveItemGrid tbody tr").css('cursor', 'pointer');
	    	jLoding('hide');       
		}
	});
	
    jQuery('#effectiveItemGrid tbody').on('click', 'tr', function () {
        var data = table.row( this ).data();
        
    });
	
	//버튼 셋팅
	_buttonSetting(table);
}

// EBOM 조회/추가 버튼 클릭
function ebomSearch(etcKind, econo) {
	jQuery.ajax({
		url :"<c:url value='/com/yura/mbom/selectPlmEbomData.do'/>",
		data: {
			etcKind : etcKind,
			econo : $('#econo').val(),
			oripno : $('#oripno').val(),
        	orirev : $('#orirev').val()
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

// 제품 개정 버튼 클릭
function revisionProduct() {
	jQuery.ajax({
		url :"<c:url value='/com/yura/mbom/revisionPart.do'/>",
		data: {
			econo : $('#econo').val(),
			oripno : $('#oripno').val(),
			orirev : $('#orirev').val()
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
<form:form commandName="ebomInfo" id="ebomInfo" name="ebomInfo" method="post" data-parsley-validate="validate" class="form-horizontal form-bordered">
<input type="hidden" id="etcKind" name="etcKind" value="${ebomInfo.etcKind}"/>
<input type="hidden" id="econo" name="econo" value="${ebomInfo.econo}"/>
<input type="hidden" id="addyn" name="addyn" value="${ebomInfo.addyn}"/>
<input type="hidden" id="oripno" name="oripno" value="${ebomInfo.pno}"/>
<input type="hidden" id="orirev" name="orirev" value="${ebomInfo.erprevafter}"/>
<div class="x_panel" style="min-height:calc(100vh - 100px);">
    <div class="x_title alert alert-success">
    	<h3>&nbsp;EBOM 조회/등록</h3> 
 	</div>
 	<div class="clearfix"></div>   
  	<div class="x_content">
  		<div class="form-actions" align="right">
  			<button type="button" class="btn btn-info" id="revisionProductBtn" onclick="revisionProduct(); return false;"><span class='glyphicon glyphicon-retweet'></span>&nbsp;제품개정</button>  			
  			<button type="button" class="btn btn-info" id="ebomSearchBtn" onclick="ebomSearch('search'); return false;"><span class='glyphicon glyphicon-search'></span>&nbsp;EBOM조회</button>
  			<button type="button" class="btn btn-info" id="addSubMaterialBtn" onclick="ebomSearch('add'); return false;"><span class='glyphicon glyphicon-plus'></span>&nbsp;부자재추가</button>
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
						<input id="econo" name="econo" class="form-control" value="${ebomInfo.econo}"/>
	                </div>
	            </div>
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">차종</label>
						<input id="carcode" name="carcode" class="form-control" value="${ebomInfo.plmcarcode}"/>
	                </div>
	            </div>
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">대표품번</label>
						<input id="pno" name="pno" class="form-control" value="${ebomInfo.pno}"/>
	                </div>
	            </div>
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">제목</label>
						<input id="title" name="title" class="form-control" value="${ebomInfo.title}"/>
	                </div>
	            </div>
	            <%-- <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">erpsite</label>
						<input id="erpsite" name="erpsite" class="form-control" value="${ebomInfo.erpsite}"/>
	                </div>
	            </div> 
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">설명</label>
						<nput id="description" name="description" class="form-control" value="${ebomInfo.description}"/>
	                </div>
	            </div> --%>
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">개정전</label>
						<input id="erprevbefore" name="erprevbefore" class="form-control" value="${ebomInfo.erprevbefore}"/>
	                </div>
	            </div>
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">개정후</label>
						<input id="erprevafter" name="erprevafter" class="form-control" value="${ebomInfo.erprevafter}"/>
	                </div>
	            </div>
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">개발단계</label>
						<input id="devstep" name="devstep" class="form-control" value="${ebomInfo.devstep}"/>
	                </div>
	            </div>
	            <%-- <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">epdpno</label>
						<nput id="epdpno" name="epdpno" class="form-control" value="${ebomInfo.epdpno}"/>
	                </div>
	            </div> --%>
	            <div class="col-md-2">
	                <div class="form-group">
	                    <label class="control-label">부자재추가 여부</label>
						<input id="state" name="state" class="form-control" value="${ebomInfo.addyn}"/>
	                </div>
	            </div>      
			</div>            
      
	       	<div class="col-md-12">          
  				<div class="form-group" style="margin-top:60px">
  					<div class="x_title">
  						설변대상 아이템 정보
  					</div>    
  				</div>         
  			</div>        
  			<div class="col-md-12">                                                       
				<table id="effectiveItemGrid" class="table table-striped table-bordered table-hover" style="width:100%">
				  <thead>
				  	<tr class="center aligned">
						<th>파트번호</th>
						<th>개정</th>
						<th>이름</th> 
						<th>아이템</th>
						<th>제품</th>
						<th>상태</th>
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