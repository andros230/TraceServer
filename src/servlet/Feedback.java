package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DbDao;

public class Feedback extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String uid = request.getParameter("uid");
		String content = request.getParameter("content");
		
		boolean rs = new DbDao().feedback(uid, content);
		PrintWriter writer = response.getWriter();
		if (rs) {
			writer.write("YES");
		}else {
			writer.write("NO");
		}
		
		writer.flush();
		writer.close();
		
	}
}