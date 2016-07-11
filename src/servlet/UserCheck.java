package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;

import dao.DbDao;


public class UserCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String openID = request.getParameter("openID");
		String md5 = request.getParameter("md5");
		
		User user = new User();
		user.setOpenID(openID);
		user.setMd5(md5);
		
		boolean rs = new DbDao().userCheck(user);
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