package dev.olog.presentation.activity_about.di

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import dev.olog.presentation.activity_about.AboutActivity

@Module(subcomponents = arrayOf(AboutActivitySubComponent::class))
abstract class AboutActivityInjector {

    @Binds
    @IntoMap
    @ActivityKey(AboutActivity::class)
    internal abstract fun injectorFactory(builder: AboutActivitySubComponent.Builder)
            : AndroidInjector.Factory<out Activity>

}