package com.timr.utils.hamcrest;

import static org.hamcrest.Matchers.equalTo;

import java.util.Map;

import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

/**
 * Hamcrest 1.3 does not support aMapWithSize()
 * see: How to verify Map size using Hamcrest
 * http://www.developersite.org/103-8775-java
 *
 */
public class IsMapWithSize<K, V> extends FeatureMatcher<Map<? extends K, ? extends V>, Integer> {
    public IsMapWithSize(Matcher<? super Integer> sizeMatcher) {
        super(sizeMatcher, "a map with size", "map size");
    }

    @Override
    protected Integer featureValueOf(Map<? extends K, ? extends V> actual) {
        return actual.size();
    }

    /**
     * Creates a matcher for {@link java.util.Map}s that matches when the
     * <code>size()</code> method returns a value that satisfies the specified
     * matcher.
     * <p/>
     * For example:
     * 
     * <pre>
     * Map&lt;String, Integer&gt; map = new HashMap&lt;&gt;();
     * map.put(&quot;key&quot;, 1);
     * assertThat(map, isMapWithSize(equalTo(1)));
     * </pre>
     * 
     * @param sizeMatcher
     *            a matcher for the size of an examined {@link java.util.Map}
     */
    @Factory
    public static <K, V> Matcher<Map<? extends K, ? extends V>> isMapWithSize(Matcher<? super Integer> sizeMatcher) {
        return new IsMapWithSize<K, V>(sizeMatcher);
    }

    /**
     * Creates a matcher for {@link java.util.Map}s that matches when the
     * <code>size()</code> method returns a value equal to the specified
     * <code>size</code>.
     * <p/>
     * For example:
     * 
     * <pre>
     * Map&lt;String, Integer&gt; map = new HashMap&lt;&gt;();
     * map.put(&quot;key&quot;, 1);
     * assertThat(map, isMapWithSize(1));
     * </pre>
     * 
     * @param size
     *            the expected size of an examined {@link java.util.Map}
     */
    @Factory
    public static <K, V> Matcher<Map<? extends K, ? extends V>> isMapWithSize(int size) {
        Matcher<? super Integer> matcher = equalTo(size);
        return IsMapWithSize.<K, V> isMapWithSize(matcher);
    }

}
