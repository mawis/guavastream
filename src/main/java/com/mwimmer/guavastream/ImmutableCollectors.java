package com.mwimmer.guavastream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.function.Function;
import java.util.stream.Collector;

public class ImmutableCollectors {
	private ImmutableCollectors() {
		// disable construction
	}

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
