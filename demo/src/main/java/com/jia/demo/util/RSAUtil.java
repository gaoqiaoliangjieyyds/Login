package com.jia.demo.util;

import cn.hutool.core.codec.Base64;
import io.swagger.annotations.ApiModel;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@ApiModel("对密码进行加密")
public class RSAUtil {
 
	
	private static String publicKey ="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJWyHz9WsaIdnj1iXB/3Kos6DB53ChWcnF5I8CzrfObyRl7/surSWumNTDrw5AH2GvJyPbIatt0CZPkdoRu5mMkCAwEAAQ==";
	private static String privateKey ="MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAlbIfP1axoh2ePWJcH/cqizoMHncKFZycXkjwLOt85vJGXv+y6tJa6Y1MOvDkAfYa8nI9shq23QJk+R2hG7mYyQIDAQABAkBk4Hf2MvHZkaQFMbdrQyDEWgW4sfd1wOgPEY/odXjiF+44DBPYdTFiZflW66d93B+y4ka1cTeJFwbofMBN6ExBAiEA6THUpdxAv3J3Pfqf7C2SxzN0rF1AIAI3BdfFY04/+30CIQCkVdjJX3ujoQ6dSElfRzV0oUcDP1ZgE0LrkpC96i0cPQIhAN+DQ682TVky/dgKQP6/L9B/IXWUa8JCXTrcdfWZKvqBAiBTzxA7Sr3Ualv4rJf2xmslquZ7an7Tip8zYtD8egQ9hQIhAKju3oHDwkBP0Sdu+ywEysWhJomWU23lhaiArfNOSaph";
	 
	 

	 
	/**
	 * 	@Description : 公钥加密
	 *  @param src
	 *  @return
	 */
	public static byte[] publicEncode(String src) {
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(publicKey));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] result = cipher.doFinal(src.getBytes());
			return result;
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	 
	/**
	 * 	@Description : 私钥解密
	 *  @param result
	 *  @return
	 */
	public static String privateDecode(byte[] result) {
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Utils.decode(privateKey.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			result = cipher.doFinal(result);
			return new String(result);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	 
	public static void main(String[] args) {

		// 明文
		String str = "123"+System.currentTimeMillis();

		// 加密
		byte[] publicEncode = publicEncode(str);
		String encodeBase64 = Base64.encode(publicEncode);

		//System.out.println(encodeBase64);

		// 解密
		String privateDecode = privateDecode(Base64.decode(encodeBase64));
		//System.out.println(privateDecode);


	}
}
