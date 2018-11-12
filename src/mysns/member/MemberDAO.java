package mysns.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mysns.util.DBManager;

public class MemberDAO {
	Connection conn;
	PreparedStatement pStmt;
	BCrypt bc = new BCrypt();
	
	Logger logger = LoggerFactory.getLogger(MemberDAO.class);
	
	public ArrayList<String> getNewMembers(){
		ArrayList<String> nmembers = new ArrayList<String>();
		conn = DBManager.getConnection();
		String sql = "select * from s_member order by date desc limit 0,7";
		try {
			pStmt = conn.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();
			while(rs.next()) {
				nmembers.add(rs.getString("uid"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			logger.info("Error Code : {}",e.getErrorCode());
		}finally {
			try {
				pStmt.close();
				conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return nmembers;
	}
	
	public boolean addMember(Member member) {
		conn = DBManager.getConnection();
		String sql = "insert into s_member (uid,name,passwd,email) values (?,?,?,?)";
		try {
			pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, member.getUid());
			pStmt.setString(2, member.getName());
			pStmt.setString(3, bc.hashpw(member.getPasswd(),bc.gensalt(10)));
			pStmt.setString(4, member.getEmail());
			pStmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("Error Code : {}",e.getErrorCode());
			return false;
		} finally {
			try {
				if (pStmt != null && !pStmt.isClosed())
					pStmt.close();
			} catch (Exception se1) {
				se1.printStackTrace();
			}
		}
	}
	
	public boolean login(String id,String passwd) {
		conn = DBManager.getConnection();
		String sql="select passwd from s_member where uid=?";
		ResultSet rs = null;
		
		try {
			pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, id);
			
			rs = pStmt.executeQuery();
			rs.next();
			if(bc.checkpw(passwd, rs.getString(1)))
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("Error Code : {}",e.getErrorCode());
			return false;
		} finally {
			try {
				if (pStmt != null && !pStmt.isClosed())
					pStmt.close();
			} catch (Exception se1) {
				se1.printStackTrace();
				
			}
		}
		return false;
	}
	public void close() {
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (Exception se2) {
		}
	}
}
