package com.sweeky.xss.encrypt;


import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AesUtil {
    private static final String AES = "AES";
    private static final String ENC_TYPE = "enc";
    private static final int KEY_LENGTH = 128;
    private static final String SECRET_KEY = "|'Hello World!]";

    public static void encrypt(String sourceFilePath, String sKey, String encFileDir) {
        if (sKey == null || sKey.length() == 0 || sKey.trim().length() == 0) {
            sKey = SECRET_KEY;
        }

        File file = new File(sourceFilePath);
        File[] sourceFileList = file.listFiles();

        if (sourceFileList != null) {
            for (File f : sourceFileList) {
                if (f.isFile()) {
                    encryptFile(f, sKey, encFileDir);
                }
            }
        }
    }

    public static void decrypt(String encFilePath, String sKey, String decFileDir) {
        if (sKey == null || sKey.length() == 0 || sKey.trim().length() == 0) {
            sKey = SECRET_KEY;
        }

        File file = new File(encFilePath);
        File[] encFileList = file.listFiles();

        if (encFileList != null) {
            for (File f : encFileList) {
                decryptFile(f, sKey, decFileDir);
            }
        }
    }

    private static void encryptFile(File srcFile, String sKey, String encFileDir) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            String[] fileNameArr = srcFile.getName().split("\\.");

            String fileName = encFileDir + File.separator + System.currentTimeMillis() + "." + fileNameArr[fileNameArr.length - 1] + "." + ENC_TYPE;

            File encryptFile = new File(fileName);
            if (!encryptFile.createNewFile()) {
                return;
            }

            inputStream = new FileInputStream(srcFile);
            outputStream = new FileOutputStream(encryptFile);

            Cipher cipher = initAesCipher(sKey, Cipher.ENCRYPT_MODE);

            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
            byte[] cache = new byte[1024];
            int nRead;
            while ((nRead = cipherInputStream.read(cache)) != -1) {
                outputStream.write(cache, 0, nRead);
                outputStream.flush();
            }
            cipherInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(inputStream, outputStream);
        }
    }

    private static void decryptFile(File encryptFile, String sKey, String decFileDir) {
        File decryptFile;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            String[] fileNameArr = encryptFile.getName().split("\\.");
            String fileName = decFileDir + File.separator + fileNameArr[fileNameArr.length - 3] + "." + fileNameArr[fileNameArr.length - 2];

            decryptFile = new File(fileName);
            if (!decryptFile.createNewFile()) {
                return;
            }

            Cipher cipher = initAesCipher(sKey, Cipher.DECRYPT_MODE);

            inputStream = new FileInputStream(encryptFile);
            outputStream = new FileOutputStream(decryptFile);

            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
            byte[] buffer = new byte[1024];
            int nRead;

            while ((nRead = inputStream.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, nRead);
                cipherOutputStream.flush();
            }
            cipherOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(inputStream, outputStream);
        }
    }

    private static Cipher initAesCipher(String sKey, int cipherMode) {
        Cipher cipher = null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(KEY_LENGTH, new SecureRandom(sKey.getBytes()));

            SecretKey secretKey = keyGenerator.generateKey();
            byte[] codeFormat = secretKey.getEncoded();

            SecretKeySpec key = new SecretKeySpec(codeFormat, AES);

            cipher = Cipher.getInstance(AES);

            cipher.init(cipherMode, key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return cipher;
    }

    private static void close(InputStream inputStream, OutputStream outputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
