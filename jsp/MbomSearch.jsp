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

<title>MBOM 조회</title>

<script type="text/javaScript">
jQuery(document).ready(function() {
    setDataGrid();              
});

// DataGrid 셋팅
function setDataGrid(){
	var table = $('#mbomGrid').DataTable( {
		ajax: {
		    url: "<c:url value='/com/yura/mbom/selectMbomSearchData.do'/>",
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
		pageLength: 10,	/* 페이지별 보여지는 row 수 */
        lengthChange: true,			/* 페이지별 개수 뷰 여부 */
        lengthMenu : [  [10, 25, 50, 100 ], [10, 25, 50, 100]  ],	//페이징 길이 select옵션 지정
        serverSide: true,			/* 서버단에서 처리 */
        deferRender: true,			/* 대용량 데이타 화면출력시 속도 개선 옵션 */
        language: lang_kor,			/* DataTables에 표시되는 언어 정보 */
        
        /* 헤더 명칭 , 너비, 정렬여부, 필터검색여부, className 등 옵션으로 지정 및 변경 가능*/
        columnDefs: [
			{
  			    defaultContent: "",
			    orderable: false,
			    className: 'select-checkbox',
			    targets:   0,
			    width: "3%"
			} ,
		    {
  			    defaultContent: "",
		        targets: 8,
	            searchable: false,
	            orderable: false,
	            width: '10%',
	            className: 'dt-body-center',                 
	            render: function (data, type, full, meta){
	            	return '<button type="button" class="btn btn-info" onclick="mbomDetail(\''+full.mbrno+'\', \''+full.econo+'\', \''+full.pno+'\', \''+full.erprevafter+'\', \''+full.state+'\')" >상세보기</button>';
	            }
	        },			    
	        {
		        defaultContent: "-",
		        targets: "_all"
		    }
		],
		columns: [
            { data: null },
            { data: 'econo'},
			{ data: 'mbrno'},
			{ data: 'pno'},
			{ data: 'erprevafter'},
			{ data: 'humid'},
			{ data: 'regdate'},
			{ data: 'state'},
			{ data: null}
		],
		/* 행, 열 및 셀을 동적으로 선택할 수있는 기능을 제공 */
		select: {
		    style:    'multi', //select 옵션 :'single', 'multi', 'os', 'multi+shift'
		    //selector: 'td:first-child'//첫번째 컬럼만 selected 이벤트 적용되게 
		},
		searching: false,			/* 필터검색 여부 */
		order: [[ 1, 'asc' ]]	/* column별 디폴트 정렬 지정 가능 */
		//scrollX: true,
		//scrollY: '40vh',
	});
	
	/* 전체 선택 이벤트 처리 */
	table.on("click", "th.select-checkbox", function() {
	    if ($("th.select-checkbox").hasClass("selected")) {
	    	table.rows().deselect();
	        $("th.select-checkbox").removeClass("selected");
	    } else {
	    	table.rows().select();
	        $("th.select-checkbox").addClass("selected");
	    }
	}).on("select deselect", function() {
	    if (table.rows({ selected: true }).count() !== table.rows().count()) {
	        $("th.select-checkbox").removeClass("selected");
	    } else {
	        $("th.select-checkbox").addClass("selected");
	    }
	});
	
	//버튼 셋팅
	_buttonSetting(table);
}
    
// mbom 상세조회
function mbomDetail(mbrno, econo, pno, pnorev, state){
	jQuery.ajax({
		url :"<c:url value='/com/yura/mbom/selectMbomSearchDetail.do'/>",		
		data: {
			mbo : mbrno,
			eco : econo,
			partno : pno,
			partrev : pnorev,
			sapyn : state
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

// DataGrid용 버튼 세팅
function _buttonSetting(_table){
	new jQuery.fn.dataTable.Buttons( _table, {
	    name: 'commands',
	    buttons: [
	    	{
	        	text: "<span class='glyphicon glyphicon-export'></span>&nbsp;ERP 전송"	        	
	        	, action: function ( e, dt, node, config ) {
	        		sapInterface();        	
	        	}
	        	, className: "btn btn-info"
	        },
	    	{ 
        	 	 text: "<span class='glyphicon glyphicon-refresh'></span>&nbsp;초기화"
     		   , action: function ( e, dt, node, config ) {
     				jQuery("#listForm").each(function() {  
     					this.reset();
     				});
     				var dt = jQuery('#mbomGrid').DataTable();
     				dt.draw(1);
               	}
     			, className : "btn btn-info"
	        },
	        { 
	        	  text: "<span class='glyphicon glyphicon-search'></span>&nbsp;검색"
        		, action: function ( e, dt, node, config ) {
        			var dt = jQuery('#mbomGrid').DataTable();
        	    	dt.draw(1);
                }
                , className: "btn btn-info"
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

function sapInterface() {
	var selectObj = $('#mbomGrid').DataTable().rows('.selected').data();
	
	if(selectObj.length == 0){ 
		alert("ERP 전송할 데이터를 선택하세요.");
	} else {
		if(confirm("ERP 데이터 전송을 하시겠습니까?") == true){
			var strMbrno = '';
			var strState = '';
			var strEcoRev = '';
        	for(i=0; i< selectObj.length; i++){
        		strMbrno += selectObj[i].mbrno + ";";
        		strState += selectObj[i].state;
        		strEcoRev += selectObj[i].erprevafter + ";";
        	}
        	
        	if (strState.indexOf('Y') != -1) {
        		alert("이미 ERP 전송한 데이터가 포함되어있습니다.");
        	}else{
        		jQuery.ajax({
	        		url :"<c:url value='/com/yura/mbom/exeSapInterface.do'/>",
	        		//url :"<c:url value='/com/yura/mbom/revisionPLMPartTest.do'/>",	
	        		data: {
	        			strmbrno : strMbrno,
	        			strecorev : strEcoRev
	        		},
	        		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
	        		success : function (data){
	        			if(data == 'Success'){
	        				alertMsg("<spring:message code='button.confirm'/>", "<spring:message code='success.request.msg'/>", "info");
	        			} else {
	        				resultMsg("fail");
	        			}
	        		},
	        		error : function(xhr, status, error){
	        			resultMsg("fail");
	        		}
        		});
        	}
		}
	}
}

/////// 개정할때 쓰는 코드
/* function sapInterface() {
	var selectObj = $('#mbomGrid').DataTable().rows('.selected').data();
	
	//if(selectObj.length == 0){ 
	//	alert("SAP 전송할 데이터를 선택하세요.");
	//} else {
		if(confirm("개정 테스트입니다.") == true){
			var strMbrno = '';
			var strState = '';
			var strEcoRev = '';
        	for(i=0; i< selectObj.length; i++){
        		strMbrno += selectObj[i].mbrno + ";";
        		strState += selectObj[i].state;
        		strEcoRev += selectObj[i].erprevafter + ";";
        	}
        	
        	//if (strState.indexOf('Y') != -1) {
        	//	alert("이미 SAP 전송한 데이터가 포함되어있습니다.");
        	//}else{
        		jQuery.ajax({
	        		//url :"<c:url value='/com/yura/mbom/exeSapInterface.do'/>",
	        		url :"<c:url value='/com/yura/mbom/revisionPLMPartTest.do'/>",	
	        		data: {
	        			strmbrno : strMbrno,
	        			strecorev : strEcoRev
	        		},
	        		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
	        		success : function (data){
	        			if(data == 'Success'){
	        				alertMsg("<spring:message code='button.confirm'/>", "<spring:message code='success.request.msg'/>", "info");
	        			} else {
	        				resultMsg("fail");
	        			}
	        		},
	        		error : function(xhr, status, error){
	        			resultMsg("fail");
	        		}
        		});
        	//}
		}
	//}
} */



jQuery('input').keyup(function(e) {
    if (e.keyCode == 13) {
		var dt = jQuery('#mbomGrid').DataTable();
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

</script>
</head>

<body class="nav-md">
<form id="listForm" name="listForm" onsubmit="return false;" cssClass="form-horizontal form-label-left input_mask">
<div class="x_panel" style="min-height:calc(100vh - 100px);">
    <div class="x_title alert alert-success">
    	<h3>&nbsp;MBOM 조회</h3>
 	</div>
 	<div class="clearfix"></div>   
  	<div class="x_content">
  		<br>
		<div class="form-group">
			<div class="col-md-2">
                <div class="form-group">
                    <label class="control-label">ECO 번호</label>
					<input id="econo" name="econo" class="form-control"/>
                </div>
            </div> 
            <div class="col-md-2">
                <div class="form-group">
                    <label class="control-label">관리번호</label>
					<input id="mbrno" name="mbrno" class="form-control"/>
                </div>
            </div> 
            <div class="col-md-2">
                <div class="form-group">
                    <label class="control-label">파트번호</label>
					<input id="pno" name="pno" class="form-control"/>
                </div>
            </div> 
            <div class="col-md-2">
                <div class="form-group">
                    <label class="control-label">개정</label>
					<input id="pnorev" name="pnorev" class="form-control"/>
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
		          <input id="regdate" name="regdate" class="form-control" />
			      <span class="glyphicon glyphicon-calendar form-control-feedback right" aria-hidden="true"></span>
				</div>
            </div>
            <div class="col-md-2">
                <div class="form-group">
                    <label class="control-label">ERP 전송 여부</label>
					<select id="state" name="state"class="form-control">
					    <option value=''></option>
					    <option value='Y'>Y</option>
					    <option value='N'>N</option>
					</select>
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
		  	<table id="mbomGrid" class="table table-striped table-bordered table-hover" style="width:100%">
			  <thead>
			    <tr class="center aligned">
			    	<th class="select-checkbox"></th>
					<th>ECO 번호</th>
					<th>관리번호</th>
					<th>파트번호</th>
					<th>개정</th>
					<th>등록자</th>
					<th>등록일</th>
					<th>ERP 전송 여부</th>
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