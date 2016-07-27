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

import bean.User;

public class SaveOpenID extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String md5 = request.getParameter("md5");
		String openID = request.getParameter("openID");

		User user = new User();
		user.setMd5(md5);
		user.setOpenID(openID);

		String uid = saveOpenID(user);
		System.out.println(uid);
		PrintWriter writer = response.getWriter();
		writer.write(uid);
		writer.flush();
		writer.close();

	}

	// Ôö¼ÓopenID
	private String saveOpenID(User user) {
		Database base = new Database();
		try {

			String uid = queryOpenID(base, user.getOpenID());
			if (uid != null) {
				PreparedStatement statement = base.PreparedStatement("update users set md5=? where openid=?");
				statement.setString(1, user.getMd5());
				statement.setString(2, user.getOpenID());
				statement.executeUpdate();
				base.close();
				return uid;
			} else {
				PreparedStatement statement = base.PreparedStatement("insert into users (md5, openid, create_time) values (?,?,?)");
				statement.setString(1, user.getMd5());
				statement.setString(2, user.getOpenID());
				statement.setString(3, util.nowTime());
				statement.executeUpdate();

				String rs_uid = queryOpenID(base, user.getOpenID());
				base.close();
				return rs_uid;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		base.close();
		return null;
	}

	private String queryOpenID(Database base, String openID) throws SQLException {
		String uid = null;
		PreparedStatement statement = base.PreparedStatement("select * from users where openid = ?");
		statement.setString(1, openID);
		ResultSet rs = base.ResultSet(statement);
		if (rs.next()) {
			uid = rs.getString("uid");
		}
		return uid;
	}
}
