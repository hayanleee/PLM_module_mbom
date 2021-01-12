<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui" %>
<%@ taglib prefix="sec"    uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/jsp/cmm/grid/dataTables-header.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">

<title>MBOM 부자재 리스트</title>

<script type="text/javaScript">
jQuery(document).ready(function() {
    setDataGrid();    
});

// DataGrid 셋팅
function setDataGrid(){
	var table = jQuery('#smGrid').on( 'processing.dt', function ( e, settings, processing ) {
		processing ? jLoding('show') : jLoding('hide');
    }).DataTable( { 
		ajax: {    
		    url: "<c:url value='/com/yura/mbom/selectMbomMasterSearchData.do'/>",
		    dataSrc: function ( json ) {
                return json.data;
		    },
		    type: "POST",			/* 전송방식 */
            data: function ( d ) {	/* Ajax 요청시 서버에 추가로 파라미터 전달 */
            	var fields = {};
				jQuery('#listForm input, #listForm select').each(function() {
					var _str = jQuery(this).val() == null ? '' : jQuery(this).val();
			    	fields[this.name] = _str.toString();
				});
				jQuery.extend(d, fields);
            	return d;			
            }        
		},
        serverSide: true,			/* 서버단에서 처리 */
        deferRender: true,			/* 대용량 데이타 화면출력시 속도 개선 옵션 */
		columns: [
			{ data: 'oid'},
			{ data: 'name'},
			{ data: 'humid'},
			{ data: 'regdate'},
			{ data: 'moddate'}
		],
		columnDefs: [    
			    {
	  			    defaultContent: "",
			        targets: 5,
		            searchable: false,
		            orderable: false,
		            width: '10%',
		            className: 'dt-body-center',
		            render: function (data, type, full, meta){
		            		return '<button type="button" class="btn btn-info" onclick="mbomSubDetail(\'update\', \''+full.oid+'\')" >상세보기</button>';
		            }
		        },
		        {
		        	"targets": [0],
		        	"visible": false
		        }
		],
		searching: false,			/* 필터검색 여부 */
		language: language_locale,
		scrollX: true,
		scrollY: '40vh', 
	    fnInitComplete : function() {
	    	jQuery("#smGrid tbody tr").css('cursor', 'pointer');
	    	jLoding('hide');
		}
	});
	   
    /* jQuery('#smGrid tbody').on('click', 'tr', function () {
        var data = table.row( this ).data();
        mbomMstDetail('update', data.oid )
    } ); */
	
	//버튼 셋팅
	_buttonSetting(table);
}

// DataGrid용 버튼 세팅
function _buttonSetting(_table){
	new jQuery.fn.dataTable.Buttons( _table, {
	    name: 'commands',
	    buttons: [
	    	{ 
        	 	 text: "<span class='glyphicon glyphicon-refresh'></span>&nbsp;초기화"
     		   , action: function ( e, dt, node, config ) {
     				jQuery("#listForm").each(function() {  
     					this.reset();
     				});
     				var dt = jQuery('#smGrid').DataTable();
     				dt.draw(1);
               	}
     			, className : "btn btn-info"
	        },
	        { 
	        	  text: "<span class='glyphicon glyphicon-search'></span>&nbsp;검색"
        		, action: function ( e, dt, node, config ) {
        			var dt = jQuery('#smGrid').DataTable();
        	    	dt.draw(1);
                }
                , className: "btn btn-info"
	        },
	    	{ 
	        	  text: "<span class='glyphicon glyphicon-plus'></span>&nbsp;등록"
        		, action: function ( e, dt, node, config ) {
        			mbomMstDetail();
                }
                , className: "btn btn-info chkRC"
	        }, 
	    	{ 
	        	extend: 'excel'
	        	, text: "<span class='glyphicon glyphicon-download-alt'></span>&nbsp;엑셀"
	        	, className: "btn btn-info chkRR"
	        }
        ]
	});
	_table.buttons().containers().appendTo('#btnArea');
	
	//버튼 권한 설정시 버튼에 chkRC(등록), chkRM(수정), chkRD(삭제),chkRR(엑셀다운) class를 추가하여 적용시킴.
	chkBtnAuthor();	
}

// Mbom 마스터 등록
function mbomMstDetail(etcKind, oid){
	jQuery.ajax({
		url :"<c:url value='/com/yura/mbom/selectMbomMasterData.do'/>",
		data: {
			etcKind : etcKind,
			oid : oid
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

// 부자재 등록/수정
function mbomSubDetail(etcKind, oid){
	jQuery.ajax({
		url :"<c:url value='/com/yura/mbom/selectSubMaterialsSearchDetail.do'/>",
		data: {
			etcKind : etcKind, 
			oid : oid
		},
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		success : function (data){
			if(data == 'fail'){
				resultMsg(data);
			} else {
				jQuery('div#contents').html(data);
				/* jQuery('div#modalLg').html(data);
				jQuery("#targetModalLg").modal("show"); */
			}
		},
		error : function(xhr, status, error){
			resultMsg("fail");
		}
	});
}

jQuery('input').keyup(function(e) {
    if (e.keyCode == 13) {
		var dt = jQuery('#smGrid').DataTable();
		dt.draw(1);
    }
});

// 캘린더 (등록일)
jQuery("#listForm [name='regdate']").datetimepicker({
  	viewMode: 'days',
  	format: 'YYYY-MM-DD',
  	locale:'ko',
  	showTodayButton: true,
  	showClose: true,
  	showClear: true
});

//캘린더 (수정일)
jQuery("#listForm [name='moddate']").datetimepicker({
  	viewMode: 'days',
  	format: 'YYYY-MM-DD',
  	locale:'ko',
  	showTodayButton: true,
  	showClose: true,
  	showClear: true
});

</script>
</head>

<body class="nav-md">
<form id="listForm" name="listForm" onsubmit="return false;" cssClass="form-horizontal form-label-left input_mask">
<div class="x_panel" style="min-height:calc(100vh - 100px);">
    <div class="x_title alert alert-success">
    	<h3>&nbsp;부자재 리스트</h3> 
 	</div>
 	<div class="clearfix"></div>   
  	<div class="x_content">
  		<br>
		<div class="form-group">
            <div class="col-md-2">
                <div class="form-group">
                    <label class="control-label">부자재 조합</label>
					<input id="name" name="name" class="form-control"/>
                </div>
            </div>
            <div class="col-md-2">
                <div class="form-group">
                    <label class="control-label">등록자</label>
					<input id="humid" name="humid" class="form-control"/>
                </div>
            </div>
            <div class="col-md-2">
                <div class="form-group has-feedback">
                    <label class="control-label">등록일</label>
					<input id="regdate" name="regdate" class="form-control"/>
					<span class="glyphicon glyphicon-calendar form-control-feedback right" aria-hidden="true"></span>
                </div>
            </div>
            <div class="col-md-2">
                <div class="form-group has-feedback">
                    <label class="control-label">수정일</label>
					<input id="moddate" name="moddate" class="form-control"/>
					<span class="glyphicon glyphicon-calendar form-control-feedback right" aria-hidden="true"></span>
                </div>
            </div>
    	</div>

	  	<div class="row"></div>
	  	<div class="ln_solid"></div>
	  	
       	<!-- DataGrid 버튼 영역 -->
		<span id="btnArea"  class="flow-right"></span>		
		<div class="row"></div>
		  		  	
	  	<div> 
			<!-- DataGrid 리스트 영역 -->
			<table id="smGrid" class="table table-striped table-bordered table-hover" style="width:100%">
			  <thead> 
			    <tr class="center aligned">
			    	<th>oid</th>
					<th>부자재 조합</th>
					<th>등록자</th>
					<th>등록일</th>
					<th>수정일</th>
					<th><spring:message code='info.detailView'/></th> <!-- 상세보기 -->
			  	</tr>
			  </thead>
			</table>
		</div>	
	</div>
</div>
</form>
</body>
</html>