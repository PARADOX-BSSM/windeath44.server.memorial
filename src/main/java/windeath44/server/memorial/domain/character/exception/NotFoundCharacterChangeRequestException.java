package windeath44.server.memorial.domain.character.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class NotFoundCharacterChangeRequestException extends GlobalException {

    public NotFoundCharacterChangeRequestException() {
        super(ErrorCode.CHARACTER_CHANGE_REQUEST_NOT_FOUND);
    }

    private static class Holder {
        private static final NotFoundCharacterChangeRequestException INSTANCE = new NotFoundCharacterChangeRequestException();
    }

    public static NotFoundCharacterChangeRequestException getInstance() {
        return Holder.INSTANCE;
    }
}
