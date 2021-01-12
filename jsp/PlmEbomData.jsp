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

<title>EBOM 조회/추가</title>

<script type="text/javaScript">
jQuery(document).ready(function() {
	setPlmEbomDataGrid();
	
	if ($('#tmpaddoid').val() && $('#parentpno').val()) {
		addPlmEbomDataGrid();
	}
});

//관련 EBOM 데이터그리드 셋팅
function setPlmEbomDataGrid(){
	var table = jQuery('#plmEbomDataGrid').on( 'processing.dt', function ( e, settings, processing ) {
		processing ? jLoding('show') : jLoding('hide');
    }).DataTable( {
		ajax: {
		    url: "<c:url value='/com/yura/mbom/selectPlmEbomDataSearch.do'/>",
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
		searching: false,			/* 필터검색 여부 */
		language: language_locale,
		paging: false,			// 이거 넣고 controller paging 부분 주석 & sql 맨아래 페이지부분 주석했더니 됨
		scrollX: true,    
		scrollY: $(window).height() * 55 / 100,
	    fnInitComplete : function() {
	    	jQuery("#plmEbomDataGrid tbody tr").css('cursor', 'pointer');
	    	jLoding('hide');       
		},
		fnRowCallback : function(row, data, dataIndex) {
		      if (data.flag == "-") {
		        $('td', row).css('background-color', 'AliceBlue');
		      }
	    }
	});
}


// 부자재 추가 선택시
function addPlmEbomDataGrid() {
	jQuery.ajax({
		url :"<c:url value='/com/yura/mbom/selectAddPlmEbomData.do'/>", 
		data: {
			mstoid : $('#tmpaddoid').val()
		},
		type : "post",
		dataType: "json", 
		success : function (data){
			if(data == 'fail'){
				resultMsg(data);
			} else {
				var resData = "";
				var pnos = $('#parentpno').val().split(";");
				var revs = $('#parentrev').val().split(";");
				
				if ($('#accumulatedata').val()){
					for (k=0; k<pnos.length-1; k++){
						for (i=0; i<data.data.length; i++) {
							resData += pnos[k] + "," + revs[k] + "," + data.data[i].pno + "," +
									   data.data[i].usage + "," + data.data[i].unit + "," + data.data[i].flag + ";"
						}
					}
					$('#accumulatedata').val( $('#accumulatedata').val() + resData );
				} else {								
					for (k=0; k<pnos.length-1; k++){														
						for (i=0; i<data.data.length; i++) {
							resData += pnos[k] + "," + revs[k] + "," + data.data[i].pno + "," +
									   data.data[i].usage + "," + data.data[i].unit + "," + data.data[i].flag + ";"
						}
					}
					$('#accumulatedata').val(resData);
				}
				
				var t = $('#plmEbomDataGrid').DataTable();
				
				var drawGrid = $('#accumulatedata').val();
				var tmp = drawGrid.split(";");
				
				for (i=0; i<tmp.length -1; i++) {
					var tmp2 = tmp[i].split(",");
					t.row.add({
           				'parentpno'	:tmp2[0],
           				'parentrev'	:tmp2[1],
           				'pno'		:tmp2[2],
           				'usage'		:tmp2[3],
           				'unit'		:tmp2[4],
           				'flag'		:tmp2[5],
           			}).draw();
				}
				
				var ecoAndMbomOid = "";
				for (k=0; k<pnos.length-1; k++){
					ecoAndMbomOid += $('#tmpaddoid').val() + "," + pnos[k] + "," + revs[k] + "|";	
				}        		
        		$('#ecosuboid').val($('#ecosuboid').val() + ecoAndMbomOid);
        		
				/* var resData = "";
				if ($('#accumulatedata').val()){
					for (i=0; i<data.data.length; i++) {
						resData += $('#parentpno').val() + "," + $('#parentrev').val() + "," + data.data[i].pno + "," +
								   data.data[i].usage + "," + data.data[i].unit + "," + data.data[i].flag + ";"
					}
					$('#accumulatedata').val( $('#accumulatedata').val() + resData );
				} else {
					for (i=0; i<data.data.length; i++) {
						resData += $('#parentpno').val() + "," + $('#parentrev').val() + "," + data.data[i].pno + "," +
								   data.data[i].usage + "," + data.data[i].unit + "," + data.data[i].flag + ";"
					}
					$('#accumulatedata').val(resData);
				}
				
				var t = $('#plmEbomDataGrid').DataTable();
				
				var drawGrid = $('#accumulatedata').val();
				var tmp = drawGrid.split(";");
				
				for (i=0; i<tmp.length -1; i++) {
					var tmp2 = tmp[i].split(",");
					t.row.add({
           				'parentpno'	:tmp2[0],
           				'parentrev'	:tmp2[1],
           				'pno'		:tmp2[2],
           				'usage'		:tmp2[3],
           				'unit'		:tmp2[4],
           				'flag'		:tmp2[5],
           			}).draw();
				}
				
        		var ecoAndMbomOid = $('#ecosuboid').val() + $('#tmpaddoid').val() + "," + $('#parentpno').val() + "," + $('#parentrev').val() + "|";
        		$('#ecosuboid').val(ecoAndMbomOid); */
			}
			
			t.order([5, 'asc']).draw();
		},
		error : function(xhr, status, error){
			resultMsg("fail");
		}  
	});
}

function addEbomSubMaterials() {
	clearHiddenData();
	jQuery.ajax({
		url :"<c:url value='/com/yura/mbom/selectPlmEbomData2.do'/>",
		data: {
			econo : $('#econo').val(),
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
}

// 부자재 추가 저장
function saveEbomSubMaterials() {
	if(confirm("저장 하시겠습니까?") == true){
		if ($('#ecosuboid').val()) {
			// mbomfinal db data 추가
			jQuery.ajax({
				url :"<c:url value='/com/yura/mbom/insertMbomFinalData.do'/>",
				data: {
					econo : $('#econo').val(),
					ecosuboid : $('#ecosuboid').val(),
					oripno : $('#oripno').val(),
					orirev : $('#orirev').val()
				},
				contentType : "application/x-www-form-urlencoded;charset=UTF-8",
				success : function (data){
					if(data == 'fail'){
						resultMsg(data);
					}
				},
				error : function(xhr, status, error){
					resultMsg("fail");
				}
			});
			
	 		alertMsg("<spring:message code='button.confirm'/>","<spring:message code='success.request.msg'/>", "info");
	 		jQuery("#targetModalLg").modal("hide");
			clearHiddenData();
		} else {
			alert("부자재를 추가하세요.");
		}		 		 
    } else {
    	return;
    }
}

function closeEbomSubMaterials() {
	clearHiddenData();
	jQuery("#targetModalLg").modal("hide");
}

function clearHiddenData() {
	$('#tmpaddoid').val('');
	$('#parentpno').val('');
	$('#parentrev').val('');
	//$('#ecosuboid').val('');
	//$('#accumulatedata').val('');
}

jQuery('#ebomInfo2').parsley();

</script>
</head>
<body class="nav-md">

<form:form commandName="ebomInfo2" id="ebomInfo2" name="ebomInfo2" method="post" data-parsley-validate="validate" class="form-horizontal form-bordered">
<input type="hidden" id="etcKind" name="etcKind" value="${ebomInfo2.etcKind}"/>
<input type="hidden" id="econo" name="econo" value="${ebomInfo2.econo}"/>
<input type="hidden" id="oripno" name="oripno" value="${ebomInfo2.oripno}"/>
<input type="hidden" id="orirev" name="orirev" value="${ebomInfo2.orirev}"/>
<input type="hidden" id="tmpaddoid" name="tmpaddoid" value="${ebomInfo2.tmpaddoid}"/>
<input type="hidden" id="parentpno" name="parentpno" value="${ebomInfo2.parentpno}"/>
<input type="hidden" id="parentrev" name="parentrev" value="${ebomInfo2.parentrev}"/>
<input type="hidden" id="ecosuboid" name="ecosuboid" value="${ebomInfo2.ecosuboid}"/>	<!-- db insert 때 사용	 mstoid, parentpno, parentrev -->
<input type="hidden" id="accumulatedata" name="accumulatedata" value="${ebomInfo2.accumulatedata}"/>	<!-- datagrid 보여줄때 사용 -->
<div class="x_panel" style="min-height:calc(100vh - 100px);">
   	<div class="x_title alert alert-success">
		<c:if test="${ebomInfo2.etcKind == 'search'}"><h3>&nbsp;EBOM 조회</h3></c:if>
		<c:if test="${ebomInfo2.etcKind == 'add'}"><h3>&nbsp;EBOM 부자재 추가</h3></c:if> 
		<div class="clearfix"></div>
  	</div>
	<table id="plmEbomDataGrid" class="table table-striped table-bordered table-hover" style="width:100%">
	  <thead>
	  	<tr class="center aligned">
			<th>상위품번(완제품)</th>
			<th>상위개정</th>
			<th>파트번호(유라코드)</th>
			<th>수량</th>
			<th>단위</th>
			<th>flag</th>
		</tr>
	  </thead>
	</table>
	<div class="ln_solid"></div>
	<div class="form-actions" align="right">
		<c:if test="${ebomInfo2.etcKind == 'add'}"><button type="button" id="addEbomSmBtn" class="btn btn-info" onclick="addEbomSubMaterials(); return false;" ><span class='glyphicon glyphicon-plus'></span>&nbsp;추가</button></c:if>
		<c:if test="${ebomInfo2.etcKind == 'add'}"><button type="button" id="saveEbomSmBtn" class="btn btn-info" onclick="saveEbomSubMaterials(); return false;" ><span class='glyphicon glyphicon-floppy-save'></span>&nbsp;저장</button></c:if>
		<button type="button" id="closeEbomSmBtn" class="btn btn-info" onclick="closeEbomSubMaterials(); return false;" ><span class='glyphicon glyphicon-remove'></span>&nbsp;닫기</button>
	  	<%-- <button type="button" id="closeEbomSmBtn" class="btn btn-info" data-dismiss="modal"><span class='glyphicon glyphicon-remove'></span>&nbsp;<spring:message code='button.close'/></button> --%>
	</div>
</div>
</form:form>

</body>
</html>