package md.crawler.server.repositories.bomba;

import md.crawler.server.model.BombaEls;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BombaRepository extends ElasticsearchRepository<BombaEls, String> {
    // Additional query methods can be defined here
}