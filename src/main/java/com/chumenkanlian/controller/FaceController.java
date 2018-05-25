package com.chumenkanlian.controller;

import com.qcloud.image.ImageClient;
import com.qcloud.image.request.FaceLiveDetectFourRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by HUANGJY on 2018/5/17.
 */
@RestController
public class FaceController {

    String appId = "1253865892";
    String secretId = "AKID66vX1JnopfMsCO45hicKrCLI4ax9Kmom";
    String secretKey = "wgEazXyEHCvWvANSQC9zd5PYWN5cxBF7";
    String bucketName = "qcloudtest";

    @RequestMapping("/face")
    public String index() {
        return "Greetings from Spring Boot!...";
    }


    /**
     * 人脸视频上传验证;
     * @param multipartFile
     * @return
     */
    @RequestMapping("/face/livedetectfour")
    public String faceFileUpload(@RequestParam("file") MultipartFile multipartFile) {
        if (!multipartFile.isEmpty()) {
            File file = null;
            try {
                file = new File(multipartFile.getOriginalFilename());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);
                System.out.println(multipartFile.getName());
                out.write(multipartFile.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Logger.getLogger(FaceController.class.getName()).log(Level.SEVERE, null, e);
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.getLogger(FaceController.class.getName()).log(Level.SEVERE, null, e);
                return "上传失败," + e.getMessage();
            }


            ImageClient imageClient = initImageClient();

            String ret =faceLiveDetectFour(imageClient,bucketName,"5566",file);

            imageClient.shutdown();

            return ret;

        } else {
            return "上传失败，因为文件是空的.";
        }
    }



    /**
     * 初始化 ImageClient
     */
    private ImageClient initImageClient(){
        ImageClient imageClient = new ImageClient(appId, secretId, secretKey);
        return imageClient;
    }

    /**
     * 检测视频和图片人物是否对上操作
     */
    private static String faceLiveDetectFour(ImageClient imageClient, String bucketName, String validate, File video) {
        String ret;
        System.out.println("====================================================");
        File liveDetectImage = null;
        boolean compareFlag = true;
        try {
            liveDetectImage = new File("C:\\hjyphoto.jpg");
        } catch (Exception ex) {
            Logger.getLogger(FaceController.class.getName()).log(Level.SEVERE, null, ex);
        }

        FaceLiveDetectFourRequest faceLiveDetectReq = new FaceLiveDetectFourRequest(bucketName, validate, compareFlag, video, liveDetectImage, "seq");
        ret = imageClient.faceLiveDetectFour(faceLiveDetectReq);
        System.out.println("face  live detect four ret:" + ret);
        return ret;
    }





    /**
     * 多参数文件上传
     */
    @RequestMapping(value = "/upload/form", method = RequestMethod.POST)
    public String handleFileUpload(HttpServletRequest request) {
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        String name=params.getParameter("name");
        System.out.println("name:"+name);
        String id=params.getParameter("filePath");
        System.out.println("id:"+id);
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(
                            new File(file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                    stream = null;
                    return "You failed to upload " + i + " => "
                            + e.getMessage();
                }
            } else {
                return "You failed to upload " + i
                        + " because the file was empty.";
            }
        }
        return "upload successful";
    }



}
