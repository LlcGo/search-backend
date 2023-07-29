package com.yupi.springbootinit.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.esdao.PostEsDao;
import com.yupi.springbootinit.model.dto.post.PostEsDTO;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Lc
 * @Date 2023/7/26
 * @PackageName: com.yupi.springbootinit.job.once
 * @ClassName: CrawlerJob
 * @Description:
 */

//@Component
@Slf4j
public class CrawlerJob implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
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
        if(b){
            log.info("成功爬取了" + posts.size() + "条数据");
        }else {
            log.error("爬取数据失败");
        }
    }
}
