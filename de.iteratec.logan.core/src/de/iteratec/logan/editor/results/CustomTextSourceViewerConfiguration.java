package de.iteratec.logan.editor.results;

import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.source.ISourceViewer;

import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class CustomTextSourceViewerConfiguration extends TextSourceViewerConfiguration {

	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		return new CustomTextDoubleClickStrategy();
	}
}
