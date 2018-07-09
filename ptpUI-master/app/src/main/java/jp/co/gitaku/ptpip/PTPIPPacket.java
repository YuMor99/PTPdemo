package jp.co.gitaku.ptpip;

import jp.co.gitaku.ptp.Buffer;

public class PTPIPPacket extends Buffer {
	static final int PTP_IP_HDR_LEN = 8;
	
	public static final int InvalidValue = 0;
	public static final int InitCommandRequestPacket = 1;
	public static final int InitCommandAckPacket = 2;
	public static final int InitEventRequestPacket = 3;
	public static final int InitEventAckPacket = 4;
	public static final int InitFailPacket = 5;
	public static final int OperationRequestPacket = 6;
	public static final int OperationResponsePacket = 7;
	public static final int EventPacket = 8;
	public static final int StartDataPacket = 9;
	public static final int DataPacket = 10;
	public static final int CancelPacket = 11;
	public static final int EndDataPacket = 12;
	public static final int ProbeRequestPacket = 13;
	public static final int ProbeResponsePacket = 14;
	
	private int packetType;
	
	protected PTPIPPacket(byte[] data) {
		super(data);
		setPacketType(getS32(4));
	}
	
	protected PTPIPPacket(int fixedSize, int variableSize, int packetType) {
		super(new byte[fixedSize + variableSize]);
		offset = 0;
		put32(length);
		put32(packetType);
	}
	
	protected void putString1(String value) {
		if (value == null) {
			return;
		}

		int i = value.length();

		for (int j = 0; j < i; j++) {
			put16(value.charAt(j));
		}
	}
	
	public int getPacketType() {
		return packetType;
	}

	public void setPacketType(int packetType) {
		this.packetType = packetType;
	}
}
