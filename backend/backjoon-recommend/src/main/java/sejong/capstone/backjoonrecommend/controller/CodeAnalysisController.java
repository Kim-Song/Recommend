package sejong.capstone.backjoonrecommend.controller;

import java.util.concurrent.CompletableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.Analysis;
import sejong.capstone.backjoonrecommend.domain.AnalysisResult;
import sejong.capstone.backjoonrecommend.domain.AnalysisResult2;
import sejong.capstone.backjoonrecommend.domain.Code;
import sejong.capstone.backjoonrecommend.dto.ai.AiProblemDTO;
import sejong.capstone.backjoonrecommend.dto.client.AnalysisClientDto;
import sejong.capstone.backjoonrecommend.dto.client.AnalysisClientProblemNumberDto;
import sejong.capstone.backjoonrecommend.dto.client.AnalysisCodeClientDto;
import sejong.capstone.backjoonrecommend.dto.client.AnalysisWrongCodeClientDto;
import sejong.capstone.backjoonrecommend.repository.CodeRepository;
import sejong.capstone.backjoonrecommend.service.AiService;
import sejong.capstone.backjoonrecommend.service.ChatGPTService;

@RestController
public class CodeAnalysisController {
    private final ChatGPTService chatGPTService;
    private final CodeRepository codeRepository;
    private final AiService aiService;

    public CodeAnalysisController(ChatGPTService chatGPTService, CodeRepository codeRepository, AiService aiService) {
        this.chatGPTService = chatGPTService;
        this.codeRepository = codeRepository;
        this.aiService = aiService;
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
        Code bestCode1 = codeRepository.getBestCodeBySpaceComplexity(number, language);
        Code bestCode2 = codeRepository.getBestCodeBySpaceComplexity(number, language);

        CompletableFuture<String> asyncBigO = chatGPTService.getAnalysis(code, Analysis.BIG_O, null);
        CompletableFuture<String> asyncSpaceComplex = chatGPTService.getAnalysis(code, Analysis.SPACE_COMPLEX, null);
        CompletableFuture<String> asyncWhatAlgo = chatGPTService.getAnalysis(code, Analysis.WHAT_ALGO, null);
        CompletableFuture<String> asyncCompareBigO = chatGPTService.getAnalysis(code, Analysis.COMPARE_BIG_O, bestCode1);
        CompletableFuture<String> asyncCompareSpaceComplex = chatGPTService.getAnalysis(code, Analysis.COMPARE_SPACE_COMPLEX, bestCode2);

        String bigO = "";
        String spaceComplex = "";
        String whatAlgo = "";
        String compareBigO = "";
        String compareSpaceComplex = "";

        try {
            bigO = asyncBigO.get();
            spaceComplex = asyncSpaceComplex.get();
            whatAlgo = asyncWhatAlgo.get();
            compareBigO = asyncCompareBigO.get();
            compareSpaceComplex = asyncCompareSpaceComplex.get();
        } catch (Exception e) {
            bigO = "내일";
            spaceComplex = "이용이";
            whatAlgo = "가능합니다";
            compareBigO = "내일 이용이 가능합니다.";
            compareSpaceComplex = "내일 이용이 가능합니다";
        }

        AnalysisClientDto analysisClientDto = new AnalysisClientDto();
        AnalysisResult analysisResult = new AnalysisResult();

        analysisResult.setBigO(bigO);
        analysisResult.setSpaceComplexDegree(spaceComplex);
        analysisResult.setWhatAlgo(whatAlgo);

        analysisResult.setCompareOtherBigOCode(bestCode1.getCode());
        analysisResult.setCompareOtherBigO(compareBigO);
        analysisResult.setCompareOtherSpaceComplexDegreeCode(bestCode2.getCode());
        analysisResult.setCompareOtherSpaceComplexDegree(compareSpaceComplex);

        analysisClientDto.setData(analysisResult);
        return analysisClientDto;
    }

    @PostMapping("/analysis2")
    private AnalysisWrongCodeClientDto getAnalysis2(@RequestBody AnalysisClientProblemNumberDto clientDto) {
        Long number = clientDto.getNumber();
        String userCode = clientDto.getCode();

        AnalysisWrongCodeClientDto analysisWrongCodeClientDto = new AnalysisWrongCodeClientDto();
        // api를 호출해서 문제를 가져온다.
        AiProblemDTO analysisByWrong = aiService.getProblemInfo(number);
        String contents = analysisByWrong.getProblem_context();
        contents += " 입력 : " + analysisByWrong.getProblem_input();
        contents += " 출력 : " + analysisByWrong.getProblem_output();
        // api를 호출해서 문제를 가져온다.

        String analysisWrong = chatGPTService.getAnalysisWrong(contents, userCode);
        AnalysisResult2 analysisResult2 = new AnalysisResult2();
        analysisResult2.setContents(analysisWrong);
        analysisWrongCodeClientDto.setData(analysisResult2);
        return analysisWrongCodeClientDto;
    }
}
