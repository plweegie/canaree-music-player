package dev.olog.presentation.dialog_add_favorite

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Html
import dev.olog.presentation.R
import dev.olog.presentation._base.BaseDialogFragment
import dev.olog.presentation.utils.makeDialog
import dev.olog.presentation.utils.withArguments
import dev.olog.shared.MediaIdHelper
import javax.inject.Inject

class AddFavoriteDialog : BaseDialogFragment() {

    companion object {
        const val TAG = "AddFavoriteDialog"
        const val ARGUMENTS_MEDIA_ID = "$TAG.arguments.media_id"
        const val ARGUMENTS_LIST_SIZE = "$TAG.arguments.list_size"
        const val ARGUMENTS_ITEM_TITLE = "$TAG.arguments.item_title"

        fun newInstance(mediaId: String, listSize: Int, itemTitle: String): AddFavoriteDialog {
            return AddFavoriteDialog().withArguments(
                    ARGUMENTS_MEDIA_ID to mediaId,
                    ARGUMENTS_LIST_SIZE to listSize,
                    ARGUMENTS_ITEM_TITLE to itemTitle
            )
        }
    }

    @Inject lateinit var mediaId: String
    @Inject @JvmField var listSize: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
                .setTitle(R.string.popup_add_to_favorites)
                .setMessage(Html.fromHtml(createMessage()))
                .setNegativeButton(R.string.popup_negative_cancel, null)
                .setPositiveButton(R.string.popup_positive_ok, { dialog, button ->
                    // todo
                })


        return builder.makeDialog()
    }

    private fun createMessage() : String {
        val itemTitle = arguments!!.getString(ARGUMENTS_ITEM_TITLE)
        if (MediaIdHelper.extractCategory(mediaId) == MediaIdHelper.MEDIA_ID_BY_ALL){
            return getString(R.string.add_song_x_to_favorite, itemTitle)
        }

        return context!!.resources.getQuantityString(
                R.plurals.add_xx_songs_to_favorite, listSize, listSize)
    }

}