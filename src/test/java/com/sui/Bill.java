package com.sui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class Bill {
    private static final String BASE_URL = "https://www.feidee.com";

    // Periodic account manual entry
    public String billEntry(int id, LocalDate day, double money) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("opt", "entry");
        data.put("id", Integer.toString(id));
        data.put("date", day.format(DateTimeFormatter.ofPattern("yyyy.M.d")));
        data.put("money", Double.toString(money));

        System.out.println(data);

        Connection.Response resp = Jsoup.connect(BASE_URL + "/bill/index.rmi")
            .data(data)
            .method(Connection.Method.POST)
            .execute();

        String str = resp.body();
        if (str.equals("{result:'false'}")) {
            throw new RuntimeException("Failed: " + str);
        }

        return str;
    }
}