package jp.co.gitaku.ptp;

public class Event extends ParamVector {
	public static final int Undefined = 16384;
	public static final int CancelTransaction = 16385;
	public static final int ObjectAdded = 16386;
	public static final int ObjectRemoved = 16387;
	public static final int StoreAdded = 16388;
	public static final int StoreRemoved = 16389;
	public static final int DevicePropChanged = 16390;
	public static final int ObjectInfoChanged = 16391;
	public static final int DeviceInfoChanged = 16392;
	public static final int RequestObjectTransfer = 16393;
	public static final int StoreFull = 16394;
	public static final int DeviceReset = 16395;
	public static final int StorageInfoChanged = 16396;
	public static final int CaptureComplete = 16397;
	public static final int UnreportedStatus = 16398;

	Event(int operationCode, Session paramSession, int param1, int param2, int param3) {
		super(operationCode, paramSession, param1, param2, param3);
	}

	public String getCodeName(int paramInt) {
		return getEventString(paramInt);
	}

	public static String getEventString(int paramInt) {
		switch (paramInt) {
		case 16384:
			return "Undefined";
		case 16385:
			return "CancelTransaction";
		case 16386:
			return "ObjectAdded";
		case 16387:
			return "ObjectRemoved";
		case 16388:
			return "StoreAdded";
		case 16389:
			return "StoreRemoved";
		case 16390:
			return "DevicePropChanged";
		case 16391:
			return "ObjectInfoChanged";
		case 16392:
			return "DeviceInfoChanged";
		case 16393:
			return "RequestObjectTransfer";
		case 16394:
			return "StoreFull";
		case 16395:
			return "DeviceReset";
		case 16396:
			return "StorageInfoChanged";
		case 16397:
			return "CaptureComplete";
		case 16398:
			return "UnreportedStatus";
		}
		return Container.getCodeString(paramInt);
	}
}