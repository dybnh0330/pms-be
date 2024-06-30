package com.binhnd.pmsbe.common.utils.jwt;

import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.services.result.impl.ResultServiceImpl;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SecurityUtils {


    public static byte[] signature(String data, PrivateKey privateKey) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(data.getBytes(StandardCharsets.UTF_8));
        return sign.sign();
    }

    public static PrivateKey getPrivateKey(String resourceName) throws IOException {
        InputStream stream = SecurityUtils.class.getClassLoader().getResourceAsStream(resourceName);
        assert stream != null;
        return getPrivateKey(new InputStreamReader(stream));
    }

    public static PrivateKey getPrivateKey(File privateKeyFile) throws IOException {
        try (PEMParser pemParser = new PEMParser(new FileReader(privateKeyFile))) {
            Object keyObject = pemParser.readObject();
            PEMDecryptorProvider provider = new JcePEMDecryptorProviderBuilder().build(null);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

            KeyPair keyPair;

            if (keyObject instanceof PEMEncryptedKeyPair) {
                keyPair = converter.getKeyPair(((PEMEncryptedKeyPair) keyObject).decryptKeyPair(provider));
            } else {
                keyPair = converter.getKeyPair((PEMKeyPair) keyObject);
            }

            return keyPair.getPrivate();
        }
    }

    public static PrivateKey getPrivateKey(Reader reader) throws IOException {
        try (PEMParser pemParser = new PEMParser(reader)) {
            Object keyObject = pemParser.readObject();
            PEMDecryptorProvider provider = new JcePEMDecryptorProviderBuilder().build(null);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

            KeyPair keyPair;

            if (keyObject instanceof PEMEncryptedKeyPair) {
                keyPair = converter.getKeyPair(((PEMEncryptedKeyPair) keyObject).decryptKeyPair(provider));
            } else {
                keyPair = converter.getKeyPair((PEMKeyPair) keyObject);
            }

            return keyPair.getPrivate();
        }
    }

    public static PublicKey getPublicKey(String resourceName) throws IOException {
        InputStream inputStream = SecurityUtils.class.getClassLoader().getResourceAsStream(resourceName);
        assert inputStream != null;
        return getPublicKey(new InputStreamReader(inputStream));
    }

    public static PublicKey getPublicKey(File publicKeyFile) throws IOException {
        try (PEMParser pemParser = new PEMParser(new FileReader(publicKeyFile))) {
            Object keyObject = pemParser.readObject();

            if (keyObject instanceof SubjectPublicKeyInfo) {
                return (new JcaPEMKeyConverter()).getPublicKey((SubjectPublicKeyInfo) keyObject);
            }

            return null;
        }
    }

    public static PublicKey getPublicKey(InputStreamReader reader) throws IOException {
        try (PEMParser pemParser = new PEMParser(reader)) {
            Object keyObject = pemParser.readObject();

            if (keyObject instanceof SubjectPublicKeyInfo) {
                return (new JcaPEMKeyConverter()).getPublicKey((SubjectPublicKeyInfo) keyObject);
            }

            return null;
        }
    }

    private static PublicKey getPublicKey() throws URISyntaxException,
            IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(
                Paths.get(ClassLoader.getSystemResource("publickey.pem").toURI()));

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static boolean verifySignature(String message, String sign, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getUrlDecoder().decode(sign);
        return signature.verify(signatureBytes);
    }

    public static Map<String, Object> getCurrentUser() {
        Map<String, Object> user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            user = (Map<String, Object>) authentication.getPrincipal();
        }
        return user;
    }

    public static String getCurrentUsername() {
        Map<String, Object> currentUser = getCurrentUser();
        if (currentUser != null && currentUser.containsKey("username")) {
            return currentUser.get("username").toString();
        }
        return null;
    }

    public static String getCurrentUserCode() {
        Map<String, Object> currentUser = getCurrentUser();
        if (currentUser != null && currentUser.containsKey("usercode")) {
            return currentUser.get("usercode").toString();
        }
        return null;
    }

    public static List<String> getCurrentScope() {
        Map<String, Object> currentUser = getCurrentUser();
        if (currentUser != null && currentUser.containsKey("scope")) {
            return (List<String>) currentUser.get("scope");
        }
        return Collections.emptyList();
    }


    private PrivateKey getPrivateKey() throws InvalidKeySpecException,
            NoSuchAlgorithmException, URISyntaxException, IOException {
        byte[] keyBytes = Files.readAllBytes(
                Paths.get(ClassLoader.getSystemResource("publickey.pem").toURI()));

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static void allowFilesTypeOrThrow(ServletContext servletContext, List<String> allowFileTypes, MultipartFile... files) throws PMSException {
        if (files == null) return;
        for (MultipartFile f : files) {
            String contentType = f.getContentType();

            allowFileTypeOrThrow(contentType, allowFileTypes);

            String mimeType = servletContext.getMimeType(f.getOriginalFilename());

            if (mimeType == null || !mimeType.equals(contentType)) {
                throw new PMSException(EnumPMSException.FILE_FORMAT_NOT_EQUAL);
            }
        }
    }

    public static void allowFileTypeOrThrow(String contentType, List<String> allowFileTypes) throws PMSException {
        if (!allowFileTypes.contains(contentType)) {
            throw new PMSException(EnumPMSException.FILE_FORMAT_NOT_ALLOW);
        }
    }
}
