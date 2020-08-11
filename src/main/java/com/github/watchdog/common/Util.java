package com.github.watchdog.common;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


@Slf4j
public class Util {


    private static final String baseUrl = "https://api.day.app/%s/%s";

    private static OkHttpClient httpClient = new OkHttpClient();


    public static void updateLogLevel(String level) {

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("root");
        rootLogger.setLevel(Level.toLevel(level));
    }


    public static void sleepMS(long ms) {

        if (ms <= 0) {
            return;
        }
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.error("Util.sleep error : ", e);
        }
    }


    public static void sleepSec(long sec) {

        if (sec <= 0) {
            return;
        }
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            log.error("Util.sleep error : ", e);
        }
    }


    public static long nowMS() {

        return System.currentTimeMillis();
    }


    public static long nowSec() {

        return System.currentTimeMillis() / 1000;
    }


    public static String timestamp2Date(long timeSeq) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        return simpleDateFormat.format(new Date(timeSeq * 1000));
    }


    public static double formatPercent(double a, double b) {

        return ((double) Math.round(a / b * 10000)) / 100;
    }


    public static byte[] compressGzip(String str) {

        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes("UTF-8"));
            gzip.close();
        } catch (IOException e) {
            log.error("compress gzip error : ", e);
        }
        return out.toByteArray();
    }


    public static String unCompressGzip(byte[] bytes) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            GZIPInputStream unGzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = unGzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString();
        } catch (Exception e) {
            log.error("un compress gzip error : ", e);
        }
        return "";
    }


    public static void writeFile(String path, byte[] content) {

        try {
            File file = new File(path);
            Files.write(content, file);
        } catch (IOException e) {
            log.error("write file error : ", e);
            Validate.isTrue(false);
        }
    }


    public static String readFile(String path) {

        File file = new File(path);
        if (file.exists()) {
            try {
                List<String> l = Files.readLines(file, Charset.defaultCharset());
                return StringUtils.join(l, "");
            } catch (IOException e) {
                log.error("read file error : ", e);
                Validate.isTrue(false);
            }
        }
        return null;
    }


    public static void sendMsg(String uid, String msg) {

        msg = URLEncoder.encode(msg);
        execUrl(String.format(baseUrl, uid, msg));
    }


    private static void execUrl(String url) {

        Request request = new Request.Builder().url(url).build();
        try {
            Response r = httpClient.newCall(request).execute();
            r.close();
        } catch (IOException e) {
            log.error("http error : ", e);
        }
    }
}
