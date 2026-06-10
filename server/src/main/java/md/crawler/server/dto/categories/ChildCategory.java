package md.crawler.server.dto.categories;

import lombok.Data;

@Data
public
class ChildCategory {
    private String name;

    public ChildCategory(String name) {
        this.name = name;
    }
}