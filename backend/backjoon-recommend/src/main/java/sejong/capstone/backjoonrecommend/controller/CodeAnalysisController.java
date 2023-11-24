package sejong.capstone.backjoonrecommend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.Analysis;
import sejong.capstone.backjoonrecommend.domain.AnalysisResult;
import sejong.capstone.backjoonrecommend.domain.Code;
import sejong.capstone.backjoonrecommend.dto.client.AnalysisClientDto;
import sejong.capstone.backjoonrecommend.dto.client.AnalysisCodeClientDto;
import sejong.capstone.backjoonrecommend.repository.CodeRepository;
import sejong.capstone.backjoonrecommend.service.ChatGPTService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CodeAnalysisController {
    private final ChatGPTService chatGPTService;
    private final CodeRepository codeRepository;

    public CodeAnalysisController(ChatGPTService chatGPTService, CodeRepository codeRepository) {
        this.chatGPTService = chatGPTService;
        this.codeRepository = codeRepository;
    }

    @PostMapping("/analysis")
    private AnalysisClientDto getAnalysis(@RequestBody AnalysisCodeClientDto codeDto) {
        Long number = codeDto.getNumber();
        Long time = codeDto.getTime();
        Long memory = codeDto.getMemory();
        String language = codeDto.getLanguage();
        String code = codeDto.getCode();

        Code inputCode = new Code();
        inputCode.setNumber(number);
        inputCode.setTime(time);
        inputCode.setMemory(memory);
        inputCode.setLanguage(language);
        inputCode.setCode(code);

        codeRepository.save(inputCode);

        String bigO = chatGPTService.getAnalysis(code, Analysis.BIG_O, null);
        System.out.println("1");
        String spaceComplex = chatGPTService.getAnalysis(code, Analysis.SPACE_COMPLEX, null);
        System.out.println("2");
        String whatAlgo = chatGPTService.getAnalysis(code, Analysis.WHAT_ALGO, null);
        System.out.println("3");
        String compareBigO = chatGPTService.getAnalysis(code, Analysis.COMPARE_BIG_O, null);
        System.out.println("4");
        String compareSpaceComplex = chatGPTService.getAnalysis(code, Analysis.COMPARE_SPACE_COMPLEX, null);
        System.out.println("5");

        AnalysisClientDto analysisClientDto = new AnalysisClientDto();
        AnalysisResult analysisResult = new AnalysisResult();

        analysisResult.setBigO(bigO);
        analysisResult.setSpaceComplexDegree(spaceComplex);
        analysisResult.setWhatAlgo(whatAlgo);
        analysisResult.setCompareOtherBigOCode("");
        analysisResult.setCompareOtherBigO(compareBigO);
        analysisResult.setCompareOtherSpaceComplexDegreeCode("");
        analysisResult.setCompareOtherSpaceComplexDegree(compareSpaceComplex);

        /*
        이건 돈아끼려고 하는거
        analysisResult.setBigO("O(N^3)");
        analysisResult.setSpaceComplexDegree("O(N^2)");
        analysisResult.setWhatAlgo("그리디");
        analysisResult.setCompareOtherBigOCode("123");
        analysisResult.setCompareOtherBigO("이 사람의 코드를 살펴보시면 ~~~하기 떄문에 이런점에서 ~~합니다.");
        analysisResult.setCompareOtherSpaceComplexDegreeCode("123");
        analysisResult.setCompareOtherSpaceComplexDegree("이 사람의 코드를 살펴보시면 ~~~하기 떄문에 이런점에서 ~~합니다.");
        */

        analysisClientDto.setData(analysisResult);
        return analysisClientDto;
    }

}
