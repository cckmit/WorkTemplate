package org.blaze.file.system.controller;

import com.alibaba.fastjson.JSONObject;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.blaze.file.system.utils.BaseConfig;
import org.blaze.file.system.utils.ZipStreamEntity;
import org.blaze.file.system.utils.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xuwei
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ToolsController")
public class ToolsController {

    @Autowired
    ZipUtils zipUtils;
    @Autowired
    BaseConfig config;

    @ResponseBody
    @PostMapping("/zipUpload")
    public JSONObject uploadZip(@RequestParam("file") MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        if (file.isEmpty()) {
            jsonObject.put("result", "fail");
            jsonObject.put("msg", "未上传文件!");
            return jsonObject;
        }
        try {
            String originalFilename = file.getOriginalFilename();
            File readFile = new File(this.config.getUploadFilePath(), Objects.requireNonNull(originalFilename));

            FileUtils.copyInputStreamToFile(file.getInputStream(), readFile);

            //自动进行解压
            ZipFile zipFile = new ZipFile(readFile);
            zipFile.setCharset(Charset.defaultCharset());
            if (!zipFile.isValidZipFile()) {
                jsonObject.put("result", "fail");
                jsonObject.put("msg", "压缩文件不合法，可能已经损坏!");
            }
            File zip = new File(this.config.getUploadFilePath());
            if (zip.isDirectory() && !zip.exists()) {
                zip.mkdirs();
            }
            zipFile.extractAll(this.config.getUploadFilePath());
            readFile.delete();

            // 从上传的文件中读取信息
            String share = FileUtils.readFileToString(new File(
                    this.config.getUploadFilePath() + originalFilename.substring(0, originalFilename.lastIndexOf(
                            ".")) + File.separator + "share" + File.separator + "readme.json"), "utf-8");
            String skip = FileUtils.readFileToString(new File(
                    this.config.getUploadFilePath() + originalFilename.substring(0, originalFilename.lastIndexOf(
                            ".")) + File.separator + "skip" + File.separator + "readme.json"), "utf-8");

            jsonObject.put("share", share);
            jsonObject.put("skip", skip);
            jsonObject.put("result", "success");
            jsonObject.put("msg", "压缩包上传成功!");

        } catch (IOException e) {
            jsonObject.put("result", "fail");
            jsonObject.put("msg", "文件解析异常!");
        }
        return jsonObject;
    }

    @GetMapping("/ZipTools")
    public JSONObject zipTools(HttpServletResponse response, HttpServletRequest request) throws FileNotFoundException {
        //获取文件夹
        JSONObject srcData = config.getSrcPath();

        JSONObject result = new JSONObject();
        //获取来源路径
        String source = request.getParameter("source");
        if (StringUtils.isBlank(source)) {
            result.put("msg", "source参数为空");
            result.put("status", false);
            return result;
        }

        String sourcePath = srcData.getString(source);
        if (StringUtils.isBlank(sourcePath)) {
            result.put("msg", "source对应的文件夹未配置");
            result.put("status", false);
            return result;
        }

        File dir = new File(sourcePath);
        if (!dir.exists() || !dir.isDirectory()) {
            result.put("msg", "source对应的文件夹未不存在");
            result.put("status", false);
            return result;
        }

        List<ZipStreamEntity> list = new ArrayList<>();
        for (File fi : Objects.requireNonNull(dir.listFiles())) {
            if (compareFile(fi, request)) {
                InputStream fileInputStream = new FileInputStream(fi);
                list.add(new ZipStreamEntity(fi.getName(), fileInputStream));
            }
        }

        if (list.isEmpty()) {
            try {
                response.sendError(202, "文件不存在");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        String fileName = request.getParameter("fileName") + ".zip";
        ZipUtils.listStreamToZipStream(list, fileName, response);
        return null;
    }

    /**
     * 文件匹配
     *
     * @param file   需要验证文件
     * @param search 查询条件
     * @return 是否匹配
     */
    private boolean compareFile(File file, HttpServletRequest search) {
        String type = search.getParameter("type");
        String start = search.getParameter("start"), end = search.getParameter("end");
        String name = file.getName();
        try {
            switch (type) {
                case "day": {
                    DateFormat format = new SimpleDateFormat("yyyyMMdd");
                    Calendar now = Calendar.getInstance();
                    now.setTime(format.parse(start));
                    Date endTime = format.parse(end);
                    while (now.getTime().compareTo(endTime) <= 0) {
                        if (name.contains(format.format(now.getTime()))) {
                            return true;
                        }
                        now.add(Calendar.DATE, 1);
                    }
                }
                break;
                case "hour": {
                    DateFormat format = new SimpleDateFormat("yyyyMMddHH");
                    Calendar now = Calendar.getInstance();
                    now.setTime(format.parse(start));
                    Date endTime = format.parse(end);
                    while (now.getTime().compareTo(endTime) <= 0) {
                        if (name.contains(format.format(now.getTime()))) {
                            return true;
                        }
                        now.add(Calendar.HOUR_OF_DAY, 1);
                    }
                }
                break;
                case "month": {
                    DateFormat format = new SimpleDateFormat("yyyyMM");
                    Calendar now = Calendar.getInstance();
                    now.setTime(format.parse(start));
                    Date endTime = format.parse(end);
                    while (now.getTime().compareTo(endTime) <= 0) {
                        if (name.contains(format.format(now.getTime()))) {
                            return true;
                        }
                        now.add(Calendar.MONTH, 1);
                    }
                }
                break;
                case "year": {
                    DateFormat format = new SimpleDateFormat("yyyy");
                    Calendar now = Calendar.getInstance();
                    now.setTime(format.parse(start));
                    Date endTime = format.parse(end);
                    while (now.getTime().compareTo(endTime) <= 0) {
                        if (name.contains(format.format(now.getTime()))) {
                            return true;
                        }
                        now.add(Calendar.YEAR, 1);
                    }
                }
                break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
