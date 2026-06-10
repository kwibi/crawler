package md.crawler.server.repositories.bomba;

import md.crawler.server.model.Atehno;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtehnoRepository extends ElasticsearchRepository<Atehno, String>
{
}
