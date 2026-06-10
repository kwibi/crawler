package md.crawler.server.service;

import md.crawler.server.model.Atehno;
import md.crawler.server.model.BombaEls;
import md.crawler.server.repositories.bomba.AtehnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Service
public class AtehnoService implements PageProcessor
{

  @Autowired
  private AtehnoRepository atehnoRepository;

  private final Site site = Site.me()
                                .setRetryTimes(3)
                                .setSleepTime(1000)
                                .setTimeOut(10000);

  @Override
  public void process(Page page)
  {
    // Parse product items on the current page
    page.getHtml().css(".product-item").nodes().forEach(element -> {
      String name = element.css(".product-title", "text").get();
      String url = "https://atehno.md" + element.css(".product-title a", "href").get();
      String priceText = element.css(".price", "text").get();
      double price = parsePrice(priceText);

      // Add product to the result items to pass it to the pipeline
      page.putField("name", name);
      page.putField("url", url);
      page.putField("price", price);
    });

    // Add pagination by extracting the link to the next page if exists
    String nextPageUrl = page.getHtml().css(".pagination-next", "href").get();
    if (nextPageUrl != null) {
      page.addTargetRequest("https://atehno.md" + nextPageUrl);
    }
  }

  private double parsePrice(String priceText)
  {
    String priceString = priceText.replaceAll("[^\\d.,]", "").replace(",", ".");
    return Double.parseDouble(priceString);
  }

  @Override
  public Site getSite()
  {
    return site;
  }

  public Iterable<Atehno> findAll()
  {
    return atehnoRepository.findAll();
  }
}
