package com.liferay.mobile.screens.ddl.model;

import java.io.Serializable;

/**
 * @author Javier Gamarra
 */
public class DocumentLocalFile extends DocumentFile implements Serializable {

	public DocumentLocalFile(String name) {
		_name = name;
	}

	@Override
	public String toData() {
		return _name;
	}

	@Override
	public boolean isValid() {
		return _name != null && !_name.isEmpty();
	}

}