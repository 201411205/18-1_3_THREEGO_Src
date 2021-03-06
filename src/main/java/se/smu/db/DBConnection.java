package se.smu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
	// 기본 변수 선언
	private Connection conn = null;
	private Statement st = null;
	private ResultSet rs = null;
	private Logger lgr = null;

	// 기본 생성자
	public DBConnection() {
		lgr = Logger.getLogger(DBConnection.class.getName());
		// 데이터베이스 주소 및 아이디, 비밀번호 설정
		String url = "jdbc:postgresql://se-project.ci1zyasoxkzt.ap-northeast-2.rds.amazonaws.com/se_project";
		String user = "se_project";
		String password = "sethreego";

		try {
			// 데이터 베이스와 연결
			this.conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 로그인 성공 여부 검사
	public boolean LogIn(String id, String pwd) {
		String sql = "select * from user_tbl where id = '" + id + "' and pwd = '" + pwd + "'";
		boolean result = false;
		rs = selectSQL(sql);
		try {
			if (rs.next()) {
				result = true;
				lgr.log(Level.INFO, "Success - Log In");
			}
		} catch (Exception e) {
			lgr.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			closeRSST();
		}
		return result;
	}

	/* 과목 */

	// 과목 데이터 추가
	public boolean setSubject(String id, Object[] row) {
		boolean result = false;
		String sql = "insert into subject_tbl values ('" + id + "', '" + row[0] + "', '" + row[1] + "', '" + row[2]
				+ "', '" + row[3] + "', '" + row[4] + "')";

		updateSQL(sql);
		lgr.log(Level.INFO, "Success - Insert Subject Data");
		result = true;
		return result;
	}

	// 과목 데이터 불러오기
	public Vector<Vector<Object>> getSubject(String id) {
		Vector<Vector<Object>> result = new Vector<Vector<Object>>();
		String sql = "select * from subject_tbl where id = '" + id + "'";
		rs = selectSQL(sql);
		try {
			while (rs.next()) {
				Vector<Object> row = new Vector<Object>();
				row.add(rs.getString("yns"));
				row.add(rs.getString("name"));
				row.add(rs.getString("day"));
				row.add(rs.getString("time"));
				row.add(rs.getString("professor"));
				result.add(row);
			}
		} catch (SQLException e) {
			lgr.log(Level.SEVERE, e.getMessage(), e);
		}
		return result;
	}

	// 과목 데이터 업데이트
	public boolean updateSubject(String id, Object[] prev, Object[] after) {
		boolean result = false;
		String sql = "update subject_tbl set yns='" + after[0] + "', name='" + after[1] + "', day='" + after[2]
				+ "', time='" + after[3] + "', professor='" + after[4] + "' where id='" + id + "' and yns='" + prev[0]
				+ "' and name='" + prev[1] + "' and day='" + prev[2] + "' and time='" + prev[3] + "' and professor='"
				+ prev[4] + "'";
		updateSQL(sql);
		lgr.log(Level.INFO, "Success - Update Subject Data");
		result = true;
		return result;
	}

	// 과목 데이터 삭제
	public boolean deleteSubject(String id, Object[] row) {
		boolean result = false;
		String sql = "delete from subject_tbl where id='" + id + "' and yns='" + row[0] + "' and name='" + row[1]
				+ "' and day='" + row[2] + "' and time='" + row[3] + "' and professor='" + row[4] + "'";
		updateSQL(sql);
		lgr.log(Level.INFO, "Success - Delete Subject Data");
		result = true;
		return result;
	}

	/* TO DO LIST */

	// Todo 데이터 추가
	public boolean setTodo(String id, Vector<Object> row) {
		boolean result = false;
		String sql = "insert into todo_tbl values ('" + id + "', " + row.get(0) + ", '" + row.get(1) + "', '"
				+ row.get(2) + "', '" + row.get(3) + "', '" + row.get(4) + "', '" + row.get(5) + "', '" + row.get(6)
				+ "')";

		updateSQL(sql);
		lgr.log(Level.INFO, "Success - Insert Todo Data");
		result = true;
		return result;
	}

	// Todo 데이터 불러오기
	public Vector<Vector<Object>> getTodo(String id) {
		Vector<Vector<Object>> result = new Vector<Vector<Object>>();
		String sql = "select * from todo_tbl where id = '" + id + "'";
		rs = selectSQL(sql);
		try {
			while (rs.next()) {
				Vector<Object> row = new Vector<Object>();
				row.add(rs.getInt("importance"));
				row.add(rs.getString("name"));
				row.add(rs.getString("deadline"));
				row.add(rs.getString("rdeadline"));
				row.add(rs.getString("state"));
				row.add(rs.getString("wtd"));
				row.add(rs.getString("memo"));
				result.add(row);
			}
		} catch (SQLException e) {
			lgr.log(Level.SEVERE, e.getMessage(), e);
		}
		return result;
	}

	// Todo 데이터 업데이트
	public boolean updateTodo(String id, Vector<Object> prev, Vector<Object> after) {
		boolean result = false;
		String sql = "update todo_tbl set importance=" + after.get(0) + ", name='" + after.get(1) + "', deadline='"
				+ after.get(2) + "', rdeadline='" + after.get(3) + "', state='" + after.get(4) + "', wtd='"
				+ after.get(5) + "', memo='" + after.get(6) + "' where id='" + id + "' and " + "importance="
				+ prev.get(0) + " and name='" + prev.get(1) + "' and deadline='" + prev.get(2) + "' and rdeadline='"
				+ prev.get(3) + "' and state='" + prev.get(4) + "' and wtd='" + prev.get(5) + "' and memo='"
				+ prev.get(6) + "'";
		updateSQL(sql);
		lgr.log(Level.INFO, "Success - Update Todo Data");
		result = true;
		return result;
	}

	// Todo 데이터 삭제
	public boolean deleteTodo(String id, Vector<Object> row) {
		boolean result = false;
		String sql = "delete from todo_tbl where id='" + id + "' and importance=" + row.get(0) + " and name='"
				+ row.get(1) + "' and deadline='" + row.get(2) + "' and rdeadline='" + row.get(3) + "' and state='"
				+ row.get(4) + "' and wtd='" + row.get(5) + "' and memo='" + row.get(6) + "'";
		updateSQL(sql);
		lgr.log(Level.INFO, "Success - Delete Todo Data");
		result = true;
		return result;
	}

	/* private function */

	// select SQL 문장일 경우 resultset 반환
	private ResultSet selectSQL(String sql) {
		st = null;
		rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
		} catch (SQLException e) {
			lgr.log(Level.SEVERE, e.getMessage(), e);
		}
		return rs;
	}

	// update 문장일 경우 실행만 하고 반환 값 없음
	private void updateSQL(String sql) {
		st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			lgr.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	// resultset과 statement 변수 close;
	private void closeRSST() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
		} catch (SQLException e) {
			lgr.log(Level.WARNING, e.getMessage(), e);
		}
	}

	// 데이터베이스 연결 종료 시 close
	public void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			lgr.log(Level.WARNING, e.getMessage(), e);
		}
	}

}
