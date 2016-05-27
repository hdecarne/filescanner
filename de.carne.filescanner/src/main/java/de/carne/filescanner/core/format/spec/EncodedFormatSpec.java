/*
 * Copyright (c) 2007-2016 Holger de Carne and contributors, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.carne.filescanner.core.format.spec;

import java.io.IOException;
import java.util.function.Supplier;

import de.carne.filescanner.core.FileScannerResult;
import de.carne.filescanner.core.FileScannerResultBuilder;
import de.carne.filescanner.core.FileScannerResultType;
import de.carne.filescanner.core.input.DecodeParams;
import de.carne.filescanner.spi.FileScannerInput;
import de.carne.filescanner.spi.FileScannerResultRenderer;
import de.carne.nio.compression.spi.Decoder;
import de.carne.util.logging.Log;

/**
 * Encoded format spec defining a section of encoded data.
 */
public class EncodedFormatSpec extends FormatSpec implements Supplier<String> {

	private static final Log LOG = new Log(EncodedFormatSpec.class);

	private final ValueExpression<DecodeParams> decodeParamsExpression;

	private EncodedFormatSpec(ValueExpression<DecodeParams> factoryExpression) {
		this.decodeParamsExpression = factoryExpression;
		setResult(this);
	}

	/**
	 * Construct {@code EncodedFormatSpec}.
	 *
	 * @param decodeParams The decode parameters to use for decoding.
	 */
	public EncodedFormatSpec(DecodeParams decodeParams) {
		this(new ValueExpression<>(decodeParams));
	}

	/**
	 * Construct {@code EncodedFormatSpec}.
	 *
	 * @param decodeParamsLambda The expression providing the decode parameters
	 *        to use for decoding.
	 */
	public EncodedFormatSpec(Supplier<DecodeParams> decodeParamsLambda) {
		this(new ValueExpression<>(decodeParamsLambda));
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.function.Supplier#get()
	 */
	@Override
	public String get() {
		DecodeParams decodeParams = this.decodeParamsExpression.decode();

		return (decodeParams != null ? decodeParams.getEncodedName() : "");
	}

	/*
	 * (non-Javadoc)
	 * @see de.carne.filescanner.core.format.spec.FormatSpec#resultType()
	 */
	@Override
	public FileScannerResultType resultType() {
		return FileScannerResultType.ENCODED_INPUT;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.core.format.spec.FormatSpec#specDecode(de.carne.
	 * filescanner.core.FileScannerResultBuilder, long)
	 */
	@SuppressWarnings("resource")
	@Override
	public long specDecode(FileScannerResultBuilder result, long position) throws IOException {
		DecodeParams decodeParams = this.decodeParamsExpression.decode();
		long decoded = 0L;

		if (decodeParams != null) {
			Decoder decoder = decodeParams.newDecoder();
			long encodedSize = decodeParams.getEncodedSize();
			FileScannerInput decodedInput;

			if (decoder != null) {
				decodedInput = result.input().scanner().decodeCache().decodeInput(result.input(), position, decoder,
						decodeParams.getDecodedPath());
				decoded = Math.max(decoder.totalIn(), encodedSize);
			} else {
				decoded = Math.max(0L, encodedSize);
				decodedInput = result.input().slice(position, position + decoded, decodeParams.getDecodedPath());
			}
			if (encodedSize >= 0 && decoded > encodedSize) {
				LOG.warning(null, "Decoding exceeded the specified encoded size; {0} addional bytes read",
						decoded - encodedSize);
			}
			result.addInput(decodedInput);
		}
		return decoded;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.core.format.spec.FormatSpec#specRender(de.carne.
	 * filescanner.core.FileScannerResult, long,
	 * de.carne.filescanner.spi.FileScannerResultRenderer)
	 */
	@Override
	public long specRender(FileScannerResult result, long position, FileScannerResultRenderer renderer)
			throws IOException, InterruptedException {
		DecodeParams decodeParams = this.decodeParamsExpression.decode();

		if (decodeParams != null) {
			decodeParams.render(renderer);
		}
		return result.size();
	}

}