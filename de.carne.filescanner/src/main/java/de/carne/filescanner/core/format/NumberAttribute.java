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
package de.carne.filescanner.core.format;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.carne.filescanner.core.FileScannerResultBuilder;

/**
 * This class defines Number based attributes of fixed size.
 *
 * @param <T> The attribute's type.
 */
public abstract class NumberAttribute<T extends Number> extends Attribute<T> {

	private final NumberAttributeType type;
	private T finalValue = null;

	/**
	 * Construct {@code NumberAttribute}.
	 *
	 * @param type The attribute's type.
	 * @param name The attribute's name.
	 */
	protected NumberAttribute(NumberAttributeType type, String name) {
		super(name);
		assert type != null;

		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * @see de.carne.filescanner.core.format.FormatSpec#matchSize()
	 */
	@Override
	public int matchSize() {
		return this.type.size();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.core.format.FormatSpec#matches(java.nio.ByteBuffer)
	 */
	@Override
	public boolean matches(ByteBuffer buffer) {
		assert buffer != null;

		T value = getValue(buffer);

		return (value != null && (this.finalValue == null || this.finalValue.equals(value)));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.core.format.FormatSpec#eval(de.carne.filescanner.
	 * core.FileScannerResultBuilder, long)
	 */
	@Override
	public long eval(FileScannerResultBuilder result, long position) throws IOException {
		int typeSize = this.type.size();

		if (isBound()) {
			ByteBuffer buffer = ensureSA(result.input().cachedRead(position, typeSize, result.order()), typeSize);

			bindValue(getValue(buffer));
		}
		return typeSize;
	}

	/**
	 * Make attribute final (with a specific value).
	 *
	 * @param finalValue The final value.
	 * @return The updated data attribute spec.
	 */
	public final NumberAttribute<T> setFinalValue(T finalValue) {
		this.finalValue = finalValue;
		return this;
	}

	/**
	 * Get the attribute's final value.
	 *
	 * @return The attribute's final value or {@code null} if the attribute is
	 *         not final.
	 */
	public final T getFinalValue() {
		return this.finalValue;
	}

}
