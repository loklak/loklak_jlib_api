package org.loklak;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.json.JSONObject;

public class Utility {

    public static Matcher<JSONObject> hasKey(final String key) {
        return new TypeSafeMatcher<JSONObject>() {
            @Override
            protected boolean matchesSafely(JSONObject jsonObject) {
                return jsonObject.has(key);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("JSONObject should have key \"").appendText(key + "\"");
            }
        };
    }
}
