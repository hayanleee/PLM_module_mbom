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

<title>부자재추가</title>

<script type="text/javaScript">
jQuery(document).ready(function() {
	$('#submstoid').val('');
	setPlmSubmDataGrid2();
});

// 부자재 추가
function setPlmSubmDataGrid2() {
	var table = jQuery('#plmSubmDataGrid2').on( 'processing.dt', function ( e, settings, processing ) {
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
				jQuery('#submInfo input, #submInfo select').each(function() {
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
	        	"targets": [ 0 ],
	        	"visible": false
	        }
		],   
		searching: false,			/* 필터검색 여부 */
		language: language_locale,
		scrollX: true,           
		scrollY: $(window).height() * 45 / 100,      
	    fnInitComplete : function() {
	    	jQuery("#plmSubmDataGrid2 tbody tr").css('cursor', 'pointer');
	    	jLoding('hide');   
		}
	});
	
	jQuery('#plmSubmDataGrid2 tbody').on('click', 'tr', function () {
		var data = table.row( this ).data();
		$('#submstoid').val(data.oid);
		
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            table.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
	} ); 
}


function clickSubItemList() {
	if ($('#submstoid').val()) {
		jQuery.ajax({
			url :"<c:url value='/com/yura/mbom/selectPlmEbomData3.do'/>",
			data: {
				etcKind : "add",
				econo : $('#econo').val(),
				tmpaddoid : $('#submstoid').val(),
				ecosuboid : $('#ecosuboid').val(),
				accumulatedata : $('#accumulatedata').val(),
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
	} else {
		alert("부자재를 선택하세요.");
	}
}

function setResearch(){
	var table = $('#plmSubmDataGrid2').DataTable();
	table.ajax.reload();
}

function clickCloseBtn(){
	$('#submstoid').val('');
	$('#ecosuboid').val('');
	$('#accumulatedata').val('');
	jQuery("#targetModalLg").modal("hide");
}

jQuery('#submInfo').parsley();

</script>
</head>
<body class="nav-md">

<form:form commandName="submInfo" id="submInfo" name="submInfo" method="post" data-parsley-validate="validate" class="form-horizontal form-bordered">
<input type="hidden" id="econo" name="econo" value="${submInfo.econo}"/>
<input type="hidden" id="oripno" name="oripno" value="${submInfo.oripno}"/>
<input type="hidden" id="orirev" name="orirev" value="${submInfo.orirev}"/>
<input type="hidden" id="ecosuboid" name="ecosuboid" value="${submInfo.ecosuboid}"/>
<input type="hidden" id="accumulatedata" name="accumulatedata" value="${submInfo.accumulatedata}"/>
<input type="hidden" id="submstoid" name="submstoid"/>
<div class="x_panel" style="min-height:calc(100vh - 100px);">
   	<div class="x_title alert alert-success">
		<h3>&nbsp;부자재 리스트</h3>
		<div class="clearfix"></div>
  	</div>
	  	
	<div class="form-group">
  		<div class="input-group" style="padding: 15px 0 0 0;">
			<label class="control-label col-md-3 col-sm-3 col-xs-12" style="width:150px;" for="부자재 조합">부자재 조합</label>
			<input id="subname" name="subname" class="form-control" style="width:500px;"/>
			<span class="input-group-btn" style="float: left;">
				<button type="button" id="searchSubname" class="btn btn-info" onclick="setResearch(); return false;" ><span class='glyphicon glyphicon-search'></span>&nbsp;검색</button>            
			</span>
		</div>
   	</div>
  	
	<div class="ln_solid"></div>
	  	
	<!-- DataGrid 리스트 영역 -->
	<table id="plmSubmDataGrid2" class="table table-striped table-bordered table-hover" style="width:100%">
	  <thead> 
	    <tr class="center aligned">
	    	<th>oid</th>
			<th>부자재 조합</th>
			<th>등록자</th>
			<th>등록일</th>
			<th>수정일</th>
	  	</tr>
	  </thead>
	</table>
	<div class="ln_solid"></div>
	<div class="form-actions" align="right">
		<button type="button" id="clickSubitem" class="btn btn-info" onclick="clickSubItemList(); return false;" ><span class='glyphicon glyphicon-ok'></span>&nbsp;선택</button>
		<button type="button" id="clickClose" class="btn btn-info" onclick="clickCloseBtn(); return false;" ><span class='glyphicon glyphicon-remove'></span>&nbsp;닫기</button>	  	
		<%-- <button type="button" id="clickClose" class="btn btn-info" data-dismiss="modal"><span class='glyphicon glyphicon-remove'></span>&nbsp;<spring:message code='button.close'/></button> --%>
	</div>
</div>
</form:form>

</body>
</html>