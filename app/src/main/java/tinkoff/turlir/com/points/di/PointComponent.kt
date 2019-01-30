package tinkoff.turlir.com.points.di

import dagger.Component
import tinkoff.turlir.com.points.point.PointPresenter

@Component(dependencies = [StorageComponent::class])
@PointScope
interface PointComponent {

    fun pointPresenter(): PointPresenter
}