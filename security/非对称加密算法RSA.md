## Java 非对称加密算法RSA

非对称加密和对称加密算法相比，多了一把秘钥，为双秘钥模式，一个公开称为公钥，一个保密称为私钥。遵循公钥加密私钥解密，或者私钥加密公钥解密。非对称加密算法源于DH算法，后又有基于椭圆曲线加密算法的密钥交换算法ECDH，不过目前最为流行的非对称加密算法是RSA，本文简单记录下RSA的使用。

## RSA算法

RSA算法是最为典型的非对称加密算法，该算法由美国麻省理工学院（MIT）的Ron Rivest、Adi Shamir和Leonard Adleman三位学者提出，并以这三位学者的姓氏开头字母命名，称为RSA算法。

RSA算法的数据交换过程分为如下几步：

1. A构建RSA秘钥对；
2. A向B发布公钥；
3. A用私钥加密数据发给B；
4. B用公钥解密数据；
5. B用公钥加密数据发给A；
6. A用私钥解密数据。

JDK8支持RSA算法：

| 算法 | 秘钥长度            | 加密模式 | 填充模式                                                     |
| :--- | :------------------ | :------- | :----------------------------------------------------------- |
| RSA  | 512~16384位，64倍数 | ECB      | NoPadding PKCS1Padding OAEPWithMD5AndMGF1Padding OAEPWithSHA1AndMGF1Padding OAEPWithSHA-1AndMGF1Padding OAEPWithSHA-224AndMGF1Padding OAEPWithSHA-256AndMGF1Padding OAEPWithSHA-384AndMGF1Padding OAEPWithSHA-512AndMGF1Padding OAEPWithSHA-512/224AndMGF1Padding OAEPWithSHA-512/2256ndMGF1Padding |

代码例子：

```
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;


public class Demo {

    @Test
    public void test() throws Exception {
        String value = "mrbird's blog";
        // 加密算法
        String algorithm = "RSA";
        // 转换模式
        String transform = "RSA/ECB/PKCS1Padding";
        // 实例化秘钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        // 初始化，秘钥长度512~16384位，64倍数
        keyPairGenerator.initialize(512);
        // 生成秘钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 公钥
        PublicKey publicKey = keyPair.getPublic();
        System.out.println("RSA公钥: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        // 私钥
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("RSA私钥: " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        // ------ 测试公钥加密，私钥解密 ------
        Cipher cipher = Cipher.getInstance(transform);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] pubEncryptBytes = cipher.doFinal(value.getBytes());
        System.out.println("RSA公钥加密后数据: " + Base64.getEncoder().encodeToString(pubEncryptBytes));

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] priDecryptBytes = cipher.doFinal(pubEncryptBytes);
        System.out.println("RSA私钥解密后数据: " + new String(priDecryptBytes));

        // ------ 测试私钥加密，公钥解密 ------
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] priEncryptBytes = cipher.doFinal(value.getBytes());
        System.out.println("RSA私钥加密后数据: " + Base64.getEncoder().encodeToString(priEncryptBytes));

        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] pubDecryptBytes = cipher.doFinal(priEncryptBytes);
        System.out.println("RSA公钥解密后数据: " + new String(pubDecryptBytes));
    }
}
```



程序输出如下：

```
RSA公钥: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKBvz9cma+hXNiv2yXg6e1PyZhHVZm3bJXDvTJP2LyXo4vs9grH36Q9kNgr6quHtuU6fEoUxUu2zbEB8dkEWB9UCAwEAAQ==
RSA私钥: MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAoG/P1yZr6Fc2K/bJeDp7U/JmEdVmbdslcO9Mk/YvJeji+z2CsffpD2Q2Cvqq4e25Tp8ShTFS7bNsQHx2QRYH1QIDAQABAkEAjemZXORdesz52/WVzEVepai6ZHfw/Kdl/PmPMSoIFmz7mk55rprl2Akn2V0odSiHSnMWvDmOUIAvHaHF4Re4wQIhAN5GxVeF7ndyoWasxqIOVb6baNkUrapBM0nacPS4WA8JAiEAuMcvNM2Z1rW74JagoGlSIfRkNUqa+3LTCN/fK7VR2W0CICs/+gYduVjkpSMlW0ENKQH9m1kh/Oiz5xbnujLj676BAiBVGif7wdXgtcLaJYXFW7ygNtcQVFQdCz13EOTQVKpl4QIgY2YyH3vUYI2J68qCGtYjj5iNHUEwwze+Za1R7y0V43k=
RSA公钥加密后数据: O55w+9ve4QPcNDXNXF3W6O3J9UHxGBWlOM8W5RkKIslMR5xoUkBqIufWO2mz5MWezfxHB9yH1mPQgrv3H1hMKQ==
RSA私钥解密后数据: mrbird's blog
RSA私钥加密后数据: UtwtFxWstrg+fQV6WSw8PYY1YP5K9bjYH7uY20SQIJ5iWQ9FEERi8+ttk2MougQro66aiJRpdCeSpIVi09J+Ew==
RSA公钥解密后数据: mrbird's blog
```



可以看到，公钥加密私钥解密和私钥加密公钥解密的模式都可行。

## 公私钥获取

假如现在有RSA公钥：

```
MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKBvz9cma+hXNiv2yXg6e1PyZhHVZm3bJXDvTJP2LyXo4vs9grH36Q9kNgr6quHtuU6fEoUxUu2zbEB8dkEWB9UCAwEAAQ==
```



RSA私钥：

```
MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAoG/P1yZr6Fc2K/bJeDp7U/JmEdVmbdslcO9Mk/YvJeji+z2CsffpD2Q2Cvqq4e25Tp8ShTFS7bNsQHx2QRYH1QIDAQABAkEAjemZXORdesz52/WVzEVepai6ZHfw/Kdl/PmPMSoIFmz7mk55rprl2Akn2V0odSiHSnMWvDmOUIAvHaHF4Re4wQIhAN5GxVeF7ndyoWasxqIOVb6baNkUrapBM0nacPS4WA8JAiEAuMcvNM2Z1rW74JagoGlSIfRkNUqa+3LTCN/fK7VR2W0CICs/+gYduVjkpSMlW0ENKQH9m1kh/Oiz5xbnujLj676BAiBVGif7wdXgtcLaJYXFW7ygNtcQVFQdCz13EOTQVKpl4QIgY2YyH3vUYI2J68qCGtYjj5iNHUEwwze+Za1R7y0V43k=
```



需要将它们还原为PublicKey和PrivateKey对象，可以参考如下代码：

```
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class Demo {

    @Test
    public void test() throws Exception {
        String value = "mrbird's blog";
        // 加密算法
        String algorithm = "RSA";
        // 转换模式
        String transform = "RSA/ECB/PKCS1Padding";
        // RSA公钥BASE64字符串
        String rsaPublicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKBvz9cma+hXNiv2yXg6e1PyZhHVZm3bJXDvTJP2LyXo4vs9grH36Q9kNgr6quHtuU6fEoUxUu2zbEB8dkEWB9UCAwEAAQ==";
        // RSA私钥BASE64字符串
        String rsaPrivateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAoG/P1yZr6Fc2K/bJeDp7U/JmEdVmbdslcO9Mk/YvJeji+z2CsffpD2Q2Cvqq4e25Tp8ShTFS7bNsQHx2QRYH1QIDAQABAkEAjemZXORdesz52/WVzEVepai6ZHfw/Kdl/PmPMSoIFmz7mk55rprl2Akn2V0odSiHSnMWvDmOUIAvHaHF4Re4wQIhAN5GxVeF7ndyoWasxqIOVb6baNkUrapBM0nacPS4WA8JAiEAuMcvNM2Z1rW74JagoGlSIfRkNUqa+3LTCN/fK7VR2W0CICs/+gYduVjkpSMlW0ENKQH9m1kh/Oiz5xbnujLj676BAiBVGif7wdXgtcLaJYXFW7ygNtcQVFQdCz13EOTQVKpl4QIgY2YyH3vUYI2J68qCGtYjj5iNHUEwwze+Za1R7y0V43k=";

        // ------- 还原公钥 --------
        byte[] publicKeyBytes = Base64.getDecoder().decode(rsaPublicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // ------- 还原私钥 --------
        byte[] privateKeyBytes = Base64.getDecoder().decode(rsaPrivateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        // ------- 测试加解密 --------
        Cipher cipher = Cipher.getInstance(transform);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] pubEncryptBytes = cipher.doFinal(value.getBytes());
        System.out.println("RSA公钥加密数据: " + Base64.getEncoder().encodeToString(pubEncryptBytes));

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] priDecryptBytes = cipher.doFinal(pubEncryptBytes);
        System.out.println("RSA私钥解密数据: " + new String(priDecryptBytes));
    }
}
```



程序输出如下：

```
RSA公钥加密数据: PdSr+WRUWIxbA7stmZ03GCwDBnE3CyFL43bTskJmBilY+9lL63Jt0KxN0S2A4ombxvngbiB8PVZiqj1oSkgWpA==
RSA私钥解密数据: mrbird's blog
```



## 分段加解密

RSA加解密中必须考虑到的密钥长度、明文长度和密文长度问题。明文长度需要小于密钥长度，而密文长度则等于密钥长度。因此当加密内容长度大于密钥长度时，有效的RSA加解密就需要对内容进行分段。

这是因为，RSA算法本身要求加密内容也就是明文长度m必须满足`0<m<密钥长度n`。如果小于这个长度就需要进行padding，因为如果没有padding，就无法确定解密后内容的真实长度，字符串之类的内容问题还不大，以0作为结束符，但对二进制数据就很难，因为不确定后面的0是内容还是内容结束符。而只要用到padding，那么就要占用实际的明文长度，于是实际明文长度需要减去padding字节长度。我们一般使用的padding标准有NoPPadding、OAEPPadding、PKCS1Padding等，其中PKCS#1建议的padding就占用了11个字节。

以秘钥长度为1024bits为例：

```
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;


public class Demo {

    @Test
    public void test() throws Exception {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i <= 29; i++) {
            value.append("18cm");
        }
        System.out.println("待加密内容长度: " + value.toString().length());
        // 加密算法
        String algorithm = "RSA";
        // 转换模式
        String transform = "RSA/ECB/PKCS1Padding";
        // 实例化秘钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        // 初始化，秘钥长度512~16384位，64倍数
        keyPairGenerator.initialize(1024);
        // 生成秘钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 公钥
        PublicKey publicKey = keyPair.getPublic();
        System.out.println("RSA公钥: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        // 私钥
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("RSA私钥: " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        // ------ 测试公钥加密，私钥解密 ------
        Cipher cipher = Cipher.getInstance(transform);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] pubEncryptBytes = cipher.doFinal(value.toString().getBytes());
        System.out.println("RSA公钥加密后数据: " + Base64.getEncoder().encodeToString(pubEncryptBytes));

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] priDecryptBytes = cipher.doFinal(pubEncryptBytes);
        System.out.println("RSA私钥解密后数据: " + new String(priDecryptBytes));

        // ------ 测试私钥加密，公钥解密 ------
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] priEncryptBytes = cipher.doFinal(value.toString().getBytes());
        System.out.println("RSA私钥加密后数据: " + Base64.getEncoder().encodeToString(priEncryptBytes));

        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] pubDecryptBytes = cipher.doFinal(priEncryptBytes);
        System.out.println("RSA公钥解密后数据: " + new String(pubDecryptBytes));
    }
}
```



程序会抛出如下异常：

```
待加密内容长度: 120
RSA公钥: MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC79DkQcppMMl11r21OMYcTlLWMJP9ZZw9BdszZPu+D1kHijbETrae84AwOrNPqrl8/vpPh2q9BLkrkfQuvSLQHk6tuefVEyWRnnnEwYJzIbjuQPhEwKU7khqjhNXdoW/27AN7kyQwFFnbLHfkc/lh6V6N6S2g5J2NmQL4hfqVgGwIDAQAB
RSA私钥: MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALv0ORBymkwyXXWvbU4xhxOUtYwk/1lnD0F2zNk+74PWQeKNsROtp7zgDA6s0+quXz++k+Har0EuSuR9C69ItAeTq2559UTJZGeecTBgnMhuO5A+ETApTuSGqOE1d2hb/bsA3uTJDAUWdssd+Rz+WHpXo3pLaDknY2ZAviF+pWAbAgMBAAECgYA2ksoC6ZO9rh4O7rnpK15SJCq2n4N5HQCD/I+sQKbg+9QziPqygQikQdWeaTY6/Rhw9NARkyKx5VQfleNPqOeEj1KwNK8pctD7nkb/PL/LZofH1uk1J0sgaSPpox2LUrIabWFs/dztbHpR/aiaQLbLfrdOgkGoQWM3FB8hMEsbSQJBAPhD/US0C6VQuMqLytuOsB7imqNttD8F6gGQAXAGz2YcgDHSHdayzhT1q12J0nrzfJGfZLZpc+4t9szS9Oh3VvcCQQDBzznlqTbaq7KaxAacdo6BRQszVMuy9kJXupINUUyw+wEaCiz4sxCJsa8ASfJBnxRGFEPyi9Hea6ijOwckDYL9AkEAkAPYqn8K9mYCHDTFg2GdVv06mS0tTxXeLfPccaDxtJk54Cyz9HSayVvNgaBOgdY2376nzI0VnAf7z8tcGHIJ9wJAcPwb5pU1U1mRL8RjjkdXYGkd1Hj0n4oMtxQfHQBuUyahR8ry2LGbTIp3WRXC0xqoOQqLahS07pOYpkA9M3llCQJBAN4oaSLXsSpZtnwekocGapsBaY62Kn9QIZGaGHJkmAwXBXEdXZfr/16BhUd4JSlfGgL3CvdP57OaWjl0CPZ0wxs=

javax.crypto.IllegalBlockSizeException: Data must not be longer than 117 bytes

	at com.sun.crypto.provider.RSACipher.doFinal(RSACipher.java:344)
	at com.sun.crypto.provider.RSACipher.engineDoFinal(RSACipher.java:389)
	at javax.crypto.Cipher.doFinal(Cipher.java:2164)
	at cc.mrbird.security.temp.rsa.Demo.test(Demo.java:42)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:33)
	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:230)
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:58)
```



对于1024长度的密钥。128字节（1024bits/8）减去PKCS#1建议的padding就占用了11个字节正好是117字节。所以加密的明文长度120字节大于117字节，程序抛出了异常。

要解决这个问题，可以采用分段加密的手段。编写一个分段加解密的工具类：

```
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * RSA分段加解密
 * 针对秘钥长度为1024bits
 */
public class RsaUtil {

    // 最大加密块长度 1024/8 - 11
    private static final int MAX_ENCRYPT_BLOCK = 117;
    // 最大解密块长度 1024/8
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final String TRANSFORM = "RSA/ECB/PKCS1Padding";

    /**
     * 公钥加密
     *
     * @param publicKey 公钥
     * @param value     待加密值
     * @return 加密值
     */
    public static String encrypt(PublicKey publicKey, String value) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Cipher cipher = Cipher.getInstance(TRANSFORM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = value.getBytes();
            int length = bytes.length;

            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (length - offSet > 0) {
                if (length - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(bytes, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(bytes, offSet, length - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥解密
     *
     * @param privateKey 私钥
     * @param encrypt    带解密值
     * @return 解密值
     */
    public static String decrypt(PrivateKey privateKey, String encrypt) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Cipher cipher = Cipher.getInstance(TRANSFORM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytes = Base64.getDecoder().decode(encrypt);
            int length = bytes.length;

            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (length - offSet > 0) {
                if (length - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(bytes, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(bytes, offSet, length - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
```

测试：

```
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;


public class Demo {

    @Test
    public void test() throws Exception {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i <= 29; i++) {
            value.append("18cm");
        }
        System.out.println("待加密内容长度: " + value.toString().length());
        // 加密算法
        String algorithm = "RSA";
        // 实例化秘钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        // 初始化，秘钥长度512~16384位，64倍数
        keyPairGenerator.initialize(1024);
        // 生成秘钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 公钥
        PublicKey publicKey = keyPair.getPublic();
        System.out.println("RSA公钥: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        // 私钥
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("RSA私钥: " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        // ------ 测试公钥加密，私钥解密 ------
        String pubEncrypt= RsaUtil.encrypt(publicKey, value.toString());
        System.out.println("RSA公钥加密后数据: " + pubEncrypt);

        String priDecrypt = RsaUtil.decrypt(privateKey, pubEncrypt);
        System.out.println("RSA私钥解密后数据: " + priDecrypt);

    }
}
```



程序输出如下：

```
待加密内容长度: 120
RSA公钥: MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJcyiFAbUHBYXe5BTY/TF5Bn4/fW4L0dK2eaDSPJr4uqTFxIj+sRDqRq71yZw3KJk0qxmmGbtMRQGuR+GVAyJ/0E2R3q2RM+aWCZmkzyDnq6xHIvV0d3mU3N8EDtPS6iO+ANOEPNKfdzr+BJN8NKnpXC2ii7phvMk/QlYqjVAbIwIDAQAB
RSA私钥: MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIlzKIUBtQcFhd7kFNj9MXkGfj99bgvR0rZ5oNI8mvi6pMXEiP6xEOpGrvXJnDcomTSrGaYZu0xFAa5H4ZUDIn/QTZHerZEz5pYJmaTPIOerrEci9XR3eZTc3wQO09LqI74A04Q80p93Ov4Ek3w0qelcLaKLumG8yT9CViqNUBsjAgMBAAECgYEAhaU2UdVuGoyxNR9acf4GW6IHoV4pYU68bnbm+2S4Xn7EdhN6DQNH6jOeLRjCTxOnnAF95/Z/GlLCpp335nbs1B1wlb6MP5l3keO2KhYuvPhnZdPInNV0aKWzOoX7gcmy12g5IAQgoYoc/IohPIpMmkgrbuGQGk2+jxnGPETgNdECQQDhDyP3VIH5AucbHHeL6fSU9kswO1eejLzRmvlKIwk15wE1xteFvIqpLOLlR8wJm+Eb5uB0HEr4X7rlWDQLq/OZAkEAnFilYybZfp4rRctsnYjFZf0QGUCCBV9hFa7xoGztV2rAkLLmsnayXzSJpOYYAOI7ekrqfLL3xALQKn8DZtr6GwJAecd9iKl7oshFUVA4B8dShwA2cyTJJou06B5ZYhpPM5GKABVWLZF13lDhfXs6FsD4L+bf8TQWBQuXz93IW8BxkQJAZqfR2BuPHRMPiKE77Of77K5PnrT7ajmpDkqy/knnQMmoLJo63Z0QG3Dsm6g0xIfG09JSypPcGQhb1DtXaXaIVwJAVVSPa1caRWLKYlEKAi1gBbrC5Zt7aTQ/ska2E3ksAhaVhScPBOEIoQf9EdbGajmpuueWeH9IlVrqQv0vFNY4gA==
RSA公钥加密后数据: cy7Var2L72bgne9F8iGro+SCQxs2ejIMPwQDJ5hQFTLvyqtT4ZJYM5i2ClgOD9viAP2Tp/X5cCX0+K1xz88hf5w/xNPWonzdaJNa2J5gQv7KGxNe/pW4mtpf878u4sIvO9sT8AktWtJC3jFtvxL9u9vJdzWl99RSRf/3sNqWj3gLRM/YpCcGM0HPuDsyUdOA4q+Tn+d2nOf36XrBtjIl2QyOTwnMoMCbC9Hlt6jN8fMsSFW8oiFNqV/+HPlhs5ZtFixhUE6SryketJfzXGmUSXH5cM/+11pB2bBxrCvtqRUE5/MZKjL2kKmrZan3kDHi4aiwLSDdpYdZn0urrJObAA==
RSA私钥解密后数据: 18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm18cm
```



## 建议

1. 公钥是通过A发送给B的，其在传递过程中很有可能被截获，也就是说窃听者很有可能获得公钥。如果窃听者获得了公钥，向A发送数据，A是无法辨别消息的真伪的。因此，虽然可以使用公钥对数据加密，但这种方式还是会有存在一定的安全隐患。如果要建立更安全的加密消息传递模型，就需要AB双方构建两套非对称加密算法密钥，仅遵循“私钥加密，公钥解密”的方式进行加密消息传递；
2. RSA不适合加密过长的数据，虽然可以通过分段加密手段解决，但过长的数据加解密耗时较长，在响应速度要求较高的情况下慎用。一般推荐使用非对称加密算法传输对称加密秘钥，双方数据加密用对称加密算法加解密。