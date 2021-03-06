package com.liferay.mobile.screens.viewsets.defaultviews.ddl.form.fields;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.liferay.mobile.screens.viewsets.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectFileDialog {

	private String currentFile = "";
	private String currentDir = "";

	private static final String SD_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();

	public interface SimpleFileDialogListener {
		public void onFileChosen(String path);
	}

	public AlertDialog createDialog(final Context context, final SimpleFileDialogListener listener) {
		currentDir = calculateDefaultPath();

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.select_file_default, null);
		dialogBuilder.setView(view);

		dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface d, int arg1) {
				d.cancel();
			}
		});

		final EditText editText = (EditText) view.findViewById(R.id.default_dialog_edit_text);
		dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface d, int arg1) {
				currentFile = editText.getText().toString();
				listener.onFileChosen(currentDir + "/" + currentFile);
			}
		});

		final List<String> files = getFileEntries(currentDir);
		final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, android.R.id.text1, files);
		ListView listView = (ListView) view.findViewById(R.id.default_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(createListener(files, editText, adapter));

		return dialogBuilder.create();
	}

	private AdapterView.OnItemClickListener createListener(final List<String> files, final EditText editText, final ArrayAdapter<String> adapter) {
		return new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String selection = files.get(position);

				String newPath = currentDir;
				if ("..".equals(selection)) {
					newPath = currentDir.substring(0, currentDir.lastIndexOf("/"));
				}
				else if (selection.indexOf('/') != -1) {
					newPath += "/" + selection.substring(0, selection.length() - 1);
				}
				else {
					newPath += "/" + selection;
				}

				currentFile = "";
				if (new File(newPath).isFile()) {
					currentFile = selection;
				}
				else {
					currentDir = newPath;
				}

				files.clear();
				files.addAll(getFileEntries(currentDir));
				adapter.notifyDataSetChanged();
				editText.setText(currentFile);
			}
		};
	}

	private String calculateDefaultPath() {
		final String defaultPath = checkIfDirExists("");
		final String sdPath = checkIfDirExists(SD_DIRECTORY);
		if (sdPath != null) {
			return sdPath;
		}
		else if (defaultPath != null) {
			return defaultPath;
		}
		return "";
	}

	public static String checkIfDirExists(String directory) {
		try {
			File file = new File(directory);
			return file.exists() && file.isDirectory() ? file.getCanonicalPath() : null;
		}
		catch (IOException e) {
			return null;
		}
	}

	private List<String> getFileEntries(String directory) {
		List<String> entries = new ArrayList<>();

		if (!currentDir.equals(SD_DIRECTORY)) {
			entries.add("..");
		}

		File dirFile = new File(directory);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return entries;
		}

		for (File file : dirFile.listFiles()) {
			if (file.isDirectory()) {
				entries.add(file.getName() + "/");
			}
			else {
				entries.add(file.getName());
			}
		}

		Collections.sort(entries, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		return entries;
	}
}