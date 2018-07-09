package jp.co.gitaku.ptpip;

public class StartDataPacket extends PTPIPPacket {

	private static int FIXED_LENGTH = 16;
	private int transactionID;
	private long totalDataLength;
	
	public StartDataPacket(byte[] data) {
		super(data);
		bytes2Content();
	}
	
	public StartDataPacket(int transactionID, int totalDataLength) {
		super(FIXED_LENGTH, 0, PTPIPPacket.StartDataPacket);
		this.transactionID = transactionID;
		this.totalDataLength = totalDataLength;
		content2Bytes();
	}
	
	public void content2Bytes() {
		// connection Number
		put32(transactionID);
		put32(totalDataLength);
	}
	
	public void bytes2Content() {
		if (length == 0) {
			return;
		}
		transactionID = getS32(8);
		totalDataLength = getS32(12);
	}
	
	public int getTransactionID() {
		return transactionID;
	}

	public long getTotalDataLength() {
		return totalDataLength;
	}
}
