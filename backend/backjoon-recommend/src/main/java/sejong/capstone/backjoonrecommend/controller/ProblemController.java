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
import sejong.capstone.backjoonrecommend.repository.ProblemRepository;
import sejong.capstone.backjoonrecommend.service.AiService;
import sejong.capstone.backjoonrecommend.service.AiServiceImpl;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemRepository problemRepository;
    private final AiService AiServiceImpl;

    @GetMapping("/problem")
    private ProblemDto getRecommendProblems(@RequestParam String id) {
        // id를 이용해서 problemNumbers를 가져온다. 즉 추천 문제들을 가져온다.
        AiDTO analysis = AiServiceImpl.getProblems(id);
        List<Long> recommendList = analysis.getRecommend_list();

        List<Problem> list = new ArrayList<>();
        for (Long number : recommendList) {
            Problem info = problemRepository.findByProblemNumber(number);
            list.add(info);
        }
        
        ProblemDto problemDto = new ProblemDto();
        problemDto.setData(list);
        return problemDto;
    }
}
