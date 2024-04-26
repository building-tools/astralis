package de.raphaelgoetz.astralis.ui.builder

import com.google.common.collect.Lists
import de.raphaelgoetz.astralis.items.builder.SmartItem
import de.raphaelgoetz.astralis.items.data.InteractionType
import de.raphaelgoetz.astralis.ui.data.InventoryRows
import de.raphaelgoetz.astralis.ui.data.InventorySlots
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class DisplayInventoryBuilder(
    javaPlugin: JavaPlugin,
    title: Component,
    holder: Player,
    rows: InventoryRows = InventoryRows.ROW6,
    list: List<SmartClick>,
    private val from: InventorySlots,
    private val to: InventorySlots
) : InventoryBuilder(javaPlugin, title, holder, rows) {

    //to should be inclusive. That's why to + 1
    private val maxItems: Int = (to.value + 1) - from.value
    private val pages = Lists.partition(list, maxItems)
    private val maxPage = pages.count()

    private var currentPageIndex = 0

    init {
        applyPage()
    }

    private fun applyPage() {

        val currentPage = pages[currentPageIndex]

        for (index in from.value until (to.value + 1)) {
            if (index >= currentPage.size) break
            val currentItem = currentPage[index]
            setSlot(index, currentItem.item, currentItem.action)
        }
    }

    fun pageLeft(slot: InventorySlots, display: ItemStack) {
        this.setBlockedSlot(slot, SmartItem(display, InteractionType.PAGE_TURN)) {
            if (currentPageIndex - 1 < 0) {
                currentPageIndex = (maxPage - 1)
                applyPage()
                return@setBlockedSlot
            }
            currentPageIndex--
            applyPage()
        }
    }

    fun pageRight(slot: InventorySlots, display: ItemStack) {
        this.setBlockedSlot(slot, SmartItem(display, InteractionType.PAGE_TURN)) {
            if (currentPageIndex + 1 >= maxPage) {
                currentPageIndex = 0
                println(currentPageIndex)
                applyPage()
                return@setBlockedSlot
            }
            currentPageIndex++
            println(currentPageIndex)
            applyPage()
        }
    }
}