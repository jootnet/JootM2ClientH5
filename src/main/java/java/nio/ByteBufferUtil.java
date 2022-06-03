package java.nio;

import com.google.gwt.typedarrays.shared.ArrayBuffer;

public final class ByteBufferUtil {
    public static ByteBuffer of(ArrayBuffer arrayBuffer) {
        return new DirectReadOnlyByteBuffer(arrayBuffer, arrayBuffer.byteLength(), 0);
    }
}
