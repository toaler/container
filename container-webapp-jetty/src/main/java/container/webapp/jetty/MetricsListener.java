package container.webapp.jetty;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.eclipse.jetty.server.HttpChannel.Listener;
import org.eclipse.jetty.server.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("metricsListener")
public class MetricsListener implements Listener {

	@Autowired
	@Qualifier("logger")
	private org.slf4j.Logger logger;

	@Override
	public void onRequestBegin(Request request) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnRequestBegin(timestamp);
	}

	@Override
	public void onBeforeDispatch(Request request) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnBeforeDispatch(timestamp);
	}

	@Override
	public void onDispatchFailure(Request request, Throwable failure) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnDispatchFailure(timestamp);
	}

	@Override
	public void onAfterDispatch(Request request) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnAfterDispatch(timestamp);
	}

	@Override
	public void onRequestContent(Request request, ByteBuffer content) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnRequestContent(timestamp);
	}

	@Override
	public void onRequestContentEnd(Request request) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnRequestContentEnd(timestamp);
	}

	@Override
	public void onRequestTrailers(Request request) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnRequestTrailers(timestamp);
	}

	@Override
	public void onRequestEnd(Request request) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnRequestEnd(timestamp);
	}

	@Override
	public void onRequestFailure(Request request, Throwable failure) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnRequestFailure(timestamp);
	}

	@Override
	public void onResponseBegin(Request request) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnRequestBegin(timestamp);
	}

	@Override
	public void onResponseCommit(Request request) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnResponseCommit(timestamp);
	}

	@Override
	public void onResponseContent(Request request, ByteBuffer content) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnResponseContent(timestamp);
	}

	@Override
	public void onResponseEnd(Request request) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnResponseEnd(timestamp);
	}

	@Override
	public void onResponseFailure(Request request, Throwable failure) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnResponseFailure(timestamp);
	}

	@Override
	public void onComplete(Request request) {
		long timestamp = System.nanoTime();
		MetricHolder metrics = getMetricHolder(request);
		metrics.setOnComplete(timestamp);
		
		logger.info(metrics.toString());
	}
	
	private MetricHolder getMetricHolder(Request request) {
		MetricHolder metrics = (MetricHolder) request.getAttribute("metricHolder");

		if (metrics == null) {
			String requestId = UUID.randomUUID().toString();
			metrics = new MetricHolder(requestId);
			request.setAttribute("metricHolder", metrics);
		}
		
		return metrics;
	}
}