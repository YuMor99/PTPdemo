package jp.co.gitaku.ptp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public class FileSendData extends Data {
	public static int TEMP_SIZE = 131072;
	private InputStream in;
	private int filesize;

	public FileSendData(URLConnection urlConn) throws IOException {
		super(false, new byte[TEMP_SIZE]);
		this.in = urlConn.getInputStream();
		this.filesize = urlConn.getContentLength();
	}

	public int getLength() {
		return this.filesize;
	}

	public int read(byte[] bytes, int off, int len) throws IOException {
		return this.in.read(bytes, off, len);
	}

	public void close() throws IOException {
		this.in.close();
	}

	@Override
	void bytes2Content() {
	}
}