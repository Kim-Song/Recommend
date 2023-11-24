package sejong.capstone.backjoonrecommend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.User;
import sejong.capstone.backjoonrecommend.dto.client.UserClientDto;
import sejong.capstone.backjoonrecommend.service.AiService;
import sejong.capstone.backjoonrecommend.service.SolvedAPIService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {

    private final SolvedAPIService solvedAPIService;

    public UserController(SolvedAPIService solvedAPIService, AiService aiService) {
        this.solvedAPIService = solvedAPIService;
    }

    @GetMapping("/user")
    private UserClientDto getUser(@RequestParam String id) {
        User user = solvedAPIService.getUser(id);
        UserClientDto userClientDto = new UserClientDto();
        userClientDto.setData(user);
        return userClientDto;
    }
}
