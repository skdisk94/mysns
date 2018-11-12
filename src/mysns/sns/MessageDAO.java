package mysns.sns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mysns.member.MemberDAO;
import mysns.util.DBManager;

public class MessageDAO {
	Connection conn = null;

	public boolean newMsg(Message msg) {
		conn = DBManager.getConnection();
		String sql = "insert into s_message(uid, msg) values (?,?)";
		PreparedStatement pStmt = null;

		Logger logger = LoggerFactory.getLogger(MemberDAO.class);
		
		try {
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, msg.getUid());
			pStmt.setString(2, msg.getMsg());
			pStmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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

	public boolean delMsg(int mid) {
		conn = DBManager.getConnection();
		String sql = "delete from s_message where mid=?";
		PreparedStatement pStmt = null;
		try {
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, mid);
			pStmt.executeUpdate();
		} catch (SQLException  e) {
			e.printStackTrace();
			System.out.println(e.getErrorCode());
			return false;
		} finally {
			try {
				if (pStmt != null && !pStmt.isClosed())
					pStmt.close();
			} catch (Exception se1) {
				se1.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean newReply(Reply reply) {
		conn = DBManager.getConnection();
		String sql = "insert into s_reply (mid, uid, rmsg) values(?,?,?)";
		PreparedStatement pStmt = null;

		try {
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1,reply.getMid());
			pStmt.setString(2, reply.getUid());
			pStmt.setString(3, reply.getRmsg());
			pStmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public boolean delReply(int rid) {
		conn = DBManager.getConnection();
		String sql = "delete from s_reply where rid=?";
		PreparedStatement pStmt = null;
		try {
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, rid);
			pStmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pStmt != null && !pStmt.isClosed())
					pStmt.close();
			} catch (Exception se1) {
				se1.printStackTrace();
			}
		}
		return true;
	}
	
	public ArrayList<MessageSet> getAll(int cnt, String suid){
		conn = DBManager.getConnection();
		ArrayList<MessageSet> datas = new ArrayList<MessageSet>();
		String sql=null;
		PreparedStatement pStmt =null;
		try {
			if((suid==null) ||(suid.equals(""))) {
				sql = "select * from s_message order by date desc limit 0,?";
				pStmt = conn.prepareStatement(sql);
				pStmt.setInt(1, cnt);
			}
			else {
				sql="select * from s_message where uid=? order by date desc limit 0,?";
				pStmt = conn.prepareStatement(sql);
				pStmt.setString(1, suid);
				pStmt.setInt(2, cnt);
			}
			
			ResultSet rs = pStmt.executeQuery();
			while(rs.next()) {
				MessageSet ms = new MessageSet();
				Message m = new Message();
				ArrayList<Reply> rlist = new ArrayList<Reply>();
				m.setMid(rs.getInt(1));
				m.setUid(rs.getString(2));
				m.setMsg(rs.getString(3));
				m.setDate(rs.getDate("date")+"/"+rs.getTime("date"));
				m.setFavcount(rs.getInt(4));
				m.setReply(rs.getInt(5));
				
				String rsql = "select * from s_reply where mid=? order by date desc";
				pStmt = conn.prepareStatement(rsql);
				pStmt.setInt(1, rs.getInt("mid"));
				ResultSet rrs = pStmt.executeQuery();
				while(rrs.next()) {
					Reply r = new Reply();
					r.setRid(rrs.getInt(1));
					r.setUid(rrs.getString(3));
					r.setDate(rrs.getDate("date")+"/"+rrs.getTime("date"));
					r.setRmsg(rrs.getString(5));
					rlist.add(r);
				}
				rrs.last();
				m.setReply(rrs.getRow());
				
				ms.setMessage(m);
				ms.setRlist(rlist);
				datas.add(ms);
				rrs.close();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (pStmt != null && !pStmt.isClosed())
					pStmt.close();
			} catch (Exception se1) {
				se1.printStackTrace();
			}
		}
		return datas;
	}
	
	public void favorite(int mid) {
		conn = DBManager.getConnection();
		String sql = "update s_message set favcount=favcount+1 where mid=?";
		PreparedStatement pStmt = null;
		try {
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, mid);
			pStmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pStmt != null && !pStmt.isClosed())
					pStmt.close();
			} catch (Exception se1) {
				se1.printStackTrace();
			}
		}
	}
	public void close() {
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (Exception se2) {
		}
	}
}
