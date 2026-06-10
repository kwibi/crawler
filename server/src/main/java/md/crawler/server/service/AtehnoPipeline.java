package md.crawler.server.service;

import md.crawler.server.model.Atehno;
import md.crawler.server.repositories.bomba.AtehnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class AtehnoPipeline implements Pipeline
{

  @Autowired
  private AtehnoRepository productRepository;

  @Override
  public void process(ResultItems resultItems, Task task)
  {
    String name = resultItems.get("name");
    String url = resultItems.get("url");
    Double price = resultItems.get("price");

    if (name != null && url != null && price != null) {
      Atehno product = new Atehno(name, url, price);
      productRepository.save(product);
    }
  }
}