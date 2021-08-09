/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

import com.huawei.agconnect.server.commons.exception.AGCException;

import java.util.Scanner;

/**
 * demo
 *
 * @since 2021-07-03
 */
public class Demo {
    public static void main(String[] args) throws AGCException {
        userInput();
        System.exit(1);
    }

    public static void userInput() {
        int num = 0;
        do {
            Scanner scan = new Scanner(System.in);

            System.out.println("1.initializeClient (init first)");
            System.out.println("2.uploadFile");
            System.out.println("3.listObjects");
            System.out.println("4.downloadFile");
            System.out.println("5.getMetadata");
            System.out.println("6.updateMetadata");
            System.out.println("7.deleteFile");
            System.out.println("Choose an operation (enter 0 to exit):");
            num = scan.nextInt();

            try {
                switch (num) {
                    case 1:
                        ServerApi.initializeClient();
                        break;
                    case 2:
                        ServerApi.uploadFile();
                        break;
                    case 3:
                        ServerApi.listObjects();
                        break;
                    case 4:
                        ServerApi.downloadFile();
                        break;
                    case 5:
                        ServerApi.getMetadata();
                        break;
                    case 6:
                        ServerApi.updateMetadata();
                        break;
                    case 7:
                        ServerApi.deleteFile();
                        break;
                    case 0:
                        System.out.println("Exit demo");
                        break;
                    default:
                        System.out.println("Please enter 1-7.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (num != 0);
    }
}
