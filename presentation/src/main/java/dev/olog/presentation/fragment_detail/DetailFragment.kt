package dev.olog.presentation.fragment_detail

import android.content.Context
import android.os.Bundle
import android.support.v4.math.MathUtils
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import dev.olog.presentation.R
import dev.olog.presentation._base.BaseFragment
import dev.olog.presentation.utils.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import javax.inject.Inject

class DetailFragment : BaseFragment() {

    companion object {
        const val TAG = "DetailFragment"
        const val ARGUMENTS_MEDIA_ID = "$TAG.arguments.media_id"

        fun newInstance(mediaId: String): DetailFragment {
            return DetailFragment().withArguments(
                    ARGUMENTS_MEDIA_ID to mediaId
            )
        }
    }

    @Inject lateinit var viewModel: DetailFragmentViewModel
    @Inject lateinit var adapter: DetailAdapter
    @Inject lateinit var recentlyAddedAdapter : DetailRecentlyAddedAdapter

    private val marginDecorator by lazy (LazyThreadSafetyMode.NONE){ HorizontalMarginDecoration(context!!) }

    private lateinit var layoutManager: GridLayoutManager

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        postponeEnterTransition()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // todo if top if dark then
        activity!!.window.removeLightStatusBar()

        viewModel.itemLiveData.subscribe(this, {
            adapter.onItemChanged(it)
            view?.header?.text = it.title
        })

        viewModel.songsLiveData.subscribe(this, {
            adapter.onSongListChanged(it)
            startPostponedEnterTransition()
        })

        viewModel.albumsLiveData.subscribe(this, adapter::onAlbumListChanged)

        viewModel.recentlyAddedLiveData.subscribe(this, {
            recentlyAddedAdapter.updateDataSet(it)
            adapter.onRecentlyAddedChanged(it)
        })
    }

    override fun onViewBound(view: View, savedInstanceState: Bundle?) {
        layoutManager = GridLayoutManager(context!!, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItem(position).type == R.layout.item_detail_album) 1 else 2
            }
        }
        view.list.layoutManager = layoutManager
        view.list.adapter = adapter

        val listObservable = RxRecyclerView.scrollEvents(view.list)
                .share()

        listObservable
                .map { layoutManager.findFirstVisibleItemPosition() >= 1 }
                .distinctUntilChanged()
                .asLiveData()
                .subscribe(this, { lightStatusBar ->
                    val window = activity!!.window
                    view.toolbar.isActivated = lightStatusBar
                    view.back.isActivated = lightStatusBar
                    view.header.isActivated = lightStatusBar
                    if (lightStatusBar){
                        window.setLightStatusBar()
                    } else {
                        window.removeLightStatusBar()
                    }
                })

        listObservable
                .map { it.dy() }
                .asLiveData()
                .subscribe(this, { dy ->
                    val floatDiff = dy.toFloat()
                    if (layoutManager.findFirstVisibleItemPosition() < 1){
                        // change alpha based on scroll
                        val alpha = MathUtils.clamp(view.toolbar.alpha + floatDiff / 400, 0f, 1f)
                        view.toolbar.alpha = alpha
                        view.header.alpha = alpha
                    } else if (view.toolbar.alpha != 1f) {
                        // after the main image is covered only increase the alpha
                        val alpha = MathUtils.clamp(view.toolbar.alpha + Math.abs(floatDiff / 400), 0f, 1f)
                        view.toolbar.alpha = alpha
                        view.header.alpha = alpha
                    }

                })
    }

    override fun onStart() {
        super.onStart()
        view!!.list.addItemDecoration(marginDecorator)
    }

    override fun onStop() {
        super.onStop()
        view!!.list.removeItemDecoration(marginDecorator)
    }

    override fun onDestroyView() {
        activity!!.window.setLightStatusBar()
        super.onDestroyView()
    }

    override fun provideView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_detail, container, false)
}