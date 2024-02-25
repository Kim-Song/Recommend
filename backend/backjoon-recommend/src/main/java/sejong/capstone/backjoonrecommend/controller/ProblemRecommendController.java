package sejong.capstone.backjoonrecommend.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.entity.Problem;
import sejong.capstone.backjoonrecommend.dto.ai.receipt.AiDTO;
import sejong.capstone.backjoonrecommend.dto.client.send.ProblemDto;
import sejong.capstone.backjoonrecommend.repository.ProblemRecommendRepository;
import sejong.capstone.backjoonrecommend.service.AiService;

@RestController
@RequiredArgsConstructor
public class ProblemRecommendController {

    private final ProblemRecommendRepository problemRecommendRepository;
    private final AiService aiService;

    @GetMapping("/problem")
    private ProblemDto getProblems(@RequestParam String id) {
        // id를 이용해서 problemNumbers를 가져온다. 즉 추천 문제들을 가져온다.
        AiDTO analysis = aiService.getProblemList(id);
        List<Long> recommendList = analysis.getRecommend_list();

        List<Problem> list = new ArrayList<>();
        for (Long number : recommendList) {
            Problem info = problemRecommendRepository.findByProblemNumber(number);
            list.add(info);
        }
        
        ProblemDto problemDto = new ProblemDto();
        problemDto.setData(list);
        return problemDto;
    }
}
