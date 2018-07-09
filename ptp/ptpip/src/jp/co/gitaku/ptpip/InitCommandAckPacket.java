package jp.co.gitaku.ptpip;

import jp.co.gitaku.util.Common;

public class InitCommandAckPacket extends PTPIPPacket {

	private static int FIXED_LENGTH = 32;
	private int connectionNumber;
	private int[] guid;
	private String friendlyName;
	private int version;
	
	public InitCommandAckPacket(byte[] data) {
		super(data);
		bytes2Content();
	}
	
	public InitCommandAckPacket(int connectionNumber, int[] guid, String friendlyName, int version) {
		super(FIXED_LENGTH, Common.getSizeOfString(friendlyName), PTPIPPacket.InitCommandAckPacket);
		this.connectionNumber = connectionNumber;
		this.guid = guid;
		this.friendlyName = friendlyName;
		this.version = version;
		content2Bytes();
	}
	
	public void content2Bytes() {
		// connection Number
		put32(connectionNumber);
		// guid
		for (int i = 0; i < guid.length; i++) {
			put8(guid[i]);
		}
		// friendly Name
		putString1(friendlyName);
		// version
		put32(version);
	}
	
	public void bytes2Content() {
		if (length == 0) {
			return;
		}
		connectionNumber = getS32(8);
		guid = Common.getGuid(data, 12);
		friendlyName = Common.getString(data, 28, length - 32);
		version = getS32(length - 4);
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}

	public int getVersion() {
		return version;
	}

	public int[] getGuid() {
		return guid;
	}

	public int getConnectionNumber() {
		return connectionNumber;
	}
}
