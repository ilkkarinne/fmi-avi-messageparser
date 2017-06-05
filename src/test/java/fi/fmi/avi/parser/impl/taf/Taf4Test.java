package fi.fmi.avi.parser.impl.taf;

import static fi.fmi.avi.parser.Lexeme.Identity.AERODROME_DESIGNATOR;
import static fi.fmi.avi.parser.Lexeme.Identity.AMENDMENT;
import static fi.fmi.avi.parser.Lexeme.Identity.CANCELLATION;
import static fi.fmi.avi.parser.Lexeme.Identity.END_TOKEN;
import static fi.fmi.avi.parser.Lexeme.Identity.ISSUE_TIME;
import static fi.fmi.avi.parser.Lexeme.Identity.TAF_START;
import static fi.fmi.avi.parser.Lexeme.Identity.VALID_TIME;

import fi.fmi.avi.data.taf.impl.TAFImpl;
import fi.fmi.avi.parser.Lexeme.Identity;
import fi.fmi.avi.parser.impl.AbstractAviMessageTest;

public class Taf4Test extends AbstractAviMessageTest<String, TAFImpl> {

	@Override
	public String getJsonFilename() {
		return "taf/taf4.json";
	}
	
	@Override
	public String getMessage() {
		return
				"TAF AMD EFAB 191100Z 1909/1915 CNL=";
	}
	
	@Override
	public String getTokenizedMessagePrefix() {
		return "";
	}
	
	@Override
	public Identity[] getLexerTokenSequenceIdentity() {
		return new Identity[] {
				TAF_START, AMENDMENT, AERODROME_DESIGNATOR, ISSUE_TIME, VALID_TIME, CANCELLATION, END_TOKEN
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
