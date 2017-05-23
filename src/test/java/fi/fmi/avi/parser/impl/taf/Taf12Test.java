package fi.fmi.avi.parser.impl.taf;

import static fi.fmi.avi.parser.Lexeme.Identity.*;

import fi.fmi.avi.data.AviationWeatherMessage;
import fi.fmi.avi.data.taf.impl.TAFImpl;
import fi.fmi.avi.parser.ParsingHints;
import fi.fmi.avi.parser.Lexeme.Identity;
import fi.fmi.avi.parser.impl.AbstractAviMessageTest;

public class Taf12Test extends AbstractAviMessageTest {

	@Override
	public String getJsonFilename() {
		return "taf/taf12.json";
	}
	
	@Override
	public String getMessage() {
		return
				"EETN 301130Z 3012/3112 14016G26KT 8000 BKN010 OVC015 TXM02/3015Z TNM10/3103Z " + 
				"TEMPO 3012/3018 3000 RADZ BR OVC004 " +
			    "BECMG 3018/3020 BKN008 SCT015CB " + 
				"TEMPO 3102/3112 3000 SHRASN BKN006 BKN015CB " + 
			    "BECMG 3104/3106 21016G30KT=";
	}
	
	@Override
	public ParsingHints getParserParsingHints() {
        ParsingHints hints = new ParsingHints();
        hints.put(ParsingHints.KEY_MESSAGE_TYPE, ParsingHints.VALUE_MESSAGE_TYPE_TAF);
        hints.put(ParsingHints.KEY_TIMEZONE_ID_POLICY, ParsingHints.VALUE_TIMEZONE_ID_POLICY_STRICT);

        return hints;
	}

	@Override
	public String getTokenizedMessagePrefix() {
		return "TAF ";
	}
	
	@Override
	public ParsingHints getLexerParsingHints() {
		return ParsingHints.TAF;
	}
	
	@Override
	public Identity[] getLexerTokenSequenceIdentity() {
		return new Identity[] {
				TAF_START, AERODROME_DESIGNATOR, ISSUE_TIME, VALID_TIME, SURFACE_WIND, HORIZONTAL_VISIBILITY, CLOUD, CLOUD,
                MAX_TEMPERATURE, MIN_TEMPERATURE, FORECAST_CHANGE_INDICATOR, CHANGE_FORECAST_TIME_GROUP, HORIZONTAL_VISIBILITY, WEATHER, WEATHER, CLOUD,
                FORECAST_CHANGE_INDICATOR, CHANGE_FORECAST_TIME_GROUP, CLOUD, CLOUD, FORECAST_CHANGE_INDICATOR, CHANGE_FORECAST_TIME_GROUP,
                HORIZONTAL_VISIBILITY, WEATHER, CLOUD, CLOUD, FORECAST_CHANGE_INDICATOR, CHANGE_FORECAST_TIME_GROUP, SURFACE_WIND, END_TOKEN
		};
	}
	
	@Override
	public Class<? extends AviationWeatherMessage> getMessageClass() {
		return TAFImpl.class;
	}

}
