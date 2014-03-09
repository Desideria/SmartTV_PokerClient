package com.cstzju.poker.utils;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

public class Converter {

	public static int UnsignedByte(byte value) {
		return value & 0xFF;
	}

	public static int LowByte(int value) {
		return value & 0xFF;
	}

	public static int MiddleByte(int value) {
		return (value >> 8) & 0xFF;
	}

	public static int HighByte(int value) {
		return (value >> 16) & 0xFF;
	}

	public static byte[] Int16ToBytes(int value) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (value & 0xFF);
		bytes[1] = (byte) ((value >> 8) & 0xFF);
		return bytes;
	}

	public static void Int16ToBytes(int value, byte[] bytes, int offset) {
		bytes[offset] = (byte) (value & 0xFF);
		bytes[offset + 1] = (byte) ((value >> 8) & 0xFF);
	}

	public static byte[] Int32ToBytes(int value) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (value & 0xFF);
		bytes[1] = (byte) ((value >> 8) & 0xFF);
		bytes[2] = (byte) ((value >> 16) & 0xFF);
		bytes[3] = (byte) ((value >> 24) & 0xFF);
		return bytes;
	}

	public static void Int32ToBytes(int value, byte[] bytes, int offset) {
		bytes[offset] = (byte) (value & 0xFF);
		bytes[offset + 1] = (byte) ((value >> 8) & 0xFF);
		bytes[offset + 2] = (byte) ((value >> 16) & 0xFF);
		bytes[offset + 3] = (byte) ((value >> 24) & 0xFF);
	}

	public static byte[] TimeToBytes(int value) {
		byte[] bytes = new byte[3];
		bytes[0] = (byte) (value & 0xFF);
		bytes[1] = (byte) ((value >> 8) & 0xFF);
		bytes[2] = (byte) ((value >> 16) & 0xFF);
		return bytes;
	}

	public static int BytesToTime(byte[] bytes, int offset) {
		return UnsignedByte(bytes[offset])
				+ (UnsignedByte(bytes[offset + 1]) << 8)
				+ (UnsignedByte(bytes[offset + 2]) << 16);
	}

	public static int BytesToTime(int lowByte, int thirdHighByte, int highByte) {
		return lowByte + (thirdHighByte << 8) + (highByte << 16);
	}

	public static int BytesToInt16(int lowByte, int highByte) {
		return lowByte + (highByte << 8);
	}

	public static int BytesToInt16(byte[] bytes, int offset) {
		return BytesToInt16(UnsignedByte(bytes[offset]),
				UnsignedByte(bytes[offset + 1]));
	}

	public static int BytesToInt32(byte[] bytes, int offset) {
		return UnsignedByte(bytes[offset])
				+ (UnsignedByte(bytes[offset + 1]) << 8)
				+ (UnsignedByte(bytes[offset + 2]) << 16)
				+ (UnsignedByte(bytes[offset + 3]) << 24);
	}

	public static int BytesToInt32(int lowByte, int thirdHighByte,
			int secondHighByte, int highByte) {
		return lowByte + (thirdHighByte << 8) + (secondHighByte << 16)
				+ (highByte << 24);
	}

	public static void StringToBytes(String value, byte[] bytes, int offset) {
		try {
			byte[] buffer = value.getBytes("UTF-8");
			// System.out.println("value:" + value + "\nbuffer:"
			// + buffer.toString());
			for (int i = 0; i < buffer.length; i++) {
				bytes[offset + i] = buffer[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String BytesToString(byte[] bytes, int offset, int length) {
		String str = "";
		try {
			int stringLength = 0;
			for (int i = 0; i < length; i++) {
				if (bytes[offset + i] == 0) {
					stringLength = i;
					break;
				}
			}
			str = new String(bytes, offset, stringLength, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String BytesToString(byte[] bytes) {
		String str = "";
		str = new String(bytes);
		return str;
	}

	// public static String BytesToString(byte[] bytes) {
	// char str[] = new char[bytes.length * 2];
	// for (int i = 0; i < bytes.length; i++) {
	// str[i * 2] = "0123456789ABCDEF".charAt((bytes[i] >> 4) & 15);
	// str[i * 2 + 1] = "0123456789ABCDEF".charAt(bytes[i] & 15);
	// }
	// return new String(str);
	// }

	public static void JSONStringToBytes(JSONObject jsonObject) {
		// try {
		// byte[] buffer = value.getBytes("UTF-8");
		// System.out.println("buffer.length"+buffer.length);
		// for (int i = 0; i < buffer.length; i++) {
		// System.out.println("i"+i);
		// bytes[offset + i] = buffer[i];
		// }
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
	}
}
