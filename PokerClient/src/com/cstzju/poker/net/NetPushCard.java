package com.cstzju.poker.net;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.cstzju.poker.utils.Common;
import com.cstzju.poker.utils.Converter;

public class NetPushCard extends NetMessage {
	public NetPushCard() {
		this.time = Common.getTime();
		this.cmd = PUSH_CARD;
	}

	public void setData(int[] pokers) throws JSONException,
			UnsupportedEncodingException {
		JSONObject jsonObject = new JSONObject();
		JSONArray cards = new JSONArray();
		int count = pokers.length;

		jsonObject.put("count", count);
		for (int i = 0; i < count; i++)
			cards.put(pokers[i]);
		jsonObject.put("cards", cards);

		this.len = jsonObject.toString().length();
		Log.i("test", "len:" + len + "-json:" + jsonObject.toString());
		this.data = new byte[len];
		this.data = jsonObject.toString().getBytes("UTF-8");
	}

	public int getCount() throws JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data));
		return Integer.parseInt(jsonObject.getString("count"));
	}

	public int[] getCardNumberArray() throws JSONException {
		int count = this.getCount();
		int[] pokers = new int[count];
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data));
		JSONArray cards = jsonObject.getJSONArray("cards");

		for (int i = 0; i < count; i++) {
			pokers[i] = cards.getInt(i);
		}
		return pokers;

	}

}
