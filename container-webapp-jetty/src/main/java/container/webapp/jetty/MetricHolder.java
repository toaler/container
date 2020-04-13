package container.webapp.jetty;

public class MetricHolder {
	
	private final String requestId;
	private long onRequestBegin;
	private long onBeforeDispatch;
	private long onDispatchFailure;
	private long onAfterDispatch;
	private long onRequestContent;
	private long onRequestContentEnd;
	private long onRequestTrailers;
	private long onRequestEnd;
	private long onRequestFailure;
	private long onResponseBegin;
	private long onResponseCommit;
	private long onResponseContent;
	private long onResponseEnd;
	private long onResponseFailure;
	private long onComplete;

	public MetricHolder(String requestId) {
		this.requestId = requestId;
	}

	public void setOnRequestBegin(long timestamp) {
		this.onRequestBegin = timestamp;
	}

	public void setOnBeforeDispatch(long timestamp) {
		this.onBeforeDispatch = timestamp;
	}

	public void setOnDispatchFailure(long timestamp) {
		this.onDispatchFailure = timestamp;
	}

	public void setOnAfterDispatch(long timestamp) {
		this.onAfterDispatch = timestamp;
	}

	public void setOnRequestContent(long timestamp) {
		this.onRequestContent = timestamp;
	}

	public void setOnRequestContentEnd(long timestamp) {
		this.onRequestContentEnd = timestamp;
	}

	public void setOnRequestTrailers(long timestamp) {
		this.onRequestTrailers = timestamp;
	}

	public void setOnRequestEnd(long timestamp) {
		this.onRequestEnd = timestamp;
	}

	public void setOnRequestFailure(long timestamp) {
		this.onRequestFailure = timestamp;
	}

	public void setOnResponseBegin(long timestamp) {
		this.onResponseBegin = timestamp;
	}

	public void setOnResponseCommit(long timestamp) {
		this.onResponseCommit = timestamp;
	}

	public void setOnResponseContent(long timestamp) {
		this.onResponseContent = timestamp;
	}

	public void setOnResponseEnd(long timestamp) {
		this.onResponseEnd = timestamp;
	}

	public void setOnResponseFailure(long timestamp) {
		this.onResponseFailure = timestamp;
	}

	public void setOnComplete(long timestamp) {
		this.onComplete = timestamp;
		
	}
	
	@Override
	public String toString() {
		return "MetricHolder [\nrequestId\t" + requestId + "\nonRequestBegin\t\t" + onRequestBegin + "\nonBeforeDispatch\t"
				+ onBeforeDispatch + "\nonDispatchFailure\t" + onDispatchFailure + "\nonAfterDispatch\t\t" + onAfterDispatch
				+ "\nonRequestContent\t" + onRequestContent + "\nonRequestContentEnd\t" + onRequestContentEnd
				+ "\nonRequestTrailers\t" + onRequestTrailers + "\nonRequestEnd\t\t" + onRequestEnd + "\nonRequestFailure\t"
				+ onRequestFailure + "\nonResponseBegin\t\t" + onResponseBegin + "\nonResponseCommit\t" + onResponseCommit
				+ "\nonResponseContent\t" + onResponseContent + "\nonResponseEnd\t\t" + onResponseEnd
				+ "\nonResponseFailure\t" + onResponseFailure + "\nonComplete\t\t" + onComplete + "\nduration\t\t" + (onComplete - onBeforeDispatch) +" ]";
	}

}
