package com.security.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Base64;

public class App
{

  public static KeyPair generateKeyPair() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048, new SecureRandom());
    KeyPair pair = generator.generateKeyPair();

    return pair;
  }

  public static String sign(String plainText, PrivateKey privateKey) throws Exception {
    Signature privateSignature = Signature.getInstance("Sha1WithRSA");
    privateSignature.initSign(privateKey);
    privateSignature.update(plainText.getBytes("UTF-8"));

    byte[] signature = privateSignature.sign();

    return Base64.getEncoder().encodeToString(signature);
  }

  public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
    Signature publicSignature = Signature.getInstance("Sha1WithRSA");
    publicSignature.initVerify(publicKey);
    publicSignature.update(plainText.getBytes("UTF-8"));

    byte[] signatureBytes = Base64.getDecoder().decode(signature);

    return publicSignature.verify(signatureBytes);
  }

  public static void main( String[] args ) throws Exception
  {
    KeyPair pair = generateKeyPair();

    String signature = sign("foobar", pair.getPrivate());

    //Let's check the signature
    boolean isCorrect = verify("foobar", signature, pair.getPublic());
    System.out.println("Signature correct: " + isCorrect);

  }
}
