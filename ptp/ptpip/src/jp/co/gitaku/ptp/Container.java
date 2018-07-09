package jp.co.gitaku.ptp;

import java.io.PrintStream;

public abstract class Container extends Buffer {
	static final int HDR_LEN = 10;
	
	protected int code;
	protected int sessionID;
	protected int transactionID;

	Container(byte[] data) {
		super(data, data.length);
	}

	Container(byte[] data, int length) {
		super(data, length);
	}

	/*
	 * set header
	 * @param Code
	 * @param SessionID
	 * @param TransactionID
	 */
	public void putHeader(int code, int sessionID, int transactionID) {
		this.code = code;
		this.sessionID = sessionID;
		this.transactionID = transactionID;
		offset = 0;
		put16(code);
		put32(sessionID);
		put32(transactionID);
	}

	public String toString() {
		StringBuffer localStringBuffer = new StringBuffer();
		String str = getBlockTypeName(getBlockType());
		int i = getCode();

		localStringBuffer.append("{ ");
		localStringBuffer.append(str);
		localStringBuffer.append("; len ");
		localStringBuffer.append(Integer.toString(getLength()));
		localStringBuffer.append("; ");
		localStringBuffer.append(getCodeName(i));
		localStringBuffer.append("; TransactionID ");
		localStringBuffer.append(Integer.toString(getTransactionID()));

		if ((this instanceof ParamVector)) {
			ParamVector localParamVector = (ParamVector) this;
			int j = localParamVector.getNumParams();

			if (j > 0) {
				localStringBuffer.append("; ");
				for (int k = 0; k < j; k++) {
					if (k != 0)
						localStringBuffer.append(" ");
					localStringBuffer.append("0x");
					localStringBuffer.append(Integer.toHexString(localParamVector.getParam(k)));
				}
			}
		}

		localStringBuffer.append(" }");
		return localStringBuffer.toString();
	}

	void dump(PrintStream paramPrintStream) {
	}

	void parse() {
		this.code = nextU16();
		this.sessionID = nextS32();
		this.transactionID = nextS32();
	}

	public int getPTPIPLength() {
		return getS32(0);
	}

	public final int getBlockType() {
		return getU16(4);
	}

	public static final String getBlockTypeName(int paramInt) {
		switch (paramInt) {
		case 1:
			return "command";
		case 2:
			return "data";
		case 3:
			return "response";
		case 4:
			return "event";
		}
		return Integer.toHexString(paramInt).intern();
	}

	public final int getCode() {
		return getU16(0);
	}

	public static final String getCodeType(int paramInt) {
		switch (paramInt >> 12) {
		case 1:
			return "OperationCode";
		case 2:
			return "ResponseCode";
		case 3:
			return "ObjectFormatCode";
		case 4:
			return "EventCode";
		case 5:
			return "DevicePropCode";
		case 9:
			return "Vendor-OpCode";
		case 10:
			return "Vendor-ResponseCode";
		case 11:
			return "Vendor-FormatCode";
		case 12:
			return "Vendor-EventCode";
		case 13:
			return "Vendor-PropCode";
		case 6:
		case 7:
		case 8:
		}
		return Integer.toHexString(paramInt).intern();
	}

	public String getCodeName(int paramInt) {
		return getCodeString(paramInt);
	}

	public final String getCodeString() {
		return getCodeName(getCode()).intern();
	}

	public static String getCodeString(int paramInt) {
		return Integer.toHexString(paramInt).intern();
	}

	public final int getTransactionID() {
		return getS32(6);
	}

	public int getSessionID() {
		return sessionID;
	}
}