package nl.inholland.BankAPI.Model;

import org.springframework.security.core.GrantedAuthority;

public enum AccountType implements GrantedAuthority {
    CURRENT, SAVINGS;
    @Override
    public String getAuthority() {
        return name();
    }
}

