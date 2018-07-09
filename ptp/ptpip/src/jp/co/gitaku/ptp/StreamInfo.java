package jp.co.gitaku.ptp;

public class StreamInfo extends Data {
	long datasetSize;
	long timeResolution;
	int frameHeaderSize;
	int frameMaxSize;
	int packetHeaderSize;
	int packetMaxSize;
	int packetAlignment;

	public StreamInfo() {
		super(true, null, 0);
	}
	
	public StreamInfo(boolean isIn, byte[] data) {
		super(isIn, data);
		bytes2Content();
	}
	
	public StreamInfo(boolean isIn,
			long datasetSize,
	long timeResolution,
	int frameHeaderSize,
	int frameMaxSize,
	int packetHeaderSize,
	int packetMaxSize,
	int packetAlignment
				) {
		super(isIn, new byte[36]);
		
		this.datasetSize = datasetSize;
		this.timeResolution = timeResolution;
		this.frameHeaderSize = frameHeaderSize;
		this.frameMaxSize = frameMaxSize;
		this.packetHeaderSize = packetHeaderSize;
		this.packetMaxSize = packetMaxSize;
		this.packetAlignment = packetAlignment;
		content2Bytes();
	}

	public void content2Bytes() {
		// connection Number
		put64(datasetSize);
		put64(timeResolution);
		put32(frameHeaderSize);
		put32(frameMaxSize);
		put32(packetHeaderSize);
		put32(packetMaxSize);
		put32(packetAlignment);
	}

	void bytes2Content() {
		this.datasetSize = nextS64();
		this.timeResolution = nextS64();
		this.frameHeaderSize = nextS32();
		this.frameMaxSize = nextS32();
		this.packetHeaderSize = nextS32();
		this.packetMaxSize = nextS32();
		this.packetAlignment = nextS32();
	}

}