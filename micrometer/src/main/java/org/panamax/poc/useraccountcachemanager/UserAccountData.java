
/**
 *
 */
package org.panamax.poc.useraccountcachemanager;

/**
 * data class which contains the user information.
 * @author Dhruvil.dhanani
 */

public class UserAccountData {
    private String id;

    private boolean newField;

    public UserAccountData() {
        newField = true;
    }

    public UserAccountData(String id) {
        this.id = id;
        newField = true;
    }

    public String id() {
        return id;
    }

    public boolean newField() {
        return newField;
    }

    public UserAccountData setNewField(boolean newField) {
        this.newField = newField;
        return this;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserAccountData{" + "id='" + id + '\'' + ", newField=" + newField + '}';
    }
}
