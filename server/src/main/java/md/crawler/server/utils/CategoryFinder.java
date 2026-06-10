package md.crawler.server.utils;

import lombok.AllArgsConstructor;
import md.crawler.server.dto.categories.ChildCategory;
import md.crawler.server.dto.categories.SuperParentCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryFinder {
    private final CategoryTreeFromFile categoryTreeFromFile;

    public List<String> findCategory(String bread) {
        String[] newBreads = bread.split("/");
        List<String> categoris = new ArrayList<>();
        List<SuperParentCategory> superParentCategories = categoryTreeFromFile.getRUBombaCategories();
        superParentCategories.forEach(superParentCategory -> {
            for (int i = 0; i < newBreads.length - 1; i++) {
                if (superParentCategory.getName().equals(newBreads[i].trim())) {
                    categoris.add(superParentCategory.getName());
                    List<ChildCategory> childCategories = (List<ChildCategory>) superParentCategory.getParentCategoryList(); // cant find child category because can't find ChildCategory
                    categoris.add(newBreads[i + 1]);
                    int finalI = i;
                    childCategories.forEach(childCategory -> {
                        if (childCategory.getName().equals(newBreads[finalI + 2])) {
                            categoris.add(newBreads[finalI + 2]);
                        }
                    });
                }
            }
        });
        return categoris;
    }
}
