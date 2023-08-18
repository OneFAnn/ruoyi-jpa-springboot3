package com.ruoyi.system.domain.idclazz;

import jakarta.persistence.Id;

public class SysUserPostId {
    /** 用户ID */
    @Id
    private Long userId;

    /** 岗位ID */
    @Id
    private Long postId;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getPostId()
    {
        return postId;
    }

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }
}
