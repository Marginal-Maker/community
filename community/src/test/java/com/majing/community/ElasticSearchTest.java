package com.majing.community;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOptionsBuilders;
import co.elastic.clients.elasticsearch._types.SortOptionsVariant;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.majing.community.dao.DiscussPostMapper;
import com.majing.community.dao.elasticSearch.DiscussPostRepository;
import com.majing.community.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.*;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;

/**
 * @author majing
 * @date 2023-11-24 14:26
 * @Description
 */
@SpringBootTest
//指定配置文件
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticSearchTest {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private DiscussPostRepository discussPostRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testInsert(){
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(241));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(242));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(243));
    }
    @Test
    public void testInsertList(){
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(101, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(102, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(103, 0, 100));

    }
    @Test
    public void testDelete(){
//        discussPostRepository.deleteById(241);
        discussPostRepository.deleteAll();

    }
    @Test
    public void testSearch(){
        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(Queries.termQueryAsQuery("title", "互联网"))
                .withQuery(Queries.termQueryAsQuery("content", "互联网"))
                .withSort(Sort.by(Sort.Direction.DESC, "type"))
                .withSort(Sort.by(Sort.Direction.DESC, "score"))
                .withSort(Sort.by(Sort.Direction.DESC, "createTime"))
                .withPageable(PageRequest.of(0 ,10))
                .build();
    }
}
