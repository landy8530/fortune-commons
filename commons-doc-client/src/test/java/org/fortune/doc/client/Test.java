package org.fortune.doc.client;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/5/30 23:37
 * @description:
 */
public class Test {

    public static void main(String[] args) throws Exception {
//		FileClient.uploadFile(new File("D:\\tmp\\Test.class"), "a.class",false);
//		FileClient.uploadFile(new File("D:\\tmp\\FUp_378131942802165004.pdf"), "FUp_378131942802165004.pdf",false);
        DocClient.replaceFile(new File("D:\\tmp\\FUp_378131942802165004.pdf"), "yt\\k\\171105144056_7470.pdf");
        DocClient.deleteFile("yt\\k\\171105144056_7470.pdf");
    }

}
