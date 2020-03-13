package self.liuhy.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import self.liuhy.common.dto.AccessTokenDTO;
import self.liuhy.common.dto.GithubUser;
import self.liuhy.common.mapper.UserMapper;
import self.liuhy.common.model.User;
import self.liuhy.common.provider.GithubProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Description: 登录授权
 * @Author: liuhy
 * @Date 2020/2/27 2:38
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String secret;
    @Value("${github.client.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserMapper userMapper;


    @GetMapping("/authCallback")
    public String authCallback(@RequestParam(name = "code") String code,
                               @RequestParam(name = "state") String state,
                               HttpServletRequest request) {
        AccessTokenDTO at = new AccessTokenDTO();
        at.setCode(code);
        at.setState(state);
        at.setClient_id(clientId);
        at.setRedirect_uri(redirectUri);
        at.setClient_secret(secret);
        String token = githubProvider.getAccessToken(at);
        GithubUser githubUser = githubProvider.getGithubUser(token);
        if (githubUser != null) {
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            // 登录成功，写cookie和session
            request.getSession().setAttribute("user", user);
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }

}
