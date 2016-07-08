package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utils.Database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javaBean.LatLngKit;

public class DbDao {

	// 保存用户MD5
	public String registerUser(String md5) {
		Database base = new Database();
		String uid = null;;
		try {
			 uid = getUid(base,md5);
			if (uid != null) {
				base.close();
				return uid;
			}
			PreparedStatement statement = base.PreparedStatement("insert into users (md5) values (?)");
			statement.setString(1, md5);
			statement.executeUpdate();
			uid = getUid(base,md5);
			base.close();
			return uid;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// 通过MD5获取UID
	private String getUid(Database base,String md5) {
		String uid = null;
		try {
			PreparedStatement statement = base.PreparedStatement("select * from users where md5 = ?");
			statement.setString(1, md5);
			ResultSet rs = base.ResultSet(statement);
			if (rs.next()) {
				uid = rs.getString("uid");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return uid;
	}

	// 保存用户路线
	public void saveJson(String json) {
		Gson gson = new Gson();
		List<LatLngKit> kits = gson.fromJson(json, new TypeToken<List<LatLngKit>>() {
		}.getType());
		Database base = new Database();
		for (LatLngKit kit : kits) {
			kit.toString();
			saveLatLng(base, kit);
		}
		base.close();
	}

	private void saveLatLng(Database base, LatLngKit kit) {
		System.out.println(kit.toString());
		try {
			PreparedStatement statement = base.PreparedStatement("insert into latlng (uid, lat, lng, date, time) values (?, ?, ?, ?, ?)");
			statement.setString(1, kit.getUid());
			statement.setString(2, kit.getLat());
			statement.setString(3, kit.getLng());
			statement.setString(4, kit.getDate());
			statement.setString(5, kit.getTime());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String HistoryDate(String uid) {
		Database base = new Database();
		String json = null;
		try {
			PreparedStatement statement = base.PreparedStatement("select distinct date from latlng WHERE uid =? order by date");
			statement.setString(1, uid);
			ResultSet rs = base.ResultSet(statement);
			List<LatLngKit> kits = new ArrayList<LatLngKit>();
			while (rs.next()) {
				String date = rs.getString("date");
				LatLngKit kit = new LatLngKit();
				kit.setDate(date);
				kits.add(kit);
			}
			Gson gson = new Gson();
			json = gson.toJson(kits);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		base.close();
		return json;
	}

	public String historyTrace(String uid, String date) {
		Database base = new Database();
		String json = null;
		try {
			//PreparedStatement statement = base.PreparedStatement("select distinct time from latlng WHERE uid = ? and date = ? order by time");
			PreparedStatement statement = base.PreparedStatement("SELECT *, COUNT(DISTINCT time) FROM latlng WHERE uid = ? and date = ? GROUP BY time");
			statement.setString(1, uid);
			statement.setString(2, date);

			ResultSet rs = base.ResultSet(statement);
			List<LatLngKit> kits = new ArrayList<LatLngKit>();
			while (rs.next()) {
				String rs_lat = rs.getString("lat");
				String rs_lng = rs.getString("lng");
				String rs_time = rs.getString("time");
				LatLngKit kit = new LatLngKit();
				kit.setLat(rs_lat);
				kit.setLng(rs_lng);
				kit.setTime(rs_time);
				kits.add(kit);
				System.out.println(kit.toString());
			}
			Gson gson = new Gson();
			json = gson.toJson(kits);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		base.close();
		return json;
	}

}
