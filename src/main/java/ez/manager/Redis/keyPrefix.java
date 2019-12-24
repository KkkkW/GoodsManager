package ez.manager.Redis;


/*Key前缀缓冲器*/
public interface keyPrefix {

    /*有效期*/
    public int expireSeconds();

    /*前缀*/

    public String getPrefix();

}
