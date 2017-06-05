package fi.fmi.avi.parser.impl.taf;

import static fi.fmi.avi.parser.Lexeme.Identity.AERODROME_DESIGNATOR;
import static fi.fmi.avi.parser.Lexeme.Identity.CAVOK;
import static fi.fmi.avi.parser.Lexeme.Identity.END_TOKEN;
import static fi.fmi.avi.parser.Lexeme.Identity.HORIZONTAL_VISIBILITY;
import static fi.fmi.avi.parser.Lexeme.Identity.ISSUE_TIME;
import static fi.fmi.avi.parser.Lexeme.Identity.REMARK;
import static fi.fmi.avi.parser.Lexeme.Identity.REMARKS_START;
import static fi.fmi.avi.parser.Lexeme.Identity.SURFACE_WIND;
import static fi.fmi.avi.parser.Lexeme.Identity.TAF_START;
import static fi.fmi.avi.parser.Lexeme.Identity.VALID_TIME;

import fi.fmi.avi.data.taf.impl.TAFImpl;
import fi.fmi.avi.parser.Lexeme.Identity;
import fi.fmi.avi.parser.ParsingHints;
import fi.fmi.avi.parser.impl.AbstractAviMessageTest;

public class Taf16Test extends AbstractAviMessageTest<String, TAFImpl> {

	@Override
	public String getJsonFilename() {
		return "taf/taf16.json";
	}
	
	// Short version of validity time
	@Override
	public String getMessage() {
		return
				"TAF EFKU 190840Z 191618 18020KT CAVOK 7500 RMK HELLO WORLD WIND 700FT 13010KT=";
	}
	
	@Override
	public String getTokenizedMessagePrefix() {
		return "";
	}
	
	@Override
	public ParsingHints getTokenizerParsingHints() {
		ParsingHints hints = super.getTokenizerParsingHints();
        hints.put(ParsingHints.KEY_VALIDTIME_FORMAT, ParsingHints.VALUE_VALIDTIME_FORMAT_PREFER_SHORT);
		return hints;
	}
	
	@Override
	public Identity[] getLexerTokenSequenceIdentity() {
		return new Identity[] {
				TAF_START, AERODROME_DESIGNATOR, ISSUE_TIME, 
        		VALID_TIME, SURFACE_WIND, CAVOK, HORIZONTAL_VISIBILITY, REMARKS_START, REMARK, REMARK,
        		REMARK, REMARK, REMARK, END_TOKEN
		};
	}

	@Override
	public Class<String> getMessageInputClass() {
		return String.class;
	}

	@Override
	public Class<TAFImpl> getMessageOutputClass() {
		return TAFImpl.class;
	}

}
