package md.crawler.server.controllers;

import lombok.RequiredArgsConstructor;
import md.crawler.server.model.Atehno;
import md.crawler.server.model.BombaEls;
import md.crawler.server.service.AtehnoPipeline;
import md.crawler.server.service.AtehnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

@RestController
@RequiredArgsConstructor
public class AtehnoController
{
  @Autowired
  private AtehnoService atehnoService;

  @Autowired
  private AtehnoPipeline atehnoPipeline;

  @PostMapping("/atehno/download")
  public void downlaod()
  {
    String categoryUrl = "https://atehno.md/category-url"; // Replace with desired category URL
    Spider.create(atehnoService)
        .addUrl(categoryUrl)
        .addPipeline(atehnoPipeline)
        .thread(5)
        .run();
  }

  @GetMapping("/atehno")
  public Iterable<Atehno> get(){
    return atehnoService.findAll();
  }
}
