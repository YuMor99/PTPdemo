package jp.co.gitaku.ptp;

public class Command extends ParamVector {
	public static final int GetDeviceInfo = 4097;
	public static final int OpenSession = 4098;
	public static final int CloseSession = 4099;
	public static final int GetStorageIDs = 4100;
	public static final int GetStorageInfo = 4101;
	public static final int GetNumObjects = 4102;
	public static final int GetObjectHandles = 4103;
	public static final int GetObjectInfo = 4104;
	public static final int GetObject = 4105;
	public static final int GetThumb = 4106;
	public static final int DeleteObject = 4107;
	public static final int SendObjectInfo = 4108;
	public static final int SendObject = 4109;
	public static final int InitiateCapture = 4110;
	public static final int FormatStore = 4111;
	public static final int ResetDevice = 4112;
	public static final int SelfTest = 4113;
	public static final int SetObjectProtection = 4114;
	public static final int PowerDown = 4115;
	public static final int GetDevicePropDesc = 4116;
	public static final int GetDevicePropValue = 4117;
	public static final int SetDevicePropValue = 4118;
	public static final int ResetDevicePropValue = 4119;
	public static final int TerminateOpenCapture = 4120;
	public static final int MoveObject = 4121;
	public static final int CopyObject = 4122;
	public static final int GetPartialObject = 4123;
	public static final int InitiateOpenCapture = 4124;
	public static final int StartEnumHandles = 4125;
	public static final int EnumHandles = 4126;
	public static final int StopEnumHandles = 4127;
	public static final int GetVendorExtensionMaps = 4128;
	public static final int GetVendorDeviceInfo = 4129;
	public static final int GetResizedImageObject = 4130;
	public static final int GetFilesystemManifest = 4131;
	public static final int GetStreamInfo = 4132;
	public static final int GetStream = 4133;
	//added here
	public static final int TakePhoto=4144;
	
	public Command(int operationCode, Session paramSession, int param1, int param2, int param3, int param4, int param5) {
		super(operationCode, paramSession, param1, param2, param3, param4, param5);
	}

	public String getCodeName(int paramInt) {
		return getOpcodeString(paramInt);
	}

	public static String getOpcodeString(int paramInt) {
		switch (paramInt) {
			case TakePhoto:
				return "TakePhoto";
		case GetDeviceInfo:
			return "GetDeviceInfo";
		case OpenSession:
			return "OpenSession";
		case CloseSession:
			return "CloseSession";
		case GetStorageIDs:
			return "GetStorageIDs";
		case GetStorageInfo:
			return "GetStorageInfo";
		case GetNumObjects:
			return "GetNumObjects";
		case GetObjectHandles:
			return "GetObjectHandles";
		case GetObjectInfo:
			return "GetObjectInfo";
		case GetObject:
			return "GetObject";
		case GetThumb:
			return "GetThumb";
		case DeleteObject:
			return "DeleteObject";
		case SendObjectInfo:
			return "SendObjectInfo";
		case SendObject:
			return "SendObject";
		case InitiateCapture:
			return "InitiateCapture";
		case FormatStore:
			return "FormatStore";
		case ResetDevice:
			return "ResetDevice";
		case SelfTest:
			return "SelfTest";
		case SetObjectProtection:
			return "SetObjectProtection";
		case PowerDown:
			return "PowerDown";
		case GetDevicePropDesc:
			return "GetDevicePropDesc";
		case GetDevicePropValue:
			return "GetDevicePropValue";
		case SetDevicePropValue:
			return "SetDevicePropValue";
		case ResetDevicePropValue:
			return "ResetDevicePropValue";
		case TerminateOpenCapture:
			return "TerminateOpenCapture";
		case MoveObject:
			return "MoveObject";
		case CopyObject:
			return "CopyObject";
		case GetPartialObject:
			return "GetPartialObject";
		case InitiateOpenCapture:
			return "InitiateOpenCapture";
		case StartEnumHandles:
			return "StartEnumHandles";
		case EnumHandles:
			return "EnumHandles";
		case StopEnumHandles:
			return "StopEnumHandles";
		case GetVendorExtensionMaps:
			return "GetVendorExtensionMaps";
		case GetVendorDeviceInfo:
			return "GetVendorDeviceInfo";
		case GetResizedImageObject:
			return "GetResizedImageObject";
		case GetFilesystemManifest:
			return "GetFilesystemManifest";
		case GetStreamInfo:
			return "GetStreamInfo";
		case GetStream:
			return "GetStream";
		}
		return Container.getCodeString(paramInt);
	}
	
	public int getDataPhaseInfo() {
		switch (this.getCode()) {
			case TakePhoto:
				return 1;
		case GetDeviceInfo:
			return 1;
		case OpenSession:
			return 1;
		case CloseSession:
			return 1;
		case GetStorageIDs:
			return 1;
		case GetStorageInfo:
			return 1;
		case GetNumObjects:
			return 1;
		case GetObjectHandles:
			return 1;
		case GetObjectInfo:
			return 1;
		case GetObject:
			return 1;
		case GetThumb:
			return 1;
		case DeleteObject:
			return 1;
		case SendObjectInfo:
			return 2;
		case SendObject:
			return 2;
		case InitiateCapture:
			return 1;
		case FormatStore:
			return 1;
		case ResetDevice:
			return 1;
		case SelfTest:
			return 1;
		case SetObjectProtection:
			return 1;
		case PowerDown:
			return 1;
		case GetDevicePropDesc:
			return 1;
		case GetDevicePropValue:
			return 1;
		case SetDevicePropValue:
			return 2;
		case ResetDevicePropValue:
			return 1;
		case TerminateOpenCapture:
			return 1;
		case MoveObject:
			return 1;
		case CopyObject:
			return 1;
		case GetPartialObject:
			return 1;
		case InitiateOpenCapture:
			return 1;
		case StartEnumHandles:
			return 1;
		case EnumHandles:
			return 1;
		case StopEnumHandles:
			return 1;
		case GetVendorExtensionMaps:
			return 1;
		case GetVendorDeviceInfo:
			return 1;
		case GetResizedImageObject:
			return 1;
		case GetFilesystemManifest:
			return 1;
		case GetStreamInfo:
			return 1;
		case GetStream:
			return 1;
		}
		return 1;
	}
}