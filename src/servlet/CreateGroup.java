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

public class CreateGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String groupName = request.getParameter("groupName");
		String uid = request.getParameter("uid");
		String password = request.getParameter("password");
		String introdu = request.getParameter("introdu");

		String rs = createGroup(groupName, uid, password, introdu);
		PrintWriter writer = response.getWriter();
		writer.write(rs);
		writer.flush();
		writer.close();
	}

	// 创建群
	private String createGroup(String groupName, String uid, String password, String introdu) {
		Database base = new Database();
		try {
			if (!checkGroupName(base, groupName)) {
				PreparedStatement statement = base.PreparedStatement("insert into group_id (group_name, uid, create_time, password, introdu) values (?,?,?,?,?)");
				statement.setString(1, groupName);
				statement.setString(2, uid);
				statement.setString(3, util.nowTime());
				statement.setString(4, password);
				statement.setString(5, introdu);
				int a = statement.executeUpdate();
				if (a == 1) {
					if (joinGroup(base, groupName, uid)) {
						base.close();
						return "YES";
					}else {
						base.close();
						return "创建群异常";
					}
				} else {
					base.close();
					return "创建群异常";
				}

			} else {
				base.close();
				return "群名已存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		base.close();
		return "服务器异常";
	}

	private boolean checkGroupName(Database base, String groupName) throws SQLException {
		PreparedStatement statement = base.PreparedStatement("select * from group_id where group_name=?");
		statement.setString(1, groupName);
		ResultSet rs = base.ResultSet(statement);
		if (rs.next()) {
			return true;
		}
		return false;
	}

	private boolean joinGroup(Database base, String groupName, String uid) throws SQLException {
		PreparedStatement statement = base.PreparedStatement("select * from group_id where group_name=?");
		statement.setString(1, groupName);
		ResultSet rs = base.ResultSet(statement);
		if (rs.next()) {
			String rs_group_id = rs.getString("id");
			return new JoinGroup().insertUID(base, uid, rs_group_id);
		}
		return false;

	}

}
