package nl.inholland.BankAPI.Security;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;

@Component
@Getter
public class JwtKeyProvider {
    @Value("${jwt.key-store}")
    private String keyStore;

    @Value("${jwt.key-store-password}123")
    private String keyStorePassword;

    @Value("${jwt.key-alias}")
    private String keyAlias;

    private Key privateKey;
    private PublicKey publicKey;

    @PostConstruct
    protected void init(){
        try {
            ClassPathResource resource = new ClassPathResource(keyStore);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(resource.getInputStream(), keyStorePassword.toCharArray());
            privateKey = keyStore.getKey(keyAlias, keyStorePassword.toCharArray());
            Certificate certificate = keyStore.getCertificate(keyAlias);
            publicKey = certificate.getPublicKey();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
