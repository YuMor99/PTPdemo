package jp.co.gitaku.ptpip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.co.gitaku.ptp.Command;
import jp.co.gitaku.ptp.DeviceInfo;
import jp.co.gitaku.ptp.Response;
import jp.co.gitaku.util.Common;

import javax.imageio.stream.FileImageInputStream;

public class PTPIPResponder {

	private ServerSocket serverSocket;
	// guid
	private int[] guid;
	// friendly Name
	private String friendlyName;
	// protocol Version
	private int protocolVersion = 65536;

	private Map<Integer, PTPIPConn> conns = new HashMap<Integer, PTPIPConn>();
	private static int connNumber = 0;

	public PTPIPResponder(int[] guid, String friendlyName, int port) throws IOException {
		this.guid = guid;
		this.friendlyName = friendlyName;
		serverSocket = new ServerSocket(port);
	}

	public PTPIPResponder(int[] guid, String friendlyName) throws IOException {
		this(guid, friendlyName, 15740);
	}

	public void service() {
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				Thread workThread = new Thread(new Handler(socket));
				workThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendEvent(EventPacket event) throws Exception {
		// TODO:
		if (conns == null || conns.size() == 0) {
			return;
		}
		Iterator<Map.Entry<Integer, PTPIPConn>> iter = conns.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, PTPIPConn> entry = iter.next();
			PTPIPConn conn = entry.getValue();
			Socket socket = conn.getEvtConn();
			if (socket == null) {
				continue;
			}
			socket.getOutputStream().write(event.getData());
		}
	}

	class Handler implements Runnable {
		private Socket socket;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				while (true) {
					byte[] inByte = Common.stream2byte(socket.getInputStream());
					PTPIPPacket inPacket = new PTPIPPacket(inByte);
					if (inPacket.getPacketType() == PTPIPPacket.InitCommandRequestPacket) {
						PTPIPConn conn = new PTPIPConn();
						conn.setConnNumber(connNumber);
						conn.setCdConn(socket);
						InitCommandAckPacket ica = new InitCommandAckPacket(connNumber, guid, friendlyName,
								protocolVersion);
						socket.getOutputStream().write(ica.getData());
						conns.put(connNumber++, conn);
						System.out.println("cd connection init success!");
					} else if (inPacket.getPacketType() == PTPIPPacket.InitEventRequestPacket) {
						InitEventRequestPacket ierp = new InitEventRequestPacket(inByte);
						InitEventAckPacket ieap = new InitEventAckPacket();
						socket.getOutputStream().write(ieap.getData());

						int connNum = ierp.getConnectionNumber();
						PTPIPConn conn = conns.get(connNum);
						if (conn == null) {
							throw new IllegalStateException("cd connection can not be found!");
						}
						conn.setEvtConn(socket);
						System.out.println("evt connection init success!");
					} else if (inPacket.getPacketType() == PTPIPPacket.OperationRequestPacket) {
						try {
							exeOperation(new OperationRequestPacket(inByte));
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						// TODO:
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (socket != null)
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void exeOperation(OperationRequestPacket orp) throws Exception {
			if (orp.getDataPhaseInfo() == OperationRequestPacket.NoDataOrDataIn) {
				if (orp.getOperationCode() == Command.GetDeviceInfo) {
					// device info TODO:
					System.out.println("get the command to get device info");
					DeviceInfo deviceInfo = new DeviceInfo(true, 
													1,
													2,
													3,
													"test1",
													4,
													new int[] {4097, 4098},
													new int[] {16385, 16386},
													new int[] {20481, 20482},
													new int[] {12289, 12290},
													new int[] {12289, 12290},
													"test2",
													"test3",
													"test4",
													"test5");
					// TODO:total data length = ?
					StartDataPacket start = new StartDataPacket(orp.getTransactionID(), deviceInfo.getLength());
					socket.getOutputStream().write(start.getData());
					DataPacket data = new DataPacket(orp.getTransactionID(), deviceInfo.getData());
					socket.getOutputStream().write(data.getData());
					EndDataPacket end = new EndDataPacket(orp.getTransactionID(), null);
					socket.getOutputStream().write(end.getData());
					OperationResponsePacket res = new OperationResponsePacket(Response.OK, orp.getTransactionID(), 0, 0, 0, 0, 0);
					socket.getOutputStream().write(res.getData());
				}
				else if(orp.getOperationCode()==Command.TakePhoto){
					// TODO
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
					System.out.println("I get the command to take a photo, time:"+date);
					System.out.println("Photo transfer.......");
					System.out.println("save the photo in D:/ptpdemo/, the file name is demo_5733.jpg");
					DataPacket res = new DataPacket(orp.getTransactionID(), image2byte("demo.jpg")); //D:\PTPIP\
					socket.getOutputStream().write(res.getData());
				}
				else if(orp.getOperationCode()==Command.OpenSession){

				}
				else{

				}
			} else if (orp.getDataPhaseInfo() == OperationRequestPacket.DataOut) {
				// TODO:
			} else {
				// TODO:
			}
		}
	}
	//图片到byte数组
	public byte[] image2byte(String path){
		byte[] data = null;
		FileImageInputStream input = null;
		try {
			input = new FileImageInputStream(new File(path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numBytesRead = 0;
			while ((numBytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, numBytesRead);
			}
			data = output.toByteArray();
			output.close();
			input.close();
		}
		catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		}
		catch (IOException ex1) {
			ex1.printStackTrace();
		}
		return data;
	}
}
