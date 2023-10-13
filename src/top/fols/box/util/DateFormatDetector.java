package top.fols.box.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Deprecated
@SuppressWarnings("UnnecessaryReturnStatement")
public class DateFormatDetector extends DataTypeConverter.Converter<String, Date> {
    static final String[] DATE_FORMATS = {
            "yyyy-MM-dd HH:mm:ss.S",


            "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd",
            "yyyy.MM",

            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd",
            "yyyy/MM",

            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy-MM",

            "yyyyMMddHHmmss",
            "yyyyMMdd",
            "yyyyMM",


            "dd.MM.yyyy HH:mm:ss",
            "dd.MM.yyyy",

            "dd/MM/yyyy HH:mm:ss",
            "dd/MM/yyyy",

            "dd-MM-yyyy HH:mm:ss",
            "dd-MM-yyyy",

            "ddMMyyyyHHmmss",
            "ddMMyyyy",


            "MM.dd.yyyy HH:mm:ss",
            "MM.dd.yyyy",

            "MM/dd/yyyy HH:mm:ss",
            "MM/dd/yyyy",

            "MM-dd-yyyy HH:mm:ss",
            "MM-dd-yyyy",

            "MMddyyyyHHmmss",
            "MMddyyyy",


            "EEE MMM dd HH:mm:ss zzz yyyy",  // JDK Date.toString()
            "d MMM yyyy HH:mm:ss 'GMT'",     // JDK Date.toGMTString()

            // ISO 8601
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",

            // RFC 822 / RFC 1123
            "EEE, dd MMM yyyy HH:mm:ss zzz",

            //HTTP
            "EEE, dd MMM yyyy HH:mm:ss z"
    };

    static final Locale[] locales;

    static {
        Set<Locale> localeSort;
        localeSort = new LinkedHashSet<>();
        localeSort.add(Locale.ENGLISH);
        localeSort.add(Locale.CHINESE);
        localeSort.add(Locale.getDefault());
        //	localeSort.add(Locale.TRADITIONAL_CHINESE);
        //  localeSort.addAll(Arrays.asList(Locale.getAvailableLocales()));

        locales = localeSort.toArray(new Locale[]{});
    }


    public DateFormatDetector() {
    }

    public DateFormatDetector(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    TimeZone timeZone;

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        this.setTimeZoneToList(getTimeZone());
    }

    public TimeZone getTimeZone() {
        TimeZone conf = timeZone;
        return conf == null ? TimeZone.getDefault() : conf;
    }

    static void addFormatText(Set<SimpleDateFormat> sdfs, String format, TimeZone timeZone) {
        final long currentTime = System.currentTimeMillis();
        final Map<Locale, Map<String, SimpleDateFormat>> smMap = new HashMap<>();
        final Map<String, Locale> lm = new LinkedHashMap<>(); //result -> locale
        for (Locale locale : locales) {
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat(format, locale);
            sdf.setTimeZone(timeZone);


            String lms = sdf.format(currentTime);
            if (!lm.containsKey(lms)) {
                lm.put(lms, locale);

                Map<String, SimpleDateFormat> mls = smMap.get(locale);
                if (null == mls)
                    smMap.put(locale, mls = new LinkedHashMap<>());
                mls.put(format, sdf);
            }
        }
        for (Locale locale : smMap.keySet()) {
            for (Map.Entry<String, SimpleDateFormat> sdf : smMap.get(locale).entrySet()) {
                sdfs.add(sdf.getValue());
            }
        }
    }


    void setTimeZoneToList(TimeZone timeZone) {
        Set<SimpleDateFormat> sdfs = this.sdfs;
        if (null != sdfs) {
            for (SimpleDateFormat sdf : sdfs) {
                sdf.setTimeZone(timeZone);
            }
        }
    }

    Set<SimpleDateFormat> sdfs;

    Set<SimpleDateFormat> getList() {
        Set<SimpleDateFormat> sdfs = this.sdfs;
        if (null == sdfs) {
            sdfs = new LinkedHashSet<>();
            for (String format : DATE_FORMATS) {
                addFormatText(sdfs, format, getTimeZone());
            }
            sdfs = this.sdfs = sdfs;
        }
        return sdfs;
    }


    public void addFormatText(String format) {
        addFormatText(getList(), Objects.requireNonNull(format), getTimeZone());
    }

    public Set<String> getFormatTexts() {
        Set<String> texts = new LinkedHashSet<>();
        for (SimpleDateFormat f : getList()) {
            texts.add(f.toPattern());
        }
        return texts;
    }

    public boolean removeFormatText(String format) {
        Objects.requireNonNull(format);

        boolean result = false;
        Iterator<SimpleDateFormat> iterator = getList().iterator();
        while (iterator.hasNext()) {
            SimpleDateFormat f = iterator.next();
            if (format.equals(f.toPattern())) {
                iterator.remove();

                result = true;
            }
        }
        return result;
    }

    volatile SimpleDateFormat[] CACHES = {null, null, null};

    void cache(SimpleDateFormat newCache) {
        SimpleDateFormat[] cs = CACHES;
        if (cs[0] == newCache) {
            return;
        } else {
            if (cs[1] == newCache) {
                cs[1] = cs[0];
            } else if (cs[2] == newCache) {
                cs[2] = cs[0];
            } else {
                cs[2] = cs[1];
                cs[1] = cs[0];
            }
            cs[0] = newCache;
        }
    }


    Date tryParse(SimpleDateFormat sdf, String input) {
        if (null == input) {
            return null;
        }
        if (null != sdf) {
            Date date = null;
            try {
                Date ret = sdf.parse(input);
                if (input.equals(sdf.format(ret))) {
                    date = ret;
                    return ret;
                }
            } catch (ParseException ignored) {
            } finally {
                if (null != date) {
                    cache(sdf);
                }
            }
        }
        return null;
    }

    Object[] parse(String input) throws ParseException {
        if (null != input) {
            for (SimpleDateFormat sdf : CACHES) {
                if (null != sdf) {
                    Date ret = tryParse(sdf, input);
                    if (null != ret) {
                        return new Object[]{sdf, ret};
                    }
                }
            }
            for (SimpleDateFormat sdf : getList()) {
                Date ret = tryParse(sdf, input);
                if (null != ret) {
                    return new Object[]{sdf, ret};
                }
            }
        }
        throw new ParseException(input, 0);
    }


    protected SimpleDateFormat parseToDateFormater(String input) throws ParseException {
        if (null == input) {
            return null;
        }
        Object[] result = parse(input);
        return (SimpleDateFormat) result[0];
    }

    public Date parseToDate(String input) throws ParseException {
        if (null == input) {
            return null;
        }
        Object[] result = parse(input);
        return (Date) result[1];
    }


    @Override
    public String toString() {
        // TODO: Implement this method
        return getFormatTexts().toString();
    }

    @Override
    public Date convert(String object, Object query) {
        // TODO: Implement this method
        if (null == object || object.isEmpty()) return null;
        if (null == query) {
            try {
                return this.parseToDate(object);
            } catch (ParseException e) {
                throw new DataTypeConverter.DataConvertException(e);
            }
        } else if (query instanceof SimpleDateFormat) {
            try {
                return ((SimpleDateFormat) query).parse(object);
            } catch (ParseException e) {
                throw new DataTypeConverter.DataConvertException(e);
            }
        }
        throw new DataTypeConverter.DataConvertException("unsupported format: " + object);
    }
}
