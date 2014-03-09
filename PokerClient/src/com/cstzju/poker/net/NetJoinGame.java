package com.cstzju.poker.net;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cstzju.poker.utils.Common;
import com.cstzju.poker.utils.Converter;

public class NetJoinGame extends NetMessage {
	public NetJoinGame() {
		time = Common.getTime();
		cmd = JOIN_GAME;
	}

	public void setData(int name, int IP) throws JSONException,
			UnsupportedEncodingException {
		// JSON Data Example: {"name": "玩家1","IP":"192.168.0.20"}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", name).put("IP", IP);
		len = jsonObject.toString().length();
		data = new byte[len];
		data = jsonObject.toString().getBytes("UTF-8");
	}

	public int getIntName() throws NumberFormatException, JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data));
		return Integer.parseInt(jsonObject.getString("name"));
	}

	public int getIntIP() throws NumberFormatException, JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data));
		return Integer.parseInt(jsonObject.getString("IP"));
	}
}
