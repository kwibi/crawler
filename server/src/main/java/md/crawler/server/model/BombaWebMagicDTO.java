package md.crawler.server.model;

import com.xuxueli.crawler.annotation.PageSelect;
import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;

@Data
@PageSelect(cssQuery = "body")
public class BombaWebMagicDTO {
    @ExtractBy(value = "body > div.main > div.product-title-box > div > div.title-6 > h1")
    private String title;
    @ExtractBy(value = "body > div.main > div.product-page > div.product-page-in > div.product-page-item.product-page-item-action > div > div > div.product-page-trade-box-in > div > div.product-price-wrap1 > div.product-newprice2 > span")
    private String newPrice;
    @ExtractBy(value = "body > div.main > div.product-page > div.product-page-in > div.product-page-item.product-page-item-action > div > div > div.product-page-trade-box-in > div > div.product-price-wrap1 > div.product-newprice2 > div")
    private String oldPrice;
    @ExtractBy(value = "body > div.main > div.catalog > div > div.catalog-item2 > div.catalog-item2-ajax > div.catalog-product-box-in > div:nth-child(1) > div.item-product-in.card_product > div.product-sector-three > div.product-price > div > div.aac-price > div.aac-price-main > span")
    private String mainPrice;
    @ExtractBy(value = "body > div.main > div.product-page > div.product-page-in > div.product-page-item.product-page-item-action > div > div > div.product-page-trade-box-in > div > div.product-price-wrap1 > div.price-reduce > span.v2")
    private String discount;
    @ExtractBy(value = "body > div.main > div.product-page > div.product-page-in > div.product-page-item.product-page-item-action > div > div > p")
    private String availability;
    @ExtractBy(value = "body > div.main > div.product-page > div.product-page-in > div.product-page-item.product-page-item-action > div > div > div.product-page-trade-box-in > div > div:nth-child(6) > div > div")
    private String availabilityTwo;
    @ExtractBy(value = "body > div.main > div.breads > div")
    private String breads;
    @ExtractBy(value = "#tabs-1 > div > div > div > div:nth-child(1)")
    private String specifications;

    private Boolean isAvailable;

}