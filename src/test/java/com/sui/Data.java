package com.sui;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Data {
    // Export data to file (xls file in Feidee web format)
    public void exportToFile(String filename) throws Exception {
        byte[] b = exportToBuffer();
        Files.write(Paths.get(filename), b);
    }

    public byte[] exportToBuffer() throws Exception {
        String downloadAddr = getExportLink();
        return HttpClient.get(downloadAddr).body().getBytes();
    }

    // Get data export link (export as xls file in Feidee web format)
    public String getExportLink() throws Exception {
        String addr = com.sui.Client.BASE_URL + "/data/index.jsp";
        Document doc = Jsoup.parse(HttpClient.get(addr).body().toString());

        Elements anchors = doc.select("table.out-data a");
        for (Element anchor : anchors) {
            String linkText = anchor.text();
            if (!linkText.equals("web版")) {
                continue;
            }
            String href = anchor.attr("href");
            if (href.isEmpty()) {
                continue;
            }

            URI baseUrl = URI.create(addr);
            URI downloadUrl = URI.create(href);
            return baseUrl.resolve(downloadUrl).toString();
        }

        throw new IOException("No matching link found");
    }
}