package jp.co.gitaku.ptp;

public class Response extends ParamVector {
	public static final int Undefined = 8192;
	public static final int OK = 8193;
	public static final int GeneralError = 8194;
	public static final int SessionNotOpen = 8195;
	public static final int InvalidTransactionID = 8196;
	public static final int OperationNotSupported = 8197;
	public static final int ParameterNotSupported = 8198;
	public static final int IncompleteTransfer = 8199;
	public static final int InvalidStorageID = 8200;
	public static final int InvalidObjectHandle = 8201;
	public static final int DevicePropNotSupported = 8202;
	public static final int InvalidObjectFormatCode = 8203;
	public static final int StoreFull = 8204;
	public static final int ObjectWriteProtected = 8205;
	public static final int StoreReadOnly = 8206;
	public static final int AccessDenied = 8207;
	public static final int NoThumbnailPresent = 8208;
	public static final int SelfTestFailed = 8209;
	public static final int PartialDeletion = 8210;
	public static final int StoreNotAvailable = 8211;
	public static final int SpecificationByFormatUnsupported = 8212;
	public static final int NoValidObjectInfo = 8213;
	public static final int InvalidCodeFormat = 8214;
	public static final int UnknownVendorCode = 8215;
	public static final int CaptureAlreadyTerminated = 8216;
	public static final int DeviceBusy = 8217;
	public static final int InvalidParentObject = 8218;
	public static final int InvalidDevicePropFormat = 8219;
	public static final int InvalidDevicePropValue = 8220;
	public static final int InvalidParameter = 8221;
	public static final int SessionAlreadyOpen = 8222;
	public static final int TransactionCanceled = 8223;
	public static final int SpecificationOfDestinationUnsupported = 8224;
	public static final int InvalidEnumID = 8225;
	public static final int Nostreamenabled = 8226;
	public static final int Invaliddataset = 8227;

	public Response(byte[] data) {
		super(data);
	}

	public Response(byte[] data, int paramInt) {
		super(data, paramInt);
	}
	
	public Response(int responseCode, Session paramSession, int param1, int param2, int param3, int param4, int param5) {
		super(responseCode, paramSession, param1, param2, param3, param4, param5);
	}

	public String getCodeName(int paramInt) {
		return getResponseString(paramInt);
	}

	public static String getResponseString(int paramInt) {
		switch (paramInt) {
		case 8192:
			return "Undefined";
		case 8193:
			return "OK";
		case 8194:
			return "GeneralError";
		case 8195:
			return "SessionNotOpen";
		case 8196:
			return "InvalidTransactionID";
		case 8197:
			return "OperationNotSupported";
		case 8198:
			return "ParameterNotSupported";
		case 8199:
			return "IncompleteTransfer";
		case 8200:
			return "InvalidStorageID";
		case 8201:
			return "InvalidObjectHandle";
		case 8202:
			return "DevicePropNotSupported";
		case 8203:
			return "InvalidObjectFormatCode";
		case 8204:
			return "StoreFull";
		case 8205:
			return "ObjectWriteProtected";
		case 8206:
			return "StoreReadOnly";
		case 8207:
			return "AccessDenied";
		case 8208:
			return "NoThumbnailPresent";
		case 8209:
			return "SelfTestFailed";
		case 8210:
			return "PartialDeletion";
		case 8211:
			return "StoreNotAvailable";
		case 8212:
			return "SpecificationByFormatUnsupported";
		case 8213:
			return "NoValidObjectInfo";
		case 8214:
			return "InvalidCodeFormat";
		case 8215:
			return "UnknownVendorCode";
		case 8216:
			return "CaptureAlreadyTerminated";
		case 8217:
			return "DeviceBusy";
		case 8218:
			return "InvalidParentObject";
		case 8219:
			return "InvalidDevicePropFormat";
		case 8220:
			return "InvalidDevicePropValue";
		case 8221:
			return "InvalidParameter";
		case 8222:
			return "SessionAlreadyOpen";
		case 8223:
			return "TransactionCanceled";
		case 8224:
			return "SpecificationOfDestinationUnsupported";
		case 8225:
			return "InvalidEnumID";
		case 8226:
			return "Nostreamenabled";
		case 8227:
			return "Invaliddataset";
		}
		return Container.getCodeString(paramInt);
	}
}