package ez.manager.Redis;

public class Dashboardkey extends BasePrefix {

    public static final int BOARD_EXPIRE = 3600 * 24 * 2;//默认两天

    private Dashboardkey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static Dashboardkey board = new Dashboardkey(BOARD_EXPIRE, "board");
}
