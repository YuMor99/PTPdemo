package jp.co.gitaku.ptpip;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import jp.co.gitaku.ptp.BaselineInitiator;
import jp.co.gitaku.ptp.Command;
import jp.co.gitaku.ptp.Data;
import jp.co.gitaku.ptp.DataStream;
import jp.co.gitaku.ptp.FileSendData;
import jp.co.gitaku.ptp.Response;
import jp.co.gitaku.util.Common;

public class PTPIPInitiator extends BaselineInitiator {
	// PTPIP Connection
	private PTPIPConn piConn;
	// GUID
	private int[] guid;
	// friendly Name
	private String friendlyName;
	// protocol Version
	private int protocolVersion = 65536;
	// Connection Number
	private int ConnectionNumber;
	// responder GUID
	private int[] responderGUID;
	// Responder Friendly Name
	private String responderFriendlyName;
	// Responder Protocol Version
	private int responderProtocolVersion;
	// event listener
	private List<PTPIPEventListener> listeners = new ArrayList<PTPIPEventListener>();

	public PTPIPInitiator(int[] guid, String friendlyName, String targetDevIP) throws Exception {
		this(guid, friendlyName, targetDevIP, 15740);
	}

	public PTPIPInitiator(int[] guid, String friendlyName, String targetDevIP, int port) throws Exception {
		// search devices
		this.guid = guid;
		this.friendlyName = friendlyName;
		init(targetDevIP, port);
	}

	private void init(String targetDevIP, int port) throws Exception {
		piConn = new PTPIPConn(targetDevIP, port);
		piConn.initCdConn();
		// create a Init Command Request Packet
		InitCommandRequestPacket icqp = new InitCommandRequestPacket(guid, friendlyName, protocolVersion);

		byte[] res = piConn.sendPacketWithRet(icqp);
		if (Common.getPacketType(res) == PTPIPPacket.InitCommandAckPacket) {
			setResponderInfo(new InitCommandAckPacket(res));
		} else {
			throw new IllegalAccessException("connection failed!");
		}

		// create a Init Event Request Packet
		InitEventRequestPacket ierp = new InitEventRequestPacket(this.ConnectionNumber);
		// event connection init
		piConn.initEvtConn();
		byte[] evtRes = piConn.sendEvtRequest(ierp);
		int packetType = Common.getPacketType(evtRes);
		if (packetType == PTPIPPacket.InitEventAckPacket) {
			System.out.println("connect success!");
			Thread evtThread = new Thread(new Handler(piConn.getEvtConn()));
			evtThread.start();
		} else if (packetType == PTPIPPacket.InitFailPacket) {
			InitFailPacket ifp = new InitFailPacket(evtRes);
			String reason = InitFailPacket.getReasonString(ifp.getReason());
			throw new IllegalAccessException(reason);
		}
	}

	public Response sendCommand(Command req, Data data) throws Exception {
		PTPIPPacket ptpipPacket = convertPacket(req);
		byte[] ret = piConn.sendPacketWithRet(ptpipPacket);
		if (Common.getPacketType(ret) == PTPIPPacket.StartDataPacket) {
			StartDataPacket start = new StartDataPacket(ret);
			long dataLength = start.getTotalDataLength();
			// TODO:
			byte[] dataTemp = new byte[(int) dataLength];
			int beginIndex = 0;
			while (true) {
				byte[] content = piConn.readContent();
				int packetType = Common.getPacketType(content);
				if (packetType == PTPIPPacket.DataPacket) {
					DataPacket dataPacket = new DataPacket(content);
					System.arraycopy(dataPacket.getDataPayload(), 0, dataTemp, beginIndex,
							dataPacket.getDataPayload().length);
					beginIndex += dataPacket.getDataPayload().length;
					continue;
				} else if (packetType == PTPIPPacket.EndDataPacket) {
					EndDataPacket end = new EndDataPacket(content);
					System.arraycopy(end.getDataPayload(), 0, dataTemp, beginIndex, (int) dataLength - beginIndex);
					continue;
				} else if (packetType == PTPIPPacket.OperationResponsePacket) {
					if (data != null) {
						data.setData(dataTemp);
					}
					OperationResponsePacket orp = new OperationResponsePacket(content);
					return convertPacket(req, orp);
				} else {
					throw new IllegalStateException("packet error!");
				}
			}

		} else if (Common.getPacketType(ret) == PTPIPPacket.OperationResponsePacket) {
			OperationResponsePacket orp = new OperationResponsePacket(ret);
			return convertPacket(req, orp);
		} else if(Common.getPacketType(ret) == PTPIPPacket.DataPacket){
			DataPacket dp=new DataPacket(ret);
			return convertPacket(req,dp);
		}
		else{
			throw new IllegalStateException("packet error!");
		}
	}

	public Response sendCommandWithData(Command req, Data data) throws Exception {
		PTPIPPacket ptpipPacket = convertPacket(req);
		piConn.sendPacket(ptpipPacket);

		StartDataPacket start = new StartDataPacket(req.getTransactionID(), data.getLength());
		piConn.sendPacket(start);
		byte[] endPayload = null;
		if (data instanceof FileSendData) {
			FileSendData fsd = (FileSendData) data;
			int remain = data.getLength();
			while (remain > 0) {
				byte[] sendData;
				if (remain >= FileSendData.TEMP_SIZE) {
					sendData = new byte[FileSendData.TEMP_SIZE];
					remain -= fsd.read(sendData, data.getLength() - remain, FileSendData.TEMP_SIZE);
					DataPacket dataPacket = new DataPacket(req.getTransactionID(), sendData);
					piConn.sendPacket(dataPacket);
				} else {
					endPayload = new byte[remain];
					remain -= fsd.read(endPayload, data.getLength() - remain, remain);
				}
			};
		} else {
			DataPacket dataPacket = new DataPacket(req.getTransactionID(), data.getData());
			piConn.sendPacket(dataPacket);
		}
		EndDataPacket end = new EndDataPacket(req.getTransactionID(), endPayload);
		byte[] ret = piConn.sendPacketWithRet(end);
		OperationResponsePacket orp = new OperationResponsePacket(ret);
		return convertPacket(req, orp);
	}

	public DataStream getStream(Command req) throws Exception {
		PTPIPPacket ptpipPacket = convertPacket(req);
		byte[] ret = piConn.sendPacketWithRet(ptpipPacket);
		if (Common.getPacketType(ret) == PTPIPPacket.StartDataPacket) {
			return new PTPIPDataStream(piConn.getCdConn(), new StartDataPacket(ret).getTotalDataLength());
		} else if (Common.getPacketType(ret) == PTPIPPacket.OperationResponsePacket) {
			OperationResponsePacket orp = new OperationResponsePacket(ret);
			Response res = convertPacket(req, orp);
			throw new IllegalStateException(res.getCodeName(res.getCode()));
		} else {
			throw new IllegalStateException("packet error!");
		}
	}

	private OperationRequestPacket convertPacket(Command comm) {
		OperationRequestPacket orp = new OperationRequestPacket(comm.getDataPhaseInfo(), comm.getCode(), comm
				.getTransactionID(), comm.getParam1(), comm.getParam2(), comm.getParam3(), comm.getParam4(), comm
				.getParam5());
		return orp;
	}

	private Response convertPacket(Command req, OperationResponsePacket orp) {
		Response response = new Response(orp.getResponseCode(), req.getSession(), orp.getParam1(), orp.getParam2(), orp
				.getParam3(), orp.getParam4(), orp.getParam5());
		return response;
	}
	private Response convertPacket(Command req, DataPacket dp) {
		Response response = new Response(dp.getDataPayload());
		return response;
	}
	private void setResponderInfo(InitCommandAckPacket initAckPacket) {
		this.responderGUID = initAckPacket.getGuid();
		this.responderFriendlyName = initAckPacket.getFriendlyName();
		this.responderProtocolVersion = initAckPacket.getVersion();
		this.ConnectionNumber = initAckPacket.getConnectionNumber();
	}

	/**
	 * @return the responderGUID
	 */
	public int[] getResponderGUID() {
		return responderGUID;
	}

	/**
	 * @return the responderFriendlyName
	 */
	public String getResponderFriendlyName() {
		return responderFriendlyName;
	}

	/**
	 * @return the responderProtocolVersion
	 */
	public int getResponderProtocolVersion() {
		return responderProtocolVersion;
	}

	/**
	 * @return the connectionNumber
	 */
	public int getConnectionNumber() {
		return ConnectionNumber;
	}

	public void addListeners(PTPIPEventListener listener) {
		listeners.add(listener);
	}

	class Handler implements Runnable {
		private Socket evtConn;

		public Handler(Socket evtConn) {
			this.evtConn = evtConn;

		}

		public void run() {
			try {
				while (true) {
					byte[] inByte = Common.stream2byte(evtConn.getInputStream());
					EventPacket evtPacket = new EventPacket(inByte);
					if (listeners != null && listeners.size() > 0) {
						for (int i = 0; i < listeners.size(); i++) {
							PTPIPEventListener listener = listeners.get(i);
							listener.processEvent(PTPIPInitiator.this, evtPacket);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (evtConn != null)
						evtConn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
