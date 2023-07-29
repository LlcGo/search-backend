package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.picture.PicturePageReq;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author Lc
 * @Date 2023/7/27
 * @PackageName: com.yupi.springbootinit.controller
 * @ClassName: PictureController
 * @Description:
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    @PostMapping("/search/page/vo")
    public BaseResponse<Page<Picture>> searchPostVOByPage(@RequestBody PicturePageReq picturePageReq,
                                                         HttpServletRequest request) {

        long size = picturePageReq.getPageSize();
        long current = picturePageReq.getCurrent();

        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Picture> picture = pictureService.getPicture(size, current, picturePageReq.getSearchText());
        return ResultUtils.success(picture);
    }
}
