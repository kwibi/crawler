package md.crawler.server.dto.categories;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParentCategory {
    private String name;
    private List<ChildCategory> childCategories;

    public ParentCategory(String name) {
        this.name = name;
        this.childCategories = new ArrayList<>();
    }

    public void addChildCategory(ChildCategory childCategory) {
        childCategories.add(childCategory);
    }
}