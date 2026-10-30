package nl.inholland.BankAPI.Security;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;

@Component
@Getter
public class JwtKeyProvider {
    //F2 Fix: path and password come from environment variables
    @Value("${jwt.key-store}")
    private Resource keyStore;

    @Value("${jwt.key-store-password}")
    private String keyStorePassword;

    @Value("${jwt.key-alias}")
    private String keyAlias;

    private Key privateKey;
    private PublicKey publicKey;

    @PostConstruct
    protected void init(){
        try {
            KeyStore store = KeyStore.getInstance("PKCS12");
            store.load(keyStore.getInputStream(), keyStorePassword.toCharArray());
            privateKey = store.getKey(keyAlias, keyStorePassword.toCharArray());
            Certificate certificate = store.getCertificate(keyAlias);
            publicKey = certificate.getPublicKey();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
