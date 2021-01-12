<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui" %>
<%@ taglib prefix="sec"    uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">

<title>부자재 등록/수정</title>

<script type="text/javaScript">
jQuery(document).ready(function() {
    chkBtnAuthor();
    
    if($('#etcKind').val() == "update") {
    	$('#unit').val($('#hiddenUnit').val()).attr("selected", "selected");
    }
});

// 등록/수정
jQuery('#mbsmdInfo').on('submit',function(e) {
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
			jQuery('#mbsmdInfo').ajaxSubmit({
				url :"<c:url value='/com/yura/mbom/modifyMbomSubMaterials.do'/>",
				contentType : "application/x-www-form-urlencoded;charset=UTF-8",
				success : function (data){
					resultMsg(data);
        			var dt = $('#submaterialsGrid').DataTable();
        	    	dt.draw(1);
        	    	if ($('#btnFlag').val() == "onlyone")
        	    		jQuery("#targetModalLg").modal("hide");
        	    	else{
        	    		$('#pno').val('');
        	    		$('#usage').val('');
        	    		$('#unit').val('');
        	    	}
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

function btnFlagChg(msg) {
	$('#btnFlag').val(msg);
	jQuery('#mbsmdInfo').submit();
}

// 삭제
function fnBmDelete() {
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
	 		jQuery('#mbsmdInfo').ajaxSubmit({
	 			url :"<c:url value='/com/yura/mbom/deleteMbomSubMaterials.do'/>",
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
	 		}); 
            var close = window.swal.close;
            window.swal.close = function() {
                close();
                window.onkeydown = null;
            };    	 		
        });		
}

function btnPnoSelect() {
	jQuery.ajax({
		url :"<c:url value='/com/yura/mbom/selectSubPartNo.do'/>",
		data: {
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
}

jQuery('#mbsmdInfo').parsley();

</script>
</head>
<body>
<form:form commandName="mbsmdInfo" id="mbsmdInfo" name="mbsmdInfo" method="post" data-parsley-validate="validate" class="form-horizontal form-bordered">
<input type="hidden" id="etcKind" name="etcKind" value="${mbsmdInfo.etcKind}"/>
<input type="hidden" id="hiddenUnit" name="hiddenUnit" value="${mbsmdInfo.unit}"/>
<div class="x_panel">
   	<div class="x_title alert alert-success">
		<h3>&nbsp;부자재 등록/수정</h3> 
		<div class="clearfix"></div>
  	</div>
   	<div class="card-body">
   	<br/>
 		<div class="form-group">
			<div class="col-md-9 col-sm-9 col-xs-12">
				<input type="hidden" id="smoid" name="smoid" class="form-control" value="${mbsmdInfo.smoid}" readonly="true" />
				<input type="hidden" id="mtoid" name="mtoid" class="form-control" value="${mbsmdInfo.mtoid}" readonly="true" />
			</div>
	    </div>
		<%-- <div class="form-group">
			<label class="control-label col-md-3 col-sm-3 col-xs-12" for="파트번호">파트번호</label>
			<div class="col-md-9 col-sm-9 col-xs-12">
				<input id="pno" name="pno" class="form-control" maxlength="20" value="${mbsmdInfo.pno}" />
			</div>
		</div> --%>
		<c:if test="${mbsmdInfo.etcKind == 'insert'}">
			<div class="form-group">
				<div class="input-group">
					<label class="control-label col-md-3 col-sm-3 col-xs-12" style="width:208px;" for="부자재 코드">부자재 코드</label>
					<input id="pno" name="pno" class="form-control" style="width:570px; float: right;" value="${mbsmdInfo.pno}" readOnly/>
					<span class="input-group-btn">
			           	<button type="button" id="btnPno" class="btn btn-default" onclick="btnPnoSelect(); return false;"><span class="glyphicon glyphicon-search"></span></button>
			       	</span>
				</div>
			</div>
		</c:if>
		<c:if test="${mbsmdInfo.etcKind == 'update'}">
			<div class="form-group">
				<label class="control-label col-md-3 col-sm-3 col-xs-12" for="부자재 코드">부자재 코드</label>
				<div class="col-md-9 col-sm-9 col-xs-12">
					<input id="pno" name="pno" class="form-control" value="${mbsmdInfo.pno}" readOnly/>
				</div>
			</div>
		</c:if>
		<div class="form-group">
			<label class="control-label col-md-3 col-sm-3 col-xs-12" for="수량">수량</label>
			<div class="col-md-9 col-sm-9 col-xs-12">
				<input id="usage" name="usage" class="form-control" maxlength="20" value="${mbsmdInfo.usage}" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-3 col-sm-3 col-xs-12" for="단위">단위</label>
			<div class="col-md-9 col-sm-9 col-xs-12">
				<%-- <input id="unit" name="unit" class="form-control" maxlength="20" value="${mbsmdInfo.unit}" /> --%>
				<select id="unit" name="unit"class="form-control">
				    <option value=''></option>
				    <option value='g'>g</option>
				    <option value='EA'>EA</option>
				</select>
			</div>
		</div>
	</div>     
	<div class="ln_solid"></div>
	<div class="form-actions" align="right">
		<input type="hidden" id="btnFlag" name="btnFlag" class="form-control" readonly="true" />
		<c:if test="${mbsmdInfo.etcKind == 'insert'}"><button type="submit" id="passSaveContinue" class="btn btn-info chkRM" onclick="btnFlagChg('continue'); return false;"><span class='glyphicon glyphicon-floppy-save'></span>&nbsp;저장(추가)</button></c:if>
		<button type="submit" id="passSave" class="btn btn-info chkRM" onclick="btnFlagChg('onlyone'); return false;"><span class='glyphicon glyphicon-floppy-save'></span>&nbsp;<spring:message code='button.save' /></button>		
		<c:if test="${mbsmdInfo.etcKind == 'update'}"><button type="button" id="deleteBtn" class="btn btn-info chkRD" onclick="fnBmDelete(); return false;" ><span class='glyphicon glyphicon-trash'></span>&nbsp;<spring:message code='button.delete'/></button></c:if>
	  	<button type="button" id="closeBtn" class="btn btn-info" data-dismiss="modal"><span class='glyphicon glyphicon-remove'></span>&nbsp;<spring:message code='button.close'/></button>
	</div>
</div>
</form:form>
</body>
</html>