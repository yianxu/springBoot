package com.es.elsaticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElsaticsearchApplicationTests {

    //??????????????????
    private static final Logger logger = LoggerFactory.getLogger(ElsaticsearchApplicationTests.class);
    /**
     * ????????????ElasticsearchTemplate??????ES
     */
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    /**
     * ????????????????????????ElasticsearchRepository??????????????????ES
     */
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ContentService contentService;

    @Test
    public void test02() {
        Book book = new Book();
        book.setId(1);
        book.setBookName("?????????");
        book.setAuthor("?????????");
        this.bookRepository.save(book);
        List<Book> bookList = bookRepository.findByBookNameLike("???");
        for (Book b : bookList) {
            System.out.println(b.getBookName());
        }
    }

    //????????????
    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("dong");
        CreateIndexResponse response = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    /**
     * ????????????????????????
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
     * ????????????
     */
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("ywb");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    /**
     * ??????????????????
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
        //?????????????????????????????????json
        request.source(JSON.toJSONString(user), XContentType.JSON);
        //?????????????????????
        IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(index.toString());
        //?????????????????????????????????
        System.out.println(index.status());
    }

    //????????????????????????
    @Test
    public void testIsExist() throws IOException {
        GetRequest getRequest = new GetRequest("ywb", "1");
        //??????????????????source????????????
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //??????????????????
    @Test
    public void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("ywb", "1");
        GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        //??????????????????
        System.out.println(response.getSourceAsString());
        System.out.println(response);
    }

    //??????????????????
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

    //????????????
    @Test
    public void testDeleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("ywb", "1");
        request.timeout("10s");
        User user = new User("ywb java", 19);
        DeleteResponse update = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(update.status());
    }

    //??????????????????
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
     * ????????????
     * ????????????
     *
     * @throws IOException
     */
    @Test
    public void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("dong");
        //??????????????????
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //????????????
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

    @Test
    public void search() {
        SearchRequest searchRequest = new SearchRequest("ywb");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchAllQueryBuilder matchAllQueryBuilder = new MatchAllQueryBuilder();
        String writeableName = matchAllQueryBuilder.getWriteableName();
        logger.info(writeableName);
    }

    @Test
    public void test() throws IOException {
        contentService.parseContent("?????????");
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
    public void stream() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("??????", 18));
        users.add(new User("??????", 19));
        users.add(new User("??????", 20));
        users.add(new User("??????", 21));
        users.add(new User("??????", 22));

        users.stream().filter((u) -> u.getAge() > 18).forEach(System.out::println);
        long count = users.stream().filter((u) -> u.getName().equals("??????")).count();
        System.out.println(count);
        List<Integer> collect = users.stream().map(User::getAge).collect(Collectors.toList());
        Map<String, User> collect1 = users.stream().collect(Collectors.toMap(User::getName, v -> v, (o, n) -> n));
        for (Map.Entry<String, User> stringUserEntry : collect1.entrySet()) {
            System.out.println("key:" + stringUserEntry.getKey() + "," + "value:" + stringUserEntry.getValue());
        }
    }

    @Test
    public void testString() {
        List<String> list1 = new ArrayList<>();
        list1.add("a");
        list1.add("b");
        list1.add("c");
        list1.add("d");
        list1.add("e");


        List<String> list2 = new ArrayList<>();
        list2.add("a");
        list2.add("b");
        list2.add("c");
        list2.add("d");

        list1.addAll(list2);
        System.out.println("?????????list1");
        for (String string : list1) {
            System.out.println(string);
        }
        list1.removeAll(list2);
        System.out.println("????????????");
        for (String string : list1) {
            System.out.println(string);
        }
    }

    @Test
    public void testRemove() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "??????", 12));
        users.add(new User(2, "??????", 13));
        users.add(new User(3, "??????", 14));
        users.add(new User(4, "??????", 15));
        System.out.println(users);
        Object o = JSONObject.toJSON(users);
        System.out.println(o);
        String s = JSONObject.toJSONString(users);
        JSONArray objects = JSON.parseArray(s);
        List<User> users1 = objects.toJavaList(User.class);
        System.out.println(s);

//        ArrayList<User> list = new ArrayList<>();
//        list.add(new User(1,"??????1",12));
//        list.add(new User(2,"??????1",13));
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
    public void testObject() {
        boolean equals = new User(1, "??????", 12).equals(new User(1, "??????", 12));
        System.out.println(equals);
    }
}
