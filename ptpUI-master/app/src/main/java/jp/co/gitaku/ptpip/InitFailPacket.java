package jp.co.gitaku.ptpip;

public class InitFailPacket extends PTPIPPacket {

	public static final int FAIL_REJECTED_INITIATOR = 1;
	public static final int FAIL_BUSY = 2;
	public static final int FAIL_UNSPECIFIED = 3;

	private static int FIXED_LENGTH = 12;
	private int reason;

	public InitFailPacket(byte[] data) {
		super(data);
		bytes2Content();
	}

	public InitFailPacket(int reason) {
		super(FIXED_LENGTH, 0, PTPIPPacket.InitFailPacket);
		this.reason = reason;
		content2Bytes();
	}

	public void content2Bytes() {
		// connection Number
		put32(reason);
	}

	public void bytes2Content() {
		if (length == 0) {
			return;
		}
		reason = getS32(8);
	}

	public int getReason() {
		return reason;
	}

	public static String getReasonString(int reasonCode) {
		switch (reasonCode) {
		case FAIL_REJECTED_INITIATOR:
			return "It is not allowed to access the device!";
		case FAIL_BUSY:
			return "The device is busy, please try again later!";
		default:
			return "Unkown exception!";
		}
	}
}
