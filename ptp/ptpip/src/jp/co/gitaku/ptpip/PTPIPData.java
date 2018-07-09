package jp.co.gitaku.ptpip;

import jp.co.gitaku.ptp.Buffer;

public class PTPIPData extends Buffer {
	private boolean in;
	
	PTPIPData(boolean isIn, byte[] data) {
		super(data);
		this.in = isIn;
	}

	PTPIPData(boolean isIn, byte[] data, int length) {
		super(data, length);
		this.in = isIn;
	}

	boolean isIn() {
		return this.in;
	}
}
