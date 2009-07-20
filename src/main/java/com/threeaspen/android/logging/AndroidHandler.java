package com.threeaspen.android.logging;

import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class AndroidHandler extends Handler {
    private static final Formatter THE_FORMATTER = new Formatter() {
        @Override
        public String format(LogRecord r) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            sw.write(r.getLoggerName());
            sw.write(": ");
            sw.write(formatMessage(r));
            Throwable thrown = r.getThrown();
            if (thrown != null) {
                sw.write("\n");
                thrown.printStackTrace(pw);
                pw.flush();
            }
            return sw.toString();
        }
    };
    private String tag;

    public AndroidHandler(String tag) {
        this.tag = tag;
        if (this.tag == null) this.tag = "";
        setFormatter(THE_FORMATTER);
    }

    public AndroidHandler() {
        this(null);
    }

    @Override
    public void close() {
        // No need to close, but must implement abstract method.
    }

    @Override
    public void flush() {
        // No need to flush, but must implement abstract method.
    }

    @Override
    public void publish(LogRecord record) {
        try {
            int level = getAndroidLevel(record.getLevel());
            /*StringBuilder tag = new StringBuilder();
            if (record.getLoggerName() != null)
                tag.append(record.getLoggerName());
            int plen = this.tag.length();
            if (tag.toString().startsWith(this.tag + ".")) {
                tag.setCharAt(plen, ':');
            } else {
                tag.insert(0, ':');
                tag.insert(0, this.tag);
            }

            // Tags must be <= 23 characters.
            int length = tag.length();
            if (length > 23) {
                // Most loggers use the full class name. Try dropping the
                // package.
                int lastPeriod = tag.lastIndexOf(".");
                if (length - lastPeriod - 1 <= 23) {
                    tag.delete(plen+1, lastPeriod);
                } else {
                    // Use last 23 chars.
                    tag.delete(plen+1, length - 23 - plen - 1);
                }
            }*/

            /*
             This doesn't work
             if (!Log.isLoggable(tag, level)) {
                return;
            }*/

            String message = getFormatter().format(record);
            Log.println(level, tag, message);
        } catch (RuntimeException e) {
            Log.e("AndroidHandler", "Error logging message.", e);
        }
    }

    static int getAndroidLevel(Level level) {
        int value = level.intValue();
        if (value >= 1000) { // SEVERE
            return Log.ERROR;
        } else if (value >= 900) { // WARNING
            return Log.WARN;
        } else if (value >= 800) { // INFO
            return Log.INFO;
        } else {
            return Log.DEBUG;
        }
    }

}
