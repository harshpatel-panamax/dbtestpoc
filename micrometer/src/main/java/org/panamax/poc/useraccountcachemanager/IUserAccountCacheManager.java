package org.panamax.poc.useraccountcachemanager;


import org.panamax.poc.useraccountcachemanager.ICacheManager;
import org.panamax.poc.useraccountcachemanager.UserAccountData;

public interface IUserAccountCacheManager extends ICacheManager<UserAccountData> {
    UserAccountData get(String id);
    void set(UserAccountData userAccountData);
}
