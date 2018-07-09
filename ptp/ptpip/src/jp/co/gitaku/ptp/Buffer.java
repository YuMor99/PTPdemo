package jp.co.gitaku.ptp;

public class Buffer {
	protected byte[] data;
	protected int length;
	protected int offset;

	protected Buffer(byte[] data) {
		this(data, data.length);
	}

	protected Buffer(byte[] data, int length) {
		if ((data != null) && ((length < 0) || (length > data.length)))
			throw new IllegalArgumentException();
		if ((data == null) && (length != 0))
			throw new IllegalArgumentException();
		this.data = data;
		this.length = length;
		this.offset = 0;
	}

	protected final int getS8(int paramInt) {
		return this.data[paramInt];
	}

	protected final int getU8(int paramInt) {
		return 0xFF & this.data[paramInt];
	}

	protected final void put8(int paramInt) {
		this.data[(this.offset++)] = (byte) paramInt;
	}

	protected final int nextS8() {
		return this.data[(this.offset++)];
	}

	protected final int nextU8() {
		return 0xFF & this.data[(this.offset++)];
	}

	protected final int[] nextS8Array() {
		int i = nextS32();
		int[] arrayOfInt = new int[i];
		for (int j = 0; j < i; j++)
			arrayOfInt[j] = nextS8();
		return arrayOfInt;
	}

	protected final int[] nextU8Array() {
		int i = nextS32();
		int[] arrayOfInt = new int[i];
		for (int j = 0; j < i; j++)
			arrayOfInt[j] = nextU8();
		return arrayOfInt;
	}

	protected final int getS16(int data) {
		int i = 0xFF & this.data[(data++)];
		i |= this.data[data] << 8;
		return i;
	}

	protected final int getU16(int data) {
		int i = 0xFF & this.data[(data++)];
		i |= 0xFF00 & this.data[data] << 8;
		return i;
	}

	protected final void put16(int data) {
		this.data[(this.offset++)] = (byte) data;
		this.data[(this.offset++)] = (byte) (data >> 8);
	}

	protected final int nextS16() {
		int i = getS16(this.offset);
		this.offset += 2;
		return i;
	}

	protected final int nextU16() {
		int i = getU16(this.offset);
		this.offset += 2;
		return i;
	}
	
	protected final void putU16Array(int[] array) {
		if (array == null) {
			put32(0);
			return;
		} else {
			put32(array.length);
		}
		
		for (int i = 0; i < array.length; i++) {
			put16(array[i]);
		}
	}
	
	protected final int[] nextS16Array() {
		int i = nextS32();
		int[] arrayOfInt = new int[i];
		for (int j = 0; j < i; j++)
			arrayOfInt[j] = nextS16();
		return arrayOfInt;
	}

	protected final int[] nextU16Array() {
		int i = nextS32();
		int[] arrayOfInt = new int[i];
		for (int j = 0; j < i; j++)
			arrayOfInt[j] = nextU16();
		return arrayOfInt;
	}

	protected final int getS32(int paramInt) {
		int i = 0xFF & this.data[(paramInt++)];
		i |= (0xFF & this.data[(paramInt++)]) << 8;
		i |= (0xFF & this.data[(paramInt++)]) << 16;
		i |= this.data[paramInt] << 24;

		return i;
	}

	protected final void put32(int paramInt) {
		this.data[(this.offset++)] = (byte) paramInt;
		this.data[(this.offset++)] = (byte) (paramInt >> 8);
		this.data[(this.offset++)] = (byte) (paramInt >> 16);
		this.data[(this.offset++)] = (byte) (paramInt >> 24);
	}
	
	protected final void put32(long paramInt) {
		this.data[(this.offset++)] = (byte) paramInt;
		this.data[(this.offset++)] = (byte) (paramInt >> 8);
		this.data[(this.offset++)] = (byte) (paramInt >> 16);
		this.data[(this.offset++)] = (byte) (paramInt >> 24);
	}

	protected final int nextS32() {
		int i = getS32(this.offset);
		this.offset += 4;
		return i;
	}

	protected final int[] nextS32Array() {
		int i = nextS32();
		int[] arrayOfInt = new int[i];
		for (int j = 0; j < i; j++) {
			arrayOfInt[j] = nextS32();
		}
		return arrayOfInt;
	}

	protected final long getS64(int paramInt) {
		long l = 0xFFFFFFFF & getS32(paramInt);

		l |= getS32(paramInt + 4) << 32;
		return l;
	}

	protected final void put64(long paramLong) {
		put32((int) paramLong);
		put32((int) (paramLong >> 32));
	}

	protected final long nextS64() {
		long l = getS64(this.offset);
		this.offset += 8;
		return l;
	}

	protected final long[] nextS64Array() {
		int i = nextS32();
		long[] arrayOfLong = new long[i];
		for (int j = 0; j < i; j++)
			arrayOfLong[j] = nextS64();
		return arrayOfLong;
	}

	protected void putString(String str) {
		if (str == null) {
			put8(0);
			return;
		}

		int i = str.length();

		if (i > 254)
			throw new IllegalArgumentException();
		put8(i + 1);
		for (int j = 0; j < i; j++)
			put16(str.charAt(j));
		put16(0);
	}

	protected String nextString() {
		int i = nextU8();

		if (i == 0) {
			return null;
		}
		StringBuffer localStringBuffer = new StringBuffer(i);
		for (int j = 0; j < i; j++) {
			localStringBuffer.append((char) nextU16());
		}
		localStringBuffer.setLength(i - 1);
		return localStringBuffer.toString();
	}
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
		this.length = data.length;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}