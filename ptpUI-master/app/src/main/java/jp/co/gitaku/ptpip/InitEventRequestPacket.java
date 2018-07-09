package jp.co.gitaku.ptpip;

public class InitEventRequestPacket extends PTPIPPacket {

	private static int FIXED_LENGTH = 12;
	private int connectionNumber;
	
	public InitEventRequestPacket(byte[] data) {
		super(data);
		bytes2Content();
	}
	
	public InitEventRequestPacket(int connectionNumber) {
		super(FIXED_LENGTH, 0, PTPIPPacket.InitEventRequestPacket);
		this.connectionNumber = connectionNumber;
		content2Bytes();
	}
	
	public void content2Bytes() {
		// connection Number
		put32(connectionNumber);
	}
	
	public void bytes2Content() {
		if (length == 0) {
			return;
		}
		connectionNumber = getS32(8);
	}
	
	public int getConnectionNumber() {
		return connectionNumber;
	}
}
