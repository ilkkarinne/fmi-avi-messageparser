package fi.fmi.avi.parser.impl.metar;

import static fi.fmi.avi.tac.lexer.Lexeme.Identity.AERODROME_DESIGNATOR;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.AIR_DEWPOINT_TEMPERATURE;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.AIR_PRESSURE_QNH;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.CLOUD;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.END_TOKEN;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.FORECAST_CHANGE_INDICATOR;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.HORIZONTAL_VISIBILITY;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.ISSUE_TIME;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.METAR_START;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.RUNWAY_VISUAL_RANGE;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.SEA_STATE;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.SURFACE_WIND;
import static fi.fmi.avi.tac.lexer.Lexeme.Identity.WEATHER;

import fi.fmi.avi.data.metar.METAR;
import fi.fmi.avi.data.metar.impl.METARImpl;
import fi.fmi.avi.converter.ConversionSpecification;
import fi.fmi.avi.parser.impl.AbstractAviMessageTest;
import fi.fmi.avi.tac.lexer.Lexeme.Identity;

public class METAR11Test extends AbstractAviMessageTest<String, METAR> {

	@Override
	public String getJsonFilename() {
		return "metar/metar11.json";
	}
	
	@Override
	public String getMessage() {
		return
				"METAR EFHK 111111Z 15008KT 0700 R04R/1500N R15/1000U R22L/1200N R04L/1000VP1500U SN VV006 M08/M10 Q1023 " + "WM01/S1 TEMPO TL1530 +SHRA BKN012CB=";
	}
	
	@Override
	public String getTokenizedMessagePrefix() {
		return "";
	}
	
	@Override
	public Identity[] getLexerTokenSequenceIdentity() {
		return new Identity[] {
				METAR_START, AERODROME_DESIGNATOR, ISSUE_TIME, SURFACE_WIND, HORIZONTAL_VISIBILITY, RUNWAY_VISUAL_RANGE,
                RUNWAY_VISUAL_RANGE, RUNWAY_VISUAL_RANGE, RUNWAY_VISUAL_RANGE, WEATHER, CLOUD, AIR_DEWPOINT_TEMPERATURE, AIR_PRESSURE_QNH,
                SEA_STATE, FORECAST_CHANGE_INDICATOR, FORECAST_CHANGE_INDICATOR, WEATHER, CLOUD, END_TOKEN
		};
	}

	@Override
    public ConversionSpecification<String, METAR> getParsingSpecification() {
        return ConversionSpecification.TAC_TO_METAR_POJO;
    }
	
	@Override
    public ConversionSpecification<METAR, String> getSerializationSpecification() {
        return ConversionSpecification.METAR_POJO_TO_TAC;
    }

	@Override
    public Class<? extends METAR> getTokenizerImplmentationClass() {
        return METARImpl.class;
    }

}
