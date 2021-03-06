package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Database;
import utils.util;


public class Feedback extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String uid = request.getParameter("uid");
		String content = request.getParameter("content");

		boolean rs = feedback(uid, content);
		PrintWriter writer = response.getWriter();
		if (rs) {
			writer.write("YES");
		} else {
			writer.write("NO");
		}

		writer.flush();
		writer.close();

	}

	// �������
	private boolean feedback(String uid, String content) {
		Database base = new Database();
		try {
			PreparedStatement statement = base.PreparedStatement("insert into feedback (uid, content, time) values (?, ?, ?)");
			statement.setString(1, uid);
			statement.setString(2, content);
			statement.setString(3, util.nowTime());
			statement.executeUpdate();
			base.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		base.close();
		return false;
	}
}