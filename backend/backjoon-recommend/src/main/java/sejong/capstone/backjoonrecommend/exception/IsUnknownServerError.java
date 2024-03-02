package sejong.capstone.backjoonrecommend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "해결이 되지 않은 서버 에러가 발생하였습니다.")
public class IsUnknownServerError extends RuntimeException{
    public IsUnknownServerError(String message) {
        super(message);
    }
}
