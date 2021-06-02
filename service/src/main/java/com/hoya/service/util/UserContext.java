package com.hoya.service.util;

/**
 * 把User存储在本地线程，方便获取当前线程UserContext
 */

import com.hoya.service.model.MiaoShaUser;

public class UserContext {

    private static ThreadLocal<MiaoShaUser> userHolder = new ThreadLocal<MiaoShaUser>();

    public static void setUser(MiaoShaUser user) {
        userHolder.set(user);
    }

    public static MiaoShaUser getUser() {
        return userHolder.get();
    }

    public static void removeUser() {
        userHolder.remove();
    }

}
