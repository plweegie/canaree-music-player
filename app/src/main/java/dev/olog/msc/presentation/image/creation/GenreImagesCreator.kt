package dev.olog.msc.presentation.image.creation

import android.content.Context
import android.provider.MediaStore
import dev.olog.msc.dagger.qualifier.ApplicationContext
import dev.olog.msc.data.repository.CommonQuery
import dev.olog.msc.domain.entity.Genre
import dev.olog.msc.domain.interactor.tab.GetAllGenresUseCase
import dev.olog.msc.utils.assertBackgroundThread
import dev.olog.msc.utils.img.ImagesFolderUtils
import dev.olog.msc.utils.img.MergedImagesCreator
import io.reactivex.Maybe
import javax.inject.Inject

private val MEDIA_STORE_URI = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI

class GenreImagesCreator @Inject constructor(
        @ApplicationContext private val ctx: Context,
        private val getAllGenresUseCase: GetAllGenresUseCase
) {


    fun execute() : Maybe<*> {
        return getAllGenresUseCase.execute()
                .firstOrError()
                .flattenAsObservable { it }
                .map {
                    val uri = MediaStore.Audio.Genres.Members.getContentUri("external", it.id)
                    Pair(it, CommonQuery.extractAlbumIdsFromSongs(ctx.contentResolver, uri))
                }
                .map { (genre, albumsId) -> try {
                    makeImage(genre, albumsId)
                } catch (ex: Exception){ false }
                }
                .reduce { acc: Boolean, curr: Boolean -> acc || curr }
                .filter { it }
                .doOnSuccess {
                    ctx.contentResolver.notifyChange(MEDIA_STORE_URI, null)
                }
    }

    private fun makeImage(genre: Genre, albumsId: List<Long>) : Boolean {
        assertBackgroundThread()
        val folderName = ImagesFolderUtils.getFolderName(ImagesFolderUtils.GENRE)
        return MergedImagesCreator.makeImages2(ctx, albumsId, folderName, "${genre.id}")
    }

}