package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Database;
import utils.util;

public class JoinGroup extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String uid = request.getParameter("uid");
		String groupID = request.getParameter("groupID");
		String password = request.getParameter("password");

		String rs = joinGroup(uid, groupID, password);
		PrintWriter writer = response.getWriter();
		writer.write(rs);
		writer.flush();
		writer.close();
	}

	// 加入群
	private String joinGroup(String uid, String groupID, String password) {
		Database base = new Database();
		try {
			if (!checkUidInGroup(base, uid, groupID)) {
				PreparedStatement statement = base.PreparedStatement("select * from group_id where id=?");
				statement.setString(1, groupID);
				ResultSet rs = base.ResultSet(statement);
				if (rs.next()) {
					String rs_password = rs.getString("password");
					if (rs_password.equals(password)) {
						if (insertUID(base, uid, groupID)) {
							base.close();
							return "YES";
						} else {
							base.close();
							return "加入群异常";
						}
					} else {
						base.close();
						return "密码有误";
					}
				} else {
					base.close();
					return "无此群,请检查群号是否正确";
				}
			} else {
				base.close();
				return "您已加入该群";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		base.close();
		return "服务器异常";
	}

	private boolean checkUidInGroup(Database base, String uid, String groupID) throws SQLException {
		PreparedStatement statement = base.PreparedStatement("select * from group1 where uid=? and group_id=?");
		statement.setString(1, uid);
		statement.setString(2, groupID);
		ResultSet rs = base.ResultSet(statement);
		if (rs.next()) {
			return true;
		}
		return false;
	}

	public boolean insertUID(Database base, String uid, String groupID) throws SQLException {
		PreparedStatement statement = base.PreparedStatement("insert into group1 (uid, group_id, lat, lng, acc, date, time) values (?,?,?,?,?,?,?)");
		statement.setString(1, uid);
		statement.setString(2, groupID);
		statement.setString(3, "0");
		statement.setString(4, "0");
		statement.setString(5, "0");
		statement.setString(6, util.getDate());
		statement.setString(7, util.getTime());
		int rs = statement.executeUpdate();
		System.out.println(rs);
		if (rs == 1) {
			return true;
		}
		return false;
	}

}
