package com.majing.community.dao.elasticSearch;

import com.majing.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author majing
 * @date 2023-11-24 14:25
 * @Description
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
