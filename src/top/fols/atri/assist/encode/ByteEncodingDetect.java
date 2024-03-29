package top.fols.atri.assist.encode;

import top.fols.box.io.interfaces.IIOBigSequenceByte;

import java.io.IOException;
import java.nio.charset.Charset;

public class ByteEncodingDetect {
    // Frequency tables to hold the GB, Big5, and EUC-TW character
    // frequencies
    static final int GB_[][];
    static final int GBK_[][];
    static final int Big5_[][];
    static final int Big5P_[][];
    static final int EUC_TW_[][];
    static final int KR_[][];
    static final int JP_[][];
    static {
        ByteEncodingDetectInit.initialize_frequencies();
        GB_ = ByteEncodingDetectInit.GB_;
        GBK_ = ByteEncodingDetectInit.GBK_;
        Big5_ = ByteEncodingDetectInit.Big5_;
        Big5P_ = ByteEncodingDetectInit.Big5P_;
        EUC_TW_ = ByteEncodingDetectInit.EUC_TW_;

        KR_ = ByteEncodingDetectInit.KR_;
        JP_ = ByteEncodingDetectInit.JP_;
    }
    static final String DEFAULT_CHARSET = Charset.defaultCharset().name();

    String defCharset = DEFAULT_CHARSET;
    public ByteEncodingDetect() { }


    @SuppressWarnings("UnnecessaryEnumModifier")
    public static enum EncodingType {
        GB2312("GB2312"),
        GBK("GBK"),
        GB18030("GB18030"),
        HZ("ASCII"),// What to put here? Sun doesn't support HZ
        BIG5("BIG5"),
        CNS11643("EUC-TW"),
        UTF8("UTF-8"),
        UNICODE("Unicode"),
        ISO2022CN("ISO2022CN"),
        EUC_KR("EUC_KR"),
        CP949("MS949"),
        ISO2022KR("ISO2022KR"),
        SJIS("SJIS"),
        EUC_JP("EUC_JP"),
        ISO2022JP("ISO2022JP"),
        ASCII("ASCII"),
        OTHER("ISO8859_1");

        String javaCharsetName;
        EncodingType(String javaCharsetName) {
            this.javaCharsetName = javaCharsetName;
        }
    }
    public String detectEncodingAsName(IIOBigSequenceByte rawtext) throws IOException {
        EncodingType encodingType = detectEncoding(rawtext);
        if (null ==  encodingType)
            return defCharset;
        return encodingType.javaCharsetName;
    }




    /**
     * Function : detectEncoding Aruguments: byte array Returns : One of the
     * encodings from the Encoding enumeration (GB2312, HZ, BIG5, EUC_TW, ASCII, or
     * OTHER) Description: This function looks at the byte array and assigns it a
     * probability score for each encoding type. The encoding type with the highest
     * probability is returned.
     *
     * @return nullable
     */
    public EncodingType detectEncoding(IIOBigSequenceByte rawtext) throws IOException {
        if (rawtext.length() == 0) {
            return null;
        }

        final int GB2312 = 0;
        final int GBK = 1;
        final int GB18030 = 2;
        final int HZ = 3;
        final int BIG5 = 4;
        final int CNS11643 = 5;
        final int ISO2022CN = 6;
        final int UTF8 = 7;
        final int UNICODE = 8;
        final int EUC_KR = 9;
        final int CP949 = 10;
        final int ISO2022KR = 11;
        final int ASCII = 12;
        final int SJIS = 13;
        final int EUC_JP = 14;
        final int ISO2022JP = 15;
        final int OTHER = 16;

        int[] scores;
        int index, maxscore = 0;
        int encoding_guess = OTHER;
        scores = new int[17];

        // Assign Scores
        scores[GB2312] = gb2312_probability(rawtext);
        scores[GBK] = gbk_probability(rawtext);
        scores[GB18030] = gb18030_probability(rawtext);
        scores[HZ] = hz_probability(rawtext);
        scores[BIG5] = big5_probability(rawtext);
        scores[CNS11643] = euc_tw_probability(rawtext);
        scores[ISO2022CN] = iso_2022_cn_probability(rawtext);
        scores[UTF8] = utf8_probability(rawtext);
        scores[UNICODE] = utf16_probability(rawtext);
        scores[EUC_KR] = euc_kr_probability(rawtext);
        scores[CP949] = cp949_probability(rawtext);
        scores[ISO2022KR] = iso_2022_kr_probability(rawtext);
        scores[ASCII] = ascii_probability(rawtext);
        scores[SJIS] = sjis_probability(rawtext);
        scores[EUC_JP] = euc_jp_probability(rawtext);
        scores[ISO2022JP] = iso_2022_jp_probability(rawtext);
        scores[OTHER] = 0;
        // Tabulate Scores
        for (index = 0; index < scores.length; index++) {
            if (scores[index] > maxscore) {
                encoding_guess = index;
                maxscore = scores[index];
            }
        }
        // Return OTHER if nothing scored above 50
        if (maxscore <= 50) {
            encoding_guess = OTHER;
        }
        switch (encoding_guess) {
            case GB2312:
                return EncodingType.GB2312;
            case GBK:
                return EncodingType.GBK;
            case GB18030:
                return EncodingType.GB18030;
            case HZ:
                return EncodingType.HZ;
            case BIG5:
                return EncodingType.BIG5;
            case CNS11643:
                return EncodingType.CNS11643;
            case UTF8:
                return EncodingType.UTF8;
            case UNICODE:
                return EncodingType.UNICODE;
            case ISO2022CN:
                return EncodingType.ISO2022CN;
            case EUC_KR:
                return EncodingType.EUC_KR;
            case CP949:
                return EncodingType.CP949;
            case ISO2022KR:
                return EncodingType.ISO2022KR;
            case SJIS:
                return EncodingType.SJIS;
            case EUC_JP:
                return EncodingType.EUC_JP;
            case ISO2022JP:
                return EncodingType.ISO2022JP;
            case ASCII:
                return EncodingType.ASCII;
            case OTHER:
                return EncodingType.OTHER;
        }
        return null;
    }


    /*
     * Function: gb2312_probability Argument: pointer to byte array Returns : number
     * from 0 to 100 representing probability text in array uses GB-2312 encoding
     */
    int gb2312_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i;
        long rawtextlen = 0;
        long dbchars = 1, gbchars = 1;
        long gbfreq = 0, totalfreq = 1;
        float rangeval = 0, freqval = 0;
        int row, column;
        // Stage 1: Check to see if characters fit into acceptable ranges
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen - 1; i++) {
            // System.err.println(rawtext[i]);
            if (rawtext.byteAt(i) >= 0) {
                // asciichars++;
            } else {
                dbchars++;
                if ((byte) 0xA1 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xF7
                        && (byte) 0xA1 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0xFE) {
                    gbchars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) + 256 - 0xA1;
                    column = rawtext.byteAt(i + 1) + 256 - 0xA1;
                    if (GB_[row][column] != 0) {
                        gbfreq += GB_[row][column];
                    } else if (15 <= row && row < 55) {
                        // In GB high-freq character range
                        gbfreq += 200;
                    }
                }
                i++;
            }
        }
        rangeval = 50 * ((float) gbchars / (float) dbchars);
        freqval = 50 * ((float) gbfreq / (float) totalfreq);
        return (int) (rangeval + freqval);
    }

    /*
     * Function: gbk_probability Argument: pointer to byte array Returns : number
     * from 0 to 100 representing probability text in array uses GBK encoding
     */
    int gbk_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i, rawtextlen = 0;
        long dbchars = 1, gbchars = 1;
        long gbfreq = 0, totalfreq = 1;
        float rangeval = 0, freqval = 0;
        int row, column;
        // Stage 1: Check to see if characters fit into acceptable ranges
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen - 1; i++) {
            // System.err.println(rawtext.get(i));
            if (rawtext.byteAt(i) >= 0) {
                // asciichars++;
            } else {
                dbchars++;
                if ((byte) 0xA1 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xF7 && // Original GB range
                        (byte) 0xA1 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0xFE) {
                    gbchars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) + 256 - 0xA1;
                    column = rawtext.byteAt(i + 1) + 256 - 0xA1;
                    // System.out.println("original row " + row + " column " + column);
                    if (GB_[row][column] != 0) {
                        gbfreq += GB_[row][column];
                    } else if (15 <= row && row < 55) {
                        gbfreq += 200;
                    }
                } else if ((byte) 0x81 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xFE && // Extended GB
                        // range
                        (((byte) 0x80 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0xFE)
                                || ((byte) 0x40 <= rawtext.byteAt(i + 1)
                                && rawtext.byteAt(i + 1) <= (byte) 0x7E))) {
                    gbchars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) + 256 - 0x81;
                    if (0x40 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0x7E) {
                        column = rawtext.byteAt(i + 1) - 0x40;
                    } else {
                        column = rawtext.byteAt(i + 1) + 256 - 0x40;
                    }
                    // System.out.println("extended row " + row + " column " + column + "
                    // rawtext.get(i) " + rawtext.get(i));
                    if (GBK_[row][column] != 0) {
                        gbfreq += GBK_[row][column];
                    }
                }
                i++;
            }
        }
        rangeval = 50 * ((float) gbchars / (float) dbchars);
        freqval = 50 * ((float) gbfreq / (float) totalfreq);
        // For regular GB files, this would give the same score, so I handicap it
        // slightly
        return (int) (rangeval + freqval) - 1;
    }

    /*
     * Function: gb18030_probability Argument: pointer to byte array Returns :
     * number from 0 to 100 representing probability text in array uses GBK encoding
     */
    int gb18030_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i, rawtextlen = 0;
        long dbchars = 1, gbchars = 1;
        long gbfreq = 0, totalfreq = 1;
        float rangeval = 0, freqval = 0;
        int row, column;
        // Stage 1: Check to see if characters fit into acceptable ranges
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen - 1; i++) {
            // System.err.println(rawtext.get(i));
            if (rawtext.byteAt(i) >= 0) {
                // asciichars++;
            } else {
                dbchars++;
                if ((byte) 0xA1 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xF7 && // Original GB range
                        i + 1 < rawtextlen && (byte) 0xA1 <= rawtext.byteAt(i + 1)
                        && rawtext.byteAt(i + 1) <= (byte) 0xFE) {
                    gbchars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) + 256 - 0xA1;
                    column = rawtext.byteAt(i + 1) + 256 - 0xA1;
                    // System.out.println("original row " + row + " column " + column);
                    if (GB_[row][column] != 0) {
                        gbfreq += GB_[row][column];
                    } else if (15 <= row && row < 55) {
                        gbfreq += 200;
                    }
                } else if ((byte) 0x81 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xFE && // Extended GB
                        // range
                        i + 1 < rawtextlen
                        && (((byte) 0x80 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0xFE)
                        || ((byte) 0x40 <= rawtext.byteAt(i + 1)
                        && rawtext.byteAt(i + 1) <= (byte) 0x7E))) {
                    gbchars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) + 256 - 0x81;
                    if (0x40 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0x7E) {
                        column = rawtext.byteAt(i + 1) - 0x40;
                    } else {
                        column = rawtext.byteAt(i + 1) + 256 - 0x40;
                    }
                    // System.out.println("extended row " + row + " column " + column + "
                    // rawtext.get(i) " + rawtext.get(i));
                    if (GBK_[row][column] != 0) {
                        gbfreq += GBK_[row][column];
                    }
                } else if ((byte) 0x81 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xFE && // Extended GB
                        // range
                        i + 3 < rawtextlen && (byte) 0x30 <= rawtext.byteAt(i + 1)
                        && rawtext.byteAt(i + 1) <= (byte) 0x39 && (byte) 0x81 <= rawtext.byteAt(i + 2)
                        && rawtext.byteAt(i + 2) <= (byte) 0xFE && (byte) 0x30 <= rawtext.byteAt(i + 3)
                        && rawtext.byteAt(i + 3) <= (byte) 0x39) {
                    gbchars++;
                    /*
                     * totalfreq += 500; row = rawtext.get(i) + 256 - 0x81; if (0x40 <=
                     * rawtext.get(i+1) && rawtext.get(i+1) <= 0x7E) { column = rawtext.get(i+1) -
                     * 0x40; } else { column = rawtext.get(i+1) + 256 - 0x40; }
                     * //System.out.println("extended row " + row + " column " + column + "
                     * rawtext.get(i) " + rawtext.get(i)); if (GBKFreq[row][column] != 0) { gbfreq
                     * += GBKFreq[row][column]; }
                     */
                }
                i++;
            }
        }
        rangeval = 50 * ((float) gbchars / (float) dbchars);
        freqval = 50 * ((float) gbfreq / (float) totalfreq);
        // For regular GB files, this would give the same score, so I handicap it
        // slightly
        return (int) (rangeval + freqval) - 1;
    }

    /*
     * Function: hz_probability Argument: byte array Returns : number from 0 to 100
     * representing probability text in array uses HZ encoding
     */
    int hz_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i, rawtextlen;
        long hzchars = 0, dbchars = 1;
        long hzfreq = 0, totalfreq = 1;
        float rangeval = 0, freqval = 0;
        long hzstart = 0, hzend = 0;
        int row, column;
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen; i++) {
            if (i + 1 < rawtextlen && rawtext.byteAt(i) == '~') {
                if (rawtext.byteAt(i + 1) == '{') {
                    hzstart++;
                    i += 2;
                    while (i < rawtextlen - 1) {
                        if (rawtext.byteAt(i) == 0x0A || rawtext.byteAt(i) == 0x0D) {
                            break;
                        } else if (rawtext.byteAt(i) == '~' && rawtext.byteAt(i + 1) == '}') {
                            hzend++;
                            i++;
                            break;
                        } else if ((0x21 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= 0x77)
                                && (0x21 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0x77)) {
                            hzchars += 2;
                            row = rawtext.byteAt(i) - 0x21;
                            column = rawtext.byteAt(i + 1) - 0x21;
                            totalfreq += 500;
                            if (GB_[row][column] != 0) {
                                hzfreq += GB_[row][column];
                            } else if (15 <= row && row < 55) {
                                hzfreq += 200;
                            }
                        } else if ((0xA1 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= 0xF7)
                                && (0xA1 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0xF7)) {
                            hzchars += 2;
                            row = rawtext.byteAt(i) + 256 - 0xA1;
                            column = rawtext.byteAt(i + 1) + 256 - 0xA1;
                            totalfreq += 500;
                            if (GB_[row][column] != 0) {
                                hzfreq += GB_[row][column];
                            } else if (15 <= row && row < 55) {
                                hzfreq += 200;
                            }
                        }
                        dbchars += 2;
                        i += 2;
                    }
                } else if (rawtext.byteAt(i + 1) == '}') {
                    hzend++;
                    i++;
                } else if (rawtext.byteAt(i + 1) == '~') {
                    i++;
                }
            }
        }
        if (hzstart > 4) {
            rangeval = 50;
        } else if (hzstart > 1) {
            rangeval = 41;
        } else if (hzstart > 0) { // Only 39 in case the sequence happened to occur
            rangeval = 39; // in otherwise non-Hz text
        } else {
            rangeval = 0;
        }
        freqval = 50 * ((float) hzfreq / (float) totalfreq);
        return (int) (rangeval + freqval);
    }

    /**
     * Function: big5_probability Argument: byte array Returns : number from 0 to
     * 100 representing probability text in array uses Big5 encoding
     */
    int big5_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i, rawtextlen = 0;
        long dbchars = 1, bfchars = 1;
        float rangeval = 0, freqval = 0;
        long bffreq = 0, totalfreq = 1;
        int row, column;
        // Check to see if characters fit into acceptable ranges
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen - 1; i++) {
            if (rawtext.byteAt(i) >= 0) {
                // asciichars++;
            } else {
                dbchars++;
                if ((byte) 0xA1 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xF9
                        && (((byte) 0x40 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0x7E)
                        || ((byte) 0xA1 <= rawtext.byteAt(i + 1)
                        && rawtext.byteAt(i + 1) <= (byte) 0xFE))) {
                    bfchars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) + 256 - 0xA1;
                    if (0x40 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0x7E) {
                        column = rawtext.byteAt(i + 1) - 0x40;
                    } else {
                        column = rawtext.byteAt(i + 1) + 256 - 0x61;
                    }
                    if (Big5_[row][column] != 0) {
                        bffreq += Big5_[row][column];
                    } else if (3 <= row && row <= 37) {
                        bffreq += 200;
                    }
                }
                i++;
            }
        }
        rangeval = 50 * ((float) bfchars / (float) dbchars);
        freqval = 50 * ((float) bffreq / (float) totalfreq);
        return (int) (rangeval + freqval);
    }

    /*
     * Function: big5plus_probability Argument: pointer to unsigned char array
     * Returns : number from 0 to 100 representing probability text in array uses
     * Big5+ encoding
     */
    int big5plus_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i, rawtextlen = 0;
        long dbchars = 1, bfchars = 1;
        long bffreq = 0, totalfreq = 1;
        float rangeval = 0, freqval = 0;
        int row, column;
        // Stage 1: Check to see if characters fit into acceptable ranges
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen - 1; i++) {
            // System.err.println(rawtext.get(i));
            if (rawtext.byteAt(i) >= 128) {
                // asciichars++;
            } else {
                dbchars++;
                if (0xA1 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= 0xF9 && // Original Big5 range
                        ((0x40 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0x7E)
                                || (0xA1 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0xFE))) {
                    bfchars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) - 0xA1;
                    if (0x40 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0x7E) {
                        column = rawtext.byteAt(i + 1) - 0x40;
                    } else {
                        column = rawtext.byteAt(i + 1) - 0x61;
                    }
                    // System.out.println("original row " + row + " column " + column);
                    if (Big5_[row][column] != 0) {
                        bffreq += Big5_[row][column];
                    } else if (3 <= row && row < 37) {
                        bffreq += 200;
                    }
                } else if (0x81 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= 0xFE && // Extended Big5 range
                        ((0x40 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0x7E)
                                || (0x80 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0xFE))) {
                    bfchars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) - 0x81;
                    if (0x40 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0x7E) {
                        column = rawtext.byteAt(i + 1) - 0x40;
                    } else {
                        column = rawtext.byteAt(i + 1) - 0x40;
                    }
                    // System.out.println("extended row " + row + " column " + column + "
                    // rawtext.get(i) " + rawtext.get(i));
                    if (Big5P_[row][column] != 0) {
                        bffreq += Big5P_[row][column];
                    }
                }
                i++;
            }
        }
        rangeval = 50 * ((float) bfchars / (float) dbchars);
        freqval = 50 * ((float) bffreq / (float) totalfreq);
        // For regular Big5 files, this would give the same score, so I handicap it
        // slightly
        return (int) (rangeval + freqval) - 1;
    }

    /*
     * Function: euc_tw_probability Argument: byte array Returns : number from 0 to
     * 100 representing probability text in array uses EUC-TW (CNS 11643) encoding
     */
    int euc_tw_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i, rawtextlen = 0;
        long dbchars = 1, cnschars = 1;
        long cnsfreq = 0, totalfreq = 1;
        float rangeval = 0, freqval = 0;
        int row, column;
        // Check to see if characters fit into acceptable ranges
        // and have expected frequency of use
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen - 1; i++) {
            if (rawtext.byteAt(i) >= 0) { // in ASCII range
                // asciichars++;
            } else { // high bit set
                dbchars++;
                if (i + 3 < rawtextlen && (byte) 0x8E == rawtext.byteAt(i) && (byte) 0xA1 <= rawtext.byteAt(i + 1)
                        && rawtext.byteAt(i + 1) <= (byte) 0xB0 && (byte) 0xA1 <= rawtext.byteAt(i + 2)
                        && rawtext.byteAt(i + 2) <= (byte) 0xFE && (byte) 0xA1 <= rawtext.byteAt(i + 3)
                        && rawtext.byteAt(i + 3) <= (byte) 0xFE) { // Planes 1 - 16
                    cnschars++;
                    // System.out.println("plane 2 or above CNS char");
                    // These are all less frequent chars so just ignore freq
                    i += 3;
                } else if ((byte) 0xA1 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xFE && // Plane 1
                        (byte) 0xA1 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0xFE) {
                    cnschars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) + 256 - 0xA1;
                    column = rawtext.byteAt(i + 1) + 256 - 0xA1;
                    if (EUC_TW_[row][column] != 0) {
                        cnsfreq += EUC_TW_[row][column];
                    } else if (35 <= row && row <= 92) {
                        cnsfreq += 150;
                    }
                    i++;
                }
            }
        }
        rangeval = 50 * ((float) cnschars / (float) dbchars);
        freqval = 50 * ((float) cnsfreq / (float) totalfreq);
        return (int) (rangeval + freqval);
    }

    /*
     * Function: iso_2022_cn_probability Argument: byte array Returns : number from
     * 0 to 100 representing probability text in array uses ISO 2022-CN encoding
     * WORKS FOR BASIC CASES, BUT STILL NEEDS MORE WORK
     */
    int iso_2022_cn_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i, rawtextlen = 0;
        long dbchars = 1, isochars = 1;
        long isofreq = 0, totalfreq = 1;
        float rangeval = 0, freqval = 0;
        int row, column;
        // Check to see if characters fit into acceptable ranges
        // and have expected frequency of use
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen - 1; i++) {
            if (rawtext.byteAt(i) == (byte) 0x1B && i + 3 < rawtextlen) { // Escape char ESC
                if (rawtext.byteAt(i + 1) == (byte) 0x24 && rawtext.byteAt(i + 2) == 0x29
                        && rawtext.byteAt(i + 3) == (byte) 0x41) { // GB Escape $ ) A
                    i += 4;
                    while (rawtext.byteAt(i) != (byte) 0x1B) {
                        dbchars++;
                        if ((0x21 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= 0x77)
                                && (0x21 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= 0x77)) {
                            isochars++;
                            row = rawtext.byteAt(i) - 0x21;
                            column = rawtext.byteAt(i + 1) - 0x21;
                            totalfreq += 500;
                            if (GB_[row][column] != 0) {
                                isofreq += GB_[row][column];
                            } else if (15 <= row && row < 55) {
                                isofreq += 200;
                            }
                            i++;
                        }
                        i++;
                    }
                } else if (i + 3 < rawtextlen && rawtext.byteAt(i + 1) == (byte) 0x24
                        && rawtext.byteAt(i + 2) == (byte) 0x29 && rawtext.byteAt(i + 3) == (byte) 0x47) {
                    // CNS Escape $ ) G
                    i += 4;
                    while (rawtext.byteAt(i) != (byte) 0x1B) {
                        dbchars++;
                        if ((byte) 0x21 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0x7E
                                && (byte) 0x21 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0x7E) {
                            isochars++;
                            totalfreq += 500;
                            row = rawtext.byteAt(i) - 0x21;
                            column = rawtext.byteAt(i + 1) - 0x21;
                            if (EUC_TW_[row][column] != 0) {
                                isofreq += EUC_TW_[row][column];
                            } else if (35 <= row && row <= 92) {
                                isofreq += 150;
                            }
                            i++;
                        }
                        i++;
                    }
                }
                if (rawtext.byteAt(i) == (byte) 0x1B && i + 2 < rawtextlen && rawtext.byteAt(i + 1) == (byte) 0x28
                        && rawtext.byteAt(i + 2) == (byte) 0x42) { // ASCII:
                    // ESC
                    // ( B
                    i += 2;
                }
            }
        }
        rangeval = 50 * ((float) isochars / (float) dbchars);
        freqval = 50 * ((float) isofreq / (float) totalfreq);
        // System.out.println("isochars dbchars isofreq totalfreq " + isochars + " " +
        // dbchars + " " + isofreq + " " + totalfreq + "
        // " + rangeval + " " + freqval);
        return (int) (rangeval + freqval);
        // return 0;
    }

    /*
     * Function: utf8_probability Argument: byte array Returns : number from 0 to
     * 100 representing probability text in array uses UTF-8 encoding of Unicode
     */
    int utf8_probability(IIOBigSequenceByte rawtext) throws IOException {
        int score = 0;
        long i, rawtextlen = 0;
        long goodbytes = 0, asciibytes = 0;
        // Maybe also use UTF8 Byte Order Mark: EF BB BF
        // Check to see if characters fit into acceptable ranges
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen; i++) {
            if ((rawtext.byteAt(i) & (byte) 0x7F) == rawtext.byteAt(i)) { // One byte
                asciibytes++;
                // Ignore ASCII, can throw off count
            } else if (-64 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= -33 && // Two bytes
                    i + 1 < rawtextlen && -128 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= -65) {
                goodbytes += 2;
                i++;
            } else if (-32 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= -17 && // Three bytes
                    i + 2 < rawtextlen && -128 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= -65
                    && -128 <= rawtext.byteAt(i + 2) && rawtext.byteAt(i + 2) <= -65) {
                goodbytes += 3;
                i += 2;
            }
        }
        if (asciibytes == rawtextlen) {
            return 0;
        }
        score = (int) (100 * ((float) goodbytes / (float) (rawtextlen - asciibytes)));
        // System.out.println("rawtextlen " + rawtextlen + " goodbytes " + goodbytes + "
        // asciibytes " + asciibytes + " score " +
        // score);
        // If not above 98, reduce to zero to prevent coincidental matches
        // Allows for some (few) bad formed sequences
        if (score > 98) {
            return score;
        } else if (score > 95 && goodbytes > 30) {
            return score;
        } else {
            return 0;
        }
    }

    /*
     * Function: utf16_probability Argument: byte array Returns : number from 0 to
     * 100 representing probability text in array uses UTF-16 encoding of Unicode,
     * guess based on BOM // NOT VERY GENERAL, NEEDS MUCH MORE WORK
     */
    int utf16_probability(IIOBigSequenceByte rawtext) throws IOException {
        if (rawtext.length() > 1 && ((byte) 0xFE == rawtext.byteAt(0) && (byte) 0xFF == rawtext.byteAt(1)) || // Big-endian
                ((byte) 0xFF == rawtext.byteAt(0) && (byte) 0xFE == rawtext.byteAt(1))) { // Little-endian
            return 100;
        }
        return 0;
        /*
         * // Check to see if characters fit into acceptable ranges rawtextlen =
         * rawtext.length; for (i = 0; i < rawtextlen; i++) { if ((rawtext.get(i) &
         * (byte)0x7F) == rawtext.get(i)) { // One byte goodbytes += 1; asciibytes++; }
         * else if ((rawtext.get(i) & (byte)0xDF) == rawtext.get(i)) { // Two bytes if
         * (i+1 < rawtextlen && (rawtext.get(i+1) & (byte)0xBF) == rawtext.get(i+1)) {
         * goodbytes += 2; i++; } } else if ((rawtext.get(i) & (byte)0xEF) ==
         * rawtext.get(i)) { // Three bytes if (i+2 < rawtextlen && (rawtext.get(i+1) &
         * (byte)0xBF) == rawtext.get(i+1) && (rawtext.get(i+2) & (byte)0xBF) ==
         * rawtext.get(i+2)) { goodbytes += 3; i+=2; } } }
         *
         * score = (int)(100 * ((float)goodbytes/(float)rawtext.length)); // An all
         * ASCII file is also a good UTF8 file, but I'd rather it // get identified as
         * ASCII. Can delete following 3 lines otherwise if (goodbytes == asciibytes) {
         * score = 0; } // If not above 90, reduce to zero to prevent coincidental
         * matches if (score > 90) { return score; } else { return 0; }
         */
    }

    /*
     * Function: ascii_probability Argument: byte array Returns : number from 0 to
     * 100 representing probability text in array uses all ASCII Description: Sees
     * if array has any characters not in ASCII range, if so, score is reduced
     */
    int ascii_probability(IIOBigSequenceByte rawtext) throws IOException {
        int score = 75;
        long i, rawtextlen;
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen; i++) {
            if (rawtext.byteAt(i) < 0) {
                score = score - 5;
            } else if (rawtext.byteAt(i) == (byte) 0x1B) { // ESC (used by ISO 2022)
                score = score - 5;
            }
            if (score <= 0) {
                return 0;
            }
        }
        return score;
    }

    /*
     * Function: euc_kr__probability Argument: pointer to byte array Returns :
     * number from 0 to 100 representing probability text in array uses EUC-KR
     * encoding
     */
    int euc_kr_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i, rawtextlen = 0;
        long dbchars = 1, krchars = 1;
        long krfreq = 0, totalfreq = 1;
        float rangeval = 0, freqval = 0;
        int row, column;
        // Stage 1: Check to see if characters fit into acceptable ranges
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen - 1; i++) {
            // System.err.println(rawtext.get(i));
            if (rawtext.byteAt(i) >= 0) {
                // asciichars++;
            } else {
                dbchars++;
                if ((byte) 0xA1 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xFE
                        && (byte) 0xA1 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0xFE) {
                    krchars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) + 256 - 0xA1;
                    column = rawtext.byteAt(i + 1) + 256 - 0xA1;
                    if (KR_[row][column] != 0) {
                        krfreq += KR_[row][column];
                    } else if (15 <= row && row < 55) {
                        krfreq += 0;
                    }
                }
                i++;
            }
        }
        rangeval = 50 * ((float) krchars / (float) dbchars);
        freqval = 50 * ((float) krfreq / (float) totalfreq);
        return (int) (rangeval + freqval);
    }

    /*
     * Function: cp949__probability Argument: pointer to byte array Returns : number
     * from 0 to 100 representing probability text in array uses Cp949 encoding
     */
    int cp949_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i, rawtextlen = 0;
        long dbchars = 1, krchars = 1;
        long krfreq = 0, totalfreq = 1;
        float rangeval = 0, freqval = 0;
        int row, column;
        // Stage 1: Check to see if characters fit into acceptable ranges
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen - 1; i++) {
            // System.err.println(rawtext.get(i));
            if (rawtext.byteAt(i) >= 0) {
                // asciichars++;
            } else {
                dbchars++;
                if ((byte) 0x81 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xFE
                        && ((byte) 0x41 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0x5A
                        || (byte) 0x61 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0x7A
                        || (byte) 0x81 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0xFE)) {
                    krchars++;
                    totalfreq += 500;
                    if ((byte) 0xA1 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xFE
                            && (byte) 0xA1 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0xFE) {
                        row = rawtext.byteAt(i) + 256 - 0xA1;
                        column = rawtext.byteAt(i + 1) + 256 - 0xA1;
                        if (KR_[row][column] != 0) {
                            krfreq += KR_[row][column];
                        }
                    }
                }
                i++;
            }
        }
        rangeval = 50 * ((float) krchars / (float) dbchars);
        freqval = 50 * ((float) krfreq / (float) totalfreq);
        return (int) (rangeval + freqval);
    }

    int iso_2022_kr_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i;
        for (i = 0; i < rawtext.length(); i++) {
            if (i + 3 < rawtext.length() && rawtext.byteAt(i) == 0x1b && (char) rawtext.byteAt(i + 1) == '$'
                    && (char) rawtext.byteAt(i + 2) == ')' && (char) rawtext.byteAt(i + 3) == 'C') {
                return 100;
            }
        }
        return 0;
    }

    /*
     * Function: euc_jp_probability Argument: pointer to byte array Returns : number
     * from 0 to 100 representing probability text in array uses EUC-JP encoding
     */
    int euc_jp_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i, rawtextlen = 0;
        long dbchars = 1, jpchars = 1;
        long jpfreq = 0, totalfreq = 1;
        float rangeval = 0, freqval = 0;
        int row, column;
        // Stage 1: Check to see if characters fit into acceptable ranges
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen - 1; i++) {
            // System.err.println(rawtext.get(i));
            if (rawtext.byteAt(i) >= 0) {
                // asciichars++;
            } else {
                dbchars++;
                if ((byte) 0xA1 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xFE
                        && (byte) 0xA1 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0xFE) {
                    jpchars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) + 256 - 0xA1;
                    column = rawtext.byteAt(i + 1) + 256 - 0xA1;
                    if (JP_[row][column] != 0) {
                        jpfreq += JP_[row][column];
                    } else if (15 <= row && row < 55) {
                        jpfreq += 0;
                    }
                }
                i++;
            }
        }
        rangeval = 50 * ((float) jpchars / (float) dbchars);
        freqval = 50 * ((float) jpfreq / (float) totalfreq);
        return (int) (rangeval + freqval);
    }

    int iso_2022_jp_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i;
        for (i = 0; i < rawtext.length(); i++) {
            if (i + 2 < rawtext.length() && rawtext.byteAt(i) == 0x1b && (char) rawtext.byteAt(i + 1) == '$'
                    && (char) rawtext.byteAt(i + 2) == 'B') {
                return 100;
            }
        }
        return 0;
    }

    /*
     * Function: sjis_probability Argument: pointer to byte array Returns : number
     * from 0 to 100 representing probability text in array uses Shift-JIS encoding
     */
    int sjis_probability(IIOBigSequenceByte rawtext) throws IOException {
        long i, rawtextlen = 0;
        long dbchars = 1, jpchars = 1;
        long jpfreq = 0, totalfreq = 1;
        float rangeval = 0, freqval = 0;
        int row, column, adjust;
        // Stage 1: Check to see if characters fit into acceptable ranges
        rawtextlen = rawtext.length();
        for (i = 0; i < rawtextlen - 1; i++) {
            // System.err.println(rawtext.get(i));
            if (rawtext.byteAt(i) >= 0) {
                // asciichars++;
            } else {
                dbchars++;
                if (i + 1 < rawtext.length()
                        && (((byte) 0x81 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0x9F)
                        || ((byte) 0xE0 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xEF))
                        && (((byte) 0x40 <= rawtext.byteAt(i + 1) && rawtext.byteAt(i + 1) <= (byte) 0x7E)
                        || ((byte) 0x80 <= rawtext.byteAt(i + 1)
                        && rawtext.byteAt(i + 1) <= (byte) 0xFC))) {
                    jpchars++;
                    totalfreq += 500;
                    row = rawtext.byteAt(i) + 256;
                    column = rawtext.byteAt(i + 1) + 256;
                    if (column < 0x9f) {
                        adjust = 1;
                        if (column > 0x7f) {
                            column -= 0x20;
                        } else {
                            column -= 0x19;
                        }
                    } else {
                        adjust = 0;
                        column -= 0x7e;
                    }
                    if (row < 0xa0) {
                        row = ((row - 0x70) << 1) - adjust;
                    } else {
                        row = ((row - 0xb0) << 1) - adjust;
                    }
                    row -= 0x20;
                    column = 0x20;
                    // System.out.println("original row " + row + " column " + column);
                    if (row < JP_.length && column < JP_[row].length && JP_[row][column] != 0) {
                        jpfreq += JP_[row][column];
                    }
                    i++;
                } else if ((byte) 0xA1 <= rawtext.byteAt(i) && rawtext.byteAt(i) <= (byte) 0xDF) {
                    // half-width katakana, convert to full-width
                }
            }
        }
        rangeval = 50 * ((float) jpchars / (float) dbchars);
        freqval = 50 * ((float) jpfreq / (float) totalfreq);
        // For regular GB files, this would give the same score, so I handicap it
        // slightly
        return (int) (rangeval + freqval) - 1;
    }

}
