package com.example.aihelpdesk.common;

/**
 * @Author wzh
 * @Date 2026/6/4 02:31
 */
public final class CurrentUserContext {

    private static final ThreadLocal<CurrentUser> HOLDER = new ThreadLocal<>();

    private  CurrentUserContext () {}

    public   static void set (CurrentUser currentUser) {
        HOLDER.set(currentUser);
    }
    public static CurrentUser get () {
        return HOLDER.get();
    }
    public static CurrentUser getRequired () {
        CurrentUser currentUser = HOLDER.get();
        if (currentUser == null) {
            throw new IllegalStateException("当前用户不存在上下文");
        }
        return currentUser;
    }

    public  static void clear () {
        HOLDER.remove();
    }
}
