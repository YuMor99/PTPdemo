package jp.co.gitaku.ptp;

public class Session {
	private int sessionId;
	private int transactionId;
	private boolean active;

	int getNextTransactionId() {
		return this.active ? this.transactionId++ : 0;
	}

	int getNextSessionID() {
		if (!this.active)
			return ++this.sessionId;
		throw new IllegalStateException("already active");
	}

	boolean isActive() {
		return this.active;
	}

	void open() {
		this.transactionId = 1;
		this.active = true;
	}

	void close() {
		this.active = false;
	}

	int getSessionId() {
		return this.sessionId;
	}
}