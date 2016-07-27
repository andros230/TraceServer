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

import bean.User;


public class UserCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String uid = request.getParameter("uid");
		String md5 = request.getParameter("md5");
		System.out.println(md5);
		
		User user = new User();
		user.setUid(uid);
		user.setMd5(md5);
		
		boolean rs = userCheck(user);
		PrintWriter writer = response.getWriter();
		if (rs) {
			writer.write("YES");
		}else {
			writer.write("NO");
		}
		writer.flush();
		writer.close();
	}
	

	// 打开APP时检查帐号是否正常
	private boolean userCheck(User user) {
		Database base = new Database();
		boolean bool = false;
		try {
			PreparedStatement statement = base.PreparedStatement("select * from users where uid = ?");
			statement.setString(1, user.getUid());
			ResultSet rs = base.ResultSet(statement);
			if (rs.next()) {
				String rs_md5 = rs.getString("md5");
				if (rs_md5.equals(user.getMd5())) {
					bool = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		base.close();
		return bool;
	}
}