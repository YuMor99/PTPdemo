package jp.co.gitaku.ptpip;

import java.io.IOException;
import java.net.Socket;

import jp.co.gitaku.ptp.DataStream;
import jp.co.gitaku.ptp.ObjectData;
import jp.co.gitaku.ptp.Response;
import jp.co.gitaku.util.Common;


public class PTPIPDataStream extends DataStream {
	private Socket socket;
	private long totalDataLength;
	private long readIndex;
	
	public PTPIPDataStream(Socket socket, long totalDataLength) {
		this.socket = socket;
		this.totalDataLength = totalDataLength;
	}
	
	public int read(ObjectData data) throws IOException {
		int result = 0;
		byte[] content = Common.stream2byte(socket.getInputStream());
		
		int packetType = Common.getPacketType(content);
		if (packetType == PTPIPPacket.DataPacket) {
			DataPacket dataPacket = new DataPacket(content);
			data.setData(dataPacket.getDataPayload());
			readIndex += dataPacket.getDataPayload().length;
			result = NORMAL_DATA;
		} else if (packetType == PTPIPPacket.EndDataPacket) {
			EndDataPacket end = new EndDataPacket(content);
			if (readIndex >= totalDataLength || end.getDataPayload() == null || end.getDataPayload().length == 0) {
				result = END_WITHOUT_DATA;
			} else {
				data.setData(end.getDataPayload());
				result = END_WITH_DATA;
			}
			byte[] ret = Common.stream2byte(socket.getInputStream());
			OperationResponsePacket orp = new OperationResponsePacket(ret);
			if (orp.getResponseCode() != Response.OK) {
				throw new IllegalStateException(Response.getCodeString(orp.getResponseCode()));
			}
		} else {
			throw new IllegalStateException("packet error!");
		}
		return result;
	}
}
