package com.meishe.myvideo.ui.Utils;

public class FormatUtils {
    private static final long DURATION_TEXT_REPAIR_VALUE = 10;
    private static final long MINUTES_60 = 60;
    private static final long SIX_MINUTES_3600 = 3600;
    private static final long TIME_SECOND = 1000000;

    public static String durationToText(long j) {
        double d = (double) j;
        Double.isNaN(d);
        double d2 = (d * 1.0d) / 1000000.0d;
        if (d2 >= 60.0d) {
            return splitLongMilliTime(j);
        }
        return String.format("%.1fs", Double.valueOf(d2));
    }

    private static String splitLongMilliTime(long j) {
        StringBuilder sb = new StringBuilder();
        long j2 = j / 1000000;
        long j3 = j2 / SIX_MINUTES_3600;
        long j4 = (j2 % SIX_MINUTES_3600) / MINUTES_60;
        long j5 = j2 % MINUTES_60;
        if (j3 != 0) {
            sb.append(j3);
            sb.append(":");
        }
        if (j4 != 0) {
            if (j4 < DURATION_TEXT_REPAIR_VALUE) {
                sb.append("0");
                sb.append(j4);
                sb.append(":");
            } else {
                sb.append(j4);
            }
            sb.append(":");
        }
        if (j5 < DURATION_TEXT_REPAIR_VALUE) {
            sb.append("0");
        }
        sb.append(j5);
        return sb.toString();
    }

    public static String formatTimeStrWithUs(long j) {
        double d = (double) j;
        Double.isNaN(d);
        int i = (int) (d / 1000000.0d);
        int i2 = i / 3600;
        int i3 = (i % 3600) / 60;
        int i4 = i % 60;
        if (i2 > 0) {
            return String.format("%02d:%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
        }
        return String.format("%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i4));
    }
}
