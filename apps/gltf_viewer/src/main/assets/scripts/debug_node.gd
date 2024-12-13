extends Node

func _ready() -> void:
	#OS.request_permission("")
	printraw("READ")
	print(OS.request_permission("android.permission.READ_EXTERNAL_STORAGE"))
	print(OS.request_permission("android.permission.WRITE_EXTERNAL_STORAGE"))
	print(OS.request_permission("android.permission.MANAGE_EXTERNAL_STORAGE"))
	print(OS.get_granted_permissions())

func _input(event: InputEvent) -> void:
	#print(event)
	pass
