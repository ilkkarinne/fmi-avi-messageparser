package fi.fmi.avi.parser.impl.lexer.token;

import static fi.fmi.avi.parser.Lexeme.Identity.FORECAST_CHANGE_INDICATOR;
import static fi.fmi.avi.parser.Lexeme.ParsedValueName.DAY1;
import static fi.fmi.avi.parser.Lexeme.ParsedValueName.HOUR1;
import static fi.fmi.avi.parser.Lexeme.ParsedValueName.MINUTE1;
import static fi.fmi.avi.parser.Lexeme.ParsedValueName.TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import fi.fmi.avi.data.AviationWeatherMessage;
import fi.fmi.avi.data.metar.Metar;
import fi.fmi.avi.data.metar.TrendForecast;
import fi.fmi.avi.data.metar.TrendTimeGroups;
import fi.fmi.avi.data.taf.TAF;
import fi.fmi.avi.data.taf.TAFChangeForecast;
import fi.fmi.avi.parser.Lexeme;
import fi.fmi.avi.parser.ParsingHints;
import fi.fmi.avi.parser.TokenizingException;
import fi.fmi.avi.parser.impl.lexer.FactoryBasedReconstructor;

/**
 * Created by rinne on 10/02/17.
 */
public class ForecastChangeIndicator extends TimeHandlingRegex {

	public enum ForecastChangeIndicatorType {
        TEMPORARY_FLUCTUATIONS("TEMPO"),
        BECOMING("BECMG"),
        WITH_40_PCT_PROBABILITY("PROB40"),
        WITH_30_PCT_PROBABILITY("PROB30"),
        AT("AT"),
        FROM("FM"), UNTIL("TL"), NO_SIGNIFICANT_CHANGES("NOSIG");

        private String code;

        ForecastChangeIndicatorType(final String code) {
            this.code = code;
        }

        public static ForecastChangeIndicatorType forCode(final String code) {
            for (ForecastChangeIndicatorType w : values()) {
                if (w.code.equals(code)) {
                    return w;
                }
            }
            return null;
        }

    }

    public ForecastChangeIndicator(final Priority prio) {
        super("^(NOSIG|TEMPO|BECMG|PROB40|PROB30)|((AT|FM|TL)([0-9]{2})?([0-9]{2})([0-9]{2}))$", prio);
    }

    @Override
    public void visitIfMatched(final Lexeme token, final Matcher match, final ParsingHints hints) {
        ForecastChangeIndicatorType indicator;
        if (match.group(1) != null) {
            indicator = ForecastChangeIndicatorType.forCode(match.group(1));
            token.setParsedValue(TYPE, indicator);
            token.identify(FORECAST_CHANGE_INDICATOR);
        } else {
            indicator = ForecastChangeIndicatorType.forCode(match.group(3));
            int day = -1;
            if (match.group(4) != null) {
                day = Integer.parseInt(match.group(4));
            }
            int hour = Integer.parseInt(match.group(5));
            int minute = Integer.parseInt(match.group(6));
            if (timeOk(day, hour, minute)) {
                if (day > -1) {
                    token.setParsedValue(DAY1, day);
                }
                token.setParsedValue(HOUR1, hour);
                token.setParsedValue(MINUTE1, minute);
                token.setParsedValue(TYPE, indicator);
                token.identify(FORECAST_CHANGE_INDICATOR);
            } else {
                token.identify(FORECAST_CHANGE_INDICATOR, Lexeme.Status.SYNTAX_ERROR, "Invalid time");
            }
        }
    }
    

    public static class Reconstructor extends FactoryBasedReconstructor {

		@Override
		public <T extends AviationWeatherMessage> List<Lexeme> getAsLexemes(T msg, Class<T> clz, ParsingHints hints,
				Object... specifier) throws TokenizingException {
            List<Lexeme> retval = new ArrayList<>();

            if (msg instanceof TAF) {
                TAFChangeForecast changeForecast = getAs(specifier, TAFChangeForecast.class);

                if (changeForecast != null) {
                    switch (changeForecast.getChangeIndicator()) {
                        case BECOMING:
                            retval.add(this.createLexeme("BECMG", FORECAST_CHANGE_INDICATOR));
                            break;
                        case TEMPORARY_FLUCTUATIONS:
                            retval.add(this.createLexeme("TEMPO", FORECAST_CHANGE_INDICATOR));
                            break;
                        case PROBABILITY_30:
                            retval.add(this.createLexeme("PROB30", FORECAST_CHANGE_INDICATOR));
                            break;
                        case PROBABILITY_40:
                            retval.add(this.createLexeme("PROB40", FORECAST_CHANGE_INDICATOR));
                            break;
                        case PROBABILITY_30_TEMPORARY_FLUCTUATIONS:
                            retval.add(this.createLexeme("PROB30", FORECAST_CHANGE_INDICATOR));
                            retval.add(this.createLexeme("TEMPO", FORECAST_CHANGE_INDICATOR));
                            break;
                        case PROBABILITY_40_TEMPORARY_FLUCTUATIONS:
                            retval.add(this.createLexeme("PROB40", FORECAST_CHANGE_INDICATOR));
                            retval.add(this.createLexeme("TEMPO", FORECAST_CHANGE_INDICATOR));
                            break;
                        case FROM:
                            retval.add(createLexeme_From(changeForecast));
                            break;
                    }
                }
            } else if (msg instanceof Metar) {
                TrendForecast trend = getAs(specifier, TrendForecast.class);
                if (trend != null) {
                    switch (trend.getChangeIndicator()) {
                        case BECOMING: {
                            retval.add(this.createLexeme("BECMG", FORECAST_CHANGE_INDICATOR));
                            List<Lexeme> periodOfChange = createTrendTimeChangePeriods(trend.getTimeGroups());
                            if (periodOfChange.isEmpty()) {
                                throw new TokenizingException("No period of time for the trend of type BECOMING");
                            }
                            retval.addAll(periodOfChange);
                            break;
                        }
                        case TEMPORARY_FLUCTUATIONS: {
                            retval.add(this.createLexeme("TEMPO", FORECAST_CHANGE_INDICATOR));
                            List<Lexeme> periodOfChange = createTrendTimeChangePeriods(trend.getTimeGroups());
                            if (!periodOfChange.isEmpty()) {
                                retval.addAll(periodOfChange);
                            }
                            break;
                        }
                        case NO_SIGNIFICANT_CHANGES:
                            retval.add(this.createLexeme("NOSIG", FORECAST_CHANGE_INDICATOR));
                            break;
                    }
                }
            }

            return retval;
		}

		private Lexeme createLexeme_From(TAFChangeForecast changeForecast) {
			StringBuilder ret = new StringBuilder("FM");
            if (changeForecast.getValidityStartDayOfMonth() > -1) {
                ret.append(String.format("%02d%02d%02d", changeForecast.getValidityStartDayOfMonth(), changeForecast.getValidityStartHour(),
                        changeForecast.getValidityStartMinute()));
            } else {
                ret.append(String.format("%02d%02d", changeForecast.getValidityStartHour(), changeForecast.getValidityStartMinute()));
            }

            return this.createLexeme(ret.toString(), FORECAST_CHANGE_INDICATOR);
        }

        private List<Lexeme> createTrendTimeChangePeriods(final TrendTimeGroups timeGroups) {
            List<Lexeme> retval = new ArrayList<>();
            if (timeGroups != null) {
                if (timeGroups.isSingleInstance()) {
                    if (timeGroups.getFromHour() > -1 && timeGroups.getFromMinute() > -1) {
                        StringBuilder ret = new StringBuilder("AT");
                        ret.append(String.format("%02d%02d", timeGroups.getFromHour(), timeGroups.getFromMinute()));
                        retval.add(this.createLexeme(ret.toString(), FORECAST_CHANGE_INDICATOR));
                    }
                } else {
                    if (timeGroups.getFromHour() > -1 && timeGroups.getFromMinute() > -1) {
                        StringBuilder ret = new StringBuilder("FM");
                        ret.append(String.format("%02d%02d", timeGroups.getFromHour(), timeGroups.getFromMinute()));
                        retval.add(this.createLexeme(ret.toString(), FORECAST_CHANGE_INDICATOR));
                    }
                    if (timeGroups.getToHour() > -1 && timeGroups.getToMinute() > -1) {
                        StringBuilder ret = new StringBuilder("TL");
                        ret.append(String.format("%02d%02d", timeGroups.getToHour(), timeGroups.getToMinute()));
                        retval.add(this.createLexeme(ret.toString(), FORECAST_CHANGE_INDICATOR));
                    }
                }
            }
            return retval;
        }

    }

}
