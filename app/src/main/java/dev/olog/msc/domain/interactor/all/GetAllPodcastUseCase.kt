package dev.olog.msc.domain.interactor.all

import dev.olog.msc.domain.entity.Podcast
import dev.olog.msc.domain.executors.ComputationScheduler
import dev.olog.msc.domain.gateway.PodcastGateway
import dev.olog.msc.domain.interactor.base.GetGroupUseCase
import javax.inject.Inject

class GetAllPodcastUseCase @Inject constructor(
        gateway: PodcastGateway,
        schedulers: ComputationScheduler
) : GetGroupUseCase<Podcast>(gateway, schedulers)