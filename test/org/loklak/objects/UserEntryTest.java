package org.loklak.objects;

import static org.junit.Assert.assertEquals;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONObject;
import org.junit.Test;

public class UserEntryTest {

    public final static DateTimeFormatter utcFormatter = ISODateTimeFormat.dateTime().withZoneUTC();
    
    @Test
    public void string2byte() throws Exception {
        UserEntry userEntry = new UserEntry("", "test", "http://test.com", "Mr. Test");
        String appearance_first = utcFormatter.print(userEntry.getAppearanceFirst().getTime());
        String appearance_latest = utcFormatter.print(userEntry.getAppearanceLatest().getTime());
        JSONObject o = userEntry.toJSON();
        String expected = "{\"appearance_first\":\"" + appearance_first + "\",\"profile_image_url_http\":\"http://test.com\",\"screen_name\":\"test\",\"user_id\":\"\",\"name\":\"Mr. Test\",\"appearance_latest\":\"" + appearance_latest + "\"}";
        assertEquals(o.toString(), expected);
    }

}
