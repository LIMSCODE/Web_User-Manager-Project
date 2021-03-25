package com.onlinepowers.springmybatis.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SHA256Util {


	public static String getEncrypt(String source, long id) {

		String result = "";

		byte[] arrayId = new byte[5];
		arrayId[0] = (byte) id;

		byte[] a = source.getBytes();
		byte[] bytes = new byte[a.length + arrayId.length];

		System.arraycopy(a, 0, bytes, 0, a.length);
		System.arraycopy(arrayId, 0, bytes, a.length, arrayId.length);

		try {
			// 암호화 방식 지정 메소드
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(bytes);

			byte[] byteData = md.digest();

			//sb에 byteData 하나씩 돌아가며 추가한다.
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
			}
			result = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * SALT를 얻어온다.
	 * @return
	 */
	public static String generateSalt() {

		Random random = new Random();

		byte[] id = new byte[8];
		random.nextBytes(id);

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < id.length; i++) {
			// byte 값을 Hex 값으로 바꾸기.
			sb.append(String.format("%02x",id[i]));
		}

		return sb.toString();
	}

}
