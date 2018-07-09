package jp.co.gitaku.ptpip;

import jp.co.gitaku.util.Common;

public class InitCommandRequestPacket extends PTPIPPacket {

	private static int FIXED_LENGTH = 28;
	private int[] guid;
	private String friendlyName;
	private int version;
	
	public InitCommandRequestPacket(byte[] data) {
		super(data);
		bytes2Content();
	}
	
	public InitCommandRequestPacket(int[] guid, String friendlyName, int version) {
		super(FIXED_LENGTH, Common.getSizeOfString(friendlyName), PTPIPPacket.InitCommandRequestPacket);
		this.guid = guid;
		this.friendlyName = friendlyName;
		this.version = version;
		convertToBytes();
	}
	
	public void convertToBytes() {
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
		guid = Common.getGuid(data, 8);
		friendlyName = Common.getString(data, 28, length - 28);
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
}
