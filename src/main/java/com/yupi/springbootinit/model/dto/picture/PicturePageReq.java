package com.yupi.springbootinit.model.dto.picture;

import com.yupi.springbootinit.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Lc
 * @Date 2023/7/27
 * @PackageName: com.yupi.springbootinit.model.dto.picture
 * @ClassName: PicturePageReq
 * @Description:
 */

@Data
public class PicturePageReq extends PageRequest implements Serializable {

    String title;

    String searchText;

    private static final long serialVersionUID = 1L;
}
