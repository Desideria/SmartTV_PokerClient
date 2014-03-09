package com.cstzju.poker.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import com.cstzju.poker.utils.Converter;

public class NetMessage {
	// 消息字段
	protected int time; // 3byte
	protected int cmd; // 1byte
	protected int len; // 1byte
	protected byte[] data;
	protected int check; // 1byte

	// 消息头常量定义
	public static final int HEAD = 0x55;

	// 消息命令定义 
	public static final int JOIN_GAME = 0x50;
	public static final int CALLED_DZ = 0x50;
	public static final int PUSH_CARD = 0x50;
	public static final int GAME_RESULT = 0x50;
//	public static final int HAVE_GROUP = 0x50;
//	public static final int HAVE_GROUP_RESULT = 0x51;
//	public static final int PLAY_GAME = 0x52;
//
//	public static final int GAME_ALREADY = 0x53;
//	public static final int GAME_START = 0x54;
//	public static final int GAME_PLAYING = 0x56;
//	public static final int REGUEST_CARD = 0x57;
//	public static final int PUSH_CARD = 0x58;
//	public static final int CARD_RESULT = 0x59;
//	public static final int GAME_RESULT = 0x61;

	// 心跳包命令
	public static final int KEEP_ALIVE = 0x62;

	/**
	 * 通讯协议BASE类
	 */
	public NetMessage() {

	}

	/**
	 * 从流中读取数据
	 * 
	 * @param stream
	 * @return 是否读取成功。数据帧HEAD不对或者校验码不对则返回false。
	 * @throws IOException
	 */
	public boolean fromStream(InputStream stream) throws IOException {
		int head = stream.read();
		// Log.i("test", "head:" + head);
		if (head != HEAD) {
			return false;
		}
		time = Converter.BytesToTime(stream.read(), stream.read(),
				stream.read());
		// Log.i("test", "time:" + time);
		cmd = stream.read();
		// Log.i("test", "cmd:" + cmd);
		len = stream.read();
		// Log.i("test", "len:" + len);
		data = new byte[len];
		int alreadyRead = 0;
		int currentRead = 0;
		while (len - alreadyRead > 0) {
			// Log.i("test", "len:" + len + "=alreadyRead:" + alreadyRead
			// + "=currentRead:" + currentRead + "=data:" + data);
			currentRead = stream.read(data, alreadyRead, len - alreadyRead);
			alreadyRead += currentRead;
		}
		Log.i("test", "data:" + Converter.BytesToString(data));
		int check = stream.read();
		// Log.i("test", "check:" + check);
		if (check != computeVerify()) {
			return false;
		}

		return true;
	}

	/**
	 * 把数据写入流中
	 * 
	 * @param stream
	 * @throws IOException
	 */
	public void toStream(OutputStream stream) throws IOException {
		// Log.i("test", "HEAD:" + HEAD);
		stream.write(HEAD);
		// Log.i("test", time + "");
		stream.write(Converter.TimeToBytes(time));
		// Log.i("test", "cmd:" + cmd);
		stream.write(cmd);
		// Log.i("test", "len:" + len);
		stream.write(len);
		if (len > 0) {
			Log.i("test", "data:" + Converter.BytesToString(data));
			stream.write(data);
		}
		stream.write(computeVerify());
		// Log.i("test", "check:" + computeVerify());
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	// public byte[] getStream(int lengh) throws IOException {
	// byte[] temp = new byte[lengh];
	// temp[0] = HEAD;
	// temp[1] = (byte) Converter.LowByte(time);
	// temp[2] = (byte) Converter.MiddleByte(time);
	// temp[3] = (byte) Converter.HighByte(time);
	// temp[4] = (byte) cmd;
	// temp[5] = (byte) len;
	// int i = 6;
	// for (; i < lengh - 1; i++) {
	// temp[i] = (byte) data[i - 6];
	// }
	// temp[lengh - 1] = (byte) computeVerify();
	// return temp;
	// }

	public int getTime() {
		return time;
	}

	public int getCmd() {
		return cmd;
	}

	public int getLength() {
		return len;
	}

	public String getData() {
		return Converter.BytesToString(data);
	}

	
	private int computeVerify() {
		int verify = 0;
		verify ^= HEAD;
		verify ^= Converter.LowByte(time);
		verify ^= Converter.MiddleByte(time);
		verify ^= Converter.HighByte(time);
		verify ^= cmd;
		verify ^= len;
		for (int i = 0; i < len; i++) {
			verify ^= Converter.UnsignedByte(data[i]);
		}
		return verify;
	}
}