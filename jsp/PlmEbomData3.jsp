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

<title>상위품번 선택</title>

<script type="text/javaScript">
jQuery(document).ready(function() {
	setPlmSubmDataGrid3();
});

// 상위품번 선택    
function setPlmSubmDataGrid3() {
	var rows_selected = [];
	var plmSubmDataGrid3 = jQuery('#plmSubmDataGrid3').on( 'processing.dt', function ( e, settings, processing ) {
		processing ? jLoding('show') : jLoding('hide');
    }).DataTable( { 
		ajax: {    
		    url: "<c:url value='/com/yura/mbom/selectParentPnoSearch.do'/>",
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
			{ data: 'revision'}
		],
        columnDefs: [{
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
        }],
        select: {
        	'style': 'multi'
        },
        /* rowCallback: function(row, data, dataIndex){
            var rowId = data[0];

            if($.inArray(rowId, rows_selected) !== -1){
               $(row).find('input[type="checkbox"]').prop('checked', true);
               $(row).addClass('selected');
            }
        }, */
		searching: false,			/* 필터검색 여부 */
		language: language_locale,
		scrollX: true,    
		scrollY: $(window).height() * 55 / 100,      
	    fnInitComplete : function() {
	    	jQuery("#plmSubmDataGrid3 tbody tr").css('cursor', 'pointer');
	    	jLoding('hide');   
		}
	});
	
	// Handle click on checkbox
	$('#plmSubmDataGrid3 tbody').on('click', 'input[type="checkbox"]', function(e){
	      var $row = $(this).closest('tr');
		
	      // Get row data
	      var data = plmSubmDataGrid3.row($row).data();
	
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
	      updateDataTableSelectAllCtrl(plmSubmDataGrid3);
	
	      // Prevent click event from propagating to parent
	      e.stopPropagation();
   	});

   	// Handle click on table cells with checkboxes
   	$('#plmSubmDataGrid3').on('click', 'tbody td, thead th:first-child', function(e){
      	$(this).parent().find('input[type="checkbox"]').trigger('click');
   	});

    // Handle click on "Select all" control
    $('thead input[name="select_all"]', plmSubmDataGrid3.table().container()).on('click', function(e){
	      if(this.checked){
	         $('#plmSubmDataGrid3 tbody input[type="checkbox"]:not(:checked)').trigger('click');
	      } else {
	         $('#plmSubmDataGrid3 tbody input[type="checkbox"]:checked').trigger('click');
	      }
	
	      // Prevent click event from propagating to parent
	      e.stopPropagation();
   	});

	// Handle table draw event
	plmSubmDataGrid3.on('draw', function(){
      	// Update state of "Select all" control
      	updateDataTableSelectAllCtrl(plmSubmDataGrid3);
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

/* function setPlmSubmDataGrid3() {
	var table = jQuery('#plmSubmDataGrid3').on( 'processing.dt', function ( e, settings, processing ) {
		processing ? jLoding('show') : jLoding('hide');
    }).DataTable( { 
		ajax: {    
		    url: "<c:url value='/com/yura/mbom/selectParentPnoSearch.do'/>",
		    dataSrc: function ( json ) {
                return json.data;
		    },
		    type: "POST",			
            data: {
            	econo : $('#econo').val()
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
		scrollY: $(window).height() * 55 / 100,      
	    fnInitComplete : function() {
	    	jQuery("#plmSubmDataGrid2 tbody tr").css('cursor', 'pointer');
	    	jLoding('hide');   
		}
	});
	
	jQuery('#plmSubmDataGrid3 tbody').on('click', 'tr', function () {
		var data = table.row( this ).data();
		$('#parentpno').val(data.pno);
		$('#parentrev').val(data.revision);
		
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            table.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
	} ); 
} */


function selectParentPno() {
	var plmSubmDataTbl = $('#plmSubmDataGrid3').DataTable();
	var pnoArray = plmSubmDataTbl.rows('.selected').data();
	var parentpno = '';
	var parentrev = '';
	for(var i=0; i < pnoArray.length; i++) {
		parentpno += pnoArray[i].pno + ";";
		parentrev += pnoArray[i].revision + ";";
	}
	$('#parentpno').val(parentpno);
	$('#parentrev').val(parentrev);
	
	if ($('#parentpno').val()) {
		jQuery.ajax({
			url :"<c:url value='/com/yura/mbom/selectPlmEbomData.do'/>",
			data: {
				etcKind : "add",
				econo : $('#econo').val(),
				tmpaddoid : $('#tmpaddoid').val(),
				parentpno : $('#parentpno').val(),
				parentrev : $('#parentrev').val(),
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
		alert("상위품번을 선택하세요.");
	}
}

function selectCloseBtn(){
	$('#tmpaddoid').val('');
	$('#parentpno').val('');
	$('#parentrev').val('');
	$('#ecosuboid').val('');
	$('#accumulatedata').val('');
	jQuery("#targetModalLg").modal("hide");
}

jQuery('#ebomInfo3').parsley();

</script>
</head>
<body class="nav-md">

<form:form commandName="ebomInfo3" id="ebomInfo3" name="ebomInfo3" method="post" data-parsley-validate="validate" class="form-horizontal form-bordered">
<input type="hidden" id="econo" name="econo" value="${ebomInfo3.econo}"/>
<input type="hidden" id="oripno" name="oripno" value="${ebomInfo3.oripno}"/>
<input type="hidden" id="orirev" name="orirev" value="${ebomInfo3.orirev}"/>
<input type="hidden" id="tmpaddoid" name="tmpaddoid" value="${ebomInfo3.tmpaddoid}"/>
<input type="hidden" id="ecosuboid" name="ecosuboid" value="${ebomInfo3.ecosuboid}"/>
<input type="hidden" id="accumulatedata" name="accumulatedata" value="${ebomInfo3.accumulatedata}"/>
<input type="hidden" id="parentpno" name="parentpno"/>
<input type="hidden" id="parentrev" name="parentrev"/>
<div class="x_panel" style="min-height:calc(100vh - 100px);">
   	<div class="x_title alert alert-success">
		<h3>&nbsp;상위품번 리스트</h3>
		<div class="clearfix"></div>
  	</div>
			<!-- DataGrid 리스트 영역 -->
			<table id="plmSubmDataGrid3" class="table table-striped table-bordered table-hover" style="width:100%">
			  <thead>
			    <tr class="center aligned">
			    	<th><input name="select_all" value="1" type="checkbox"></th>
					<th>상위품번(완제품)</th>
					<th>개정</th>
			  	</tr>
			  </thead>
			</table>
			<div class="ln_solid"></div>
			<div class="form-actions" align="right">
				<button type="button" id="clkParentPno" class="btn btn-info" onclick="selectParentPno(); return false;" ><span class='glyphicon glyphicon-ok'></span>&nbsp;선택</button>
				<button type="button" id="clkClose" class="btn btn-info" onclick="selectCloseBtn(); return false;" ><span class='glyphicon glyphicon-remove'></span>&nbsp;닫기</button>	  	
			</div>
		</div>	
	</div>
</div>
</form:form>

</body>
</html>