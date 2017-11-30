package dev.olog.presentation.dialog_add_playlist

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Html
import dev.olog.presentation.R
import dev.olog.presentation._base.BaseDialogFragment
import dev.olog.presentation.navigation.Navigator
import dev.olog.presentation.utils.makeDialog
import dev.olog.presentation.utils.withArguments
import dev.olog.shared.MediaIdHelper
import javax.inject.Inject

class AddPlaylistDialog : BaseDialogFragment() {

    companion object {
        const val TAG = "AddPlaylistDialog"
        const val ARGUMENTS_MEDIA_ID = "$TAG.arguments.media_id"
        const val ARGUMENTS_LIST_SIZE = "$TAG.arguments.list_size"
        const val ARGUMENTS_ITEM_TITLE = "$TAG.arguments.item_title"

        fun newInstance(mediaId: String, listSize: Int, itemTitle: String): AddPlaylistDialog {
            return AddPlaylistDialog().withArguments(
                    ARGUMENTS_MEDIA_ID to mediaId,
                    ARGUMENTS_LIST_SIZE to listSize,
                    ARGUMENTS_ITEM_TITLE to itemTitle
            )
        }
    }

    @Inject lateinit var presenter: AddPlaylistPresenter
    @Inject lateinit var navigator: Navigator
    @Inject lateinit var mediaId: String
    @Inject @JvmField var listSize: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
                .setTitle(Html.fromHtml(createMessage()))
                .setItems(createItems(), { _, which -> presenter.onItemClick(which) })
                .setPositiveButton(R.string.popup_new_playlist, { _, _ ->
                    navigator.toCreatePlaylistDialog(mediaId)
                })

        return builder.makeDialog()
    }

    private fun createMessage() : String {
        val itemTitle = arguments!!.getString(ARGUMENTS_ITEM_TITLE)
        if (MediaIdHelper.extractCategory(mediaId) == MediaIdHelper.MEDIA_ID_BY_ALL){
            return getString(R.string.add_song_x_to_playlist, itemTitle)
        }
        return context!!.resources.getQuantityString(R.plurals.add_xx_songs_to_playlist, listSize, listSize)
    }

    private fun createItems(): Array<out CharSequence> {
        val playlistsAsList = presenter.getPlaylistsAsList().map(DisplayablePlaylist::playlistTitle)
        if (playlistsAsList.isEmpty()){
            return arrayOf(context!!.getString(R.string.popup_no_playlist))
        }

        return playlistsAsList.toTypedArray()
    }

}