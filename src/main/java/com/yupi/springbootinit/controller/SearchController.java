package com.yupi.springbootinit.controller;

import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.PageRequest;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.search.SearchRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.model.vo.searchVo;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author Lc
 * @Date 2023/7/28
 * @PackageName: com.yupi.springbootinit.controller
 * @ClassName: SearchController
 * @Description:
 */

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private PictureService pictureService;

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @PostMapping("/all")
    public BaseResponse<searchVo> getAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request){
        String type = searchRequest.getType();
        SearchTypeEnum enumByValue = SearchTypeEnum.getEnumByValue(type);
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        long pageSize = searchRequest.getPageSize();
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        searchVo searchVo = new searchVo();
        //搜索出所有内容
        if(enumByValue == null){
            Page<Picture> picture = pictureService.getPicture(pageSize, current, searchText);
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            Page<Post> postPage = postService.page(new Page<>(current, pageSize),
                    postService.getQueryWrapper(postQueryRequest));
            Page<PostVO> postVOPage = postService.getPostVOPage(postPage, request);

            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            Page<User> userPage = userService.page(new Page<>(current, pageSize),
                    userService.getQueryWrapper(userQueryRequest));
            Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
            List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
            userVOPage.setRecords(userVO);

            searchVo.setUserVOList(userVOPage);
            searchVo.setPostVOList(postVOPage);
            searchVo.setPictureList(picture);
        }else {
            switch (enumByValue) {
                case POST:
                    PostQueryRequest postQueryRequest = new PostQueryRequest();
                    postQueryRequest.setSearchText(searchText);
                    Page<Post> postPage = postService.page(new Page<>(current, pageSize),
                            postService.getQueryWrapper(postQueryRequest));
                    Page<PostVO> postVOPage = postService.getPostVOPage(postPage, request);
                    searchVo.setPostVOList(postVOPage);
                    break;
                case USER:
                    UserQueryRequest userQueryRequest = new UserQueryRequest();
                    userQueryRequest.setUserName(searchText);
                    Page<User> userPage = userService.page(new Page<>(current, pageSize),
                            userService.getQueryWrapper(userQueryRequest));
                    Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
                    List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
                    userVOPage.setRecords(userVO);
                    searchVo.setUserVOList(userVOPage);
                    break;
                case PICTURE:
                    Page<Picture> picture = pictureService.getPicture(pageSize, current, searchText);
                    searchVo.setPictureList(picture);
                    break;
            }
        }
        return ResultUtils.success(searchVo);
    }
}
