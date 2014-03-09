package com.cstzju.poker.net;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cstzju.poker.utils.Common;
import com.cstzju.poker.utils.Converter;

public class NetCalledDZ extends NetMessage {
	public NetCalledDZ() {
		time = Common.getTime();
		cmd = CALLED_DZ;
	}

	/**
	 * 
	 * @param result
	 *            要叫地主则赋值为1，否则为0.
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public void setData(int result) throws JSONException,
			UnsupportedEncodingException {
		// JSON Data Example: {"DZ":1}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("DZ", result);
		len = jsonObject.toString().length();
		data = new byte[len];
		data = jsonObject.toString().getBytes("UTF-8");
	}

	public int getIntResult() throws NumberFormatException, JSONException {
		JSONObject jsonObject = new JSONObject(Converter.BytesToString(data));
		return Integer.parseInt(jsonObject.getString("DZ"));
	}

}
