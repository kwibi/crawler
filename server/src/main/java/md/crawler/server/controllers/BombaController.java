package md.crawler.server.controllers;

import lombok.RequiredArgsConstructor;
import md.crawler.server.model.BombaEls;
import md.crawler.server.service.BombaService;
import md.crawler.server.service.BombaServiceTest;
import md.crawler.server.service.BombaServiceWebMagic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

@RestController
@RequiredArgsConstructor
public class BombaController {
    public final BombaServiceTest bombaServiceTest;

    @PostMapping("/download")
    public void downlaod(){
        bombaServiceTest.downloadData();
//        Spider.create(new BombaServiceWebMagic())
//                .addUrl("https://bomba.md/ru/") // Start URL (category page)
//                .addPipeline(new ConsolePipeline()) // Output results to console
//                .thread(5) // Number of threads
//                .run();
    }
    @GetMapping("/")
    public Iterable<BombaEls> get(){
        return bombaServiceTest.findAll();
    }
}
