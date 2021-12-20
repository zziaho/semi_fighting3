package com.boong.board.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.boong.board.model.vo.Board;

import static com.boong.common.JDBCTemplate.close;

public class BoardDao {
	
	public BoardDao() {
		// TODO Auto-generated constructor stub
	}
	
	// BOARD테이블 전체 데이터를 가져오는 메소드
	public List<Board> selectBoard(Connection conn, int cPage, int numPerpage) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Board>	list = new ArrayList();
		String sql = "select * from(select rownum as rnum, b.* from(select * from board order by board_date desc) b) where rnum between ? and ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (cPage-1) * numPerpage + 1);
			pstmt.setInt(2, numPerpage);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Board b = Board.builder()
						.boardNo(rs.getInt("board_no"))
						.boardTitle(rs.getString("board_title"))
						.boardContent(rs.getString("board_content"))
						.boardDate(rs.getDate("board_date"))
						.boardViewCount(rs.getInt("board_view_count"))
						.boardLike(rs.getInt("board_like"))
						.boardCategory(rs.getInt("board_category"))
						.boardOriginalFilename(rs.getString("board_original_filename"))
						.boardRenamedFilename(rs.getString("board_renamed_filename"))
						.boardWriter(rs.getString("board_writer"))
						.build();
				list.add(b);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close(rs);
			close(pstmt);
		}
		return list;
	}
	
	// BOARD테이블 전체 데이터 개수를 반환하는 메소드
	public int selectBoardCount(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int totalData = 0;
		String sql = "select count(*) from board";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				totalData = rs.getInt(1);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close(rs);
			close(pstmt);
		}
		return totalData;
	}

}
