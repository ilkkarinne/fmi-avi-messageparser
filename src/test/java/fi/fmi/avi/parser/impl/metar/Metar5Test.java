package fi.fmi.avi.parser.impl.metar;

import static fi.fmi.avi.parser.Lexeme.Identity.AERODROME_DESIGNATOR;
import static fi.fmi.avi.parser.Lexeme.Identity.AIR_DEWPOINT_TEMPERATURE;
import static fi.fmi.avi.parser.Lexeme.Identity.AIR_PRESSURE_QNH;
import static fi.fmi.avi.parser.Lexeme.Identity.AUTOMATED;
import static fi.fmi.avi.parser.Lexeme.Identity.CAVOK;
import static fi.fmi.avi.parser.Lexeme.Identity.END_TOKEN;
import static fi.fmi.avi.parser.Lexeme.Identity.ISSUE_TIME;
import static fi.fmi.avi.parser.Lexeme.Identity.METAR_START;
import static fi.fmi.avi.parser.Lexeme.Identity.SURFACE_WIND;

import java.io.IOException;

import fi.fmi.avi.data.metar.impl.MetarImpl;
import fi.fmi.avi.parser.Lexeme.Identity;
import fi.fmi.avi.parser.TokenizingException;
import fi.fmi.avi.parser.impl.AbstractAviMessageTest;

public class Metar5Test extends AbstractAviMessageTest<String, MetarImpl> {

	@Override
	public String getJsonFilename() {
		return "metar/metar5.json";
	}
	
	@Override
	public String getMessage() {
		return
				"METAR EFTU 011350Z AUTO VRB02KT CAVOK 22/12 Q1008=";
	}
	
	@Override
	public String getTokenizedMessagePrefix() {
		return "";
	}

	
	// Remove this overridden method once the tokenizer is working
	@Override
	public void testTokenizer() throws TokenizingException, IOException {
		
	}

	// Remove this overridden method once the parser is working
	@Override
	public void testParser() throws IOException {
		
	}
	
	@Override
	public Identity[] getLexerTokenSequenceIdentity() {
		return new Identity[] {
				METAR_START, AERODROME_DESIGNATOR, ISSUE_TIME, AUTOMATED, SURFACE_WIND, CAVOK, AIR_DEWPOINT_TEMPERATURE,
                AIR_PRESSURE_QNH, END_TOKEN
		};
	}

    @Override
    public Class<String> getMessageInputClass() {
        return String.class;
    }

    @Override
    public Class<MetarImpl> getMessageOutputClass() {
        return MetarImpl.class;
	}
}
