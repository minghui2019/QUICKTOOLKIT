package com.sui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Category {
    private static final String BASE_URL = "https://www.feidee.com";
    private AccountBook accountBook;

    // Initialize account book, category, account, store, project, member and other information
    public void syncMetaInfo() throws IOException {
        Document doc = Jsoup.connect(BASE_URL + "/tally/new.do").get();
        Element div = doc.selectFirst("div#filter-bar div.fb-choose");

        // Parse category information
        AccountBook accountBook = new AccountBook();
        accountBook.setCategories(parseCategories(div));

        // Parse store, member, account and project information
        accountBook.setStores(parseIdNames(div, "store"));
        accountBook.setMembers(parseIdNames(div, "member"));
        accountBook.setAccounts(parseIdNames(div, "account"));
        accountBook.setProjects(parseIdNames(div, "project"));

        this.accountBook = accountBook;
    }

    // Parse HTML document to generate categories
    private List<Category> parseCategories(Element doc) {
        List<Category> categories = new ArrayList<>();
        Elements anchors = doc.select("div#panel-category a");
        for (Element anchor : anchors) {
            String idStr = anchor.attr("id");
            int[] idAndType = categoryIdTypeSplit(idStr);
            int id = idAndType[0];
            int categoryType = idAndType[1];

            Category category = new Category();
            category.setType(categoryType);
            category.setSub(!anchor.hasClass("ctit"));
            category.setId(id);
            category.setName(anchor.text());

            // Find subcategory IDs
            if (!category.isSub()) {
                String subAnchorClass = idStr.substring(0, idStr.length() - 2);
                Elements subAnchors = anchor.parent().select("a." + subAnchorClass);

                List<Integer> subIds = new ArrayList<>();
                for (Element subAnchor : subAnchors) {
                    String subIdStr = subAnchor.attr("id");
                    int subId = categoryIdTypeSplit(subIdStr)[0];
                    subIds.add(subId);
                }

                category.setSubIds(subIds);
            }

            categories.add(category);
        }

        return categories;
    }

    private int[] categoryIdTypeSplit(String idStr) {
        // Implementation of this function is not provided in the original Go code
        return new int[2];
    }

    // Parse HTML document to generate categories
public static List<IdName> parseIdNames(Selection doc, String zone) {
    String prefix = "c" + zone.substring(0, 1).toUpperCase() + zone.substring(1, 3) + "-";

    List<IdName> idNames = new ArrayList<>();
    Selection anchors = doc.select("div#panel-" + zone + " a");
    for (Element anchor : anchors) {
        String idStr = anchor.attr("id");
        if (idStr.equals(prefix + "a")) {
            continue;
        }

        idStr = idStr.replace("-a", "");
        idStr = idStr.replace(prefix, "");
        int id = Integer.parseInt(idStr);

        idNames.add(new IdName(id, anchor.text()));
    }

    return idNames;
}

// Extract category ID and type from cCat-???-??????-a formatted string
public static int[] categoryIdTypeSplit(String idStr) {
    idStr = idStr.replace("-a", "");

    int categoryType = 0;

    if (idStr.startsWith("cCat-out-")) {
        categoryType = TranTypePayout;
        idStr = idStr.replace("cCat-out-", "");
    } else if (idStr.startsWith("cCat-in-")) {
        categoryType = TranTypeIncome;
        idStr = idStr.replace("cCat-in-", "");
    }

    int id = Integer.parseInt(idStr);
    return new int[]{id, categoryType};
}
}

class AccountBook {
    private List<Category> categories;
    private List<IdName> stores;
    private List<IdName> members;
    private List<IdName> accounts;
    private List<IdName> projects;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<IdName> getStores() {
        return stores;
    }

    public void setStores(List<IdName> stores) {
        this.stores = stores;
    }

    public List<IdName> getMembers() {
        return members;
    }

    public void setMembers(List<IdName> members) {
        this.members = members;
    }

    public List<IdName> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<IdName> accounts) {
        this.accounts = accounts;
    }

    public List<IdName> getProjects() {
        return projects;
    }

    public void setProjects(List<IdName> projects) {
        this.projects = projects;
    }
}

class Category extends IdName {
    private int type; // Category type: expenditure or income
    private boolean isSub; // Whether it is a subcategory
    private List<Integer> subIds;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSub() {
        return isSub;
    }

    public void setSub(boolean isSub) {
        this.isSub = isSub;
    }

    public List<Integer> getSubIds() {
        return subIds;
    }

    public void setSubIds(List<Integer> subIds) {
        this.subIds = subIds;
    }
}

class IdName {
  // Implementation of this class is not provided in the original Go code
}