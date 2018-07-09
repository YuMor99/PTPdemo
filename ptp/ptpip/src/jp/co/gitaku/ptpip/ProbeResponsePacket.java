package jp.co.gitaku.ptpip;

public class ProbeResponsePacket extends PTPIPPacket {

	private static int FIXED_LENGTH = 8;
	
	public ProbeResponsePacket(byte[] data) {
		super(data);
	}
	
	public ProbeResponsePacket() {
		super(FIXED_LENGTH, 0, PTPIPPacket.ProbeResponsePacket);
	}
}
