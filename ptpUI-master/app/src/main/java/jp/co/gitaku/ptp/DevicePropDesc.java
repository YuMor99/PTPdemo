package jp.co.gitaku.ptp;

import java.util.Vector;

import jp.co.gitaku.util.Common;

public class DevicePropDesc extends Data {
	int propertyCode;
	int dataType;
	boolean writable;
	Object factoryDefault;
	Object currentValue;
	int formType;
	Object constraints;
	public static final int BatteryLevel = 20481;
	public static final int FunctionalMode = 20482;
	public static final int ImageSize = 20483;
	public static final int CompressionSetting = 20484;
	public static final int WhiteBalance = 20485;
	public static final int RGBGain = 20486;
	public static final int FStop = 20487;
	public static final int FocalLength = 20488;
	public static final int FocusDistance = 20489;
	public static final int FocusMode = 20490;
	public static final int ExposureMeteringMode = 20491;
	public static final int FlashMode = 20492;
	public static final int ExposureTime = 20493;
	public static final int ExposureProgramMode = 20494;
	public static final int ExposureIndex = 20495;
	public static final int ExposureBiasCompensation = 20496;
	public static final int DateTime = 20497;
	public static final int CaptureDelay = 20498;
	public static final int StillCaptureMode = 20499;
	public static final int Contrast = 20500;
	public static final int Sharpness = 20501;
	public static final int DigitalZoom = 20502;
	public static final int EffectMode = 20503;
	public static final int BurstNumber = 20504;
	public static final int BurstInterval = 20505;
	public static final int TimelapseNumber = 20506;
	public static final int TimelapseInterval = 20507;
	public static final int FocusMeteringMode = 20508;
	public static final int UploadURL = 20509;
	public static final int Artist = 20510;
	public static final int CopyrightInfo = 20511;
	public static final int SupportedStreams = 20512;
	public static final int EnabledStreams = 20513;
	public static final int VideoFormat = 20514;
	public static final int VideoResolution = 20515;
	public static final int VideoQuality = 20516;
	public static final int VideoFrameRate = 20517;
	public static final int VideoContrast = 20518;
	public static final int VideoBrightness = 20519;
	public static final int AudioFormat = 20520;
	public static final int AudioBitrate = 20521;
	public static final int AudioSamplingRate = 20522;
	public static final int AudioBitPerSample = 20523;
	public static final int AudioVolume = 20524;
	
	static NameMap[] names = { new NameMap(20481, "BatteryLevel"), new NameMap(20482, "FunctionalMode"),
			new NameMap(20483, "ImageSize"), new NameMap(20484, "CompressionSetting"),
			new NameMap(20485, "WhiteBalance"), new NameMap(20486, "RGBGain"), new NameMap(20487, "FStop"),
			new NameMap(20488, "FocalLength"), new NameMap(20489, "FocusDistance"), new NameMap(20490, "FocusMode"),
			new NameMap(20491, "ExposureMeteringMode"), new NameMap(20492, "FlashMode"),
			new NameMap(20493, "ExposureTime"), new NameMap(20494, "ExposureProgramMode"),
			new NameMap(20495, "ExposureIndex"), new NameMap(20496, "ExposureBiasCompensation"),
			new NameMap(20497, "DateTime"), new NameMap(20498, "CaptureDelay"), new NameMap(20499, "StillCaptureMode"),
			new NameMap(20500, "Contrast"), new NameMap(20501, "Sharpness"), new NameMap(20502, "DigitalZoom"),
			new NameMap(20503, "EffectMode"), new NameMap(20504, "BurstNumber"), new NameMap(20505, "BurstInterval"),
			new NameMap(20506, "TimelapseNumber"), new NameMap(20507, "TimelapseInterval"),
			new NameMap(20508, "FocusMeteringMode"), new NameMap(20509, "UploadURL"), new NameMap(20510, "Artist"),
			new NameMap(20511, "CopyrightInfo"), new NameMap(20512, "SupportedStreams"), new NameMap(20513, "EnabledStreams"),
			new NameMap(20514, "VideoFormat"), new NameMap(20515, "VideoResolution"), new NameMap(20516, "VideoQuality"),
			new NameMap(20517, "VideoFrameRate"), new NameMap(20518, "VideoContrast"), new NameMap(20519, "VideoBrightness"),
			new NameMap(20520, "AudioFormat"), new NameMap(20521, "AudioBitrate"), new NameMap(20522, "AudioSamplingRate"),
			new NameMap(20523, "AudioBitPerSample"), new NameMap(20524, "AudioVolume") };

	void bytes2Content() {
		this.propertyCode = nextU16();
		this.dataType = nextU16();
		this.writable = (nextU8() != 0);

		this.factoryDefault = DevicePropValue.get(this.dataType, this);
		this.currentValue = DevicePropValue.get(this.dataType, this);

		this.formType = nextU8();
		switch (this.formType) {
		case 0:
			break;
		case 1:
			this.constraints = new Range(this.dataType, this);
			break;
		case 2:
			this.constraints = parseEnumeration();
			break;
		default:
			System.err.println("ILLEGAL prop desc form, " + this.formType);
			this.formType = 0;
		}
	}

	public void print() {
		Common.output(getPropertyName(this.propertyCode));
		Common.output(" = ");
		Common.output(this.currentValue);
		if (!this.writable)
			Common.output(", read-only");
		Common.output(", ");
		Common.output(DevicePropValue.getTypeName(this.dataType));
		Object localObject;
		switch (this.formType) {
		case 0:
			break;
		case 1:
			localObject = (Range) this.constraints;
			Common.output(" from ");
			Common.output(((Range) localObject).getMinimum());
			Common.output(" to ");
			Common.output(((Range) localObject).getMaximum());
			Common.output(" by ");
			Common.output(((Range) localObject).getIncrement());

			break;
		case 2:
			localObject = (Vector) this.constraints;
			Common.output(" { ");
			for (int i = 0; i < ((Vector) localObject).size(); i++) {
				if (i != 0)
					Common.output(", ");
				Common.output(((Vector) localObject).elementAt(i));
			}
			Common.output(" }");

			break;
		default:
			Common.output(" form ");
			Common.output(this.formType);
			Common.output(" (error)");
		}

		Common.output(", default ");
		Common.outputln(this.factoryDefault);
	}

	public boolean isWritable() {
		return this.writable;
	}

	public Object getValue() {
		return this.currentValue;
	}

	public Object getDefault() {
		return this.factoryDefault;
	}

	public String getCodeName(int paramInt) {
		return getPropertyName(paramInt);
	}

	public static String getPropertyName(int paramInt) {
		for (int i = 0; i < names.length; i++)
			if (names[i].value == paramInt)
				return names[i].name;
		return Container.getCodeString(paramInt);
	}

	public static int getPropertyCode(String paramString) {
		for (int i = 0; i < names.length; i++) {
			if (names[i].name.equalsIgnoreCase(paramString)) {
				return names[i].value;
			}
		}
		return Integer.parseInt(paramString, 16);
	}

	public Range getRange() {
		if (this.formType == 1)
			return (Range) this.constraints;
		return null;
	}

	private Vector parseEnumeration() {
		int i = nextU16();
		Vector<Object> localVector = new Vector<Object>(i);

		while (i-- > 0)
			localVector.addElement(DevicePropValue.get(this.dataType, this));
		return localVector;
	}

	public Vector getEnumeration() {
		if (this.formType == 2)
			return (Vector) this.constraints;
		return null;
	}

	public static final class Range {
		private Object min;
		private Object max;
		private Object step;

		Range(int index, DevicePropDesc devicePropDesc) {
			this.min = DevicePropValue.get(index, devicePropDesc);
			this.max = DevicePropValue.get(index, devicePropDesc);
			this.step = DevicePropValue.get(index, devicePropDesc);
		}

		public Object getMaximum() {
			return this.max;
		}

		public Object getMinimum() {
			return this.min;
		}

		public Object getIncrement() {
			return this.step;
		}
	}

	static class NameMap {
		int value;
		String name;

		NameMap(int paramInt, String paramString) {
			this.value = paramInt;
			this.name = paramString;
		}
	}
}