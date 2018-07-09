package jp.co.gitaku.ptp;

import java.io.PrintStream;

public class ParamVector extends Container {
	static final int MIN_LEN = 10;
	static final int MAX_LEN = 30;
	protected Session session;

	ParamVector(byte[] paramArrayOfByte) {
		super(paramArrayOfByte);
	}

	ParamVector(byte[] paramArrayOfByte, int paramInt) {
		super(paramArrayOfByte, paramInt);
	}
	
	private ParamVector(int paramNum, int code, Session session) {
		super(new byte[HDR_LEN + 4 * paramNum]);
		this.session = session;
		putHeader(code, session.getSessionId(), session.getNextTransactionId());
	}

	ParamVector(int code, Session paramSession, int param1, int param2, int param3) {
		this(3, code, paramSession);
		put32(param1);
		put32(param2);
		put32(param3);
	}
	
	ParamVector(int code, Session paramSession, int param1, int param2, int param3, int param4, int param5) {
		this(5, code, paramSession);
		put32(param1);
		put32(param2);
		put32(param3);
		put32(param4);
		put32(param5);
	}

	public final int getParam1() {
		return getS32(10);
	}

	public final int getParam2() {
		return getS32(14);
	}

	public final int getParam3() {
		return getS32(18);
	}

	public final int getParam4() {
		return getS32(22);
	}

	public final int getParam5() {
		return getS32(26);
	}

	public final int getNumParams() {
		return (this.length - 10) / 4;
	}

	int getParam(int paramInt) {
		return getS32(10 + 4 * paramInt);
	}

	void dump(PrintStream paramPrintStream) {
		paramPrintStream.print(toString());
	}

	public Session getSession() {
		return session;
	}
}