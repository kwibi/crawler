package md.crawler.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Service
@AllArgsConstructor
public class BombaServiceWebMagic implements PageProcessor {

    @Override
    public void process(Page page) {
        // Extract product page URLs from listings
        page.addTargetRequests(page.getHtml().links().regex("(https://bomba\\.md/item/\\d+)").all());

        // Extract data from product detail pages
        String title = page.getHtml().xpath("//h2[@class='product-title']/a/text()").toString();
        String price = page.getHtml().xpath("//span[@class='product-price']/text()").toString();
        String url = page.getHtml().xpath("//h2[@class='product-title']/a/@href").toString();

        // Put extracted data into result items
        page.putField("title", title);
        page.putField("price", price);
        page.putField("url", url);

        // If title is null, skip this page
        if (page.getResultItems().get("title") == null) {
            page.setSkip(true);
        }

        // Add logic to handle pagination
        // Identify the XPath for the "Next" button or pagination link
        String nextPageUrl = page.getHtml().xpath("//a[@class='next']/@href").toString();
        if (nextPageUrl != null) {
            page.addTargetRequest(nextPageUrl);
        }
    }

    @Override
    public Site getSite() {
        return Site.me()
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                .setRetryTimes(3)
                .setSleepTime(1000)
                .addCookie("sessionid", "11233323");
    }

}
