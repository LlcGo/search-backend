package com.yupi.springbootinit.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Lc
 * @Date 2023/7/28
 * @PackageName: com.yupi.springbootinit.model.vo
 * @ClassName: searchVo
 * @Description:
 */

@Data
public class searchVo implements Serializable {

    private Page<UserVO> userVOList;

    private Page<PostVO> postVOList;

    private Page<Picture> pictureList;

    private static final long serialVersionUID = 1L;
}
