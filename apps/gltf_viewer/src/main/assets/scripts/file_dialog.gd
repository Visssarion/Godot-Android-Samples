extends FileDialog



func _on_file_selected(path: String) -> void:
	print(path)
	print(ProjectSettings.globalize_path(path))
	print(FileAccess.file_exists(path))
	$".."._load_gltf(path)
