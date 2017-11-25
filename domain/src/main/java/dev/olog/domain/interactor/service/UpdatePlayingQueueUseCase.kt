package dev.olog.domain.interactor.service

import dev.olog.domain.executor.IoScheduler
import dev.olog.domain.gateway.PlayingQueueGateway
import dev.olog.domain.interactor.base.CompletableUseCaseWithParam
import io.reactivex.Completable
import javax.inject.Inject

class UpdatePlayingQueueUseCase @Inject constructor(
        schedulers: IoScheduler,
        private val gateway: PlayingQueueGateway

) : CompletableUseCaseWithParam<List<Long>>(schedulers) {

    override fun buildUseCaseObservable(param: List<Long>): Completable {
        return gateway.update(param)
    }
}