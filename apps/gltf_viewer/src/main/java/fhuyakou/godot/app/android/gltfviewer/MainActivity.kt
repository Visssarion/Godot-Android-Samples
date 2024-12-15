package fhuyakou.godot.app.android.gltfviewer

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import org.godotengine.godot.Godot
import org.godotengine.godot.GodotFragment
import org.godotengine.godot.GodotHost
import org.godotengine.godot.plugin.GodotPlugin


/**
 * Implements the [GodotHost] interface so it can access functionality from the [Godot] instance.
 */
class MainActivity: AppCompatActivity(), GodotHost {

    private var godotFragment: GodotFragment? = null

    internal var appPlugin: AppPlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val selectFileButton = findViewById<Button>(R.id.btn_select_file)
        selectFileButton.setOnClickListener {
            openFileChooser()
        }
        val currentGodotFragment = supportFragmentManager.findFragmentById(R.id.godot_fragment_container)
        if (currentGodotFragment is GodotFragment) {
            godotFragment = currentGodotFragment
        } else {
            godotFragment = GodotFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.godot_fragment_container, godotFragment!!)
                .commitNowAllowingStateLoss()
        }

        initAppPluginIfNeeded(godot!!)

        var itemsSelectionFragment = supportFragmentManager.findFragmentById(R.id.item_selection_fragment_container)
        if (itemsSelectionFragment !is ItemsSelectionFragment) {
            itemsSelectionFragment = ItemsSelectionFragment.newInstance(1)
            supportFragmentManager.beginTransaction()
                .replace(R.id.item_selection_fragment_container, itemsSelectionFragment)
                .commitAllowingStateLoss()
        }

        checkAndRequestPermissions(this)

    }
    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*" // Используйте нужный MIME-тип
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        // Создаем chooser для лучшего UX
        val chooser = Intent.createChooser(intent, "Select a GLTF File")
        startActivityForResult(chooser, 42)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                // Преобразование Uri в строковый путь
                val gltfPath = uri.path
                if (gltfPath != null) {
                    appPlugin?.showGLTF(gltfPath)
                }
            }
        }
    }
    private fun initAppPluginIfNeeded(godot: Godot) {
        if (appPlugin == null) {
            appPlugin = AppPlugin(godot)
        }
    }

    override fun getActivity() = this

    override fun getGodot() = godotFragment?.godot

    override fun getHostPlugins(godot: Godot): Set<GodotPlugin> {
        initAppPluginIfNeeded(godot)

        return setOf(appPlugin!!)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Log.i("Test", "Nice")
            } else {
                Log.i("Test", "Bad")
            }
        }
    }

}

fun checkAndRequestPermissions(activity: Activity) {
    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
    val requestCode = 1

    val permissionsToRequest = permissions.filter {
        ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
    }

    if (permissionsToRequest.isNotEmpty()) {
        ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), requestCode)
    }
}
