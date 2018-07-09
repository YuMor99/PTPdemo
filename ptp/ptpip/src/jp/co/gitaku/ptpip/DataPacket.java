package jp.co.gitaku.ptpip;

public class DataPacket extends PTPIPPacket {

	private static int FIXED_LENGTH = 12;
	private int transactionID;
	private byte[] dataPayload;
	
	public DataPacket(byte[] data) {
		super(data);
		bytes2Content();
	}
	
	public DataPacket(int transactionID, byte[] dataPayload) {
		super(FIXED_LENGTH, dataPayload.length, PTPIPPacket.DataPacket);
		this.transactionID = transactionID;
		this.dataPayload = dataPayload;
		content2Bytes();
	}
	
	public void content2Bytes() {
		// connection Number
		put32(transactionID);
		System.arraycopy(dataPayload, 0, data, 12, dataPayload.length);
	}
	
	public void bytes2Content() {
		if (length == 0) {
			return;
		}
		transactionID = getS32(8);
		dataPayload = new byte[length - 12];
		System.arraycopy(data, 12, dataPayload, 0, dataPayload.length);
	}
	
	public int getTransactionID() {
		return transactionID;
	}

	public byte[] getDataPayload() {
		return dataPayload;
	}
}
