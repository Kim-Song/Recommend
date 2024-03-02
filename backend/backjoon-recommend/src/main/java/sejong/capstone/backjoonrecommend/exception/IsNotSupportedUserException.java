package sejong.capstone.backjoonrecommend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "해당 유저는 지원하지 않습니다.")
public class IsNotSupportedUserException extends RuntimeException {
    public IsNotSupportedUserException(String content) {
        super(content);
    }
}
