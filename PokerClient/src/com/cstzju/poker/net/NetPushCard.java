package com.cstzju.poker.net;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cstzju.poker.utils.Common;
import com.cstzju.poker.utils.Converter;

public class NetPushCard extends NetMessage {
	public NetPushCard() {
		time = Common.getTime();
		cmd = PUSH_CARD;
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

		len = jsonObject.toString().length();
		data = new byte[len];
		data = jsonObject.toString().getBytes("UTF-8");
	}

	public int getCount() throws JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data, 0,
				len));
		return Integer.parseInt(jsonObject.getString("count"));

	}

	public int[] getCardNumberArray() throws JSONException {
		int count = this.getCount();
		int[] pokers = new int[count];

		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data, 0,
				len));
		JSONArray cards = jsonObject.getJSONArray("cards");
		for (int i = 0; i < count; i++) {
			pokers[i] = cards.getInt(i);
		}
		return pokers;

	}
}
