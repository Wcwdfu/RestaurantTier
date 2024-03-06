package com.site.restauranttier.user;

import com.site.restauranttier.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String userNickname;
    private String userEmail;
    private String userTokenId;
    private String loginApi;

    public SessionUser(User user) {
        this.userNickname = user.getUserNickname();
        this.userEmail = user.getUserEmail();
        this.userTokenId = user.getUserTokenId();
        this.loginApi = user.getLoginApi();
    }
}
