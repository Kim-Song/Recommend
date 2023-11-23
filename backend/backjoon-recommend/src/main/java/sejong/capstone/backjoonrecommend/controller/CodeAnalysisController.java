package sejong.capstone.backjoonrecommend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.BigO;
import sejong.capstone.backjoonrecommend.dto.client.AnalysisCodeClientDto;
import sejong.capstone.backjoonrecommend.dto.client.BigOClientDto;
import sejong.capstone.backjoonrecommend.service.ChatGPTService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CodeAnalysisController {
    private final ChatGPTService chatGPTService;

    public CodeAnalysisController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    @PostMapping("/big-o")
    private BigOClientDto getBigO(@RequestBody AnalysisCodeClientDto codeDto) {
        String code = codeDto.getCode();
        BigO bigO = chatGPTService.getBigO(code);
        BigOClientDto bigOClientDto = new BigOClientDto();
        bigOClientDto.setData(bigO);
        return bigOClientDto;
    }
}
