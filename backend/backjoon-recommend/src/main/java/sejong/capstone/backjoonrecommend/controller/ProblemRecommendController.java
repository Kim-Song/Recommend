package sejong.capstone.backjoonrecommend.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.Problem;
import sejong.capstone.backjoonrecommend.dto.ai.AiDTO;
import sejong.capstone.backjoonrecommend.dto.client.ProblemClientDto;
import sejong.capstone.backjoonrecommend.repository.ProblemRecommendRepository;
import sejong.capstone.backjoonrecommend.service.AiService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ProblemRecommendController {

    private final ProblemRecommendRepository problemRecommendRepository;
    private final AiService aiService;

    public ProblemRecommendController(ProblemRecommendRepository problemRecommendRepository, AiService aiService) {
        this.problemRecommendRepository = problemRecommendRepository;
        this.aiService = aiService;
    }

    @GetMapping("/problem")
    private ProblemClientDto getProblems(@RequestParam String id) {
        // id를 이용해서 problemNumbers를 가져온다. 즉 추천 문제들을 가져온다.
        AiDTO analysis = aiService.getAnalysis(id);
        List<Long> recommendList = analysis.getRecommend_list();

        List<Problem> list = new ArrayList<>();
        for (Long number : recommendList) {
            Problem info = problemRecommendRepository.findByProblemNumber(number);
            list.add(info);
        }
        
        ProblemClientDto problemClientDto = new ProblemClientDto();
        problemClientDto.setData(list);
        return problemClientDto;
    }
}
