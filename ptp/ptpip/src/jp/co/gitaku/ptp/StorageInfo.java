package jp.co.gitaku.ptp;

import jp.co.gitaku.util.Common;

public class StorageInfo extends Data {
	int storageType;
	int filesystemType;
	int accessCapability;
	long maxCapacity;
	long freeSpaceInBytes;
	int freeSpaceInImages;
	String storageDescription;
	String volumeLabel;

	public StorageInfo() {
		super(true, null, 0);
	}

	public StorageInfo(boolean isIn, byte[] data) {
		super(isIn, data);
		bytes2Content();
	}

	void bytes2Content() {
		this.storageType = nextU16();
		this.filesystemType = nextU16();
		this.accessCapability = nextU16();
		this.maxCapacity = nextS64();
		this.freeSpaceInBytes = nextS64();
		this.freeSpaceInImages = nextS32();
		this.storageDescription = nextString();
		this.volumeLabel = nextString();
	}

	public void print() {
		Common.outputln("StorageInfo:");
		String strStorType;
		switch (this.storageType) {
		case 0:
			strStorType = "undefined";
			break;
		case 1:
			strStorType = "Fixed ROM";
			break;
		case 2:
			strStorType = "Removable ROM";
			break;
		case 3:
			strStorType = "Fixed RAM";
			break;
		case 4:
			strStorType = "Removable RAM";
			break;
		default:
			strStorType = "Reserved";
			strStorType = strStorType + Integer.toHexString(this.storageType);
		}
		Common.outputln("Storage Type: " + strStorType);
		String strFileSysType;
		switch (this.filesystemType) {
		case 0:
			strFileSysType = "undefined";
			break;
		case 1:
			strFileSysType = "flat";
			break;
		case 2:
			strFileSysType = "hierarchical";
			break;
		case 3:
			strFileSysType = "dcf";
			break;
		default:
			if ((this.filesystemType & 0x8000) != 0)
				strFileSysType = "Reserved-0x";
			else
				strFileSysType = "Vendor-0x";
			strFileSysType = strFileSysType + Integer.toHexString(this.filesystemType);
		}
		Common.outputln("Filesystem Type: " + strFileSysType);

		String strAccCap;
		switch (this.accessCapability) {
		case 0:
			strAccCap = "Read-write";
			break;
		case 1:
			strAccCap = "Read-only without object deletion";
			break;
		case 2:
			strAccCap = "Read-only with object deletion";
			break;
		default:
			strAccCap = "Reserved";
			strAccCap = strAccCap + Integer.toHexString(this.accessCapability);
		}
		Common.outputln("Access Capability: " + strAccCap);

		if (this.maxCapacity != -1L)
			Common.outputln("Capacity in bytes: " + this.maxCapacity);
		if (this.freeSpaceInBytes != -1L)
			Common.outputln("Free bytes: " + this.freeSpaceInBytes);
		if (this.freeSpaceInImages != -1) {
			Common.outputln("Free space in Images: " + this.freeSpaceInImages);
		}
		if (this.storageDescription != null)
			Common.outputln("Description: " + this.storageDescription);
		if (this.volumeLabel != null)
			Common.outputln("Volume Label: " + this.volumeLabel);
	}
}