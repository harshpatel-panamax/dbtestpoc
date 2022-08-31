/**
 *
 */
package org.panamax.poc.useraccountcachemanager;

/**
 * @author Dhruvil.dhanani
 */
public class DelegateUserAccountCacheManager extends DelegateBaseManage<UserAccountData> implements IUserAccountCacheManager {

    private final IUserAccountCacheManager cacheManager;

    public DelegateUserAccountCacheManager(IUserAccountCacheManager cacheManager,
            Handler handler, MatrixRecorder recorder,
            MatrixIdGenerator idGenerator) {
        super(cacheManager, handler, recorder, idGenerator);
        this.cacheManager = cacheManager;
    }

    @Override
    public UserAccountData get(String id) {
        return handle("get",
                      () -> cacheManager.get(id));
    }

    @Override
    public void set(UserAccountData userAccountData) {
        handle("set",
                      () -> cacheManager.set(userAccountData));
        ;
    }
}
