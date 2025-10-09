package com.helheim.mel.util;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;

public class DailyCode {
    private static final String SECRET = "super_secret_key";

    public static String getTokenToday(){
        String today = LocalDate.now(ZoneId.of("Asia/Tokyo")).toString();

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));

            byte[] hash = mac.doFinal(today.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder()
                         .withoutPadding()
                         .encodeToString(hash)
                         .substring(0,8)
                         .toUpperCase();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
