/**
 *  UTF8
 *  Copyright 22.02.2015 by Michael Peter Christen, @0rb1t3r
 *  This class is the android version from the original file,
 *  taken from the loklak_server project. It may be slightly different.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program in the file lgpl21.txt
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package org.loklak.tools;

import java.nio.charset.StandardCharsets;

/**
 * Provides methods for converting Strings to bytes without locking concurrent threads and
 * efficient methods for converting bytes to String.
 */
public class UTF8 {

    public boolean insensitive;

    // restricts instantiation of UTF8
    private UTF8() {}

    /**
     * Using the string method with the default charset given as argument should prevent using the
     * charset cache in FastCharsetProvider.java:118 which locks all concurrent threads using a
     * UTF8.String() method.
     * @param bytes The bytes to be decoded into characters
     * @return String from the decoded bytes
     */
    public final static String String(final byte[] bytes) {
        return bytes == null ? "" : new String(bytes, 0, bytes.length, StandardCharsets.UTF_8);
    }

    /**
     *Similar to <code>String(final byte[])</code>, this method provides flexibility to select
     * the start of decoding and the number of bytes to decode.
     * @param bytes The bytes to be decoded into characters
     * @param offset The index of first byte to be decoded
     * @param length The number of bytes to be decoded
     * @return String from the decoded bytes.
     */
    public final static String String(final byte[] bytes, final int offset, final int length) {
        assert bytes != null;
        return new String(bytes, offset, length, StandardCharsets.UTF_8);
    }

    /**
     * getBytes() as method for String synchronizes during the look-up for the
     * Charset object for the default charset as given with a default charset name.
     * With our call using a given charset object, the call is much easier to perform
     * and it omits the synchronization for the charset lookup.
     * @param string String to be encoded into a sequence of bytes using UTF_8
     * @return The resultant byte array
     */
    public final static byte[] getBytes(final String string) {
        if (string == null)
            return null;
        return string.getBytes(StandardCharsets.UTF_8);
    }

    /**
     *Same functionality as <code>getBytes(String)</code>.
     * @param stringBuilder StringBuilder to be encoded into a sequence of bytes using UTF_8
     * @return The resultant byte array
     */
    public final static byte[] getBytes(final StringBuilder stringBuilder) {
        if (stringBuilder == null)
            return null;
        return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

}
