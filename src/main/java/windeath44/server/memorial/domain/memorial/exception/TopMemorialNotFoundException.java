package windeath44.server.memorial.domain.memorial.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class TopMemorialNotFoundException extends GlobalException {
    public TopMemorialNotFoundException() {
        super(ErrorCode.TOP_MEMORIAL_NOT_FOUND);
    }
}
