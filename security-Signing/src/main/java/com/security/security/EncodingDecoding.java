package com.security.security;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncodingDecoding {

  private static String symmetricKey = "topSecretKey1234";
  private static String symmetricAlg = "AES";

  private static byte[] encodeSymetric(byte[] data) throws Exception {
    Cipher c = Cipher.getInstance(symmetricAlg);
    SecretKeySpec k = new SecretKeySpec(symmetricKey.getBytes("UTF-8"), symmetricAlg);
    c.init(Cipher.ENCRYPT_MODE, k);

    return c.doFinal(data);
  }

  private static byte[] decodeSymetric(byte[] data) throws Exception  {
    Cipher c = Cipher.getInstance(symmetricAlg);
    SecretKeySpec k = new SecretKeySpec(symmetricKey.getBytes("UTF-8"), symmetricAlg);
    c.init(Cipher.DECRYPT_MODE, k);

    return c.doFinal(data);
  }

  public static void main(String[] args) throws Exception {
    byte[] data = "Plain text".getBytes("UTF-8");

    System.out.println("Data: " + new String(data));
    byte[] encodedData = encodeSymetric(data);
    System.out.println("Encrypted data: " + new String(encodedData));
    byte[] decodedData = decodeSymetric(encodedData);
    System.out.println("Decrypted data: " + new String(decodedData));
    System.out.println("Process ok? : " + Arrays.equals(data, decodedData));
  }

}
