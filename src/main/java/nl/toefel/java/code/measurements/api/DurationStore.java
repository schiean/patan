/*
 *    Copyright 2016 Christophe Hesters
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package nl.toefel.java.code.measurements.api;

import java.util.Map;

/**
 * Methods for measuring elapsed time during program execution.
 */
public interface DurationStore extends Resettable {

	/**
	 * Starts a new stop watch. The elapsed time can be recorded later on by calling {@link #recordElapsedTime(String, Stopwatch)}.
	 * The stopwatch is not stored within the store, clients must keep track of the stopwatch.
	 *
	 * @return a new instance, directly started when this method is invoked.
	 */
	Stopwatch startStopwatch();

	/**
	 * Records the elapsed time and merges the result into the statistical distribution that is identified by the given name.
	 * If no statistical distribution exists with the given name, it is created. The value that is read from the stopwatch is returned.
	 *
	 * @param eventName name to store the elapsed time under
	 * @param stopwatch the {@link Stopwatch} that measures the elapsed time
	 * @return the recorded elapsed millis
	 */
	long recordElapsedTime(String eventName, Stopwatch stopwatch);

	/**
	 * Finds the current statistical distribution for the recorded durations under the given name. If the name has not been found,
	 * a empty distribution will be returned, use the {@link StatisticalDistribution#isEmpty()} method to check for emptiness.
	 *
	 * @param eventName the name of the event to lookup
	 * @return a copy of the internal statistic, never null
	 */
	StatisticalDistribution findDuration(String eventName);

	/**
	 * Returns a snapshot of all the recorded durations
	 *
	 * @return a snapshot of all recorded durations. The snapshot can be modified and is detached from the implementation.
	 */
	Map<String, StatisticalDistribution> getAllDurationsSnapshot();

	/**
	 * Returns a snapshot of all the recorded durations and clears the internal state
	 *
	 * @return a snapshot of all recorded durations. The snapshot can be modified and is detached from the implementation.
	 */
	Map<String, StatisticalDistribution> getAllDurationsSnapshotAndReset();

	/**
	 * Runs the operation wrapped in a timedTask and records it's execution duration when finished under the given eventName,
	 * the postfix of '.ok' or '.failure' separates their measurements.
	 *
	 * Java 8 API Extension. This allows for short readable code.
	 *
	 * return recordElapsedTime("someMethod", someMethod);
	 *
	 * which is equivalent to: <code>
	 *      Stopwatch stopwatch = startStopwatch();
	 *      try{
	 *               T val = someMethod();
	 *               recordElapsedTime("someMethod.ok", stopwatch);
	 *               return val;
	 *      }catch(Exception e){
	 *               recordElapsedTime("someMethod.failed", stopwatch);
	 *               throw e;
	 *      }
	 *
	 * </code>
	 *
	 * @see DurationStore#recordElapsedTime(String, Runnable) for void lambda
	 *
	 * @param eventName
	 * @param task
	 */
	<T> T recordElapsedTime(String eventName, TimedTask<T> task);

	/**
	 * Runs the operation wrapped in a timedTask and records it's execution duration when finished under the given eventName,
	 * the postfix of '.ok' or '.failure' separates their measurements.
	 *
	 * recordElapsedTime("someMethod", someMethod);
	 *
	 * which is equivalent to: <code>
	 *      Stopwatch stopwatch = startStopwatch();
	 *      try{
	 *               someMethod();
	 *               recordElapsedTime("someMethod.ok", stopwatch);
	 *      }catch(Exception e){
	 *               recordElapsedTime("someMethod.failed", stopwatch);
	 *               throw e;
	 *      }
	 *
	 * </code>
	 *
	 * @see DurationStore#recordElapsedTime(String, TimedTask) for Lambda with return
	 *
	 * @param eventName
	 * @param runnable
	 */
	void recordElapsedTime(String eventName, Runnable runnable);
}
