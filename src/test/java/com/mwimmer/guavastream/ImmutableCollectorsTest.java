package com.mwimmer.guavastream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.stream.Stream;
import org.junit.Test;
import static java.util.stream.IntStream.range;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class ImmutableCollectorsTest {
	private static final String KEY_PREFIX = "key-";
	private static final String VALUE_PREFIX = "value-";
	private static final String[] VALUES  = {"ONE", "TWO", "THREE"};
	private static final int MIN = 0;
	private static final int MAX = 1000;

	@Test
	public void streamsCanBeCollectedToLists() {
		final ImmutableList<String> result =
			Stream.of(VALUES)
			.collect(ImmutableCollectors.toList());

		verifyContentEquals(VALUES, result);
	}

	@Test
	public void streamsCanBeCollectedToSets() {
		final ImmutableSet<String> result =
			Stream.of(VALUES)
			.collect(ImmutableCollectors.toSet());

		verifyContentEquals(VALUES, result);
	}

	@Test
	public void streamsCanBeCollectedToMaps() {
		final ImmutableMap<String, String> result =
			Stream.of(VALUES)
			.collect(
				ImmutableCollectors.toMap(
					this::key,
					this::value));

		verifyContentEquals(asMap(VALUES), result);
	}

	@Test
	public void parallelStreamsCanBeCollectedToLists() {
		final ImmutableList<String> numbers =
			parallelNumberStream()
			.collect(ImmutableCollectors.toList());

		verifyNumberOfElements(MAX, numbers);
	}

	@Test
	public void parallelStreamsCanBeCollectedToSets() {
		final ImmutableSet<String> numbers =
			parallelNumberStream()
			.collect(ImmutableCollectors.toSet());

		verifyNumberOfElements(MAX, numbers);
	}

	@Test
	public void parallelStreamCanBeCollectedToMaps() {
		final ImmutableMap<String, String> numberMap =
			parallelNumberStream()
			.collect(
				ImmutableCollectors.toMap(
					this::key,
					this::value));

		verifyNumberOfMapEntries(MAX, numberMap);
	}

	private Stream<String> parallelNumberStream() {
		return range(MIN, MAX)
			.parallel()
			.mapToObj(Integer::toString);
	}

	private void verifyContentEquals(
		final String[] expectedContent,
		final Collection<String> collection) {

		assertEquals(
			"Invalid collection size.",
			expectedContent.length,
			collection.size());

		assertThat(
			collection,
			containsInAnyOrder(expectedContent));
	}

	private void verifyContentEquals(
		final ImmutableMap<String, String> expectedMap,
		final ImmutableMap<String, String> map) {

		assertEquals(
			expectedMap,
			map);
	}

	private void verifyNumberOfElements(
		final int expectedElementCount,
		final Collection<String> collection) {

		assertEquals(
			"Incorrect number of elements in collection",
			expectedElementCount,
			collection.size());
	}

	private void verifyNumberOfMapEntries(
		final int expectedEntryCount,
		final ImmutableMap<String, String> map) {

		assertEquals(
			"Incorrect number of map entries",
			expectedEntryCount,
			map.size());
	}

	private ImmutableMap<String, String> asMap(final String[] template) {
		final ImmutableMap.Builder<String, String> builder =
			ImmutableMap.builder();

		for (final String element : template) {
			builder.put(key(element), value(element));
		}

		return builder.build();
	}

	private String key(final String element) {
		return KEY_PREFIX + element;
	}

	private String value(final String element) {
		return VALUE_PREFIX + element;
	}
}
