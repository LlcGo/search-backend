package com.yupi.springbootinit;

import cn.hutool.core.lang.Assert;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Lc
 * @Date 2023/7/26
 * @PackageName: com.yupi.springbootinit
 * @ClassName: crawlerTest
 * @Description:
 */

@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;

    @Test
    public void testDoc() throws IOException {
        Document doc = Jsoup.connect("https://zhuanlan.zhihu.com/p/578391279").get();
        Elements newsHeadlines = doc.select("RichText ztext Post-RichText css-1g0fqss");
        Elements img = doc.getElementsByTag("img");
        for (Element element : img) {
            Elements img1 = element.getElementsByTag("img");
            String src = img1.attr("src");
            if(src.startsWith("data")){
                continue;
            }
            System.out.println(src);
        }
    }

    @Test
    public void testBiYing() throws IOException {
        Document doc = Jsoup.connect("https://cn.bing.com/images/search?q=小黑子&first=1").get();
        Elements select = doc.select(".iuscp.isv");
        for (Element element : select) {
            String m1 = element.select(".iusc").get(0).attr("m");
            Map<String,Object> map = JSONUtil.toBean(m1,Map.class);
            String url = (String)map.get("murl");
            String title = (String)element.select(".inflnk").get(0).attr("aria-label");
            System.out.println(title);
            System.out.println(url);
        }
    }

    @Test
    public void test(){
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String result = HttpRequest.post("https://www.code-nav.cn/api/post/search/page/vo")
                .body(json)
                .execute().body();
        System.out.println(result);
        Map<String,Object> map =JSONUtil.toBean(result,Map.class);
        System.out.println(map);
        JSONObject data = (JSONObject)map.get("data");
        JSONArray records = (JSONArray)data.get("records");
        System.out.println(records);
        ArrayList<Post> posts = new ArrayList<>();
        for (Object record : records) {
            JSONObject record1 = (JSONObject)record;
            String title = record1.getStr("title");
            String content = record1.getStr("content");
            JSONArray tags = (JSONArray)record1.get("tags");
            List<String> tagList = tags.toList(String.class);
            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            posts.add(post);
        }
        boolean b = postService.saveBatch(posts);
        Assert.isTrue(b);
    }
}
