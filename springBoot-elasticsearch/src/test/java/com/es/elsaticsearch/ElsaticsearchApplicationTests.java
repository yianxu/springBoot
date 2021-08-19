package com.es.elsaticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.es.elsaticsearch.entity.Book;
import com.es.elsaticsearch.entity.User;
import com.es.elsaticsearch.repository.BookRepository;
import com.es.elsaticsearch.service.ContentService;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElsaticsearchApplicationTests {

    /**
     * 方式一：ElasticsearchTemplate操作ES
     */
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 方式二：编写一个ElasticsearchRepository子接口来操作ES
     */
    @Autowired
    private BookRepository bookRepository;


    @Test
    public void test02(){
        Book book = new Book();
        book.setId(2);
        book.setBookName("JAVA");
        book.setAuthor("xujian");
        Book b = this.bookRepository.save(book);
    }

    //创建索引
    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("dong");
        CreateIndexResponse response = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    /**
     * 测试索引是否存在
     *
     * @throws IOException
     */
    @Test
    public void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("ywb");
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("ywb");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    /**
     * 测试添加文档
     *
     * @throws IOException
     */
    @Test
    public void createDocument() throws IOException {
        User user = new User("ywb", 18);
        IndexRequest request = new IndexRequest("ywb");
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");
        //将我们的数据放入请求，json
        request.source(JSON.toJSONString(user), XContentType.JSON);
        //客服端发送请求
        IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(index.toString());
        //对应我们的命令返回状态
        System.out.println(index.status());
    }

    //判断是否存在文档
    @Test
    public void testIsExist() throws IOException {
        GetRequest getRequest = new GetRequest("ywb", "1");
        //不获取返回的source的上下文
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //获取文档信息
    @Test
    public void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("ywb", "1");
        GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        //打印文档信息
        System.out.println(response.getSourceAsString());
        System.out.println(response);
    }

    //更新文档信息
    @Test
    public void testUpdateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("ywb", "1");
        request.timeout("1s");
        User user = new User("ywb java", 19);
        request.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse update = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println(update);
        System.out.println(update.status());
    }
    //删除文档
    @Test
    public void testDeleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("ywb", "1");
        request.timeout("10s");
        User user = new User("ywb java", 19);
        DeleteResponse update = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(update.status());
    }
    //批量插入数据
    @Test
    public void testBulkRequest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("zhangsan", 1));
        users.add(new User("lishi", 12));
        users.add(new User("wangwu", 13));
        users.add(new User("zhaoliu", 14));
        users.add(new User("tianqi", 15));
        for (int i = 0; i < users.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("ywb")
                            .id("" + i + 1)
                            .source(JSON.toJSONString(users.get(i)), XContentType.JSON)
            );
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk);

    }

    /**
     * 搜索请求
     * 条件构造
     *
     * @throws IOException
     */
    @Test
    public void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("dong");
        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询所有
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
//        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("name","zhangsan");
        searchSourceBuilder.query(matchAllQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        System.out.println("=======");
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }
    //日志信息输出
    private static Logger logger = LoggerFactory.getLogger(ElsaticsearchApplicationTests.class);

    @Test
    public void search(){
        SearchRequest searchRequest = new SearchRequest("ywb");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchAllQueryBuilder matchAllQueryBuilder = new MatchAllQueryBuilder();
        String writeableName = matchAllQueryBuilder.getWriteableName();
        logger.info(writeableName);
    }

    @Autowired
    private ContentService contentService;

    @Test
    public void test() throws IOException {
        contentService.parseContent("程序员");
    }

    @Test
    public void testSearchContent() throws IOException {
        List<Map<String, Object>> java = contentService.searchPage("python", 1, 15);
        for (Map<String, Object> stringObjectMap : java) {
            for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
                System.out.println(stringObjectEntry);
            }
        }
    }

    @Test
    public void stream(){
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("张三",18));
        users.add(new User("李四",19));
        users.add(new User("王五",20));
        users.add(new User("赵六",21));
        users.add(new User("田七",22));

        users.stream().filter((u) -> u.getAge() > 18).forEach(System.out::println);
        long count = users.stream().filter((u) -> u.getName().equals("张三")).count();
        System.out.println(count);
        List<Integer> collect = users.stream().map(User::getAge).collect(Collectors.toList());
        Map<String, User> collect1 = users.stream().collect(Collectors.toMap(User::getName, v -> v, (o, n) -> n));
        for (Map.Entry<String, User> stringUserEntry : collect1.entrySet()) {
            System.out.println("key:"+stringUserEntry.getKey()+","+"value:"+stringUserEntry.getValue());
        }
    }

    @Test
    public void testString(){
        List<String>list1=new ArrayList<>();
        list1.add("a");
        list1.add("b");
        list1.add("c");
        list1.add("d");
        list1.add("e");


        List<String>list2=new ArrayList<>();
        list2.add("a");
        list2.add("b");
        list2.add("c");
        list2.add("d");

        list1.addAll(list2);
        System.out.println("添加到list1");
        for (String string : list1) {
            System.out.println(string);
        }
        list1.removeAll(list2);
        System.out.println("去除重复");
        for (String string : list1) {
            System.out.println(string);
        }
    }

    @Test
    public void testRemove(){
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1,"张三",12));
        users.add(new User(2,"李四",13));
        users.add(new User(3,"王五",14));
        users.add(new User(4,"赵六",15));
        System.out.println(users);
        Object o = JSONObject.toJSON(users);
        System.out.println(o);
        String s = JSONObject.toJSONString(users);
        JSONArray objects = JSON.parseArray(s);
        List<User> users1 = objects.toJavaList(User.class);
        System.out.println(s);

//        ArrayList<User> list = new ArrayList<>();
//        list.add(new User(1,"张三1",12));
//        list.add(new User(2,"李四1",13));
//
//        boolean b = users.removeAll(list);
//
//        for (User user : users) {
//            System.out.println(user.getName());
//        }
//        System.out.println("=========");
//        boolean b1 = users.addAll(list);
//        for (User user : users) {
//            System.out.println(user);
//        }
    }

    @Test
    public void testObject(){
        boolean equals = new User(1, "张三", 12).equals(new User(1, "张三", 12));
        System.out.println(equals);
    }
}
