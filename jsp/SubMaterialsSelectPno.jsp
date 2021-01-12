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

<title>파트번호 선택</title>

<script type="text/javaScript">
jQuery(document).ready(function() {
	setPlmPnoDataGrid();
});


function setPlmPnoDataGrid() {
	var table = jQuery('#plmPnoDataGrid').on( 'processing.dt', function ( e, settings, processing ) {
		processing ? jLoding('show') : jLoding('hide');
    }).DataTable( { 
		ajax: {    
		    url: "<c:url value='/com/yura/mbom/selectPnoSearch.do'/>",
		    dataSrc: function ( json ) {
                return json.data;
		    },
		    type: "POST",
            /* data: {
            	pno : $('#partname').val()
            } */
		    data: function ( d ) {	/* Ajax 요청시 서버에 추가로 파라미터 전달 */
            	var fields = {};
				jQuery('#pnoInfo input, #pnoInfo select').each(function() {
					var _str = jQuery(this).val() == null ? '' : jQuery(this).val();
			    	fields[this.name] = _str.toString();
				});
				jQuery.extend(d, fields);
            	return d;			
            }
		},
        serverSide: true,			
        deferRender: true,
		columns: [
			{ data: 'pno'},
			{ data: 'revision'}
		],
		searching: false,			
		language: language_locale,
		scrollX: true,    
		scrollY: $(window).height() * 45 / 100,      
	    fnInitComplete : function() {
	    	jQuery("#plmPnoDataGrid tbody tr").css('cursor', 'pointer');
	    	jLoding('hide');
		}
	});
	
	jQuery('#plmPnoDataGrid tbody').on('click', 'tr', function () {
		var data = table.row( this ).data();
		$('#selectpno').val(data.pno);
		
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            table.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
	} ); 
}


function selectPnoBtn() {
	if ($('#selectpno').val()) {
		jQuery.ajax({
			url :"<c:url value='/com/yura/mbom/selectSubMaterialsDetail.do'/>",
			data: {
				etcKind : "insert",
				pno : $('#selectpno').val(),
				mtoid : $('#mtoid').val()
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
		alert("상위품번을 선택하세요.");
	}
}

function setResearch(){
	var table = $('#plmPnoDataGrid').DataTable();
	table.ajax.reload();
}

function selectCloseBtn(){
	$('#selectpno').val('');
	jQuery("#targetModalLg").modal("hide");
}

jQuery('#pnoInfo').parsley();

</script>
</head>
<body class="nav-md">

<form:form commandName="pnoInfo" id="pnoInfo" name="pnoInfo" method="post" data-parsley-validate="validate" class="form-horizontal form-bordered">
<input type="hidden" id="selectpno" name="selectpno"/>
<input type="hidden" id="mtoid" name="mtoid" value="${pnoInfo.mtoid}"/>
<div class="x_panel" style="min-height:calc(100vh - 100px);">
   	<div class="x_title alert alert-success">
		<h3>&nbsp;부자재 코드 리스트</h3>
		<div class="clearfix"></div>
  	</div>
  	
  	<div class="form-group">
  		<div class="input-group" style="padding: 15px 0 0 0;">
			<label class="control-label col-md-3 col-sm-3 col-xs-12" style="width:150px;" for="부자재 코드">부자재 코드</label>
			<input id="partname" name="partname" class="form-control" style="width:500px;"/>
			<span class="input-group-btn" style="float: left;">
				<button type="button" id="searchPartname" class="btn btn-info" onclick="setResearch(); return false;" ><span class='glyphicon glyphicon-search'></span>&nbsp;검색</button>            
			</span>
		</div>
   	</div>
  	
	<div class="ln_solid"></div>
  	
	<!-- DataGrid 리스트 영역 -->
	<table id="plmPnoDataGrid" class="table table-striped table-bordered table-hover" style="width:100%">
	  <thead>
	    <tr class="center aligned">
			<th>부자재 코드</th>
			<th>개정</th>
	  	</tr>
	  </thead>
	</table>
	<div class="ln_solid"></div>
	<div class="form-actions" align="right">
		<button type="button" id="clkSelect" class="btn btn-info" onclick="selectPnoBtn(); return false;" ><span class='glyphicon glyphicon-ok'></span>&nbsp;선택</button>
		<button type="button" id="clkClose" class="btn btn-info" onclick="selectCloseBtn(); return false;" ><span class='glyphicon glyphicon-remove'></span>&nbsp;닫기</button>	  	
	</div>
</div>
</form:form>

</body>
</html>