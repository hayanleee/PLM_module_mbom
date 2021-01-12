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

<title>파트개정</title>

<script type="text/javaScript">
jQuery(document).ready(function() {
	setRevPartDataGrid();
});

// 개정할 파트 선택
function setRevPartDataGrid() {
	var rows_selected = [];
	var revPartDataGrid = jQuery('#revPartDataGrid').on( 'processing.dt', function ( e, settings, processing ) {
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
		pageLength: 10,				/* 페이지별 보여지는 row 수 */
        lengthChange: true,			/* 페이지별 개수 뷰 여부 */
        lengthMenu : [  [10, 25, 50, 100, -1 ], [10, 25, 50, 100, "All"]  ],	//페이징 길이 select옵션 지정        
        serverSide: true,			/* 서버단에서 처리 */
        deferRender: true,		 	/* 대용량 데이타 화면출력시 속도 개선 옵션 */
		columns: [
			{ data: 'active'},
			{ data: 'pno'},
			{ data: 'revision'},
			{ data: 'name'},
			{ data: 'plmtype'},
			{ data: 'product'},
			{ data: 'state'}
		],
        columnDefs: [
        	{
	            'targets': 0,
	            'searchable': false,
	            'orderable': false,
	            'width': '1%',
	            'className': 'dt-body-center',
	            'render': function (data, type, full, meta){
	                return '<input type="checkbox">';
	            },
	            'checkboxes': {
	 				'selectRow': true            	
	            }
        	},
        	{
        		"targets": [ 6 ],
	        	"visible": false
        	}
        ],
        select: {
        	'style': 'multi'
        },
		searching: false,			/* 필터검색 여부 */
		language: language_locale,
		scrollX: true,    
		scrollY: $(window).height() * 45 / 100,      
	    fnInitComplete : function() {
	    	jQuery("#revPartDataGrid tbody tr").css('cursor', 'pointer');
	    	jLoding('hide');   
		}
	});
	
	// Handle click on checkbox
	$('#revPartDataGrid tbody').on('click', 'input[type="checkbox"]', function(e){
	      var $row = $(this).closest('tr');
		
	      // Get row data
	      var data = revPartDataGrid.row($row).data();
	
	      // Get row ID
	      var rowId = data[0];
	
	      // Determine whether row ID is in the list of selected row IDs 
	      var index = $.inArray(rowId, rows_selected);
	
	      // If checkbox is checked and row ID is not in list of selected row IDs
	      if(this.checked && index === -1){
	         rows_selected.push(rowId);
	
	      // Otherwise, if checkbox is not checked and row ID is in list of selected row IDs
	      } else if (!this.checked && index !== -1){
	         rows_selected.splice(index, 1);
	      }
	
	      if(this.checked){
	         $row.addClass('selected');
	      } else {
	         $row.removeClass('selected');
	      }
	
	      // Update state of "Select all" control
	      updateDataTableSelectAllCtrl(revPartDataGrid);
	
	      // Prevent click event from propagating to parent
	      e.stopPropagation();
   	});

   	// Handle click on table cells with checkboxes
   	$('#revPartDataGrid').on('click', 'tbody td, thead th:first-child', function(e){
      	$(this).parent().find('input[type="checkbox"]').trigger('click');
   	});

    // Handle click on "Select all" control
    $('thead input[name="select_all"]', revPartDataGrid.table().container()).on('click', function(e){
	      if(this.checked){
	         $('#revPartDataGrid tbody input[type="checkbox"]:not(:checked)').trigger('click');
	      } else {
	         $('#revPartDataGrid tbody input[type="checkbox"]:checked').trigger('click');
	      }
	
	      // Prevent click event from propagating to parent
	      e.stopPropagation();
   	});

	// Handle table draw event
	revPartDataGrid.on('draw', function(){
      	// Update state of "Select all" control
      	updateDataTableSelectAllCtrl(revPartDataGrid);
   	});

   	// Handle form submission event 
   	$('#frm-example').on('submit', function(e){
	      var form = this;
	      
	      // Iterate over all selected checkboxes
	      $.each(rows_selected, function(index, rowId){
	         // Create a hidden element 
	         $(form).append(
	             $('<input>')
	                .attr('type', 'hidden')
	                .attr('name', 'id[]')
	                .val(rowId)
	         );
	      });
   	});
}

function updateDataTableSelectAllCtrl(table){
	   var $table             = table.table().node();
	   var $chkbox_all        = $('tbody input[type="checkbox"]', $table);
	   var $chkbox_checked    = $('tbody input[type="checkbox"]:checked', $table);
	   var chkbox_select_all  = $('thead input[name="select_all"]', $table).get(0);

	   // If none of the checkboxes are checked
	   if($chkbox_checked.length === 0){
	      chkbox_select_all.checked = false;
	      if('indeterminate' in chkbox_select_all){
	         chkbox_select_all.indeterminate = false;
	      }

	   // If all of the checkboxes are checked
	   } else if ($chkbox_checked.length === $chkbox_all.length){
	      chkbox_select_all.checked = true;
	      if('indeterminate' in chkbox_select_all){
	         chkbox_select_all.indeterminate = false;
	      }

	   // If some of the checkboxes are checked
	   } else {
	      chkbox_select_all.checked = true;
	      if('indeterminate' in chkbox_select_all){
	         chkbox_select_all.indeterminate = true;
	      }
	   }
}

function selectRevisionBtn() {
	var revPartDataTbl = $('#revPartDataGrid').DataTable();
	var pnoArray = revPartDataTbl.rows('.selected').data();
	var pno = '';
	var rev = '';
	for(var i=0; i < pnoArray.length; i++) {
		pno += pnoArray[i].pno + ";";
		rev += pnoArray[i].revision + ";";
	}
	$('#pno').val(pno);
	$('#rev').val(rev);
	
	if ($('#revlevel').val()) {
		if ($('#pno').val()) {
			jQuery.ajax({
	    		url :"<c:url value='/com/yura/mbom/revisionPLMPart.do'/>",		
	    		data: {
					econo : $('#econo').val(),
					pno : $('#pno').val(),
					rev : $('#rev').val(),
					level : $('#revlevel').val(),
					oripno : $('#oripno').val(),
					orirev : $('#orirev').val()
	    		},
	    		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
	    		success : function (data){
	    			if(data == 'Success'){
	    				alertMsg("<spring:message code='button.confirm'/>", "<spring:message code='success.request.msg'/>", "info");    				
	    		 		jQuery("#targetModalLg").modal("hide");
	    			} else {
	    				resultMsg("fail");
	    			}
	    		},
	    		error : function(xhr, status, error){
	    			resultMsg("fail");
	    		}
			});
		} else {
			alert("개정할 파트를 선택하세요.");
		}
	} else {
		alert("개정 번호를 입력하세요.");
	}
}

function selectCloseBtn(){
	jQuery("#targetModalLg").modal("hide");
}

jQuery('#revPart').parsley();

</script>
</head>
<body class="nav-md">

<form:form commandName="revPart" id="revPart" name="revPart" method="post" data-parsley-validate="validate" class="form-horizontal form-bordered">
<input type="hidden" id="econo" name="econo" value="${revPart.econo}"/>
<input type="hidden" id="oripno" name="oripno" value="${revPart.oripno}"/>
<input type="hidden" id="orirev" name="orirev" value="${revPart.orirev}"/>
<input type="hidden" id="pno" name="pno"/>
<input type="hidden" id="rev" name="rev"/>
<div class="x_panel" style="min-height:calc(100vh - 100px);">
   	<div class="x_title alert alert-success">
		<h3>&nbsp;파트개정 리스트</h3>
		<div class="clearfix"></div>
  	</div>
  	
  	<div class="form-group">
  		<div class="input-group" style="padding: 15px 0 0 0;">
			<label class="control-label col-md-3 col-sm-3 col-xs-12" style="width:200px;" for="Custom Revision Level">Custom Revision Level</label>
			<input id="revlevel" name="revlevel" class="form-control" style="width:500px;"/>
		</div>
   	</div>
  	
	<div class="ln_solid"></div>
	
	<!-- DataGrid 리스트 영역 -->
	<table id="revPartDataGrid" class="table table-striped table-bordered table-hover" style="width:100%">
	  <thead>
	    <tr class="center aligned">
	    	<th><input name="select_all" value="1" type="checkbox"></th>
			<th>파트번호</th>
			<th>개정</th>
			<th>이름</th> 
			<th>아이템</th>
			<th>제품</th>
			<th>상태</th>
	  	</tr>
	  </thead>
	</table>
	<div class="ln_solid"></div>
	<div class="form-actions" align="right">
		<button type="button" id="clkRevPart" class="btn btn-info" onclick="selectRevisionBtn(); return false;" ><span class='glyphicon glyphicon-ok'></span>&nbsp;개정</button>
		<button type="button" id="clkClose" class="btn btn-info" onclick="selectCloseBtn(); return false;" ><span class='glyphicon glyphicon-remove'></span>&nbsp;닫기</button>	  	
	</div>
</div>
</form:form>

</body>
</html>