package jp.co.gitaku.ptpip;

import java.net.Socket;
import java.net.UnknownHostException;

import jp.co.gitaku.util.Common;

public class PTPIPConn {
	private String host;
	private int port = 15740;
	private Socket cdConn;
	private Socket evtConn;
	private int connNumber;

	public PTPIPConn() {
	}

	public PTPIPConn(String host, int port) {
		// host check
		if (host != null && host.length() > 0) {
			this.host = host;
		} else {
			throw new IllegalArgumentException("host can not be null!");
		}
		this.port = port;
	}

	// initiate PIP-IP Connection
	public void initCdConn() throws Exception {
		try {
			cdConn = new Socket(host, port);
		} catch (UnknownHostException e) {
			throw new IllegalAccessError("device can not be found!");
		} catch (Exception e) {
			if (cdConn != null) {
				cdConn.close();
			}
			throw e;
		}
	}

	public void initEvtConn() throws Exception {
		try {
			evtConn = new Socket(host, port);
		} catch (UnknownHostException e) {
			throw new IllegalAccessError("device can not be found!");
		} catch (Exception e) {
			if (evtConn != null) {
				evtConn.close();
			}
			throw e;
		}
	}

	public byte[] sendPacketWithRet(PTPIPPacket piPacket) throws Exception {
		cdConn.getOutputStream().write(piPacket.getData());
		return Common.stream2byte(cdConn.getInputStream());
	}

	public void sendPacket(PTPIPPacket piPacket) throws Exception {
		cdConn.getOutputStream().write(piPacket.getData());
	}
	
	public byte[] sendEvtRequest(PTPIPPacket piPacket) throws Exception {
		evtConn.getOutputStream().write(piPacket.getData());
		return Common.stream2byte(evtConn.getInputStream());
	}

	public byte[] readContent() throws Exception {
		return Common.stream2byte(cdConn.getInputStream());
	}

	public void close() {
		try {
			if (cdConn != null) {
				cdConn.close();
			}
			if (evtConn != null) {
				evtConn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getConnNumber() {
		return connNumber;
	}

	public void setConnNumber(int connNumber) {
		this.connNumber = connNumber;
	}

	public Socket getCdConn() {
		return cdConn;
	}

	public void setCdConn(Socket cdConn) {
		this.cdConn = cdConn;
	}

	public Socket getEvtConn() {
		return evtConn;
	}

	public void setEvtConn(Socket evtConn) {
		this.evtConn = evtConn;
	}
}
