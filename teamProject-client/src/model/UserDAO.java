package model;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private Connection getConnection() {
	//	if (conn == null) {
			String user = "madang";
			String passwd = "madang"; // 달라진부분
			String unicode = "autoReconnect=true&characterEncoding=UTF-8";
			String url = "jdbc:mysql://35.190.228.94:3306/teamdb?";//
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(url + unicode, user, passwd);
			} catch (Exception e) {
				e.printStackTrace();
			}
	//	}
		return conn;
	}

	// �ڿ�����
	private void dbClose(Connection conn, PreparedStatement pstmt) {
		try {			
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void dbClose(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {

			if (rs != null)
				rs.close();			
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int user_insert(String id, String username, String password) {
		int result = 0;
		try {
			conn = getConnection();
			String sql = "insert into usertbl(id,username,password)";
			sql += " values(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, username);
			pstmt.setString(3, password);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose(conn, pstmt);
		}

		return result;
	}

	//select
	public UserVO getUser(int no) {
		conn = getConnection();
		String sql = "select * from usertbl where no=?";
		UserVO vo = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				vo = new UserVO();
				vo.setID(rs.getString(1));
				vo.setUsername(rs.getString(2));
				vo.setPassword(rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose(conn, pstmt, rs);
		}
		return vo;
	}

	//delete
	public int user_delete(String id) {
		conn = getConnection();
		String sql = "delete from usertbl where id=? ";
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose(conn, pstmt);
		}
		return result;

	}

	public ArrayList<UserVO> getUsers() {
		conn = getConnection();
		String sql = "select * from usertbl";
		ArrayList<UserVO> list = new ArrayList<>();
		try {
			if(conn!=null) {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				UserVO vo = new UserVO();
				vo.setID(rs.getString(1));
				vo.setUsername(rs.getString(2));
				vo.setPassword(rs.getString(3));
				list.add(vo);
			}
			}else {
				System.out.println("커넥션 없음");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose(conn, pstmt, rs);
		}
		return list;
	}
}
