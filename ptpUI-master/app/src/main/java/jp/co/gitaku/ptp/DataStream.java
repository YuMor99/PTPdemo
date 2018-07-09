package jp.co.gitaku.ptp;

import java.io.IOException;

public abstract class DataStream {
	public static int NORMAL_DATA = 0;
	public static int END_WITH_DATA = 1;
	public static int END_WITHOUT_DATA = 2;
	public static int ERROR_DATA = -1;
	
	public abstract int read(ObjectData data) throws IOException;
}
