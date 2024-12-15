import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore

fun getAbsolutePath(context: Context, uri: Uri): String? {
    val documentId = DocumentsContract.getDocumentId(uri)
    val id = documentId.split(":")[1]
    val column = arrayOf(MediaStore.Files.FileColumns.DATA)
    val selection = "${MediaStore.Files.FileColumns._ID}=?"
    val selectionArgs = arrayOf(id)

    val cursor: Cursor? = context.contentResolver.query(
        MediaStore.Files.getContentUri("external"),
        column,
        selection,
        selectionArgs,
        null
    )

    var filePath: String? = null
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(column[0])
            filePath = it.getString(columnIndex)
        }
    }
    return filePath
}
