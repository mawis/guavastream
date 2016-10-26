package com.mwimmer.guavastream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Implementations of {@link java.util.stream.Collector} that collect streams
 * to different immutable collections of
 * <a href='https://github.com/google/guava'>Guava: Google Core Libraries for
 * Java 6+</a>.
 *
 * The following are examples of how to use these collectors:
 *
 * <pre>
 * final ImmutableList&lt;String&gt; names = customers.stream()
 *     .map(Customer::getName)
 *     .collect(ImmutableCollectors.toList());
 * </pre>
 *
 * <pre>
 * final ImmutableSet&lt;String&gt; names = customers.stream()
 *     .map(Customer::getName)
 *     .collect(ImmutableCollectors.toSet());
 * </pre>
 *
 * <pre>
 * final ImmutableMap&lt;UUID, String&gt; namesById = customers.stream()
 *     .collect(ImmutableCollectors.toMap(
 *             Customer::getId,
 *             Customer::getName));
 * </pre>
 */
public class ImmutableCollectors {
	private ImmutableCollectors() {
		// disable construction
	}

	/**
	 * Returns a {@link java.util.stream.Collector} that accumulates the input
	 * elements in an {@link com.google.common.collect.ImmutableList} of the
	 * collected elements.
	 *
	 * @throws NullPointerException if any element that gets collected is a
	 * null element
	 */
	public static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>>
		toList() {

		return Collector.of(
			ImmutableList::builder,
			ImmutableList.Builder<T>::add,
			(left, right) -> {
				left.addAll(right.build());
				return left;
			},
			ImmutableList.Builder<T>::build);
	}

	/**
	 * Returns a {@link java.util.stream.Collector} that accumulates the input
	 * elements in an {@link com.google.common.collect.ImmutableSet} of the
	 * collected elements.
	 *
	 * Duplicate elements are ignored (only the first duplicate element is
	 * added).
	 *
	 * @throws NullPointerException if any element that gets collected is a
	 * null element
	 */
	public static <T> Collector<T, ImmutableSet.Builder<T>, ImmutableSet<T>>
		toSet() {

		return Collector.of(
			ImmutableSet::builder,
			ImmutableSet.Builder<T>::add,
			(left, right) -> {
				left.addAll(right.build());
				return left;
			},
			ImmutableSet.Builder<T>::build);
	}

	/**
	 * Returns a {@link java.util.stream.Collector} that accumulates elements
	 * into a {@link com.google.common.collect.ImmutableMap} whose keys and
	 * values are created by applying the {@code keyMapper} and {@code
	 * valueMapper} functions to the elements.
	 *
	 * Duplicate keys are not allowed, and will cause the collector to fail.
	 */
	public static <T, K, V> Collector<T,
		ImmutableMap.Builder<K, V>, ImmutableMap<K, V>> toMap(
			final Function<? super T, ? extends K> keyMapper,
			final Function<? super T, ? extends V> valueMapper) {

		return Collector.of(
			ImmutableMap::builder,
			(accumulator, element) ->
			accumulator.put(
				keyMapper.apply(element), valueMapper.apply(element)),
			(left, right) -> {
				left.putAll(right.build());
				return left;
			},
			ImmutableMap.Builder<K, V>::build);
	}
}
