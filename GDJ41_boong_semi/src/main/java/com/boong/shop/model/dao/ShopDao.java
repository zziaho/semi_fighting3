package com.boong.shop.model.dao;

import static com.boong.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.boong.member.model.vo.Member;
import com.boong.shop.model.vo.Basket;
import com.boong.shop.model.vo.Order;
import com.boong.shop.model.vo.OrderProduct;
import com.boong.shop.model.vo.Product;
import com.boong.shop.model.vo.ProductComment;

public class ShopDao {

	public ShopDao() {
		/*
		 * try { String
		 * path=ShopDao.class.getResource("/sql/shop/shopsql.properties").getPath();
		 * Shop.load(new FileReader(path)); }catch(IOException e) { e.printStackTrace();
		 * }
		 */
	}

	public List<Product> selectProductList(Connection conn, int cPage, int numPerpage) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Product> result = new ArrayList();
		String sql = "SELECT * FROM (SELECT ROWNUM AS RNUM, P.* FROM (SELECT * FROM SHOP_PRODUCT ORDER BY SHOP_PRODUCT_DATE ASC) P ) WHERE RNUM BETWEEN ? AND ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (cPage - 1) * numPerpage + 1);
			pstmt.setInt(2, cPage * numPerpage);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Product p = Product.builder().shopProductId(rs.getInt("shop_product_id"))
						.shopProductName(rs.getString("shop_product_name"))
						.shopProductPrice(rs.getInt("shop_product_price"))
						.shopProductContent(rs.getString("shop_product_content"))
						.shopProductStock(rs.getInt("shop_product_stock"))
						.shopProductDate(rs.getDate("shop_product_date"))
						.shopProductSales(rs.getInt("shop_product_sales"))
						.shopProductImage(rs.getString("shop_product_image"))
						.shopProductImageRename(rs.getString("shop_product_imagerename")).build();
				result.add(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return result;

	}

	public int selectProductListCount(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		String sql = "SELECT COUNT(*) FROM SHOP_PRODUCT";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return result;
	}

	public Product selectProduct(Connection conn, int shopProductId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Product p = null;
		String sql = "SELECT * FROM SHOP_PRODUCT WHERE SHOP_PRODUCT_ID=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, shopProductId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				p = Product.builder().shopProductId(rs.getInt("shop_product_id"))
						.shopProductName(rs.getString("shop_product_name"))
						.shopProductPrice(rs.getInt("shop_product_price"))
						.shopProductContent(rs.getString("shop_product_content"))
						.shopProductStock(rs.getInt("shop_product_stock"))
						.shopProductDate(rs.getDate("shop_product_date"))
						.shopProductSales(rs.getInt("shop_product_sales"))
						.shopProductImage(rs.getString("shop_product_image"))
						.shopProductImageRename(rs.getString("shop_product_imagerename")).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return p;
	}

	public int insertProduct(Connection conn, Product p) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "INSERT INTO SHOP_PRODUCT VALUES(DEFAULT,?,?,?,?,DEFAULT,DEFAULT,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			// default
			pstmt.setString(1, p.getShopProductName());
			pstmt.setInt(2, p.getShopProductPrice());
			pstmt.setString(3, p.getShopProductContent());
			pstmt.setInt(4, p.getShopProductStock());
			// default
			// default
			pstmt.setString(5, p.getShopProductImage());
			pstmt.setString(6, p.getShopProductImageRename());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int insertProductComment(Connection conn, ProductComment pc) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "INSERT INTO SHOP_COMMENT VALUES(DEFAULT,?,DEFAULT,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pc.getShopCommentContent());
			pstmt.setInt(2, pc.getShopCommentLevel());
			pstmt.setInt(3, pc.getShopCommentRef());
			pstmt.setString(4, pc.getMemberId());
			pstmt.setInt(5, pc.getShopProductId());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public List<ProductComment> selectProductCommentList(Connection conn, int shopProductId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductComment> list = new ArrayList();
		String sql = "SELECT * FROM SHOP_COMMENT WHERE SHOP_PRODUCT_ID=? START WITH SHOP_COMMENT_LEVEL=1 CONNECT BY PRIOR SHOP_COMMENT_ID=SHOP_COMMENT_REF";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, shopProductId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProductComment pc = ProductComment.builder().shopCommentId(rs.getInt("shop_comment_id"))
						.shopCommentContent(rs.getString("shop_comment_content"))
						.shopCommentDate(rs.getDate("shop_comment_date"))
						.shopCommentLevel(rs.getInt("shop_comment_level")).shopCommentRef(rs.getInt("shop_comment_ref"))
						.memberId(rs.getString("member_id")).shopProductId(rs.getInt("shop_product_id")).build();

				list.add(pc);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return list;
	}

	public int updateProduct(Connection conn, Product p) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "UPDATE SHOP_PRODUCT SET SHOP_PRODUCT_NAME=?,SHOP_PRODUCT_PRICE=?,SHOP_PRODUCT_CONTENT=?,SHOP_PRODUCT_STOCK=?,SHOP_PRODUCT_IMAGE=?,SHOP_PRODUCT_IMAGERENAME=? WHERE SHOP_PRODUCT_ID=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, p.getShopProductName());
			pstmt.setInt(2, p.getShopProductPrice());
			pstmt.setString(3, p.getShopProductContent());
			pstmt.setInt(4, p.getShopProductStock());
			pstmt.setString(5, p.getShopProductImage());
			pstmt.setString(6, p.getShopProductImageRename());
			pstmt.setInt(7, p.getShopProductId());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int insertBasket(Connection conn, Basket b) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "INSERT INTO SHOP_BASKET VALUES(?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b.getBasketNumber());
			pstmt.setString(2, b.getMemberId());
			pstmt.setInt(3, b.getProductId());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;

	}

	public List<Basket> selectBasket(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Basket> list = new ArrayList();
		String sql = "SELECT * FROM SHOP_BASKET JOIN SHOP_PRODUCT  USING (SHOP_PRODUCT_ID) WHERE MEMBER_ID=?";
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, memberId);
			rs = pstmt.executeQuery();
			while (rs.next()) {				

				 Basket b=Basket.builder() 
						 .memberId(rs.getString("member_id"))
						 .productId(rs.getInt("shop_product_id"))
						 .basketNumber(rs.getInt("shop_basket_number"))
						 .shopProductName(rs.getString("shop_product_name"))
						 .shopProductPrice(rs.getInt("shop_product_price"))
						 .shopProductStock(rs.getInt("shop_product_stock"))
						 .shopProductImageRename(rs.getString("shop_product_imagerename"))
						 .build();
				 
				list.add(b);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return list;
	}

	public int updateBasket(Connection conn, Basket b) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "UPDATE SHOP_BASKET SET SHOP_BASKET_NUMBER=?  WHERE SHOP_PRODUCT_ID=? AND MEMBER_ID=? ";
		try {
			System.out.println(b);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b.getBasketNumber());
			pstmt.setInt(2, b.getProductId());
			pstmt.setString(3, b.getMemberId());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		System.out.println(result);
		return result;
		
	}

	public int deleteBasket(Connection conn, Basket b) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "DELETE FROM SHOP_BASKET WHERE SHOP_PRODUCT_ID=? AND MEMBER_ID=? ";
		try {
			System.out.println(b);
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, b.getProductId());
			pstmt.setString(2, b.getMemberId());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		System.out.println(result);
		return result;
	}

	public int insertOrder(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "INSERT INTO SHOP_ORDER VALUES(DEFAULT,?,DEFAULT,DEFAULT,DEFAULT,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "???????????? ??????");
			pstmt.setString(2, member.getMemberId());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getAddress());
			pstmt.setString(5, member.getPhone());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public Order selectOrder(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Order o = null;
		String sql = "SELECT * FROM SHOP_ORDER WHERE MEMBER_ID=? AND SHOP_ORDER_STATUS=0";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				o=Order.builder()
						.orderId(rs.getInt("shop_order_id"))
						.orderReceiver(rs.getString("shop_order_receiver"))
						.orderReceiverAdd(rs.getString("shop_order_receiveradd"))
						.orderPhone(rs.getString("shop_order_phone"))
						.orderRequest(rs.getString("shop_order_request"))
						.orderPrice(rs.getInt("shop_order_price"))
						.orderDate(rs.getDate("shop_order_date"))
						.orderStatus(rs.getInt("shop_order_status"))
						.memberId(rs.getString("member_id"))
						.build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return o;
	}

	public int insertOrderProduct(Connection conn, int orderId, Basket b) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "INSERT INTO SHOP_ORDER_PRODUCT VALUES(?,?,?,DEFAULT)";
		try {			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b.getProductId());
			pstmt.setInt(2, orderId);
			pstmt.setInt(3, b.getBasketNumber());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int deleteBasket(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "DELETE FROM SHOP_BASKET WHERE MEMBER_ID=? ";
		try {			
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, memberId);			
			result = pstmt.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		System.out.println(result);
		return result;
	}

	public List<OrderProduct> selectOrderProduct(Connection conn, int orderId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrderProduct> list = new ArrayList();
		String sql = "SELECT * FROM SHOP_ORDER_PRODUCT JOIN SHOP_PRODUCT USING (SHOP_PRODUCT_ID)  WHERE SHOP_ORDER_ID=? AND ORDER_PRODUCT_STATUS=0 ";
		try {
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, orderId);
			rs = pstmt.executeQuery();
			while (rs.next()) {				
				OrderProduct op=OrderProduct.builder()
						.productId(rs.getInt("SHOP_PRODUCT_ID"))
						.orderId(rs.getInt("SHOP_ORDER_ID"))
						.orderProductNumber(rs.getInt("ORDER_PRODUCT_NUMBER"))
						.orderProductStatus(rs.getInt("ORDER_PRODUCT_STATUS"))
						
						.shopProductName(rs.getString("SHOP_PRODUCT_NAME"))
						.shopProductPrice(rs.getInt("SHOP_PRODUCT_PRICE"))
						.shopProductStock(rs.getInt("SHOP_PRODUCT_STOCK"))
						.shopProductImageRename(rs.getString("SHOP_PRODUCT_IMAGERENAME"))
						.build();				 
				list.add(op);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return list;
	}

	public int updateOrder(Connection conn, OrderProduct op) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "UPDATE SHOP_ORDER_PRODUCT SET ORDER_PRODUCT_NUMBER=?  WHERE SHOP_PRODUCT_ID=? AND SHOP_ORDER_ID=? ";
		try {
			System.out.println(op);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, op.getOrderProductNumber());
			pstmt.setInt(2, op.getProductId());
			pstmt.setInt(3, op.getOrderId());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		System.out.println(result);
		return result;
	}

	public int deleteOrder(Connection conn, OrderProduct op) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "DELETE FROM SHOP_ORDER_PRODUCT WHERE SHOP_ORDER_ID=? AND SHOP_PRODUCT_ID=?  ";
		try {			
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, op.getOrderId());			
			pstmt.setInt(2, op.getProductId());			
			result = pstmt.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		System.out.println(result);
		return result;
	}

}
