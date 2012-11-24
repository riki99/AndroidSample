package app.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class EncryptUtil {

    private static final String ALGORITHM = "DESede";
    private static final String KEY_DIGEST_ALGORITHM = "SHA-256";


	// ----------------------------------------------------------------------
	// ## : EncryptUtil 暗号化キーの生成
	// ----------------------------------------------------------------------
	public static byte[] getKeyByte(String secretKey) {
		try {
	        // 鍵の種をダイジェスト化して鍵として利用します。
	        // ポイント: SHA-256は32バイトあるので、DESedeで必要となる
	    	// 24バイトより長いので都合が良い。
			MessageDigest msgDigest = MessageDigest.getInstance(KEY_DIGEST_ALGORITHM);
            msgDigest.update(secretKey.getBytes());
            byte[] digestedKey = msgDigest.digest();
            // 実際に利用するキーを作成します。
            final byte[] keySeed = new byte[24];
            for (int index = 0; index < keySeed.length; index++) {
                keySeed[index] = digestedKey[index];
            }
    		return keySeed;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("ダイジェスト[" + KEY_DIGEST_ALGORITHM
                    + "]取得に失敗しました。:" + e.toString());
        }
	}

	// ----------------------------------------------------------------------
	// ## : EncryptUtil 文字列の暗号化
	// ----------------------------------------------------------------------
	public static String encryptStr(String value, String secretKey) {
		return encryptStr(value.getBytes(), getKeyByte(secretKey));
	}
	public static String encryptStr(String value, byte[] secretKey) {
		return encryptStr(value.getBytes(), secretKey);
	}
	public static String encryptStr(byte[] value, byte[] secretKey) {
		return StringUtil.byteToStringHex(encrypt(value, secretKey));
	}
	public static byte[] encrypt(byte[] value, byte[] secretKey) {
		try {
			SecretKeyFactory keyFac = SecretKeyFactory.getInstance(ALGORITHM);
			DESedeKeySpec keySpec = new DESedeKeySpec(secretKey);
			SecretKey secKey = keyFac.generateSecret(keySpec);
			Cipher encoder = Cipher.getInstance(ALGORITHM);
			encoder.init(Cipher.ENCRYPT_MODE, secKey);
			byte[] b = encoder.doFinal(value);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ----------------------------------------------------------------------
	// ## : EncryptUtil 文字列の復号化
	// ----------------------------------------------------------------------
	public static String decryptStr(String value, String secretKey) {
		byte[] b = StringUtil.stringToByteHex(value);
		return decryptStr(b, getKeyByte(secretKey));
	}
	public static String decryptStr(String value, byte[] secretKey) {
		byte[] b = StringUtil.stringToByteHex(value);
		return decryptStr(b, secretKey);
	}
	public static String decryptStr(byte[] value, byte[] secretKey) {
		return new String(decrypt(value, secretKey));
	}
	public static byte[] decrypt(byte[] value, byte[] secretKey) {
		try {
			SecretKeyFactory keyFac = SecretKeyFactory.getInstance(ALGORITHM);
			DESedeKeySpec keySpec = new DESedeKeySpec(secretKey);
			SecretKey secKey = keyFac.generateSecret(keySpec);
			Cipher decoder = Cipher.getInstance(ALGORITHM);
			decoder.init(Cipher.DECRYPT_MODE, secKey);
			return decoder.doFinal(value);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// -----------------------------------------------------------------------------------

	// ----------------------------------------------------------------------
	// ## : EncryptUtil ファイル暗号、複号
	// ----------------------------------------------------------------------
	public static void encrypt(File inFile, File outFile, String secretKey) throws IOException {
		encrypt(inFile, outFile, getKeyByte(secretKey));
	}
	public static void encrypt(File inFile, File outFile, byte[] secretKey) throws IOException {
		try {
			InputStream in = new BufferedInputStream(
					new FileInputStream(inFile));
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(secretKey));

			OutputStream out = new CipherOutputStream(
					new BufferedOutputStream(new FileOutputStream(outFile)),
					cipher);
			copyANdClose(in, out);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("アルゴリズムの取得に失敗しました。:"
					+ e.toString());
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("パディング不正で失敗しました。:"
					+ e.toString());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("秘密鍵不正で失敗しました。:" + e.toString());
		}
	}

	// ----------------------------------------------------------------------
	// ## : EncryptUtil ファイル複号
	// ----------------------------------------------------------------------
	public static void decrypt(File inFile, File outFile, String secretKey) throws IOException {
		decrypt(inFile, outFile, getKeyByte(secretKey));
	}
	public static void decrypt(File inFile, File outFile, byte[] secretKey) throws IOException {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey(secretKey));
			InputStream in = new CipherInputStream(
					new BufferedInputStream(new FileInputStream(inFile)),
					cipher);
			OutputStream out = new BufferedOutputStream(
					new FileOutputStream(outFile));

			copyANdClose(in, out);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("アルゴリズムの取得に失敗しました。:"
					+ e.toString());
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("パディング不正で失敗しました。:"
					+ e.toString());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("秘密鍵不正で失敗しました。:" + e.toString());
		}
	}


	public static String generateKey(){
		try {
			KeyGenerator kg = KeyGenerator.getInstance("Blowfish");
			kg.init(128);
			SecretKey commonKey = kg.generateKey();
			byte[] buff = commonKey.getEncoded();
			return StringUtil.byteToStringHex(buff);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// ----------------------------------------------------------------------
	// ## : EncryptUtil private
	// ----------------------------------------------------------------------
	private static void copyANdClose(InputStream in, OutputStream out) throws IOException {
		try {
			byte[] byteBuf = new byte[100 * 1024];
			for (;;) {
				int length = in.read(byteBuf);
				if (length <= 0) {
					break;
				}
				out.write(byteBuf, 0, length);
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

    private static SecretKey getSecretKey(final byte[] keySeed) {
        try {
            final SecretKeyFactory factory = SecretKeyFactory
                    .getInstance(ALGORITHM);
            return factory.generateSecret(new DESedeKeySpec(keySeed));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("アルゴリズムの取得に失敗しました。:"
                    + e.toString());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("秘密鍵が不正です。:" + e.toString());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("秘密鍵の仕様が不正です。:" + e.toString());
        }
    }

    public static void main(String[] args) {
		System.out.println(generateKey());
	}


}
