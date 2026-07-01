<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Home</title>

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {

			// 1. 일반 PUT 요청 버튼 이벤트
			$("#putBtn").on("click", function() {
				var boardNoVal = $("#boardNo").val();
				var titleVal = $("#title").val();
				var contentVal = $("#content").val();
				var writerVal = $("#writer").val();

				var boardObject = {
					boardNo : boardNoVal,
					title : titleVal,
					content : contentVal,
					writer : writerVal
				};

				$.ajax({
					type : "put",
					url : "/board/update/" + boardNoVal,
					data : JSON.stringify(boardObject),
					contentType : "application/json; charset=utf-8",
					success : function(result) {
						console.log("result: " + result);
						if (result === "SUCCESS") {
							alert("SUCCESS");
						}
					} // <-- success 함수를 여기서 올바르게 닫아줍니다.
				}); // <-- $.ajax를 여기서 올바르게 닫아줍니다.
			}); // <-- #putBtn 클릭 이벤트를 여기서 올바르게 닫아줍니다.

			// 2. 헤더 포함 PUT 요청 버튼 이벤트 (body에 버튼이 있어 미리 구현해 두었습니다)
			$("#putHeaderBtn").on("click", function() {
				
				var boardNoVal = $("#boardNo").val();
				var titleVal = $("#title").val();
				var contentVal = $("#content").val();
				var writerVal = $("#writer").val();

				var boardObject = {
					boardNo : boardNoVal,
					title : titleVal,
					content : contentVal,
					writer : writerVal
				};

				$.ajax({
					type : "put",
					url : "/board/update/" + boardNoVal,
					headers : {
						"X-HTTP-Method-Override" : "PUT"
					},
					data : JSON.stringify(boardObject),
					contentType : "application/json; charset=utf-8",
					success : function(result) {
						console.log("result: " + result);
						if (result === "SUCCESS") {
							alert("SUCCESS");
						}
					}
				});
			});

		}); // <-- $(document).ready를 여기서 최종적으로 닫아줍니다.
	</script>
</head>
<body>
<h1>Ajax Home</h1>
<form>
	boardNo: <input type="text" name="boardNo" value="" id="boardNo"><br>
	title: <input type="text" name="title" value="" id="title"><br>
	content: <input type="text" name="content" value="" id="content"><br>
	writer: <input type="text" name="writer" value="" id="writer"><br>
</form>
<div>
	<button id="putBtn">수정(put)</button>
	<button id="putHeaderBtn">수정(put with header)</button>
</div>
</body>
</html>