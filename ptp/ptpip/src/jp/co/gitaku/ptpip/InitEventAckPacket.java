package jp.co.gitaku.ptpip;

public class InitEventAckPacket extends PTPIPPacket {

	private static int FIXED_LENGTH = 8;
	
	public InitEventAckPacket(byte[] data) {
		super(data);
	}
	
	public InitEventAckPacket() {
		super(FIXED_LENGTH, 0, PTPIPPacket.InitEventAckPacket);
	}
}
