package fhuyakou.godot.app.android.gltfviewer

import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo

/**
 * Runtime [GodotPlugin] used to enable interaction with the Godot gdscript logic.
 */
class AppPlugin(godot: Godot) : GodotPlugin(godot) {

    companion object {
        val SHOW_GLTF_SIGNAL = SignalInfo("show_gltf", String::class.java)
        val DEBUG_WINDOW = SignalInfo("debug_window")
    }

    override fun getPluginName() = "AppPlugin"

    override fun getPluginSignals() = setOf(SHOW_GLTF_SIGNAL, DEBUG_WINDOW)

    /**
     * Used to emit a signal to the gdscript logic to update the gltf being shown.
     *
     * @param glbFilepath Filepath of the gltf asset to be shown
     */
    internal fun showGLTF(glbFilepath: String) {
        emitSignal(SHOW_GLTF_SIGNAL.name, glbFilepath)
    }

    internal fun debugWindow(){
        emitSignal(DEBUG_WINDOW.name)
    }
}
