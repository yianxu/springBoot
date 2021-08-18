package com.es.elsaticsearch.service;

import com.alibaba.fastjson.JSON;
import com.es.elsaticsearch.entity.Content;
import com.es.elsaticsearch.utils.HtmlParseUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.MacAddressProvider;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : ywb
 * @createdDate : 2020/5/31
 * @updatedDate
 */
@Service
public class ContentService {
    @Autowired
    private HtmlParseUtils htmlParseUtils;

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    /**
     * 插叙数据
     * @param keyWords
     * @return
     * @throws IOException
     */
    public Boolean parseContent(String keyWords) throws IOException {
        List<Content> contents = htmlParseUtils.listGoods(keyWords);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        for (int i = 0; i < contents.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("dong")
                    .source(JSON.toJSONString(contents.get(i)), XContentType.JSON));

        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    public List<Content> list(String keyWords) throws IOException {
        List<Content> contents = htmlParseUtils.listGoods(keyWords);
        return contents;
    }

    /**
     * 分页查询ElasticSearch数据，并高亮显示
     * @param keyWords
     * @param pageNum
     * @param pageSize
     * @return
     * @throws IOException
     */
    public List<Map<String,Object>> searchPage(String keyWords,Integer pageNum,Integer pageSize) throws IOException {
        if(pageNum<=1){
            pageNum=1;
        }
        SearchRequest searchRequest = new SearchRequest("jd");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //分页
        sourceBuilder.from(pageNum);
        sourceBuilder.size(pageSize);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title",keyWords);
        sourceBuilder.query(termQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        //多个高亮显示设置为true
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style = 'color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        //执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //解析结果
        List<Map<String, Object>> maps = new ArrayList<>();
        for (SearchHit hit : search.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            //原来的结果
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //解析高亮字段 ,将原来的字段换成高亮字段显示
            if(title!=null){
                Text[] fragments = title.fragments();
                String newTitle = "";
                for (Text fragment : fragments) {
                    newTitle+=fragment;
                }
                //高亮字段替换原来内容
                sourceAsMap.put("title",newTitle);
            }
            maps.add(hit.getSourceAsMap());
        }
        return maps;
    }
}
