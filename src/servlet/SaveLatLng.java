package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Database;
import utils.util;

import com.google.gson.Gson;

import bean.LatLngKit;

public class SaveLatLng extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String uid = request.getParameter("uid");
		String groupID = request.getParameter("groupID");
		String lat = request.getParameter("lat");
		String lng = request.getParameter("lng");
		String acc = request.getParameter("acc");

		LatLngKit kit = new LatLngKit();
		kit.setUid(uid);
		kit.setGroupID(groupID);
		kit.setLat(lat);
		kit.setLng(lng);
		kit.setAcc(acc);

		String json = saveLatLng(kit);

		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
		writer.close();

	}

	private String saveLatLng(LatLngKit kit) {
		Database base = new Database();
		try {
			PreparedStatement statement = base.PreparedStatement("update group1 set lat=?, lng=?, acc=?, date=?, time=? where uid=? and group_id=?");
			statement.setString(1, kit.getLat());
			statement.setString(2, kit.getLng());
			statement.setString(3, kit.getAcc());
			statement.setString(4, util.getDate());
			statement.setString(5, util.getTime());
			statement.setString(6, kit.getUid());
			statement.setString(7, kit.getGroupID());
			statement.executeUpdate();
			String json = groupLocation(base, kit.getUid(), kit.getGroupID());
			if (json != null) {
				base.close();
				return json;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		base.close();
		return null;
	}

	private String groupLocation(Database base, String uid, String groupID) {
		try {
			PreparedStatement statement = base.PreparedStatement("select * from group1 where date=? and group_id=? and uid !=?");
			statement.setString(1, util.getDate());
			statement.setString(2, groupID);
			statement.setString(3, uid);
			ResultSet rs = base.ResultSet(statement);
			List<LatLngKit> kits = new ArrayList<LatLngKit>();
			while (rs.next()) {
				String time = rs.getString("time");
				if (compareTime(time)) {
					LatLngKit kit = new LatLngKit();
					kit.setUid(rs.getString("uid"));
					kit.setLat(rs.getString("lat"));
					kit.setLng(rs.getString("lng"));
					kit.setAcc(rs.getString("acc"));
					kits.add(kit);
				}

			}
			Gson gson = new Gson();
			String json = gson.toJson(kits);
			System.out.println(json);
			return json;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean compareTime(String time) throws ParseException {
		int s = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date d1 = sdf.parse(util.getTime());
		Date d2 = sdf.parse(time);
		long l = d1.getTime() - d2.getTime();
		s = (int) (l / 1000); // Ãë
		if (s > 20) {
			return false;
		} else {
			return true;
		}
	}
}