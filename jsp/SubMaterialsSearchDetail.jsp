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
<title>부자재 등록/수정</title> 
<script type="text/javaScript">

jQuery('#mbsmInfo').parsley();

jQuery(document).ready(function() {
	fn_init();
});
       
function fn_init(){
	 if($('#etcKind').val() == 'update'){
		 setSubMaterialsDataGrid();
	 }
}

function fnBack(){
	jLoding('show');
	jQuery.ajax({
		type:'post',
		url : "<c:url value='/com/yura/mbom/selectSubMaterialsSearch.do'/>",
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

// 수정
jQuery('#mbsmInfo').on('submit',function(e) {
	 e.preventDefault();
	 if ( jQuery(this).parsley().isValid() ) {
	    swal({
            title: "<spring:message code='button.confirm'/>",
            text: "<spring:message code='common.save.msg'/>",
            type: "info",
            showCancelButton: true,
            closeOnConfirm: false,
            showLoaderOnConfirm: true,
            confirmButtonText: "<spring:message code='button.confirm'/>",
            cancelButtonText: "<spring:message code='button.reset'/>",
        },
        function(){
			jQuery('#mbsmInfo').ajaxSubmit({
				url :"<c:url value='/com/yura/mbom/modifyMbomMasterData.do'/>",
				contentType : "application/x-www-form-urlencoded;charset=UTF-8",
				success : function (data){
					resultMsg(data);
        			var dt = $('#submaterialsGrid').DataTable();
        	    	dt.draw(1);
        	    	jQuery("#targetModalLg").modal("hide");	
				},
				error : function(xhr, status, error){
					resultMsg("fail");
				}
			})
            var close = window.swal.close;
            window.swal.close = function() {
                close();
                window.onkeydown = null;
            };			     
        });		 
	}
});

// 삭제
function mbmtDelete() {
	    swal({
            title: "<spring:message code='button.confirm'/>",
            text: "<spring:message code='common.delete.msg'/>",
            type: "warning",
            showCancelButton: true,
            closeOnConfirm: false,
            showLoaderOnConfirm: true,
            confirmButtonText: "<spring:message code='button.confirm'/>",
            cancelButtonText: "<spring:message code='button.reset'/>",            
        },
        function(){
	 		jQuery('#mbsmInfo').ajaxSubmit({       
	 			url :"<c:url value='/com/yura/mbom/deleteMbomMasterData.do'/>",
	 			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
	 			success : function (data){
	 				resultMsg(data);
	 				fnBack(); 
	 			},      
	 			error : function(xhr, status, error){
	 				resultMsg("fail");
	 			}
	 		}); 
            var close = window.swal.close;
            window.swal.close = function() {
                close();
                window.onkeydown = null;
            };    	 		
        });		
}

//관련 부자재 데이터그리드 셋팅
function setSubMaterialsDataGrid(){
	var table = jQuery('#submaterialsGrid').on( 'processing.dt', function ( e, settings, processing ) {
		processing ? jLoding('show') : jLoding('hide');
    }).DataTable( { 
		ajax: {    
		    url: "<c:url value='/com/yura/mbom/selectSubMaterialsSearchData.do'/>",
		    dataSrc: function ( json ) {
                return json.data;
		    },
		    type: "POST",			/* 전송방식 */
            data: {
            	oid : $('#oid').val()
            }              
		},
        serverSide: true,			/* 서버단에서 처리 */
        deferRender: true,			/* 대용량 데이타 화면출력시 속도 개선 옵션 */
		columns: [
			{ data: 'smoid'},
			{ data: 'name'},
			{ data: 'oid'},
			{ data: 'pno'},
			{ data: 'usage'},
			{ data: 'unit'},
			{ data: 'humid'},
			{ data: 'regdate'},
			{ data: 'modhumid'},
			{ data: 'moddate'}
		],
		columnDefs: [
	        {
	        	"targets": [0, 2],
	        	"visible": false
	        }
		],
		searching: false,			/* 필터검색 여부 */
		language: language_locale,
		scrollX: true,    
		scrollY: '180px',     
	    fnInitComplete : function() {
	    	jQuery("#submaterialsGrid tbody tr").css('cursor', 'pointer');
	    	jLoding('hide');       
		}
	});
	
    jQuery('#submaterialsGrid tbody').on('click', 'tr', function () {
        var data = table.row( this ).data();
        addsmDetail('update' , data.smoid )
    });
	
	//버튼 셋팅
	_buttonSetting(table);
}

// 부자재 추가
function addsmDetail(etcKind, smoid){
	jQuery.ajax({
		url :"<c:url value='/com/yura/mbom/selectSubMaterialsDetail.do'/>",
		data: {
			etcKind : etcKind,
			smoid : smoid,
			mtoid : $('#oid').val()
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
<form:form commandName="mbsmInfo" id="mbsmInfo" name="mbsmInfo" method="post" data-parsley-validate="validate" class="form-horizontal form-bordered">
<input type="hidden" id="etcKind" name="etcKind" value="${mbsmInfo.etcKind}"/>
<input type="hidden" id="oid" name="oid" value="${mbsmInfo.oid}"/>
<div class="x_panel" style="min-height:calc(100vh - 100px);">
    <div class="x_title alert alert-success">
    	<h3>&nbsp;부자재 등록/수정</h3> 
 	</div>
 	<div class="clearfix"></div>   
  	<div class="x_content">
  		<div class="form-actions" align="right">
  			<button type="submit" id="passSave" class="btn btn-info chkRM"><span class='glyphicon glyphicon-floppy-save'></span>&nbsp;<spring:message code='button.save' /></button>
			<button type="button" id="deleteBtn" class="btn btn-info chkRD" onclick="mbmtDelete(); return false;" ><span class='glyphicon glyphicon-trash'></span>&nbsp;<spring:message code='button.delete'/></button>
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
	                    <label class="control-label">부자재 조합</label>
						<input id="name" name="name" class="form-control" value="${mbsmInfo.name}"/>
	                </div>
	            </div>    
			</div>            
      
	       	<div class="col-md-12">
  				<div class="form-group" style="margin-top:60px">
  					<div class="x_title">
  						부자재 정보
  					</div>    
  				</div>
  				<div class="form-actions" align="right">
	  				<button type="button" class="btn btn-info" id="addSubMaterials" onclick="addsmDetail('insert'); return false;"><span class='glyphicon glyphicon-plus'></span>&nbsp;추가</button>
  				</div>
  			</div>
  			<div class="col-md-12">                                                       
				<table id="submaterialsGrid" class="table table-striped table-bordered table-hover" style="width:100%">
				  <thead>
				  	<tr class="center aligned">
						<th>smoid</th>
						<th>부자재 조합</th>
						<th>oid</th> 
						<th>부자재 코드</th>
						<th>수량</th>
						<th>단위</th>
						<th>등록자</th>
						<th>등록일</th>
						<th>수정자</th>
						<th>수정일</th>
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
