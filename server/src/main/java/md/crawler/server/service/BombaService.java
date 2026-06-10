package md.crawler.server.service;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.parser.PageParser;
import lombok.AllArgsConstructor;
import md.crawler.server.model.BombaDTO;
import md.crawler.server.model.BombaEls;
import md.crawler.server.repositories.bomba.BombaRepository;
import md.crawler.server.utils.CategoryFinder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static md.crawler.server.utils.SitemapCrawlerUtils.parseRobotsTxt;
import static md.crawler.server.utils.SitemapCrawlerUtils.readRobotsTxt;

@Service
@AllArgsConstructor
public class BombaService {
    private final BombaRepository repository;
    private final CategoryFinder categoryFinder;

    public void downloadData() {
        String baseUrl = "https://bomba.md";
        String robotsTxtUrl = baseUrl + "/robots.txt";
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String robotsTxtContent = readRobotsTxt(robotsTxtUrl);
            Map<String, List<String>> robotsRules = parseRobotsTxt(robotsTxtContent, baseUrl);
            arrayList.addAll(robotsRules.get("Disallow"));
            System.out.println(robotsTxtContent);
            // TO DO Create a String... with Disallow urls and put it in parse configuration, then configurate threads and crawl-delay and useragent
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,String > cookies = new HashMap<>();
        cookies.put("sessionid", "11233323");
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                .setCookieMap(cookies)
//                .setUrls( "https://www.bomba.md/ro") // work
                .setUrls( "https://www.bomba.md/ro/")
//                .setUrls("https://www.maximum.md/") // work
//				.setWhiteUrlRegexs(arrayList.toArray(new String[0]))
//				.setWhiteUrlRegexs("https://www.bomba.md/ro/product/.*") // work
				.setWhiteUrlRegexs("https://www.bomba.md/ro/product/.*") // work
				.setWhiteUrlRegexs("https://www.bomba.md/ro/category/.*") // work
//				.setWhiteUrlRegexs("https://maximum\\.md/ro/\\d+") // work
//                .setWhiteUrlRegexs(
//                        "https://www.bomba.md/ro/category/.*",
//                        "https://bomba\\.md/search/.*",
//                        "https://bomba\\.md/order/cart/.*",
//                        "https://maximum.md/ru/.*",
//                        "https://bomba.md/ru/.*",
//                        "https://bomba\\.md/product/compare/.*",
//                        "https://bomba\\.md/product/favorite/.*",
//                        "https://bomba\\.md/user/.*",
//                        "https://bomba\\.md/user/restore/.*",
//                        "https://bomba\\.md\\?preloader=.*",
//                        "https://bomba\\.md/public/products/.*",
//                        "https://bomba\\.md/public/instruction/.*",
//                        "https://bomba\\.md/public/feedback/.*",
//                        "https://bomba\\.md/public/old_products/.*",
//                        "https://bomba\\.md/public/xml_partners/.*",
//                        "https://bomba\\.md/public/razm_setka/.*",
//                        "https://bomba\\.md/public/html/.*",
//                        "https://bomba\\.md/product/get_feedback/.*",
//                        ".*")
//                .setAllowSpread(false)
                .setThreadCount(1)
                .setPageParser(new PageParser<BombaDTO>() {
//                    @Override
//                    public void preParse(PageRequest pageRequest) {
//                        // implement crowler configs here
//                    }

                    @Override
                    public void parse(Document html, Element pageVoElement, BombaDTO bombaDTO) {
                        String pageUrl = html.baseUri();
                        System.out.println(pageUrl + "：");
                        if (bombaDTO.getSpecifications() != null) {
                            if (bombaDTO.getAvailability() != null && (bombaDTO.getAvailability().equals("В наличии ✔") || bombaDTO.getAvailability().equals("Este disponibil ✔"))) {
                                bombaDTO.setIsAvailable(true);
                                bombaDTO.setAvailability("available");
                            } else {
                                bombaDTO.setIsAvailable(false);
                                bombaDTO.setAvailability("not available");
                            }
                            if (bombaDTO.getTitle() != null) {
                                System.out.println("Title :" + bombaDTO.getTitle());
                                if (bombaDTO.getOldPrice() == null) {
                                    System.out.println("Main Price: " + bombaDTO.getMainPrice());
                                } else {
                                    System.out.println("New Price: " + bombaDTO.getNewPrice());
                                    System.out.println("Old Price: " + bombaDTO.getOldPrice());
                                }
                                System.out.println("Availability: " + bombaDTO.getAvailability());
                            }
                            List<String> categories = new ArrayList<>();
                            if (bombaDTO.getBreads() != null) {
                                categories = categoryFinder.findCategory(bombaDTO.getBreads());
                            }

                            BombaEls bombaEls = new BombaEls();
                            bombaEls.setAvailability(bombaDTO.getAvailability());
                            bombaEls.setOldPrice(bombaDTO.getOldPrice());
                            bombaEls.setTitle(bombaDTO.getTitle());
                            bombaEls.setMainPrice(bombaDTO.getMainPrice());
                            bombaEls.setNewPrice(bombaDTO.getNewPrice());
                            if (categories != null) {
                                bombaEls.setSuperParentCategory(categories.get(0));
                                bombaEls.setParentCategory(categories.get(1));
                                bombaEls.setChildCategory(categories.get(2));
                            }
                            bombaEls.setIsAvailable(bombaDTO.getIsAvailable());
                            if (bombaEls.getTitle() != null) {
                                repository.save(bombaEls);
                            }
                        }
                    }
                })
                .build();

        crawler.start(true);

    }

    public BombaEls save(BombaEls entity) {
        return repository.save(entity);
    }

    public Iterable<BombaEls> findAll() {
        return repository.findAll();
    }
}
