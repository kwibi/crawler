package md.crawler.server.utils;

import md.crawler.server.dto.categories.ChildCategory;
import md.crawler.server.dto.categories.ParentCategory;
import md.crawler.server.dto.categories.SuperParentCategory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryTreeFromFile {

    public List<SuperParentCategory> getRUBombaCategories() {
        return getSuperParentCategories("ru");
    }

    public List<SuperParentCategory> getROBombaCategories() {
        return getSuperParentCategories("ro");
    }

    private static List<SuperParentCategory> getSuperParentCategories(String language) {
        try {
            String filePath = language.equals("ru") ? "classpath:bomba/bomba_categories_ru.txt" : "classpath:bomba/bomba_categories_ro.txt";
            File file = ResourceUtils.getFile(filePath);

//            // Print the category structure
//            printCategoryStructure(superParentCategories);
            return readCategoriesFromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<SuperParentCategory> readCategoriesFromFile(File file) throws IOException {
        List<SuperParentCategory> superParentCategories = new ArrayList<>();
        SuperParentCategory currentSuperParent = null;
        ParentCategory currentParent = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int depth = getDepth(line);
                String categoryName = line.trim().substring(depth);

                if (depth == 2) {
                    // Super parent category
                    currentSuperParent = new SuperParentCategory(categoryName);
                    superParentCategories.add(currentSuperParent);
                    currentParent = null;
                } else if (depth == 1) {
                    // Parent category
                    currentParent = new ParentCategory(categoryName);
                    currentSuperParent.addParentCategory(currentParent, new ArrayList<>());
                } else {
                    // Child category
                    if (currentParent != null) {
                        currentParent.addChildCategory(new ChildCategory(categoryName));
                    }
                }
            }
        }

        return superParentCategories;
    }

    private static int getDepth(String line) {
        int depth = 0;
        while (line.startsWith("-") || line.startsWith("--")) {
            depth++;
            line = line.substring(1);
        }
        return depth;
    }

//    private static void printCategoryStructure(List<SuperParentCategory> superParentCategories) {
//        for (SuperParentCategory superParentCategory : superParentCategories) {
//            System.out.println(superParentCategory.getName());
//
//            for (Map.Entry<ParentCategory, List<ChildCategory>> entry : superParentCategory.getParentCategoryList().entrySet()) {
//                ParentCategory parentCategory = entry.getKey();
//                System.out.println("  " + parentCategory.getName());
//
//                List<ChildCategory> childCategories = entry.getKey().getChildCategories();
//                for (ChildCategory childCategory : childCategories) {
//                    System.out.println("    " + childCategory.getName());
//                }
//            }
//        }
//    }
}