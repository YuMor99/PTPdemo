package jp.co.gitaku.ptpip;

public class CancelPacket extends PTPIPPacket {

	private static int FIXED_LENGTH = 12;
	private int transactionID;
	
	public CancelPacket(byte[] data) {
		super(data);
	}
	
	public CancelPacket(int transactionID) {
		super(FIXED_LENGTH, 0, PTPIPPacket.CancelPacket);
		this.transactionID = transactionID;
		content2Bytes();
	}
	
	public void content2Bytes() {
		// connection Number
		put32(transactionID);
	}
	
	public void bytes2Content() {
		if (length == 0) {
			return;
		}
		transactionID = getS32(8);
	}
	
	public int getTransactionID() {
		return transactionID;
	}
}
