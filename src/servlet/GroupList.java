package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Database;
import bean.Group;

import com.google.gson.Gson;

public class GroupList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String uid = request.getParameter("uid");

		String rs = groupList(uid);
		PrintWriter writer = response.getWriter();
		writer.write(rs);
		writer.flush();
		writer.close();
	}

	// ÈºÁÐ±í
	private String groupList(String uid) {
		Database base = new Database();
		try {
			PreparedStatement statement = base.PreparedStatement("select * from group1 where uid=?");
			statement.setString(1, uid);
			ResultSet rs = base.ResultSet(statement);
			List<Group> list = new ArrayList<Group>();
			while (rs.next()) {
				String rs_groupID = rs.getString("group_id");
				Group group = new Group();
				group.setGroupID(rs_groupID);
				list.add(group);
			}
			base.close();
			Gson gson = new Gson();
			return gson.toJson(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		base.close();
		return null;
	}
}
