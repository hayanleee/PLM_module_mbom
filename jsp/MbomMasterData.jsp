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

<title>부자재마스터 등록/수정</title>

<script type="text/javaScript">
jQuery(document).ready(function() {
    chkBtnAuthor();
});

// 등록/수정
jQuery('#mbmtInfo').on('submit',function(e) {
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
			jQuery('#mbmtInfo').ajaxSubmit({
				url :"<c:url value='/com/yura/mbom/modifyMbomMasterData.do'/>",
				contentType : "application/x-www-form-urlencoded;charset=UTF-8",
				success : function (data){
					resultMsg(data);
        			var dt = $('#smGrid').DataTable();
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
	 		jQuery('#mbmtInfo').ajaxSubmit({
	 			url :"<c:url value='/com/yura/mbom/deleteMbomMasterData.do'/>",
	 			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
	 			success : function (data){
	 				resultMsg(data);
        			var dt = $('#smGrid').DataTable();
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

jQuery('#mbmtInfo').parsley();

</script>
</head>
<body>
<form:form commandName="mbmtInfo" id="mbmtInfo" name="mbmtInfo" method="post" data-parsley-validate="validate" class="form-horizontal form-bordered">
<input type="hidden" id="etcKind" name="etcKind" value="${mbmtInfo.etcKind}"/>
<div class="x_panel">
   	<div class="x_title alert alert-success">
		<h3>&nbsp;부자재마스터 등록/수정</h3> 
		<div class="clearfix"></div>		
  	</div>
   	<div class="card-body">
   	<br/>
 		<div class="form-group">
		    <c:choose>             
				<c:when test="${mbmtInfo.etcKind == 'update'}">
					<div class="col-md-9 col-sm-9 col-xs-12">
						<input type="hidden" id="smoid" name="smoid" class="form-control" value="${mbmtInfo.oid}" readonly="true" />
					</div>  
				</c:when>     
		    </c:choose>         
	    </div>
		<div class="form-group">
			<label class="control-label col-md-3 col-sm-3 col-xs-12" for="부자재 조합">부자재 조합</label>
			<div class="col-md-9 col-sm-9 col-xs-12">
				<input id="name" name="name" class="form-control" maxlength="20" value="${mbmtInfo.name}" />
			</div>
		</div>
	</div>     
	<div class="ln_solid"></div>
	<div class="form-actions" align="right">
		<button type="submit" id="passSave" class="btn btn-info chkRM"><span class='glyphicon glyphicon-floppy-save'></span>&nbsp;<spring:message code='button.save' /></button>
		<c:if test="${mbmtInfo.etcKind == 'update'}"><button type="button" id="deleteBtn" class="btn btn-info chkRD" onclick="fnBmDelete(); return false;" ><span class='glyphicon glyphicon-trash'></span>&nbsp;<spring:message code='button.delete'/></button></c:if>
	  	<button type="button" id="closeBtn" class="btn btn-info" data-dismiss="modal"><span class='glyphicon glyphicon-remove'></span>&nbsp;<spring:message code='button.close'/></button>
	</div>
</div>
</form:form>
</body>
</html>