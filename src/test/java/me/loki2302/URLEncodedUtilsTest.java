package me.loki2302;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class URLEncodedUtilsTest {
    @Test
    public void canParseQueryString() {
        String queryString = "a=hello%20there&m=%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82";
        List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(queryString, Charset.forName("UTF-8"));
        Map<String, String> params = nameValuePairsToMap(nameValuePairs);
        assertEquals(2, params.size());

        String aValue = params.get("a");
        assertEquals("hello there", aValue);

        String mValue = params.get("m");
        assertEquals("\u043F\u0440\u0438\u0432\u0435\u0442", mValue); // russian 'privet'
    }

    @Test
    public void canBuildQueryString() {
        List<? extends NameValuePair> nameValuePairs = Arrays.asList(
                new BasicNameValuePair("a", "hello there"),
                new BasicNameValuePair("m", "\u043F\u0440\u0438\u0432\u0435\u0442"));
        String queryString = URLEncodedUtils.format(nameValuePairs, Charset.forName("UTF-8"));
        assertEquals("a=hello+there&m=%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82", queryString);
    }

    private static Map<String, String> nameValuePairsToMap(Iterable<NameValuePair> nameValuePairs) {
        Map<String, String> map = new HashMap<String, String>();
        for(NameValuePair nameValuePair : nameValuePairs) {
            map.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        return map;
    }
}
