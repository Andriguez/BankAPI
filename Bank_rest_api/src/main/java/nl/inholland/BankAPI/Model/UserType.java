package nl.inholland.BankAPI.Model;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    GUEST, CUSTOMER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
