package jp.co.gitaku.ptpip;

public class OperationRequestPacket extends PTPIPPacket {

	public static int NoDataOrDataIn = 1;
	public static int DataOut = 2;
	public static int UnknownData = 3;
	
	private static int FIXED_LENGTH = 38;
	private int dataPhaseInfo;
	private int operationCode;
	private int transactionID;
	private int param1;
	private int param2;
	private int param3;
	private int param4;
	private int param5;
	
	public OperationRequestPacket(byte[] data) {
		super(data);
		bytes2Content();
	}
	
	public OperationRequestPacket(int dataPhaseInfo, int operationCode, int transactionID,
			int param1, int param2, int param3, int param4, int param5) {
		super(FIXED_LENGTH, 0, PTPIPPacket.OperationRequestPacket);
		this.dataPhaseInfo = dataPhaseInfo;
		this.operationCode = operationCode;
		this.transactionID = transactionID;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
		this.param5 = param5;
		convertToBytes();
	}
	
	public void convertToBytes() {
		put32(dataPhaseInfo);
		put16(operationCode);
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
		dataPhaseInfo = getS32(index);
		operationCode = getU16(index += 4);
		transactionID = getS32(index += 2);
		param1 = getS32(index += 4);
		param2 = getS32(index += 4);
		param3 = getS32(index += 4);
		param4 = getS32(index += 4);
		param5 = getS32(index += 4);
	}

	public int getDataPhaseInfo() {
		return dataPhaseInfo;
	}

	public int getOperationCode() {
		return operationCode;
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
