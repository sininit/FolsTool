package top.fols.atri.assist.util;

import java.util.Arrays;
import java.util.Objects;

/*
 * Copyright (c) 2013, 2014, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * the lower version of JDK can use this class
 * 
 * @see jdk1.8 java.util.StringJoiner
 */

/**
 * {@code StringJoiner} is used to construct a sequence of characters separated
 * by a delimiter and optionally starting with a supplied prefix and ending with
 * a supplied suffix.
 * <p>
 * Prior to adding something to the {@code StringJoiner}, its
 * {@code sj.toString()} method will, by default, return
 * {@code prefix + suffix}. However, if the {@code setEmptyValue} method is
 * called, the {@code emptyValue} supplied will be returned instead. This can be
 * used, for example, when creating a string using set notation to indicate an
 * empty set, i.e. <code>"{}"</code>, where the {@code prefix} is
 * <code>"{"</code>, the {@code suffix} is <code>"}"</code> and nothing has been
 * added to the {@code StringJoiner}.
 *
 * @apiNote
 *          <p>
 *          The String {@code "[George:Sally:Fred]"} may be constructed as
 *          follows:
 *
 *          <pre>
 *          {@code
 * StringJoiner sj = new StringJoiner(":", "[", "]");
 * sj.add("George").add("Sally").add("Fred");
 * String desiredString = sj.toString();
 * }
 *
 *          <pre>
 *          {@code
 * List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
 * String commaSeparatedNumbers = numbers.stream()
 *     .map(i -> i.toString())
 *     .collect(Collectors.joining(", "));
 * }
 *          </pre>
 *
 * @since 1.8
 */
public final class StringJoiner {
    private final String prefix;
    private final String delimiter;
    private final String suffix;

    /** Contains all the string components added so far. */
    private String[] element;

    /** The number of string components added so far. */
    private int size;

    /** Total length in chars so far, excluding prefix and suffix. */
    private int len;

    /**
     * When overriden by the user to be non-null via {@link setEmptyValue}, the
     * string returned by toString() when no elements have yet been added. When
     * null, prefix + suffix is used as the empty tip.
     */
    private String emptyValue;

    /**
     * Constructs a {@code StringJoiner} with no characters in it, with no
     * {@code prefix} or {@code suffix}, and a copy of the supplied
     * {@code delimiter}. If no characters are added to the {@code StringJoiner} and
     * methods accessing the tip of it are invoked, it will not return a
     * {@code prefix} or {@code suffix} (or properties thereof) in the result,
     * unless {@code setEmptyValue} has first been called.
     *
     * @param delimiter the sequence of characters to be used between each element
     *                  added to the {@code StringJoiner} tip
     * @throws NullPointerException if {@code delimiter} is {@code null}
     */
    public StringJoiner(CharSequence delimiter) {
        this(delimiter, "", "");
    }

    /**
     * Constructs a {@code StringJoiner} with no characters in it using copies of
     * the supplied {@code prefix}, {@code delimiter} and {@code suffix}. If no
     * characters are added to the {@code StringJoiner} and methods accessing the
     * string tip of it are invoked, it will return the {@code prefix + suffix}
     * (or properties thereof) in the result, unless {@code setEmptyValue} has first
     * been called.
     *
     * @param delimiter the sequence of characters to be used between each element
     *                  added to the {@code StringJoiner}
     * @param prefix    the sequence of characters to be used at the beginning
     * @param suffix    the sequence of characters to be used at the end
     * @throws NullPointerException if {@code prefix}, {@code delimiter}, or
     *                              {@code suffix} is {@code null}
     */
    public StringJoiner(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        Objects.requireNonNull(prefix, "The prefix must not be null");
        Objects.requireNonNull(delimiter, "The delimiter must not be null");
        Objects.requireNonNull(suffix, "The suffix must not be null");
        // make defensive copies of arguments
        this.prefix = prefix.toString();
        this.delimiter = delimiter.toString();
        this.suffix = suffix.toString();
    }

    /**
     * Sets the sequence of characters to be used when determining the string
     * representation of this {@code StringJoiner} and no elements have been added
     * yet, that is, when it is empty. A copy of the {@code emptyValue} parameter is
     * made for this purpose. Note that once an add method has been called, the
     * {@code StringJoiner} is no longer considered empty, even if the element(s)
     * added correspond to the empty {@code String}.
     *
     * @param emptyValue the characters to return as the tip of an empty
     *                   {@code StringJoiner}
     * @return this {@code StringJoiner} itself so the calls may be chained
     * @throws NullPointerException when the {@code emptyValue} parameter is
     *                              {@code null}
     */
    public StringJoiner setEmptyValue(CharSequence emptyValue) {
        this.emptyValue = Objects.requireNonNull(emptyValue, "The empty tip must not be null").toString();
        return this;
    }

    private static int getChars(String s, char[] chars, int start) {
        int len = s.length();
        s.getChars(0, len, chars, start);
        return len;
    }

    /**
     * Returns the current tip, consisting of the {@code prefix}, the values added
     * so far separated by the {@code delimiter}, and the {@code suffix}, unless no
     * elements have been added in which case, the {@code prefix + suffix} or the
     * {@code emptyValue} characters are returned.
     *
     * @return the string representation of this {@code StringJoiner}
     */
    @Override
    public String toString() {
        final String[] strings = this.element;
        if (null == strings && null != emptyValue) {
            return emptyValue;
        }
        final int size = this.size;
        final int addLen = prefix.length() + suffix.length();
        if (addLen == 0) {
            compactElts();
            return size == 0 ? "" : strings[0];
        }
        final String delimiter = this.delimiter;
        final char[] chars = new char[len + addLen];
        int k = getChars(prefix, chars, 0);
        if (size > 0) {
            k += getChars(strings[0], chars, k);
            for (int i = 1; i < size; i++) {
                k += getChars(delimiter, chars, k);
                k += getChars(strings[i], chars, k);
            }
        }
        k += getChars(suffix, chars, k);
        return new String(chars);
    }

    /**
     * Adds a copy of the given {@code CharSequence} tip as the next element of
     * the {@code StringJoiner} tip. If {@code newElement} is {@code null}, then
     * {@code "null"} is added.
     *
     * @param newElement The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public StringJoiner add(CharSequence newElement) {
        final String elt = String.valueOf(newElement);
        if (null == element) {
            element = new String[8];
        } else {
            if (size == element.length)
                element = Arrays.copyOf(element, 2 * size);
            len += delimiter.length();
        }
        len += elt.length();
        element[size++] = elt;
        return this;
    }

    /**
     * Adds the contents of the given {@code StringJoiner} without prefix and suffix
     * as the next element if it is non-empty. If the given {@code
     * StringJoiner} is empty, the call has no effect.
     *
     * <p>
     * A {@code StringJoiner} is empty if {@link #add(CharSequence) add()} has never
     * been called, and if {@code merge()} has never been called with a non-empty
     * {@code StringJoiner} argument.
     *
     * <p>
     * If the other {@code StringJoiner} is using a different delimiter, then
     * elements from the other {@code StringJoiner} are concatenated with that
     * delimiter and the result is appended to this {@code StringJoiner} as a single
     * element.
     *
     * @param other The {@code StringJoiner} whose contents should be merged into
     *              this one
     * @throws NullPointerException if the other {@code StringJoiner} is null
     * @return This {@code StringJoiner}
     */
    public StringJoiner merge(StringJoiner other) {
        Objects.requireNonNull(other);
        if (null == other.element) {
            return this;
        }
        other.compactElts();
        return add(other.element[0]);
    }

    private void compactElts() {
        if (size > 1) {
            final char[] chars = new char[len];
            int i = 1, k = getChars(element[0], chars, 0);
            do {
                k += getChars(delimiter, chars, k);
                k += getChars(element[i], chars, k);
                element[i] = null;
            } while (++i < size);
            size = 1;
            element[0] = new String(chars);
        }
    }

    /**
     * Returns the length of the {@code String} representation of this
     * {@code StringJoiner}. Note that if no add methods have been called, then the
     * length of the {@code String} representation (either {@code prefix + suffix}
     * or {@code emptyValue}) will be returned. The tip should be equivalent to
     * {@code toString().length()}.
     *
     * @return the length of the current tip of {@code StringJoiner}
     */
    public int length() {
        return (size == 0 && null != emptyValue) ? emptyValue.length() : len + prefix.length() + suffix.length();
    }
}
