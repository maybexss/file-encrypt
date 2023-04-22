package com.sweeky.xss.main;

import com.sweeky.xss.encrypt.AesUtil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        encAndDec();
    }

    private static void encAndDec() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input Option(ENC for Encrypt, DEC for Decrypt):");

        String optionType = scanner.nextLine();
        if ("ENC".equalsIgnoreCase(optionType)) {
            System.out.print("Input the source files' folder path:");
            String filePath = scanner.nextLine();
            while (filePath == null || filePath.length() <= 0) {
                System.out.print("Invalid filePath, input the source files' folder path:");
                filePath = scanner.nextLine();
            }

            System.out.print("Input the files' store folder path after them encrypted:");
            String encryptFilePath = scanner.nextLine();
            while (encryptFilePath == null || encryptFilePath.length() <= 0) {
                System.out.print("Invalid encryptFilePath, input the files' store folder path after them encrypted:");
                encryptFilePath = scanner.nextLine();
            }

            System.out.print("Create your encrypt password(default is |'Hello World!]):");
            String sKey = scanner.nextLine();

            AesUtil.encrypt(filePath, sKey, encryptFilePath);
        } else if ("DEC".equalsIgnoreCase(optionType)) {
            System.out.print("Input your encrypted files' folder path:");
            String encryptFilePath = scanner.nextLine();
            while (encryptFilePath == null || encryptFilePath.length() <= 0) {
                System.out.print("Invalid encryptFilePath, input your encrypted files' folder path:");
                encryptFilePath = scanner.nextLine();
            }

            System.out.print("Input the files' store folder path after them decrypted:");
            String decryptFilePath = scanner.nextLine();
            while (decryptFilePath == null || decryptFilePath.length() <= 0) {
                System.out.print("Invalid decryptFilePath, input the files' store folder path after them decrypted:");
                decryptFilePath = scanner.nextLine();
            }

            System.out.print("Input your created password(default is |'Hello World!]):");
            String sKey = scanner.nextLine();

            AesUtil.decrypt(encryptFilePath, sKey, decryptFilePath);
        } else {
            System.out.println("Input error：ENC for Encrypt, DEC for Decrypt，Other input will be ignored");
        }
    }
}
