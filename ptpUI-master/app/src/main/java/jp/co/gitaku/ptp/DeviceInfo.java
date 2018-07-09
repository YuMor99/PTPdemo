package jp.co.gitaku.ptp;

import jp.co.gitaku.util.Common;

public class DeviceInfo extends Data {
	int standardVersion;
	int vendorExtensionId;
	int vendorExtensionVersion;
	String vendorExtensionDesc;
	int functionalMode;
	int[] operationsSupported;
	int[] eventsSupported;
	int[] propertiesSupported;
	int[] captureFormats;
	int[] imageFormats;
	String manufacturer;
	String model;
	String deviceVersion;
	String serialNumber;

	public DeviceInfo() {
		super(true, null, 0);
	}
	
	public DeviceInfo(boolean isIn, byte[] data) {
		super(isIn, data);
		bytes2Content();
	}
	
	public DeviceInfo(boolean isIn,
//					int code,
//					int sessionID,
//					int transactionID,
					int standardVersion,
					int vendorExtensionId,
					int vendorExtensionVersion,
					String vendorExtensionDesc,
					int functionalMode,
					int[] operationsSupported,
					int[] eventsSupported,
					int[] propertiesSupported,
					int[] captureFormats,
					int[] imageFormats,
					String manufacturer,
					String model,
					String deviceVersion,
					String serialNumber
				) {
		super(isIn, 
				// new byte[HDR_LEN + getDataLength(vendorExtensionDesc,
				new byte[getDataLength(vendorExtensionDesc,
				operationsSupported, 
				eventsSupported, 
				propertiesSupported, 
				captureFormats, 
				imageFormats, 
				manufacturer, 
				model, 
				deviceVersion, 
				serialNumber)]);
//		putHeader(code, sessionID, transactionID);
		this.standardVersion = standardVersion;
		this.vendorExtensionId = vendorExtensionId;
		this.vendorExtensionVersion = vendorExtensionVersion;
		this.vendorExtensionDesc = vendorExtensionDesc;
		this.functionalMode = functionalMode;
		this.operationsSupported = operationsSupported;
		this.eventsSupported = eventsSupported;
		this.propertiesSupported = propertiesSupported;
		this.captureFormats = captureFormats;
		this.imageFormats = imageFormats;
		this.manufacturer = manufacturer;
		this.model = model;
		this.deviceVersion = deviceVersion;
		this.serialNumber = serialNumber;
		content2Bytes();
	}

	public void content2Bytes() {
		// connection Number
		put16(standardVersion);
		put32(vendorExtensionId);
		put16(vendorExtensionVersion);
		putString(vendorExtensionDesc);
		put16(functionalMode);
		putU16Array(operationsSupported);
		putU16Array(eventsSupported);
		putU16Array(propertiesSupported);
		putU16Array(captureFormats);
		putU16Array(imageFormats);
		putString(manufacturer);
		putString(model);
		putString(deviceVersion);
		putString(serialNumber);
	}
	
	private boolean supports(int[] paramArrayOfInt, int paramInt) {
		for (int i = 0; i < paramArrayOfInt.length; i++) {
			if (paramInt == paramArrayOfInt[i])
				return true;
		}
		return false;
	}

	public boolean supportsOperation(int paramInt) {
		return supports(this.operationsSupported, paramInt);
	}

	public boolean supportsEvent(int paramInt) {
		return supports(this.eventsSupported, paramInt);
	}

	public boolean supportsProperty(int paramInt) {
		return supports(this.propertiesSupported, paramInt);
	}

	public boolean supportsCaptureFormat(int paramInt) {
		return supports(this.captureFormats, paramInt);
	}

	public boolean supportsImageFormat(int paramInt) {
		return supports(this.imageFormats, paramInt);
	}

	void bytes2Content() {
//		super.parse();
		this.standardVersion = nextU16();
		this.vendorExtensionId = nextS32();
		this.vendorExtensionVersion = nextU16();
		this.vendorExtensionDesc = nextString();

		this.functionalMode = nextU16();
		this.operationsSupported = nextU16Array();
		this.eventsSupported = nextU16Array();
		this.propertiesSupported = nextU16Array();

		this.captureFormats = nextU16Array();
		this.imageFormats = nextU16Array();
		this.manufacturer = nextString();
		this.model = nextString();

		this.deviceVersion = nextString();
		this.serialNumber = nextString();
	}

	static String funcMode(int paramInt) {
		switch (paramInt) {
		case 0:
			return "standard";
		case 1:
			return "sleeping";
		}

		StringBuffer localStringBuffer = new StringBuffer();

		if ((paramInt & 0x8000) == 0)
			localStringBuffer.append("reserved 0x");
		else
			localStringBuffer.append("vendor 0x");
		localStringBuffer.append(Integer.toHexString(paramInt & 0xFFFF7FFF));
		return localStringBuffer.toString();
	}

	static String vendorToString(int paramInt) {
		switch (paramInt) {
		case 1:
			return "Eastman Kodak Company";
		case 2:
			return "Seiko Epson";
		case 3:
			return "Agilent Technologies, Inc.";
		case 4:
			return "Polaroid Corporation";
		case 5:
			return "Agfa-Gevaert";
		case 6:
			return "Microsoft Corporation";
		}
		return "0x" + Integer.toHexString(paramInt);
	}
	
	static int getDataLength(String vendorExtensionDesc,
								int[] operationsSupported,
								int[] eventsSupported,
								int[] propertiesSupported,
								int[] captureFormats,
								int[] imageFormats,
								String manufacturer,
								String model,
								String deviceVersion,
								String serialNumber) {
		int length = 0;
		
		// standard Version Length
		length += 2;
		// vendor Extension IdLength
		length += 4;
		// vendor Extension Version Length 
		length += 2;
		// vendor Extension Desc Length
		length += Common.getStringSize(vendorExtensionDesc);
		// functional Mode Length 
		length += 2;
		// operations Supported Length
		length += Common.getCodeArraySize(operationsSupported);
		// events Supported Length 
		length += Common.getCodeArraySize(eventsSupported);
		// properties Supported Length 
		length += Common.getCodeArraySize(propertiesSupported);
		// capture Formats Length 
		length += Common.getCodeArraySize(captureFormats);
		// image Formats Length 
		length += Common.getCodeArraySize(imageFormats);
		// manufacturer Length 
		length += Common.getStringSize(manufacturer);
		// model Length 
		length += Common.getStringSize(model);
		// device Version Length 
		length += Common.getStringSize(deviceVersion);
		// serial Number Length 
		length += Common.getStringSize(serialNumber);
		
		return length;
	}

	public void print() {
		if (this.operationsSupported == null) {
			return;
		}

		Common.outputln("DeviceInfo:");
		Common.outputln("PTP Version: " + this.standardVersion / 100 + "." + this.standardVersion % 100);
//		if (this.vendorExtensionId != 0) {
//			Common.output("Extensions (");
//			Common.output(vendorToString(this.vendorExtensionId));
//			Common.output(")");
//			if (this.vendorExtensionVersion != 0) {
//				Common.output(" Version: ");
//				Common.output(this.vendorExtensionDesc);
//			}
//			if (this.vendorExtensionDesc != null) {
//				Common.output(" Desc: ");
//				Common.output(this.vendorExtensionDesc);
//			}
//			Common.outputln();
//		}
		
		if (this.functionalMode != 0) {
			Common.output("Functional Mode: ");
			Common.outputln(funcMode(this.functionalMode));
		}
		
		Common.outputln("Operations Supported:");
		Common.output("\t");
		int i = 8;
		String str;
		for (int j = 0; j < this.operationsSupported.length; j++) {
			str = Command.getOpcodeString(this.operationsSupported[j]);
			i = Common.addString(i, str);
		}
		Common.outputln();

		Common.outputln("Events Supported:");
		Common.output("\t");
		i = 8;
		for (int k = 0; k < this.eventsSupported.length; k++) {
			str = Event.getEventString(this.eventsSupported[k]);
			i = Common.addString(i, str);
		}
		Common.outputln();

		Common.outputln("Device Properties Supported:");
		Common.output("\t");
		i = 8;
		for (int m = 0; m < this.propertiesSupported.length; m++) {
			str = DevicePropDesc.getPropertyName(this.propertiesSupported[m]);
			i = Common.addString(i, str);
		}
		Common.outputln();

		Common.outputln("Capture Formats Supported:");
		Common.output("\t");
		i = 8;
		for (int n = 0; n < this.captureFormats.length; n++) {
			str = ObjectInfo.getFormatString(this.captureFormats[n]);
			i = Common.addString(i, str);
		}
		Common.outputln();

		Common.outputln("Image Formats Supported:");
		Common.output("\t");
		i = 8;
		for (int i1 = 0; i1 < this.imageFormats.length; i1++) {
			str = ObjectInfo.getFormatString(this.imageFormats[i1]);
			i = Common.addString(i, str);
		}
		Common.outputln();
		
		if (this.manufacturer != null)
			Common.outputln("Manufacturer: " + this.manufacturer);
		if (this.model != null)
			Common.outputln("Model: " + this.model);
		if (this.deviceVersion != null)
			Common.outputln("Device Version: " + this.deviceVersion);
		if (this.serialNumber != null) {
			Common.outputln("Serial Number: " + this.serialNumber);
		}

		if (this.vendorExtensionId != 0) {
			Common.output("Vendor Extension from ");
			Common.output(vendorToString(this.vendorExtensionId));
			Common.output(", version ");
			Common.output(this.standardVersion / 100);
			Common.output(".");
			Common.outputln(this.standardVersion % 100);

			if (this.vendorExtensionDesc != null) {
				Common.output("Description: ");
				Common.outputln(this.vendorExtensionDesc);
			}
		}
	}
	
	public int getStandardVersion() {
		return standardVersion;
	}

	public int getVendorExtensionId() {
		return vendorExtensionId;
	}

	public int getVendorExtensionVersion() {
		return vendorExtensionVersion;
	}

	public String getVendorExtensionDesc() {
		return vendorExtensionDesc;
	}

	public int getFunctionalMode() {
		return functionalMode;
	}

	public int[] getOperationsSupported() {
		return operationsSupported;
	}

	public int[] getEventsSupported() {
		return eventsSupported;
	}

	public int[] getPropertiesSupported() {
		return propertiesSupported;
	}

	public int[] getCaptureFormats() {
		return captureFormats;
	}

	public int[] getImageFormats() {
		return imageFormats;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getModel() {
		return model;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public String getSerialNumber() {
		return serialNumber;
	}
}