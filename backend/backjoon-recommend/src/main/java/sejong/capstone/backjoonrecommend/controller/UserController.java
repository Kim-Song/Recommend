package sejong.capstone.backjoonrecommend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.User;
import sejong.capstone.backjoonrecommend.dto.client.UserClientDto;
import sejong.capstone.backjoonrecommend.service.SolvedAPI;

@RestController
public class UserController {

    private final SolvedAPI solvedAPI;

    public UserController(SolvedAPI solvedAPI) {
        this.solvedAPI = solvedAPI;
    }

    @GetMapping("/user")
    private UserClientDto getUser(@RequestParam String id) {
        User user = solvedAPI.getUser(id);
        UserClientDto userClientDto = new UserClientDto();
        userClientDto.setData(user);
        return userClientDto;
    }

}
