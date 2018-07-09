package jp.co.gitaku.ptp;

import jp.co.gitaku.util.Common;

public class ObjectFilesystemInfo extends Data {
	int objectHandle;
	int storageID;
	int objectFormat;
	int protectionStatus;
	long objectCompressedSize64;
	int parentObject;
	int associationType;
	int associationDesc;
	int sequenceNumber;
	String filename;
	String modificationDate;

	public ObjectFilesystemInfo() {
		super(true, null, 0);
	}
	
	public ObjectFilesystemInfo(boolean isIn, byte[] data) {
		super(isIn, data);
		bytes2Content();
	}
	
	public ObjectFilesystemInfo(boolean isIn,
		int objectHandle,
		int storageID,
		int objectFormat,
		int protectionStatus,
		long objectCompressedSize64,
		int parentObject,
		int associationType,
		int associationDesc,
		int sequenceNumber,
		String filename,
		String modificationDate
				) {
		super(isIn, 
				// new byte[HDR_LEN + getDataLength(vendorExtensionDesc,
				new byte[getDataLength(filename, modificationDate)]);
		
		this.objectHandle = objectHandle;
		this.storageID = storageID;
		this.objectFormat = objectFormat;
		this.protectionStatus = protectionStatus;
		this.objectCompressedSize64 = objectCompressedSize64;
		this.parentObject = parentObject;
		this.associationType = associationType;
		this.associationDesc = associationDesc;
		this.sequenceNumber = sequenceNumber;
		this.filename = filename;
		this.modificationDate = modificationDate;
		content2Bytes();
	}

	public void content2Bytes() {
		// connection Number
		put32(objectHandle);
		put32(storageID);
		put16(objectFormat);
		put16(protectionStatus);
		put64(objectCompressedSize64);
		put32(parentObject);
		put16(associationType);
		put32(associationDesc);
		put32(sequenceNumber);
		putString(filename);
		putString(modificationDate);
	}

	void bytes2Content() {
		this.objectHandle = nextS32();
		this.storageID = nextS32();
		this.objectFormat = nextU16();
		this.protectionStatus = nextU16();
		this.objectCompressedSize64 = nextS64();
		this.parentObject = nextS32();
		this.associationType = nextU16();
		this.associationDesc = nextS32();
		this.sequenceNumber = nextS32();
		this.filename = nextString();
		this.modificationDate = nextString();
	}


	static int getDataLength(String filename,
			String modificationDate
) {
		int length = 0;
		
		// objectHandle
		length += 4;
		// storageID
		length += 4;
		// objectFormat
		length += 2;
		// protectionStatus
		length += 2;
		// objectCompressedSize64
		length += 8;
		// parentObject
		length += 4;
		// associationType
		length += 2;
		// associationDesc 
		length += 4;
		// sequenceNumber
		length += 4;
		// filename
		length += Common.getStringSize(filename);
		// modificationDate
		length += Common.getStringSize(modificationDate);
		
		return length;
	}

}