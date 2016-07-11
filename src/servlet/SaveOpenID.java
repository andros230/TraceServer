package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DbDao;

public class SaveOpenID extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String uid = request.getParameter("uid");
		String md5 = request.getParameter("md5");
		String openID = request.getParameter("openID");
		
		String rs = new DbDao().saveOpenID(uid, openID, md5);
		
		PrintWriter writer = response.getWriter();
		writer.write(rs);
		writer.flush();
		writer.close();

	}

}
