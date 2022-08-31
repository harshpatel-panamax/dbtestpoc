/**
 *
 */
package org.panamax.poc.useraccountcachemanager;

/**
 * @author Dhruvil.dhanani
 */
public class UserAccountCacheManager extends BaseManager implements IUserAccountCacheManager {
    private final RedisUserAccountCacheDriver driver;

    public UserAccountCacheManager(RedisUserAccountCacheDriver driver) {
        this.driver = driver;
    }

    @Override
    public UserAccountData get(String id) {
        return driver.getById(id);
    }

    @Override
    public void set(UserAccountData userAccountData) {
        driver.set(userAccountData);
    }
}
