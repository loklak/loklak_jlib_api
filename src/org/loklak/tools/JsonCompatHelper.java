/**
 * JsonCompatHelper
 * Copyright 06.06.2016 by Marc Nause, @nause_marc
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; wo even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program in the file lgpl21.txt
 * If not, see <http://www.gnu.org/licenses/>.
 */

package org.loklak.tools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Provides workaround methods for different JSONObject implementations in the
 * original json.org library and the code include in Android.
 */
public class JsonCompatHelper {

    private static final String JSON_OBJECT_METHOD_NAME = "put";
    private static boolean hasCollectionMethod;

    static {
        try {
            hasCollectionMethod = JSONObject.class
                    .getMethod(
                            JSON_OBJECT_METHOD_NAME,
                            new Class[]{String.class, Collection.class}) != null;
        } catch (NoSuchMethodException | SecurityException e) {
            hasCollectionMethod = false;
        }
    }

    public static JSONObject put(
            final JSONObject jsonObject,
            final String key,
            final Collection<?> value) {
        if (hasCollectionMethod) {
            return jsonObject.put(key, value);
        } else {
            return jsonObject.put(key, new JSONArray(value));
        }
    }

}
