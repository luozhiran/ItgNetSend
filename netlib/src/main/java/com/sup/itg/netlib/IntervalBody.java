package com.sup.itg.netlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public class IntervalBody extends RequestBody {
    private static final MediaType CONTENT_TYPE = MediaType.get("application/octet-stream");

    private File mFile;
    private long mOffset = 0;

    IntervalBody(File file, long offset) {
        mFile = file;
        mOffset = offset;
    }

    @Override
    public MediaType contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length() - mOffset;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(mFile);
            BufferedSource bufferedSource = Okio.buffer(source);
            byte[] buffer = new byte[4096];
            int len = 0;
            int totleSize = 0;
            while ((len = bufferedSource.read(buffer)) != -1) {
                if (totleSize == mOffset) {
                    sink.write(buffer, 0, len);
                } else {
                    totleSize += len;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            source.close();
        }
    }

    public static final class Builder {
        private File file;
        private long offset = 0;

        public Builder addFile(File file) {
            this.file = file;
            return this;
        }

        public Builder addFileOffset(long offset) {
            this.offset = offset;
            return this;
        }

        public IntervalBody build() {
            return new IntervalBody(file, offset);
        }

    }
}
