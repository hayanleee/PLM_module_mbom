<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui" %>
<%@ taglib prefix="sec" 	  uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt"%>  
<%@ include file="/WEB-INF/jsp/cmm/grid/dataTables-header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">

<title>MBOM 상세조회</title>

<script type="text/javaScript">
jQuery(document).ready(function() {
	setMbomDetailDataGrid();
});

// MBOM 상세조회 데이터그리드 셋팅
function setMbomDetailDataGrid(){
	var table = jQuery('#mbomDetailDataGrid').on( 'processing.dt', function ( e, settings, processing ) {
		processing ? jLoding('show') : jLoding('hide');
    }).DataTable( {
		ajax: {
		    url: "<c:url value='/com/yura/mbom/selectMbomDetailData.do'/>",
		    dataSrc: function ( json ) {
                return json.data;
		    },
		    type: "POST",			/* 전송방식 */
            data: {
            	mbo : $('#mbo').val(),
            	eco : $('#eco').val()
            }
		},
        serverSide: false,			/* 서버단에서 처리 */
        deferRender: true,			/* 대용량 데이타 화면출력시 속도 개선 옵션 */
		columns: [
			{ data: 'parentpno'},
			{ data: 'parentrev'},
			{ data: 'pno'},
			{ data: 'usage'},
			{ data: 'unit'},
			{ data: 'flag'}
		],
		columnDefs: [
	        {
	        	"targets": [ 5 ],
	        	"visible": false
	        }
		],
		order: [[5, "asc"]],
		searching: false,			/* 필터검색 여부 */
		language: language_locale,
		paging: false,			// 이거 넣고 controller paging 부분 주석 & sql 맨아래 페이지부분 주석했더니 됨
		scrollX: true,    
		scrollY: $(window).height() * 55 / 100,
	    fnInitComplete : function() {
	    	jQuery("#mbomDetailDataGrid tbody tr").css('cursor', 'pointer');
	    	jLoding('hide');       
		},
		fnRowCallback : function(row, data, dataIndex) {
		      if (data.flag == "-") {
		        $('td', row).css('background-color', 'AliceBlue');
		      }
	    }
	});
	//버튼 셋팅
	_buttonSetting2(table);
}

function _buttonSetting2(_table){
	new jQuery.fn.dataTable.Buttons( _table, {
	    name: 'commands',
	    buttons: [
	    	{ 
	        	extend: 'excel'
	        	, text: "<span class='glyphicon glyphicon-download-alt'></span>&nbsp;엑셀"
	        	, className: "btn btn-info chkRR"
	        },
	        {
	        	text: "<span class='glyphicon glyphicon-remove'></span>&nbsp;닫기"	        	
	        	, action: function ( e, dt, node, config ) {
	        		closeMbomDetail();        	
	        	}
	        	, className: "btn btn-info"
	        }
        ]
	});
	_table.buttons().containers().appendTo('#btnArea2');
	
	//버튼 권한 설정시 버튼에 chkRC(등록), chkRM(수정), chkRD(삭제),chkRR(엑셀다운) class를 추가하여 적용시킴.
	chkBtnAuthor();	
}

function closeMbomDetail() {
	jQuery("#targetModalLg").modal("hide");
}

jQuery('#mbomFinal').parsley();

</script>
</head>
<body class="nav-md">

<form:form commandName="mbomFinal" id="mbomFinal" name="mbomFinal" method="post" data-parsley-validate="validate" class="form-horizontal form-bordered">
<input type="hidden" id="mbo" name="mbo" value="${mbomFinal.mbo}"/>
<input type="hidden" id="eco" name="eco" value="${mbomFinal.eco}"/>
<div class="x_panel" style="min-height:calc(100vh - 100px);">
   	<div class="x_title alert alert-success">
		<h3>&nbsp;MBOM 상세조회</h3>
		<div class="clearfix"></div>
  	</div>
  	
	<table id="mbomDetailDataGrid" class="table table-striped table-bordered table-hover" style="width:100%">
	  <thead>
	  	<tr class="center aligned">
			<th>상위품번</th>
			<th>상위개정</th>
			<th>파트번호</th>
			<th>수량</th>
			<th>단위</th>
			<th>flag</th>
		</tr>
	  </thead>
	</table>
	
	<!-- DataGrid 버튼 영역 -->
	<span id="btnArea2" class="flow-right"></span>
	<!-- <div class="form-actions" align="right">
		<button type="button" id="closeMbomDetailBtn" class="btn btn-info" onclick="closeMbomDetail(); return false;" ><span class='glyphicon glyphicon-remove'></span>&nbsp;닫기</button>		
	</div>
	 -->
	 
</div>
</form:form>

</body>
</html>