package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;

/**
 * @Author Lc
 * @Date 2023/7/27
 * @PackageName: com.yupi.springbootinit.service
 * @ClassName: PictureService
 * @Description:
 */

public interface PictureService {

    Page<Picture> getPicture(long pageSize, long pageNum, String searchText);

}
