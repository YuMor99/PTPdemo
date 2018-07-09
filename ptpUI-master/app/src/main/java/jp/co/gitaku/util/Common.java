package jp.co.gitaku.util;

//import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Common {
	public static int getSizeOfString(String value) {
		if (value == null) {
			return 0;
		} else {
			return 2 * value.length();
		}
	}

	public static byte[] stream2byte(InputStream in) throws IOException {
		byte[] len = readStream(in, 4);
//		DataInputStream data = new DataInputStream(in);
//		data.readFully(len, 0, 4);
		
		int length = bytesToInt32(len, 0);
		byte[] content = readStream(in, length - 4);
//		System.arraycopy(len, 0, allData, 0, 4);
//		data.readFully(allData, 4, length - 4);
		
		byte[] allData = new byte[length];
		System.arraycopy(len, 0, allData, 0, 4);
		System.arraycopy(content, 0, allData, 4, content.length);
		return allData;
	}
	
	private static byte[] readStream(InputStream in, int len) throws IOException{
		byte[] ret = new byte[len];
		int remain = len;
//		while (in.available() > 0 && remain > 0) {
		while (remain > 0) {
			if (in.available() > 0) {
				remain -= in.read(ret, len-remain, remain);
			}
		}
		if (remain >0) throw new IOException("Unexpacted EOF!" + remain);
		return ret;
	}

	public static int bytesToInt32(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = i * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}
	
	public static int bytesToInt16(byte[] b, int offset) {
		int value = 0;
		value += (b[offset + 1] & 0xFF) << 8;
		value += b[offset];
		
		return value;
	}
	
	public static int[] getGuid(byte[] b, int offset) {
		int[] guid = new int[16];
		for (int i = 0; i < 16; i++) {
			guid[i] = b[offset + i];
		}
		return guid;
	}

	public static String getString(byte[] b, int offset, int length) {
		byte[] b1 = new byte[length];
		System.arraycopy(b, offset, b1, 0, length);
		return new String(b1);
	}

	public static int getPacketType(byte[] data) {
		byte[] b = new byte[4];
		System.arraycopy(data, 4, b, 0, 4);
		return bytesToInt(b);
	}

	public static byte[] intToBytes(int n) {
		byte[] b = new byte[4];

		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (n >> (i * 8));

		}
		return b;
	}

	public static int bytesToInt(byte[] b) {

		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for (int i = b.length - 1; i >= 0; i--) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}
	
	public static int getStringSize(String str) {
		if (str == null) {
			return 1;
		} else {
			return 1 + 2 * str.length() + 2;
		}
	}
	
	public static int getCodeArraySize(int[] codeArray) {
		if (codeArray == null) {
			return 4;
		} else {
			return 4 + 2 * codeArray.length;
		}
	}
	
	public static void outputln(Object str) {
		System.out.println(str);
	}
	
	public static void output(Object str) {
		System.out.print(str);
	}
	
	public static void outputln() {
		System.out.println();
	}
	
	public static int addString(int len, String str) {
		len += str.length();
		len++;
		if (len < 80) {
			output(str);
			output(" ");
		} else {
			outputln();
			output("\t");
			output(str);
			output(" ");
			len = 8 + str.length() + 1;
		}
		return len;
	}
}
