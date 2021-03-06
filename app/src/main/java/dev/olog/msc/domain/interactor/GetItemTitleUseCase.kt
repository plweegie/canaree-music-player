package dev.olog.msc.domain.interactor

import dev.olog.msc.domain.executors.IoScheduler
import dev.olog.msc.domain.interactor.base.ObservableUseCaseWithParam
import dev.olog.msc.domain.interactor.item.*
import dev.olog.msc.utils.MediaId
import dev.olog.msc.utils.MediaIdCategory
import io.reactivex.Observable
import javax.inject.Inject

class GetItemTitleUseCase @Inject constructor(
        schedulers: IoScheduler,
        private val getFolderUseCase: GetFolderUseCase,
        private val getPlaylistUseCase: GetPlaylistUseCase,
        private val getSongUseCase: GetSongUseCase,
        private val getAlbumUseCase: GetAlbumUseCase,
        private val getArtistUseCase: GetArtistUseCase,
        private val getGenreUseCase: GetGenreUseCase,
        private val getPodcastUseCase: GetPodcastUseCase,
        private val getPodcastPlaylistUseCase: GetPodcastPlaylistUseCase,
        private val getPodcastAlbumUseCase: GetPodcastAlbumUseCase,
        private val getPodcastArtistUseCase: GetPodcastArtistUseCase

) : ObservableUseCaseWithParam<String, MediaId>(schedulers) {


    override fun buildUseCaseObservable(param: MediaId): Observable<String> {
        return when (param.category){
            MediaIdCategory.FOLDERS -> getFolderUseCase.execute(param).map { it.title }
            MediaIdCategory.PLAYLISTS -> getPlaylistUseCase.execute(param).map { it.title }
            MediaIdCategory.SONGS -> getSongUseCase.execute(param).map { it.title }
            MediaIdCategory.ALBUMS -> getAlbumUseCase.execute(param).map { it.title }
            MediaIdCategory.ARTISTS -> getArtistUseCase.execute(param).map { it.name }
            MediaIdCategory.GENRES -> getGenreUseCase.execute(param).map { it.name }
            MediaIdCategory.PODCASTS_PLAYLIST -> getPodcastPlaylistUseCase.execute(param).map { it.title }
            MediaIdCategory.PODCASTS -> getPodcastUseCase.execute(param).map { it.title }
            MediaIdCategory.PODCASTS_ARTISTS -> getPodcastArtistUseCase.execute(param).map { it.name }
            MediaIdCategory.PODCASTS_ALBUMS -> getPodcastAlbumUseCase.execute(param).map { it.title }
            else -> throw IllegalArgumentException("invalid media category ${param.category}")
        }
    }
}