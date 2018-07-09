package jp.co.gitaku.ptp;

public class DataArray extends Data {

	private int eleLength;
	private Object elementList;
	
	public DataArray(int eleLength) {
		this.eleLength = eleLength;
	}
	
	@Override
	void bytes2Content() {
//		int numElements = nextS32();
		if (eleLength == 4) {
			elementList = nextS32Array();
		} else if (eleLength == 8) {
			elementList = nextS64Array();
		}
//		int[] elements = new int[numElements];
//		long[] elements = new int[numElements];
//		for (int i = 0; i < numElements; i++) {
//			if (eleLength == 4) {
//				elements[i] = nextS32();
//			} else if (eleLength == 8) {
//				elementList[i] = 
//			}
//		}
	}

	public int getElementSize() {
		return eleLength;
	}

	public Object getElementList() {
		return elementList;
	}

}
