package sejong.capstone.backjoonrecommend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "추천된 문제가 없습니다. 분류를 넓혀주세요.")
public class IsNotRecommendProblemException extends RuntimeException {
    public IsNotRecommendProblemException(String content) {
        super(content);
    }
}
