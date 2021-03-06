<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ page import="com.boong.shop.model.vo.*,java.util.List" %>
<%   
	Order order=(Order)request.getAttribute("order");
	List<OrderProduct> list=(List)request.getAttribute("orderProductList");
	int total=0;
	for(OrderProduct op : list){
		total+=(op.getShopProductPrice())*(op.getOrderProductNumber()); 
	}
	order.setOrderPrice(total+3000);
%>

<%@ include file="/views/common/header.jsp"%>

<style>
	section>div{width:80%; text-align: center;margin: 0 10% 0 10%;}
	section#pm-container{width:900px; margin:0 auto; margin-bottom:50px; padding-top:20px; text-align:center;}
	section#pm-container h2{margin:10px 0;}
	section#pm-container .enroll-btn{margin:5px; }
	section#pm-container .enroll-btn-container{width:100%; text-align: right;}
	table#tbl-pm{width:100%; margin:0 auto; /* border-top:1px solid black; */ border-collapse:collapse; clear:both; }
	table#tbl-pm th, table#tbl-pm td {border-bottom:2px solid #ddd;  padding: 5px 0; text-align:center;} 
	/*글쓰기버튼*/
	input#btn-add{float:right; margin: 0 0 15px;}
	/*페이지바*/
	div#pageBar{margin-top:10px; text-align:center; background-color:rgba(1,138,216,1);}
	div#pageBar span{color: white;}	
	#tbl-pm td>input{width:50px;}
	
	div#order-container
	{
		padding-top:50px;
		width:800px;
		margin:0 auto;
		text-align:center;
	}
	div#order-container h2
	{
		margin:10px 0;
	}
	table#tbl-order
	{
		
		margin:0 auto;
		/* border-bottom:1px solid black; */
		border-collapse:collapse;
	}
	table#tbl-order th
	{
		width:125px;
		border-top:2px solid #ddd; 
		padding:10px ;
		text-align:center;
	}
	table#tbl-order td
	{
		border-top:2px solid #ddd;  
		padding:5px 5px 5px 10px;
		text-align:left;
	}
	div.total-price{height: 50px; padding-top: 10px;}
	.btn{
	width:70px; height:35px;
	border:none;
	border-radius: 10px;
	cursor:pointer; 
	background:rgba(1,138,216,1); color:white;
	font-size:13px;
	
	box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
	}
	.btn:hover{
	color:lightgrey;
	}
	
</style>

<div style="height: 63px; background-color: rgba(1,138,216,1)"></div>

	<section id="pm-container">
		<h2 >주문페이지</h2>
		
			
		
		<%-- <div class="enroll-btn-container">
			<form action="<%=request.getContextPath()%>/shop/orderEnroll.do" method="post">
			<input type="hidden" name="id" value="<%=loginMember.getMemberId()%>">
			<input class="enroll-btn" type="submit" value="주문하기" >		
			</form>
		</div> --%>
		
		<table id="tbl-pm">
			<tr>
				<th colspan="2">상품명</th>
									
				<th>가격</th>
				<th>수량</th>
				<th>수정</th>
				<th>삭제</th>				
			</tr>
			<%
			if(list.isEmpty()){
			%>
				<tr>
					<td colspan="6">조회된 상품이 없습니다</td>
				</tr>
			<%
			}else{ 
					
					for(OrderProduct op : list){
			%>
				<tr>
					<td width="200px" height="100px" style="background-size:cover ;background-image:url('<%=request.getContextPath()%>/upload/shop/<%=op.getShopProductImageRename()%>');" ></td>
					<td>
						<a style="text-decoration-line:none; " href="<%=request.getContextPath()%>/shop/shopView.do?shopProductId=<%=op.getProductId()%>">
							<%=op.getShopProductName() %>
						</a>
					</td>					
					<td><%=(op.getShopProductPrice())*(op.getOrderProductNumber())%></td>
					<form action="<%=request.getContextPath()%>/shop/orderUpdate.do" method="post">
					<input type="hidden" name="orderId" value="<%=op.getOrderId()%>">
					<input type="hidden" name="productId" value="<%=op.getProductId()%>">
					<input type="hidden" name="memberId" value="<%=loginMember.getMemberId()%>">
					
					<td><input  type="number" name="orderNum" value="<%=op.getOrderProductNumber()%>" min="0" max="<%=op.getShopProductStock()%>"></td>
					<%System.out.println(loginMember.getMemberId()); %>
					<td>
						<input type="submit" class="btn" value="수정" >				
					</td>
					</form>
					<form action="<%=request.getContextPath()%>/shop/orderDelete.do" method="post">
					<td>
						<input type="submit" class="btn" value="삭제">
						<input type="hidden" name="orderId" value="<%=op.getOrderId()%>">			
						<input type="hidden" name="productId" value="<%=op.getProductId()%>">			
						<input type="hidden" name="memberId" value="<%=loginMember.getMemberId()%>">			
					</td>
					</form>
				</tr>
			<%	}
			}%>
		</table>
		<div class="total-price"><h5>결제금액 = 상품금액 합 + 배송비 = <%=(order.getOrderPrice())-3000%>원 + 3000원 = <%=order.getOrderPrice()%>원</h5></div>
		
		<div id="order-container">
			<h2>배송지 정보</h2>
			<form action='<%=request.getContextPath()%>/shop/orderEnd.do' method="post">
			<table id="tbl-order">
			
				
				<tr>
					<th>수령인</th>
					<td><input type="text" name="name" value="<%=order.getOrderReceiver()%>"></td>
				</tr>
				<tr>
					<th>배송지</th>
					<td><input type="text" name="address" value="<%=order.getOrderReceiverAdd()%>" ></td>
				</tr>
				<tr>
					<th>전화번호</th>
					<td><input type="text" name="phone" value="<%=order.getOrderPhone()%>" ></td>
				</tr>
					<input type="hidden" name="price" value="<%=order.getOrderPrice()%>" >
				<tr>
					<th>요청사항</th>
					<td><textarea cols="50" rows="4" name="request" ><%=order.getOrderRequest() %></textarea></td>
				</tr>
				<tr>
					<th colspan="2">
						<input type="submit" class="btn" style="width: 90px;" value="결제하기">
						<button class="btn" onclick="location.assign('<%=request.getContextPath()%>/shop/Main.do')">취소</button>						
					</th>
				</tr>
			
			</table>
			
			</form>
		</div>
		
		
		
		
		
		
		
	</section>


















<%@ include file="/views/common/footer.jsp"%>
