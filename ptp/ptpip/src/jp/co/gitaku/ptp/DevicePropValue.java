package jp.co.gitaku.ptp;

import java.io.PrintStream;

public class DevicePropValue extends Data {
	int typecode;
	Object value;
	public static final int s8 = 1;
	public static final int u8 = 2;
	public static final int s16 = 3;
	public static final int u16 = 4;
	public static final int s32 = 5;
	public static final int u32 = 6;
	public static final int s64 = 7;
	public static final int u64 = 8;
	public static final int s128 = 9;
	public static final int u128 = 10;
	public static final int s8array = 16385;
	public static final int u8array = 16386;
	public static final int s16array = 16387;
	public static final int u16array = 16388;
	public static final int s32array = 16389;
	public static final int u32array = 16390;
	public static final int s64array = 16391;
	public static final int u64array = 16392;
	public static final int s128array = 16393;
	public static final int u128array = 16394;
	public static final int string = 65535;

	DevicePropValue(int paramInt) {
		this.typecode = paramInt;
	}

	public DevicePropValue(int typeCode, Object value) {
		this.typecode = typeCode;
		this.value = value;
		setValue(value);
	}

	public Object getValue() {
		return this.value;
	}

	public int getTypeCode() {
		return this.typecode;
	}

	void bytes2Content() {
//		this.value = get(this.typecode, this);
	}
	
	void dump(PrintStream paramPrintStream) {
		paramPrintStream.print("Type: ");
		paramPrintStream.print(getTypeName(this.typecode));
		paramPrintStream.print(", Value: ");
		paramPrintStream.println(this.value.toString());
	}

	public String getCodeName(int typecode) {
		return getTypeName(typecode);
	}

	static Object get(int typecode, Buffer paramBuffer) {
		switch (typecode) {
		case 1:
			return new Integer(paramBuffer.nextS8());
		case 2:
			return new Integer(paramBuffer.nextU8());
		case 3:
			return new Integer(paramBuffer.nextS16());
		case 4:
			return new Integer(paramBuffer.nextU16());
		case 5:
			return new Integer(paramBuffer.nextS32());
		case 6:
			return new Long(0xFFFFFFFF & paramBuffer.nextS32());
		case 7:
			return new Long(paramBuffer.nextS64());
		case 8:
			return new Long(paramBuffer.nextS64());
		case 16385:
			return paramBuffer.nextS8Array();
		case 16386:
			return paramBuffer.nextU8Array();
		case 16387:
			return paramBuffer.nextS16Array();
		case 16388:
			return paramBuffer.nextU16Array();
		case 16389:
		case 16390:
			return paramBuffer.nextS32Array();
		case 16391:
		case 16392:
			return paramBuffer.nextS64Array();
		case 65535:
			return paramBuffer.nextString();
		}
		throw new IllegalArgumentException();
	}

	public void setValue(Object value) {
		this.offset = 0;
		switch (typecode) {
		case 1:
			setData(new byte[1]);
			put8((Integer)value);
			break;
		case 2:
			setData(new byte[1]);
			put8((Integer)value);
			break;
		case 3:
			setData(new byte[2]);
			put16((Integer)value);
			break;
		case 4:
			setData(new byte[2]);
			put16((Integer)value);
			break;
		case 5:
			setData(new byte[4]);
			put32((Integer)value);
			break;
		case 6:
			setData(new byte[4]);
			put32((Long)value);
			break;
		case 7:
			setData(new byte[8]);
			put64((Long)value);
			break;
		case 8:
			setData(new byte[8]);
			put64((Long)value);
			break;
		case 16385:
			setData(new byte[4 + ((int[])value).length]);
			put8Array((int[])value);
			break;
		case 16386:
			setData(new byte[4 + ((int[])value).length]);
			put8Array((int[])value);
			break;
		case 16387:
			setData(new byte[4 + 2 * ((int[])value).length]);
			putU16Array((int[])value);
			break;
		case 16388:
			setData(new byte[4 + 2 * ((int[])value).length]);
			putU16Array((int[])value);
			break;
		case 16389:
		case 16390:
			setData(new byte[4 + 4 * ((int[])value).length]);
			put32Array((int[])value);
			break;
		case 16391:
		case 16392:
			setData(new byte[4 + 8 * ((long[])value).length]);
			put64Array((long[])value);
			break;
		case 65535:
			setData(new byte[2 + 2 * ((String)value).length()]);
			putString((String)value);
		}
	}
	
	public static String getTypeName(int paramInt) {
		switch (paramInt) {
		case 1:
			return "s8";
		case 2:
			return "u8";
		case 3:
			return "s16";
		case 4:
			return "u16";
		case 5:
			return "s32";
		case 6:
			return "u32";
		case 7:
			return "s64";
		case 8:
			return "u64";
		case 9:
			return "s128";
		case 10:
			return "u128";
		case 16385:
			return "s8array";
		case 16386:
			return "u8array";
		case 16387:
			return "s16array";
		case 16388:
			return "u16array";
		case 16389:
			return "s32array";
		case 16390:
			return "u32array";
		case 16391:
			return "s64array";
		case 16392:
			return "u64array";
		case 16393:
			return "s128array";
		case 16394:
			return "u128array";
		case 65535:
			return "string";
		}
		return Container.getCodeString(paramInt);
	}
	
	private final void put8Array(int[] array) {
		if (array == null) {
			put32(0);
			return;
		} else {
			put32(array.length);
		}
		
		for (int i = 0; i < array.length; i++) {
			put8(array[i]);
		}
	}
	
	private final void put32Array(int[] array) {
		if (array == null) {
			put32(0);
			return;
		} else {
			put32(array.length);
		}
		
		for (int i = 0; i < array.length; i++) {
			put32(array[i]);
		}
	}
	
	private final void put64Array(long[] array) {
		if (array == null) {
			put32(0);
			return;
		} else {
			put32(array.length);
		}
		
		for (int i = 0; i < array.length; i++) {
			put64(array[i]);
		}
	}
}