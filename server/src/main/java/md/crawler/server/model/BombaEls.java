package md.crawler.server.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
@Data
@Document(indexName = "mgz_bomba_1")
public class BombaEls {
    @Id
    private String id;
    private String title;
    private String newPrice;
    private String oldPrice;
    private String mainPrice;
    private String availability;
    private String superParentCategory;
    private String parentCategory;
    private String childCategory;
    private String url;
    private Boolean isAvailable;

//    @Field(type = FieldType.Nested, includeInParent = true)
//    private List<Author> authors;

}
