package md.crawler.server.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SitemapCrawlerUtils {
    public static String readRobotsTxt(String robotsTxtUrl) throws IOException {
        URL url = new URL(robotsTxtUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append('\n');
            }

            return content.toString();
        }
    }

    public static Map<String, List<String>> parseRobotsTxt(String robotsTxtContent, String baseUrl) {
        return Arrays.stream(robotsTxtContent.split("\n"))
                .map(line -> line.split(": ", 2))
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(
                        parts -> parts[0].trim(),
                        parts -> {
                            String value = !parts[1].trim().equals("/") ? baseUrl + parts[1].trim().replaceAll("\\*", "") : "/";
                            // Use ArrayList instead of Collections.singletonList
                            List<String> list = new ArrayList<>();
                            list.add(value);
                            return list;

                        },
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        }
                ));
    }
}
