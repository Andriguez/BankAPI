package nl.inholland.BankAPI.Model;

import org.springframework.security.core.GrantedAuthority;


public enum AccountStatus implements GrantedAuthority {
    ACTIVE, INACTIVE;
    @Override
    public String getAuthority() {
        return name();
    }
}
