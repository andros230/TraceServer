package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.LatLngKit;

import dao.DbDao;

public class RealTimeSaveLatLng extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		
		String uid = request.getParameter("uid");
		String lat = request.getParameter("lat");
		String lng = request.getParameter("lng");
		String date = request.getParameter("date");
		String time = request.getParameter("time");
		
		LatLngKit kit = new LatLngKit();
		kit.setUid(uid);
		kit.setLat(lat);
		kit.setLng(lng);
		kit.setDate(date);
		kit.setTime(time);
		
		boolean rs = new DbDao().RealTimeSaveLatLng(kit);
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