package fi.fmi.avi.parser.impl.metar;

import static fi.fmi.avi.parser.Lexeme.Identity.*;

import java.io.IOException;

import fi.fmi.avi.data.AviationWeatherMessage;
import fi.fmi.avi.data.metar.impl.MetarImpl;
import fi.fmi.avi.parser.ParsingHints;
import fi.fmi.avi.parser.TokenizingException;
import fi.fmi.avi.parser.Lexeme.Identity;
import fi.fmi.avi.parser.impl.AbstractAviMessageTest;

public class Metar8Test extends AbstractAviMessageTest {

	@Override
	public String getJsonFilename() {
		return "metar/metar8.json";
	}
	
	@Override
	public String getMessage() {
		return
				"EGXE 061150Z 03010KT 9999 FEW020 17/11 Q1014 BLACKBLU TEMPO 6000 SHRA SCT020 BLACKWHT=";
	}
	
	@Override
	public String getTokenizedMessagePrefix() {
		return "METAR ";
	}
	
	@Override
	public ParsingHints getLexerParsingHints() {
		return ParsingHints.METAR;
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
				METAR_START, AERODROME_DESIGNATOR, ISSUE_TIME, SURFACE_WIND, HORIZONTAL_VISIBILITY, CLOUD,
                AIR_DEWPOINT_TEMPERATURE, AIR_PRESSURE_QNH, COLOR_CODE, FORECAST_CHANGE_INDICATOR, HORIZONTAL_VISIBILITY, WEATHER, CLOUD, COLOR_CODE,
                END_TOKEN
		};
	}
	
	@Override
	public Class<? extends AviationWeatherMessage> getMessageClass() {
		return MetarImpl.class;
	}

}
