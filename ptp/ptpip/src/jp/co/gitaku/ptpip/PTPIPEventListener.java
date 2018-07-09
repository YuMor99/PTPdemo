package jp.co.gitaku.ptpip;

import java.util.EventListener;
import jp.co.gitaku.ptp.BaselineInitiator;

public interface PTPIPEventListener extends EventListener {
	public void processEvent(BaselineInitiator initiator, EventPacket event);
}
