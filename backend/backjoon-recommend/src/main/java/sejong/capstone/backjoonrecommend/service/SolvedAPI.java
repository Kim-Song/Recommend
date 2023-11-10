package sejong.capstone.backjoonrecommend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sejong.capstone.backjoonrecommend.domain.User;

@Service
@Slf4j
public class SolvedAPI {
    static final String BASE_URL = "https://solved.ac/api/v3";

    private RestTemplate restTemplate = new RestTemplate();
    public User getUser(String id){
        String URL = BASE_URL + "/user/show?handle=" + id;

        ResponseEntity<User> forEntity = restTemplate.getForEntity(URL, User.class);
        User user = forEntity.getBody();

        return user;
    }
}