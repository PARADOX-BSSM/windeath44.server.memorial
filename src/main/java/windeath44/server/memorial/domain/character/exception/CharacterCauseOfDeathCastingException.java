package windeath44.server.memorial.domain.character.exception;

import windeath44.server.memorial.global.error.exception.ErrorCode;
import windeath44.server.memorial.global.error.exception.GlobalException;

public class CharacterCauseOfDeathCastingException extends GlobalException {
    public CharacterCauseOfDeathCastingException() {
        super(ErrorCode.CHARACTER_CAUSE_OF_DEATH_CASTING_FAILED);
    }
    static class Holder {
        private final static CharacterCauseOfDeathCastingException INSTANCE = new CharacterCauseOfDeathCastingException();
    }

    public  static CharacterCauseOfDeathCastingException getInstance() {
        return Holder.INSTANCE;
    }
}
