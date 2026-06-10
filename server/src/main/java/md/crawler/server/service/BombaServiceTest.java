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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class BombaServiceTest {
    private final BombaRepository repository;
    private final CategoryFinder categoryFinder;

    public void downloadData() {
//        getBombaSitemap();
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/javascript");
        headers.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36");
        headers.put("accept-language","en-US,en;q=0.9,de;q=0.8");
        headers.put("x-client-data","CJC2yQEIpLbJAQipncoBCKz4ygEIkqHLAQid/swBCIWgzQEI5b7OAQjSx84BCJjKzgEY9snNAQ==");
        Map<String,String > cookies = new HashMap<>();
        cookies.put("sessionid", "11233323");
        cookies.put("Cookie", "__Secure-3PAPISID=fBqwN-MWW3N8i1FZ/As9RROJHziBubJQJt; __Secure-3PSID=g.a000pQijJXMpT2Jue6H35sJ31OcttvZvt0QCeK7yWhchVVhi-DFUyYsuUQOH_QhQY9Q7OFdADgACgYKAQISARQSFQHGX2Miz-WIprOAD62izWUptEMyRRoVAUF8yKrFbRgw9AyAfOMz9YDbQUhq0076; NID=518=JF68aM7dmrH8CW3sZQmWicREr_HS3vqBMp-xmwJnH6W4sAEAWQ_SvtC8COiz6RRTWKULmssGco60vpvZU01TQj6htdGH2EcC8gG1kO014pA2oh3oyDNB-9eYCpj4UT8ybXk2x45z_AesQ0y5aEU2pRC2xJQ11sheTyCkt69AYV-iyDKjoc62vdmsCn9vqrcVCL_jKVSusTyKURjv0Dx1JylLmJRZspnHucCYDhqgytmEn9Wgaat0ttnLeDhNVwcBV0WMS_dQ1_5bwe8X5t4pG1Ee9L7ehCAerRX9rdwb4SpuM9xodcSG5X2GoE3Iz0dlTAt_Zm-ldfxn2yLEoJCCJOATl4eYAlz2mG9OAzbnHW5xYY6LxjgLsIBTQtaFe-456IB8vg1ZJgLtSfM64m2gmjfjtQ; __Secure-3PSIDTS=sidts-CjIBQT4rX13rVk2ugGEctI5bJid1MH83XMBOehX6VSVO7vB8phb9oL46Zvp3-Tlh4OKPEBAA; __Secure-3PSIDCC=AKEyXzWK0LtzXZDcwOW43t0VZXf8fz8Qg8UUwSaIECwE49sVMVyuRHGiluTcLcLzqi07d0obgiWm");
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://bomba.md/ru/")
//                .setWhiteUrlRegexs("https://www.bomba.md/ru/product/.*")
//                .setWhiteUrlRegexs("https://www.bomba.md/ru/category/.*")
                .setThreadCount(3)
                .setHeaderMap(headers)
                .setCookieMap(cookies)
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                .setPageParser(new PageParser<BombaDTO>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, BombaDTO bombaDTO) {
                        bombaParser(html, bombaDTO);
                    }
                })
                .build();

        crawler.start(true);

    }


    private void bombaParser(Document html, BombaDTO bombaDTO) {
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

            BombaEls bombaEls = createDPOfromDTO(bombaDTO, categories);
            if (bombaEls.getTitle() != null) {
                repository.save(bombaEls);
            }
        }
    }

    private static BombaEls createDPOfromDTO(BombaDTO bombaDTO, List<String> categories) {
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
        return bombaEls;
    }

    public BombaEls save(BombaEls entity) {
        return repository.save(entity);
    }

    public Iterable<BombaEls> findAll() {
        return repository.findAll();
    }

//    private static void getBombaSitemap() {
//        String baseUrl = "https://bomba.md";
//        String robotsTxtUrl = baseUrl + "/robots.txt";
//        ArrayList<String> arrayList = new ArrayList<>();
//        try {
//            String robotsTxtContent = readRobotsTxt(robotsTxtUrl);
//            Map<String, List<String>> robotsRules = parseRobotsTxt(robotsTxtContent, baseUrl);
//            arrayList.addAll(robotsRules.get("Disallow"));
//            System.out.println(robotsTxtContent);
//            // TO DO Create a String... with Disallow urls and put it in parse configuration, then configurate threads and crawl-delay and useragent
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
