package com.jum.common.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @ClassName IOUtil
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/9/4
 * @Version 1.0
 */
public class IOUtil {

    private static final int BUFFER_SIZE = 1024 * 8;

    /**
     * read string.
     *
     * @param reader Reader instance.
     * @return String.
     * @throws IOException
     */
    public static String read(Reader reader) throws IOException
    {
        try(StringWriter writer = new StringWriter())
        {
            write(reader, writer);
            return writer.getBuffer().toString();
        }
    }

    /**
     * write.
     *
     * @param reader Reader.
     * @param writer Writer.
     * @return count.
     * @throws IOException
     */
    public static long write(Reader reader, Writer writer) throws IOException
    {
        return write(reader, writer, BUFFER_SIZE);
    }

    /**
     * write.
     *
     * @param reader Reader.
     * @param writer Writer.
     * @param bufferSize buffer size.
     * @return count.
     * @throws IOException
     */
    public static long write(Reader reader, Writer writer, int bufferSize) throws IOException
    {
        int read;
        long total = 0;
        char[] buf = new char[BUFFER_SIZE];
        while( ( read = reader.read(buf) ) != -1 )
        {
            writer.write(buf, 0, read);
            total += read;
        }
        return total;
    }





}
