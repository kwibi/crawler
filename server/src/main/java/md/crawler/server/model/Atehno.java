package md.crawler.server.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "mgz_bomba_1")
public class Atehno
{

  @Id
  private Long id;

  private String name;
  private String url;
  private double price;

  // Constructors, getters, and setters
  public Atehno() {}

  public Atehno(String name, String url, double price)
  {
    this.name = name;
    this.url = url;
    this.price = price;
  }

  // Getters and Setters
}