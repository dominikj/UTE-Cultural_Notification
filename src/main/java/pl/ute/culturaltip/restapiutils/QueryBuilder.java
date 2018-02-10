package pl.ute.culturaltip.restapiutils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by dominik on 05.02.18.
 */
public class QueryBuilder {
    private QueryBuilder() {
    }

    public static String buildQuery(RestApiParams params) {
        Iterator<Map.Entry<String, String>> iterator =
                params.getQueryParams().entrySet().iterator();

        StringBuilder builder = new StringBuilder(params.getUri());
        builder.append("?");

        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            builder.append(param.getKey())
                    .append("=")
                    .append(param.getValue())
                    .append("&");
        }
        return builder.toString();
    }
}
