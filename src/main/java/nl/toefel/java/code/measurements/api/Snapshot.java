package nl.toefel.java.code.measurements.api;

import java.util.Map;

/**
 * A snapshot of the collected statistics. The snapshot should not be related to the implementation
 * anymore. Any changes made to the data should have no effect on the internal statistics stores.
 */
public interface Snapshot {

	/**
	 * @return instant in time when this snapshot was taken (ms precision) comparable with {@link System#currentTimeMillis()}
	 */
	long getTimestampTaken();

	/**
	 * @return all the recorded durations described as a statistical distribution by name (never null)
	 */
	Map<String, StatisticalDistribution> getDurations();

	/**
	 * @return all the collected counter values (timesOccurred) by their name (never null)
	 */
	Map<String, Long> getOccurrences();

	/**
	 * @return all the collected statistics by name (never null)
	 */
	Map<String, StatisticalDistribution> getSamples();
}
