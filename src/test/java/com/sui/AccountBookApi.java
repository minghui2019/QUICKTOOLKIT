package com.sui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AccountBookApi {
    private static final String BASE_URL = "https://www.feidee.com";
    private List<IdName> accountBookList;

    // Refresh account book list
    public void syncAccountBookList() throws IOException {
        Document doc = Jsoup.connect(BASE_URL + "/report_index.do").get();
        Elements lists = doc.select("ul.s-accountbook-all li");
        if (lists.size() == 0) {
            throw new RuntimeException("Account book list not found");
        }

        List<IdName> idNames = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            String name = lists.get(i).attr("title");
            int id = Integer.parseInt(lists.get(i).attr("data-bookid"));
            if (id != 0 && !name.isEmpty()) {
                idNames.add(new IdName(id, name));
            }
        }
        this.accountBookList = idNames;
    }

    // Switch account book
    public void switchAccountBook(String name) throws IOException {
        int accountBookId = 0;
        for (IdName accountBook : this.accountBookList) {
            if (accountBook.getName().equals(name)) {
                accountBookId = accountBook.getId();
                break;
            }
        }

        if (accountBookId == 0) {
            throw new RuntimeException("Account book not found");
        }

        String url = BASE_URL + "/systemSet/book.do?opt=switch&switchId=" + accountBookId + "&return=xxx";
        Document doc = Jsoup.connect(url).get();
        if (!doc.location().contains("200")) {
            throw new RuntimeException("Response error: " + doc.location());
        }

        syncMetaInfo();
    }

    private void syncMetaInfo() {
        // Implementation of this function is not provided in the original Go code
    }
}

class IdName {
    private int id;
    private String name;

    public IdName(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}