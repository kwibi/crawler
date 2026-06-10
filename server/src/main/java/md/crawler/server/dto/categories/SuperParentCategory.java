package md.crawler.server.dto.categories;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
@Data
public class SuperParentCategory {
    private String name;
    private HashMap<ParentCategory, List<ChildCategory>> parentCategoryList;

    public SuperParentCategory(String name) {
        this.name = name;
        this.parentCategoryList = new HashMap<>();
    }

    public void addParentCategory(ParentCategory parentCategory, List<ChildCategory> childCategories) {
        parentCategoryList.put(parentCategory, childCategories);
    }
}
