package dev.olog.presentation._base.list

import dev.olog.shared.clearThenPut
import dev.olog.shared.swap

class BetterMap <K, Model> constructor(
        enums: Array<K>,
        private val data: MutableMap<K, MutableList<Model>> = mutableMapOf()

) {

    init {
        for (enum in enums) {
            data[enum] = mutableListOf()
        }
    }

    operator fun get(position: Int): Model {
        var totalSize = 0
        for (value in data.values) {
            if (position in totalSize until (totalSize + value.size)){
                val realPosition = position - totalSize
                return value[realPosition]
            } else{
                totalSize += value.size
            }
        }
        throw IllegalArgumentException("invalid position $position")
    }

    fun size() = data.values.sumBy { it.size }

    fun wrappedValue() : MutableMap<K, MutableList<Model>> = data.toMutableMap() // pass a copy

    fun update(data: BetterMap<K, Model>) : BetterMap<K, Model> {
        this.data.clearThenPut(data.wrappedValue())
        return this
    }

    fun swap(from: Int, to: Int){
        val currentList = getListAtPosition(from)
        val realFrom = getPositionInList(from)
        val realTo = getPositionInList(to)
        currentList.swap(realFrom, realTo)
    }

    fun getListAtPosition(position: Int) : MutableList<Model> {
        var total = 0
        for (entry in data.entries) {
            total += entry.value.size
            if (total > position) {
                return entry.value
            }
        }
        throw IllegalArgumentException("invalid position $position, total size is ${size()}")
    }

    private fun getPositionInList(position: Int): Int {
        var totalSize = 0
        for (value in data.values) {
            if (position in totalSize until (totalSize + value.size)){
                return position - totalSize
            } else{
                totalSize += value.size
            }
        }
        throw IllegalArgumentException("invalid position $position")
    }

}