package jp.co.gitaku.ptpip;

public class OperationResponsePacket extends PTPIPPacket {

	public static int NoDataOrDataIn = 1;
	public static int DataOut = 2;
	public static int UnknownData = 3;
	
	private static int FIXED_LENGTH = 34;
	private int responseCode;
	private int transactionID;
	private int param1;
	private int param2;
	private int param3;
	private int param4;
	private int param5;
	
	public OperationResponsePacket(byte[] data) {
		super(data);
		bytes2Content();
	}
	
	public OperationResponsePacket(int responseCode, int transactionID,
			int param1, int param2, int param3, int param4, int param5) {
		super(FIXED_LENGTH, 0, PTPIPPacket.OperationResponsePacket);
		this.responseCode = responseCode;
		this.transactionID = transactionID;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
		this.param5 = param5;
		convertToBytes();
	}
	
	public void convertToBytes() {
		put16(responseCode);
		put32(transactionID);
		put32(param1);
		put32(param2);
		put32(param3);
		put32(param4);
		put32(param5);
	}
	
	public void bytes2Content() {
		if (length == 0) {
			return;
		}
		int index = 8;
		responseCode = getU16(index);
		transactionID = getS32(index += 2);
		if (index + 4 >= length) {
			return;
		}
		param1 = getS32(index += 4);
		if (index + 4 >= length) {
			return;
		}
		param2 = getS32(index += 4);
		if (index + 4 >= length) {
			return;
		}
		param3 = getS32(index += 4);
		if (index + 4 >= length) {
			return;
		}
		param4 = getS32(index += 4);
		if (index + 4 >= length) {
			return;
		}
		param5 = getS32(index += 4);
	}

	public int getResponseCode() {
		return responseCode;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public int getParam1() {
		return param1;
	}

	public int getParam2() {
		return param2;
	}

	public int getParam3() {
		return param3;
	}

	public int getParam4() {
		return param4;
	}

	public int getParam5() {
		return param5;
	}
}
