package com.yupi.springbootinit.model.dto.search;

import com.yupi.springbootinit.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Lc
 * @Date 2023/7/28
 * @PackageName: com.yupi.springbootinit.model.dto.search
 * @ClassName: SearchRequest
 * @Description:
 */

@Data
public class SearchRequest extends PageRequest implements Serializable {

    private String searchText;

    private String type;

    private static final long serialVersionUID = 1L;

}
