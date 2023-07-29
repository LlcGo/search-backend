package com.yupi.springbootinit.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Author Lc
 * @Date 2023/7/27
 * @PackageName: com.yupi.springbootinit.service.impl
 * @ClassName: PictureServiceImpl
 * @Description:
 */

@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public Page<Picture> getPicture(long pageSize, long pageNum, String searchText) {
        long current = (pageNum-1) * pageSize;
        String url = String.format("https://cn.bing.com/images/search?q=%s&first=%d",searchText,current);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements select = doc.select(".iuscp.isv");
        ArrayList<Picture> pictures = new ArrayList<>();
        for (Element element : select) {
            String m1 = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m1, Map.class);
            String picUrl = (String) map.get("murl");
            String title = (String) element.select(".inflnk").get(0).attr("aria-label");
            System.out.println(title);
            System.out.println(url);
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(picUrl);
            pictures.add(picture);
            if(pictures.size() >= pageSize){
                break;
            }
        }
        Page<Picture> picturePage = new Page<>(pageNum, pageSize);
        picturePage.setRecords(pictures);
        return  picturePage;
    }
}
