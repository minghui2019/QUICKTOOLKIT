package com.sui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sui.pojo.IdName;

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
    private List<com.sui.pojo.Category> parseCategories(Element doc) {
        List<com.sui.pojo.Category> categories = new ArrayList<>();
        Elements anchors = doc.select("div#panel-category a");
        for (Element anchor : anchors) {
            String idStr = anchor.attr("id");
            int[] idAndType = categoryIdTypeSplit(idStr);
            int id = idAndType[0];
            int categoryType = idAndType[1];

            com.sui.pojo.Category category = new com.sui.pojo.Category();
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

    // Parse HTML document to generate categories
	public static List<IdName> parseIdNames(Element div, String zone) {
	    String prefix = "c" + zone.substring(0, 1).toUpperCase() + zone.substring(1, 3) + "-";
	
	    List<IdName> idNames = new ArrayList<>();
	    Elements anchors = div.select("div#panel-" + zone + " a");
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
	        categoryType = com.sui.Client.TRAN_TYPE_PAYOUT;
	        idStr = idStr.replace("cCat-out-", "");
	    } else if (idStr.startsWith("cCat-in-")) {
	        categoryType = com.sui.Client.TRAN_TYPE_INCOME;
	        idStr = idStr.replace("cCat-in-", "");
	    }
	
	    int id = Integer.parseInt(idStr);
	    return new int[]{id, categoryType};
	}
}

