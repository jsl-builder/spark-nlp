//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.tensorflow.internal.buffer;

import java.io.Serializable;
import java.nio.ReadOnlyBufferException;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerScope;
import org.tensorflow.internal.c_api.TF_TString;
import org.tensorflow.internal.c_api.global.tensorflow;
import org.tensorflow.ndarray.buffer.DataBuffer;
import org.tensorflow.ndarray.impl.buffer.AbstractDataBuffer;
import org.tensorflow.ndarray.impl.buffer.Validator;

public class ByteSequenceTensorBuffer extends AbstractDataBuffer<byte[]> implements Serializable {
    private final TF_TString data;

    public static <T> long computeSize(ByteSequenceProvider<?> byteSequenceProvider) {
        return byteSequenceProvider.numSequences() * (long)Loader.sizeof(TF_TString.class);
    }

    public <T> void init(ByteSequenceProvider<T> byteSequenceProvider) {
        ByteSequenceTensorBuffer.InitDataWriter writer = new ByteSequenceTensorBuffer.InitDataWriter();
        byteSequenceProvider.forEach(writer::writeNext);
    }

    public long size() {
        return this.data.capacity() - this.data.position();
    }

    public byte[] getObject(long index) {
        Validator.getArgs(this, index);
        TF_TString tstring = this.data.getPointer(index);
        BytePointer ptr = tensorflow.TF_TString_GetDataPointer(tstring).capacity(tensorflow.TF_TString_GetSize(tstring));
        return ptr.getStringBytes();
    }

    public DataBuffer<byte[]> setObject(byte[] values, long index) {
        throw new ReadOnlyBufferException();
    }

    public boolean isReadOnly() {
        return true;
    }

    public DataBuffer<byte[]> copyTo(DataBuffer<byte[]> dst, long size) {
        if (size == this.size() && dst instanceof ByteSequenceTensorBuffer) {
            ByteSequenceTensorBuffer tensorDst = (ByteSequenceTensorBuffer)dst;

            for(int i = 0; (long)i < size; ++i) {
                tensorflow.TF_TString_Assign(tensorDst.data.getPointer((long)i), this.data.getPointer((long)i));
            }
        } else {
            this.slowCopyTo(dst, size);
        }

        return this;
    }

    public DataBuffer<byte[]> slice(long index, long size) {
        return new ByteSequenceTensorBuffer(this.data.getPointer(index), size);
    }

    ByteSequenceTensorBuffer(Pointer tensorMemory, long numElements) {
        this.data = (TF_TString)(new TF_TString(tensorMemory)).capacity(tensorMemory.position() + numElements);
    }

    private class InitDataWriter {
        long index;

        private InitDataWriter() {
            this.index = 0L;
        }

        void writeNext(byte[] bytes) {
            PointerScope scope = new PointerScope(new Class[0]);
            Throwable var3 = null;

            try {
                TF_TString tstring = ByteSequenceTensorBuffer.this.data.getPointer((long)(this.index++));
                tensorflow.TF_TString_Copy(tstring, new BytePointer(bytes), (long)bytes.length);
            } catch (Throwable var12) {
                var3 = var12;
                throw var12;
            } finally {
                if (scope != null) {
                    if (var3 != null) {
                        try {
                            scope.close();
                        } catch (Throwable var11) {
                            var3.addSuppressed(var11);
                        }
                    } else {
                        scope.close();
                    }
                }

            }

        }
    }
}
