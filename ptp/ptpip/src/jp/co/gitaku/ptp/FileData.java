package jp.co.gitaku.ptp;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileData extends Data {
	private FileOutputStream out;

	public FileData(FileOutputStream fileOutputString) {
		this.out = fileOutputString;
	}

	public void write(byte[] bytes, int paramInt1, int paramInt2) throws IOException {
		this.out.write(bytes, paramInt1, paramInt2);
	}

	public void close() throws IOException {
		this.out.close();
	}

	final void parse() {
	}

	@Override
	void bytes2Content() {
	}
}