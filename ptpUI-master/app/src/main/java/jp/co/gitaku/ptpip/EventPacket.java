package jp.co.gitaku.ptpip;

public class EventPacket extends PTPIPPacket {

	private static int FIXED_LENGTH = 26;
	private int eventCode;
	private int transactionID;
	private int param1;
	private int param2;
	private int param3;
	
	public EventPacket(byte[] data) {
		super(data);
		bytes2Content();
	}
	
	public EventPacket(int eventCode, int transactionID, int param1, int param2, int param3) {
		super(FIXED_LENGTH, 0, PTPIPPacket.InitEventRequestPacket);
		this.eventCode = eventCode;
		this.transactionID = transactionID;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		content2Bytes();
	}

	public void content2Bytes() {
		// connection Number
		put16(eventCode);
		put32(transactionID);
		put32(param1);
		put32(param2);
		put32(param3);
	}
	
	public void bytes2Content() {
		if (length == 0) {
			return;
		}
		eventCode = getU16(8);
		transactionID = getS32(10);
		if (length < 18)
			return;
		param1 = getS32(14);
		if (length < 22)
			return;
		param2 = getS32(18);
		if (length < 26)
			return;
		param3 = getS32(22);
	}
	
	public int getEventCode() {
		return eventCode;
	}

	public void setEventCode(int eventCode) {
		this.eventCode = eventCode;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public int getParam1() {
		return param1;
	}

	public void setParam1(int param1) {
		this.param1 = param1;
	}

	public int getParam2() {
		return param2;
	}

	public void setParam2(int param2) {
		this.param2 = param2;
	}

	public int getParam3() {
		return param3;
	}

	public void setParam3(int param3) {
		this.param3 = param3;
	}
	
}
