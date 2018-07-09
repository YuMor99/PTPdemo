package jp.co.gitaku.ptpip;

public class ProbeRequestPacket extends PTPIPPacket {

	private static int FIXED_LENGTH = 8;
	
	public ProbeRequestPacket(byte[] data) {
		super(data);
	}
	
	public ProbeRequestPacket() {
		super(FIXED_LENGTH, 0, PTPIPPacket.ProbeRequestPacket);
	}
}
