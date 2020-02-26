package self.liuhy.common.provider;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;
import self.liuhy.common.dto.AccessTokenDTO;
import self.liuhy.common.dto.GithubUser;

import java.io.IOException;
import java.util.Objects;

/**
 * @Description: TODO
 * @Author: liuhy
 * @Date 2020/2/27 2:48
 */
/**
 * Controller是将类作为一个路由API的一个承载者
 * Component仅仅只是把当前类初始化到Spring的上下文
 */
@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO at) {
        final MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON.toJSONString(at), mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();
            String tokenstr = res.split("&")[0];
            String token = tokenstr == null ? null: tokenstr.split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getGithubUser (String token) {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + token)
                .build();
        try (Response response = client.newCall(req).execute()) {
            String res = Objects.requireNonNull(response.body()).string();
            return JSON.parseObject(res, GithubUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
