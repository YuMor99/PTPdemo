package test;

import jp.co.gitaku.ptp.*;
import jp.co.gitaku.ptpip.PTPIPInitiator;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.bytedeco.javacv.*;
public class testInitiate {
	PTPIPInitiator piInit;

	public static void main(String args[]) {
		init();
		//曾被注释掉
		 testInitiate test = new testInitiate();
		 test.testCmd();
		 //曾被注释掉
	}

	void testCmd() {
		int[] guid = { 0xe0, 0xcf, 0x20, 0x3a, 0xc3, 0x2a, 0x1b, 0x41, 0x98, 0x10, 0x0a, 0x93, 0x88, 0xec, 0x53, 0x6c };
//		String ip = "192.168.1.131";
//      曾经是192.168.1.121
		String ip = "192.168.1.104";
		// /192.168.1.131
		try {
			piInit = new PTPIPInitiator(guid, "aaa", ip);
			//piInit.openSession();
			//System.out.println(2);
			piInit.getDeviceInfo();
			System.out.println(3);
			piInit.takePhoto();
			// piInit.getDeviceInfo();
			// piInit.closeSession();
			// int[] storIDs = piInit.getStorageIDs();
			// for (int storID : storIDs) {
			// StorageInfo storInfo = piInit.getStorageInfo(storID);
			// storInfo.print();
			// }
			// System.out.println(piInit.getNumObjects(65537, 0, 0));
			int[] handles = piInit.getObjectHandles(-1, ObjectInfo.EXIF_JPEG, 0);
			// // // 36 66332 131108 196644 262148 327684
			for (int handle : handles) {
				System.out.println(handle);
				ObjectInfo objInfo = piInit.getObjectInfo(handle);
				objInfo.print();
			}
			// File file = new File("E:\\PTPIP\\1.JPG");
			// FileOutputStream out = new FileOutputStream(file);
			// Data data = piInit.getThumb(327684);
			// out.write(data.getData());
			// out.close();
			// System.out.println(piInit.getThumb(393220));
			// piInit.deleteObject(327684, 0);
			// System.out.println(piInit.getObjectHandles(-1, 0, 0).length);
			// piInit.initiateCapture(0, 0);
			// sendFile("E:\\PTPIP\\12.jpg");
			// int[] handles1 = piInit.getObjectHandles(-1,
			// ObjectInfo.EXIF_JPEG, 0);
			// for (int handle : handles1) {
			// ObjectInfo objInfo = piInit.getObjectInfo(handle);
			// objInfo.print();
			// }
			// piInit.getDevicePropDesc(0xd001).print();
			// piInit.getDevicePropDesc(0xd002).print();
			// piInit.getDevicePropDesc(0xd003).print();
			// piInit.getDevicePropDesc(0xd005).print();
			// piInit.getDevicePropDesc(0xd102).print();
			// piInit.getDevicePropDesc(0xd161).print();
			// System.out.println(piInit.getDevicePropValue(6,
			// 0xd001).getValue());
			// DevicePropValue newValue = new DevicePropValue(6, 4L);
			// piInit.setDevicePropValue(0xd002, newValue);
			// piInit.getDevicePropDesc(0xd002).print();
			// int[] handles1 = piInit.getObjectHandles(-1,
			// ObjectInfo.EXIF_JPEG, 0);
			// for (int handle : handles1) {
			// ObjectData data = new ObjectData();
			// int actualNum = piInit.getPartialObject(data, handle, 0, 9);
			// System.out.println();
			// System.out.println(actualNum);
			// System.out.println(data.getLength());
			// for (int i = 0; i < actualNum; i++) {
			// System.out.print(data.getData()[i] + " ");
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void init() {
		JmDNS jmdns = null;
		try {
			jmdns = JmDNS.create();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		jmdns.addServiceListener("_ptp._tcp.local.", new Listener());
	}

	int sendFile(String filePath) throws Exception {
		Object obj;
		File localFile = new File(filePath);

		if (!localFile.exists())
			throw new Exception("not a file");
		if (localFile.isDirectory()) {
			System.err.println("... can't send directory yet: " + filePath);
			return -1;
		}

		obj = localFile.getAbsolutePath();
		if (File.separatorChar != '/')
			obj = ((String) obj).replace(File.separatorChar, '/');
		if (!((String) obj).startsWith("/"))
			obj = "/" + (String) obj;
		filePath = ("file:" + (String) obj);

		System.out.print("sendFile ");
		System.out.print(filePath);
		System.out.println(" ");
		try {
			URL localURL = new URL(filePath);
			try {
				obj = localURL.openConnection();
				ObjectInfo localObjectInfo = new ObjectInfo((URLConnection) obj, piInit.getDeviceInfo());
				FileSendData fsd = new FileSendData((URLConnection) obj);

				Response res = piInit.sendObjectInfo(localObjectInfo, 0, 0);
				if (Response.OK == res.getCode()) {
					System.out.println("StorageID:" + res.getParam1());
					System.out.println("Parent ObjectHandle:" + res.getParam2());
					System.out.println("ObjectHandle:" + res.getParam3());
					piInit.sendObject(fsd);
				} else {
					throw new Exception(res.toString());
				}

			} catch (IOException localIOException) {
				System.out.println("\n\t... IOException, " + localIOException.getMessage());

				throw localIOException;
			}
		} catch (MalformedURLException localMalformedURLException) {
			System.out.println("\n\t... not a file or URL:  " + filePath);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			System.out.println("\n\t... can't send, " + localIllegalArgumentException.getMessage());
		}
		return 1;
	}
}

class Listener implements ServiceListener {

	@Override
	public void serviceAdded(ServiceEvent event) {
	}

	@Override
	public void serviceRemoved(ServiceEvent event) {
	}

	@Override
	public void serviceResolved(ServiceEvent event) {
		ServiceInfo info = event.getInfo();

		int[] guid = { 0xe0, 0xcf, 0x20, 0x3a, 0xc3, 0x2a, 0x1b, 0x41, 0x98, 0x10, 0x0a, 0x93, 0x88, 0xec, 0x53, 0x6c };
		String ip = "192.168.1.121";
		// /192.168.1.131
		ip = info.getInetAddresses()[0].toString().split("/")[1];
		System.out.println(ip);
		// 203e4f68-2423-4b46-9868-157593b63ef3
		// int[] guid = {0x20, 0x3e, 0x4f, 0x68, 0x24, 0x23, 0x4b, 0x46, 0x98,
		// 0x68, 0x15, 0x75, 0x93, 0xb6, 0x3e, 0xf3};
		// String ip = "192.168.1.88";
		//	e0:cf:20:3a:c3:2a:1b:41:98:10:0a:93:88:ec:53:6c:58:00:50:00:2d:00:50:00:43
		try {
			PTPIPInitiator piInit = new PTPIPInitiator(guid, "aaa", ip);
			piInit.addListeners(new testListener());
			System.out.println("connect success");
			piInit.openSession();
			// DeviceInfo devInfo = piInit.getDeviceInfo();
			// devInfo.print();

			// piInit.closeSession();
			// piInit.getStorageIDs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}