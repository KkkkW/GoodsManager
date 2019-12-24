package ez.manager.Util;


import ez.manager.Model.User;

import java.util.HashSet;

public class RunnableThreadWebCount implements Runnable {

    private static HashSet<User> set = new HashSet<User>();

    @Override
    public void run() {
        System.out.println("计数线程已经启动.....");
    }

    public static int addCount(User user) {
        set.add(user);
        System.out.println("网站访问人数：" + set.size());
        return set.size();
    }
    public static int delUser(User user){
        set.remove(user);
        System.out.println("网站访问人数：" + set.size());
        return set.size();
    }
}
