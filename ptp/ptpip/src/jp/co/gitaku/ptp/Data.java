package jp.co.gitaku.ptp;

public abstract class Data extends Buffer {
	private boolean in;

	public Data() {
		this(true, null, 0);
	}

	public Data(boolean isIn, byte[] data) {
		super(data);
		this.in = isIn;
	}

	public Data(boolean isIn, byte[] data, int length) {
		super(data, length);
		this.in = isIn;
	}

	public boolean isIn() {
		return this.in;
	}

	public String getCodeName(int paramInt) {
		return Command.getOpcodeString(paramInt);
	}

//	public String toString() {
//		StringBuffer localStringBuffer = new StringBuffer();
//		int i = getCode();
//
//		localStringBuffer.append("{ ");
//		localStringBuffer.append(Container.getBlockTypeName(getBlockType()));
//		if (this.in)
//			localStringBuffer.append(" IN");
//		else
//			localStringBuffer.append(" OUT");
////		localStringBuffer.append("; len ");
////		localStringBuffer.append(Integer.toString(getLength()));
//		localStringBuffer.append("; ");
//		localStringBuffer.append(Command.getOpcodeString(i));
//		localStringBuffer.append("; TransactionID ");
//		localStringBuffer.append(Integer.toString(getTransactionID()));
//		localStringBuffer.append("}");
//		return localStringBuffer.toString();
//	}
	
	public void setData(byte[] data) {
		this.data = data;
		this.length = data.length;
		bytes2Content();
	}
	
	abstract void bytes2Content();
}