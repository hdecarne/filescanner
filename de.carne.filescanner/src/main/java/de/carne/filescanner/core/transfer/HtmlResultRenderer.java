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
package de.carne.filescanner.core.transfer;

import java.io.IOException;

import de.carne.filescanner.spi.FileScannerResultRenderer;
import de.carne.filescanner.util.Hexadecimal;

/**
 * Base class for all {@code FileScannerResultRenderer} implementations that
 * generate HTML output.
 */
abstract class HtmlResultRenderer extends FileScannerResultRenderer {

	private final HtmlResultRendererURLHandler urlHandler;

	public HtmlResultRenderer(HtmlResultRendererURLHandler urlHandler) {
		this.urlHandler = urlHandler;
	}

	/*
	 * (non-Javadoc)
	 * @see de.carne.filescanner.spi.FileScannerResultRenderer#writePreamble()
	 */
	@Override
	protected void writePreamble() throws IOException, InterruptedException {
		write("<!DOCTYPE HTML>\n<html>\n<head>\n<meta charset=\"utf-8\">\n</head>\n<body>\n");
	}

	/*
	 * (non-Javadoc)
	 * @see de.carne.filescanner.spi.FileScannerResultRenderer#writeEpilogue()
	 */
	@Override
	protected void writeEpilogue() throws IOException, InterruptedException {
		write("</body>\n</html>\n");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.spi.FileScannerResultRenderer#writeBeginMode(de.
	 * carne.filescanner.spi.FileScannerResultRenderer.Mode)
	 */
	@Override
	protected void writeBeginMode(Mode mode) throws IOException, InterruptedException {
		write("<span class=\"", mode.name().toLowerCase(), "\">");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.spi.FileScannerResultRenderer#writeEndMode(de.carne.
	 * filescanner.spi.FileScannerResultRenderer.Mode)
	 */
	@Override
	protected void writeEndMode(Mode mode) throws IOException, InterruptedException {
		write("</span>");
	}

	/*
	 * (non-Javadoc)
	 * @see de.carne.filescanner.spi.FileScannerResultRenderer#writeBreak()
	 */
	@Override
	protected void writeBreak() throws IOException, InterruptedException {
		write("<br/>\n");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.spi.FileScannerResultRenderer#writeText(de.carne.
	 * filescanner.spi.FileScannerResultRenderer.Mode, java.lang.String)
	 */
	@Override
	protected void writeText(Mode mode, String text) throws IOException, InterruptedException {
		write(text);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.spi.FileScannerResultRenderer#writeRefText(de.carne.
	 * filescanner.spi.FileScannerResultRenderer.Mode, java.lang.String, long)
	 */
	@Override
	protected void writeRefText(Mode mode, String text, long position) throws IOException, InterruptedException {
		write("<a href=\"#", Hexadecimal.formatL(position), "\">", text, "</a>");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.spi.FileScannerResultRenderer#writeImage(de.carne.
	 * filescanner.spi.FileScannerResultRenderer.Mode,
	 * de.carne.filescanner.spi.FileScannerResultRenderer.StreamHandler)
	 */
	@Override
	protected void writeImage(Mode mode, StreamHandler streamHandler) throws IOException, InterruptedException {
		write("<img src=\"", this.urlHandler.openStream(streamHandler).toExternalForm(), "\"/>");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.spi.FileScannerResultRenderer#writeRefImage(de.carne
	 * .filescanner.spi.FileScannerResultRenderer.Mode,
	 * de.carne.filescanner.spi.FileScannerResultRenderer.StreamHandler, long)
	 */
	@Override
	protected void writeRefImage(Mode mode, StreamHandler streamHandler, long position)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.writeRefImage(mode, streamHandler, position);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.spi.FileScannerResultRenderer#writeVideo(de.carne.
	 * filescanner.spi.FileScannerResultRenderer.Mode,
	 * de.carne.filescanner.spi.FileScannerResultRenderer.StreamHandler)
	 */
	@Override
	protected void writeVideo(Mode mode, StreamHandler streamHandler) throws IOException, InterruptedException {
		write("<video src=\"", this.urlHandler.openStream(streamHandler).toExternalForm(), "\"/>");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.carne.filescanner.spi.FileScannerResultRenderer#writeRefVideo(de.carne
	 * .filescanner.spi.FileScannerResultRenderer.Mode,
	 * de.carne.filescanner.spi.FileScannerResultRenderer.StreamHandler, long)
	 */
	@Override
	protected void writeRefVideo(Mode mode, StreamHandler streamHandler, long position)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.writeRefVideo(mode, streamHandler, position);
	}

	protected abstract void write(String... artefacts) throws IOException, InterruptedException;

}
