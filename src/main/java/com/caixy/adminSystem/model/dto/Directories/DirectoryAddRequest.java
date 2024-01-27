package com.caixy.adminSystem.model.dto.Directories;

import lombok.Data;

import java.io.Serializable;

/**
 * 新增目录分组请求体
 *
 * @name: com.caixy.adminSystem.model.dto.Directories.DirectoryAddRequest
 * @author: CAIXYPROMISE
 * @since: 2024-01-25 18:39
 **/
@Data
public class DirectoryAddRequest implements Serializable
{
    private String tagName;
    private static final long serialVersionUID = 1L;
}
