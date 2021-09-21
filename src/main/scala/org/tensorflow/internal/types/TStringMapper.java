package org.tensorflow.internal.types;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.tensorflow.RawTensor;
import org.tensorflow.TensorMapper;
import org.tensorflow.internal.buffer.ByteSequenceProvider;
import org.tensorflow.internal.buffer.ByteSequenceTensorBuffer;
import org.tensorflow.internal.buffer.TensorBuffers;
import org.tensorflow.ndarray.NdArray;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.buffer.DataBuffer;
import org.tensorflow.ndarray.buffer.layout.DataLayout;
import org.tensorflow.ndarray.buffer.layout.DataLayouts;
import org.tensorflow.ndarray.impl.dense.DenseNdArray;
import org.tensorflow.types.TString;

public final class TStringMapper extends TensorMapper<TString> {
    private static final DataLayout<DataBuffer<byte[]>, String> UTF_8_LAYOUT;

    public TStringMapper() {
    }

    protected TString mapDense(RawTensor tensor) {
        ByteSequenceTensorBuffer buffer = TensorBuffers.toStrings(nativeHandle(tensor), tensor.shape().size());
        return new TStringMapper.DenseTString(tensor, buffer, UTF_8_LAYOUT);
    }

    static {
        UTF_8_LAYOUT = DataLayouts.ofStrings(StandardCharsets.UTF_8);
    }

    private static final class DenseTString extends DenseNdArray<String> implements TStringMapper.TStringInternal, Serializable {
        final RawTensor rawTensor;
        final ByteSequenceTensorBuffer buffer;

        public <T> void init(ByteSequenceProvider<T> byteSequenceProvider) {
            this.buffer.init(byteSequenceProvider);
        }

        public TString using(Charset charset) {
            return new TStringMapper.DenseTString(this.rawTensor, this.buffer, DataLayouts.ofStrings(charset));
        }

        public NdArray<byte[]> asBytes() {
            return NdArrays.wrap(this.shape(), this.buffer);
        }

        public Class<TString> type() {
            return TString.class;
        }

        public RawTensor asRawTensor() {
            return this.rawTensor;
        }

        DenseTString(RawTensor rawTensor, ByteSequenceTensorBuffer buffer, DataLayout<DataBuffer<byte[]>, String> layout) {
            super(layout.applyTo(buffer), rawTensor.shape());
            this.rawTensor = rawTensor;
            this.buffer = buffer;
        }
    }

    interface TStringInternal extends TString {
        <T> void init(ByteSequenceProvider<T> var1);
    }
}
