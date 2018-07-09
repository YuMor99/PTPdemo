package test;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.gitaku.ptp.BaselineInitiator;
import jp.co.gitaku.ptp.Data;
import jp.co.gitaku.ptp.Event;
import jp.co.gitaku.ptp.ObjectInfo;
import jp.co.gitaku.ptpip.EventPacket;
import jp.co.gitaku.ptpip.PTPIPEventListener;

public class testListener implements PTPIPEventListener {
	//private static String path = "E:\\PTPIP\\test\\";
	private static String path = "D:\\PTPIP\\test\\";
	@Override
	public void processEvent(BaselineInitiator initiator, EventPacket event) {
		String eventCode = Event.getEventString(event.getEventCode());
		System.out.println("Event Code: " + eventCode);
		System.out.println("Transaction ID: " + event.getTransactionID());
		System.out.println("Parameter 1: " + event.getParam1());
		System.out.println("Parameter 2: " + event.getParam2());
		System.out.println("Parameter 3: " + event.getParam3());
		if ("c101".equals(eventCode)) {
			try {
				int[] handles = initiator.getObjectHandles(-1, ObjectInfo.EXIF_JPEG, 0);
				int handle = handles[handles.length - 1];
				System.out.println("Object Handle: " + eventCode);
				getFile(initiator, handle);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("c103".equals(eventCode)) {
			try {
				getFile(initiator, event.getParam1());
				System.out.println("process OK");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	void getFile(BaselineInitiator initiator, int handle) throws Exception {
		ObjectInfo objInfo = initiator.getObjectInfo(handle);
		Data data = initiator.getObject(handle);
//		String suffixDate = "";
//		suffixDate = "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//		File file = new File(path + objInfo.getFilename() + suffixDate);
		File file = new File(path + objInfo.getFilename());
		FileOutputStream out = new FileOutputStream(file);
		out.write(data.getData());
		out.close();
	}
}
